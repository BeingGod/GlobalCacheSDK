package com.hw.globalcachesdk.entity;

/**
 * 集群配置
 * @author 章睿彬
 */
public class ClusterConf {

    private int ptNum;

    private int pgNum;

    private String publicNetwork;

    private String clusterNetwork;

    public String getPublicNetwork() {
        return publicNetwork;
    }

    public void setPublicNetwork(String publicNetwork) {
        this.publicNetwork = publicNetwork;
    }

    public String getClusterNetwork() {
        return clusterNetwork;
    }

    public void setClusterNetwork(String clusterNetwork) {
        this.clusterNetwork = clusterNetwork;
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
}
