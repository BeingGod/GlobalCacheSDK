package com.hw.globalcachesdk.executor;

import cn.hutool.extra.ssh.JschUtil;
import com.hw.globalcachesdk.ExecutePrivilege;
import com.hw.globalcachesdk.entity.AbstractEntity;
import com.hw.globalcachesdk.exception.CommandExecException;
import com.hw.globalcachesdk.exception.ConfigureParserException;
import com.hw.globalcachesdk.exception.ReturnValueParseException;
import com.hw.globalcachesdk.utils.ConfigureParser;
import com.jcraft.jsch.Session;

import java.io.OutputStream;
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
        java.net.URL resource = this.getClass().getResource(conf.path());

        if (resource == null) {
            System.err.println("未找到" + class_.getName() + "配置文件");
            throw new RuntimeException("未找到配置文件");
        }

        String realPath = resource.getPath();
        try {
            // 解析对应注解的接口配置文件
            this.des = ConfigureParser.parse(realPath);
        } catch (ConfigureParserException e) {
            System.err.println(class_.getName() + "配置文件解析失败");
            throw new RuntimeException("配置文件解析失败");
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
        String path = script.path();
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
     * 通过自定义错误流判断shell脚本是否执行成功
     *
     * @param sshSession SSH会话
     * @param command 需要执行的命令
     * @return 命令执行结果
     * @throws CommandExecException 命令执行失败抛出此异常
     */
    protected String execInternal(Session sshSession, String command) throws CommandExecException {
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
        String returnValue = JschUtil.exec(sshSession, command, Charset.defaultCharset(), newErrStream);
        if (flag[0] == 0) {
            throw new CommandExecException("命令执行失败");
        } else {
            return returnValue;
        }
    }
}
