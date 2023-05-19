package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.entity.AbstractEntity;
import com.hw.globalcachesdk.entity.ErrorCodeEntity;
import com.hw.globalcachesdk.exception.ReturnValueParseException;
import com.hw.globalcachesdk.executor.AbstractCommandExecutorSync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

/**
 * Client节点GlobalCache部署检查
 * @author 章睿彬
 */
@Configure(path = "/configure/CheckClient.xml")
@Script(path = "/check/client_check.sh")
public class CheckClient extends AbstractCommandExecutorSync {
    public CheckClient() {
        super(CheckClient.class);
    }

    @Override
    public AbstractEntity parseOf(String returnValue) throws ReturnValueParseException {
        ErrorCodeEntity entity = new ErrorCodeEntity();
        if (returnValue.contains("FATAL")) {
            entity.setErrorCode(1);
        } else {
            entity.setErrorCode(0);
        }
        entity.setMessage(returnValue);

        return entity;
    }
}
