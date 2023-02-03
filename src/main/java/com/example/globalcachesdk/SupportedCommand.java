package com.example.globalcachesdk;

import com.example.globalcachesdk.executor.Registry;

/**
 * 节点支持的可执行命令
 * @author 章睿彬
 */
public enum SupportedCommand {
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
     * 获取PT信息
     */
    @Registry
    QUERY_PT_INFO,
}
