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

@Configure(path= "/configure/QueryStaticNetInfo.xml")
public class QueryStaticNetInfo extends AbstractCommandExecutor {
    private static final Pattern MAC_ADDR_PATTARN = Pattern.compile("(?<=ether )\\S+");
    private static final Pattern IPV4_ADDR_PATTARN = Pattern.compile("(?<=inet )\\S+");
    private static final Pattern IPV6_ADDR_PATTARN = Pattern.compile("(?<=inet6 )\\S+");

    public QueryStaticNetInfo() {
        super(QueryStaticNetInfo.class);
    }

    @Override
    public AbstractEntity exec(Session sshSession, String args) throws CommandExecException {
        String command = "ip addr";
        String[] returnValue = execInternal(sshSession, command).split("\\n[0-9]*: |^1: ");

        StaticNetInfo staticNetInfo = new StaticNetInfo();
        ArrayList<StaticNetInfo.InterfaceInfo> interfaceInfoList = new ArrayList<>();

        for (int i = 1; i < returnValue.length; i++) {
            StaticNetInfo.InterfaceInfo interfaceInfo = new StaticNetInfo.InterfaceInfo();
            interfaceInfo.setName(returnValue[i].split(":")[0]);
            System.out.println(returnValue[i]);
            Matcher macMatcher = MAC_ADDR_PATTARN.matcher(returnValue[i]);
            Matcher ipv4Matcher = IPV4_ADDR_PATTARN.matcher(returnValue[i]);
            Matcher ipv6Matcher = IPV6_ADDR_PATTARN.matcher(returnValue[i]);
            try{
                interfaceInfo.setMac(macMatcher.group(0));
            } catch (IllegalStateException e) {
                interfaceInfo.setMac(null);
                System.out.println("not find mac");
            }

            try{
                interfaceInfo.setIpv4(ipv4Matcher.group(0));
            } catch (IllegalStateException e) {
                interfaceInfo.setIpv4(null);
                System.out.println("not find ipv4");

            }

            try {
                interfaceInfo.setIpv6(ipv6Matcher.group(0));
            } catch (IllegalStateException e) {
                interfaceInfo.setIpv6(null);
                System.out.println("not find ipv6");

            }

            interfaceInfoList.add(interfaceInfo);
        }
        staticNetInfo.setInterfaceInfoList(interfaceInfoList);
        return staticNetInfo;
    }
}
