package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.executor.AbstractCommandExecutorAsync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

/**
 * client节点安装Ceph软件包
 * @author 章睿彬
 */
@Configure(path = "/configure/ClientNodeInstallCeph.xml")
@Script(path = "/deploy/client/client_install_ceph.sh")
public class ClientNodeInstallCeph extends AbstractCommandExecutorAsync {
    public ClientNodeInstallCeph() {
        super(ClientNodeInstallCeph.class);
    }
}
