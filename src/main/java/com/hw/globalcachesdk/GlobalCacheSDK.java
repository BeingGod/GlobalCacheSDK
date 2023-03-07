package com.hw.globalcachesdk;

import com.hw.globalcachesdk.entity.CephConf;
import com.hw.globalcachesdk.entity.ClientConf;
import com.hw.globalcachesdk.entity.ClusterConf;
import com.hw.globalcachesdk.exception.CommandExecutorFactoryException;
import com.hw.globalcachesdk.exception.GlobalCacheSDKException;
import com.hw.globalcachesdk.exception.SessionException;
import com.hw.globalcachesdk.exception.SSHSessionPoolException;
import com.hw.globalcachesdk.executor.*;
import com.hw.globalcachesdk.pool.SSHSessionPool;
import com.hw.globalcachesdk.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Global Cache SDK
 * @author 章睿彬
 */
public class GlobalCacheSDK {

    private static GlobalCacheSDK instance = null;

    private SSHSessionPool sshSessionPool = null;

    private Map<String, String> ip2hostnameTable = null;

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
    public static synchronized void createSession(String host, String user, String password, int port) throws GlobalCacheSDKException {
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
    public static synchronized void createSession(String host, String user, String privateKeyPath, byte[] passphrase, int port) throws GlobalCacheSDKException {
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
    public static synchronized void releaseSession(String host, String user) throws GlobalCacheSDKException {
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
    public synchronized static HashMap<String, CommandExecuteResult> queryMemInfo(ArrayList<String> hosts) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(RegisterExecutor.QUERY_MEM_INFO);
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
    public synchronized static HashMap<String, CommandExecuteResult> queryCpuInfo(ArrayList<String> hosts) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(RegisterExecutor.QUERY_CPU_INFO);
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
    public synchronized static HashMap<String, CommandExecuteResult> queryCacheDiskInfo(String host) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(RegisterExecutor.QUERY_CACHE_DISK_INFO);
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
    public synchronized static HashMap<String, CommandExecuteResult> queryClusterAlarmInfo(String host) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(RegisterExecutor.QUERY_CLUSTER_AlARM_INFO);
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
    public synchronized static HashMap<String, CommandExecuteResult> queryDiskInfo(String host) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(RegisterExecutor.QUERY_DISK_INFO);
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
    public synchronized static HashMap<String, CommandExecuteResult> queryUptime(ArrayList<String> hosts) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(RegisterExecutor.QUERY_UPTIME_INFO);
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
    public synchronized static HashMap<String, CommandExecuteResult> queryClusterStatusInfo(String host) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(RegisterExecutor.QUERY_CLUSTER_STATUS_INFO);
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
    public synchronized static HashMap<String, CommandExecuteResult> queryNodeStatusInfo(String host) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(RegisterExecutor.QUERY_NODE_STATUS_INFO);
        try {
            String user = Utils.enumExecutePrivilegeName(executor.getDes().getExecutePrivilege());
            return getInstance().sshSessionPool.execute(host, user, executor);
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
    public synchronized static HashMap<String, CommandExecuteResult> queryDiskIoInfo(ArrayList<String> hosts) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(RegisterExecutor.QUERY_DISK_IO_INFO);
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
     * @see com.hw.globalcachesdk.entity.DynamicNetInfo
     */
    public synchronized static HashMap<String, CommandExecuteResult> queryStaticNetInfo(String host) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(RegisterExecutor.QUERY_STATIC_NET_INFO);
        try {
            String user = Utils.enumExecutePrivilegeName(executor.getDes().getExecutePrivilege());
            return getInstance().sshSessionPool.execute(host, user, executor);
        } catch (SSHSessionPoolException e) {
            throw new GlobalCacheSDKException("SSH会话池异常", e);
        }
    }

    /**
     * 获取动态网络信息
     *
     * @param hosts 需要获取主机IP列表
     * @return 动态网络信息查询结果
     * @throws GlobalCacheSDKException 执行失败抛出此异常
     * @see com.hw.globalcachesdk.entity.DynamicNetInfo
     */
    public synchronized static HashMap<String, CommandExecuteResult> queryDynamicNetInfo(ArrayList<String> hosts) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(RegisterExecutor.QUERY_DYNAMIC_NET_INFO);
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
     * 获取节点PT信息
     *
     * @param host Ceph1节点IP
     * @param nodeId 节点ID
     * @return 节点PT信息查询结果
     * @throws GlobalCacheSDKException 执行失败抛出此异常
     * @see com.hw.globalcachesdk.entity.PtInfo
     */
    public synchronized static HashMap<String, CommandExecuteResult> queryNodePtInfo(String host, int nodeId) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(RegisterExecutor.QUERY_NODE_PT_INFO);
        try {
            String user = Utils.enumExecutePrivilegeName(executor.getDes().getExecutePrivilege());
            String args = "" + nodeId;
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
    public synchronized static HashMap<String, CommandExecuteResult> queryAllPtInfo(String host) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(RegisterExecutor.QUERY_ALL_PT_INFO);
        try {
            String user = Utils.enumExecutePrivilegeName(executor.getDes().getExecutePrivilege());
            return getInstance().sshSessionPool.execute(host, user, executor);
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
    public synchronized static HashMap<String, CommandExecuteResult> queryDiskPtInfo(String host, Integer diskId) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(RegisterExecutor.QUERY_DISK_PT_INFO);
        try {
            String user = Utils.enumExecutePrivilegeName(executor.getDes().getExecutePrivilege());
            String args = "" + diskId;
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
    public synchronized static HashMap<String, CommandExecuteResult> queryPtIoInfo(String host) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(RegisterExecutor.QUERY_PT_IO_INFO);
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
    public synchronized static HashMap<String, CommandExecuteResult> queryNodePgInfo(String host, int nodeId) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(RegisterExecutor.QUERY_NODE_PG_INFO);
        try {
            String user = Utils.enumExecutePrivilegeName(executor.getDes().getExecutePrivilege());
            String args = "" + nodeId;
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
    public synchronized static HashMap<String, CommandExecuteResult> queryAllPgInfo(String host) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(RegisterExecutor.QUERY_ALL_PG_INFO);
        try {
            String user = Utils.enumExecutePrivilegeName(executor.getDes().getExecutePrivilege());
            return getInstance().sshSessionPool.execute(host, user, executor);
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
    public synchronized static HashMap<String, CommandExecuteResult> queryDiskPgInfo(String host, Integer diskId) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(RegisterExecutor.QUERY_DISK_PG_INFO);
        try {
            String user = Utils.enumExecutePrivilegeName(executor.getDes().getExecutePrivilege());
            String args = "" + diskId;
            return getInstance().sshSessionPool.execute(host, user, executor, args);
        } catch (SSHSessionPoolException e) {
            throw new GlobalCacheSDKException("SSH会话池异常", e);
        }
    }

    /* ===============================================================自动化部署接口===============================================================*/

    /**
     * 初始化集群配置
     *
     * @param cephConfs Ceph节点配置信息
     * @param clientConfs Client节点配置信息
     * @param clusterConf 集群配置信息
     * @return
     * @throws GlobalCacheSDKException 执行失败抛出此异常
     * @see com.hw.globalcachesdk.entity.ClientConf
     * @see com.hw.globalcachesdk.entity.CephConf
     * @see com.hw.globalcachesdk.entity.ClusterConf
     */
    public synchronized static HashMap<String, CommandExecuteResult> initClusterSettings(ArrayList<CephConf> cephConfs, ArrayList<ClientConf> clientConfs, ClusterConf clusterConf) throws GlobalCacheSDKException {
        for (CephConf cephConf : cephConfs) {
            getInstance().ip2hostnameTable.put(cephConf.getHostname(), cephConf.getPublicIp());
        }

        for (ClientConf clientConf : clientConfs) {
            getInstance().ip2hostnameTable.put(clientConf.getHostname(), clientConf.getPublicIp());
        }

        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(RegisterExecutor.INIT_CLUSTER_SETTINGS);
        try {
            ArrayList<String> hosts = new ArrayList<>();
            for (Map.Entry<String, String> entry : getInstance().ip2hostnameTable.entrySet()) {
                hosts.add(entry.getKey());
            }

            ArrayList<String> users = new ArrayList<>(hosts.size());
            String user = Utils.enumExecutePrivilegeName(executor.getDes().getExecutePrivilege());
            for (String host : hosts) {
                users.add(user);
            }

            // hostnamelist.txt
            StringBuilder hostnamelistContent = new StringBuilder("");
            for (Map.Entry<String, String> entry : getInstance().ip2hostnameTable.entrySet()) {
                // ip hostname
                hostnamelistContent.append(entry.getKey()).append(" ").append(entry.getValue()).append("\n");
            }

            // nodelist.txt
            StringBuilder nodelistContent = new StringBuilder("");
            int ccmMonitorNodeNum = -1;
            int zkServerNodeNum = -1;
            for (CephConf cephConf : cephConfs) {
                if (cephConf.isCcmMonitor()) {
                    ccmMonitorNodeNum = cephConf.getNodeNumber();
                }
                if (cephConf.isZkServer()) {
                    zkServerNodeNum = cephConf.getNodeNumber();
                }
            }

            for (CephConf cephConf : cephConfs) {
                // hostname node_number public_ip public_ip local_ip cluster_ip pt_num pg_num <device1> <device2> zk_server ccm_monitor
                nodelistContent.append(cephConf.getHostname()).append(" ")
                        .append(cephConf.getNodeNumber()).append(" ")
                        .append(cephConf.getPublicIp()).append(" ")
                        .append(cephConf.getPublicIp()).append(" ")
                        .append(cephConf.getLocalIp()).append(" ")
                        .append(cephConf.getClusterIp()).append(" ")
                        .append(clusterConf.getPtNum()).append(" ")
                        .append(clusterConf.getPtNum()).append(" ")
                        .append("<device1>").append(" ")
                        .append("<device2>").append(" ")
                        .append(zkServerNodeNum).append(" ")
                        .append(ccmMonitorNodeNum).append("\n")
                        .append("\n");
            }

            // script.conf
            String ntpServer = "";
            for (CephConf cephConf : cephConfs) {
                if (cephConf.isNtpServer()) {
                    ntpServer = cephConf.getPublicIp();
                }
            }

            ArrayList<String> scriptConfContents = new ArrayList<>();
            StringBuilder scriptConfContent = new StringBuilder("");
            for (CephConf cephConf : cephConfs) {
                scriptConfContent.append("hostname: ").append(cephConf.getHostname()).append("\n")
                        .append("ip: ").append(cephConf.getPublicIp()).append("\n")
                        .append("mask: ").append(cephConf.getNetworkMask()).append("\n")
                        .append("public_network: ").append(clusterConf.getPublicNetwork()).append("\n")
                        .append("cluster_network: ").append(clusterConf.getClusterNetwork()).append("\n")
                        .append("ntp_server: ").append(ntpServer).append("\n")
                        .append("password: ").append(cephConf.getRootPasswd());
            }

            for (ClientConf clientConf : clientConfs) {
                scriptConfContent.append("hostname: ").append(clientConf.getHostname()).append("\n")
                        .append("ip: ").append(clientConf.getPublicIp()).append("\n")
                        .append("mask: ").append(clientConf.getNetworkMask()).append("\n")
                        .append("public_network: ").append(clusterConf.getPublicNetwork()).append("\n")
                        .append("cluster_network: ").append(clusterConf.getClusterNetwork()).append("\n")
                        .append("ntp_server: ").append(ntpServer).append("\n")
                        .append("password: ").append(clientConf.getRootPasswd());
                scriptConfContents.add(scriptConfContent.toString());
            }


            // disklist.txt
            StringBuilder disklistContent = new StringBuilder();
            for (CephConf cephConf : cephConfs) {
                for (String dataDisk : cephConf.getDataDiskList()) {
                    disklistContent.append(dataDisk).append("\n");
                }
                for (String cacheDisk : cephConf.getCacheDiskList()) {
                    disklistContent.append(cacheDisk).append("\n");
                }
            }

            ArrayList<String> args = new ArrayList<>();
            // hostnamelist.txt disklist.txt nodelist.txt script.conf
            for (int i = 0; i < hosts.size(); ++i) {
                String arg = hostnamelistContent.toString() + " " + disklistContent.toString() + " "
                        + nodelistContent.toString() + " " + scriptConfContents.get(i);
                args.add(arg);
            }

            return getInstance().sshSessionPool.execute(hosts, users, executor, args);
        } catch (SSHSessionPoolException e) {
            throw new GlobalCacheSDKException("SSH会话池异常", e);
        }
    }

    public synchronized static HashMap<String, CommandExecuteResult> checkHardware() {
        return null;
    }

    public synchronized static HashMap<String, CommandExecuteResult> compileNodeConfigureEnv() {
        return null;
    }

    public synchronized static HashMap<String, CommandExecuteResult> compileNodeBuildPackages() {
        return null;
    }

    public synchronized static HashMap<String, CommandExecuteResult> compileNodeDistributePackages() {
        return null;
    }

    public synchronized static HashMap<String, CommandExecuteResult> cephConfigureEnvironment() {
        return null;
    }

    public synchronized static HashMap<String, CommandExecuteResult> ntpServerConfigure() {
        return null;
    }

    public synchronized static HashMap<String, CommandExecuteResult> ntpClientConfigure() {
        return null;
    }

    public synchronized static HashMap<String, CommandExecuteResult> cephNodeInstallPackages() {
        return null;
    }

    public synchronized static HashMap<String, CommandExecuteResult> cephNodeDeploy() {
        return null;
    }

    public synchronized static HashMap<String, CommandExecuteResult> serverNodeConfigureEnvironment() {
        return null;
    }

    public synchronized static HashMap<String, CommandExecuteResult> clientNodeConfigureEnvironment() {
        return null;
    }

    public synchronized static HashMap<String, CommandExecuteResult> serverNodeInstallPackages() {
        return null;
    }

    public synchronized static HashMap<String, CommandExecuteResult> clientNodeInstallPackages() {
        return null;
    }

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
    public synchronized static HashMap<String, CommandExecuteResult> gcServiceControl(ArrayList<String> hosts, String op) throws GlobalCacheSDKException {
        switch (op) {
            case "start":
            case "restart":
            case "stop":
            case "clean":
            case "init":
                AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(RegisterExecutor.GLOBAL_CACHE_SERVICE_CONTROL);
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
     * @param registerExecutor 命令枚举
     * @return 命令参数配置信息
     * @throws GlobalCacheSDKException 当命令未注册时抛出此异常
     */
    public static CommandExecutorDescription getCommandConf(RegisterExecutor registerExecutor) throws GlobalCacheSDKException {
        CommandExecutorFactory commandExecutorFactory = getInstance().commandExecutorFactory;
        if (null == commandExecutorFactory.getCommandExecutor(registerExecutor)) {
            throw new GlobalCacheSDKException("命令未注册");
        }

        return commandExecutorFactory.getCommandExecutor(registerExecutor).getDes();
    }

    /**
     * 设置当前命令的请求超时时间
     *
     * @param registerExecutor 命令枚举
     * @param timeout 超时时间 单位: 秒
     *                注意：超时时间必须大于0, 且为整数
     * @throws GlobalCacheSDKException 当命令未注册时抛出此异常
     */
    public synchronized static void setCommandTimeout(RegisterExecutor registerExecutor, int timeout) throws GlobalCacheSDKException {
        if (timeout <= 0) {
            throw new GlobalCacheSDKException("超时时间必须大于0");
        }

        CommandExecutorFactory commandExecutorFactory = getInstance().commandExecutorFactory;
        if (null == commandExecutorFactory.getCommandExecutor(registerExecutor)) {
            throw new GlobalCacheSDKException("命令未注册");
        }

        commandExecutorFactory.getCommandExecutor(registerExecutor).getDes().setTimeout(timeout);
    }

    /**
     * 获取命令执行接口类型
     * 可以使用该接口确定调用该接口需要传入哪些hosts
     *
     * @param registerExecutor 命令枚举
     * @return 命令需要执行的节点枚举
     * @throws GlobalCacheSDKException 当命令未注册时抛出此异常
     */
    public static ExecuteNode getCommandExecuteNode(RegisterExecutor registerExecutor) throws GlobalCacheSDKException {
        CommandExecutorFactory commandExecutorFactory = getInstance().commandExecutorFactory;
        if (null == commandExecutorFactory.getCommandExecutor(registerExecutor)) {
            throw new GlobalCacheSDKException("命令未注册");
        }

        return commandExecutorFactory.getCommandExecutor(registerExecutor).getDes().getExecuteNode();
    }

    /* ===============================================================私有方法===============================================================*/

    private static synchronized GlobalCacheSDK getInstance() throws GlobalCacheSDKException {
        if (null == instance) {
            instance = new GlobalCacheSDK();
        }

        return instance;
    }

    private GlobalCacheSDK() throws GlobalCacheSDKException {
        ip2hostnameTable = new Hashtable<>();
        sshSessionPool = new SSHSessionPool();
        try {
            commandExecutorFactory = new CommandExecutorFactory();
        } catch (CommandExecutorFactoryException e) {
            throw new GlobalCacheSDKException("命令注册失败", e);
        }
    }
}
