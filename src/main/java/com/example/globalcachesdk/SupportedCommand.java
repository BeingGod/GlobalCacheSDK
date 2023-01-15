package com.example.globalcachesdk;

/**
 * 节点支持的可执行命令
 * @author 章睿彬
 */
public enum SupportedCommand {
    /**
     * 获取节点内存信息
     */
    QUERY_MEM_INFO,
    /**
     * 获取节点CPU信息
     */
    QUERY_CPU_INFO,
    /**
     * GC服务控制
     */
    GC_SERVICE_CONTROL,
}
