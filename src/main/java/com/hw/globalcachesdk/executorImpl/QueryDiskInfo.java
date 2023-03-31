package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.entity.AbstractEntity;
import com.hw.globalcachesdk.entity.DiskInfo;
import com.hw.globalcachesdk.exception.ReturnValueParseException;
import com.hw.globalcachesdk.executor.AbstractCommandExecutorSync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 请求节点磁盘信息
 * @author 章睿彬, 蔡润培
 */
@Configure(path = "/configure/QueryDiskInfo.xml")
@Script(path = "/home/GlobalCacheScripts/data/disk/disk.sh")
public class QueryDiskInfo extends AbstractCommandExecutorSync {

    public QueryDiskInfo() {
        super(QueryDiskInfo.class);
    }

    @Override
    public AbstractEntity parseOf(String returnValue) throws ReturnValueParseException {
        DiskInfo diskInfo = new DiskInfo();

        ArrayList<DiskInfo.Disk> diskList = new ArrayList<>();
        //返回值按行切分
        String[] diskListString = returnValue.split("\n");
        for (String diskInfoString:diskListString) {
            //行内按空格（包含连续空格）切分0为名称1为类型
            String[] diskInfoListString = diskInfoString.replaceAll("\\s{2,}", " ").split(" ");
            DiskInfo.Disk disk = new DiskInfo.Disk();
            disk.setName(diskInfoListString[0]);
            if("0".equals(diskInfoListString[1])){
                disk.setType(DiskInfo.DiskType.ROTA);
            } else if ("1".equals(diskInfoListString[1])) {
                disk.setType(DiskInfo.DiskType.NVME);
            }
            diskList.add(disk);
        }

        diskInfo.setDisksList(diskList);

        return diskInfo;
    }
}
