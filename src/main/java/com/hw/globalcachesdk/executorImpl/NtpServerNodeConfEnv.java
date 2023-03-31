package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.executor.AbstractCommandExecutorAsync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

@Configure(path = "/configure/NtpServerNodeConfEnv.xml")
@Script(path = "/home/GlobalCacheScripts/envs/configure/ceph/configure_ntp_server.sh")
public class NtpServerNodeConfEnv extends AbstractCommandExecutorAsync {
    public NtpServerNodeConfEnv() {
        super(NtpServerNodeConfEnv.class);
    }
}
