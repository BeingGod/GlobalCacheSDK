package com.example.globalcachesdk.entity;

import cn.hutool.core.date.DateTime;

import java.util.ArrayList;

/**
 * 集群异常信息
 * @author 章睿彬
 */
public class ClusterAlarmInfo extends AbstractEntity {


    public static class AlarmInfo {

        /**
         * 告警序号
         */
        private int alarmNumber;

        /**
         * 告警日期
         */
        private DateTime time;

        /**
         * 告警节点
         */
        private String nodeIp;

        /**
         * 告警磁盘ID
         */
        private String diskIp;

        /**
         * 告警磁盘SN码
         */
        private String diskSn;

        /**
         * 错误日志
         */
        private String log;

        public DateTime getTime() {
            return time;
        }

        public void setTime(DateTime time) {
            this.time = time;
        }

        public String getNodeIp() {
            return nodeIp;
        }

        public void setNodeIp(String nodeIp) {
            this.nodeIp = nodeIp;
        }

        public String getDiskIp() {
            return diskIp;
        }

        public void setDiskIp(String diskIp) {
            this.diskIp = diskIp;
        }

        public String getDiskSn() {
            return diskSn;
        }

        public void setDiskSn(String diskSn) {
            this.diskSn = diskSn;
        }

        public int getAlarmNumber() {
            return alarmNumber;
        }

        public void setAlarmNumber(int alarmNumber) {
            this.alarmNumber = alarmNumber;
        }

        public String getLog() {
            return log;
        }

        public void setLog(String log) {
            this.log = log;
        }
    }

    /**
     * 告警信息列表
     */
    private ArrayList<AlarmInfo> alarmInfoList;

    public ArrayList<AlarmInfo> getAlarmInfoList() {
        return alarmInfoList;
    }

    public void setAlarmInfoList(ArrayList<AlarmInfo> alarmInfoList) {
        this.alarmInfoList = alarmInfoList;
    }
}
