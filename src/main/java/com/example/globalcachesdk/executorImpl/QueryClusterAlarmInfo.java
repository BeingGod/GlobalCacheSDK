package com.example.globalcachesdk.executorImpl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.example.globalcachesdk.entity.AbstractEntity;
import com.example.globalcachesdk.entity.ClusterAlarmInfo;
import com.example.globalcachesdk.entity.ClusterStatusInfo;
import com.example.globalcachesdk.entity.DataDiskPartInfo;
import com.example.globalcachesdk.exception.CommandExecException;
import com.example.globalcachesdk.executor.AbstractCommandExecutor;
import com.example.globalcachesdk.executor.Configure;
import com.jcraft.jsch.Session;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 查询集群异常信息
 * @author 章睿彬
 */
@Configure(path="/configure/QueryClusterAlarmInfo.xml")
public class QueryClusterAlarmInfo extends AbstractCommandExecutor {

    private static final Pattern ClUSTER_AlARM_INFO_PATTERN = Pattern.compile("\\((.*?)\\)");

    public QueryClusterAlarmInfo() {
        super(QueryClusterAlarmInfo.class);
    }

    @Override
    public AbstractEntity exec(Session sshSession, String args) throws CommandExecException {

        ArrayList<ClusterAlarmInfo.AlarmInfo> alarmInfoList=new ArrayList<>();

        ClusterAlarmInfo clusterAlarmInfo = new ClusterAlarmInfo();

        String command = "bash /home/GlobalCacheScripts/SDK/cluster_alarm_info.sh";

        String returnValue = execInternal(sshSession, command);

        String[] returnValueList = returnValue.split("\n");

        for(int i=0;i<returnValueList.length-1;i++){

            String [] str=returnValueList[i].split(", ");
            ClusterAlarmInfo.AlarmInfo alarmInfo= new ClusterAlarmInfo.AlarmInfo();

            String log=str[5];
            String time=str[1].substring(6);

            DateTime datetime = DateUtil.parse(time,"yyyy-MM-dd HH:mm:ss");


            alarmInfo.setTime(datetime);

            alarmInfo.setLog(str[5]);


            Matcher matcher_alarm=ClUSTER_AlARM_INFO_PATTERN.matcher(str[0]);
            Matcher matcher_nodeIp=ClUSTER_AlARM_INFO_PATTERN.matcher(str[2]);
            Matcher matcher_diskId=ClUSTER_AlARM_INFO_PATTERN.matcher(str[3]);
            Matcher matcher_diskSn=ClUSTER_AlARM_INFO_PATTERN.matcher(str[4]);

            if(matcher_alarm.find()){
                alarmInfo.setAlarmNumber(Integer.parseInt(matcher_alarm.group(1)));
            }
            if(matcher_nodeIp.find()){
                String strnodeIp=matcher_nodeIp.group(1);
                alarmInfo.setNodeIp(strnodeIp);
            }
            if(matcher_diskId.find()){
                String strdiskId=matcher_diskId.group(1);
                alarmInfo.setDiskIp(strdiskId);
            }
            if(matcher_diskSn.find()){
                String strdiskSn=matcher_diskSn.group(1);
                alarmInfo.setDiskSn(strdiskSn);
            }
            alarmInfoList.add(alarmInfo);

        }

        clusterAlarmInfo.setAlarmInfoList(alarmInfoList);
        return clusterAlarmInfo;

    }
}
