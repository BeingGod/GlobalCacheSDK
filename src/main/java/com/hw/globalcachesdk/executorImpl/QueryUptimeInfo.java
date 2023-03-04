package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.entity.AbstractEntity;
import com.hw.globalcachesdk.entity.UptimeInfo;
import com.hw.globalcachesdk.exception.ReturnValueParseException;
import com.hw.globalcachesdk.executor.AbstractCommandExecutorSync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

/**
 * 请求节点运行时间
 * @author 章睿彬
 */
@Configure(path = "/configure/QueryUptimeInfo.xml")
@Script(path = "/home/GlobalCacheScripts/SDK/uptime.sh")
public class QueryUptimeInfo extends AbstractCommandExecutorSync {

    public QueryUptimeInfo() {
        super(QueryUptimeInfo.class);
    }

    @Override
    public AbstractEntity parseOf(String returnValue) throws ReturnValueParseException {
        UptimeInfo uptimeInfo = new UptimeInfo();
        if (returnValue.length() < 1){
            return null;
        } else {
            int date = 0, hour = 0, min = 0;
            returnValue = returnValue.substring(0, returnValue.length() - 1);
            int dateIndex = returnValue.indexOf("d");
            if (dateIndex != -1) {
                date = Integer.parseInt(returnValue.substring(0, dateIndex).trim());
            }
            int hourIndex = returnValue.indexOf(":");
            if (hourIndex == 1){
                hour = Integer.parseInt(returnValue.substring(0, 1).trim());
            } else {
                hour = Integer.parseInt(returnValue.substring(hourIndex - 2, hourIndex).trim());
            }
            if (returnValue.substring(hourIndex + 1).contains(",")){
                min = Integer.parseInt(returnValue.substring(hourIndex + 1).replaceAll(",","").trim());
            } else{
                min = Integer.parseInt(returnValue.substring(hourIndex + 1).trim());
            }
            uptimeInfo.setUptime((long) date * 24 * 60 + hour * 60L +min);
        }

        return uptimeInfo;
    }
}
