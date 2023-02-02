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
    private static final Pattern CACHE_INFO_PATTERN = Pattern.compile("^.*\\n");
    public QueryCacheDiskInfo() {
        super(QueryCacheDiskInfo.class);
    }

    @Override
    public AbstractEntity exec(Session sshSession, String args) throws CommandExecException {
        System.out.println("exec"); // ## DEBUG ##
        String command = "bash /home/GlobalCacheScripts/SDK/cache_disk_info.sh | grep diskId";
        String returnValue = execInternal(sshSession, command);

        CacheDiskInfo cacheDiskInfo = new CacheDiskInfo();

        Matcher matcher = CACHE_INFO_PATTERN.matcher(returnValue);
        ArrayList<CacheDiskInfo.CacheDisk> cacheDiskList = new ArrayList<>();
        while (matcher.find()) {
            CacheDiskInfo.CacheDisk cacheDisk = cacheDiskInfo.new CacheDisk();
            String value = matcher.group(0);
            cacheDisk.setDiskId(Integer.parseInt(value.substring(value.lastIndexOf("diskId: ") + 1, value.indexOf(", diskName"))));
            // TO-DO
        }
        cacheDiskInfo.setDiskList(cacheDiskList);
        return cacheDiskInfo;
    }
}
