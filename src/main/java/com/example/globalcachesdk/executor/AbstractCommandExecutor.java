package com.example.globalcachesdk.executor;

import cn.hutool.extra.ssh.JschUtil;
import com.example.globalcachesdk.ExecuteNode;
import com.example.globalcachesdk.ExecutePrivilege;
import com.example.globalcachesdk.entity.AbstractEntity;
import com.example.globalcachesdk.exception.CommandExecException;
import com.example.globalcachesdk.exception.ConfParserException;
import com.example.globalcachesdk.utils.ConfigureParser;
import com.jcraft.jsch.Session;

import java.io.OutputStream;
import java.nio.charset.Charset;

import static cn.hutool.core.io.resource.ResourceUtil.getResource;

/**
 * Shell命令调用
 * @author 章睿彬
 */
public abstract class AbstractCommandExecutor {

    /**
     * 命令配置信息
     */
    protected CommandExecutorDescription des = null;

    public AbstractCommandExecutor(Class<?> class_) {
        if (!class_.isAnnotationPresent(Configure.class)) {
            return;
        }

        Configure conf = class_.getAnnotation(Configure.class);
        String path = conf.path();
        java.net.URL resource = this.getClass().getResource(conf.path());

        if (resource == null) {
            System.err.println("未找到配置文件");
            // 采用默认配置
            this.des = new CommandExecutorDescription();
            return;
        }

        String realPath = resource.getPath();
        try {
            // 解析对应注解的接口配置文件
            this.des = ConfigureParser.parse(realPath);
        } catch (ConfParserException e) {
            System.err.println("未知的命令配置文件");
            // 采用默认配置
            this.des = new CommandExecutorDescription();
        }
    }

    /**
     * 统一执行命令接口
     * 注意: 所有子类需要实现该方法
     *
     * @param sshSession SSH会话
     * @param args 命令参数
     * @return 装箱后的命令结果
     * @throws CommandExecException 命令执行失败抛出此异常
     */
    public abstract AbstractEntity exec(Session sshSession, String args) throws CommandExecException;

    public CommandExecutorDescription getDes() {
        return des;
    }

    public void setDes(CommandExecutorDescription des) {
        this.des = des;
    }

    /**
     * 执行命令
     * 通过自定义错误流判断shell脚本是否执行成功
     *
     * @param sshSession SSH会话
     * @param command 需要执行的命令
     * @param args 命令参数
     * @return 命令执行结果
     * @throws CommandExecException 命令执行失败抛出此异常
     */
    protected String execInternal(Session sshSession, String command, String args) throws CommandExecException {
        if (des.isWithArgs() && 0 == args.length()) {
            // 校验参数
            throw new CommandExecException("参数为空");
        }

        if (des.getExecutePrivilege() == ExecutePrivilege.ROOT) {
            // 校验权限
            final String root = "root";
            if (!root.equals(whoami(sshSession))) {
                throw new CommandExecException("用户没有该命令执行权限");
            }
        }

        if (des.getExecutePrivilege() == ExecutePrivilege.GLOBAL_CACHE_OP) {
            // 校验权限
            final String globalcacheop = "globalcacheop";
            if (!globalcacheop.equals(whoami(sshSession))) {
                throw new CommandExecException("用户没有该命令执行权限");
            }
        }

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
        String returnValue = JschUtil.exec(sshSession, command + " " + args, Charset.defaultCharset(), newErrStream);
        if (flag[0] == 0) {
            throw new CommandExecException("命令执行失败");
        } else {
            return returnValue;
        }
    }

    private String whoami(Session sshSession) throws CommandExecException {
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
