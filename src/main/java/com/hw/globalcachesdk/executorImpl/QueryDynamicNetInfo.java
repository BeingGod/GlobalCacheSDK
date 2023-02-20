package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.entity.AbstractEntity;
import com.hw.globalcachesdk.entity.DynamicNetInfo;
import com.hw.globalcachesdk.exception.ReturnValueParseException;
import com.hw.globalcachesdk.executor.AbstractCommandExecutor;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

import java.util.ArrayList;

/**
 * 请求动态网络信息
 * @author 章睿彬
 */
@Configure(path = "/configure/QueryDynamicNetInfo.xml")
@Script(path = "/home/GlobalCacheScripts/SDK/dynamic_net.sh")
public class QueryDynamicNetInfo extends AbstractCommandExecutor {

    public QueryDynamicNetInfo() {
        super(QueryDynamicNetInfo.class);
    }

    @Override
    public AbstractEntity parseOf(String returnValue) throws ReturnValueParseException {
        String[] returnValueList = returnValue.split("\n");

        ArrayList<DynamicNetInfo.InterfaceInfo> interfaceInfos = new ArrayList<>();
        for (int i = 1; i < returnValueList.length; i++) {
            //消除所有连续的空格，为按空格切分做准备
            while(returnValueList[i].contains("  ")){
                returnValueList[i] = returnValueList[i].replaceAll("  "," ");
            }
            // Average: IFACE rxpck/s txpck/s rxkB/s txkB/s rxcmp/s txcmp/s rxmcst/s %ifutil
            String[] temp = returnValueList[i].split(" ");
            DynamicNetInfo.InterfaceInfo interfaceInfo = new DynamicNetInfo.InterfaceInfo();
            interfaceInfo.setName(temp[1]);
            interfaceInfo.setRxpcks((int) Float.parseFloat(temp[2]));
            interfaceInfo.setTxpcks((int) Float.parseFloat(temp[3]));
            interfaceInfo.setRxkBs(Float.parseFloat(temp[4]));
            interfaceInfo.setTxkBs(Float.parseFloat(temp[5]));

            interfaceInfos.add(interfaceInfo);
        }

        DynamicNetInfo dynamicNetInfo = new DynamicNetInfo();
        dynamicNetInfo.setInterfaceInfos(interfaceInfos);

        return dynamicNetInfo;
    }
}
