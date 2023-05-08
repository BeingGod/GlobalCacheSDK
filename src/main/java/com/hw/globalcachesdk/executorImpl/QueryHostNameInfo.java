package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.entity.AbstractEntity;
import com.hw.globalcachesdk.entity.HostNameInfo;
import com.hw.globalcachesdk.exception.ReturnValueParseException;
import com.hw.globalcachesdk.executor.AbstractCommandExecutorSync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

/**
 * 请求节点名称
 * @author 章睿彬
 */
@Configure(path = "/configure/QueryHostNameInfo.xml")
@Script(path = "/data/hostname/hostname.sh")
public class QueryHostNameInfo extends AbstractCommandExecutorSync {

    public QueryHostNameInfo() {
        super(QueryHostNameInfo.class);
    }


    @Override
    public AbstractEntity parseOf(String returnValue) throws ReturnValueParseException {
        returnValue = returnValue.replace("\n","");

        HostNameInfo hostNameInfo = new HostNameInfo();
        hostNameInfo.setHostname(returnValue);

        if (returnValue.contains("ceph1")) {
            hostNameInfo.setHostType(HostNameInfo.HostType.CEPH1);

            return hostNameInfo;
        } else if (returnValue.contains("ceph")) {
            hostNameInfo.setHostType(HostNameInfo.HostType.CEPH);

            return hostNameInfo;
        } else if (returnValue.contains("client")) {
            hostNameInfo.setHostType(HostNameInfo.HostType.CLIENT);

            return hostNameInfo;
        } else {
            hostNameInfo.setHostType(HostNameInfo.HostType.UNKNOWN);

            return hostNameInfo;
        }
    }
}
