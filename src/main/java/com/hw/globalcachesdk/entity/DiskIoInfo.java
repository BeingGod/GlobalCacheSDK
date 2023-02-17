package com.hw.globalcachesdk.entity;

import java.util.ArrayList;

/**
 * 节点磁盘IO信息
 * @author 李金泽
 */
public class DiskIoInfo extends AbstractEntity {

    /**
     * 本节点全部磁盘IO状态
     */
    ArrayList<Disk> diskArrayList;

    public static class Disk{
        /**
         * 磁盘名称
         */
        String diskName;

        /**
         * 该设备每秒的传输次数
         */
        float tps;
        /**
         * 每秒从设备（drive expressed）读取的数据量；
         */
        float kbRead;
        /**
         * 每秒向设备（drive expressed）写入的数据量；kB/s
         */
        float kbWrite;
        /**
         * 读取的总数据量；kB/s
         */
        String kbReadTotal;
        /**
         * 写入的总数量数据量；kB/s
         */
        String kbWrittenTotal;

        public String getDiskName() {
            return diskName;
        }

        public void setDiskName(String diskName) {
            this.diskName = diskName;
        }

        public float getTps() {
            return tps;
        }

        public void setTps(float tps) {
            this.tps = tps;
        }

        public float getKbRead() {
            return kbRead;
        }

        public void setKbRead(float kbRead) {
            this.kbRead = kbRead;
        }

        public float getKbWrite() {
            return kbWrite;
        }

        public void setKbWrite(float kbWrite) {
            this.kbWrite = kbWrite;
        }

        public String getKbReadTotal() {
            return kbReadTotal;
        }

        public void setKbReadTotal(String kbReadTotal) {
            this.kbReadTotal = kbReadTotal;
        }

        public String getKbWrittenTotal() {
            return kbWrittenTotal;
        }

        public void setKbWrittenTotal(String kbWrittenTotal) {
            this.kbWrittenTotal = kbWrittenTotal;
        }
    }

    public ArrayList<Disk> getDiskArrayList() {
        return diskArrayList;
    }

    public void setDiskArrayList(ArrayList<Disk> diskArrayList) {
        this.diskArrayList = diskArrayList;
    }
}
