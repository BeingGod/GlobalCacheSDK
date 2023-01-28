package com.example.globalcachesdk.executorImpl;

import com.example.globalcachesdk.entity.AbstractEntity;
import com.example.globalcachesdk.entity.UptimeInfo;
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
        String command = "bash /home/GlobalCacheScripts/SDK/uptime.sh";

        String returnValue = execInternal(sshSession, command).trim();
        UptimeInfo uptimeInfo=new UptimeInfo();
        if(returnValue.length()<1){
            return null;
        }else{
            int date=0,hour=0,min=0;
            returnValue=returnValue.substring(0,returnValue.length()-1);
            int dateNum=returnValue.indexOf("d");
            if(dateNum!=-1){
                date= Integer.parseInt(returnValue.substring(0,dateNum).trim());
            }
            int hourNum=returnValue.indexOf(":");
            if(hourNum==1){
                hour=Integer.parseInt(returnValue.substring(hourNum-1,hourNum).trim());
            }else{
                hour=Integer.parseInt(returnValue.substring(hourNum-2,hourNum).trim());
            }
            min=Integer.parseInt(returnValue.substring(hourNum+1).trim());
            uptimeInfo.setUptime(date*24*60+hour*60+min);
        }
        return uptimeInfo;
    }
}
