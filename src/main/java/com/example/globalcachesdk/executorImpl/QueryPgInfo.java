package com.example.globalcachesdk.executorImpl;

import com.example.globalcachesdk.entity.AbstractEntity;
import com.example.globalcachesdk.entity.PgInfo;
import com.example.globalcachesdk.exception.CommandExecException;
import com.example.globalcachesdk.executor.AbstractCommandExecutor;
import com.example.globalcachesdk.executor.Configure;
import com.jcraft.jsch.Session;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 查询PG信息
 * @author 章睿彬
 */
@Configure(path = "/configure/QueryPgInfo.xml")
public class QueryPgInfo extends AbstractCommandExecutor {

    private static final Pattern PG_NUM_PATTERN = Pattern.compile("[0-9]+");

    private static final Pattern PG_COPY_INFO_PATTERN = Pattern.compile("<[0-9]+, [0-9]+, [0-9A-Z_]+>");

    public QueryPgInfo() {
        super(QueryPgInfo.class);
    }

    /**
     * 输入格式: <1, 2, PG_COPY_STATE_RUNNING>
     */
    private static PgInfo.PgCopyInfo parseof(String str) {
        String[] strList = str.substring(1,str.length()-1).split(",");

        PgInfo.PgCopyInfo pgCopyInfo = new PgInfo.PgCopyInfo();
        pgCopyInfo.setNodeId(Integer.parseInt(strList[0].trim()));
        pgCopyInfo.setDiskId(Integer.parseInt(strList[1].trim()));
        pgCopyInfo.setState(PgInfo.CopyState.valueOf(strList[2].trim()));

        return pgCopyInfo;
    }

    /**
     * 输入格式:
     *    1 |     25 |          1 |          2 |        PG_STATE_NORMAL |      3 |[<1, 2, PG_COPY_STATE_RUNNING>, <0, 0, PG_COPY_STATE_RUNNING>, <2, 5, PG_COPY_STATE_RUNNING>, ]
     *    2 |     25 |          2 |          4 |        PG_STATE_NORMAL |      3 |[<2, 4, PG_COPY_STATE_RUNNING>, <0, 1, PG_COPY_STATE_RUNNING>, <1, 2, PG_COPY_STATE_RUNNING>, ]
     *    4 |     25 |          1 |          3 |        PG_STATE_NORMAL |      3 |[<1, 3, PG_COPY_STATE_RUNNING>, <2, 5, PG_COPY_STATE_RUNNING>, <0, 0, PG_COPY_STATE_RUNNING>, ]
     *    5 |     25 |          2 |          5 |        PG_STATE_NORMAL |      3 |[<2, 5, PG_COPY_STATE_RUNNING>, <1, 3, PG_COPY_STATE_RUNNING>, <0, 1, PG_COPY_STATE_RUNNING>, ]
     *  ......
     *  179 |     25 |          2 |          5 |        PG_STATE_NORMAL |      3 |[<2, 5, PG_COPY_STATE_RUNNING>, <1, 3, PG_COPY_STATE_RUNNING>, <0, 1, PG_COPY_STATE_RUNNING>, ]
     */
    private static ArrayList<PgInfo.Pg> parseOf(String str) {
        ArrayList<PgInfo.Pg> pgView = new ArrayList<>();

        String[] strList = str.split("\n");
        for (String s : strList) {
            String[] temp = s.split("\\|");
            for (int k = 0; k < temp.length; ++k) {
                temp[k] = temp[k].trim();
            }

            PgInfo.Pg pg = new PgInfo.Pg();
            pg.setPgId(Integer.parseInt(temp[0]));
            pg.setBv(Integer.parseInt(temp[1]));
            pg.setMasterDisk(Integer.parseInt(temp[2]));
            pg.setMasterDisk(Integer.parseInt(temp[3]));
            pg.setState(PgInfo.PgState.valueOf(temp[4]));
            pg.setCopyNum(Integer.parseInt(temp[5]));

            ArrayList<PgInfo.PgCopyInfo> copyInfos = new ArrayList<>();

            Matcher matcher = PG_COPY_INFO_PATTERN.matcher(temp[6]);
            while (matcher.find()) {
                copyInfos.add(QueryPgInfo.parseof(matcher.group(0)));
            }
            pg.setCopyInfos(copyInfos);

            pgView.add(pg);
        }

        return pgView;
    }

    @Override
    public AbstractEntity exec(Session sshSession, String args) throws CommandExecException {
        String command = "bash /home/GlobalCacheScripts/SDK/pg_info/pg_info.sh " + args;

        String returnValue = execInternal(sshSession, command);
        String[] returnValueList = returnValue.split("\n");

        if ("all".equals(args)) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 6; i < returnValueList.length; ++i) {
                stringBuilder.append(returnValueList[i]).append("\n");
            }
            PgInfo pgInfo = new PgInfo();
            pgInfo.setPrimaryPgView(QueryPgInfo.parseOf(stringBuilder.toString()));

            return pgInfo;
        } else {
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
                primaryStringBuilder.append(returnValueList[i] + "\n");
            }

            StringBuilder secondaryPrimaryBuilder = new StringBuilder();
            for (int i = 5 + primaryPgNum; i < 5 + primaryPgNum + secondaryPgNum; ++i) {
                secondaryPrimaryBuilder.append(returnValueList[i] + "\n");
            }

            PgInfo pgInfo = new PgInfo();
            pgInfo.setPrimaryPgView(QueryPgInfo.parseOf(primaryStringBuilder.toString()));
            pgInfo.setSecondaryPgView(QueryPgInfo.parseOf(secondaryPrimaryBuilder.toString()));

            return pgInfo;
        }
    }
}
