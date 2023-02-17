package com.hw.globalcachesdk.executor;

import com.hw.globalcachesdk.ExecuteNode;
import com.hw.globalcachesdk.ExecutePrivilege;

/**
 * CommandExecutor配置信息描述
 * @author 章睿彬
 */
public class CommandExecutorDescription {

    /**
     * 命令名称
     */
    private String name = "Unknown";

    /**
     * 是否为异步调用接口（默认同步）
     * for future
     */
    private boolean isAsync = false;

    /**
     * 是否携带参数
     */
    private boolean withArgs = false;

    /**
     * 当前命令需要在哪些节点执行
     */
    private ExecuteNode executeNode = ExecuteNode.ALL_NODES;

    /**
     * 当前命令需要的执行权限
     */
    private ExecutePrivilege executePrivilege = ExecutePrivilege.USER;

    /**
     * 接口超时时间
     * 单位：秒
     */
    private int timeout = 1;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
                "命令名称: " + name + "\n" +
                "是否异步: " + (isAsync ? "Yes" : "No") + "\n" +
                "携带参数: " + (withArgs ? "Yes" : "No") + "\n" +
                "执行节点: " + executeNode.toString() + "\n" +
                "需求权限: " + executePrivilege.toString() + "\n" +
                "超时阈值: " + timeout + " s\n" +
                "====================";
    }
}
