package com.hw;

import com.hw.globalcachesdk.GlobalCacheSDK;
import com.hw.globalcachesdk.StatusCode;
import com.hw.globalcachesdk.SupportedCommand;
import com.hw.globalcachesdk.entity.ErrorCodeEntity;
import com.hw.globalcachesdk.entity.MemInfo;
import com.hw.globalcachesdk.entity.CpuInfo;
import com.hw.globalcachesdk.entity.UptimeInfo;
import com.hw.globalcachesdk.exception.GlobalCacheSDKException;
import com.hw.globalcachesdk.executor.CommandExecuteResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Global Cache SDK 调用示例
 * @author 章睿彬
 */
public class GlobalCacheSDKExample {

	public static void sessionDemo() {
		System.out.println("==============Session Demo==============");

		ArrayList<String> hosts = new ArrayList<>();
		hosts.add("175.34.8.36");
		hosts.add("175.34.8.37");
		hosts.add("175.34.8.38");
		hosts.add("175.34.8.39");

		ArrayList<String> users = new ArrayList<>();
		users.add("globalcachesdk");
		users.add("globalcachesdk");
		users.add("globalcachesdk");
		users.add("globalcachesdk");

		ArrayList<String> passwords = new ArrayList<>();
		passwords.add("globalcachesdk");
		passwords.add("globalcachesdk");
		passwords.add("globalcachesdk");
		passwords.add("globalcachesdk");

		for (int i = 0;i < hosts.size(); i++) {
			try {
				GlobalCacheSDK.createSession(hosts.get(i), users.get(i), passwords.get(i), 22);
				System.out.println(hosts.get(i) + " SSH会话创建成功");
			} catch (GlobalCacheSDKException e) {
				System.out.println(hosts.get(i) + " SSH会话创建失败");
				e.printStackTrace();
			}
		}

		for (String host : hosts) {
			try {
				GlobalCacheSDK.releaseSession(host, "globalcachesdk");
				System.out.println(host + " SSH会话释放成功");
			} catch (GlobalCacheSDKException e) {
				System.out.println(host + " SSH会话释放失败");
				e.printStackTrace();
			}
		}
	}

	public static void queryCpuInfoDemo() {
		System.out.println("==============queryCpuInfo Demo==============");

		ArrayList<String> hosts = new ArrayList<>();
		hosts.add("175.34.8.36");
		hosts.add("175.34.8.37");
		hosts.add("175.34.8.38");
		hosts.add("175.34.8.39");

		ArrayList<String> users = new ArrayList<>();
		users.add("globalcachesdk");
		users.add("globalcachesdk");
		users.add("globalcachesdk");
		users.add("globalcachesdk");

		ArrayList<String> passwords = new ArrayList<>();
		passwords.add("globalcachesdk");
		passwords.add("globalcachesdk");
		passwords.add("globalcachesdk");
		passwords.add("globalcachesdk");

		for (int i = 0;i < hosts.size(); i++) {
			try {
				GlobalCacheSDK.createSession(hosts.get(i), users.get(i), passwords.get(i), 22);
				System.out.println(hosts.get(i) + " SSH会话创建成功");
			} catch (GlobalCacheSDKException e) {
				System.out.println(hosts.get(i) + " SSH会话创建失败");
				e.printStackTrace();
			}
		}

		Map<String, CpuInfo> nodesCpuInfoHashMap = new HashMap<>(hosts.size());
		try {
			for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.queryCpuInfo(hosts).entrySet()) {
				if (entry.getValue().getStatusCode() == StatusCode.SUCCESS) {
					nodesCpuInfoHashMap.put(entry.getKey(), (CpuInfo) entry.getValue().getData());
				}
			}
		} catch (GlobalCacheSDKException e) {
			System.out.println("接口调用失败");
			e.printStackTrace();
		}
		System.out.println(nodesCpuInfoHashMap);

