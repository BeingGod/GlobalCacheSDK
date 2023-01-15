package com.example.globalcachesdk.sdk.info;

import com.example.globalcachesdk.entity.AbstractEntity;
import com.example.globalcachesdk.entity.CpuInfo;
import com.example.globalcachesdk.exception.CommandExecException;
import com.example.globalcachesdk.executor.AbstractCommandExecutor;
import com.example.globalcachesdk.executor.CommandExecutorDescription;
import com.jcraft.jsch.Session;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 请求CPU信息
 * @author 章睿彬
 */
public class QueryCpuInfo extends AbstractCommandExecutor {

    /**
     * 节点CPU信息正则表达式
     */
    private static final Pattern CPU_INFO_PATTERN = Pattern.compile("\\d+\\.\\d+");

    public QueryCpuInfo() {
        super();
        des = defaultDes();
    }

    /**
     * 请求节点CPU信息
     *
     * @param sshSession ssh会话
     * @param args 参数
     * @return CPU信息
     * @throws CommandExecException 命令执行失败抛出此异常
     */
    @Override
    public AbstractEntity exec(Session sshSession, String args) throws CommandExecException {
        String command = "bash /home/GlobalCacheScripts/SDK/cpu_usage.sh " + args;

        String returnValue = execInternal(sshSession, command, args);

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

     public static CommandExecutorDescription defaultDes() {
        // @TODO: 支持从XML中反转生成
        CommandExecutorDescription des = new CommandExecutorDescription();
        des.setClassPath("com.example.globalcachesdk.sdk.info.QueryCpuInfo");
        des.setExecuteNode(CommandExecutorDescription.ExecuteNode.ALL_NODES);
        des.setExecutePrivilege(CommandExecutorDescription.ExecutePrivilege.USER);
        des.setWithArgs(false);
        des.setTimeout(2);

        return des;
    }
}
