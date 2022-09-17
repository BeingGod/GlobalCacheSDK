package com.example.demo.globalcachesdk.exception;

/**
 * SSH连接不存在异常
 * @author ya059
 */
public class SessionNotExistException extends Exception {

    /**
     * 构造函数
     * @param message 异常信息
     */
    public SessionNotExistException(String message) {
        super(message);
    }

    /**
     * 构造函数
     * @param message 异常信息
     * @param cause 异常原因
     */
    public SessionNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
