package com.example.globalcachesdk;

import cn.hutool.Hutool;
import cn.hutool.extra.ssh.JschUtil;
import com.example.globalcachesdk.exception.GlobalCacheSDKException;
import com.example.globalcachesdk.exception.SessionException;
import com.example.globalcachesdk.exception.SshSessionPoolException;
import com.example.globalcachesdk.executor.AbstractCommandExecutor;
import com.example.globalcachesdk.executor.CommandExecuteResult;
import com.example.globalcachesdk.executor.CommandExecutorFactory;
import com.example.globalcachesdk.pool.SshSessionPool;
import com.example.globalcachesdk.sdk.deploy.GCServiceControl;
import com.example.globalcachesdk.sdk.info.QueryCpuInfo;
import com.example.globalcachesdk.sdk.info.QueryMemInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Global Cache SDK
 * @author 章睿彬
 */
public class GlobalCacheSDK {

    private static GlobalCacheSDK instance = null;

    private SshSessionPool sshSessionPool = null;

    private CommandExecutorFactory commandExecutorFactory = null;

    /* ===============================================================会话管理接口===============================================================*/

    /**
     * 创建一个SSH会话，若未抛出异常则表示执行成功
     * 在调用功能接口之前，需要调用该接口创建SSH会话
     * 创建SSH会话需要建立SSH连接，该过程较慢，建议在初始化的功能中一次性建立所有连接
     *
     * @param host 需要创建会话的主机IP
     * @param password 需要创建会话的主机密码
     * @param port 需要创建会话的主机端口
     * @throws GlobalCacheSDKException 会话创建失败抛出此异常
     */
    public static void createSession(String host, String user, String password, int port) throws GlobalCacheSDKException {
        try {
            getInstance().sshSessionPool.createSession(host, user, password, port);
        } catch (SessionException e) {
            throw new GlobalCacheSDKException("会话异常", e);
        }
    }

    /**
     * 移除一个SSH会话，若未抛出异常则表示执行成功
     * 当已不需要一个会话的时候，可以调用该接口移除SSH会话
     *
     * @param host 需要移除会话的主机IP
     * @throws GlobalCacheSDKException 会话关闭失败/会话不存在抛出此异常
     */
    public static void releaseSession(String host) throws GlobalCacheSDKException {
        try {
            getInstance().sshSessionPool.releaseSession(host);
        } catch (SessionException e) {
            throw new GlobalCacheSDKException("会话异常", e);
        }
    }

    /* ===============================================================信息获取接口===============================================================*/

    /**
     * 获取节点的内存信息
     * 内存信息包括：总内存，可用内存，空闲内存
     * 所有内存信息的单位均为 Byte
     *
     * @param hosts 需要获取主机IP列表
     * @return 每个节点内存信息查询结果
     * @throws GlobalCacheSDKException 执行失败抛出此异常
     * @see com.example.globalcachesdk.entity.MemInfo
     */
    public static HashMap<String, CommandExecuteResult> queryMemInfo(ArrayList<String> hosts) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(SupportedCommand.QUERY_MEM_INFO);
        try {
            return getInstance().sshSessionPool.execute(hosts, executor);
        } catch (SshSessionPoolException e) {
            throw new GlobalCacheSDKException("SSH会话池异常", e);
        }
    }

    /**
     * 获取节点的CPU信息
     * CPU信息包括：CPU总体利用率，每个逻辑核心的利用率
     * CPU的利用率以百分比为单位
     *
     * @param hosts 需要获取主机IP列表
     * @return 每个节点CPU信息查询结果
     * @throws GlobalCacheSDKException 执行失败抛出此异常
     * @see com.example.globalcachesdk.entity.CpuInfo
     */
    public static HashMap<String, CommandExecuteResult> queryCpuInfo(ArrayList<String> hosts) throws GlobalCacheSDKException {
        AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(SupportedCommand.QUERY_CPU_INFO);
        try {
            return getInstance().sshSessionPool.execute(hosts, executor);
        } catch (SshSessionPoolException e) {
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
     * @see com.example.globalcachesdk.entity.ErrorCodeEntity
     */
    public static HashMap<String, CommandExecuteResult> gcServiceControl(ArrayList<String> hosts, String op) throws GlobalCacheSDKException {
        switch (op) {
            case "start":
            case "restart":
            case "stop":
            case "clean":
            case "init":
                AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(SupportedCommand.GC_SERVICE_CONTROL);
                try {
                    ArrayList<String> args = new ArrayList<>(hosts.size());
                    for (String host : hosts) {
                        args.add(op);
                    }
                    return getInstance().sshSessionPool.execute(hosts, executor, args);
                } catch (SshSessionPoolException e) {
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
     * @throws GlobalCacheSDKException 当命令未注册时抛出此异常
     */
    public static void printCommandConf(SupportedCommand supportedCommand) throws GlobalCacheSDKException {
        CommandExecutorFactory commandExecutorFactory = getInstance().commandExecutorFactory;
        if (null == commandExecutorFactory.getCommandExecutor(supportedCommand)) {
            throw new GlobalCacheSDKException("命令未注册");
        }

        System.out.println(commandExecutorFactory.getCommandExecutor(supportedCommand).getDes().toString());
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

    /* ===============================================================私有方法===============================================================*/

    private static synchronized GlobalCacheSDK getInstance() throws GlobalCacheSDKException {
        if (null == instance) {
            instance = new GlobalCacheSDK();
        }

        return instance;
    }

    private GlobalCacheSDK() throws GlobalCacheSDKException {
        sshSessionPool = new SshSessionPool();
        commandExecutorFactory = new CommandExecutorFactory();

        // 注册所有的命令类
        // @TODO: 支持采用注解进行注册
        if (!commandExecutorFactory.registryCommandExecutor(SupportedCommand.QUERY_CPU_INFO, QueryCpuInfo.defaultDes())) {
            throw new GlobalCacheSDKException("QueryCpuINfo注册失败");
        }

        if (!commandExecutorFactory.registryCommandExecutor(SupportedCommand.QUERY_MEM_INFO, QueryMemInfo.defaultDes())) {
            throw new GlobalCacheSDKException("QueryMemInfo注册失败");
        }

        if (!commandExecutorFactory.registryCommandExecutor(SupportedCommand.GC_SERVICE_CONTROL, GCServiceControl.defaultDes())) {
            throw new GlobalCacheSDKException("GCServiceConrtol注册失败");
        }
    }
}
