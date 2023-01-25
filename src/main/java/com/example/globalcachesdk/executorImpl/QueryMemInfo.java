package com.example.globalcachesdk.executorImpl;

import com.example.globalcachesdk.entity.AbstractEntity;
import com.example.globalcachesdk.entity.MemInfo;
import com.example.globalcachesdk.exception.CommandExecException;
import com.example.globalcachesdk.executor.AbstractCommandExecutor;
import com.example.globalcachesdk.executor.Configure;
import com.jcraft.jsch.Session;

/**
 * 请求内存信息
 * @author 章睿彬
 */
@Configure(path= "/configure/QueryMemInfo.xml")
public class QueryMemInfo extends AbstractCommandExecutor {

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

        String[] returnValueList = returnValue.split("\n");

        MemInfo memInfo = new MemInfo();
        memInfo.setTotal(Long.parseLong(returnValueList[0]));
        memInfo.setUsed(Long.parseLong(returnValueList[1]));
        memInfo.setFree(Long.parseLong(returnValueList[2]));
        memInfo.setCache(Long.parseLong(returnValueList[4]));
        memInfo.setAvail(Long.parseLong(returnValueList[5]));

        return memInfo;
    }
}
