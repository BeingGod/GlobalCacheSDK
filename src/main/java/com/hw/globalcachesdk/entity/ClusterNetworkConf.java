package com.hw.globalcachesdk.entity;

/**
 * 集群网络配置
 * @author 章睿彬
 */
public class ClusterNetworkConf {

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
}
