package com.hw.globalcachesdk.entity;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PG信息
 * @author 章睿彬
 */
public class PgInfo extends AbstractEntity {
    /**
     * PG状态
     */
    public enum PgState {
        /**
         *  正常状态，可以服务IO
         */
        PG_STATE_NORMAL,
        /**
         * PG处于切主状态
         */
        PG_STATE_DOWN,
        /**
         *  PG处于降级状态，丢失一个副本
         */
        PG_STATE_DEGRADE_LOSS1,
        /**
         * PG处于降级状态，丢失两个副本
         */
        PG_STATE_DEGRADE_LOSS2,
        /**
         * PG正处于恢复状态
         */
        PG_STATE_RECOVERY,
        /**
         * PG处于故障状态，不能提供IO服务
         */
        PG_STATE_FAULT,
    }

    /**
     * PG副本状态
     */
    public enum CopyState {
        /**
         * 副本处于运行状态
         */
        PG_COPY_STATE_RUNNING,
        /**
         * 副本所在节点或者磁盘处于DOWN状态
         */
        PG_COPY_STATE_DOWN,
        /**
         * 副本所在节点或者磁盘处于OUT状态
         */
        PG_COPY_STATE_OUT,
        /**
         * 副本处于恢复状态
         */
        PG_COPY_STATE_RECOVERY,
    }

    /**
     * PG副本
     */
    public static class PgCopyInfo {

        private int nodeId;

        private int diskId;
        /**
         * 副本状态
         */
        private CopyState state;

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

        public CopyState getState() {
            return state;
        }

        public void setState(CopyState state) {
            this.state = state;
        }

        /*
         * 输入格式: <1, 2, PG_COPY_STATE_RUNNING>
         */
        private static PgInfo.PgCopyInfo parseOf(String str) {
            String[] strList = str.substring(1,str.length()-1).split(",");

            PgInfo.PgCopyInfo pgCopyInfo = new PgInfo.PgCopyInfo();
            pgCopyInfo.setNodeId(Integer.parseInt(strList[0].trim()));
            pgCopyInfo.setDiskId(Integer.parseInt(strList[1].trim()));
            pgCopyInfo.setState(PgInfo.CopyState.valueOf(strList[2].trim()));

            return pgCopyInfo;
        }
    }

    /**
     * PG
     */
    public static class Pg {

        private int pgId;
        /**
         * PG版本号
         */
        private int bv;
        /**
         * 主本所在节点ID
         */
        private int masterNode;
        /**
         * 主本所在磁盘ID
         */
        private int masterDisk;
        /**
         * 副本数量
         */
        private int copyNum;
        /**
         * PG状态
         */
        private PgState state;
        /**
         * 三副本状态
         */
        private ArrayList<PgCopyInfo> copyInfos;

        public int getPgId() {
            return pgId;
        }

        public void setPgId(int pgId) {
            this.pgId = pgId;
        }

        public int getBv() {
            return bv;
        }

        public void setBv(int bv) {
            this.bv = bv;
        }

        public int getMasterNode() {
            return masterNode;
        }

        public void setMasterNode(int masterNode) {
            this.masterNode = masterNode;
        }

        public int getMasterDisk() {
            return masterDisk;
        }

        public void setMasterDisk(int masterDisk) {
            this.masterDisk = masterDisk;
        }

        public int getCopyNum() {
            return copyNum;
        }

        public void setCopyNum(int copyNum) {
            this.copyNum = copyNum;
        }

        public PgState getState() {
            return state;
        }

        public void setState(PgState state) {
            this.state = state;
        }

        public ArrayList<PgCopyInfo> getCopyInfos() {
            return copyInfos;
        }

        public void setCopyInfos(ArrayList<PgCopyInfo> copyInfo) {
            this.copyInfos = copyInfo;
        }
    }

    private static final Pattern PG_COPY_INFO_PATTERN = Pattern.compile("<[0-9]+, [0-9]+, [0-9A-Z_]+>");

    /**
     * 主PG
     */
    ArrayList<Pg> primaryPgView = new ArrayList<>();

    /**
     * 副PG
     */
    ArrayList<Pg> secondaryPgView = new ArrayList<>();

    public ArrayList<Pg> getPrimaryPgView() {
        return primaryPgView;
    }

    public void setPrimaryPgView(ArrayList<Pg> primaryPgView) {
        this.primaryPgView = primaryPgView;
    }

    public ArrayList<Pg> getSecondaryPgView() {
        return secondaryPgView;
    }

    public void setSecondaryPgView(ArrayList<Pg> secondaryPgView) {
        this.secondaryPgView = secondaryPgView;
    }

    public int getPrimaryPgNum() {
        return primaryPgView.size();
    }

    public int getSecondaryPgNum() {
        return secondaryPgView.size();
    }

    /*
     * 输入格式:
     *    1 |     25 |          1 |          2 |        PG_STATE_NORMAL |      3 |[<1, 2, PG_COPY_STATE_RUNNING>, <0, 0, PG_COPY_STATE_RUNNING>, <2, 5, PG_COPY_STATE_RUNNING>, ]
     *    2 |     25 |          2 |          4 |        PG_STATE_NORMAL |      3 |[<2, 4, PG_COPY_STATE_RUNNING>, <0, 1, PG_COPY_STATE_RUNNING>, <1, 2, PG_COPY_STATE_RUNNING>, ]
     *    4 |     25 |          1 |          3 |        PG_STATE_NORMAL |      3 |[<1, 3, PG_COPY_STATE_RUNNING>, <2, 5, PG_COPY_STATE_RUNNING>, <0, 0, PG_COPY_STATE_RUNNING>, ]
     *    5 |     25 |          2 |          5 |        PG_STATE_NORMAL |      3 |[<2, 5, PG_COPY_STATE_RUNNING>, <1, 3, PG_COPY_STATE_RUNNING>, <0, 1, PG_COPY_STATE_RUNNING>, ]
     *  ......
     *  179 |     25 |          2 |          5 |        PG_STATE_NORMAL |      3 |[<2, 5, PG_COPY_STATE_RUNNING>, <1, 3, PG_COPY_STATE_RUNNING>, <0, 1, PG_COPY_STATE_RUNNING>, ]
     */
    public static ArrayList<PgInfo.Pg> parseOf(String str) {
        if (str.equals("")) {
            return null;
        }

        ArrayList<PgInfo.Pg> pgView = new ArrayList<>();

        String[] strList = str.split("\n");
        for (String s : strList) {
            String[] temp = s.split("\\|");
            for (int k = 0; k < temp.length; ++k) {
                temp[k] = temp[k].trim();
            }

            PgInfo.Pg pg = new PgInfo.Pg();
            pg.setPgId(Integer.parseInt(temp[0]));
            pg.setBv(Integer.parseInt(temp[1]));
            pg.setMasterNode(Integer.parseInt(temp[2]));
            pg.setMasterDisk(Integer.parseInt(temp[3]));
            pg.setState(PgInfo.PgState.valueOf(temp[4]));
            pg.setCopyNum(Integer.parseInt(temp[5]));

            ArrayList<PgInfo.PgCopyInfo> copyInfos = new ArrayList<>();

            Matcher matcher = PG_COPY_INFO_PATTERN.matcher(temp[6]);
            while (matcher.find()) {
                copyInfos.add(PgCopyInfo.parseOf(matcher.group(0)));
            }
            pg.setCopyInfos(copyInfos);

            pgView.add(pg);
        }

        return pgView;
    }
}
