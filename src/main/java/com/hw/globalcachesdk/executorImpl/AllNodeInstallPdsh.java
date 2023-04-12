package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.executor.AbstractCommandExecutorAsync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

/**
 * 所有节点安装pdsh
 * @author 章睿彬
 */
@Configure(path = "/configure/AllNodeInstallPdsh.xml")
@Script(path = "/compile/install_pdsh.sh")
public class AllNodeInstallPdsh extends AbstractCommandExecutorAsync {

    public AllNodeInstallPdsh() {
        super(AllNodeInstallPdsh.class);
    }
}
