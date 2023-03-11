package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.executor.AbstractCommandExecutorAsync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

/**
 * 部署Ceph
 * @author 章睿彬
 */
@Configure(path = "/configure/Ceph1NodeDeployCeph.xml")
@Script(path = "/home/GlobalCacheScripts/deploy/ceph/deploy_ceph.sh")
public class Ceph1NodeDeployCeph extends AbstractCommandExecutorAsync {
    public Ceph1NodeDeployCeph() {
        super(Ceph1NodeDeployCeph.class);
    }
}
