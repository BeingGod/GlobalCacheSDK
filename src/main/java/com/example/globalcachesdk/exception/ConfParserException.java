package com.example.globalcachesdk.exception;


/**
 * 配置文件解析器异常
 * @author 章睿彬
 */
public class ConfParserException extends Exception {

    /**
     * 构造函数
     * @param message 异常信息
     */
    public ConfParserException(String message) {
        super(message);
    }

    /**
     * 构造函数
     * @param message 异常信息
     * @param cause 异常原因
     */
    public ConfParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
