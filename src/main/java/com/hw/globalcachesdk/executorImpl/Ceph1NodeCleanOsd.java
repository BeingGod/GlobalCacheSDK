package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.executor.AbstractCommandExecutorAsync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

/**
 * 清理OSD
 * @author 章睿彬
 */
@Configure(path = "/configure/Ceph1NodeCleanOsd.xml")
@Script(path = "/deploy/ceph/clean_osd.sh")
public class Ceph1NodeCleanOsd extends AbstractCommandExecutorAsync {
    public Ceph1NodeCleanOsd() {
        super(Ceph1NodeCleanOsd.class);
    }
}
