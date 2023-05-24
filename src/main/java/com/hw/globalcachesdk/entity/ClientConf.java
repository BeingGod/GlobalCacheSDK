package com.hw.globalcachesdk.entity;

/**
 * Client节点配置
 * @author 章睿彬
 */
public class ClientConf {
    private String hostname;

    private String networkMask;

    private String remoteIp;

    private String publicIp;

    private String rootPasswd;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getPublicIp() {
        return publicIp;
    }

    public void setPublicIp(String publicIp) {
        this.publicIp = publicIp;
    }

    public String getRootPasswd() {
        return rootPasswd;
    }

    public void setRootPasswd(String rootPasswd) {
        this.rootPasswd = rootPasswd;
    }

    public String getNetworkMask() {
        return networkMask;
    }

    public void setNetworkMask(String networkMask) {
        this.networkMask = networkMask;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public ClientConf(String hostname, String networkMask, String remoteIp, String publicIp, String rootPasswd) {
        this.hostname = hostname;
        this.networkMask = networkMask;
        this.remoteIp = remoteIp;
        this.publicIp = publicIp;
        this.rootPasswd = rootPasswd;
    }
}
