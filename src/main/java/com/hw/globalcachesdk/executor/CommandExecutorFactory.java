package com.hw.globalcachesdk.executor;

import com.hw.globalcachesdk.SupportedCommand;
import com.hw.globalcachesdk.exception.CommandExecutorFactoryException;
import com.hw.globalcachesdk.utils.Utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * CommandExecutor工厂类
 * 用于根据Description注册具体的CommandExecutor
 * @author 章睿彬
 */
public class CommandExecutorFactory {
    /**
     * 已注册的方法
     */
    private final HashMap<SupportedCommand, AbstractCommandExecutor> registriedCommandExecutor;

    /**
     * 类地址前缀
     */
    static final String CLASS_PATH_PREFIX = "com.hw.globalcachesdk.executorImpl.";

    public CommandExecutorFactory() throws CommandExecutorFactoryException {
        this.registriedCommandExecutor = new HashMap<>();

        Field[] fields = SupportedCommand.class.getFields();

        // 根据注解的枚举类型, 注册相应的命令类
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Registry.class)) {
                continue;
            }

            // 对有注解的枚举类型进行处理
            String enumName = field.getName();
            String classPath = Utils.enumFullClassPath(CLASS_PATH_PREFIX, enumName);

            // 获取类对象
            Class<?> class_ = null;
            try {
                class_ = Class.forName(classPath);
            } catch (ClassNotFoundException e) {
                throw new CommandExecutorFactoryException("找不到指定类", e);
            }

            // 获取构造函数
            Constructor<?> constructor = null;
            try {
                constructor = class_.getConstructor();
            } catch (NoSuchMethodException e) {
                throw new CommandExecutorFactoryException("找不到指定类构造函数", e);
            }
            // 通过构造器对象的newInstance方法进行对象的初始化
            try {
                AbstractCommandExecutor abstractCommandExecutor = (AbstractCommandExecutor) constructor.newInstance();
                registriedCommandExecutor.put(SupportedCommand.valueOf(field.getName()), abstractCommandExecutor);
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                throw new CommandExecutorFactoryException("类初始化失败", e);

            }
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
