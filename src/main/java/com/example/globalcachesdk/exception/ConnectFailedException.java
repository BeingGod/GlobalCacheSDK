package com.example.globalcachesdk.exception;

/**
 * SSH连接创建失败异常
 * @author 章睿彬
 */
public class ConnectFailedException extends Exception {

    /**
     * 构造函数
     * @param message 异常信息
     */
    public ConnectFailedException(String message) {
        super(message);
    }

    /**
     * 构造函数
     * @param message 异常信息
     * @param cause 异常原因
     */
    public ConnectFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}

