package com.example;

import com.example.globalcachesdk.GlobalCacheSdk;
import com.example.globalcachesdk.StatusCode;
import com.example.globalcachesdk.entity.MemInfo;
import com.example.globalcachesdk.entity.CpuInfo;
import com.example.globalcachesdk.exception.ConnectFailedException;
import com.example.globalcachesdk.exception.SessionAlreadyExistException;
import com.example.globalcachesdk.exception.SessionCloseFailedException;
import com.example.globalcachesdk.exception.SessionNotExistException;
import com.example.globalcachesdk.excutor.CommandExecuteResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Global Cache SDK 调用示例
 * @author ya059
 */
public class GlobalCacheSdkExample {

	public static void main(String[] args) {
		ArrayList<String> hosts = new ArrayList<>();
		hosts.add("175.34.8.36");
		hosts.add("175.34.8.37");
		hosts.add("175.34.8.38");
		hosts.add("175.34.8.39");

		ArrayList<String> users = new ArrayList<>();
		users.add("root");
		users.add("root");
		users.add("root");
		users.add("root");

		ArrayList<String> passwords = new ArrayList<>();
		passwords.add("75=bYmdmMu");
		passwords.add("75=bYmdmMu");
		passwords.add("75=bYmdmMu");
		passwords.add("75=bYmdmMu");

		for (int i = 0;i < hosts.size(); i++) {
			try {
				GlobalCacheSdk.createSession(hosts.get(i), users.get(i), passwords.get(i), 22);
				System.out.println(hosts.get(i) + " SSH会话创建成功");
			} catch (SessionAlreadyExistException | ConnectFailedException e) {
				System.out.println(hosts.get(i) + " SSH会话创建失败");
				e.printStackTrace();
			}
		}
		Map<String, MemInfo> nodesMemInfoHashMap = new HashMap<>(hosts.size());
		for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSdk.getNodesMemInfo(hosts).entrySet()) {
			if (entry.getValue().getStatusCode() == StatusCode.SUCCESS) {
				nodesMemInfoHashMap.put(entry.getKey(), (MemInfo) entry.getValue().getData());
			}
		}

		System.out.println(nodesMemInfoHashMap);

		Map<String, CpuInfo> nodesCpuInfoHashMap = new HashMap<>(hosts.size());
		for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSdk.getNodesCpuInfo(hosts).entrySet()) {
			if (entry.getValue().getStatusCode() == StatusCode.SUCCESS) {
				nodesCpuInfoHashMap.put(entry.getKey(), (CpuInfo) entry.getValue().getData());
			}
		}

		System.out.println(nodesCpuInfoHashMap);

		for (String host : hosts) {
			try {
				GlobalCacheSdk.releaseSession(host);
				System.out.println(host + " SSH会话释放成功");
			} catch (SessionNotExistException | SessionCloseFailedException e) {
				System.out.println(host + " SSH会话释放失败");
				e.printStackTrace();
			}
		}
	}
}
