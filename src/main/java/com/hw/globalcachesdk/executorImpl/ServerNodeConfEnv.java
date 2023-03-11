package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.executor.AbstractCommandExecutorAsync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

/**
 * 配置GC客户端节点环境
 * @author 章睿彬
 */
@Configure(path = "/configure/ServerodeConfEnv.xml")
@Script(path = "/home/GlobalCacheScripts/envs/configure/client/configure_server_env.sh")
public class ServerNodeConfEnv extends AbstractCommandExecutorAsync {
    public ServerNodeConfEnv() {
        super(ServerNodeConfEnv.class);
    }
}
