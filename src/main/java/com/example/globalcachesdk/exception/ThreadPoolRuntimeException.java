package com.example.globalcachesdk.exception;

/**
 * 线程池运行时错误
 * @author 李金泽
 */
public class ThreadPoolRuntimeException extends Exception{
    /**
     * 构造函数
     * @param message 异常信息
     */
    public ThreadPoolRuntimeException(String message) {super(message);}

    /**
     * 构造函数
     * @param message 异常信息
     * @param cause 异常原因
     */
    public ThreadPoolRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
