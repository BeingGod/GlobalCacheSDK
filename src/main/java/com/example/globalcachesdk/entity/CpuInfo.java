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
     * 节点CPU整体占用率
    */
    private double totalUsage;

    /**
     * 节点逻辑核心占用率 单位 百分比
     */
    private ArrayList<Double> coreUsage;
}
