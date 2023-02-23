package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.entity.AbstractEntity;
import com.hw.globalcachesdk.entity.CacheDiskInfo;
import com.hw.globalcachesdk.exception.ReturnValueParseException;
import com.hw.globalcachesdk.executor.AbstractCommandExecutor;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 请求缓存盘信息
 * @author 章睿彬, 蔡润培
 */
@Configure(path = "/configure/QueryCacheDiskInfo.xml")
@Script(path = "/home/GlobalCacheScripts/SDK/cache_disk_info.sh", suffixCommand = "| grep diskId")
public class QueryCacheDiskInfo extends AbstractCommandExecutor {
    private static final Pattern CACHE_INFO_PATTERN = Pattern.compile(".*\\n");
    public QueryCacheDiskInfo() {
        super(QueryCacheDiskInfo.class);
    }

    @Override
    public AbstractEntity parseOf(String returnValue) throws ReturnValueParseException {
        CacheDiskInfo cacheDiskInfo = new CacheDiskInfo();
        Matcher matcher = CACHE_INFO_PATTERN.matcher(returnValue);
        ArrayList<CacheDiskInfo.CacheDisk> cacheDiskList = new ArrayList<>();
        while (matcher.find()) {
            CacheDiskInfo.CacheDisk cacheDisk = new CacheDiskInfo.CacheDisk();
            String[] value = matcher.group(0).split("diskId: |, |diskName: |diskSn: |capacity: |state: ");

            cacheDisk.setDiskId(Integer.parseInt(value[1]));
            cacheDisk.setDiskName(value[3].replaceAll("/dev/",""));
            cacheDisk.setDiskSn(value[5]);
            cacheDisk.setCapacity(Integer.parseInt(value[7].split("\\(MB\\)")[0]));
            if ("VDISK_STATE_UP".equals(value[9].trim())) {
                cacheDisk.setState(CacheDiskInfo.CacheDiskState.VDISK_STATE_UP);
            } else if ("VDISK_STATE_DOWN".equals(value[9].trim())) {
                cacheDisk.setState(CacheDiskInfo.CacheDiskState.VDISK_STATE_DOWN);
            }
            cacheDiskList.add(cacheDisk);
        }

        cacheDiskInfo.setDiskList(cacheDiskList);
        return cacheDiskInfo;
    }
}
