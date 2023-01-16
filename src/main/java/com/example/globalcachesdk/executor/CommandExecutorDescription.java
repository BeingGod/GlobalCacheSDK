package com.example.globalcachesdk.executor;

import com.example.globalcachesdk.ExecuteNode;
import com.example.globalcachesdk.ExecutePrivilege;

/**
 * CommandExecutor配置信息描述
 * @author 章睿彬
 */
public class CommandExecutorDescription {

    /**
     * 是否为异步调用接口（默认同步）
     * for future
     */
    private boolean isAsync = false;

    /**
     * 是否携带参数
     */
    private boolean withArgs;

    /**
     * 当前命令需要在哪些节点执行
     */
    private ExecuteNode executeNode;

    /**
     * 当前命令需要的执行权限
     */
    private ExecutePrivilege executePrivilege;

    /**
     * 接口超时时间
     * 单位：秒
     */
    private int timeout;

    public boolean isAsync() {
        return isAsync;
    }

    public void setAsync(boolean async) {
        isAsync = async;
    }

    public boolean isWithArgs() {
        return withArgs;
    }

    public void setWithArgs(boolean withArgs) {
        this.withArgs = withArgs;
    }

    public ExecuteNode getExecuteNode() {
        return executeNode;
    }

    public void setExecuteNode(ExecuteNode executeNode) {
        this.executeNode = executeNode;
    }

    public ExecutePrivilege getExecutePrivilege() {
        return executePrivilege;
    }

    public void setExecutePrivilege(ExecutePrivilege executePrivilege) {
        this.executePrivilege = executePrivilege;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        return "====================\n" +
                "命令名称: " + this.getClass().getName() + "\n" +
                "是否异步: " + (isAsync ? "Yes" : "No") + "\n" +
                "携带参数: " + (withArgs ? "Yes" : "No") + "\n" +
                "执行节点: " + executeNode.toString() + "\n" +
                "需求权限: " + executePrivilege.toString() + "\n" +
                "超时阈值: " + timeout + " s\n" +
                "====================";
    }
}
