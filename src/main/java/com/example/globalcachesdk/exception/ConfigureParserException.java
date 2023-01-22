package com.example.globalcachesdk.exception;


/**
 * 配置文件解析器异常
 * @author 章睿彬
 */
public class ConfigureParserException extends Exception {

    /**
     * 构造函数
     * @param message 异常信息
     */
    public ConfigureParserException(String message) {
        super(message);
    }

    /**
     * 构造函数
     * @param message 异常信息
     * @param cause 异常原因
     */
    public ConfigureParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
