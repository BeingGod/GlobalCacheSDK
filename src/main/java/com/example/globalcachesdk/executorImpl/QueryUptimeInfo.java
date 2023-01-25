package com.example.globalcachesdk.executorImpl;

import com.example.globalcachesdk.entity.AbstractEntity;
import com.example.globalcachesdk.exception.CommandExecException;
import com.example.globalcachesdk.executor.AbstractCommandExecutor;
import com.example.globalcachesdk.executor.Configure;
import com.jcraft.jsch.Session;

/**
 * 请求节点运行时间
 * @author 章睿彬
 */
@Configure(path="/configure/QueryUptimeInfo.xml")
public class QueryUptimeInfo extends AbstractCommandExecutor {

    public QueryUptimeInfo() {
        super(QueryUptimeInfo.class);
    }

    @Override
    public AbstractEntity exec(Session sshSession, String args) throws CommandExecException {
        return null;
    }
}
