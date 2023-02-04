package com.example.globalcachesdk.executorImpl;

import com.example.globalcachesdk.entity.AbstractEntity;
import com.example.globalcachesdk.entity.NodeStatusInfo;
import com.example.globalcachesdk.entity.PtIoInfo;
import com.example.globalcachesdk.exception.CommandExecException;
import com.example.globalcachesdk.executor.AbstractCommandExecutor;
import com.example.globalcachesdk.executor.Configure;
import com.jcraft.jsch.Session;

import java.util.ArrayList;

/**
 * 查询节点PtIo信息
 * @author 李金泽
 */
@Configure(path="/configure/QueryPtIoInfo.xml")
public class QueryPtIoInfo extends AbstractCommandExecutor {

    public QueryPtIoInfo() {
        super(QueryPtIoInfo.class);
    }

    @Override
    public AbstractEntity exec(Session sshSession, String args) throws CommandExecException {
        String command = "bash /home/GlobalCacheScripts/SDK/pt_io_info/pt_io_info.sh";

        //sh脚本执行返回值
        String returnValue = execInternal(sshSession, command);
        //按行切分
        String[] returnValueList = returnValue.split("\n");
        for (int i=0;i< returnValueList.length;i++){
            returnValueList[i]=returnValueList[i].replaceAll("\t","").trim();
        }
        //返回对象
        PtIoInfo ptIoInfo=new PtIoInfo();
        //添加Pt总数
        ptIoInfo.setPtNum(Integer.parseInt(returnValueList[2].substring(6).trim()));
        //返回对象中的Node ArrayList
        ArrayList<PtIoInfo.Pt> nodeArrayList=new ArrayList<>();
        for (int i = 3; i < returnValueList.length; i++) {
            String[] nodeInfoList = returnValueList[i].split(",|:");
            PtIoInfo.Pt pt=new PtIoInfo.Pt();
            pt.setPtId(Integer.parseInt(nodeInfoList[1].trim()));
            PtIoInfo.IoInfo ioInfo=new PtIoInfo.IoInfo();
            ioInfo.setIoCount(Integer.parseInt(nodeInfoList[3].trim()));
            ioInfo.setReadCount(Integer.parseInt(nodeInfoList[5].trim()));
            ioInfo.setReadSize(Integer.parseInt(nodeInfoList[7].trim()));
            ioInfo.setWriteSizeCount(Integer.parseInt(nodeInfoList[9].trim()));
            ioInfo.setWriteSize(Integer.parseInt(nodeInfoList[11].trim()));
            pt.setIoInfo(ioInfo);
            nodeArrayList.add(pt);
        }
        ptIoInfo.setPtArrayList(nodeArrayList);
        return ptIoInfo;
    }
}
