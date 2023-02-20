package com.hw.globalcachesdk.entity;

import java.util.ArrayList;

/**
 * 动态网络信息
 * @author 章睿彬
 */
public class DynamicNetInfo extends AbstractEntity {
    /**
     * 网卡信息
     */
    public static class InterfaceInfo {
        /**
         * 网卡名称
         */
        private String name;

        /**
         * 接收数据包速率
         */
        private int rxpcks;

        /**
         * 发送数据包速率
         */
        private int txpcks;

        /**
         * 接受数据速率 kB/s
         */
        private float rxkBs;

        /**
         * 发送数据速率 kB/s
         */
        private float txkBs;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public float getRxkBs() {
            return rxkBs;
        }

        public void setRxkBs(float rxkBs) {
            this.rxkBs = rxkBs;
        }

        public float getTxkBs() {
            return txkBs;
        }

        public void setTxkBs(float txkBs) {
            this.txkBs = txkBs;
        }

        public int getRxpcks() {
            return rxpcks;
        }

        public void setRxpcks(int rxpcks) {
            this.rxpcks = rxpcks;
        }

        public int getTxpcks() {
            return txpcks;
        }

        public void setTxpcks(int txpcks) {
            this.txpcks = txpcks;
        }
    }

    /**
     * 网卡动态信息
     */
    ArrayList<InterfaceInfo> interfaceInfos;

    public ArrayList<InterfaceInfo> getInterfaceInfos() {
        return interfaceInfos;
    }

    public void setInterfaceInfos(ArrayList<InterfaceInfo> interfaceInfos) {
        this.interfaceInfos = interfaceInfos;
    }
}
