package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.entity.AbstractEntity;
import com.hw.globalcachesdk.entity.ErrorCodeEntity;
import com.hw.globalcachesdk.exception.ReturnValueParseException;
import com.hw.globalcachesdk.executor.AbstractCommandExecutorSync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

/**
 * 节点配置文件检查
 * @author 章睿彬
 */
@Configure(path = "/configure/CheckConf.xml")
@Script(path = "/check/conf_check.sh")
public class CheckConf extends AbstractCommandExecutorSync {

    public CheckConf() {
        super(CheckConf.class);
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
