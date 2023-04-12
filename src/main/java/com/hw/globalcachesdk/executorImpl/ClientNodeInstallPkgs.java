package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.executor.AbstractCommandExecutorAsync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

/**
 * client节点安装软件包
 * @author 章睿彬
 */
@Configure(path = "/configure/ClientNodeInstallPkgs.xml")
@Script(path = "/deploy/client/client_install.sh")
public class ClientNodeInstallPkgs extends AbstractCommandExecutorAsync {
    public ClientNodeInstallPkgs() {
        super(ClientNodeInstallPkgs.class);
    }
}
