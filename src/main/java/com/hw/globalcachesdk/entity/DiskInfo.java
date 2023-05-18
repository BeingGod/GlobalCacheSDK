package com.hw.globalcachesdk.entity;

import java.util.ArrayList;

/**
 * 磁盘信息
 * @author 章睿彬
 */
public class DiskInfo extends AbstractEntity {
    /**
     * 磁盘类型
     */
    public enum DiskType {
        /**
         * ROTA硬盘
         */
        ROTA,
        /**
         * NVME硬盘
         */
        NVME,
        /**
         * 未知磁盘类型
         */
        UNKNOWN,
    }

    /**
     * 磁盘
     */
    public static class Disk {
        /**
         * 设备名称
         */
        private String name;
        /**
         * 设备类型
         */
        private DiskType type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public DiskType getType() {
            return type;
        }

        public void setType(DiskType type) {
            this.type = type;
        }
    }

    /**
     * 磁盘信息列表
     */
    private ArrayList<Disk> disksList;

    public int getDiskNum() {
        return disksList.size();
    }

    public ArrayList<Disk> getDisksList() {
        return disksList;
    }

    public void setDisksList(ArrayList<Disk> disksList) {
        this.disksList = disksList;
    }
}
