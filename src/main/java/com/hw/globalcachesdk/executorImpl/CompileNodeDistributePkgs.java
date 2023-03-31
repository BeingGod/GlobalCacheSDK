package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.executor.AbstractCommandExecutorAsync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

/**
 * 分发安装包
 * @author 章睿彬
 */
@Configure(path = "/configure/CompileNodeDistributePkgs.xml")
@Script(path = "/home/GlobalCacheScripts/compile/distribute_packages.sh")
public class CompileNodeDistributePkgs extends AbstractCommandExecutorAsync {

    public CompileNodeDistributePkgs() {
        super(CompileNodeDistributePkgs.class);
    }

}
