package com.hw.globalcachesdk.pool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 异步Shell结果读取线程命令
 * @author 章睿彬
 */
public class AsyncThread extends Thread {

    /**
     * 字符串缓冲区
     */
    private BufferedReader reader = null;

    /**
     * 执行结果字符串队列
     */
    private BlockingQueue<String> stringQueue = null;

    /**
     * 当前线程是否执行完毕
     */
    private AsyncFinishFlag flag = null;

    /**
     * 读取字数串队列为空的线程休眠时间
     * 单位: ms
     */
    private int sleepTime = 10;

    public AsyncThread(InputStream inputStream, AsyncFinishFlag flag) {
        reader = new BufferedReader(new InputStreamReader(inputStream, Charset.defaultCharset()));
        stringQueue = new LinkedBlockingQueue<>();
        this.flag = flag;
    }

    @Override
    public void run() {
        String line;
        while (true) {
            try {
                if ((line = reader.readLine()) == null) {
                    break;
                } else {
                    // 将当前读取到的行入队
                    stringQueue.put(line);
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        // 标记线程已完成
        flag.finish();
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public synchronized void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public String readLine() throws InterruptedException {
        if (flag.isFinished() && stringQueue.size() == 0) {
            // 线程结束 && 所有数据已从缓冲区中取出 -> 返回null
            return null;
        }

        while (stringQueue.size() == 0) {
            if (flag.isFinished()) {
                return null;
            }
            // 当前缓冲区中没有结果 && 线程未完成 -> 休眠当前线程
            sleep(sleepTime);
        }

        String retVal = stringQueue.peek();
        // 将队列首元素出队（阻塞调用）
        stringQueue.take();
        
        return retVal;
    }
}
