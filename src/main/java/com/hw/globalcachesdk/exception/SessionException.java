package com.hw.globalcachesdk.exception;

/**
 * SSH会话异常
 * @author 章睿彬
 */
public class SessionException extends Exception {

    /**
     * 构造函数
     * @param message 异常信息
     */
    public SessionException(String message) {
        super(message);
    }

    /**
     * 构造函数
     * @param message 异常信息
     * @param cause 异常原因
     */
    public SessionException(String message, Throwable cause) {
        super(message, cause);
    }
}
