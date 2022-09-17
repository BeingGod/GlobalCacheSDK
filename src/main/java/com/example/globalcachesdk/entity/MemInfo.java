package com.example.globalcachesdk.entity;

import lombok.Data;

/**
 * 节点内存信息
 * @author ya059
 */
@Data
public class MemInfo {
    /**
     * 节点总内存 单位B
     */
    private long total;

    /**
     * 节点可用内存 单位B
     */
    private long avail;

    /**
     * 节点空闲内存 单位B
     */
    private long free;
}
