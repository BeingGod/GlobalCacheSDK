package com.example.globalcachesdk.exception;

/**
 * 命令执行异常
 * @author 章睿彬
 */
public class CommandExecException extends Exception {

    /**
     * 构造函数
     * @param message 异常信息
     */
    public CommandExecException(String message) {super(message);}

    /**
     * 构造函数
     * @param message 异常信息
     * @param cause 异常原因
     */
    public CommandExecException(String message, Throwable cause) {
        super(message, cause);
    }
}
