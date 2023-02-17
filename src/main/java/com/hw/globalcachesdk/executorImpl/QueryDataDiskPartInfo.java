package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.entity.AbstractEntity;
import com.hw.globalcachesdk.entity.DataDiskPartInfo;
import com.hw.globalcachesdk.exception.CommandExecException;
import com.hw.globalcachesdk.executor.AbstractCommandExecutor;
import com.hw.globalcachesdk.executor.Configure;
import com.jcraft.jsch.Session;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 请求节点数据盘分区信息
 * @author 章睿彬
 */
@Configure(path="/configure/QueryDataDiskPartInfo.xml")
public class QueryDataDiskPartInfo extends AbstractCommandExecutor {

    private static final Pattern DATA_DISK_PART_INFO_PATTERN = Pattern.compile("/var/lib/ceph/osd/ceph(.*)");

    public QueryDataDiskPartInfo() {
        super(QueryDataDiskPartInfo.class);
    }

    @Override
    public AbstractEntity exec(Session sshSession, String args) throws CommandExecException {
        String command = "bash /home/GlobalCacheScripts/SDK/datadiskpart.sh";
        String returnValue = execInternal(sshSession, command);
        String[] returnValueList = returnValue.split("\n");

        ArrayList<DataDiskPartInfo.Part> partList = new ArrayList<>();
        DataDiskPartInfo dataDiskPartInfo = new DataDiskPartInfo();
        for (String s : returnValueList) {
            String[] str = s.split("\\s+");
            DataDiskPartInfo.Part part = new DataDiskPartInfo.Part();
            Matcher matcher = DATA_DISK_PART_INFO_PATTERN.matcher(str[5]);
            if (matcher.find()) {
                part.setDiskName(str[5]);
                part.setCapacity(Long.parseLong(str[1]));
                part.setUsed(Long.parseLong(str[2]));
                part.setAvail(Long.parseLong(str[3]));
                partList.add(part);
            }
        }
        dataDiskPartInfo.setPartList(partList);

        return dataDiskPartInfo;
    }
}
