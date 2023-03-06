package com.hw.globalcachesdk.entity;

import com.hw.globalcachesdk.exception.AsyncThreadException;
import com.hw.globalcachesdk.pool.AsyncFinishFlag;
import com.hw.globalcachesdk.pool.AsyncThread;
import com.jcraft.jsch.Channel;

import java.io.IOException;
import java.io.InputStream;

/**
 * 异步命令实体
 * @author 章睿彬
 */
public class AsyncEntity extends AbstractEntity {

    private AsyncThread thread = null;

    private Channel channel = null;

    /**
     * @param inputStream Shell结果输入流
     */
    public AsyncEntity(InputStream inputStream, Channel channel) {
        AsyncFinishFlag flag = new AsyncFinishFlag();
        thread = new AsyncThread(inputStream, flag);
        thread.start();
        this.channel = channel;
    }

    /**
     * 等待结果读取完毕
     * 调用该接口会阻塞当前线程, 直到异步线程从输出流中读取完全部结果
     * @return true/调用成功 false/调用失败
     */
    public boolean waitFinish() {
        try {
            // 阻塞当前线程，直至所有结果读取完毕
            thread.join();
            // 关闭缓冲区
            thread.getReader().close();
            if (!channel.isClosed()) {
                // 关闭channel
                channel.disconnect();
            }
            return true;
        } catch (InterruptedException | IOException e) {
            return false;
        }
    }

    /**
     * 读取一行输出, 当缓存区读取完毕时, 返回null
     * @return 输出字符串/null
     * @throws AsyncThreadException 线程中断时抛出此异常
     */
    public String readLine() throws AsyncThreadException {
        try {
            return thread.readLine();
        } catch (InterruptedException e) {
            throw new AsyncThreadException("线程中断", e);
        }
    }

    /**
     * 读取多行输出并将其进行拼接
     * @param num 需要读取的行数
     * @return 输出字符串/null
     * @throws AsyncThreadException 线程中断时抛出此异常
     */
    public String readLines(int num) throws AsyncThreadException {
        StringBuilder stringBuilder = new StringBuilder("");
        while (num != 0) {
            if (this.readLine() == null) {
                // 读取完毕
                break;
            }
            stringBuilder.append(this.readLine()).append("\n");
            num -= 1;
        }
        return stringBuilder.toString();
    }
}