package com.hw.globalcachesdk.executor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 配置命令执行Shell脚本路径
 * @author 章睿彬
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Script {
    /**
     * 设置脚本执行路径
     * @return 脚本执行路径
     */
    String path();

    /**
     * 设置前缀命令
     * @return 前缀命令字符串
     */
    String prefixCommand() default "";

    /**
     * 设置后缀命令
     * @return 后缀命令字符串
     */
    String suffixCommand() default "";
}
