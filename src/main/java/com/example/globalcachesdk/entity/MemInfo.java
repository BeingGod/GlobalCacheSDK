package com.example.globalcachesdk.entity;

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
     * 节点可用内存 单位Byte
     */
    private long avail;

    /**
     * 节点空闲内存 单位Byte
     */
    private long free;

    /**
     * @return 总内存大小，以Byte为单位
     */
    public long getTotal() {
        return total;
    }

    /**
     * @param total 总内存大小，以Byte为单位
     */
    public void setTotal(long total) {
        this.total = total;
    }

    /**
     * @return 可用内存大小，以Byte为单位
     */
    public long getAvail() {
        return avail;
    }

    /**
     * @param avail 可用内存大小，以Byte为单位
     */
    public void setAvail(long avail) {
        this.avail = avail;
    }

    /**
     * @return 空闲内存大小，以Byte为单位
     */
    public long getFree() {
        return free;
    }

    /**
     * @param free 空闲内存大小，以Byte为单位
     */
    public void setFree(long free) {
        this.free = free;
    }
}
