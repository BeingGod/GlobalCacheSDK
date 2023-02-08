package com.example.globalcachesdk.entity;

import java.util.ArrayList;

/**
 * 节点状态信息
 * @author 章睿彬
 */
public class NodeStatusInfo extends AbstractEntity {
    /**
     * 节点状态
     */
    public enum NodeState {
        /**
         * 非法状态
         */
        NODE_STATE_INVALID,
        /**
         * 节点正在启动
         */
        NODE_STATE_UP,
        /**
         * 节点下线
         */
        NODE_STATE_DOWN,
        /**
         * 节点正在工作
         */
        NODE_STATE_RUNNING,
        /**
         * 节点在集群中
         */
        NODE_STATE_IN,
        /**
         * 节点不在集群中
         */
        NODE_STATE_OUT,
    }

    /**
     * 磁盘状态
     */
    public enum DiskState {
        /**
         * 可服务IO状态
         */
        VDISK_STATE_UP,
        /**
         * 不可服务IO状态
         */
        VDISK_STATE_DOWN,
    }

    /**
     * 磁盘
     */
    public static class Disk {
        /**
         * 磁盘ID
         */
        private int diskId;
        /**
         * 磁盘名称
         */
        private String diskName;
        /**
         * 磁盘SN号
         */
        private String diskSn;
        /**
         * 磁盘容量，单位：Mb
         */
        private int capacity;
        /**
         * 磁盘状态
         */
        private DiskState state;

        public int getDiskId() {
            return diskId;
        }

        public void setDiskId(int diskId) {
            this.diskId = diskId;
        }

        public String getDiskName() {
            return diskName;
        }

        public void setDiskName(String diskName) {
            this.diskName = diskName;
        }

        public String getDiskSn() {
            return diskSn;
        }

        public void setDiskSn(String diskSn) {
            this.diskSn = diskSn;
        }

        public int getCapacity() {
            return capacity;
        }

        public void setCapacity(int capacity) {
            this.capacity = capacity;
        }

        public DiskState getState() {
            return state;
        }

        public void setState(DiskState state) {
            this.state = state;
        }

    }

    /**
     * 节点
     */
    public static class Node {
        /**
         * 节点ID
         */
        private int nodeId;
        /**
         * 节点状态集合
         * 一个节点可能有多种状态
         */
        private ArrayList<NodeState> stateList;
        /**
         * 磁盘数
         */
        private int diskNum;
        /**
         * 集群公开IP
         */
        private String publicIp;
        /**
         * 集群内网IP
         */
        private String clusterIp;
        /**
         * 当前节点缓存盘列表
         */
        private ArrayList<Disk> disks;

        public int getNodeId() {
            return nodeId;
        }

        public void setNodeId(int nodeId) {
            this.nodeId = nodeId;
        }

        public ArrayList<NodeState> getStateList() {
            return stateList;
        }

        public void setStateList(ArrayList<NodeState> stateList) {
            this.stateList = stateList;
        }

        public int getDiskNum() {
            return diskNum;
        }

        public void setDiskNum(int diskNum) {
            this.diskNum = diskNum;
        }

        public String getPublicIp() {
            return publicIp;
        }

        public ArrayList<Disk> getDisks() {
            return disks;
        }

        public void setDisks(ArrayList<Disk> disks) {
            this.disks = disks;
        }

        public void setPublicIp(String publicIp) {
            this.publicIp = publicIp;
        }

        public String getClusterIp() {
            return clusterIp;
        }

        public void setClusterIp(String clusterIp) {
            this.clusterIp = clusterIp;
        }
    }

    /**
     * 当前节点数
     */
    private int currentNodeNum;

    /**
     * 配置节点数
     */
    private int configNodeNum;

    /**
     * 节点信息列表
     */
    private ArrayList<Node> nodeList;

    public int getCurrentNodeNum() {
        return currentNodeNum;
    }

    public void setCurrentNodeNum(int currentNodeNum) {
        this.currentNodeNum = currentNodeNum;
    }

    public int getConfigNodeNum() {
        return configNodeNum;
    }

    public void setConfigNodeNum(int configNodeNum) {
        this.configNodeNum = configNodeNum;
    }

    public ArrayList<Node> getNodeList() {
        return nodeList;
    }

    public void setNodeList(ArrayList<Node> nodeList) {
        this.nodeList = nodeList;
    }
}
