package com.hw.globalcachesdk.exception;

/**
 * 异步线程异常
 * @author 章睿彬
 */
public class AsyncThreadException extends Exception {

    /**
     * 构造函数
     * @param message 异常信息
     */
    public AsyncThreadException(String message) {super(message);}

    /**
     * 构造函数
     * @param message 异常信息
     * @param cause 异常原因
     */
    public AsyncThreadException(String message, Throwable cause) {
        super(message, cause);
    }
}
