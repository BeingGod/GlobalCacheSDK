package com.example.demo.globalcachesdk;

/**
 * 请求状态码
 * @author ya059
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
     * SSH连接超时
     */
    CONN_TIMEOUT,
}
