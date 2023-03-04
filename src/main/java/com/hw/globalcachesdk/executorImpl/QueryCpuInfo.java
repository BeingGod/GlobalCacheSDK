package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.entity.AbstractEntity;
import com.hw.globalcachesdk.entity.CpuInfo;
import com.hw.globalcachesdk.exception.ReturnValueParseException;
import com.hw.globalcachesdk.executor.AbstractCommandExecutorSync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 请求CPU信息
 * @author 章睿彬, 蔡润培
 */
@Configure(path = "/configure/QueryCpuInfo.xml")
@Script(path = "/home/GlobalCacheScriptsNew/data/cpu/cpu.sh")
public class QueryCpuInfo extends AbstractCommandExecutorSync {

    /**
     * 节点CPU信息正则表达式
     */
    private static final Pattern CPU_INFO_PATTERN = Pattern.compile("\\d+\\.\\d+");

    public QueryCpuInfo() {
        super(QueryCpuInfo.class);
    }

    @Override
    public AbstractEntity parseOf(String returnValue) throws ReturnValueParseException {
        Matcher matcher = CPU_INFO_PATTERN.matcher(returnValue);
        CpuInfo cpuInfo = new CpuInfo();

        // 整体核心利用率
        if (matcher.find()) {
            cpuInfo.setTotalUsage(100 - Double.parseDouble(matcher.group(0)));
        }

        // 每个逻辑核心的利用率
        ArrayList<Double> coreUsage = new ArrayList<>();
        while (matcher.find()) {
            coreUsage.add(100 - Double.parseDouble(matcher.group(0)));
        }
        cpuInfo.setCoreUsage(coreUsage);

        return cpuInfo;
    }
}
