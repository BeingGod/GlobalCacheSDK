package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.entity.AbstractEntity;
import com.hw.globalcachesdk.entity.ErrorCodeEntity;
import com.hw.globalcachesdk.exception.ReturnValueParseException;
import com.hw.globalcachesdk.executor.AbstractCommandExecutor;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Global Cache 服务控制
 * @author 章睿彬
 */
@Configure(path = "/configure/GlobalCacheServiceControl.xml")
@Script(path = "/home/GlobalCacheScripts/utils/gc_service_control.sh", suffixCommand = "> /dev/null && echo $?")
public class GlobalCacheServiceControl extends AbstractCommandExecutor {

    /**
     * 节点gcServiceControl信息正则表达式
     */
    private static final Pattern GC_SERVICE_CONTROL_PATTERN = Pattern.compile("\\d+");

    public GlobalCacheServiceControl() {
        super(GlobalCacheServiceControl.class);
    }

    @Override
    public AbstractEntity parseOf(String returnValue) throws ReturnValueParseException {
        Matcher matcher = GC_SERVICE_CONTROL_PATTERN.matcher(returnValue);

        ErrorCodeEntity errorCodeEntity = new ErrorCodeEntity();
        if (matcher.find()) {
            errorCodeEntity.setErrorCode(Integer.parseInt(matcher.group(0)));
        }

        return errorCodeEntity;
    }
}
