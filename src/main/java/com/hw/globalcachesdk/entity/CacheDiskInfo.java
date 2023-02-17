package com.hw.globalcachesdk.entity;

import java.util.ArrayList;

/**
 * 缓存盘信息
 * @author 章睿彬
 */
public class CacheDiskInfo extends AbstractEntity {
    /**
     * 缓存盘状态
     */
    public enum CacheDiskState {
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
     * 缓存盘
     */
    public static class CacheDisk {
        /**
         * 磁盘分区唯一标识Id
         */
        private int diskId;
        /**
         * 磁盘分区Sn号
         */
        private String diskSn;
        /**
         * 磁盘容量，单位:MB
         */
        private long capacity;
        /**
         * 磁盘状态
         */
        private CacheDiskState state;

        public int getDiskId() {
            return diskId;
        }

        public void setDiskId(int diskId) {
            this.diskId = diskId;
        }

        public String getDiskSn() {
            return diskSn;
        }

        public void setDiskSn(String diskSn) {
            this.diskSn = diskSn;
        }

        public long getCapacity() {
            return capacity;
        }

        public void setCapacity(long capacity) {
            this.capacity = capacity;
        }

        public CacheDiskState getState() {
            return state;
        }

        public void setState(CacheDiskState state) {
            this.state = state;
        }
    }

    /**
     * 磁盘信息列表
     */
    private ArrayList<CacheDisk> diskList;

    public int getDiskNum() {
        return diskList.size();
    }

    public ArrayList<CacheDisk> getDiskList() {
        return diskList;
    }

    public void setDiskList(ArrayList<CacheDisk> diskList) {
        this.diskList = diskList;
    }
}
