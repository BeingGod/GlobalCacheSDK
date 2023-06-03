package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.executor.AbstractCommandExecutorAsync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

/**
 * 格式化磁盘
 * @author 章睿彬
 */
@Configure(path = "/configure/CephNodeDiskPartition.xml")
@Script(path = "/deploy/ceph/partition.sh")
public class CephNodeDiskPartition extends AbstractCommandExecutorAsync {
    public CephNodeDiskPartition() {
        super(CephNodeDiskPartition.class);
    }
}
