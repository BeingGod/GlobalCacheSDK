package com.example.globalcachesdk.exception;

/**
 * SSH会话不存在异常
 * @author 章睿彬
 */
public class SessionAlreadyExistException extends Exception {
    /**
     * 构造函数
     * @param message 异常信息
     */
    public SessionAlreadyExistException(String message) {
        super(message);
    }

    /**
     * 构造函数
     * @param message 异常信息
     * @param cause 异常原因
     */
    public SessionAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
