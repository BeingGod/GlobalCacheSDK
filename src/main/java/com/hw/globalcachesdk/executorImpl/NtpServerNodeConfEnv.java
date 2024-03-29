package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.executor.AbstractCommandExecutorAsync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

@Configure(path = "/configure/NtpServerNodeConfEnv.xml")
@Script(path = "/envs/ceph/configure_ntp_server.sh", suffixCommand = "&& source /etc/profile")
public class NtpServerNodeConfEnv extends AbstractCommandExecutorAsync {
    public NtpServerNodeConfEnv() {
        super(NtpServerNodeConfEnv.class);
    }
}
