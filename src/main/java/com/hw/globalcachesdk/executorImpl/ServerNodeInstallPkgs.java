package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.executor.AbstractCommandExecutorAsync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

/**
 * server节点安装软件包
 * @author 章睿彬
 */
@Configure(path = "/configure/ServerNodeInstallPkgs.xml")
@Script(path = "/deploy/server/server_install.sh")
public class ServerNodeInstallPkgs extends AbstractCommandExecutorAsync {
    public ServerNodeInstallPkgs() {
        super(ServerNodeInstallPkgs.class);
    }
}
