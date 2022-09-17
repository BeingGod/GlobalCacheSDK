package com.example.globalcachesdk.exception;

/**
 * 不支持的命令
 * @author ya059
 */
public class UnsupportCommandException extends Exception {

    /**
     * 构造函数
     * @param message 异常信息
     */
    public UnsupportCommandException(String message) {
        super(message);
    }

    /**
     * 构造函数
     * @param message 异常信息
     * @param cause 异常原因
     */
    public UnsupportCommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
