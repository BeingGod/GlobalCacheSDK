package com.example.globalcachesdk.entity;

import java.util.ArrayList;

/**
 * PT信息
 * @author 章睿彬
 */
public class PtInfo extends AbstractEntity {
    /**
     * PT状态
     */
    public enum PtState {
        /**
         * PT处于初始化状态
         */
        PT_STATE_INIT,
        /**
         * PT处于OK状态，可以服务IO
         */
        PT_STATE_OK,
        /**
         * PT处于迁出状态
         */
        PT_STATE_TRIM,
        /**
         * PT处于迁入状态
         */
        PT_STATE_REPLAY,
        /**
         * PT处于故障状态
         */
        PT_STATE_FAULT,
    }

    /**
     * PT映射组
     */
    public static class PtMapInfo {
        private int nodeId;
        private int diskId;
        private int vnodeId;

        public int getNodeId() {
            return nodeId;
        }

        public void setNodeId(int nodeId) {
            this.nodeId = nodeId;
        }

        public int getDiskId() {
            return diskId;
        }

        public void setDiskId(int diskId) {
            this.diskId = diskId;
        }

        public int getVnodeId() {
            return vnodeId;
        }

        public void setVnodeId(int vnodeId) {
            this.vnodeId = vnodeId;
        }
    }

    /**
     * PT
     */
    public static class Pt {

        private int ptId;

        /**
         * PT版本号
         */
        private int bv;

        /**
         * PT状态
         */
        private PtState state;

        /**
         * pt在对应节点上的映射索引
         */
        private int indexInNode;

        /**
         * PT映射组
         */
        private PtMapInfo ptMapInfo;

        /**
         * 备用PT映射组
         */
        private PtMapInfo backupPtMapInfo;

        public int getPtId() {
            return ptId;
        }

        public void setPtId(int ptId) {
            this.ptId = ptId;
        }

        public int getBv() {
            return bv;
        }

        public void setBv(int bv) {
            this.bv = bv;
        }

        public PtState getState() {
            return state;
        }

        public void setState(PtState state) {
            this.state = state;
        }

        public int getIndexInNode() {
            return indexInNode;
        }

        public void setIndexInNode(int indexInNode) {
            this.indexInNode = indexInNode;
        }

        public PtMapInfo getPtMapInfo() {
            return ptMapInfo;
        }

        public void setPtMapInfo(PtMapInfo ptMapInfo) {
            this.ptMapInfo = ptMapInfo;
        }

        public PtMapInfo getBackupPtMapInfo() {
            return backupPtMapInfo;
        }

        public void setBackupPtMapInfo(PtMapInfo backupPtMapInfo) {
            this.backupPtMapInfo = backupPtMapInfo;
        }
    }

    /**
     * PT信息集合
     */
    private ArrayList<Pt> ptList;

    public ArrayList<Pt> getPtList() {
        return ptList;
    }

    public void setPtList(ArrayList<Pt> ptList) {
        this.ptList = ptList;
    }
}
