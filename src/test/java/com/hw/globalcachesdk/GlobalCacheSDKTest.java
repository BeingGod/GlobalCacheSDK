package com.hw.globalcachesdk;

import cn.hutool.core.collection.CollUtil;
import com.hw.globalcachesdk.GlobalCacheSDK;
import com.hw.globalcachesdk.StatusCode;
import com.hw.globalcachesdk.exception.GlobalCacheSDKException;
import com.hw.globalcachesdk.executor.CommandExecuteResult;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GlobalCacheSDKTest {

    private final static String scriptsPath = "/home/GlobalCacheVisualTools/scripts";

    private final static String rootPwd = "75=bYmdmMu";

    private final static String globalcacheopPwd = "globalcacheop";

    private final static String ceph1Ip = "175.34.8.36";

    private final static String[] cephIps = new String[]{
            "175.34.8.36",
            "175.34.8.37",
            "175.34.8.38",
    };

    private final static String[] clientIps = new String[]{
            "175.34.8.39",
    };

    private static ArrayList<String> getCephIpList() {
        return CollUtil.newArrayList(cephIps);
    }

    private static ArrayList<String> getClientIpList() {
        return CollUtil.newArrayList(clientIps);
    }

    private static ArrayList<String> getAllIpList() {
        ArrayList<String> allIpList = new ArrayList<>();
        allIpList.addAll(getCephIpList());
        allIpList.addAll(getClientIpList());
        return allIpList;
    }

    @BeforeClass
    public static void init() throws Exception {
        GlobalCacheSDK.setScriptsPath(scriptsPath);
        for (String ip : getCephIpList()) {
            GlobalCacheSDK.createSession(ip, "root", rootPwd, 22);
            GlobalCacheSDK.createSession(ip, "globalcacheop", globalcacheopPwd, 22);
        }

        for (String ip : getClientIpList()) {
            GlobalCacheSDK.createSession(ip, "root", rootPwd, 22);
        }
    }

    @AfterClass
    public static void release() throws Exception {
        for (String ip : getCephIpList()) {
            GlobalCacheSDK.releaseSession(ip, "root");
            GlobalCacheSDK.releaseSession(ip, "globalcacheop");
        }

        for (String ip : getClientIpList()) {
            GlobalCacheSDK.releaseSession(ip, "root");
        }
    }

    @Test
    public void queryMemInfo() {
        ArrayList<String> hosts = getCephIpList();
        try {
            for (Map.Entry<String, CommandExecuteResult> entry: GlobalCacheSDK.queryMemInfo(hosts).entrySet()) {
                Assert.assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                assertNotNull(entry.getValue().getData());
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void queryCpuInfo() {
        ArrayList<String> hosts = getCephIpList();
        try {
            for (Map.Entry<String, CommandExecuteResult> entry: GlobalCacheSDK.queryCpuInfo(hosts).entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                assertNotNull(entry.getValue().getData());
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void queryCacheDiskInfo() {
        String host = ceph1Ip;
        try {
            for (Map.Entry<String, CommandExecuteResult> entry: GlobalCacheSDK.queryCacheDiskInfo(host).entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                assertNotNull(entry.getValue().getData());
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void queryClusterAlarmInfo() {
        String host = ceph1Ip;
        try {
            for (Map.Entry<String, CommandExecuteResult> entry: GlobalCacheSDK.queryClusterAlarmInfo(host).entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                assertNotNull(entry.getValue().getData());
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void queryDiskInfo() {
        ArrayList<String> hosts = getCephIpList();
        try {
            for (Map.Entry<String, CommandExecuteResult> entry: GlobalCacheSDK.queryDiskInfo(hosts).entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                assertNotNull(entry.getValue().getData());
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void queryUptime() {
        ArrayList<String> hosts = getCephIpList();
        try {
            for (Map.Entry<String, CommandExecuteResult> entry: GlobalCacheSDK.queryUptime(hosts).entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                assertNotNull(entry.getValue().getData());
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void queryClusterStatusInfo() {
        String host = ceph1Ip;
        try {
            for (Map.Entry<String, CommandExecuteResult> entry: GlobalCacheSDK.queryClusterStatusInfo(host).entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                assertNotNull(entry.getValue().getData());
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void queryNodeStatusInfo() {
        String host = ceph1Ip;
        try {
            for (Map.Entry<String, CommandExecuteResult> entry: GlobalCacheSDK.queryNodeStatusInfo(host).entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                assertNotNull(entry.getValue().getData());
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void queryDiskIoInfo() {
        ArrayList<String> hosts = getCephIpList();
        try {
            for (Map.Entry<String, CommandExecuteResult> entry: GlobalCacheSDK.queryUptime(hosts).entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                assertNotNull(entry.getValue().getData());
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void queryStaticNetInfo() {
        ArrayList<String> hosts = getCephIpList();
        try {
            for (String host : hosts) {
                for (Map.Entry<String, CommandExecuteResult> entry: GlobalCacheSDK.queryStaticNetInfo(host).entrySet()) {
                    assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                    assertNotNull(entry.getValue().getData());
                }
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void queryDynamicNetInfo() {
        ArrayList<String> hosts = getCephIpList();
        try {
            for (Map.Entry<String, CommandExecuteResult> entry: GlobalCacheSDK.queryDynamicNetInfo(hosts).entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                assertNotNull(entry.getValue().getData());
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void queryNodePtInfo() {
        String host = ceph1Ip;
        int nodeId = 0;
        try {
            for (Map.Entry<String, CommandExecuteResult> entry: GlobalCacheSDK.queryNodePtInfo(host, nodeId).entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                assertNotNull(entry.getValue().getData());
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void queryAllPtInfo() {
        String host = ceph1Ip;
        try {
            for (Map.Entry<String, CommandExecuteResult> entry: GlobalCacheSDK.queryAllPtInfo(host).entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                assertNotNull(entry.getValue().getData());
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void queryDiskPtInfo() {
        String host = ceph1Ip;
        int diskId = 0;
        try {
            for (Map.Entry<String, CommandExecuteResult> entry: GlobalCacheSDK.queryDiskPtInfo(host, diskId).entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                assertNotNull(entry.getValue().getData());
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void queryPtIoInfo() {
        String host = ceph1Ip;
        try {
            for (Map.Entry<String, CommandExecuteResult> entry: GlobalCacheSDK.queryNodeStatusInfo(host).entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                assertNotNull(entry.getValue().getData());
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void queryNodePgInfo() {
        String host = ceph1Ip;
        try {
            for (Map.Entry<String, CommandExecuteResult> entry: GlobalCacheSDK.queryNodeStatusInfo(host).entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                assertNotNull(entry.getValue().getData());
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void queryAllPgInfo() {
        String host = ceph1Ip;
        try {
            for (Map.Entry<String, CommandExecuteResult> entry: GlobalCacheSDK.queryAllPgInfo(host).entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                assertNotNull(entry.getValue().getData());
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void queryDiskPgInfo() {
        String host = ceph1Ip;
        int diskId = 0;
        try {
            for (Map.Entry<String, CommandExecuteResult> entry: GlobalCacheSDK.queryDiskPgInfo(host, diskId).entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                assertNotNull(entry.getValue().getData());
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }

}