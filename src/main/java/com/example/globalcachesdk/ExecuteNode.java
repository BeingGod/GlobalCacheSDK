package com.example.globalcachesdk;

/**
 * 当前命令需要在哪些节点执行
 * @author 章睿彬
 */
public enum ExecuteNode {
    /**
     * 集群所有节点
     */
    ALL_NODES,
    /**
     * 集群任意节点
     */
    ANY_NODES,
    /**
     * 集群所有Ceph节点
     */
    ALL_CEPH_NODES,
    /**
     * 集群所有Client节点
     */
    ALL_CLIENT_NODES,
    /**
     * 集群任意Ceph节点
     */
    ANY_CEPH_NODES,
    /**
     * 集群任意Client节点
     */
    ANY_CLIENT_NODES,
    /**
     * Ceph1节点
     */
    CEPH1_ONLY,
}
