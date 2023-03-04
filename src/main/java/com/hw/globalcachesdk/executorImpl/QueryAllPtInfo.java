package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.entity.AbstractEntity;
import com.hw.globalcachesdk.entity.PtInfo;
import com.hw.globalcachesdk.exception.ReturnValueParseException;
import com.hw.globalcachesdk.executor.AbstractCommandExecutorSync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

/**
 * 查询PT信息
 * @author 章睿彬
 */
@Configure(path = "/configure/QueryAllPtInfo.xml")
@Script(path = "/home/GlobalCacheScripts/SDK/pt_info/pt_info.sh")
public class QueryAllPtInfo extends AbstractCommandExecutorSync {

    public QueryAllPtInfo() {
        super(QueryAllPtInfo.class);
    }

    @Override
    public AbstractEntity parseOf(String returnValue) throws ReturnValueParseException {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            String[] returnValueList = returnValue.split("\n");

            for (int i = 5; i < returnValueList.length; ++i) {
                stringBuilder.append(returnValueList[i]).append("\n");
            }
            return PtInfo.parseOf(stringBuilder.toString());

        } catch (Exception e) {
            throw new ReturnValueParseException("返回值解析失败", e);
        }
    }
}
