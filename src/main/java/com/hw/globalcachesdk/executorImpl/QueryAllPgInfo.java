package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.entity.AbstractEntity;
import com.hw.globalcachesdk.entity.PgInfo;
import com.hw.globalcachesdk.exception.ReturnValueParseException;
import com.hw.globalcachesdk.executor.AbstractCommandExecutorSync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;


/**
 * 查询PG信息
 * @author 章睿彬
 */
@Configure(path = "/configure/QueryAllPgInfo.xml")
@Script(path = "/home/GlobalCacheScripts/data/all_pg/all_pg.sh")
public class QueryAllPgInfo extends AbstractCommandExecutorSync {

    public QueryAllPgInfo() {
        super(QueryAllPgInfo.class);
    }

    @Override
    public AbstractEntity parseOf(String returnValue) throws ReturnValueParseException {
        try {
            String[] returnValueList = returnValue.split("\n");

            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 6; i < returnValueList.length; ++i) {
                stringBuilder.append(returnValueList[i]).append("\n");
            }
            PgInfo pgInfo = new PgInfo();
            pgInfo.setPrimaryPgView(PgInfo.parseOf(stringBuilder.toString()));

            return pgInfo;
        } catch (Exception e) {
            throw new ReturnValueParseException("返回值解析失败", e);
        }
    }
}
