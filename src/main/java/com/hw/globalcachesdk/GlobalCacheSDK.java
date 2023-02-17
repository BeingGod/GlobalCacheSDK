package com.hw.globalcachesdk;

import com.hw.globalcachesdk.exception.CommandExecutorFactoryException;
import com.hw.globalcachesdk.exception.GlobalCacheSDKException;
import com.hw.globalcachesdk.exception.SessionException;
import com.hw.globalcachesdk.exception.SSHSessionPoolException;
import com.hw.globalcachesdk.executor.AbstractCommandExecutor;
import com.hw.globalcachesdk.executor.CommandExecuteResult;
import com.hw.globalcachesdk.executor.CommandExecutorDescription;
import com.hw.globalcachesdk.executor.CommandExecutorFactory;
import com.hw.globalcachesdk.pool.SSHSessionPool;
import com.hw.globalcachesdk.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Global Cache SDK
 * @author 章睿彬
 */
public class GlobalCacheSDK {

    private static GlobalCacheSDK instance = null;

    private SSHSessionPool sshSessionPool = null;

    private CommandExecutorFactory commandExecutorFactory = null;

    /* ===============================================================会话管理接口===============================================================*/

    /**
     * 创建一个SSH会话，若未抛出异常则表示执行成功
     * 在调用功能接口之前，需要调用该接口创建SSH会话
     * 创建SSH会话需要建立SSH连接，该过程较慢，建议在初始化的功能中一次性建立所有连接
     *
     * @param host     需要创建会话的主机IP
     * @param user     需要创建会话的用户名
     * @param password 需要创建会话的主机密码
     * @param port     需要创建会话的主机端口
     * @throws GlobalCacheSDKException 会话创建失败抛出此异常
     * @deprecated 注意: 该接口传入密码不安全，建议只在测试接口时调用
     */
    public static void createSession(String host, String user, String password, int port) throws GlobalCacheSDKException {
        try {
            getInstance().sshSessionPool.createSession(host, user, password, port);
        } catch (SessionException e) {
            throw new GlobalCacheSDKException("会话异常", e);
        }
    }

    /**
     * 创建一个SSH会话，若未抛出异常则表示执行成功
     * 在调用功能接口之前，需要调用该接口创建SSH会话
     * 创建SSH会话需要建立SSH连接，该过程较慢，建议在初始化的功能中一次性建立所有连接
     *
     * @param host     需要创建会话的主机IP
     * @param user     需要创建会话的用户名
     * @param privateKeyPath 私钥路径
     * @param passphrase 私钥密码
     * @param port     需要创建会话的主机端口
     * @throws GlobalCacheSDKException 会话创建失败抛出此异常
     */
    public static void createSession(String host, String user, String privateKeyPath, byte[] passphrase, int port) throws GlobalCacheSDKException {
        try {
            getInstance().sshSessionPool.createSession(host, user, privateKeyPath, passphrase, port);
        } catch (SessionException e) {
            throw new GlobalCacheSDKException("会话异常", e);
        }
    }

    /**
     * 判断Session是否存在
     *
     * @param host session的主机IP
     * @param user 用户名称
     * @return 是否存在
     * @throws GlobalCacheSDKException GlobalCacheSDK对象为空抛出此异常
     */
    public static boolean isSessionExist(String host, String user) throws GlobalCacheSDKException {
        return getInstance().sshSessionPool.isSessionExist(host, user);
    }

    /**
     * 移除一个SSH会话，若未抛出异常则表示执行成功
     * 当已不需要一个会话的时候，可以调用该接口移除SSH会话
     *
     * @param host 需要移除会话的主机IP
     * @param user 需要移除会话的用户名
     * @throws GlobalCacheSDKException 会话关闭失败/会话不存在抛出此异常
     */
    public static void releaseSession(String host, String user) throws GlobalCacheSDKException {
        try {
            getInstance().sshSessionPool.releaseSession(host, user);
        } catch (SessionException e) {
            throw new GlobalCacheSDKException("会话异常", e);
        }
    }

