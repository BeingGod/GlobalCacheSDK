package com.example.globalcachesdk.entity;

import lombok.Data;

import java.util.ArrayList;

/**
 * 节点CPU信息
 * @author RP Cai
 */
@Data
public class CpuInfo {
    /**
     * 节点CPU占用率 单位 百分比
     * ArrayList第零号数据为整体占用情况 其他为各核心
    */
    private ArrayList<Double> usage;

    public CpuInfo() {
        usage = new ArrayList<Double>();
    }

    public void setUsage(double idle) {
        (this.usage).add(100.00 - idle);
    }
}
