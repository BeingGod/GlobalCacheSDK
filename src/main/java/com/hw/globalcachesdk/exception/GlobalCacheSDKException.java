package com.hw.globalcachesdk.exception;


/**
 * Global Cache SDK 异常
 * @author 章睿彬
 */
public class GlobalCacheSDKException extends Exception {

    /**
     * 构造函数
     * @param message 异常信息
     */
    public GlobalCacheSDKException(String message) {
        super(message);
    }

    /**
     * 构造函数
     * @param message 异常信息
     * @param cause 异常原因
     */
    public GlobalCacheSDKException(String message, Throwable cause) {
        super(message, cause);
    }
}
