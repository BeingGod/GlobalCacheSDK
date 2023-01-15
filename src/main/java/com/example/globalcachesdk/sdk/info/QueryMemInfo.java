package com.example.globalcachesdk.sdk.info;

import com.example.globalcachesdk.ExecuteNode;
import com.example.globalcachesdk.ExecutePrivilege;
import com.example.globalcachesdk.entity.AbstractEntity;
import com.example.globalcachesdk.entity.MemInfo;
import com.example.globalcachesdk.exception.CommandExecException;
import com.example.globalcachesdk.executor.AbstractCommandExecutor;
import com.example.globalcachesdk.executor.CommandExecutorDescription;
import com.jcraft.jsch.Session;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 请求内存信息
 * @author 章睿彬
 */
public class QueryMemInfo extends AbstractCommandExecutor {
    /**
     * 节点内存信息正则表达式
     */
    private static final Pattern MEM_INFO_PATTERN = Pattern.compile("\\d+");

    public QueryMemInfo() {
        super();
        des = defaultDes();
    }

    /**
     * 请求节点内存信息
     *
     * @param sshSession ssh会话
     * @return 内存信息
     * @throws CommandExecException 命令执行失败异常
     */
    @Override
    public AbstractEntity exec(Session sshSession, String args) throws CommandExecException {
        String command = "bash /home/GlobalCacheScripts/SDK/mem_usage.sh " + args;

        String returnValue = execInternal(sshSession, command, args);

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

    public static CommandExecutorDescription defaultDes() {
        // @TODO: 支持从XML中反转生成
        CommandExecutorDescription des = new CommandExecutorDescription();
        // 需要类的完整路径
        des.setClassPath("com.example.globalcachesdk.sdk.info.QueryMemInfo");
        des.setExecuteNode(ExecuteNode.ALL_NODES);
        des.setExecutePrivilege(ExecutePrivilege.USER);
        des.setWithArgs(false);
        des.setTimeout(2);

        return des;
    }
}
