package com.example.globalcachesdk.entity;

/**
 * 集群状态信息
 * @author 章睿彬
 */
public class ClusterStatusInfo extends AbstractEntity {
    /**
     * 集群状态
     */
    public enum ClusterStatus {
        /**
         * 集群处于上电状态
         */
        CLUSTER_STATE_STARTING,
        /**
         * 集群处于告警状态
         */
        CLUSTER_STATE_WARN,
        /**
         * 集群处于运行状态
         */
        CLUSTER_STATE_RUNNING,
        /**
         * 集群处于故障状态
         */
        CLUSTER_STATE_ERROR,
        /**
         * 集群处于正常状态
         */
        CLUSTER_STATE_OK,
        /**
         * 集群处于UNKNOWN状态
         */
        CLUSTER_STATE_UNKNOWN,
    }

    /**
     * 集群状态
     */
    private ClusterStatus clusterStatus;

    public ClusterStatus getClusterStatus() {
        return clusterStatus;
    }

    public void setClusterStatus(ClusterStatus clusterStatus) {
        this.clusterStatus = clusterStatus;
    }
}
