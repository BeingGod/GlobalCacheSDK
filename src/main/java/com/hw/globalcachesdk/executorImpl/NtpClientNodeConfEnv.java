package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.executor.AbstractCommandExecutorAsync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

@Configure(path = "/configure/NtpClientNodeConfEnv.xml")
@Script(path = "/envs/configure/ceph/configure_ntp_client.sh")
public class NtpClientNodeConfEnv extends AbstractCommandExecutorAsync {
    public NtpClientNodeConfEnv() {
        super(NtpClientNodeConfEnv.class);
    }
}
