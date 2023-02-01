package com.example.globalcachesdk.executorImpl;

import com.example.globalcachesdk.entity.AbstractEntity;
import com.example.globalcachesdk.exception.CommandExecException;
import com.example.globalcachesdk.executor.AbstractCommandExecutor;
import com.example.globalcachesdk.executor.Configure;
import com.jcraft.jsch.Session;

/**
 * 查询集群异常信息
 * @author 章睿彬
 */
@Configure(path="/configure/QueryClusterAlarmInfo.xml")
public class QueryClusterAlarmInfo extends AbstractCommandExecutor {

    public QueryClusterAlarmInfo() {
        super(QueryClusterAlarmInfo.class);
    }

    @Override
    public AbstractEntity exec(Session sshSession, String args) throws CommandExecException {
        return null;
    }
}
