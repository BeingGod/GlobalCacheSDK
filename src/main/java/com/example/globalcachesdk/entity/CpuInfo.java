package com.example.globalcachesdk.entity;

import lombok.Data;

@Data
public class CpuInfo {
    private double[] usage;

    public CpuInfo() {
        usage = new double[129];
    }

    public void setUsage(int idx, double idle) {
        (this.usage)[idx] = 100.00 - idle;
    }
}
