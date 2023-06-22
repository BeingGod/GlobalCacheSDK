package com.hw.globalcachesdk.executor;

import cn.hutool.extra.ssh.JschUtil;
import com.hw.globalcachesdk.ExecutePrivilege;
import com.hw.globalcachesdk.GlobalCacheSDK;
import com.hw.globalcachesdk.entity.AbstractEntity;
import com.hw.globalcachesdk.exception.CommandExecException;
import com.hw.globalcachesdk.exception.ConfigureParserException;
import com.hw.globalcachesdk.exception.ReturnValueParseException;
import com.hw.globalcachesdk.utils.ConfigureParser;
import com.jcraft.jsch.Session;

import java.io.*;
import java.nio.charset.Charset;

/**
 * Shell命令调用(同步)
 * @author 章睿彬
 */
public abstract class AbstractCommandExecutorSync extends AbstractCommandExecutor {

    public AbstractCommandExecutorSync(Class<?> class_) {
        if (!class_.isAnnotationPresent(Configure.class)) {
            return;
        }

        Configure conf = class_.getAnnotation(Configure.class);
        InputStream inputStream = this.getClass().getResourceAsStream(conf.path());
        try {
            // 解析对应注解的接口配置文件
            this.des = ConfigureParser.parse(inputStream);
        } catch (ConfigureParserException e) {
            System.err.println(class_.getName() + "配置文件解析失败");
            throw new RuntimeException("配置文件解析失败", e);
        }

        if (this.des.isAsync()) {
            System.err.println(class_.getName() + "不为同步命令");
            throw new RuntimeException("命令类型错误");
        }
    }

    /**
     * 统一执行命令接口
     *
     * @param sshSession SSH会话
     * @param args       命令参数
     * @return 命令返回结果
     * @throws CommandExecException 命令执行失败抛出此异常
     */
    public String exec(Session sshSession, String args) throws CommandExecException {
        if (!this.getClass().isAnnotationPresent(Script.class)) {
            throw new CommandExecException("当前Executor未绑定Shell脚本");
        }

        Script script = this.getClass().getAnnotation(Script.class);
        String prefixCommand = script.prefixCommand();
        String suffixCommand = script.suffixCommand();
        String path = GlobalCacheSDK.getScriptsPath() + script.path();
        String command = "";
        // TODO: 优化逻辑
        if ("".equals(prefixCommand) && ! "".equals(suffixCommand)) {
            command = "bash" + " " + path + " " + args + " " + suffixCommand;
        }
        if ("".equals(suffixCommand) && ! "".equals(prefixCommand)) {
            command = prefixCommand + " " + "bash" + " " + path + " " + args;
        }
        if (! "".equals(suffixCommand) && ! "".equals(prefixCommand)) {
            command = prefixCommand + " " + "bash" + " " + path + " " + args + " " + suffixCommand;
        }
        if ("".equals(suffixCommand) && "".equals(prefixCommand)) {
            command = "bash" + " " + path + " " + args;
        }

        return execInternal(sshSession, command);
    }

    /**
     * 返回结果解析接口
     * 注意: 所有子类需要实现该方法
     *
     * @param returnValue exec执行命令返回结果
     * @return 装箱后的命令结果
     * @throws ReturnValueParseException 字符串解析失败时抛出此异常
     */
    public abstract AbstractEntity parseOf(String returnValue) throws ReturnValueParseException;

    /**
     * 执行命令
     *
     * @param sshSession SSH会话
     * @param command 需要执行的命令
     * @return 命令执行结果
     * @throws CommandExecException 命令执行失败抛出此异常
     */
    protected String execInternal(Session sshSession, String command) throws CommandExecException {
        String returnValue = JschUtil.exec(sshSession, command, Charset.defaultCharset());

        return returnValue;
    }
}
