package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.executor.AbstractCommandExecutorAsync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

/**
 * 编译依赖包
 * @author 章睿彬
 */
@Configure(path = "/configure/CompileNodeBuildPkgs.xml")
@Script(path = "/home/GlobalCacheScriptsNew/compile/compile_build.sh")
public class CompileNodeBuildPkgs extends AbstractCommandExecutorAsync {

    public CompileNodeBuildPkgs() {
        super(CompileNodeBuildPkgs.class);
    }

}
