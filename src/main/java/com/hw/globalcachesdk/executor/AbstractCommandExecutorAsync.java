package com.hw.globalcachesdk.executor;

import com.hw.globalcachesdk.ExecutePrivilege;
import com.hw.globalcachesdk.GlobalCacheSDK;
import com.hw.globalcachesdk.exception.CommandExecException;
import com.hw.globalcachesdk.exception.ConfigureParserException;
import com.hw.globalcachesdk.utils.ConfigureParser;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Shell命令调用(异步)
 * @author 章睿彬
 */
public abstract class AbstractCommandExecutorAsync extends AbstractCommandExecutor {

    public AbstractCommandExecutorAsync(Class<?> class_) {
        if (!class_.isAnnotationPresent(Configure.class)) {
            return;
        }

        Configure conf = class_.getAnnotation(Configure.class);
        InputStream inputStream = this.getClass().getResourceAsStream(conf.path());
        try {
            // 解析对应注解的接口配置文件
            this.des = ConfigureParser.parse(inputStream);
        } catch (ConfigureParserException e) {
            throw new RuntimeException("settings parse failed", e);
        }

        if (!this.des.isAsync()) {
            throw new RuntimeException("command type error");
        }
    }

    /**
     * 统一执行命令接口
     *
     * @param channel    通道
     * @param args       命令参数
     * @return 命令返回结果
     * @throws CommandExecException 命令执行失败抛出此异常
     */
    public InputStream exec(Channel channel, String args) throws CommandExecException {
        if (!this.getClass().isAnnotationPresent(Script.class)) {
            throw new CommandExecException("executor is not bind shell script, please use @Script to bind");
        }
        if (channel.isClosed()) {
            throw new CommandExecException("channel is not opened");
        }

        Script script =  this.getClass().getAnnotation(Script.class);
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

        return execInternal(channel, command);
    }

    /**
     * 执行命令
     *
     * @param channel 通道
     * @param command 需要执行的命令
     * @return 命令执行结果
     * @throws CommandExecException 命令执行失败抛出此异常
     */
    protected InputStream execInternal(Channel channel, String command) throws CommandExecException {
        try {
            InputStream inputStream = channel.getInputStream();
            OutputStream outputStream = channel.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);

            printWriter.println(command);
            printWriter.println("exit");
            printWriter.flush();

            return inputStream;

        } catch (IOException e) {
            throw new CommandExecException("input or output stream open failed", e);
        }
    }
}
