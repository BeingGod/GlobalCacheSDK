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
    QUERY_CLUSTER_ALARM_INFO,
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
     * 获取节点名称
     */
    @Registry
    QUERY_HOST_NAME_INFO,
    /**
     * Ceph节点配置
     */
    @Registry
    CEPH_NODE_CONF_ENV,
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
     * 客户端编译依赖软件包
     */
    @Registry
    CLIENT_NODE_BUILD_PKGS,
    /**
     * 格式化磁盘
     */
    @Registry
    CEPH_NODE_DISK_PARTITION,
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
     * 分发软件包
     */
    @Registry
    COMPILE_NODE_DISTRIBUTE_PKGS,
    /**
     * Ceph部署检查
     */
    @Registry
    CHECK_CEPH,
    /**
     * Client节点GlobalCache部署检查
     */
    @Registry
    CHECK_CLIENT,
    /**
     * Compile节点依赖包编译检查
     */
    @Registry
    CHECK_COMPILE,
    /**
     * 节点配置文件检查
     */
    @Registry
    CHECK_CONF,
    /**
     * 软件包分发检查
     */
    @Registry
    CHECK_DISTRIBUTE,
    /**
     * 检查节点硬件
     */
    @Registry
    CHECK_HARDWARE,
    /**
     * Server节点GlobalCache部署检查
     */
    @Registry
    CHECK_SERVER,
    /**
     * Ceph1节点清理OSD
     */
    @Registry
    CEPH1_NODE_CLEAN_OSD,
    /**
     * GlobalCache软件运行检查
     */
    @Registry
    CHECK_GLOBAL_CACHE_RUNNING,
    /**
     * client节点安装ceph
     */
    @Registry
    CLIENT_NODE_INSTALL_CEPH,
    /**
     * server节点卸载globalcache
     */
    SERVER_NODE_UNINSTALL,
}
