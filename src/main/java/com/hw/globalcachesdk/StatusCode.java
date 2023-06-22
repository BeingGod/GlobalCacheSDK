package com.hw.globalcachesdk;

/**
 * 请求状态码
 * 除了请求成功，其他状态均表示请求失败
 * @author 章睿彬
 */
public enum StatusCode {
    /**
     * 请求成功
     */
    SUCCESS,
    /**
     * 会话不存在
     */
    SESSION_NOT_EXIST,
    /**
     * 执行命令失败
     */
    EXEC_COMMAND_FAILED,
    /**
     * 执行命令超时
     */
    EXEC_COMMAND_TIMEOUT,
    /**
     * SSH连接超时
     */
    CONN_TIMEOUT,
    /**
     * 返回值解析失败
     */
    RETURN_VALUE_PARSE_FAILED,
    /**
     * 权限拒绝
     */
    PERMISSION_DENIED,
}
