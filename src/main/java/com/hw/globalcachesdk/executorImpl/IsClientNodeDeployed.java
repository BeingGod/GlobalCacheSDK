package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.entity.AbstractEntity;
import com.hw.globalcachesdk.entity.ErrorCodeEntity;
import com.hw.globalcachesdk.exception.ReturnValueParseException;
import com.hw.globalcachesdk.executor.AbstractCommandExecutorSync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;


/**
 * Client节点部署检查
 * @author 章睿彬
 */
@Configure(path = "/configure/IsClientNodeDeployed.xml")
@Script(path = "/deploy/client/client_check.sh", suffixCommand = "> /dev/null && echo $?")
public class IsClientNodeDeployed extends AbstractCommandExecutorSync {

    public IsClientNodeDeployed() {
        super(IsClientNodeDeployed.class);
    }

    @Override
    public AbstractEntity parseOf(String returnValue) throws ReturnValueParseException {
        returnValue = returnValue.replace("\n","");

        ErrorCodeEntity entity = new ErrorCodeEntity();
        int errorCode = Integer.parseInt(returnValue);
        entity.setErrorCode(errorCode);

        switch (errorCode) {
            case 0:
                entity.setMessage("Client has been deployed!");
                break;
            case 1:
                entity.setMessage("Client is not deployed!");
                break;
            default:
                entity.setMessage("Unknown");
                break;
        }

        return entity;
    }
}
