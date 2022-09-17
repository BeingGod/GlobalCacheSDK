package com.example.globalcachesdk;

import com.example.globalcachesdk.exception.ConnectFailedException;
import com.example.globalcachesdk.exception.SessionAlreadyExistException;
import com.example.globalcachesdk.exception.SessionNotExistException;
import com.example.globalcachesdk.excutor.CommandExecuteResult;
import com.example.globalcachesdk.pool.SshSessionPool;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Global Cache SDK
 * @author ya059
 */
public class GlobalCacheSdk {

    /**
     * 创建一个SSH会话
     * @param host 需要创建链接的主机IP
     * @param password 需要创建链接的主机密码
     * @param port 需要创建链接的主机端口
     * @return
     */
    public static void createSession(String host, String user, String password, int port) throws SessionAlreadyExistException, ConnectFailedException {
        SshSessionPool sshSessionPool = SshSessionPool.getInstance();

        sshSessionPool.createSession(host, user, password, port);
    }

    /**
     * 移除一个SSH会话
     * @param host 需要移除链接的主机IP
     * @return
     */
    public static void releaseSession(String host) throws SessionAlreadyExistException, SessionNotExistException {
        SshSessionPool sshSessionPool = SshSessionPool.getInstance();

        sshSessionPool.releaseSession(host);
    }

    /**
     * 获取节点的内存信息
     * @param hosts 需要获取主机IP列表
     * @return
     * {
     *     host1: {
     *         String:
     *         Object:
     *     },
     *     host2: {
     *
     *     }
     * }
     */
    public static HashMap<String, CommandExecuteResult> getNodesMemInfo(ArrayList<String> hosts) {
        SshSessionPool sshSessionPool = SshSessionPool.getInstance();

        return sshSessionPool.execute(hosts, SupportedCommand.GET_MEM_INFO);
    }

    /**
     * 获取节点的CPU信息
     * @param hosts 需要获取主机IP列表
     * @return
     */
    public static HashMap<String, Object> getNodesCpuInfo(ArrayList<String> hosts) {
        return null;
    }

}
