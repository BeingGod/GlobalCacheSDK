package com.hw.globalcachesdk.executor;

import cn.hutool.extra.ssh.JschUtil;
import com.hw.globalcachesdk.ExecutePrivilege;
import com.hw.globalcachesdk.entity.AbstractEntity;
import com.hw.globalcachesdk.exception.CommandExecException;
import com.hw.globalcachesdk.exception.ConfigureParserException;
import com.hw.globalcachesdk.exception.ReturnValueParseException;
import com.hw.globalcachesdk.utils.ConfigureParser;
import com.jcraft.jsch.Session;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Shell命令调用
 * @author 章睿彬
 */
public abstract class AbstractCommandExecutor {

    /**
     * 命令配置信息
     */
    protected CommandExecutorDescription des = null;

    public CommandExecutorDescription getDes() {
        return des;
    }

    public void setDes(CommandExecutorDescription des) {
        this.des = des;
    }

    protected String whoami(Session sshSession) throws CommandExecException {
        String command = "whoami";

        String returnValue = JschUtil.exec(sshSession, command, Charset.defaultCharset()).trim();

        return returnValue;
    }
}
