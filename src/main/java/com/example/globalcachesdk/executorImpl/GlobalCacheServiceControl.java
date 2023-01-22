package com.example.globalcachesdk.executorImpl;

import com.example.globalcachesdk.entity.AbstractEntity;
import com.example.globalcachesdk.entity.ErrorCodeEntity;
import com.example.globalcachesdk.exception.CommandExecException;
import com.example.globalcachesdk.executor.AbstractCommandExecutor;
import com.example.globalcachesdk.executor.Configure;
import com.jcraft.jsch.Session;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Global Cache 服务控制
 * @author 章睿彬
 */
@Configure(path= "/configure/GlobalCacheServiceControl.xml")
public class GlobalCacheServiceControl extends AbstractCommandExecutor {

    /**
     * 节点gcServiceControl信息正则表达式
     */
    private static final Pattern GC_SERVICE_CONTROL_PATTERN = Pattern.compile("\\d+");

    public GlobalCacheServiceControl() {
        super(GlobalCacheServiceControl.class);
    }

    @Override
    public AbstractEntity exec(Session sshSession, String args) throws CommandExecException {
        // @TODO: 命令执行时间较长，用异步避免阻塞
        String command = "bash /home/GlobalCacheScripts/utils/gc_service_control.sh " + args + " > /dev/null && echo $?";

        String returnValue = execInternal(sshSession, command, args);

        Matcher matcher = GC_SERVICE_CONTROL_PATTERN.matcher(returnValue);

        ErrorCodeEntity errorCodeEntity = new ErrorCodeEntity();
        if (matcher.find()) {
            errorCodeEntity.setErrorCode(Integer.parseInt(matcher.group(0)));
        }

        return errorCodeEntity;
    }
}
