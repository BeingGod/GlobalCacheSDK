package com.example.globalcachesdk.executorImpl;

import com.example.globalcachesdk.entity.AbstractEntity;
import com.example.globalcachesdk.entity.StaticNetInfo;
import com.example.globalcachesdk.exception.CommandExecException;
import com.example.globalcachesdk.executor.AbstractCommandExecutor;
import com.example.globalcachesdk.executor.Configure;
import com.jcraft.jsch.Session;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 查询静态网络信息
 * @author 蔡润培
 */
@Configure(path = "/configure/QueryStaticNetInfo.xml")
public class QueryStaticNetInfo extends AbstractCommandExecutor {

    private static final Pattern MAC_ADDR_PATTERN = Pattern.compile("(?<=ether )\\S+");

    private static final Pattern IPV4_ADDR_PATTERN = Pattern.compile("(?<=inet )\\S+");

    private static final Pattern IPV6_ADDR_PATTERN = Pattern.compile("(?<=inet6 )\\S+");

    public QueryStaticNetInfo() { super(QueryStaticNetInfo.class); }

    @Override
    public AbstractEntity exec(Session sshSession, String args) throws CommandExecException {
        String command = "ip addr";
        String[] returnValueList = execInternal(sshSession, command).split("\\n[0-9]*: |^1: ");

        StaticNetInfo staticNetInfo = new StaticNetInfo();
        ArrayList<StaticNetInfo.InterfaceInfo> interfaceInfoList = new ArrayList<>();

        for (int i = 1; i < returnValueList.length; i++) {
            StaticNetInfo.InterfaceInfo interfaceInfo = new StaticNetInfo.InterfaceInfo();
            String interfaceName = returnValueList[i].split(":")[0];
            interfaceInfo.setName(interfaceName);

            Matcher macMatcher = MAC_ADDR_PATTERN.matcher(returnValueList[i]);
            Matcher ipv4Matcher = IPV4_ADDR_PATTERN.matcher(returnValueList[i]);
            Matcher ipv6Matcher = IPV6_ADDR_PATTERN.matcher(returnValueList[i]);

            if (macMatcher.find()) {
                interfaceInfo.setMac(macMatcher.group(0));
            } else {
                interfaceInfo.setMac("");
            }
            if (ipv4Matcher.find()) {
                interfaceInfo.setIpv4(ipv4Matcher.group(0));
            } else {
                interfaceInfo.setIpv4("");
            }
            if (ipv6Matcher.find()) {
                interfaceInfo.setIpv6(ipv6Matcher.group(0));
            } else {
                interfaceInfo.setIpv6("");
            }
            interfaceInfoList.add(interfaceInfo);
        }
        staticNetInfo.setInterfaceInfoList(interfaceInfoList);

        return staticNetInfo;
    }
}
