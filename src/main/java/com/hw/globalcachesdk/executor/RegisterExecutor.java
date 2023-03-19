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
    /**
     * 配置所有节点CephConfEnv
     */
    @Registry
    ALL_NODE_CEPH_CONF_ENV,
    /**
     * 配置编译节点
     */
    @Registry
    COMPILE_NODE_CONF_ENV,
    /**
     * 配置ntp服务端
     */
    @Registry
    NTP_SERVER_NODE_CONF_ENV,
    /**
     * 配置ntp客户端
     */
    @Registry
    NTP_CLIENT_NODE_CONF_ENV,
    /**
     * 配置GC客户端节点环境
     */
    @Registry
    CLIENT_NODE_CONF_ENV,
    /**
     * 配置GC服务端节点环境
     */
    @Registry
    SERVER_NODE_CONF_ENV,
    /**
     * 编译依赖软件包
     */
    @Registry
    COMPILE_NODE_BUILD_PKGS,
    /**
     * 安装Ceph软件包
     */
    @Registry
    CEPH_NODE_INSTALL_PKGS,
    /**
     * 部署Ceph
     */
    @Registry
    CEPH1_NODE_DEPLOY_CEPH,
    /**
     * server节点安装软件包
     */
    @Registry
    SERVER_NODE_INSTALL_PKGS,
    /**
     * client节点安装软件包
     */
    @Registry
    CLIENT_NODE_INSTALL_PKGS,
    /**
     * 检查节点硬件
     */
    @Registry
    CHECK_HARDWARE,
    /**
     * 检查依赖包是否编译成功
     */
    @Registry
    IS_PKGS_BUILD_SUCCESS,
    /**
     * 检查Ceph节点是否部署成功
     */
    @Registry
    IS_CEPH_NODE_DEPLOYED,
    /**
     * 检查Client节点是否部署成功
     */
    @Registry
    IS_CLIENT_NODE_DEPLOYED,
    /**
     * 检查Server节点是否部署成功
     */
    @Registry
    IS_SERVER_NODE_DEPLOYED,
}
