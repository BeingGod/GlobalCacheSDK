package com.hw.globalcachesdk.executor;

/**
 * 节点支持的可执行命令
 * @author 章睿彬
 */
public enum RegisterExecutor {
    /**
     * 获取节点内存信息
     */
    @Registry
    QUERY_MEM_INFO,
    /**
     * 获取节点CPU信息
     */
    @Registry
    QUERY_CPU_INFO,
    /**
     * GC服务控制
     */
    @Registry
    GLOBAL_CACHE_SERVICE_CONTROL,
    /**
     * 获取节点磁盘信息
     */
    @Registry
    QUERY_DISK_INFO,
    /**
     * 获取节点运行时间
     */
    @Registry
    QUERY_UPTIME_INFO,
    /**
     * 获取节点运行状态
     */
    @Registry
    QUERY_CLUSTER_STATUS_INFO,
    /**
     * 获取节点状态
     */
    @Registry
    QUERY_NODE_STATUS_INFO,
    /**
     * 获取节点数据盘分区信息
     */
    @Registry
    QUERY_DATA_DISK_PART_INFO,
    /**
     * 查询节点PtIo信息
     */
    @Registry
    QUERY_PT_IO_INFO,
    /**
     * 获取缓存盘状态
     */
    @Registry
    QUERY_CACHE_DISK_INFO,
    /**
     * 获取集群异常状态信息
     */
    @Registry
    QUERY_CLUSTER_AlARM_INFO,
    /**
     * 获取全部PG信息
     */
    @Registry
    QUERY_ALL_PG_INFO,
    /**
     * 获取节点PG信息
     */
    @Registry
    QUERY_NODE_PG_INFO,
    /**
     * 获取磁盘PG信息
     */
    @Registry
    QUERY_DISK_PG_INFO,
    /**
     * 获取全部PT信息
     */
    @Registry
    QUERY_ALL_PT_INFO,
    /**
     * 获取节点PT信息
     */
    @Registry
    QUERY_NODE_PT_INFO,
    /**
     * 获取磁盘PT信息
     */
    @Registry
    QUERY_DISK_PT_INFO,
    /**
     * 获取节点静态网卡信息
     */
    @Registry
    QUERY_STATIC_NET_INFO,
    /**
     * 获取节点动态网卡信息
     */
    @Registry
    QUERY_DYNAMIC_NET_INFO,
    /**
     * 获取节点磁盘IO状态
     */
    @Registry
    QUERY_DISK_IO_INFO,
}
