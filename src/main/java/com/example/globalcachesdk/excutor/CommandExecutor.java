package com.example.globalcachesdk.excutor;

import cn.hutool.extra.ssh.JschUtil;
import com.example.globalcachesdk.entity.MemInfo;
import com.example.globalcachesdk.entity.CpuInfo;
import com.example.globalcachesdk.exception.CommandExecFailedException;
import com.jcraft.jsch.Session;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Shell命令调用
 * @author 章睿彬
 */
public class CommandExecutor {

    /**
     * 节点内存信息正则表达式
     */
    private static final Pattern MEM_INFO_PATTERN = Pattern.compile("\\d+");

    /**
     * 节点CPU信息正则表达式
     */
    private static final Pattern CPU_INFO_PATTERN = Pattern.compile("\\d+\\.\\d+");

    /**
     * 执行命令
     * 通过自定义错误流判断shell脚本是否执行成功
     *
     * @param sshSession SSH会话
     * @param command 需要执行的命令
     * @return 命令执行结果
     * @throws CommandExecFailedException 命令执行失败抛出此异常
     */
    private static String exec(Session sshSession, String command) throws CommandExecFailedException {
        try {
            int[] flag = {1};
            OutputStream newErrStream = new OutputStream() {
                @Override
                public void write(int b) {
                    //TODO：本处输出为ASCII强转实现，UTF-8适配
                    System.err.print((char) b);
                }
                @Override
                public void write(byte[] b, int off, int len) {
                    flag[0] = 0;
                    StringBuilder errMsg= new StringBuilder();
                    for (int i = 0; i < len; i++) {
                        errMsg.append((char) b[off + i]);
                    }
                    System.err.print(errMsg);
                }
            };
            String returnValue = JschUtil.exec(sshSession, command, Charset.defaultCharset(), newErrStream);
            if (flag[0] == 0) {
                throw new CommandExecFailedException("命令执行失败");
            } else {
                return returnValue;
            }
        } catch (CommandExecFailedException e) {
            throw new CommandExecFailedException("命令执行失败");
        }
    }

    /**
     * 请求节点内存信息
     *
     * @param sshSession ssh会话
     * @return 内存信息
     * @throws CommandExecFailedException 命令执行失败异常
     */
    public static MemInfo queryMemInfo(Session sshSession) throws CommandExecFailedException {
        String command = "sh /root/scripts/mem_info.sh";

        String returnValue = exec(sshSession, command);

        Matcher matcher = MEM_INFO_PATTERN.matcher(returnValue);
        MemInfo memInfo = new MemInfo();
        if (matcher.find()) {
            memInfo.setTotal(Long.parseLong(matcher.group(0)));
        }
        if (matcher.find()) {
            memInfo.setFree(Long.parseLong(matcher.group(0)));
        }
        if (matcher.find()) {
            memInfo.setAvail(Long.parseLong(matcher.group(0)));
        }

        return memInfo;
    }

    /**
     * 请求节点CPU信息
     *
     * @param sshSession ssh会话
     * @return CPU信息
     * @throws CommandExecFailedException 命令执行失败抛出此异常
     */
    public static CpuInfo queryCpuInfo(Session sshSession) throws CommandExecFailedException {
        String command = "sh /home/globalcachesdk/scripts/cpu_usage.sh";

        String returnValue = exec(sshSession, command);

        Matcher matcher = CPU_INFO_PATTERN.matcher(returnValue);
        CpuInfo cpuInfo = new CpuInfo();

        // 整体核心利用率
        if (matcher.find()) {
            cpuInfo.setTotalUsage(100 - Double.parseDouble(matcher.group(0)));
        }

        // 每个逻辑核心的利用率
        ArrayList<Double> coreUsage = new ArrayList<>();
        while (matcher.find()) {
            coreUsage.add(100 - Double.parseDouble(matcher.group(0)));
        }
        cpuInfo.setCoreUsage(coreUsage);

        return cpuInfo;
    }
}
