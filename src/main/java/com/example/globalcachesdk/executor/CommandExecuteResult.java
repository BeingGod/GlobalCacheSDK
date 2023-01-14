package com.example.globalcachesdk.executor;

import com.example.globalcachesdk.StatusCode;
import com.example.globalcachesdk.entity.AbstractEntity;

/**
 * 节点命令执行结果
 * 对于每个命令执行结果，使用请求状态码表示其状态
 *  除了请求成功，其他状态均表示请求失败，其结果无效
 * @author 章睿彬
 */
public class CommandExecuteResult {

    /**
     * 状态码
     * 状态码不为SUCCESS则表示执行失败
     */
    private StatusCode statusCode;

    /**
     * 节点执行结果
     * 状态码不为SUCCESS，该值为null
     */
    private AbstractEntity data;

    /**
     * @return 请求状态码
     */
    public StatusCode getStatusCode() {
        return statusCode;
    }

    /**
     * @param statusCode 请求状态码
     */
    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * @return 请求结果数据实体
     */
    public AbstractEntity getData() {
        return data;
    }

    /**
     * @param data 请求结果数据实体
     */
    public void setData(AbstractEntity data) {
        this.data = data;
    }
}
