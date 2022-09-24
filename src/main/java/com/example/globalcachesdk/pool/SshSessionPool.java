package com.example.globalcachesdk.pool;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.extra.ssh.JschRuntimeException;
import com.example.globalcachesdk.entity.AbstractEntity;
import com.example.globalcachesdk.entity.MemInfo;
import com.example.globalcachesdk.entity.CpuInfo;
import com.example.globalcachesdk.exception.*;
import com.example.globalcachesdk.excutor.CommandExecutor;
import com.jcraft.jsch.Session;
import cn.hutool.extra.ssh.JschUtil;
import com.example.globalcachesdk.SupportedCommand;
import com.example.globalcachesdk.excutor.CommandExecuteResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.example.globalcachesdk.StatusCode.*;

/**
 * SSH连接池，使用单例模式进行实现，用于持久化SSH连接和并发SSH请求
 * @author 章睿彬
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
     * 构造函数，具体执行操作：
     * 1. 获取当前运行环境的系统处理器个数，并初始化定长的线程池
     * 2. 初始化HashMap
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
     *
     * @return SSH会话池实例
     */
    public static synchronized SshSessionPool getInstance() {
        if (instance == null) {
            // 使用懒加载的形式进行初始化
            instance = new SshSessionPool();
        }
        return instance;
    }

    /**
     * 创建SSH连接, 并将其添加到hostSessionHashMap中，没有任何异常则表示执行成功
     * 如果链接失败需要抛出ConnectFailedException异常
     * 如果链接已存在抛出SessionAlreadyExistException异常
     *
     * @param host 主机IP
     * @param user 用户名
     * @param password 密码
     * @param port 端口号
     * @throws SessionAlreadyExistException 会话已存在抛出此异常
     * @throws ConnectFailedException SSH连接失败抛出此异常
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
     * 释放SSH连接，并将其从hostSessionHashMap中移，没有任何异常则表示执行成功
     *
     * @param host 主机IP
     * @throws SessionNotExistException 会话不存在抛出此异常
     * @throws SessionCloseFailedException 会话关闭失败抛出此异常
     */
    public void releaseSession(String host) throws SessionNotExistException,SessionCloseFailedException  {
        Session session =  hostSessionHashMap.get(host);
        if (session != null) {
            try {
                JschUtil.close(session);
                // 当数据量大，只使用get时采用破坏遍历结构的方式换取最高的执行效率
                hostSessionHashMap.remove(host);
            } catch (JschRuntimeException | IORuntimeException e) {
                throw new SessionCloseFailedException("链接关闭失败");
            }
        } else {
            throw new SessionNotExistException("主机不存在");
        }
    }

    /**
     * 根据host获取session给函数调用使用
     *
     * @param host 需要获取连接session的主机IP
     * @return JSCH会话对象
     * @throws SessionNotExistException 会话不存在抛出此异常
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
     *
     * @param hosts 需要执行命令的主机IP列表
     * @param type 命令类型
     * @return 每个节点命令执行结果
     */
    public HashMap<String, CommandExecuteResult> execute(ArrayList<String> hosts, SupportedCommand type) {
        // 需要执行的命令函数名
        String methodName = null;
        // 命令函数返回值
        Object returnValue = null;
        switch (type) {
            // 根据不同的类型，使用反射机制获取需要执行的函数
            case GET_MEM_INFO:
                returnValue = new MemInfo();
                methodName = "queryMemInfo";
                break;
            case GET_CPU_INFO:
                returnValue = new CpuInfo();
                methodName = "queryCpuInfo";
                break;

            default:
                break;
        }

        // 需要执行的函数对象
        Method method = null;
        try {
            // 使用反射获取需要执行的函数对象
            method = CommandExecutor.class.getMethod(methodName, Session.class);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return executeWrapper(hosts, method, returnValue);
    }

    /**
     * 利用连接池和线程池获取各节点的信息存到hashmap中
     *
     * @param hosts  需要执行命令的主机列表
     * @param method 需要执行的方法（不带参）
     * @return 每个结点的运行结果
     */
    public synchronized HashMap<String, CommandExecuteResult> executeWrapper(ArrayList<String> hosts, Method method, Object returnValue) {
        // 初始化一个哈希表存放运行结果
        HashMap<String, CommandExecuteResult> commandExecuteResultHashMap = new HashMap<>(hosts.size());
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
                    commandExecuteResult.setData((AbstractEntity) data);
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
}
