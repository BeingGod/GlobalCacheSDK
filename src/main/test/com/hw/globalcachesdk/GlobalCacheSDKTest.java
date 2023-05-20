package com.hw.globalcachesdk;

import cn.hutool.core.map.MapUtil;
import com.hw.globalcachesdk.entity.ErrorCodeEntity;
import com.hw.globalcachesdk.exception.GlobalCacheSDKException;
import com.hw.globalcachesdk.executor.CommandExecuteResult;
import org.junit.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
public class GlobalCacheSDKTest {

    public static String[][] nodeInfo =new String[][] {
            {"ceph1", "175.34.8.36"},
            {"ceph2", "175.34.8.37"},
            {"ceph3", "175.34.8.38"},
            {"client1", "175.34.8.39"},
    };

    public static String globalcacheopPwd = "globalcacheop";

    public static String rootPwd =  "75=bYmdmMu";

    public static final String SCRIPTS_ROOT = "/home/zrb/GlobalCacheScripts";

    private static HashMap<String, String> hostname2ipTable = null;

    public static ArrayList<String> getCephHosts() {
        ArrayList<String> hosts = new ArrayList<>();
        for (Map.Entry<String, String> entry : hostname2ipTable.entrySet()) {
            String hostname = entry.getKey();
            if (hostname.contains("ceph")) {
                hosts.add(entry.getValue());
            }
        }

        return hosts;
    }

    public static ArrayList<String> getClientHosts() {
        ArrayList<String> hosts = new ArrayList<>();
        for (Map.Entry<String, String> entry : hostname2ipTable.entrySet()) {
            String hostname = entry.getKey();
            if (hostname.contains("client")) {
                hosts.add(entry.getValue());
            }
        }

        return hosts;
    }

    public static String getCeph1Host() {
        return hostname2ipTable.get("ceph1");
    }

    @BeforeClass
    public static void setUp() throws Exception {
        HashMap<Object, Object> temp = MapUtil.of(nodeInfo);

        hostname2ipTable = new HashMap<>();

        for (Map.Entry<Object,Object> entry : temp.entrySet()) {
            hostname2ipTable.put((String) entry.getKey(),(String) entry.getValue());

            GlobalCacheSDK.createSession((String) entry.getValue(), "root", rootPwd, 22);
            String hostname = (String) entry.getKey();
            if (hostname.contains("ceph")) {
                GlobalCacheSDK.createSession((String) entry.getValue(), "globalcacheop", globalcacheopPwd, 22);
            }
        }
        GlobalCacheSDK.setScriptsPath(SCRIPTS_ROOT);

        GlobalCacheSDK.setHostname2ipTable(hostname2ipTable);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        for (Map.Entry<String,String> entry : hostname2ipTable.entrySet()) {
            GlobalCacheSDK.releaseSession(entry.getValue(), "root");
            String hostname = entry.getKey();
            if (hostname.contains("ceph")) {
                GlobalCacheSDK.releaseSession(entry.getValue(), "globalcacheop");
            }
        }
    }

    @Test
    public void queryMemInfo() {
        ArrayList<String> hosts = getCephHosts();
        try {
            for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.queryMemInfo(hosts).entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void queryCpuInfo() {
        ArrayList<String> hosts = getCephHosts();
        try {
            for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.queryCpuInfo(hosts).entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void queryCacheDiskInfo() {
        try {
            for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.queryCacheDiskInfo(getCeph1Host()).entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void queryClusterAlarmInfo() {
        try {
            for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.queryClusterAlarmInfo(getCeph1Host()).entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void queryDiskInfo() {
        ArrayList<String> hosts = getCephHosts();
        try {
            for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.queryDiskInfo(hosts).entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void queryUptime() {
        ArrayList<String> hosts = getCephHosts();
        try {
            for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.queryUptime(hosts).entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void queryClusterStatusInfo() {
        try {
            for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.queryClusterStatusInfo(getCeph1Host()).entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void queryNodeStatusInfo() {
        try {
            for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.queryNodeStatusInfo(getCeph1Host()).entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void queryDiskIoInfo() {
        ArrayList<String> hosts = getCephHosts();
        try {
            for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.queryDiskIoInfo(hosts).entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void queryStaticNetInfo() {
        try {
            for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.queryStaticNetInfo(getCeph1Host()).entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void queryDynamicNetInfo() {
        ArrayList<String> hosts = getCephHosts();
        try {
            for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.queryDynamicNetInfo(hosts).entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void queryNodePtInfo() {
        try {
            for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.queryNodePtInfo(getCeph1Host(), 0).entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void queryAllPtInfo() {
        try {
            for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.queryAllPtInfo(getCeph1Host()).entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void queryDiskPtInfo() {
        try {
            for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.queryDiskPtInfo(getCeph1Host(), 0).entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void queryPtIoInfo() {
        try {
            for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.queryPtIoInfo(getCeph1Host()).entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void queryNodePgInfo() {
        try {
            for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.queryNodePgInfo(getCeph1Host(), 0).entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void queryAllPgInfo() {
        try {
            for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.queryAllPgInfo(getCeph1Host()).entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void queryDiskPgInfo() {
        try {
            for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.queryDiskPgInfo(getCeph1Host(),0).entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void queryHostNameInfo() {
        try {
            for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.queryHostNameInfo(getCeph1Host()).entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void checkCeph() {
        try {
            for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.checkCeph().entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void checkClient() {
        try {
            for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.checkClient().entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void checkCompile() {
        try {
            for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.checkCompile().entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void checkConf() {
        try {
            for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.checkConf().entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void checkDistribute() {
        try {
            for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.checkDistribute().entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void checkServer() {
        try {
            for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.checkServer().entrySet()) {
                ErrorCodeEntity entity = (ErrorCodeEntity) entry.getValue().getData();
                System.out.println(entity.getMessage());
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void checkHardware() {
        try {
            for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.checkHardware().entrySet()) {
                assertEquals(StatusCode.SUCCESS, entry.getValue().getStatusCode());
                
            }
        } catch (GlobalCacheSDKException e) {
            throw new RuntimeException(e);
        }
    }
}