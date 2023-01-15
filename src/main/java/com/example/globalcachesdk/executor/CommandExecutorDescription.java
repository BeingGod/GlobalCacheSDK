package com.example.globalcachesdk.executor;

/**
 * CommandExecutor配置信息描述
 * @author 章睿彬
 */
public class CommandExecutorDescription {
    /**
     * 当前命令需要在哪些节点执行
     */
    public enum ExecuteNode {
        /**
         * 集群所有节点
         */
        ALL_NODES,
        /**
         * 集群所有Ceph节点
         */
        ALL_CEPH_NOES,
        /**
         * 集群所有Client节点
         */
        ALL_CLIENT_NODES,
        /**
         * 集群人体Ceph节点
         */
        ANY_CEPH_NOES,
        /**
         * 集群任意Client节点
         */
        ANY_CLIENT_NODES,
        /**
         * Ceph1节点
         */
        CEPH1_ONLY,
    }

    /**
     * 当前命令需要的执行权限
     */
    public enum ExecutePrivilege {
        /**
         * root用户
         */
        ROOT,
        /**
         * global cache运维用户
         */
        GLOBAL_CACHE_OP,
        /**
         * 普通用户
         */
        USER,
    }

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
     * 方法名称
     */
    private String classPath;

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

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
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
                "命令名称: " + classPath + "\n" +
                "是否异步: " + (isAsync ? "Yes" : "No") + "\n" +
                "携带参数: " + (withArgs ? "Yes" : "No") + "\n" +
                "执行节点: " + executeNode.toString() + "\n" +
                "需求权限: " + executePrivilege.toString() + "\n" +
                "超时阈值: " + timeout + " s\n" +
                "====================";
    }
}
