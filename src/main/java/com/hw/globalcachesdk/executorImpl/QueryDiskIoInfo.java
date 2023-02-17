package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.entity.AbstractEntity;
import com.hw.globalcachesdk.entity.DiskIoInfo;
import com.hw.globalcachesdk.exception.CommandExecException;
import com.hw.globalcachesdk.executor.AbstractCommandExecutor;
import com.hw.globalcachesdk.executor.Configure;
import com.jcraft.jsch.Session;

import java.util.ArrayList;

/**
 * 请求节点磁盘IO状态
 * @author 李金泽
 */
@Configure(path= "/configure/QueryDiskIoInfo.xml")
public class QueryDiskIoInfo extends AbstractCommandExecutor {

    public QueryDiskIoInfo() {
        super(QueryDiskIoInfo.class);
    }

    @Override
    public AbstractEntity exec(Session sshSession, String args) throws CommandExecException {
        String command = "bash /home/GlobalCacheScripts/SDK/disk_IO_info.sh";
        //执行命令
        String returnValue = execInternal(sshSession, command);
        //按行切分
        String[] returnValueList = returnValue.split("\n");
        //数据返回对象
        DiskIoInfo diskIoInfo=new DiskIoInfo();
        //磁盘列表
        ArrayList<DiskIoInfo.Disk> disks=new ArrayList<>();
        for (int i = 4; i < returnValueList.length; i++) {
            DiskIoInfo.Disk disk=new DiskIoInfo.Disk();
            //消除所有连续的空格，为按空格切分做准备
            while(returnValueList[i].contains("  ")){
                returnValueList[i]=returnValueList[i].replaceAll("  "," ");
            }
            String[] diskInfoList = returnValueList[i].split(" ");
            disk.setDiskName(diskInfoList[0]);
            disk.setTps(Float.parseFloat(diskInfoList[1]));
            disk.setKbRead(Float.parseFloat(diskInfoList[2]));
            disk.setKbWrite(Float.parseFloat(diskInfoList[3]));
            disk.setKbReadTotal(diskInfoList[4]);
            disk.setKbWrittenTotal(diskInfoList[5]);
            disks.add(disk);
        }
        diskIoInfo.setDiskArrayList(disks);
        return diskIoInfo;
    }
}
