package com.example;

import com.example.globalcachesdk.GlobalCacheSDK;
import com.example.globalcachesdk.StatusCode;
import com.example.globalcachesdk.SupportedCommand;
import com.example.globalcachesdk.entity.MemInfo;
import com.example.globalcachesdk.entity.CpuInfo;
import com.example.globalcachesdk.exception.GlobalCacheSDKException;
import com.example.globalcachesdk.executor.CommandExecuteResult;

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
				GlobalCacheSDK.releaseSession(host);
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
			System.out.println("请求CPU信息失败");
			e.printStackTrace();
		}
		System.out.println(nodesCpuInfoHashMap);

		for (String host : hosts) {
			try {
				GlobalCacheSDK.releaseSession(host);
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
			System.out.println("请求内存信息失败");
			e.printStackTrace();
		}
		System.out.println(nodesMemInfoHashMap);

		for (String host : hosts) {
			try {
				GlobalCacheSDK.releaseSession(host);
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
			GlobalCacheSDK.printCommandConf(SupportedCommand.GET_CPU_INFO);
			GlobalCacheSDK.setCommandTimeout(SupportedCommand.GET_CPU_INFO, timeout);
			System.out.println("修改后配置...");
			GlobalCacheSDK.printCommandConf(SupportedCommand.GET_CPU_INFO);
		} catch (GlobalCacheSDKException e) {
			System.out.println("命令未注册");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		sessionDemo();
		queryCpuInfoDemo();
		queryMemInfoDemo();
		setCommandTimeoutDemo();
	}
}
