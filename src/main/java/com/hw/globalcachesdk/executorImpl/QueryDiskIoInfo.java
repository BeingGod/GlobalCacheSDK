package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.entity.AbstractEntity;
import com.hw.globalcachesdk.entity.DiskIoInfo;
import com.hw.globalcachesdk.exception.ReturnValueParseException;
import com.hw.globalcachesdk.executor.AbstractCommandExecutorSync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

import java.util.ArrayList;

/**
 * 请求节点磁盘IO状态
 * @author 李金泽
 */
@Configure(path = "/configure/QueryDiskIoInfo.xml")
@Script(path = "/home/GlobalCacheScriptsNew/data/disk_io/disk_io.sh")
public class QueryDiskIoInfo extends AbstractCommandExecutorSync {

    public QueryDiskIoInfo() {
        super(QueryDiskIoInfo.class);
    }

    @Override
    public AbstractEntity parseOf(String returnValue) throws ReturnValueParseException {
        //按行切分
        String[] returnValueList = returnValue.split("\n");
        //数据返回对象
        DiskIoInfo diskIoInfo = new DiskIoInfo();
        //磁盘列表
        ArrayList<DiskIoInfo.DiskIo> diskIos = new ArrayList<>();
        for (int i = 4; i < returnValueList.length; i++) {
            DiskIoInfo.DiskIo diskIo = new DiskIoInfo.DiskIo();
            //消除所有连续的空格，为按空格切分做准备
            while(returnValueList[i].contains("  ")){
                returnValueList[i] = returnValueList[i].replaceAll("  "," ");
            }
            String[] diskInfoList = returnValueList[i].split(" ");
            diskIo.setDiskName(diskInfoList[0]);
            diskIo.setTps(Float.parseFloat(diskInfoList[1]));
            diskIo.setKbRead(Float.parseFloat(diskInfoList[2]));
            diskIo.setKbWrite(Float.parseFloat(diskInfoList[3]));
            diskIo.setKbReadTotal(diskInfoList[4]);
            diskIo.setKbWrittenTotal(diskInfoList[5]);
            diskIos.add(diskIo);
        }
        diskIoInfo.setDiskIoList(diskIos);

        return diskIoInfo;
    }
}
