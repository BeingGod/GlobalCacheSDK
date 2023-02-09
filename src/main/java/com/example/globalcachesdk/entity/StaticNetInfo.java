package com.example.globalcachesdk.entity;

import java.util.ArrayList;

public class StaticNetInfo extends AbstractEntity {
    public static class InterfaceInfo {
        private String name;
        private String ipv4;
        private String ipv6;
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
