package com.example.globalcachesdk.entity;

/**
 * 错误码实体
 * @author 章睿彬
 */
public class ErrorCodeEntity extends AbstractEntity {
    /**
     * 错误码
     */
    private int errorCode;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
