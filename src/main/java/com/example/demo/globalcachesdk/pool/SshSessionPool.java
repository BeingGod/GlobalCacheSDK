package com.example.demo.globalcachesdk.pool;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.extra.ssh.JschRuntimeException;
import com.example.demo.globalcachesdk.entity.MemInfo;
import com.example.demo.globalcachesdk.exception.ConnectFailedException;
import com.example.demo.globalcachesdk.exception.SessionAlreadyExistException;
import com.example.demo.globalcachesdk.exception.SessionNotExistException;
import com.example.demo.globalcachesdk.excutor.CommandExcutor;
import com.jcraft.jsch.Session;
import cn.hutool.extra.ssh.JschUtil;
import com.example.demo.globalcachesdk.SupportedCommand;
import com.example.demo.globalcachesdk.excutor.CommandExecuteResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.example.demo.globalcachesdk.StatusCode.*;

/**
 * SSH连接池
 * 用于持久化SSH连接
 * @author ya059
 */
public class SshSessionPool {

    /**
     * 类实例
     */
    private static SshSessionPool instance = null;

    /**
     * 当前连接的节点信息
     */
    private static HashMap<String, Session> hostSessionHashMap = null;

    /**
     * 定长线程池
     */
    private static ExecutorService threadPool = null;

    /**
     * 构造函数
     */
    private SshSessionPool() {
        // 获取系统处理器个数，作为线程池数量
        int nThreads = Runtime.getRuntime().availableProcessors();
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().build();

        // 当提交的任务数大于corePoolSize时，会优先放到队列缓冲区，只有填满了缓冲区后，才会判断当前运行的任务是否大于maxPoolSize，
        // 小于时会新建线程处理。大于时就触发了拒绝策略，
        // 本池中操作均为IO密集型，设定线程池最大线程数 = 可获取的逻辑处理器个数 * 2 + 1
        // TODO 对于maximumPoolSize大小和capacity大小的选择需要进行优化
        threadPool = new ThreadPoolExecutor(nThreads * 2 + 1, 100, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1024),
                namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        // 初始化Session表
        hostSessionHashMap = new HashMap<>();
    }

    /**
     * 使用懒加载形式进行类初始化
     * @return ssh会话池实例
     */
    public static synchronized SshSessionPool getInstance() {
        if (instance == null) {
            // 使用懒加载的形式进行初始化
            instance = new SshSessionPool();
        }
        return instance;
    }

    /**
     * 创建SSH连接, 并将其添加到hostSessionHashMap中
     * 没有任何异常则表示执行成功
     * 如果链接失败需要抛出ConnectFailedException异常
     * 如果链接已存在抛出SessionAlreadyExistException异常
     * @param host 主机IP
     * @param user 用户名
     * @param password 密码
     * @param port 端口号
     */
    public void createSession(String host, String user, String password, int port) throws SessionAlreadyExistException, ConnectFailedException {
        Session session =  hostSessionHashMap.get(host);
        if (session == null) {
            // 当前会话不存在
            try {
                Session newSession = JschUtil.getSession(host,port,user,password);
                hostSessionHashMap.put(host, newSession);
            } catch (JschRuntimeException | IORuntimeException e) {
                throw new ConnectFailedException("连接失败",e);
            }
        } else {
            throw new SessionAlreadyExistException("当前链接已存在");
        }
    }

    /**
     * 释放SSH连接，并将其从hostSessionHashMap中移除
     * 没有任何异常则表示执行成功
     * 如果链接不存在需要抛出SessionNotExistException异常
     * @param host 主机IP
     */
    public void releaseSession(String host) throws SessionNotExistException, SessionAlreadyExistException {
        Session session =  hostSessionHashMap.get(host);
        if (session != null) {
            try {
                JschUtil.close(session);
                // 当数据量大，只使用get时采用破坏遍历结构的方式换取最高的执行效率
                hostSessionHashMap.remove(host);
            }
            catch (JschRuntimeException | IORuntimeException e) {
                throw new SessionAlreadyExistException("链接关闭失败");
            }
        } else {
            throw new SessionNotExistException("主机不存在");
        }
    }

    /**
     * 根据host获取session给函数调用使用
     * @param host 需要获取连接session的主机IP
     * @return session
     * @throws SessionNotExistException 会话不存在异常
     */
    private Session getSession(String host) throws SessionNotExistException {
        Session session =  hostSessionHashMap.get(host);
        if (session != null){
            return session;
        } else {
            throw new SessionNotExistException("主机不存在");
        }
    }

    /**
     * 执行无参命令
     * 根据type的值, 在hosts的所有节点执行命令
     * @param hosts 需要执行命令的主机IP列表
     * @param type 命令类型
     * @return 每个节点命令执行结果
     * {
     *     host: CommandExecuteResult,
     *     ......
     * }
     */
    public HashMap<String, CommandExecuteResult> execute(ArrayList<String> hosts, SupportedCommand type) {
        // 需要执行的命令函数
        Method method = null;
        Object returnValue = null;
        try {
            switch (type) {
                // 根据不同的类型，使用反射机制获取需要执行的函数
                case GET_MEM_INFO:
                    returnValue = new MemInfo();
                    method = CommandExcutor.class.getMethod("queryMemInfo", Session.class);
                    break;
                case GET_CPU_INFO:
                    break;

                default:
                    throw new UnsupportedOperationException("不支持的命令");
            }
        } catch (NoSuchMethodException e) {
            throw new UnsupportedOperationException("不支持的命令", e);
        }

        return executeWrapper(hosts, method, returnValue);
    }

