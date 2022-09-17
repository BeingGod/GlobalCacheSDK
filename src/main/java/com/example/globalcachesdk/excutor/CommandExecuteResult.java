package com.example.globalcachesdk.excutor;

import com.example.globalcachesdk.StatusCode;
import lombok.Data;

/**
 * 节点命令执行结果
 * @author ya059
 */
@Data
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
    private Object data;
}
