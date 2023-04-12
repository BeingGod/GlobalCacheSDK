package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.executor.AbstractCommandExecutorAsync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

/**
 * 配置所有节点CephConfEnv
 * @author 李金泽
 */
@Configure(path = "/configure/AllNodeCephConfEnv.xml")
@Script(path = "/envs/configure/ceph/configure_ceph_env.sh")
public class AllNodeCephConfEnv extends AbstractCommandExecutorAsync {

    public AllNodeCephConfEnv() {
        super(AllNodeCephConfEnv.class);
    }

}
