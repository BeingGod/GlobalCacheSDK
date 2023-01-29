package com.example.globalcachesdk.executorImpl;

import com.example.globalcachesdk.entity.AbstractEntity;
import com.example.globalcachesdk.entity.ClusterStatusInfo;
import com.example.globalcachesdk.exception.CommandExecException;
import com.example.globalcachesdk.executor.AbstractCommandExecutor;
import com.example.globalcachesdk.executor.Configure;
import com.jcraft.jsch.Session;

/**
 * 查询集群状态
 * @author 章睿彬
 */
@Configure(path="/configure/QueryClusterStatusInfo.xml")
public class QueryClusterStatusInfo extends AbstractCommandExecutor {
    public QueryClusterStatusInfo() {
        super(QueryClusterStatusInfo.class);
    }

    @Override
    public AbstractEntity exec(Session sshSession, String args) throws CommandExecException {
        String command = "bash /home/GlobalCacheScripts/SDK/cluster_status/cluster_status.sh";

        String returnValue = execInternal(sshSession, command);

        ClusterStatusInfo clusterStatusInfo = new ClusterStatusInfo();
        clusterStatusInfo.setClusterStatus(ClusterStatusInfo.ClusterStatus.valueOf(returnValue.substring(0,returnValue.length()-1)));

        return clusterStatusInfo;
    }
}
