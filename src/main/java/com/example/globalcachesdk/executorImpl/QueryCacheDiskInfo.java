package com.example.globalcachesdk.executorImpl;

import com.example.globalcachesdk.entity.AbstractEntity;
import com.example.globalcachesdk.entity.CacheDiskInfo;
import com.example.globalcachesdk.exception.CommandExecException;
import com.example.globalcachesdk.executor.AbstractCommandExecutor;
import com.example.globalcachesdk.executor.Configure;
import com.jcraft.jsch.Session;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 请求缓存盘信息
 * @author 章睿彬, 蔡润培
 */
@Configure(path="/configure/QueryCacheDiskInfo.xml")
public class QueryCacheDiskInfo extends AbstractCommandExecutor {
    private static final Pattern CACHE_INFO_PATTERN = Pattern.compile(".*\\n");
    public QueryCacheDiskInfo() {
        super(QueryCacheDiskInfo.class);
    }

    @Override
    public AbstractEntity exec(Session sshSession, String args) throws CommandExecException {
        String command = "bash /home/GlobalCacheScripts/SDK/cache_disk_info.sh | grep diskId";
        String returnValue = execInternal(sshSession, command);

        CacheDiskInfo cacheDiskInfo = new CacheDiskInfo();
        Matcher matcher = CACHE_INFO_PATTERN.matcher(returnValue);
        ArrayList<CacheDiskInfo.CacheDisk> cacheDiskList = new ArrayList<>();
        while (matcher.find()) {
            CacheDiskInfo.CacheDisk cacheDisk = new CacheDiskInfo.CacheDisk();
            String[] value = matcher.group(0).split("diskId: |, |diskName: |diskSn: |capacity: |state: ");

            cacheDisk.setDiskId(Integer.parseInt(value[1]));
            cacheDisk.setDiskSn(value[5]);
            cacheDisk.setCapacity(Integer.parseInt(value[7].split("\\(MB\\)")[0]));
            if (value[9].trim().equals("VDISK_STATE_UP")) {
                cacheDisk.setState(CacheDiskInfo.CacheDiskState.VDISK_STATE_UP);
            } else if (value[9].trim().equals("VDISK_STATE_DOWN")) {
                cacheDisk.setState(CacheDiskInfo.CacheDiskState.VDISK_STATE_DOWN);
            }
            cacheDiskList.add(cacheDisk);
        }

        cacheDiskInfo.setDiskList(cacheDiskList);
        return cacheDiskInfo;
    }
}
