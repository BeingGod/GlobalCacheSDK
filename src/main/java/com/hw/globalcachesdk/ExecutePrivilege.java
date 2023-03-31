package com.hw.globalcachesdk;

/**
 * 当前命令需要的执行权限
 * @author 章睿彬
 */
public enum ExecutePrivilege {
    /**
     * root用户
     */
    ROOT,
    /**
     * global cache运维用户
     */
    GLOBAL_CACHE_OP,
    /**
     * 普通用户
     */
    @Deprecated
    USER,
}
