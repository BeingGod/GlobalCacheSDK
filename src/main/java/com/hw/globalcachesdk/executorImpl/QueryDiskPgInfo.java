package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.entity.AbstractEntity;
import com.hw.globalcachesdk.entity.PgInfo;
import com.hw.globalcachesdk.exception.ReturnValueParseException;
import com.hw.globalcachesdk.executor.AbstractCommandExecutor;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 查询节点PG信息
 * @author 章睿彬
 */
@Configure(path = "/configure/QueryPgInfo.xml")
@Script(path = "/home/GlobalCacheScripts/SDK/pg_info/pg_info.sh")
public class QueryDiskPgInfo extends AbstractCommandExecutor {

    private static final Pattern PG_NUM_PATTERN = Pattern.compile("[0-9]+");

    public QueryDiskPgInfo() {
        super(QueryDiskPgInfo.class);
    }

    @Override
    public AbstractEntity parseOf(String returnValue) throws ReturnValueParseException {
        try {
            String[] returnValueList = returnValue.split("\n");

            Matcher matcherPgNum = PG_NUM_PATTERN.matcher(returnValueList[returnValueList.length-1]);

            int primaryPgNum = 0;
            if (matcherPgNum.find()) {
                primaryPgNum = Integer.parseInt(matcherPgNum.group(0));
            }

            int secondaryPgNum = 0;
            if (matcherPgNum.find()) {
                secondaryPgNum = Integer.parseInt(matcherPgNum.group(0));
            }

            StringBuilder primaryStringBuilder = new StringBuilder();
            for (int i = 3; i < 3 + primaryPgNum; ++i) {
                primaryStringBuilder.append(returnValueList[i]).append("\n");
            }

            StringBuilder secondaryPrimaryBuilder = new StringBuilder();
            for (int i = 5 + primaryPgNum; i < 5 + primaryPgNum + secondaryPgNum; ++i) {
                secondaryPrimaryBuilder.append(returnValueList[i]).append("\n");
            }

            PgInfo pgInfo = new PgInfo();
            pgInfo.setPrimaryPgView(PgInfo.parseOf(primaryStringBuilder.toString()));
            pgInfo.setSecondaryPgView(PgInfo.parseOf(secondaryPrimaryBuilder.toString()));

            return pgInfo;
        }  catch (Exception e) {
            throw new ReturnValueParseException("返回值解析失败", e);
        }
    }
}