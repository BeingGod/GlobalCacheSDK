package com.hw.globalcachesdk.pool;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.extra.ssh.ChannelType;
import com.hw.globalcachesdk.ExecutePrivilege;
import com.hw.globalcachesdk.StatusCode;
import com.hw.globalcachesdk.entity.AbstractEntity;
import com.hw.globalcachesdk.entity.AsyncEntity;
import com.hw.globalcachesdk.exception.*;
import com.hw.globalcachesdk.executor.AbstractCommandExecutor;
import com.hw.globalcachesdk.executor.AbstractCommandExecutorAsync;
import com.hw.globalcachesdk.executor.AbstractCommandExecutorSync;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;
import cn.hutool.extra.ssh.JschUtil;
import com.hw.globalcachesdk.executor.CommandExecuteResult;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;

import static com.hw.globalcachesdk.StatusCode.*;

/**
 * SSH连接池
 * 用于持久化SSH连接和并发SSH请求
 * @author 章睿彬
 */
public class SSHSessionPool {
    /**
     * 当前连接的节点信息
     * 由于存在同一节点不同权限用户，需要使用host和user作为键
     * host, user : session
     */
    private HashMap<Pair<String, String>, Session> hostSessionHashMap = null;

    /**
     * 定长线程池
     */
    private ExecutorService threadPool = null;