    /**
     * 执行带参命令
     * 根据type的值, 在hosts的所有节点执行命令
     * @param hosts 需要执行命令的主机IP列表
     * @param type 命令类型
     * @param args 参数
     * @return 每个节点命令执行结果
     * {
     *    host: CommandExecuteResult,
     *    ......
     * }
     */
    public HashMap<String, CommandExecuteResult> execute(ArrayList<String> hosts, SupportedCommand type, ArrayList<String> args) {
        // 将参数列表解析为字符串
        StringBuilder argsStringBuilder = new StringBuilder();
        for (String arg : args) {
            argsStringBuilder.append(" " + arg);
        }

        Method method = null;

        return executeWrapper(hosts, method, argsStringBuilder.toString());
    }

    /**
     * 利用连接池和线程池获取各节点的信息存到hashmap中
     * @param hosts  需要执行命令的主机列表
     * @param method 需要执行的方法（不带参）
     * @return 每个结点的运行结果
     */
    public synchronized HashMap<String, CommandExecuteResult> executeWrapper(ArrayList<String> hosts, Method method, Object returnValue) {
        // 初始化一个哈希表存放运行结果
        HashMap<String, CommandExecuteResult> commandExecuteResultHashMap = new HashMap<>();
        // 一个对象锁，保护共有资源防止输出时结果数量异常
        Lock lock = new ReentrantLock();
        for(int i = 0; i < hosts.size(); i++) {
            int finalI = i;
            threadPool.execute(() -> {
                // 向线程池中添加任务
                CommandExecuteResult commandExecuteResult =new CommandExecuteResult();
                try {
                    // 每个线程一个Session
                    Session session = getSession(hosts.get(finalI));
                    // 设置参数
                    Object[] parameters = new Object[1];
                    parameters[0] = session;
                    // 执行命令
                    Object data = method.invoke(returnValue, parameters);
                    commandExecuteResult.setStatusCode(SUCCESS);
                    commandExecuteResult.setData(data);
                } catch (SessionNotExistException e) {
                    // 连接不存在，设置状态码
                    commandExecuteResult.setStatusCode(SESSION_NOT_EXIST);
                } catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {
                    // 命令执行失败，设置状态码
                    commandExecuteResult.setStatusCode(EXEC_COMMAND_FAILED);
                } finally {
                    // 加锁保护结果生成过程
                    lock.lock();
                    // 向总哈希表中添加当前节点运行结果
                    commandExecuteResultHashMap.put(""+hosts.get(finalI), commandExecuteResult);
                    // 解锁
                    lock.unlock();
                }
            });
        }
        // TODO: 优化忙等待的方式
        while (commandExecuteResultHashMap.size() < hosts.size()){
            //主线程死锁等待子线程全部结束，添加一行输出空格防止死锁被优化
            System.out.print("");
        }

        return commandExecuteResultHashMap;
    }

//    /**
//     * 利用连接池和线程池获取各节点的信息存到hashmap中
//     * @param hosts  需要执行命令的主机列表
//     * @param method 需要执行的方法（带参）
//     * @param args 方法需要参数
//     * @return 每个结点的运行结果
//     */
//    public synchronized HashMap<String, CommandExecuteResult> executeWrapper(ArrayList<String> hosts, Method method, String args) {
//        // 初始化一个哈希表存放运行结果
//        HashMap<String, CommandExecuteResult> commandExecuteResultHashMap = new HashMap<>();
//        // 一个对象锁，保护共有资源防止输出时结果数量异常
//        Lock lock = new ReentrantLock();
//        for(int i = 0; i < hosts.size(); i++) {
//            int finalI = i;
//            // 向线程池中添加任务
//            threadPool.execute(() -> {
//                CommandExecuteResult commandExecuteResult =new CommandExecuteResult();
//                try {
//                    // 每个线程一个Session
//                    Session session = getSession(hosts.get(finalI));
//                    // 执行命令, 将脚本需要参数以字符串方式传入
//                    Object data = method.invoke(session, args);
//                    commandExecuteResult.setStatusCode(SUCCESS);
//                    commandExecuteResult.setData(data);
//                } catch (SessionNotExistException e) {
//                    // 连接不存在，设置状态码
//                    commandExecuteResult.setStatusCode(SESSION_NOT_EXIST);
//                } catch (InvocationTargetException | IllegalAccessException e) {
//                    // 命令执行失败，设置状态码
//                    commandExecuteResult.setStatusCode(EXEC_COMMAND_FAILED);
//                } finally {
//                    // 加锁保护结果生成过程
//                    lock.lock();
//                    // 向总哈希表中添加当前节点运行结果
//                    commandExecuteResultHashMap.put(""+hosts.get(finalI), commandExecuteResult);
//                    // 解锁
//                    lock.unlock();
//                }
//            });
//        }
//        // TODO: 优化忙等待的方式
//        while (commandExecuteResultHashMap.size() < hosts.size()){
//            //主线程死锁等待子线程全部结束，添加一行输出空格防止死锁被优化
//            System.out.print("");
//        }
//
//        return commandExecuteResultHashMap;
//    }

}
