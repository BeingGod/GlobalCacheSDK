package com.hw.globalcachesdk.entity;

/**
 * 错误码实体
 * @author 章睿彬
 */
public class ErrorCodeEntity extends AbstractEntity {
    /**
     * 错误码
     */
    private int errorCode;

    /**
     * 错误信息
     */
    private String message;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
