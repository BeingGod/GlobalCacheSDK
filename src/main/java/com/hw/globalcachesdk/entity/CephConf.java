package com.hw.globalcachesdk.entity;

import java.util.ArrayList;

/**
 * Ceph节点配置信息
 * @author 章睿彬
 */
public class CephConf {
    private String hostname;

    private int nodeNumber;

    private boolean isZkServer;

    private boolean isNtpServer;

    private boolean isCcmMonitor;

    private String localIp;

    private String publicIp;

    private String clusterIp;

    private String networkMask;

    private String rootPasswd;

    private int ptNum;

    private int pgNum;

    private ArrayList<String> dataDiskList;

    private ArrayList<String> cacheDiskList;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getNodeNumber() {
        return nodeNumber;
    }

    public void setNodeNumber(int nodeNumber) {
        this.nodeNumber = nodeNumber;
    }

    public boolean isZkServer() {
        return isZkServer;
    }

    public void setZkServer(boolean zkServer) {
        isZkServer = zkServer;
    }

    public boolean isCcmMonitor() {
        return isCcmMonitor;
    }

    public void setCcmMonitor(boolean ccmMonitor) {
        isCcmMonitor = ccmMonitor;
    }

    public String getLocalIp() {
        return localIp;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }

    public String getPublicIp() {
        return publicIp;
    }

    public void setPublicIp(String publicIp) {
        this.publicIp = publicIp;
    }

    public String getClusterIp() {
        return clusterIp;
    }

    public void setClusterIp(String clusterIp) {
        this.clusterIp = clusterIp;
    }

    public int getPtNum() {
        return ptNum;
    }

    public void setPtNum(int ptNum) {
        this.ptNum = ptNum;
    }

    public int getPgNum() {
        return pgNum;
    }

    public void setPgNum(int pgNum) {
        this.pgNum = pgNum;
    }

    public ArrayList<String> getDataDiskList() {
        return dataDiskList;
    }

    public void setDataDiskList(ArrayList<String> dataDiskList) {
        this.dataDiskList = dataDiskList;
    }

    public ArrayList<String> getCacheDiskList() {
        return cacheDiskList;
    }

    public void setCacheDiskList(ArrayList<String> cacheDiskList) {
        this.cacheDiskList = cacheDiskList;
    }

    public boolean isNtpServer() {
        return isNtpServer;
    }

    public void setNtpServer(boolean ntpServer) {
        isNtpServer = ntpServer;
    }

    public String getNetworkMask() {
        return networkMask;
    }

    public void setNetworkMask(String networkMask) {
        this.networkMask = networkMask;
    }

    public String getRootPasswd() {
        return rootPasswd;
    }

    public void setRootPasswd(String rootPasswd) {
        this.rootPasswd = rootPasswd;
    }
}
