package com.example.globalcachesdk.executorImpl;

import com.example.globalcachesdk.entity.AbstractEntity;
import com.example.globalcachesdk.entity.CpuInfo;
import com.example.globalcachesdk.entity.PtInfo;
import com.example.globalcachesdk.exception.CommandExecException;
import com.example.globalcachesdk.executor.AbstractCommandExecutor;
import com.example.globalcachesdk.executor.Configure;
import com.jcraft.jsch.Session;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 查询PT信息
 * @author 章睿彬
 */
@Configure(path="/configure/QueryPtInfo.xml")
public class QueryPtInfo extends AbstractCommandExecutor {

    private static final Pattern PT_MAP_INFO_PATTERN = Pattern.compile("[0-9]+, [0-9]+, [0-9]+");

    public QueryPtInfo() {
        super(QueryPtInfo.class);
    }

    private static PtInfo parseOf(String str) {
        String[] strList = str.split("\n");

        PtInfo ptInfo = new PtInfo();
        ArrayList<PtInfo.Pt> ptList = new ArrayList<>();
        for (String s : strList) {
            String[] temp = s.split("\\|");
            for (int k = 0; k < temp.length; ++k) {
                temp[k] = temp[k].trim();
            }

            PtInfo.Pt pt = new PtInfo.Pt();
            pt.setPtId(Integer.parseInt(temp[0]));
            pt.setBv(Integer.parseInt(temp[1]));
            pt.setState(PtInfo.PtState.valueOf(temp[2]));
            pt.setIndexInNode(Integer.parseInt(temp[3]));

            Matcher matcher = PT_MAP_INFO_PATTERN.matcher(temp[4]);

            PtInfo.PtMapInfo ptMapInfo = new PtInfo.PtMapInfo();
            if (matcher.find()) {
                String[] ptMapInfoList = matcher.group(0).split(",");
                ptMapInfo.setNodeId(Integer.parseInt(ptMapInfoList[0].trim()));
                ptMapInfo.setDiskId(Integer.parseInt(ptMapInfoList[1].trim()));
                ptMapInfo.setVnodeId(Integer.parseInt(ptMapInfoList[2].trim()));
            }
            pt.setPtMapInfo(ptMapInfo);

            PtInfo.PtMapInfo backupPtMapInfo = new PtInfo.PtMapInfo();
            if (matcher.find()) {
                String[] ptMapInfoList = matcher.group(0).split(",");
                backupPtMapInfo.setNodeId(Integer.parseInt(ptMapInfoList[0].trim()));
                backupPtMapInfo.setDiskId(Integer.parseInt(ptMapInfoList[1].trim()));
                backupPtMapInfo.setVnodeId(Integer.parseInt(ptMapInfoList[2].trim()));
            }
            pt.setBackupPtMapInfo(backupPtMapInfo);

            ptList.add(pt);
        }

        ptInfo.setPtList(ptList);

        return ptInfo;
    }

    @Override
    public AbstractEntity exec(Session sshSession, String args) throws CommandExecException {
        String command = "bash /home/GlobalCacheScripts/SDK/pt_info/pt_info.sh " + args;

        String returnValue = execInternal(sshSession, command);
        String[] returnValueList = returnValue.split("\n");

        StringBuilder stringBuilder = new StringBuilder();
        if ("all".equals(args)) {
            for (int i = 5; i < returnValueList.length; ++i) {
                stringBuilder.append(returnValueList[i] + "\n");
            }
        } else {
            for (int i = 3; i < returnValueList.length - 1; ++i) {
                stringBuilder.append(returnValueList[i] + "\n");
            }
        }
        return QueryPtInfo.parseOf(stringBuilder.toString());
    }
}
