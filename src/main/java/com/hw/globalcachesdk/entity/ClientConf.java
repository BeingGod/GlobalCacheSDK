package com.hw.globalcachesdk.entity;

/**
 * Client节点配置
 * @author 章睿彬
 */
public class ClientConf {
    private String hostname;

    private String networkMask;

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
}
