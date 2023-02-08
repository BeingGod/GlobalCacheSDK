package com.example.globalcachesdk.entity;

import java.util.ArrayList;

/**
 * 数据盘分区信息
 * @author 章睿彬
 */
public class DataDiskPartInfo extends AbstractEntity {
    /**
     * 数据盘分区
     */
    public static class Part {
        /**
         * 分区挂载路径
         */
        private String diskName;
        /**
         * 磁盘容量
         */
        private long capacity;
        /**
         * 已用大小
         */
        private long used;
        /**
         * 可用大小
         */
        private long avail;

        public String getDiskName() {
            return diskName;
        }

        public void setDiskName(String diskName) {
            this.diskName = diskName;
        }

        public long getCapacity() {
            return capacity;
        }

        public void setCapacity(long capacity) {
            this.capacity = capacity;
        }

        public long getUsed() {
            return used;
        }

        public void setUsed(long used) {
            this.used = used;
        }

        public long getAvail() {
            return avail;
        }

        public void setAvail(long avail) {
            this.avail = avail;
        }
    }

    /**
     * 分区信息列表
     */
    private ArrayList<Part> partList;

    public int getPartNum() {
        return partList.size();
    }

    public ArrayList<Part> getPartList() {
        return partList;
    }

    public void setPartList(ArrayList<Part> partList) {
        this.partList = partList;
    }
}
