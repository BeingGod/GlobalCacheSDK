package com.hw.globalcachesdk.entity;

/**
 * 节点内存信息
 * @author 章睿彬
 */
public class MemInfo extends AbstractEntity {

    /**
     * 节点总内存 单位Byte
     */
    private long total;

    /**
     * 节点已使用总内存 单位Byte
     */
    private long used;

    /**
     * 节点空闲内存 单位Byte
     */
    private long free;

    /**
     * 节点Cache内存 单位Byte
     */
    private long cache;

    /**
     * 节点可用内存 单位Byte
     */
    private long avail;

    public long getUsed() {
        return used;
    }

    public void setUsed(long used) {
        this.used = used;
    }

    public long getCache() {
        return cache;
    }

    public void setCache(long cache) {
        this.cache = cache;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getAvail() {
        return avail;
    }

    public void setAvail(long avail) {
        this.avail = avail;
    }

    public long getFree() {
        return free;
    }

    public void setFree(long free) {
        this.free = free;
    }
}
