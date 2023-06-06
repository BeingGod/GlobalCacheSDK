package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.executor.AbstractCommandExecutorAsync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

/**
 * 配置所有节点CephConfEnv
 * @author 李金泽
 */
@Configure(path = "/configure/CephNodeConfEnv.xml")
@Script(path = "/envs/ceph/configure_ceph_env.sh", suffixCommand = "&& source /etc/profile")
public class CephNodeConfEnv extends AbstractCommandExecutorAsync {

    public CephNodeConfEnv() {
        super(CephNodeConfEnv.class);
    }

}
