package com.hw.globalcachesdk.entity;

import java.util.ArrayList;

/**
 * 静态网络信息
 * @author 蔡润培
 */
public class StaticNetInfo extends AbstractEntity {
    /**
     * 网卡信息
     */
    public static class InterfaceInfo {
        /**
         * 网卡名称
         */
        private String name;
        /**
         * 网卡IPV4地址
         * 注意: 如果网卡没有IPV4地址, 该值为""
         */
        private String ipv4;
        /**
         * 网卡IPV6地址
         * 注意: 如果网卡没有IPV6地址, 该值为""
         */
        private String ipv6;
        /**
         * 网卡MAC地址
         */
        private String mac;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIpv4() {
            return ipv4;
        }

        public void setIpv4(String ipv4) {
            this.ipv4 = ipv4;
        }

        public String getIpv6() {
            return ipv6;
        }

        public void setIpv6(String ipv6) {
            this.ipv6 = ipv6;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }
    }

    private ArrayList<InterfaceInfo> interfaceInfoList;

    public ArrayList<InterfaceInfo> getInterfaceInfoList() {
        return interfaceInfoList;
    }

    public void setInterfaceInfoList(ArrayList<InterfaceInfo> interfaceInfoList) {
        this.interfaceInfoList = interfaceInfoList;
    }

    public int getInterfaceNum() {
        return interfaceInfoList.size();
    }

}
