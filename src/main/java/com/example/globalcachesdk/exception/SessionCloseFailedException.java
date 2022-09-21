package com.example.globalcachesdk.exception;

/**
 * JSCH SSH连接关闭失败异常
 * @author ya059
 */
public class SessionCloseFailedException extends Exception {
    /**
     * 构造函数
     * @param message 异常信息
     */
    public SessionCloseFailedException(String message) {
        super(message);
    }

    /**
     * 构造函数
     * @param message 异常信息
     * @param cause 异常原因
     */
    public SessionCloseFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
