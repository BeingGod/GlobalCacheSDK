package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.entity.AbstractEntity;
import com.hw.globalcachesdk.entity.ErrorCodeEntity;
import com.hw.globalcachesdk.exception.ReturnValueParseException;
import com.hw.globalcachesdk.executor.AbstractCommandExecutorSync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 初始化集群配置
 * @author 章睿彬
 */
@Configure(path = "/configure/InitClusterSettings.xml")
@Script(path = "/home/GlobalCacheScriptsNew/envs/configure/init_cluster_settings.sh",
        suffixCommand = "> /dev/null && echo $?")
public class InitClusterSettings extends AbstractCommandExecutorSync {

    private static final Pattern ERROR_CODE_PATTERN = Pattern.compile("\\d+");

    public InitClusterSettings() {
        super(InitClusterSettings.class);
    }

    @Override
    public AbstractEntity parseOf(String returnValue) throws ReturnValueParseException {
        Matcher matcher = ERROR_CODE_PATTERN.matcher(returnValue);

        ErrorCodeEntity errorCodeEntity = new ErrorCodeEntity();
        if (matcher.find()) {
            errorCodeEntity.setErrorCode(Integer.parseInt(matcher.group(0)));
        }

        return errorCodeEntity;
    }
}
