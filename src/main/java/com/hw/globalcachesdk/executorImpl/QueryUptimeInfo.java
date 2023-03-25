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
@Script(path = "/home/GlobalCacheScriptsNew/data/uptime/uptime.sh")
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
            //判断是否有day的输入
            int dateIndex = returnValue.indexOf("d");
            if (dateIndex != -1) {
                //获取日期
                date = Integer.parseInt(returnValue.substring(0, dateIndex).trim());
                //处理字符串，去掉日期相关内容
                returnValue=returnValue.substring(returnValue.indexOf(",")+1);
            }
            //判断显示格式，带不带小时
            int hourIndex = returnValue.indexOf(":");
            if(hourIndex!=-1){
                //带小时，显示格式：“  5:03,“
                if (hourIndex == 1){
                    hour = Integer.parseInt(returnValue.substring(0, 1).trim());
                } else {
                    hour = Integer.parseInt(returnValue.substring(hourIndex - 2, hourIndex).trim());
                }
                //去掉行尾的","
                min = Integer.parseInt(returnValue.substring(hourIndex + 1).replaceAll(",","").trim());
            }else{
                //不带小时，显示格式：“  55min,”
                int minIndex=returnValue.indexOf("m");
                min=Integer.parseInt( returnValue.substring(0,minIndex).trim());
            }
            uptimeInfo.setUptime((long) date * 24 * 60 + hour * 60L +min);
        }

        return uptimeInfo;
    }
}
