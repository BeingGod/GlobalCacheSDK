package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.entity.AbstractEntity;
import com.hw.globalcachesdk.entity.ErrorCodeEntity;
import com.hw.globalcachesdk.exception.ReturnValueParseException;
import com.hw.globalcachesdk.executor.AbstractCommandExecutorSync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;


/**
 * Ceph节点部署检查
 * @author 章睿彬
 */
@Configure(path = "/configure/IsCephNodeDeployed.xml")
@Script(path = "/home/GlobalCacheScripts/deploy/ceph/ceph_check.sh", suffixCommand = "> /dev/null && echo $?")
public class IsCephNodeDeployed extends AbstractCommandExecutorSync {

    public IsCephNodeDeployed() {
        super(IsCephNodeDeployed.class);
    }

    @Override
    public AbstractEntity parseOf(String returnValue) throws ReturnValueParseException {
        returnValue = returnValue.replace("\n","");

        ErrorCodeEntity entity = new ErrorCodeEntity();
        int errorCode = Integer.parseInt(returnValue);
        entity.setErrorCode(errorCode);

        switch (errorCode) {
            case 0:
                entity.setMessage("Ceph has been deployed!");
                break;
            case 1:
                entity.setMessage("Ceph is not deployed!");
                break;
            default:
                entity.setMessage("Unknown");
                break;
        }

        return entity;
    }
}
