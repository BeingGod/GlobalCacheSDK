package com.example.globalcachesdk.entity;

import cn.hutool.core.date.DateTime;

import java.util.ArrayList;

/**
 * 集群异常信息
 * @author 章睿彬
 */
public class ClusterAlarmInfo extends AbstractEntity {
    public enum AlarmLevel {
        /**
         * 错误
         */
        ERROR,
        /**
         * 警告
         */
        WARN,
    }

    public enum AlarmComponent {
        /**
         * 节点异常
         */
        NODE,
        /**
         * 磁盘异常
         */
        DISK,
        /**
         * 网络异常
         */
        NETWORK,
        /**
         * 日志系统异常
         */
        LOGSYSTEM,
    }

    public static class AlarmInfo {
        /**
         * 告警级别
         */
        private AlarmLevel level;

        /**
         * 告警组件
         */
        private AlarmComponent component;

        /**
         * 告警日期
         */
        private DateTime time;

        /**
         * 告警节点
         */
        private ArrayList<String> nodeIpList;

        /**
         * 告警磁盘ID
         */
        private ArrayList<String> diskIpList;

        /**
         * 告警磁盘SN码
         */
        private ArrayList<String> diskSnList;

        /**
         * 错误日志
         */
        private String log;

        public AlarmLevel getLevel() {
            return level;
        }

        public void setLevel(AlarmLevel level) {
            this.level = level;
        }

        public AlarmComponent getComponent() {
            return component;
        }

        public void setComponent(AlarmComponent component) {
            this.component = component;
        }

        public DateTime getTime() {
            return time;
        }

        public void setTime(DateTime time) {
            this.time = time;
        }

        public ArrayList<String> getNodeIpList() {
            return nodeIpList;
        }

        public void setNodeIpList(ArrayList<String> nodeIpList) {
            this.nodeIpList = nodeIpList;
        }

        public ArrayList<String> getDiskIpList() {
            return diskIpList;
        }

        public void setDiskIpList(ArrayList<String> diskIpList) {
            this.diskIpList = diskIpList;
        }

        public ArrayList<String> getDiskSnList() {
            return diskSnList;
        }

        public void setDiskSnList(ArrayList<String> diskSnList) {
            this.diskSnList = diskSnList;
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
