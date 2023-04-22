package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.entity.AbstractEntity;
import com.hw.globalcachesdk.entity.ErrorCodeEntity;
import com.hw.globalcachesdk.exception.ReturnValueParseException;
import com.hw.globalcachesdk.executor.AbstractCommandExecutorSync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;


/**
 * 依赖包构建检查
 * @author 章睿彬
 */
@Configure(path = "/configure/IsPkgsBuildSuccessOnServer.xml")
@Script(path = "/compile/compile_check_server.sh", suffixCommand = "> /dev/null && echo $?")
public class IsPkgsBuildSuccessOnServer extends AbstractCommandExecutorSync {

    public IsPkgsBuildSuccessOnServer() {
        super(IsPkgsBuildSuccessOnServer.class);
    }

    @Override
    public AbstractEntity parseOf(String returnValue) throws ReturnValueParseException {
        returnValue = returnValue.replace("\n","");

        ErrorCodeEntity entity = new ErrorCodeEntity();
        int errorCode = Integer.parseInt(returnValue);
        entity.setErrorCode(errorCode);

        switch (errorCode) {
            case 0:
                entity.setMessage("All dependency packages build successfully!");
                break;
            case 1:
                entity.setMessage("Dependency packages build failed, please check log...");
                break;
            default:
                entity.setMessage("Unknown");
                break;
        }

        return entity;
    }
}
