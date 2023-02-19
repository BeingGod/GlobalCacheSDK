package com.hw.globalcachesdk.executor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 配置CommandExecutor默认XML配置路径
 * @author 章睿彬
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Configure {
    /**
     * 设置XML文件路径
     * @return XML路径
     */
    String path();
}
