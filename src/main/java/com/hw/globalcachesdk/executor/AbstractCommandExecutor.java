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

        int[] flag = {1};
        OutputStream newErrStream = new OutputStream() {
            @Override
            public void write(int b) {
                //TODO：本处输出为ASCII强转实现，UTF-8适配
                System.err.print((char) b);
            }
            @Override
            public void write(byte[] b, int off, int len) {
                flag[0] = 0;
                StringBuilder errMsg= new StringBuilder();
                for (int i = 0; i < len; i++) {
                    errMsg.append((char) b[off + i]);
                }
                System.err.print(errMsg);
            }
        };
        String returnValue = JschUtil.exec(sshSession, command, Charset.defaultCharset(), newErrStream).trim();
        if (flag[0] == 0) {
            throw new CommandExecException("命令执行失败");
        } else {
            return returnValue;
        }
    }
}
