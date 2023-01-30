package com.example.globalcachesdk.executorImpl;

import com.example.globalcachesdk.entity.AbstractEntity;
import com.example.globalcachesdk.entity.DiskInfo;
import com.example.globalcachesdk.exception.CommandExecException;
import com.example.globalcachesdk.executor.AbstractCommandExecutor;
import com.example.globalcachesdk.executor.Configure;
import com.jcraft.jsch.Session;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 请求节点磁盘信息
 * @author 章睿彬, 蔡润培
 */
@Configure(path="/configure/QueryDiskInfo.xml")
public class QueryDiskInfo extends AbstractCommandExecutor {

    private static final Pattern DISK_INFO_PATTERN = Pattern.compile("[a-z].*");
    private static final Pattern DISK_ROTA_PATTERN = Pattern.compile("^sd.");
    private static final Pattern DISK_NVME_PATTERN = Pattern.compile("^nvme.");
    public QueryDiskInfo() {
        super(QueryDiskInfo.class);
    }

    @Override
    public AbstractEntity exec(Session sshSession, String args) throws CommandExecException {
        String command = "bash /home/GlobalCacheScripts/SDK/disk_info.sh";
        String returnValue = execInternal(sshSession, command);
        DiskInfo diskInfo = new DiskInfo();

        Matcher matcher = DISK_INFO_PATTERN.matcher(returnValue);
        ArrayList<DiskInfo.Disk> diskList = new ArrayList<>();
        while (matcher.find()) {
            DiskInfo.Disk disk = diskInfo.new Disk();
            disk.setName("/dev/" + matcher.group(0));
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
