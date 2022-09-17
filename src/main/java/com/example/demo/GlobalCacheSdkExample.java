package com.example.demo;

import com.example.demo.globalcachesdk.GlobalCacheSdk;
import com.example.demo.globalcachesdk.StatusCode;
import com.example.demo.globalcachesdk.entity.MemInfo;
import com.example.demo.globalcachesdk.exception.ConnectFailedException;
import com.example.demo.globalcachesdk.exception.SessionAlreadyExistException;
import com.example.demo.globalcachesdk.exception.SessionNotExistException;
import com.example.demo.globalcachesdk.excutor.CommandExecuteResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

		Map<String, MemInfo> nodesMemInfoHashMap = new HashMap<>();
		for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSdk.getNodesMemInfo(hosts).entrySet()) {
			if (entry.getValue().getStatusCode() == StatusCode.SUCCESS) {
				nodesMemInfoHashMap.put(entry.getKey(), (MemInfo) entry.getValue().getData());
			}
		}

		System.out.println(nodesMemInfoHashMap);

		for (int i = 0;i < hosts.size(); i++) {
			try {
				GlobalCacheSdk.releaseSession(hosts.get(i));
				System.out.println(hosts.get(i) + " SSH会话释放成功");
			} catch (SessionNotExistException | SessionAlreadyExistException e) {
				System.out.println(hosts.get(i) + " SSH会话释放失败");
				e.printStackTrace();
			}
		}
	}
}
