package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.entity.AbstractEntity;
import com.hw.globalcachesdk.entity.ErrorCodeEntity;
import com.hw.globalcachesdk.exception.ReturnValueParseException;
import com.hw.globalcachesdk.executor.AbstractCommandExecutorSync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;


/**
 * GlobalCache状态检查
 * @author 章睿彬
 */
@Configure(path = "/configure/IsGlobalCacheServiceRun.xml")
@Script(path = "/home/GlobalCacheScriptsNew/mgr/gc_service_control.sh", suffixCommand = "> /dev/null && echo $?")
public class IsGlobalCacheServiceRun extends AbstractCommandExecutorSync {

    public IsGlobalCacheServiceRun() {
        super(IsGlobalCacheServiceRun.class);
    }

    @Override
    public AbstractEntity parseOf(String returnValue) throws ReturnValueParseException {
        returnValue = returnValue.replace("\n","");

        ErrorCodeEntity entity = new ErrorCodeEntity();
        int errorCode = Integer.parseInt(returnValue);
        entity.setErrorCode(errorCode);

        switch (errorCode) {
            case 0:
                entity.setMessage("Global Cache service is running!");
                break;
            default:
                entity.setMessage("Global Cache service is not running!");
                break;
        }

        return entity;
    }
}