    /* ===============================================================信息获取接口===============================================================*/

    /**
     * 获取节点的内存信息
     *
     * @param hosts 需要获取主机IP列表
     * @return 每个节点内存信息查询结果
     * @throws GlobalCacheSDKException 执行失败抛出此异常
     * @see com.hw.globalcachesdk.entity.MemInfo
     */
    public static HashMap<String, CommandExecuteResult> queryMemInfo(ArrayList<String> hosts) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(SupportedCommand.QUERY_MEM_INFO);
        try {
            ArrayList<String> users = new ArrayList<>(hosts.size());
            String user = Utils.enumExecutePrivilegeName(executor.getDes().getExecutePrivilege());
            for (String host : hosts) {
                users.add(user);
            }
            return getInstance().sshSessionPool.execute(hosts, users, executor);
        } catch (SSHSessionPoolException e) {
            throw new GlobalCacheSDKException("SSH会话池异常", e);
        }
    }

    /**
     * 获取节点的CPU信息
     *
     * @param hosts 需要获取主机IP列表
     * @return 每个节点CPU信息查询结果
     * @throws GlobalCacheSDKException 执行失败抛出此异常
     * @see com.hw.globalcachesdk.entity.CpuInfo
     */
    public static HashMap<String, CommandExecuteResult> queryCpuInfo(ArrayList<String> hosts) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(SupportedCommand.QUERY_CPU_INFO);
        try {
            ArrayList<String> users = new ArrayList<>(hosts.size());
            String user = Utils.enumExecutePrivilegeName(executor.getDes().getExecutePrivilege());
            for (String host : hosts) {
                users.add(user);
            }
            return getInstance().sshSessionPool.execute(hosts, users, executor);
        } catch (SSHSessionPoolException e) {
            throw new GlobalCacheSDKException("SSH会话池异常", e);
        }
    }

    /**
     * 获取GlobalCache缓存盘信息
     *
     * @param host Ceph1节点IP
     * @return CLI查询结果
     * @throws GlobalCacheSDKException 执行失败抛出此异常
     * @see com.hw.globalcachesdk.entity.CacheDiskInfo
     */
    public static HashMap<String, CommandExecuteResult> queryCacheDiskInfo(String host) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(SupportedCommand.QUERY_CACHE_DISK_INFO);
        try {
            String user = Utils.enumExecutePrivilegeName(executor.getDes().getExecutePrivilege());
            return getInstance().sshSessionPool.execute(host, user, executor);
        } catch (SSHSessionPoolException e) {
            throw new GlobalCacheSDKException("SSH会话池异常", e);
        }
    }

    /**
     * 获取集群异常状态信息
     *
     * @param host Ceph1节点IP
     * @return 集群异常状态信息查询结果
     * @throws GlobalCacheSDKException 执行失败抛出此异常
     * @see com.hw.globalcachesdk.entity.ClusterAlarmInfo
     */
    public static HashMap<String, CommandExecuteResult> queryClusterAlarmInfo(String host) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(SupportedCommand.QUERY_CLUSTER_AlARM_INFO);
        try {
            String user = Utils.enumExecutePrivilegeName(executor.getDes().getExecutePrivilege());
            return getInstance().sshSessionPool.execute(host, user, executor);
        } catch (SSHSessionPoolException e) {
            throw new GlobalCacheSDKException("SSH会话池异常", e);
        }
    }

    /**
     * 获取节点的磁盘信息
     *
     * @param host 需要获取主机IP
     * @return 节点磁盘信息查询结果
     * @throws GlobalCacheSDKException 执行失败抛出此异常
     * @see com.hw.globalcachesdk.entity.DiskInfo
     */
    public static HashMap<String, CommandExecuteResult> queryDiskInfo(String host) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(SupportedCommand.QUERY_DISK_INFO);
        try {
            String user = Utils.enumExecutePrivilegeName(executor.getDes().getExecutePrivilege());
            return getInstance().sshSessionPool.execute(host, user, executor);
        } catch (SSHSessionPoolException e) {
            throw new GlobalCacheSDKException("SSH会话池异常", e);
        }
    }

    /**
     * 获取节点的运行时间
     *
     * @param hosts 需要获取主机IP列表
     * @return 每个节点运行时间查询结果
     * @throws GlobalCacheSDKException 执行失败抛出此异常
     * @see com.hw.globalcachesdk.entity.UptimeInfo
     */
    public static HashMap<String, CommandExecuteResult> queryUptime(ArrayList<String> hosts) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(SupportedCommand.QUERY_UPTIME_INFO);
        try {
            ArrayList<String> users = new ArrayList<>(hosts.size());
            String user = Utils.enumExecutePrivilegeName(executor.getDes().getExecutePrivilege());
            for (String host : hosts) {
                users.add(user);
            }
            return getInstance().sshSessionPool.execute(hosts, users, executor);
        } catch (SSHSessionPoolException e) {
            throw new GlobalCacheSDKException("SSH会话池异常", e);
        }
    }
    
    /**
     * 获取集群状态
     *
     * @param host Ceph1节点IP
     * @return 集群状态查询结果
     * @throws GlobalCacheSDKException 执行失败抛出此异常
     * @see com.hw.globalcachesdk.entity.ClusterStatusInfo
     */
    public static HashMap<String, CommandExecuteResult> queryClusterStatusInfo(String host) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(SupportedCommand.QUERY_CLUSTER_STATUS_INFO);
        try {
            String user = Utils.enumExecutePrivilegeName(executor.getDes().getExecutePrivilege());
            return getInstance().sshSessionPool.execute(host, user, executor);
        } catch (SSHSessionPoolException e) {
            throw new GlobalCacheSDKException("SSH会话池异常", e);
        }
    }

    /**
     * 获取节点状态
     *
     * @param host Ceph1节点IP
     * @return 节点状态查询结果
     * @throws GlobalCacheSDKException 执行失败抛出此异常
     * @see com.hw.globalcachesdk.entity.NodeStatusInfo
     */
    public static HashMap<String, CommandExecuteResult> queryNodeStatusInfo(String host) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(SupportedCommand.QUERY_NODE_STATUS_INFO);
        try {
            String user = Utils.enumExecutePrivilegeName(executor.getDes().getExecutePrivilege());
            return getInstance().sshSessionPool.execute(host, user, executor);
        } catch (SSHSessionPoolException e) {
            throw new GlobalCacheSDKException("SSH会话池异常", e);
        }
    }

    /**
     * 获取节点的数据盘分区信息
     *
     * @param hosts 需要获取主机IP列表
     * @return 每个节点据盘分区信息查询结果
     * @throws GlobalCacheSDKException 执行失败抛出此异常
     * @see com.hw.globalcachesdk.entity.DataDiskPartInfo
     */
    public static HashMap<String, CommandExecuteResult> queryDataDiskPartInfo(ArrayList<String> hosts) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(SupportedCommand.QUERY_DATA_DISK_PART_INFO);
        try {
            ArrayList<String> users = new ArrayList<>(hosts.size());
            String user = Utils.enumExecutePrivilegeName(executor.getDes().getExecutePrivilege());
            for (String host : hosts) {
                users.add(user);
            }
            return getInstance().sshSessionPool.execute(hosts, users, executor);
        } catch (SSHSessionPoolException e) {
            throw new GlobalCacheSDKException("SSH会话池异常", e);
        }
    }

    /**
     * 获取节点的磁盘IO信息
     *
     * @param hosts 需要获取主机IP列表
     * @return 每个节点磁盘分区信息查询结果
     * @throws GlobalCacheSDKException 执行失败抛出此异常
     * @see com.hw.globalcachesdk.entity.DiskIoInfo
     */
    public static HashMap<String, CommandExecuteResult> queryDiskIoInfo(ArrayList<String> hosts) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(SupportedCommand.QUERY_DISK_IO_INFO);
        try {
            ArrayList<String> users = new ArrayList<>(hosts.size());
            String user = Utils.enumExecutePrivilegeName(executor.getDes().getExecutePrivilege());
            for (String host : hosts) {
                users.add(user);
            }
            return getInstance().sshSessionPool.execute(hosts, users, executor);
        } catch (SSHSessionPoolException e) {
            throw new GlobalCacheSDKException("SSH会话池异常", e);
        }
    }

    /**
     * 获取静态网络信息
     *
     * @param host 需查询节点IP
     * @return 静态网络信息查询结果
     * @throws GlobalCacheSDKException 执行失败抛出此异常
     * @see com.hw.globalcachesdk.entity.StaticNetInfo
     */
    public static HashMap<String, CommandExecuteResult> queryStaticNetInfo(String host) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(SupportedCommand.QUERY_STATIC_NET_INFO);
        try {
            String user = Utils.enumExecutePrivilegeName(executor.getDes().getExecutePrivilege());
            return getInstance().sshSessionPool.execute(host, user, executor);
        } catch (SSHSessionPoolException e) {
            throw new GlobalCacheSDKException("SSH会话池异常", e);
        }
    }

    /**
     * 获取节点PT信息
     *
     * @param host Ceph1节点IP
     * @param nodeId 节点ID
     * @return 节点PT信息查询结果
     * @throws GlobalCacheSDKException 执行失败抛出此异常
     * @see com.hw.globalcachesdk.entity.PtInfo
     */
    public static HashMap<String, CommandExecuteResult> queryNodePtInfo(String host, int nodeId) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(SupportedCommand.QUERY_PT_INFO);
        try {
            String user = Utils.enumExecutePrivilegeName(executor.getDes().getExecutePrivilege());
            String args = "node " + nodeId;
            return getInstance().sshSessionPool.execute(host, user, executor, args);
        } catch (SSHSessionPoolException e) {
            throw new GlobalCacheSDKException("SSH会话池异常", e);
        }
    }

    /**
     * 获取集群PT信息
     *
     * @param host Ceph1节点IP
     * @return 集群PT信息查询结果
     * @throws GlobalCacheSDKException 执行失败抛出此异常
     * @see com.hw.globalcachesdk.entity.PtInfo
     */
    public static HashMap<String, CommandExecuteResult> queryAllPtInfo(String host) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(SupportedCommand.QUERY_PT_INFO);
        try {
            String user = Utils.enumExecutePrivilegeName(executor.getDes().getExecutePrivilege());
            String args = "all";
            return getInstance().sshSessionPool.execute(host, user, executor, args);
        } catch (SSHSessionPoolException e) {
            throw new GlobalCacheSDKException("SSH会话池异常", e);
        }
    }

    /**
     * 获取磁盘PT信息
     *
     * @param host Ceph1节点IP
     * @param diskId 磁盘ID
     * @return 磁盘PT信息查询结果
     * @throws GlobalCacheSDKException 执行失败抛出此异常
     * @see com.hw.globalcachesdk.entity.PtInfo
     */
    public static HashMap<String, CommandExecuteResult> queryDiskPtInfo(String host, Integer diskId) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(SupportedCommand.QUERY_PT_INFO);
        try {
            String user = Utils.enumExecutePrivilegeName(executor.getDes().getExecutePrivilege());
            String args = "disk " + diskId;
            return getInstance().sshSessionPool.execute(host, user, executor, args);
        } catch (SSHSessionPoolException e) {
            throw new GlobalCacheSDKException("SSH会话池异常", e);
        }
    }

    /**
     * 获取PtIo信息
     *
     * @param host Ceph1节点IP
     * @return 节点状态查询结果
     * @throws GlobalCacheSDKException 执行失败抛出此异常
     * @see com.hw.globalcachesdk.entity.PtIoInfo
     */
    public static HashMap<String, CommandExecuteResult> queryPtIoInfo(String host) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(SupportedCommand.QUERY_PT_IO_INFO);
        try {
            String user = Utils.enumExecutePrivilegeName(executor.getDes().getExecutePrivilege());
            return getInstance().sshSessionPool.execute(host, user, executor);
        } catch (SSHSessionPoolException e) {
            throw new GlobalCacheSDKException("SSH会话池异常", e);
        }
    }

    /**
     * 获取节点PG信息
     *
     * @param host Ceph1节点IP
     * @param nodeId 节点ID
     * @return 节点PG信息查询结果
     * @throws GlobalCacheSDKException 执行失败抛出此异常
     * @see com.hw.globalcachesdk.entity.PgInfo
     */
    public static HashMap<String, CommandExecuteResult> queryNodePgInfo(String host, int nodeId) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(SupportedCommand.QUERY_PG_INFO);
        try {
            String user = Utils.enumExecutePrivilegeName(executor.getDes().getExecutePrivilege());
            String args = "node " + nodeId;
            return getInstance().sshSessionPool.execute(host, user, executor, args);
        } catch (SSHSessionPoolException e) {
            throw new GlobalCacheSDKException("SSH会话池异常", e);
        }
    }

    /**
     * 获取集群PG信息
     *
     * @param host Ceph1节点IP
     * @return 集群PG信息查询结果
     * @throws GlobalCacheSDKException 执行失败抛出此异常
     * @see com.hw.globalcachesdk.entity.PgInfo
     */
    public static HashMap<String, CommandExecuteResult> queryAllPgInfo(String host) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(SupportedCommand.QUERY_PG_INFO);
        try {
            String user = Utils.enumExecutePrivilegeName(executor.getDes().getExecutePrivilege());
            String args = "all";
            return getInstance().sshSessionPool.execute(host, user, executor, args);
        } catch (SSHSessionPoolException e) {
            throw new GlobalCacheSDKException("SSH会话池异常", e);
        }
    }

    /**
     * 获取磁盘PG信息
     *
     * @param host Ceph1节点IP
     * @param diskId 磁盘ID
     * @return 磁盘PG信息查询结果
     * @throws GlobalCacheSDKException 执行失败抛出此异常
     * @see com.hw.globalcachesdk.entity.PgInfo
     */
    public static HashMap<String, CommandExecuteResult> queryDiskPgInfo(String host, Integer diskId) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(SupportedCommand.QUERY_PG_INFO);
        try {
            String user = Utils.enumExecutePrivilegeName(executor.getDes().getExecutePrivilege());
            String args = "disk " + diskId;
            return getInstance().sshSessionPool.execute(host, user, executor, args);
        } catch (SSHSessionPoolException e) {
            throw new GlobalCacheSDKException("SSH会话池异常", e);
        }
    }

    /* ===============================================================自动化部署接口===============================================================*/

    /**
     * 控制GlobalCache服务
     * 注意: 该命令需要ROOT权限才能执行
     *
     * @param hosts 主机IP列表
     * @param op 操作，说明如下：
     *           start: 第一次启动GlobalCache服务
     *           restart: 重启GlobalCache服务（也可用作开启GlobalCache服务）
     *           stop: 停止GlobalCache服务
     *           clean: 清理zookeeper
     *           init: bdm初始化
     * @return 每个节点命令执行错误码, 错误码不为0表示执行失败
     * @throws GlobalCacheSDKException op不支持/执行失败抛出此异常
     * @see com.hw.globalcachesdk.entity.ErrorCodeEntity
     */
    public static HashMap<String, CommandExecuteResult> gcServiceControl(ArrayList<String> hosts, String op) throws GlobalCacheSDKException {
        switch (op) {
            case "start":
            case "restart":
            case "stop":
            case "clean":
            case "init":
                AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(SupportedCommand.GLOBAL_CACHE_SERVICE_CONTROL);
                try {
                    ArrayList<String> args = new ArrayList<>(hosts.size());
                    ArrayList<String> users = new ArrayList<>(hosts.size());
                    String user = Utils.enumExecutePrivilegeName(executor.getDes().getExecutePrivilege());
                    for (String host : hosts) {
                        args.add(op);
                        users.add(user);
                    }
                    return getInstance().sshSessionPool.execute(hosts, users, executor, args);
                } catch (SSHSessionPoolException e) {
                    throw new GlobalCacheSDKException("SSH会话池异常", e);
                }
            default:
                throw new GlobalCacheSDKException("不支持的操作");
        }
    }


    /* ===============================================================辅助配置接口===============================================================*/

    /**
     * 打印当前命令的配置信息
     *
     * @param supportedCommand 命令枚举
     * @return 命令参数配置信息
     * @throws GlobalCacheSDKException 当命令未注册时抛出此异常
     */
    public static CommandExecutorDescription getCommandConf(SupportedCommand supportedCommand) throws GlobalCacheSDKException {
        CommandExecutorFactory commandExecutorFactory = getInstance().commandExecutorFactory;
        if (null == commandExecutorFactory.getCommandExecutor(supportedCommand)) {
            throw new GlobalCacheSDKException("命令未注册");
        }

        return commandExecutorFactory.getCommandExecutor(supportedCommand).getDes();
    }

    /**
     * 设置当前命令的请求超时时间
     *
     * @param supportedCommand 命令枚举
     * @param timeout 超时时间 单位: 秒
     *                注意：超时时间必须大于0, 且为整数
     * @throws GlobalCacheSDKException 当命令未注册时抛出此异常
     */
    public static void setCommandTimeout(SupportedCommand supportedCommand, int timeout) throws GlobalCacheSDKException {
        if (timeout <= 0) {
            throw new GlobalCacheSDKException("超时时间必须大于0");
        }

        CommandExecutorFactory commandExecutorFactory = getInstance().commandExecutorFactory;
        if (null == commandExecutorFactory.getCommandExecutor(supportedCommand)) {
            throw new GlobalCacheSDKException("命令未注册");
        }

        commandExecutorFactory.getCommandExecutor(supportedCommand).getDes().setTimeout(timeout);
    }

    /**
     * 获取命令执行接口类型
     * 可以使用该接口确定调用该接口需要传入哪些hosts
     *
     * @param supportedCommand 命令枚举
     * @return 命令需要执行的节点枚举
     * @throws GlobalCacheSDKException 当命令未注册时抛出此异常
     */
    public static ExecuteNode getCommandExecuteNode(SupportedCommand supportedCommand) throws GlobalCacheSDKException {
        CommandExecutorFactory commandExecutorFactory = getInstance().commandExecutorFactory;
        if (null == commandExecutorFactory.getCommandExecutor(supportedCommand)) {
            throw new GlobalCacheSDKException("命令未注册");
        }

        return commandExecutorFactory.getCommandExecutor(supportedCommand).getDes().getExecuteNode();
    }

    /* ===============================================================私有方法===============================================================*/

    private static synchronized GlobalCacheSDK getInstance() throws GlobalCacheSDKException {
        if (null == instance) {
            instance = new GlobalCacheSDK();
        }

        return instance;
    }

    private GlobalCacheSDK() throws GlobalCacheSDKException {
        sshSessionPool = new SSHSessionPool();
        try {
            commandExecutorFactory = new CommandExecutorFactory();
        } catch (CommandExecutorFactoryException e) {
            throw new GlobalCacheSDKException("命令注册失败", e);
        }
    }
}
