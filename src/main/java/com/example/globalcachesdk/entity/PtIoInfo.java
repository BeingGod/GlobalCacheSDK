package com.example.globalcachesdk.entity;

import java.util.ArrayList;

/**
 * PtIo信息
 * @author 李金泽
 */
public class PtIoInfo extends AbstractEntity {
    /**
     * 总PT数量
     */
    private int ptNum;

    public static class IoInfo{
        /**
         * 总IO个数
         */
        int ioCount;
        /**
         * 读请求个数
         */
        int readCount;
        /**
         * 读请求Size总和
         */
        long readSize;
        /**
         * 写请求个数
         */
        int writeSizeCount;
        /**
         * 写请求Size总和
         */
        long writeSize;

        public int getIoCount() {
            return ioCount;
        }

        public void setIoCount(int ioCount) {
            this.ioCount = ioCount;
        }

        public int getReadCount() {
            return readCount;
        }

        public void setReadCount(int readCount) {
            this.readCount = readCount;
        }

        public long getReadSize() {
            return readSize;
        }

        public void setReadSize(long readSize) {
            this.readSize = readSize;
        }

        public int getWriteSizeCount() {
            return writeSizeCount;
        }

        public void setWriteSizeCount(int writeSizeCount) {
            this.writeSizeCount = writeSizeCount;
        }

        public long getWriteSize() {
            return writeSize;
        }

        public void setWriteSize(long writeSize) {
            this.writeSize = writeSize;
        }
    }

    public static class Pt{
        /**
         * pt唯一标识ID
         */
        int ptId;
        /**
         * 当前Pt Io信息
         */
        IoInfo ioInfo;

        public int getPtId() {
            return ptId;
        }

        public void setPtId(int ptId) {
            this.ptId = ptId;
        }

        public IoInfo getIoInfo() {
            return ioInfo;
        }

        public void setIoInfo(IoInfo ioInfo) {
            this.ioInfo = ioInfo;
        }
    }

    /**
     * Pt列表
     */
    ArrayList<Pt> ptArrayList;

    public int getPtNum() {
        return ptNum;
    }

    public void setPtNum(int ptNum) {
        this.ptNum = ptNum;
    }

    public ArrayList<Pt> getPtArrayList() {
        return ptArrayList;
    }

    public void setPtArrayList(ArrayList<Pt> ptArrayList) {
        this.ptArrayList = ptArrayList;
    }
}
