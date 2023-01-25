package com.example.globalcachesdk.entity;

import java.util.ArrayList;

/**
 * 节点CPU信息
 * @author 蔡润培
 */
public class CpuInfo extends AbstractEntity {

    /**
     * 节点CPU整体占用率
    */
    private double totalUsage;

    /**
     * 节点逻辑核心占用率 单位 百分比
     */
    private ArrayList<Double> coreUsage;

    public double getTotalUsage() {
        return totalUsage;
    }

    public void setTotalUsage(double totalUsage) {
        this.totalUsage = totalUsage;
    }

    public ArrayList<Double> getCoreUsage() {
        return coreUsage;
    }

    public void setCoreUsage(ArrayList<Double> coreUsage) {
        this.coreUsage = coreUsage;
    }
}