		for (String host : hosts) {
			try {
				GlobalCacheSDK.releaseSession(host, "globalcachesdk");
				System.out.println(host + " SSH会话释放成功");
			} catch (GlobalCacheSDKException e) {
				System.out.println(host + " SSH会话释放失败");
				e.printStackTrace();
			}
		}
	}

	public static void queryMemInfoDemo() {
		System.out.println("==============queryMemInfo Demo==============");

		ArrayList<String> hosts = new ArrayList<>();
		hosts.add("175.34.8.36");
		hosts.add("175.34.8.37");
		hosts.add("175.34.8.38");
		hosts.add("175.34.8.39");

		ArrayList<String> users = new ArrayList<>();
		users.add("globalcachesdk");
		users.add("globalcachesdk");
		users.add("globalcachesdk");
		users.add("globalcachesdk");

		ArrayList<String> passwords = new ArrayList<>();
		passwords.add("globalcachesdk");
		passwords.add("globalcachesdk");
		passwords.add("globalcachesdk");
		passwords.add("globalcachesdk");

		for (int i = 0;i < hosts.size(); i++) {
			try {
				GlobalCacheSDK.createSession(hosts.get(i), users.get(i), passwords.get(i), 22);
				System.out.println(hosts.get(i) + " SSH会话创建成功");
			} catch (GlobalCacheSDKException e) {
				System.out.println(hosts.get(i) + " SSH会话创建失败");
				e.printStackTrace();
			}
		}

		Map<String, MemInfo> nodesMemInfoHashMap = new HashMap<>(hosts.size());
		try {
			for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.queryMemInfo(hosts).entrySet()) {
				if (entry.getValue().getStatusCode() == StatusCode.SUCCESS) {
					nodesMemInfoHashMap.put(entry.getKey(), (MemInfo) entry.getValue().getData());
				}
			}
		} catch (GlobalCacheSDKException e) {
			System.out.println("接口调用失败");
			e.printStackTrace();
		}
		System.out.println(nodesMemInfoHashMap);

		for (String host : hosts) {
			try {
				GlobalCacheSDK.releaseSession(host, "globalcachesdk");
				System.out.println(host + " SSH会话释放成功");
			} catch (GlobalCacheSDKException e) {
				System.out.println(host + " SSH会话释放失败");
				e.printStackTrace();
			}
		}
	}

	public static void gcServiceControlDemo() {
		System.out.println("==============gcServiceControl Demo==============");

		ArrayList<String> hosts = new ArrayList<>();
		hosts.add("175.34.8.36");
		hosts.add("175.34.8.37");
		hosts.add("175.34.8.38");

		ArrayList<String> users = new ArrayList<>();
		users.add("root");
		users.add("root");
		users.add("root");

		ArrayList<String> passwords = new ArrayList<>();
		passwords.add("75=bYmdmMu");
		passwords.add("75=bYmdmMu");
		passwords.add("75=bYmdmMu");

		for (int i = 0;i < hosts.size(); i++) {
			try {
				GlobalCacheSDK.createSession(hosts.get(i), users.get(i), passwords.get(i), 22);
				System.out.println(hosts.get(i) + " SSH会话创建成功");
			} catch (GlobalCacheSDKException e) {
				System.out.println(hosts.get(i) + " SSH会话创建失败");
				e.printStackTrace();
			}
		}

		try {
			for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.gcServiceControl(hosts, "restart").entrySet()) {
				if (entry.getValue().getStatusCode() == StatusCode.SUCCESS) {
					ErrorCodeEntity errorCodeEntity = (ErrorCodeEntity) entry.getValue().getData();
					if (0 == errorCodeEntity.getErrorCode()) {
						System.out.println("节点:" + entry + " 重启GC成功");
					} else {
						System.out.println("节点:" + entry + " 重启GC失败");
					}
				} else {
					System.out.println("接口调用失败");
				}
			}
		} catch (GlobalCacheSDKException e) {
			System.out.println("接口调用失败");
			e.printStackTrace();
		}

		for (String host : hosts) {
			try {
				GlobalCacheSDK.releaseSession(host, "root");
				System.out.println(host + " SSH会话释放成功");
			} catch (GlobalCacheSDKException e) {
				System.out.println(host + " SSH会话释放失败");
				e.printStackTrace();
			}
		}
	}

	public static void setCommandTimeoutDemo() {
		System.out.println("==============setCommandTimeout Demo==============");

		int timeout = 3;
		try {
			System.out.println("修改前配置...");
			System.out.println(GlobalCacheSDK.getCommandConf(SupportedCommand.QUERY_CPU_INFO));
			GlobalCacheSDK.setCommandTimeout(SupportedCommand.QUERY_CPU_INFO, timeout);
			System.out.println("修改后配置...");
			System.out.println(GlobalCacheSDK.getCommandConf(SupportedCommand.QUERY_CPU_INFO));
		} catch (GlobalCacheSDKException e) {
			System.out.println("命令未注册");
			e.printStackTrace();
		}
	}

	public static void queryUptimeInfoDemo() {
		System.out.println("==============queryUptimeInfo Demo==============");

		ArrayList<String> hosts = new ArrayList<>();
		hosts.add("175.34.8.36");
		hosts.add("175.34.8.37");
		hosts.add("175.34.8.38");
		hosts.add("175.34.8.39");

		ArrayList<String> users = new ArrayList<>();
		users.add("globalcachesdk");
		users.add("globalcachesdk");
		users.add("globalcachesdk");
		users.add("globalcachesdk");

		ArrayList<String> passwords = new ArrayList<>();
		passwords.add("globalcachesdk");
		passwords.add("globalcachesdk");
		passwords.add("globalcachesdk");
		passwords.add("globalcachesdk");

		for (int i = 0;i < hosts.size(); i++) {
			try {
				GlobalCacheSDK.createSession(hosts.get(i), users.get(i), passwords.get(i), 22);
				System.out.println(hosts.get(i) + " SSH会话创建成功");
			} catch (GlobalCacheSDKException e) {
				System.out.println(hosts.get(i) + " SSH会话创建失败");
				e.printStackTrace();
			}
		}

		Map<String, UptimeInfo> nodesUptimeInfoHashMap = new HashMap<>(hosts.size());
		try {
			for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.queryUptime(hosts).entrySet()) {
				if (entry.getValue().getStatusCode() == StatusCode.SUCCESS) {
					nodesUptimeInfoHashMap.put(entry.getKey(), (UptimeInfo) entry.getValue().getData());
				}
			}
		} catch (GlobalCacheSDKException e) {
			System.out.println("接口调用失败");
			e.printStackTrace();
		}
		System.out.println(nodesUptimeInfoHashMap);

		for (String host : hosts) {
			try {
				GlobalCacheSDK.releaseSession(host, "globalcachesdk");
				System.out.println(host + " SSH会话释放成功");
			} catch (GlobalCacheSDKException e) {
				System.out.println(host + " SSH会话释放失败");
				e.printStackTrace();
			}
		}
	}


	public static void main(String[] args) {
//		sessionDemo();
//		queryCpuInfoDemo();
//		queryMemInfoDemo();
		queryUptimeInfoDemo();
//		setCommandTimeoutDemo();
//		gcServiceControlDemo();
		return;
	}
}
