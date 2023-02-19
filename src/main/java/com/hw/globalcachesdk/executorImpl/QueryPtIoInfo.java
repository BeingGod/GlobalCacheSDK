package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.entity.AbstractEntity;
import com.hw.globalcachesdk.entity.PtIoInfo;
import com.hw.globalcachesdk.exception.ReturnValueParseException;
import com.hw.globalcachesdk.executor.AbstractCommandExecutor;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

import java.util.ArrayList;

/**
 * 查询节点PtIo信息
 * @author 李金泽
 */
@Configure(path = "/configure/QueryPtIoInfo.xml")
@Script(path = "/home/GlobalCacheScripts/SDK/pt_io_info/pt_io_info.sh")
public class QueryPtIoInfo extends AbstractCommandExecutor {

    public QueryPtIoInfo() {
        super(QueryPtIoInfo.class);
    }

    @Override
    public AbstractEntity parseOf(String returnValue) throws ReturnValueParseException {
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
            PtIoInfo.PtIo ptIo =new PtIoInfo.PtIo();
            ptIo.setIoCount(Integer.parseInt(nodeInfoList[3].trim()));
            ptIo.setReadCount(Integer.parseInt(nodeInfoList[5].trim()));
            ptIo.setReadSize(Integer.parseInt(nodeInfoList[7].trim()));
            ptIo.setWriteSizeCount(Integer.parseInt(nodeInfoList[9].trim()));
            ptIo.setWriteSize(Integer.parseInt(nodeInfoList[11].trim().substring(0,nodeInfoList[11].trim().length()-1)));
            pt.setIoInfo(ptIo);
            nodeArrayList.add(pt);
        }
        ptIoInfo.setPtArrayList(nodeArrayList);

        return ptIoInfo;
    }

}
