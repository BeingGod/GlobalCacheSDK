package com.example.globalcachesdk.executorImpl;

import com.example.globalcachesdk.entity.AbstractEntity;
import com.example.globalcachesdk.exception.CommandExecException;
import com.example.globalcachesdk.executor.AbstractCommandExecutor;
import com.example.globalcachesdk.executor.Configure;
import com.jcraft.jsch.Session;

/**
 * 请求缓存盘信息
 * @author 章睿彬
 */
@Configure(path="/configure/QueryCacheDiskInfo.xml")
public class QueryCacheDiskInfo extends AbstractCommandExecutor {
    public QueryCacheDiskInfo() {
        super(QueryCacheDiskInfo.class);
    }

    @Override
    public AbstractEntity exec(Session sshSession, String args) throws CommandExecException {
        return null;
    }
}
