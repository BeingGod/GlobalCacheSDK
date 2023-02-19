package com.hw.globalcachesdk.exception;

/**
 * 返回值解析异常
 * @author 章睿彬
 */
public class ReturnValueParseException extends Exception {
    /**
     * 构造函数
     * @param message 异常信息
     */
    public ReturnValueParseException(String message) {
        super(message);
    }

    /**
     * 构造函数
     * @param message 异常信息
     * @param cause 异常原因
     */
    public ReturnValueParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
