package com.example.globalcachesdk.executorImpl;

import com.example.globalcachesdk.entity.AbstractEntity;
import com.example.globalcachesdk.exception.CommandExecException;
import com.example.globalcachesdk.executor.AbstractCommandExecutor;
import com.example.globalcachesdk.executor.Configure;
import com.jcraft.jsch.Session;

/**
 * 查询PT信息
 * @author 章睿彬
 */
@Configure(path="/configure/QueryPtInfo.xml")
public class QueryPtInfo extends AbstractCommandExecutor {

    public QueryPtInfo() {
        super(QueryPtInfo.class);
    }

    @Override
    public AbstractEntity exec(Session sshSession, String args) throws CommandExecException {
        return null;
    }
}
