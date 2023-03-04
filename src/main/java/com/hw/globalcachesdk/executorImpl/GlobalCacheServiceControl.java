package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.executor.AbstractCommandExecutorAsync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

/**
 * Global Cache 服务控制
 * @author 章睿彬
 */
@Configure(path = "/configure/GlobalCacheServiceControl.xml")
@Script(path = "/home/GlobalCacheScripts/utils/gc_service_control.sh")
public class GlobalCacheServiceControl extends AbstractCommandExecutorAsync {

    public GlobalCacheServiceControl() {
        super(GlobalCacheServiceControl.class);
    }
}
