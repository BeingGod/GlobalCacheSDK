package com.example.demo.globalcachesdk.excutor;


import cn.hutool.core.io.IORuntimeException;
import cn.hutool.extra.ssh.JschRuntimeException;
import cn.hutool.extra.ssh.JschUtil;
import com.example.demo.globalcachesdk.entity.MemInfo;
import com.example.demo.globalcachesdk.exception.CommandExecFailedException;
import com.jcraft.jsch.Session;
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
    private static final Pattern MEM_INFO_PATTERN = Pattern.compile("\\d+");

    /**
     * 执行命令, 若执行失败则抛出异常
     * TODO: 待实现
     * @param sshSession SSH会话
     * @param command 需要执行的命令
     * @return 命令执行结果
     * @throws CommandExecFailedException 命令执行失败异常
     */
    private static String exce(Session sshSession, String command) throws CommandExecFailedException {
        return null;
    }

    /**
     * 请求节点内存信息
     * @param sshSession ssh会话
     * @return MemInfo
     */
    public static MemInfo queryMemInfo(Session sshSession) throws CommandExecFailedException {
        String command = "sh /root/scripts/mem_info.sh";

        // TODO: 使用exec函数进行替换
        String returnValue;
        try {
            returnValue = JschUtil.exec(sshSession, command, Charset.defaultCharset());
        } catch (IORuntimeException | JschRuntimeException e) {
            throw new CommandExecFailedException("命令执行失败", e);
        }

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
}
