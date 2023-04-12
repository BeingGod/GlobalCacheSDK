package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.entity.AbstractEntity;
import com.hw.globalcachesdk.entity.MemInfo;
import com.hw.globalcachesdk.exception.ReturnValueParseException;
import com.hw.globalcachesdk.executor.AbstractCommandExecutorSync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

/**
 * 请求内存信息
 * @author 章睿彬
 */
@Configure(path = "/configure/QueryMemInfo.xml")
@Script(path = "/data/mem/mem.sh")
public class QueryMemInfo extends AbstractCommandExecutorSync {

    public QueryMemInfo() {
        super(QueryMemInfo.class);
    }

    @Override
    public AbstractEntity parseOf(String returnValue) throws ReturnValueParseException {
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
