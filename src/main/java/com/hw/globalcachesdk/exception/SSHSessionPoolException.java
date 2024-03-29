package com.hw.globalcachesdk.exception;

/**
 * ssh session pool exception
 * @author 章睿彬
 */
public class SSHSessionPoolException extends Exception {

    /**
     * 构造函数
     * @param message 异常信息
     */
    public SSHSessionPoolException(String message) {
        super(message);
    }

    /**
     * 构造函数
     * @param message 异常信息
     * @param cause 异常原因
     */
    public SSHSessionPoolException(String message, Throwable cause) {
        super(message, cause);
    }
}
