package com.hw.globalcachesdk.executorImpl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.hw.globalcachesdk.entity.AbstractEntity;
import com.hw.globalcachesdk.entity.ClusterAlarmInfo;
import com.hw.globalcachesdk.exception.ReturnValueParseException;
import com.hw.globalcachesdk.executor.AbstractCommandExecutor;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 查询集群异常信息
 * @author 章睿彬, 杨凤仪
 */
@Configure(path = "/configure/QueryClusterAlarmInfo.xml")
@Script(path = "/home/GlobalCacheScripts/SDK/cluster_alarm_info.sh")
public class QueryClusterAlarmInfo extends AbstractCommandExecutor {

    private static final Pattern CLUSTER_ALARM_INFO_PATTERN = Pattern.compile("\\((.*?)\\)");

    public QueryClusterAlarmInfo() {
        super(QueryClusterAlarmInfo.class);
    }

    @Override
    public AbstractEntity parseOf(String returnValue) throws ReturnValueParseException {
        String[] returnValueList = returnValue.split("\n");

        ArrayList<ClusterAlarmInfo.AlarmInfo> alarmInfoList=new ArrayList<>();
        ClusterAlarmInfo clusterAlarmInfo = new ClusterAlarmInfo();

        for(int i=0;i<returnValueList.length-1;i++){
            String [] str=returnValueList[i].split(", ");
            ClusterAlarmInfo.AlarmInfo alarmInfo= new ClusterAlarmInfo.AlarmInfo();

            String log=str[5];
            String time=str[1].substring(6);

            DateTime datetime = DateUtil.parse(time,"yyyy-MM-dd HH:mm:ss");

            alarmInfo.setTime(datetime);
            alarmInfo.setLog(str[5]);

            Matcher matcherAlarm = CLUSTER_ALARM_INFO_PATTERN.matcher(str[0]);
            Matcher matcherNodeIp = CLUSTER_ALARM_INFO_PATTERN.matcher(str[2]);
            Matcher matcherDiskId = CLUSTER_ALARM_INFO_PATTERN.matcher(str[3]);
            Matcher matcherDiskSn = CLUSTER_ALARM_INFO_PATTERN.matcher(str[4]);

            if(matcherAlarm.find()){
                alarmInfo.setAlarmNumber(Integer.parseInt(matcherAlarm.group(1)));
            }
            if(matcherNodeIp.find()){
                String strnodeIp=matcherNodeIp.group(1);
                alarmInfo.setNodeIp(strnodeIp);
            }
            if(matcherDiskId.find()){
                String strdiskId=matcherDiskId.group(1);
                alarmInfo.setDiskIp(strdiskId);
            }
            if(matcherDiskSn.find()){
                String strdiskSn=matcherDiskSn.group(1);
                alarmInfo.setDiskSn(strdiskSn);
            }
            alarmInfoList.add(alarmInfo);
        }
        clusterAlarmInfo.setAlarmInfoList(alarmInfoList);

        return clusterAlarmInfo;
    }
}