    /**
     * 构造函数，具体执行操作：
     * 1. 获取当前运行环境的系统处理器个数，并初始化定长的线程池
     * 2. 初始化HashMap
     */
    public SSHSessionPool() {
        // 获取系统处理器个数，作为线程池数量
        int nThreads = Runtime.getRuntime().availableProcessors();
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().build();

        int corePoolSize = nThreads * 2 + 1;
        /* 集群最大节点数为512 */
        int maxmumPoolSize = Math.max(corePoolSize, 512);
        int bufferCapacity = Math.max(corePoolSize, 512);
        // 当提交的任务数大于corePoolSize时，会优先放到队列缓冲区，只有填满了缓冲区后，才会判断当前运行的任务是否大于maxPoolSize，
        // 小于时会新建线程处理。大于时就触发了拒绝策略，
        // 本池中操作均为IO密集型，设定线程池最大线程数 = 可获取的逻辑处理器个数 * 2 + 1
        threadPool = new ThreadPoolExecutor(corePoolSize, maxmumPoolSize, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(bufferCapacity),
                namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        // 初始化Session表
        hostSessionHashMap = new HashMap<>();
    }

    public boolean isSessionConnected(String host, String user) throws SessionException {
        Session session =  hostSessionHashMap.get(Pair.of(host, user));
        if (session == null) {
            return false;
        } else {
            boolean isConnected = session.isConnected();
            if (!isConnected) {
                releaseSession(host, user);
            }
            return isConnected;
        }
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
     * @throws SessionException 会话已存在或连接已存在抛出此异常
     */
    public void createSession(String host, String user, String password, int port) throws SessionException {
        Session session =  hostSessionHashMap.get(Pair.of(host, user));
        if (session == null) {
            // 当前会话不存在
            try {
                Session newSession = JschUtil.getSession(host,port,user,password);
                hostSessionHashMap.put(Pair.of(host, user), newSession);
            } catch (RuntimeException e) {
                throw new SessionException("connect failed", e);
            }
        } else {
            throw new SessionException("session already exist");
        }
    }

    /**
     * 创建SSH连接, 并将其添加到hostSessionHashMap中，没有任何异常则表示执行成功
     * 如果链接失败需要抛出ConnectFailedException异常
     * 如果链接已存在抛出SessionAlreadyExistException异常
     *
     * @param host 主机IP
     * @param user 用户名
     * @param privateKeyPath 私钥路径
     * @param passphrase 私钥密码
     * @param port 端口号
     * @throws SessionException 会话已存在或连接已存在抛出此异常
     */
    public void createSession(String host, String user, String privateKeyPath, byte[] passphrase, int port) throws SessionException {
        Session session =  hostSessionHashMap.get(Pair.of(host, user));
        if (session == null) {
            // 当前会话不存在
            try {
                Session newSession = JschUtil.getSession(host, port, user, privateKeyPath, passphrase);
                hostSessionHashMap.put(Pair.of(host, user), newSession);
            } catch (RuntimeException e) {
                throw new SessionException("connect failed", e);
            }
        } else {
            throw new SessionException("session already exist");
        }
    }

    /**
     * 释放SSH连接，并将其从hostSessionHashMap中移，没有任何异常则表示执行成功
     *
     * @param host 主机IP
     * @param user 用户名
     * @throws SessionException 会话不存在/会话关闭失败抛出此异常
     */
    public void releaseSession(String host, String user) throws SessionException  {
        Session session =  hostSessionHashMap.get(Pair.of(host, user));
        if (session != null) {
            try {
                JschUtil.close(session);
                // 当数据量大，只使用get时采用破坏遍历结构的方式换取最高地执行效率
                hostSessionHashMap.remove(Pair.of(host, user));
            } catch (RuntimeException e) {
                throw new SessionException("session close failed", e);
            }
        } else {
            throw new SessionException("host not exist");
        }
    }

    /**
     * 判断Session是否存在
     *
     * @param host session的主机IP
     * @param user 用户名称
     * @return 是否存在
     */
    public boolean isSessionExist(String host, String user) {
        Session session =  hostSessionHashMap.get(Pair.of(host, user));
        return session != null;
    }

    /**
     * 根据host获取session给函数调用使用
     *
     * @param host 需要获取连接session的主机IP
     * @param user 用户名称
     * @return JSCH会话对象
     * @throws SessionException 会话不存在抛出此异常
     */
    private Session getSession(String host, String user) throws SessionException {
        Session session =  hostSessionHashMap.get(Pair.of(host, user));
        if (session != null){
            return session;
        } else {
            throw new SessionException("host not exist");
        }
    }

    /**
     * 执行带参命令
     * 根据type的值, 在hosts的所有节点执行命令
     * 注意: hosts和args的个数需要一致
     *
     * @param hosts 需要执行命令的节点IP列表
     * @param users 需要执行命令的用户名列表
     * @param executor 需要执行的方法
     * @param args 命令参数
     * @return 每个节点命令执行结果
     * @throws SSHSessionPoolException 线程池发生内部问题抛出此异常
     */
    public HashMap<String, CommandExecuteResult> execute(ArrayList<String> hosts, ArrayList<String> users, AbstractCommandExecutor executor, ArrayList<String> args) throws SSHSessionPoolException {
        if (0 == hosts.size()) {
            throw new SSHSessionPoolException("node-ip table is empty");
        }

        if (executor.getDes().isWithArgs() && hosts.size() != args.size()) {
            throw new SSHSessionPoolException("node size is not equal to args size");
        }

        try {
            return executeInternal(hosts, users, executor, args);
        } catch (InterruptedException e) {
            throw new SSHSessionPoolException("thread interrupted", e);
        }
    }

    /**
     * 执行带参命令
     *
     * @param host 需要执行命令的节点IP
     * @param user 需要执行命令的用户名
     * @param executor 需要执行的方法
     * @param arg 命令参数
     * @return 每个节点命令执行结果
     * @throws SSHSessionPoolException 线程池发生内部问题抛出此异常
     */
    public HashMap<String, CommandExecuteResult> execute(String host, String user, AbstractCommandExecutor executor, String arg) throws SSHSessionPoolException {
        try {
            ArrayList<String> hosts = new ArrayList<>(1);
            ArrayList<String> users = new ArrayList<>(1);
            ArrayList<String> args = new ArrayList<>(1);

            hosts.add(host);
            users.add(user);
            args.add(arg);

            return executeInternal(hosts, users, executor, args);
        } catch (InterruptedException e) {
            throw new SSHSessionPoolException("thread interrupted", e);
        }
    }

    /**
     * 执行无参命令
     * 根据type的值, 在hosts的所有节点执行命令
     *
     * @param hosts 需要执行命令的节点IP列表
     * @param users 需要执行命令的用户名列表
     * @param executor 需要执行的方法
     * @return 每个节点命令执行结果
     * @throws SSHSessionPoolException 线程池发生内部问题抛出此异常
     */
    public HashMap<String, CommandExecuteResult> execute(ArrayList<String> hosts, ArrayList<String> users, AbstractCommandExecutor executor) throws SSHSessionPoolException {
        if (0 == hosts.size()) {
            throw new SSHSessionPoolException("node-ip table is empty");
        }

        if (users.size() != hosts.size()) {
            throw new SSHSessionPoolException("node size is not equal to user size");
        }

        try {
            return executeInternal(hosts, users, executor, null);
        } catch (InterruptedException e) {
            throw new SSHSessionPoolException("thread interrupted", e);
        }
    }

    /**
     * 执行无参命令
     * 根据type的值, 在host的节点执行命令
     *
     * @param host 需要执行命令的节点IP
     * @param user 需要执行命令的用户名
     * @param executor 需要执行的方法
     * @return 每个节点命令执行结果
     * @throws SSHSessionPoolException 线程池发生内部问题抛出此异常
     */
    public HashMap<String, CommandExecuteResult> execute(String host, String user, AbstractCommandExecutor executor) throws SSHSessionPoolException {
        try {
            ArrayList<String> hosts = new ArrayList<>(1);
            ArrayList<String> users = new ArrayList<>(1);

            hosts.add(host);
            users.add(user);

            return executeInternal(hosts, users, executor, null);
        } catch (InterruptedException e) {
            throw new SSHSessionPoolException("thread interrupted", e);
        }
    }

    /**
     * 利用连接池和线程池获取各节点的信息存到hashmap中
     *
     * @param hosts  需要执行命令的主机列表
     * @param users 需要执行命令的用户名列表
     * @param executor 需要执行的方法
     * @param args 命令参数
     * @return 每个结点的运行结果
     * @throws InterruptedException 线程意外中断抛出此异常
     */
    public HashMap<String, CommandExecuteResult> executeInternal(ArrayList<String> hosts, ArrayList<String> users, AbstractCommandExecutor executor, ArrayList<String> args) throws InterruptedException, SSHSessionPoolException {
        // 初始化一个哈希表存放运行结果
        HashMap<String, CommandExecuteResult> commandExecuteResultHashMap = new HashMap<>(hosts.size());
        // 初始化一个哈希表存放中间返回值
        HashMap<String, String> returnValueHashMap = new HashMap<>(hosts.size());
        //设置信号量，节点数
        final CountDownLatch countDownLatch = new CountDownLatch(hosts.size());
        for (int i = 0; i < hosts.size(); i++) {
            int finalI = i;
            threadPool.execute(() -> {
                // 向线程池中添加任务
                CommandExecuteResult commandExecuteResult = new CommandExecuteResult();
                try {
                    // 每个线程一个Session
                    Session session = getSession(hosts.get(finalI), users.get(finalI));

                    if (!checkPrivilege(session, executor.getDes().getExecutePrivilege())) {
                        commandExecuteResult.setStatusCode(PERMISSION_DENIED);
                    } else {
                        if (!executor.getDes().isAsync()) {
                            // 同步命令
                            AbstractCommandExecutorSync syncExecutor = (AbstractCommandExecutorSync) executor;
                            // 执行命令
                            String returnValue = syncExecutor.exec(session, executor.getDes().isWithArgs() ? args.get(finalI) : "");
                            // 存入中间结果
                            setReturnValueResult(returnValueHashMap, "" + hosts.get(finalI), returnValue);
                            // 命令执行成功，设置状态码
                            commandExecuteResult.setStatusCode(SUCCESS);
                        } else {
                            // 异步命令
                            AbstractCommandExecutorAsync asyncExecutor = (AbstractCommandExecutorAsync)executor;
                            Channel channel = JschUtil.openChannel(session, ChannelType.SHELL);
                            // 执行命令 -> 得到Shell结果输出流
                            InputStream stream = asyncExecutor.exec(channel, executor.getDes().isWithArgs() ? args.get(finalI) : "");
                            // 命令执行成功，设置状态码
                            commandExecuteResult.setStatusCode(SUCCESS);
                            commandExecuteResult.setData(new AsyncEntity(stream, channel));
                        }
                    }
                } catch (SessionException e) {
                    // 连接不存在，设置状态码
                    commandExecuteResult.setStatusCode(SESSION_NOT_EXIST);
                } catch (CommandExecException e) {
                    // 命令执行失败，设置状态码
                    commandExecuteResult.setStatusCode(EXEC_COMMAND_FAILED);
                } finally {
                    // 向哈希表中添加当前节点请求状态码
                    setCommandExecuteResult(commandExecuteResultHashMap,"" + hosts.get(finalI), commandExecuteResult);
                    //信号量减一
                    countDownLatch.countDown();
                }
            });
        }

        if (!countDownLatch.await(executor.getDes().getTimeout(), TimeUnit.SECONDS)) {
            // 超时
            for (String host : hosts) {
                if (null == commandExecuteResultHashMap.get(host)) {
                    CommandExecuteResult commandExecuteResult = new CommandExecuteResult();
                    commandExecuteResult.setStatusCode(EXEC_COMMAND_TIMEOUT);
                    commandExecuteResultHashMap.put("" + host, commandExecuteResult);
                }
            }
        }

        if (!executor.getDes().isAsync()) {
            // 对于同步命令需要解析其结果
            AbstractCommandExecutorSync syncExecutor = (AbstractCommandExecutorSync) executor;
            for (String host : hosts) {
                // 解析中间结果
                if (commandExecuteResultHashMap.get(host).getStatusCode() == SUCCESS) {
                    // 如果状态码为SUCESS，说明命令执行成功，解析中间结果
                    try {
                        AbstractEntity entity = syncExecutor.parseOf(returnValueHashMap.get(host));
                        commandExecuteResultHashMap.get(host).setData(entity);
                    } catch (ReturnValueParseException e) {
                        // 解析失败，更新状态码
                        commandExecuteResultHashMap.get(host).setStatusCode(RETURN_VALUE_PARSE_FAILED);
                    }
                }
            }
        }

        return commandExecuteResultHashMap;
    }

    /**
     * 向Map中存入命令执行结果
     *
     * @param commandExecuteResultHashMap 要存入的Map
     * @param host 节点IP
     * @param commandExecuteResult 结果对象
     */
    private synchronized void setCommandExecuteResult(HashMap<String, CommandExecuteResult> commandExecuteResultHashMap ,String host, CommandExecuteResult commandExecuteResult) {
        commandExecuteResultHashMap.put(host, commandExecuteResult);
    }

    /**
     * 向Map中存入结果
     *
     * @param returnValueHashMap 要存入的Map
     * @param host 节点IP
     * @param returnValue 返回值字符串
     */
    private synchronized void setReturnValueResult(HashMap<String, String> returnValueHashMap ,String host, String returnValue) {
        returnValueHashMap.put(host, returnValue);
    }

    private boolean checkPrivilege(Session sshSession, ExecutePrivilege expectPrivilege) {
        String command = "whoami";

        String returnValue = JschUtil.exec(sshSession, command, Charset.defaultCharset()).trim();

        final String root = "root";
        final String globalcacheop = "globalcacheop";

        if (expectPrivilege == ExecutePrivilege.ROOT) {
            // 校验权限
            if (!root.equals(returnValue)) {
                return false;
            }
        }

        if (expectPrivilege == ExecutePrivilege.GLOBAL_CACHE_OP) {
            // 校验权限
            if (!globalcacheop.equals(returnValue) && !root.equals(returnValue)) {
                return false;
            }
        }

        return true;
    }
}
