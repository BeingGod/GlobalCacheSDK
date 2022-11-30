package com.example.globalcachesdk;

import com.example.globalcachesdk.exception.*;
import com.example.globalcachesdk.excutor.CommandExecuteResult;
import com.example.globalcachesdk.pool.SshSessionPool;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Global Cache SDK
 * @author 章睿彬
 */
public class GlobalCacheSdk {

    /**
     * 创建一个SSH会话，若未抛出异常则表示执行成功
     * 在调用功能接口之前，需要调用该接口创建SSH会话
     * 创建SSH会话需要建立SSH连接，该过程较慢，建议在初始化的功能中一次性建立所有连接
     *
     * @param host 需要创建会话的主机IP
     * @param password 需要创建会话的主机密码
     * @param port 需要创建会话的主机端口
     * @throws SessionAlreadyExistException 会话已存在抛出此异常
     */
    public static void createSession(String host, String user, String password, int port) throws SessionAlreadyExistException, ConnectFailedException {
        SshSessionPool sshSessionPool = SshSessionPool.getInstance();

        sshSessionPool.createSession(host, user, password, port);
    }

    /**
     * 移除一个SSH会话，若未抛出异常则表示执行成功
     * 当已不需要一个会话的时候，可以调用该接口移除SSH会话
     *
     * @param host 需要移除会话的主机IP
     * @throws SessionCloseFailedException 会话关闭失败抛出此异常
     * @throws SessionNotExistException 会话不存在抛出此异常
     */
    public static void releaseSession(String host) throws SessionCloseFailedException, SessionNotExistException {
        SshSessionPool sshSessionPool = SshSessionPool.getInstance();

        sshSessionPool.releaseSession(host);
    }

    /**
     * 获取节点的内存信息
     * 内存信息包括：总内存，可用内存，空闲内存
     * 所有内存信息的单位均为 Byte
     *
     * @param hosts 需要获取主机IP列表
     * @return 每个节点内存信息查询结果
     * @throws ThreadPoolRuntimeException 运行时线程池故障抛出此异常
     * @see com.example.globalcachesdk.entity.MemInfo
     */
    public static HashMap<String, CommandExecuteResult> getNodesMemInfo(ArrayList<String> hosts) throws ThreadPoolRuntimeException {
        SshSessionPool sshSessionPool = SshSessionPool.getInstance();

        return sshSessionPool.execute(hosts, SupportedCommand.GET_MEM_INFO);
    }

    /**
     * 获取节点的CPU信息
     * CPU信息包括：CPU总体利用率，每个逻辑核心的利用率
     * CPU的利用率以百分比为单位
     *
     * @param hosts 需要获取主机IP列表
     * @return 每个节点CPU信息查询结果
     * @throws ThreadPoolRuntimeException 运行时线程池故障抛出此异常
     * @see com.example.globalcachesdk.entity.CpuInfo
     */
    public static HashMap<String, CommandExecuteResult> getNodesCpuInfo(ArrayList<String> hosts) throws ThreadPoolRuntimeException {
        SshSessionPool sshSessionPool = SshSessionPool.getInstance();

        return  sshSessionPool.execute(hosts, SupportedCommand.GET_CPU_INFO);
    }

}
