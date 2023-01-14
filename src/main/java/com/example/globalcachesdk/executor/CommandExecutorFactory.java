package com.example.globalcachesdk.executor;

import com.example.globalcachesdk.SupportedCommand;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * CommandExecutor工厂类
 * 用于根据Description注册具体的CommandExecutor
 * @author 章睿彬
 */
public class CommandExecutorFactory {
    /**
     *    已注册的方法
     */
    private final HashMap<SupportedCommand, AbstractCommandExecutor> registriedCommandExecutor;

    public CommandExecutorFactory() {
        this.registriedCommandExecutor = new HashMap<>();
    }

    /**
     *
     * @param command 命令类型
     * @param des 需要注册的命令类的描述
     * @return 是否注册成功
     */
    public boolean registryCommandExecutor(SupportedCommand command, CommandExecutorDescription des) {
        if (null != registriedCommandExecutor.get(command)) {
            // 命令已注册
            return false;
        }

        // 获取类对象
        Class<?> class_ = null;
        try {
            class_ = Class.forName(des.getClassPath());
        } catch (ClassNotFoundException e) {
            return false;
        }

        // 获取构造函数
        Constructor<?> constructor = null;
        try {
            constructor = class_.getConstructor();
        } catch (NoSuchMethodException e) {
            return false;
        }

        // 通过构造器对象的newInstance方法进行对象的初始化
        try {
            AbstractCommandExecutor abstractCommandExecutor = (AbstractCommandExecutor) constructor.newInstance();
            registriedCommandExecutor.put(command, (AbstractCommandExecutor) abstractCommandExecutor);

            return true;
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            return false;
        }
    }

    /**
     * 获取CommandExecutor
     *
     * @param command 命令类型
     * @return 需要执行的命令类
     */
    public AbstractCommandExecutor getCommandExecutor(SupportedCommand command) {
        return registriedCommandExecutor.get(command);
    }
}
