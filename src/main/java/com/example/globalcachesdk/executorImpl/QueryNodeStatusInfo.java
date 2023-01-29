package com.example.globalcachesdk.executorImpl;

import com.example.globalcachesdk.entity.AbstractEntity;
import com.example.globalcachesdk.entity.ClusterStatusInfo;
import com.example.globalcachesdk.exception.CommandExecException;
import com.example.globalcachesdk.executor.AbstractCommandExecutor;
import com.example.globalcachesdk.executor.Configure;
import com.jcraft.jsch.Session;

/**
 * 查询节点状态
 * @author 章睿彬
 */
@Configure(path="/configure/QueryClusterStatusInfo.xml")
public class QueryNodeStatusInfo extends AbstractCommandExecutor {
    public QueryNodeStatusInfo() {
        super(QueryNodeStatusInfo.class);
    }

    @Override
    public AbstractEntity exec(Session sshSession, String args) throws CommandExecException {
        return null;
    }
}
