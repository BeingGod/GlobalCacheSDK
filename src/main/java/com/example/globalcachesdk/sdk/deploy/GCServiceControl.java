package com.example.globalcachesdk.sdk.deploy;

import com.example.globalcachesdk.entity.AbstractEntity;
import com.example.globalcachesdk.entity.ErrorCodeEntity;
import com.example.globalcachesdk.exception.CommandExecException;
import com.example.globalcachesdk.executor.AbstractCommandExecutor;
import com.example.globalcachesdk.executor.CommandExecutorDescription;
import com.jcraft.jsch.Session;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Global Cache 服务控制
 * @author 章睿彬
 */
public class GCServiceControl extends AbstractCommandExecutor {

    /**
     * 节点gcServiceControl信息正则表达式
     */
    private static final Pattern GC_SERVICE_CONTROL_PATTERN = Pattern.compile("\\d+");

    public GCServiceControl() {
        super();
        des = defaultDes();
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

    public static CommandExecutorDescription defaultDes() {
        // @TODO: 支持从XML中反转生成
        CommandExecutorDescription des = new CommandExecutorDescription();
        des.setClassPath("com.example.globalcachesdk.sdk.deploy.GCServiceControl");
        des.setExecuteNode(CommandExecutorDescription.ExecuteNode.ALL_NODES);
        des.setExecutePrivilege(CommandExecutorDescription.ExecutePrivilege.ROOT);
        des.setWithArgs(true);
        des.setTimeout(60);

        return des;
    }
}
