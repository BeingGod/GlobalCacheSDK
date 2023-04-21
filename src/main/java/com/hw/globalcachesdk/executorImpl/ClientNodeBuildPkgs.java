package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.executor.AbstractCommandExecutorAsync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

/**
 * 编译依赖包
 * @author 章睿彬
 */
@Configure(path = "/configure/ClientNodeBuildPkgs.xml")
@Script(path = "/compile/compile_build_client.sh")
public class ClientNodeBuildPkgs extends AbstractCommandExecutorAsync {

    public ClientNodeBuildPkgs() {
        super(ClientNodeBuildPkgs.class);
    }

}
