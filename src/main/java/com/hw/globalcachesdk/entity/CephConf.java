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

    private String remoteIp;

    private String localIp;

    private String publicIp;

    private String clusterIp;

    private String publicNetworkMask;

    private String rootPasswd;

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
        return publicNetworkMask;
    }

    public void setNetworkMask(String publicNetworkMask) {
        this.publicNetworkMask = publicNetworkMask;
    }

    public String getRootPasswd() {
        return rootPasswd;
    }

    public void setRootPasswd(String rootPasswd) {
        this.rootPasswd = rootPasswd;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public CephConf(String hostname, int nodeNumber, boolean isZkServer, boolean isNtpServer, boolean isCcmMonitor, String remoteIp, String localIp, String publicIp, String clusterIp, String publicNetworkMask, String rootPasswd, ArrayList<String> dataDiskList, ArrayList<String> cacheDiskList) {
        this.hostname = hostname;
        this.nodeNumber = nodeNumber;
        this.isZkServer = isZkServer;
        this.isNtpServer = isNtpServer;
        this.isCcmMonitor = isCcmMonitor;
        this.remoteIp = remoteIp;
        this.localIp = localIp;
        this.publicIp = publicIp;
        this.clusterIp = clusterIp;
        this.publicNetworkMask = publicNetworkMask;
        this.rootPasswd = rootPasswd;
        this.dataDiskList = dataDiskList;
        this.cacheDiskList = cacheDiskList;
    }
}
