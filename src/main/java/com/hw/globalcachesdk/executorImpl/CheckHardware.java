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
@Script(path = "/check/hardware_check.sh")
public class CheckHardware extends AbstractCommandExecutorSync {

    public CheckHardware() {
        super(CheckHardware.class);
    }

    @Override
    public AbstractEntity parseOf(String returnValue) throws ReturnValueParseException {
        returnValue = returnValue.replace("\n","");

        ErrorCodeEntity entity = new ErrorCodeEntity();
        int errorCode = 0;

        if (returnValue.contains("check mem failed")) {
            errorCode += 1;
        }
        if (returnValue.contains("check cpu failed")) {
            errorCode += 2;
        }

        entity.setErrorCode(errorCode);
        entity.setMessage(returnValue);

        return entity;
    }
}
