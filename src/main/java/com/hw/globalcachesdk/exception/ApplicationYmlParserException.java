package com.hw.globalcachesdk.exception;

/**
 * 配置文件解析器异常
 * @author 章睿彬
 */
public class ApplicationYmlParserException extends Exception {
    /**
     * 构造函数
     * @param message 异常信息
     */
    public ApplicationYmlParserException(String message) {
        super(message);
    }

    /**
     * 构造函数
     * @param message 异常信息
     * @param cause 异常原因
     */
    public ApplicationYmlParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
