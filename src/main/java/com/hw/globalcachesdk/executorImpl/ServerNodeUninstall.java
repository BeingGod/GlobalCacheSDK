package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.executor.AbstractCommandExecutorAsync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

/**
 * 卸载服务端节点globalcache
 * @author 章睿彬
 */
@Configure(path = "/configure/ServerNodeUninstall.xml")
@Script(path = "/deploy/server/server_uninstall.sh")
public class ServerNodeUninstall extends AbstractCommandExecutorAsync {
    public ServerNodeUninstall() {
        super(ServerNodeUninstall.class);
    }
}
