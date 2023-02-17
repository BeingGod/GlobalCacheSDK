package com.hw.globalcachesdk.entity;

/**
 * 上线时间
 * @author 章睿彬
 */
public class UptimeInfo extends AbstractEntity {
    /**
     * 上线时间 单位:分钟
     */
    private long uptime;

    public long getUptime() {
        return uptime;
    }

    public void setUptime(long uptime) {
        this.uptime = uptime;
    }
}
