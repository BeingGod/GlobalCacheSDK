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

    /**
     * @return CPU核心整体占用率，以百分比为单位
     */
    public double getTotalUsage() {
        return totalUsage;
    }

    /**
     * @param totalUsage CPU核心整体占用率，以百分比为单位
     */
    public void setTotalUsage(double totalUsage) {
        this.totalUsage = totalUsage;
    }

    /**
     * @return CPU所有逻辑核心占用率，以百分比为单位
     */
    public ArrayList<Double> getCoreUsage() {
        return coreUsage;
    }

    /**
     * @param coreUsage CPU所有逻辑核心占用率，以百分比为单位
     */
    public void setCoreUsage(ArrayList<Double> coreUsage) {
        this.coreUsage = coreUsage;
    }
}
