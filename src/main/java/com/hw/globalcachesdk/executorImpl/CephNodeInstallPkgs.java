package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.executor.AbstractCommandExecutorAsync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

/**
 * 安装Ceph软件包
 * @author 章睿彬
 */
@Configure(path = "/configure/CephNodeInstallPkgs.xml")
@Script(path = "/home/GlobalCacheScripts/deploy/ceph/install_ceph.sh")
public class CephNodeInstallPkgs extends AbstractCommandExecutorAsync {
    public CephNodeInstallPkgs() {
        super(CephNodeInstallPkgs.class);
    }
}
