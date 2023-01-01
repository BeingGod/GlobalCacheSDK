package com.example.globalcachesdk;

/**
 * 节点支持的可执行命令
 * @author 章睿彬
 */
public enum SupportedCommand {
    /**
     * 获取节点内存信息
     */
    GET_MEM_INFO,
    /**
     * 获取节点CPU信息
     */
    GET_CPU_INFO,
    /**
     * 初始化节点BDM
     */
    BDM_INIT,
    /**
     * 清理节点zookeeper
     */
    ZOOKEEPER_CLEAN,
    /**
     * 配置节点GC_Service
     */
    GC_SERVICE_CONTROL,

}
