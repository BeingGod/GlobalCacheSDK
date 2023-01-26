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
    @Registry
    QUERY_CACHE_DISK_INFO,
}
