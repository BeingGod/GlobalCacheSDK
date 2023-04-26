package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.executor.AbstractCommandExecutorAsync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

/**
 * 配置GC客户端节点环境
 * @author 章睿彬
 */
@Configure(path = "/configure/ClientNodeConfEnv.xml")
@Script(path = "/envs/configure/client/configure_client_env.sh", suffixCommand = "&& source /etc/profile")
public class ClientNodeConfEnv extends AbstractCommandExecutorAsync {
    public ClientNodeConfEnv() {
        super(ClientNodeConfEnv.class);
    }
}
