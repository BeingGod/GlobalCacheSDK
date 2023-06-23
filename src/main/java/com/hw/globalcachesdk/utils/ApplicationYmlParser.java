package com.hw.globalcachesdk.utils;

import com.hw.globalcachesdk.exception.ApplicationYmlParserException;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

/**
 * YML配置文件解析器
 * @author 章睿彬
 */
public class ApplicationYmlParser {

    public static Map<String, Object> parse(InputStream inputStream) throws ApplicationYmlParserException {
        // 读取 YAML 文件
        try {
            // 解析 YAML 文件
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(inputStream);

            return data;
        } catch (Exception e) {
            throw new ApplicationYmlParserException("parse failed");
        }
    }
}
