package com.hw.globalcachesdk.utils;

import com.hw.globalcachesdk.ExecuteNode;
import com.hw.globalcachesdk.ExecutePrivilege;
import com.hw.globalcachesdk.exception.ConfigureParserException;
import com.hw.globalcachesdk.executor.CommandExecutorDescription;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * XML配置文件解析器
 * @author 章睿彬
 */
public class ConfigureParser {
    /**
     * XML配置文件解析器
     * @param path 文件路径
     * @return CommandExecutorDescription
     * @throws ConfigureParserException 解析失败抛出此异常
     */
    public static CommandExecutorDescription parse(String path) throws ConfigureParserException {
        File f = new File(path);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = null;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(f);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new ConfigureParserException("解析失败", e);
        }

        if (doc.getElementsByTagName("name").getLength() != 1 ||
            doc.getElementsByTagName("async").getLength() != 1 ||
            doc.getElementsByTagName("args").getLength() != 1 ||
            doc.getElementsByTagName("execute").getLength() != 1 ||
            doc.getElementsByTagName("privilege").getLength() != 1 ||
            doc.getElementsByTagName("timeout").getLength() != 1
        ) {
            throw new ConfigureParserException("配置文件格式错误");
        }

        CommandExecutorDescription des = new CommandExecutorDescription();

        Node nameNode = doc.getElementsByTagName("name").item(0).getFirstChild();
        Node asyncNode = doc.getElementsByTagName("async").item(0).getFirstChild();
        Node argsNode = doc.getElementsByTagName("args").item(0).getFirstChild();
        Node executeNode = doc.getElementsByTagName("execute").item(0).getFirstChild();
        Node privilegeNode = doc.getElementsByTagName("privilege").item(0).getFirstChild();
        Node timeoutNode = doc.getElementsByTagName("timeout").item(0).getFirstChild();

        des.setName(nameNode.getNodeValue());
        des.setAsync("true".equals(asyncNode.getNodeValue()));
        des.setWithArgs("true".equals(argsNode.getNodeValue()));
        des.setTimeout(Integer.parseInt(timeoutNode.getNodeValue()));
        des.setExecuteNode(ExecuteNode.valueOf(executeNode.getNodeValue()));
        des.setExecutePrivilege(ExecutePrivilege.valueOf(privilegeNode.getNodeValue()));

        return des;
    }
}
