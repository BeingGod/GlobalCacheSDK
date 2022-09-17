package com.example.globalcachesdk.exception;

/**
 * 命令执行失败异常
 * @author ya059
 */
public class CommandExecFailedException extends Exception {

    /**
     * 构造函数
     * @param message 异常信息
     */
    public CommandExecFailedException(String message) {super(message);}

    /**
     * 构造函数
     * @param message 异常信息
     * @param cause 异常原因
     */
    public CommandExecFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
