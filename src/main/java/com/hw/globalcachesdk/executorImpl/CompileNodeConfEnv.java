package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.executor.AbstractCommandExecutorAsync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

@Configure(path = "/configure/CompileNodeConfEnv.xml")
@Script(path = "/envs/configure/compile/configure_compile_env.sh", suffixCommand = "&& source /etc/profile")
public class CompileNodeConfEnv extends AbstractCommandExecutorAsync {

    public CompileNodeConfEnv() {
        super(CompileNodeConfEnv.class);
    }
}

