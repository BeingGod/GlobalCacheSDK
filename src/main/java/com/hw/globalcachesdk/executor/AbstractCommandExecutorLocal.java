package com.hw.globalcachesdk.executor;

import cn.hutool.extra.ssh.JschUtil;
import com.hw.globalcachesdk.GlobalCacheSDK;
import com.hw.globalcachesdk.entity.AbstractEntity;
import com.hw.globalcachesdk.exception.CommandExecException;
import com.hw.globalcachesdk.exception.ConfigureParserException;
import com.hw.globalcachesdk.exception.ReturnValueParseException;
import com.hw.globalcachesdk.utils.ConfigureParser;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Shell命令调用(同步)
 * @author 章睿彬
 */
public abstract class AbstractCommandExecutorLocal extends AbstractCommandExecutor {

    public AbstractCommandExecutorLocal(Class<?> class_) {
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

        if (this.des.isAsync()) {
            throw new RuntimeException("command type error");
        }
    }

    /**
     * 统一执行命令接口
     *
     * @param args       命令参数
     * @return 命令返回结果
     * @throws CommandExecException 命令执行失败抛出此异常
     */
    public String exec(String args) throws CommandExecException {
        if (!this.getClass().isAnnotationPresent(Script.class)) {
            throw new CommandExecException("executor is not bind shell script, please use @Script to bind");
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

        return execInternal(command);
    }

    public String exec() throws CommandExecException {
        return exec("");
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
     * @param command 需要执行的命令
     * @return 命令执行结果
     * @throws CommandExecException 命令执行失败抛出此异常
     */
    protected String execInternal(String command) throws CommandExecException {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));

            StringBuilder returnValue = new StringBuilder();

            String line = "";
            while ((line = input.readLine()) != null) {
                returnValue.append(line);
            }
            input.close();

            return returnValue.toString();
        } catch (IOException e) {
            throw new CommandExecException("process exec io exception ", e);
        }
    }
}
