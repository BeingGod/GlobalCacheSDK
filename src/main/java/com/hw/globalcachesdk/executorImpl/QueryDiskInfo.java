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
@Script(path = "/home/GlobalCacheScripts/SDK/disk_info.sh")
public class QueryDiskInfo extends AbstractCommandExecutorSync {

    private static final Pattern DISK_INFO_PATTERN = Pattern.compile("[a-z].*");
    private static final Pattern DISK_ROTA_PATTERN = Pattern.compile("^sd.");
    private static final Pattern DISK_NVME_PATTERN = Pattern.compile("^nvme.");
    public QueryDiskInfo() {
        super(QueryDiskInfo.class);
    }

    @Override
    public AbstractEntity parseOf(String returnValue) throws ReturnValueParseException {
        DiskInfo diskInfo = new DiskInfo();

        Matcher matcher = DISK_INFO_PATTERN.matcher(returnValue);
        ArrayList<DiskInfo.Disk> diskList = new ArrayList<>();
        while (matcher.find()) {
            DiskInfo.Disk disk = new DiskInfo.Disk();
            disk.setName(matcher.group(0));
            if (DISK_ROTA_PATTERN.matcher(matcher.group(0)).find()) {
                disk.setType(DiskInfo.DiskType.ROTA);
            } else if (DISK_NVME_PATTERN.matcher(matcher.group(0)).find()) {
                disk.setType(DiskInfo.DiskType.NVME);
            }
            diskList.add(disk);
        }
        diskInfo.setDisksList(diskList);

        return diskInfo;
    }
}
