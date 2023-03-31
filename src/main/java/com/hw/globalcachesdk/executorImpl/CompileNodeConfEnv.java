package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.executor.AbstractCommandExecutorAsync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

@Configure(path = "/configure/CompileNodeConfEnv.xml")
@Script(path = "/home/GlobalCacheScripts/envs/configure/compile/configure_compile_env.sh")
public class CompileNodeConfEnv extends AbstractCommandExecutorAsync {

    public CompileNodeConfEnv() {
        super(CompileNodeConfEnv.class);
    }
}

