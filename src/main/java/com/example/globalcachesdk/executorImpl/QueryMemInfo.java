package com.example.globalcachesdk.executorImpl;

import com.example.globalcachesdk.entity.AbstractEntity;
import com.example.globalcachesdk.entity.MemInfo;
import com.example.globalcachesdk.exception.CommandExecException;
import com.example.globalcachesdk.executor.AbstractCommandExecutor;
import com.example.globalcachesdk.executor.Configure;
import com.jcraft.jsch.Session;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 请求内存信息
 * @author 章睿彬
 */
@Configure(path= "/configure/QueryMemInfo.xml")
public class QueryMemInfo extends AbstractCommandExecutor {
    /**
     * 节点内存信息正则表达式
     */
    private static final Pattern MEM_INFO_PATTERN = Pattern.compile("\\d+");

    public QueryMemInfo() {
        super(QueryMemInfo.class);
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
        String command = "bash /home/GlobalCacheScripts/SDK/mem_usage.sh";

        String returnValue = execInternal(sshSession, command);

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
