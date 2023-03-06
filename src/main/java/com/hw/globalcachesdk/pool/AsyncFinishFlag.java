package com.hw.globalcachesdk.pool;

/**
 * 异步线程结束标志
 * @author 章睿彬
 */
public class AsyncFinishFlag {
    private volatile boolean finished = false;

    public boolean isFinished() {
        return finished;
    }

    public void finish() {
        this.finished = true;
    }
}
