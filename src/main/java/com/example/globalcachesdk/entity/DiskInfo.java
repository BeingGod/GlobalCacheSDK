package com.example.globalcachesdk.entity;

import java.util.ArrayList;

/**
 * 磁盘信息
 * @author 章睿彬
 */
public class DiskInfo extends AbstractEntity {
    /**
     * 磁盘类型
     */
    enum DiskType {
        /**
         * SATA硬盘
         */
        ROTA,
        /**
         * NVME硬盘
         */
        NVME,
    }

    public class Disk {
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
     * 节点磁盘数目
     */
    private int diskNum;

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
