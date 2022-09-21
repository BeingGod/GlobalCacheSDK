package com.example.globalcachesdk.excutor;


import cn.hutool.core.io.IORuntimeException;
import cn.hutool.extra.ssh.JschRuntimeException;
import cn.hutool.extra.ssh.JschUtil;
import com.example.globalcachesdk.entity.MemInfo;
import com.example.globalcachesdk.entity.CpuInfo;
import com.example.globalcachesdk.exception.CommandExecFailedException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Shell命令调用
 * @author ya059
 */
public class CommandExcutor {

    /**
     * 节点内存信息正则表达式
     */
    private static final Pattern MEM_INFO_PATTERN = Pattern.compile("\\d+"),
            CPU_INFO_PATTERN = Pattern.compile("\\d+\\.\\d+");

    /**
     * 执行命令, 若执行失败则抛出异常
     * @param sshSession SSH会话
     * @param command 需要执行的命令
     * @return 命令执行结果
     * @throws CommandExecFailedException 命令执行失败异常
     */
    private static String exec(Session sshSession, String command) throws CommandExecFailedException {
        try {
            int[] flag = {1};
            OutputStream newErrStream = new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    //TODO：本处输出为ASCII强转实现，UTF-8适配
                    System.err.print((char) b);
                }
                @Override
                public void write(byte[] b, int off, int len) throws IOException {
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
        }catch (CommandExecFailedException e) {
            throw new CommandExecFailedException("命令执行失败");
        }
    }

    /**
     * 请求节点内存信息
     * @param sshSession ssh会话
     * @return MemInfo
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
     * @param sshSession ssh会话
     * @return CpuInfo
     */
    public static CpuInfo queryCpuInfo(Session sshSession) throws CommandExecFailedException {
        String command = "bash /root/scripts/cpu_usage.sh";

        // TODO: 使用exec函数进行替换
        String returnValue;
        try {
            returnValue = JschUtil.exec(sshSession, command, Charset.defaultCharset());
        } catch (IORuntimeException | JschRuntimeException e) {
            throw new CommandExecFailedException("命令执行失败", e);
        }

        Matcher matcher = CPU_INFO_PATTERN.matcher(returnValue);
        CpuInfo cpuInfo = new CpuInfo();

        while(matcher.find()) {
            cpuInfo.setUsage(Double.parseDouble(matcher.group(0)));
        }

        return cpuInfo;
    }
}
