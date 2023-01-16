package com.example.globalcachesdk.exception;

/**
 * 命令注册函数异常
 * @author 章睿彬
 */
public class CommandExecutorFactoryException extends Exception {
    /**
     * 构造函数
     * @param message 异常信息
     */
    public CommandExecutorFactoryException(String message) {super(message);}

    /**
     * 构造函数
     * @param message 异常信息
     * @param cause 异常原因
     */
    public CommandExecutorFactoryException(String message, Throwable cause) {
        super(message, cause);
    }
}