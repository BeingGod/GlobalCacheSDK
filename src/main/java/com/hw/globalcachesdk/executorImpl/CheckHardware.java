package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.entity.AbstractEntity;
import com.hw.globalcachesdk.entity.ErrorCodeEntity;
import com.hw.globalcachesdk.exception.ReturnValueParseException;
import com.hw.globalcachesdk.executor.AbstractCommandExecutorSync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;


/**
 * 节点硬件检查
 * @author 章睿彬
 */
@Configure(path = "/configure/CheckHardware.xml")
@Script(path = "/home/GlobalCacheScriptsNew/envs/check/check_hardware.sh", suffixCommand = "> /dev/null && echo $?")
public class CheckHardware extends AbstractCommandExecutorSync {

    public CheckHardware() {
        super(CheckHardware.class);
    }

    @Override
    public AbstractEntity parseOf(String returnValue) throws ReturnValueParseException {
        returnValue = returnValue.replace("\n","");

        ErrorCodeEntity entity = new ErrorCodeEntity();
        int errorCode = Integer.parseInt(returnValue);
        entity.setErrorCode(errorCode);

        switch (errorCode) {
            case 0:
                entity.setMessage("The hardware environment meets the requirements");
                break;
            case 1:
                entity.setMessage("CPU is not satisfied!");
                break;
            case 2:
                entity.setMessage("Memory is not satisified!");
                break;
            default:
                entity.setMessage("Unknown");
                break;
        }

        return entity;
    }
}
