package com.hw.globalcachesdk.entity;

/**
 * 节点名称信息
 * @author 章睿彬
 */
public class HostNameInfo extends AbstractEntity {

    /**
     * 节点类型
     */
    public enum HostType {
        // ceph1类型
        CEPH1,
        // ceph类型
        CEPH,
        // client类型
        CLIENT,
        // 未知类型
        UNKNOWN,
    }

    String hostname;

    HostType hostType;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public HostType getHostType() {
        return hostType;
    }

    public void setHostType(HostType hostType) {
        this.hostType = hostType;
    }
}