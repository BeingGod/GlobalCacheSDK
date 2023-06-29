package com.hw.globalcachesdk.entity;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static final Pattern PT_MAP_INFO_PATTERN = Pattern.compile("[0-9]+, [0-9]+, [0-9]+");

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

    public static PtInfo parseOf(String str) {
        if (str.equals("")) {
            return null;
        }

        String[] strList = str.split("\n");

        PtInfo ptInfo = new PtInfo();
        ArrayList<PtInfo.Pt> ptList = new ArrayList<>();
        for (String s : strList) {
            String[] temp = s.split("\\|");
            for (int k = 0; k < temp.length; ++k) {
                temp[k] = temp[k].trim();
            }

            PtInfo.Pt pt = new PtInfo.Pt();
            pt.setPtId(Integer.parseInt(temp[0]));
            pt.setBv(Integer.parseInt(temp[1]));
            pt.setState(PtInfo.PtState.valueOf(temp[2]));
            pt.setIndexInNode(Integer.parseInt(temp[3]));

            Matcher matcher = PT_MAP_INFO_PATTERN.matcher(temp[4]);

            // 主PT
            PtInfo.PtMapInfo ptMapInfo = new PtInfo.PtMapInfo();
            if (matcher.find()) {
                String[] ptMapInfoList = matcher.group(0).split(",");
                ptMapInfo.setNodeId(Integer.parseInt(ptMapInfoList[0].trim()));
                ptMapInfo.setDiskId(Integer.parseInt(ptMapInfoList[1].trim()));
                ptMapInfo.setVnodeId(Integer.parseInt(ptMapInfoList[2].trim()));
            }
            pt.setPtMapInfo(ptMapInfo);

            // 备份PT
            PtInfo.PtMapInfo backupPtMapInfo = new PtInfo.PtMapInfo();
            if (matcher.find()) {
                String[] ptMapInfoList = matcher.group(0).split(",");
                backupPtMapInfo.setNodeId(Integer.parseInt(ptMapInfoList[0].trim()));
                backupPtMapInfo.setDiskId(Integer.parseInt(ptMapInfoList[1].trim()));
                backupPtMapInfo.setVnodeId(Integer.parseInt(ptMapInfoList[2].trim()));
            }
            pt.setBackupPtMapInfo(backupPtMapInfo);

            ptList.add(pt);
        }

        ptInfo.setPtList(ptList);

        return ptInfo;
    }
}
