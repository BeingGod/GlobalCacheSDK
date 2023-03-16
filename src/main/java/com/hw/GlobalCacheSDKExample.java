package com.hw;

import com.hw.globalcachesdk.GlobalCacheSDK;
import com.hw.globalcachesdk.StatusCode;
import com.hw.globalcachesdk.entity.*;
import com.hw.globalcachesdk.exception.AsyncThreadException;
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

	public static void gcServiceControlRestartDemo() {
		System.out.println("==============gcServiceControlRestart Demo==============");

		ArrayList<String> hosts = new ArrayList<>();
		hosts.add("175.34.8.36");

		ArrayList<String> users = new ArrayList<>();
		users.add("root");

		ArrayList<String> passwords = new ArrayList<>();
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

		Map<String, AsyncEntity> entityMap = new HashMap<>(hosts.size());
		try {
			for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.gcServiceControl(hosts, "restart").entrySet()) {
				if (entry.getValue().getStatusCode() == StatusCode.SUCCESS) {
					entityMap.put(entry.getKey(), (AsyncEntity) entry.getValue().getData());
				} else {
					System.out.println("接口调用失败");
				}
			}
		} catch (GlobalCacheSDKException e) {
			System.out.println("接口调用失败");
			e.printStackTrace();
		}

		// Example1：获取实时输出
		// 以查看175.34.8.36的接口的输出为例
		AsyncEntity entity = entityMap.get("175.34.8.36");
		while (true) {
			try {
				String line = entity.readLine();
				if (line == null) {
					// 结果读取完毕
					break;
				}
				System.out.println(line);
			} catch (AsyncThreadException e) {
				System.err.println("异步线程异常");
				break;
			}
		}
		entity.waitFinish(); // 此时线程已经读取完毕，关闭缓冲区和Channel

		// Example2：一次性读取全部输出
//		entity.waitFinish(); // 此时线程已经读取完毕，关闭缓冲区和Channel
//		while (true) {
//			try {
//				String line = entity.readLine();
//				if (line == null) {
//					// 结果读取完毕
//					break;
//				}
//				System.out.println(line);
//			} catch (AsyncThreadException e) {
//				System.err.println("异步线程异常");
//				break;
//			}
//		}

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

	public static void initClusterSettingsDemo(){
		System.out.println("==============initClusterSettings Demo==============");

		ArrayList<CephConf> cephConfs= new ArrayList<>();
		ArrayList<ClientConf> clientConfs= new ArrayList<>();
		ClusterConf clusterConf=new ClusterConf();

		ArrayList<String>dataDiskList=new ArrayList<>();
		ArrayList<String>cacheDiskList=new ArrayList<>();
		dataDiskList.add("sdb");
		dataDiskList.add("sdc");
		dataDiskList.add("sdd");
		dataDiskList.add("sde");

		cacheDiskList.add("nvme01");
		cacheDiskList.add("nvme02");

		int num=0;
		CephConf cephConf1=new CephConf("ceph1",++num,true,true,true,"175.34.8.36",
				"175.34.8.36","175.34.8.36","255.255.192.0","75=bYmdmMu",dataDiskList,cacheDiskList);
		CephConf cephConf2=new CephConf("ceph2",++num,false,false,false,"175.34.8.37",
				"175.34.8.37","175.34.8.36","255.255.192.0","75=bYmdmMu",dataDiskList,cacheDiskList);
		CephConf cephConf3=new CephConf("ceph3",++num,false,false,false,"175.34.8.38",
				"175.34.8.38","175.34.8.36","255.255.192.0","75=bYmdmMu",dataDiskList,cacheDiskList);

		cephConfs.add(cephConf1);
		cephConfs.add(cephConf2);
		cephConfs.add(cephConf3);

		ClientConf clientConf1=new ClientConf("client1","255.255.192.0","175.34.8.39","75=bYmdmMu");

		clientConfs.add(clientConf1);

		clusterConf.setPtNum(180);
		clusterConf.setPgNum(180);
		clusterConf.setPublicNetwork("175.34.0.0/18");
		clusterConf.setClusterNetwork("175.34.0.0/18");

		try {
			GlobalCacheSDK.initClusterSettings(cephConfs, clientConfs, clusterConf);
		} catch (GlobalCacheSDKException e) {
			System.err.println("配置文件初始化失败");
			e.printStackTrace();
		}

		System.out.println("配置文件初始化成功");
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

	public static void queryDiskPgInfoDemo() {
		System.out.println("==============queryDiskPgInfo Demo==============");

		String host = "175.34.8.36";
		String user = "globalcacheop";
		String password = "globalcacheop";
		try {
			GlobalCacheSDK.createSession(host, user, password, 22);
			System.out.println(host + " SSH会话创建成功");
		} catch (GlobalCacheSDKException e) {
			System.out.println(host + " SSH会话创建失败");
			e.printStackTrace();
		}

		Map<String, PgInfo> infoMap = new HashMap<>();
		try {
			for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.queryDiskPgInfo(host, 0).entrySet()) {
				if (entry.getValue().getStatusCode() == StatusCode.SUCCESS) {
					infoMap.put(entry.getKey(), (PgInfo) entry.getValue().getData());
				}
			}
		} catch (GlobalCacheSDKException e) {
			System.out.println("接口调用失败");
			e.printStackTrace();
		}
		System.out.println(infoMap);

		try {
			GlobalCacheSDK.releaseSession(host, user);
			System.out.println(host + " SSH会话释放成功");
		} catch (GlobalCacheSDKException e) {
			System.out.println(host + " SSH会话释放失败");
			e.printStackTrace();
		}
	}

	public static void queryNodePgInfoDemo() {
		System.out.println("==============queryNodePgInfo Demo==============");

		String host = "175.34.8.36";
		String user = "globalcacheop";
		String password = "globalcacheop";
		try {
			GlobalCacheSDK.createSession(host, user, password, 22);
			System.out.println(host + " SSH会话创建成功");
		} catch (GlobalCacheSDKException e) {
			System.out.println(host + " SSH会话创建失败");
			e.printStackTrace();
		}

		Map<String, PgInfo> infoMap = new HashMap<>();
		try {
			for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.queryNodePgInfo(host, 0).entrySet()) {
				if (entry.getValue().getStatusCode() == StatusCode.SUCCESS) {
					infoMap.put(entry.getKey(), (PgInfo) entry.getValue().getData());
				}
			}
		} catch (GlobalCacheSDKException e) {
			System.out.println("接口调用失败");
			e.printStackTrace();
		}
		System.out.println(infoMap);

		try {
			GlobalCacheSDK.releaseSession(host, user);
			System.out.println(host + " SSH会话释放成功");
		} catch (GlobalCacheSDKException e) {
			System.out.println(host + " SSH会话释放失败");
			e.printStackTrace();
		}
	}

	public static void queryAllPgInfoDemo() {
		System.out.println("==============queryAllPgInfo Demo==============");

		String host = "175.34.8.36";
		String user = "globalcacheop";
		String password = "globalcacheop";
		try {
			GlobalCacheSDK.createSession(host, user, password, 22);
			System.out.println(host + " SSH会话创建成功");
		} catch (GlobalCacheSDKException e) {
			System.out.println(host + " SSH会话创建失败");
			e.printStackTrace();
		}

		Map<String, PgInfo> infoMap = new HashMap<>();
		try {
			for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.queryAllPgInfo(host).entrySet()) {
				if (entry.getValue().getStatusCode() == StatusCode.SUCCESS) {
					infoMap.put(entry.getKey(), (PgInfo) entry.getValue().getData());
				}
			}
		} catch (GlobalCacheSDKException e) {
			System.out.println("接口调用失败");
			e.printStackTrace();
		}
		System.out.println(infoMap);

		try {
			GlobalCacheSDK.releaseSession(host, user);
			System.out.println(host + " SSH会话释放成功");
		} catch (GlobalCacheSDKException e) {
			System.out.println(host + " SSH会话释放失败");
			e.printStackTrace();
		}
	}

	public static void queryAllPtInfoDemo() {
		System.out.println("==============queryAllPtInfo Demo==============");

		String host = "175.34.8.36";
		String user = "globalcacheop";
		String password = "globalcacheop";
		try {
			GlobalCacheSDK.createSession(host, user, password, 22);
			System.out.println(host + " SSH会话创建成功");
		} catch (GlobalCacheSDKException e) {
			System.out.println(host + " SSH会话创建失败");
			e.printStackTrace();
		}

		Map<String, PtInfo> infoMap = new HashMap<>();
		try {
			for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.queryAllPtInfo(host).entrySet()) {
				if (entry.getValue().getStatusCode() == StatusCode.SUCCESS) {
					infoMap.put(entry.getKey(), (PtInfo) entry.getValue().getData());
				}
			}
		} catch (GlobalCacheSDKException e) {
			System.out.println("接口调用失败");
			e.printStackTrace();
		}
		System.out.println(infoMap);

		try {
			GlobalCacheSDK.releaseSession(host, user);
			System.out.println(host + " SSH会话释放成功");
		} catch (GlobalCacheSDKException e) {
			System.out.println(host + " SSH会话释放失败");
			e.printStackTrace();
		}
	}

	public static void queryDynamicNetInfoDemo() {
		System.out.println("==============queryDynamicNetInfo Demo==============");

		ArrayList<String> hosts = new ArrayList<>();
		hosts.add("175.34.8.36");

		ArrayList<String> users = new ArrayList<>();
		users.add("globalcachesdk");

		ArrayList<String> passwords = new ArrayList<>();
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

		Map<String, DynamicNetInfo> infoMap = new HashMap<>(hosts.size());
		try {
			for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.queryDynamicNetInfo(hosts).entrySet()) {
				if (entry.getValue().getStatusCode() == StatusCode.SUCCESS) {
					infoMap.put(entry.getKey(), (DynamicNetInfo) entry.getValue().getData());
				}
			}
		} catch (GlobalCacheSDKException e) {
			System.out.println("接口调用失败");
			e.printStackTrace();
		}
		System.out.println(infoMap);

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

	private static void compilePkgsDemo() {
		System.out.println("==============compile packages Demo==============");
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

		// 配置编译节点
		{
			Map<String, AsyncEntity> entityMap = new HashMap<>(hosts.size());
			try {
				for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.compileNodeConfEnv().entrySet()) {
					if (entry.getValue().getStatusCode() == StatusCode.SUCCESS) {
						entityMap.put(entry.getKey(), (AsyncEntity) entry.getValue().getData());
					} else {
						System.out.println("接口调用失败");
					}
				}
			} catch (GlobalCacheSDKException e) {
				System.out.println("接口调用失败");
				e.printStackTrace();
			}

			// 未完成节点计数
			int countDown = entityMap.size();
			while (countDown > 0) {
				for (Map.Entry<String, AsyncEntity> entry : entityMap.entrySet()) {
					AsyncEntity entity = entry.getValue();
					try {
						String line = entity.readLine();
						if (line == null) {
							// 结果读取完毕
							entityMap.remove(entry.getKey());
							entity.waitFinish(); // 此时线程已经读取完毕，关闭缓冲区和Channel
							countDown-=1;
							continue;
						}
						// 将数据发送给前端
						System.out.println(entry.getKey() + ": " + line);
					} catch (AsyncThreadException e) {
						System.err.println("异步线程异常");
					}
				}
			}
		}

		// 编译软件
		{
			Map<String, AsyncEntity> entityMap = new HashMap<>(hosts.size());
			try {
				for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.compileNodeBuildPkgs().entrySet()) {
					if (entry.getValue().getStatusCode() == StatusCode.SUCCESS) {
						entityMap.put(entry.getKey(), (AsyncEntity) entry.getValue().getData());
					} else {
						System.out.println("接口调用失败");
					}
				}
			} catch (GlobalCacheSDKException e) {
				System.out.println("接口调用失败");
				e.printStackTrace();
			}

			// 未完成节点计数
			int countDown = entityMap.size();
			while (countDown > 0) {
				for (Map.Entry<String, AsyncEntity> entry : entityMap.entrySet()) {
					AsyncEntity entity = entry.getValue();
					try {
						String line = entity.readLine();
						if (line == null) {
							// 结果读取完毕
							entityMap.remove(entry.getKey());
							entity.waitFinish(); // 此时线程已经读取完毕，关闭缓冲区和Channel
							countDown-=1;
							continue;
						}
						// 将数据发送给前端
						System.out.println(entry.getKey() + ": " + line);
					} catch (AsyncThreadException e) {
						System.err.println("异步线程异常");
					}
				}
			}
		}

		// 分发软件包
		{
			Map<String, AsyncEntity> entityMap = new HashMap<>(hosts.size());
			try {
				for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.compileNodeDistributePkgs().entrySet()) {
					if (entry.getValue().getStatusCode() == StatusCode.SUCCESS) {
						entityMap.put(entry.getKey(), (AsyncEntity) entry.getValue().getData());
					} else {
						System.out.println("接口调用失败");
					}
				}
			} catch (GlobalCacheSDKException e) {
				System.out.println("接口调用失败");
				e.printStackTrace();
			}

			// 未完成节点计数
			int countDown = entityMap.size();
			while (countDown > 0) {
				for (Map.Entry<String, AsyncEntity> entry : entityMap.entrySet()) {
					AsyncEntity entity = entry.getValue();
					try {
						String line = entity.readLine();
						if (line == null) {
							// 结果读取完毕
							entityMap.remove(entry.getKey());
							entity.waitFinish(); // 此时线程已经读取完毕，关闭缓冲区和Channel
							countDown-=1;
							continue;
						}
						// 将数据发送给前端
						System.out.println(entry.getKey() + ": " + line);
					} catch (AsyncThreadException e) {
						System.err.println("异步线程异常");
					}
				}
			}
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

	private static void deployCephDemo() {
		System.out.println("==============deploy Ceph Demo==============");

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
				GlobalCacheSDK.createSession(hosts.get(i), users.get(i), passwords.get(i), 22);
				System.out.println(hosts.get(i) + " SSH会话创建成功");
			} catch (GlobalCacheSDKException e) {
				System.out.println(hosts.get(i) + " SSH会话创建失败");
				e.printStackTrace();
			}
		}

		// 配置Ceph基础环境
		{
			Map<String, AsyncEntity> entityMap = new HashMap<>(hosts.size());
			try {
				for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.allNodeCephConfEnv().entrySet()) {
					if (entry.getValue().getStatusCode() == StatusCode.SUCCESS) {
						entityMap.put(entry.getKey(), (AsyncEntity) entry.getValue().getData());
					} else {
						System.out.println("接口调用失败");
					}
				}
			} catch (GlobalCacheSDKException e) {
				System.out.println("接口调用失败");
				e.printStackTrace();
			}

			// 未完成节点计数
			int countDown = entityMap.size();
			while (countDown > 0) {
				for (Map.Entry<String, AsyncEntity> entry : entityMap.entrySet()) {
					AsyncEntity entity = entry.getValue();
					try {
						String line = entity.readLine();
						if (line == null) {
							// 结果读取完毕
							entityMap.remove(entry.getKey());
							entity.waitFinish(); // 此时线程已经读取完毕，关闭缓冲区和Channel
							countDown-=1;
							continue;
						}
						// 将数据发送给前端
						System.out.println(entry.getKey() + ": " + line);
					} catch (AsyncThreadException e) {
						System.err.println("异步线程异常");
					}
				}
			}
		}

		// 配置ntp服务端
		{
			Map<String, AsyncEntity> entityMap = new HashMap<>(hosts.size());
			try {
				for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.ntpServerNodeConfEnv().entrySet()) {
					if (entry.getValue().getStatusCode() == StatusCode.SUCCESS) {
						entityMap.put(entry.getKey(), (AsyncEntity) entry.getValue().getData());
					} else {
						System.out.println("接口调用失败");
					}
				}
			} catch (GlobalCacheSDKException e) {
				System.out.println("接口调用失败");
				e.printStackTrace();
			}

			// 未完成节点计数
			int countDown = entityMap.size();
			while (countDown > 0) {
				for (Map.Entry<String, AsyncEntity> entry : entityMap.entrySet()) {
					AsyncEntity entity = entry.getValue();
					try {
						String line = entity.readLine();
						if (line == null) {
							// 结果读取完毕
							entityMap.remove(entry.getKey());
							entity.waitFinish(); // 此时线程已经读取完毕，关闭缓冲区和Channel
							countDown-=1;
							continue;
						}
						// 将数据发送给前端
						System.out.println(entry.getKey() + ": " + line);
					} catch (AsyncThreadException e) {
						System.err.println("异步线程异常");
					}
				}
			}
		}

		// 配置ntp客户端
		{
			Map<String, AsyncEntity> entityMap = new HashMap<>(hosts.size());
			try {
				for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.ntpClientNodeConfEnv().entrySet()) {
					if (entry.getValue().getStatusCode() == StatusCode.SUCCESS) {
						entityMap.put(entry.getKey(), (AsyncEntity) entry.getValue().getData());
					} else {
						System.out.println("接口调用失败");
					}
				}
			} catch (GlobalCacheSDKException e) {
				System.out.println("接口调用失败");
				e.printStackTrace();
			}

			// 未完成节点计数
			int countDown = entityMap.size();
			while (countDown > 0) {
				for (Map.Entry<String, AsyncEntity> entry : entityMap.entrySet()) {
					AsyncEntity entity = entry.getValue();
					try {
						String line = entity.readLine();
						if (line == null) {
							// 结果读取完毕
							entityMap.remove(entry.getKey());
							entity.waitFinish(); // 此时线程已经读取完毕，关闭缓冲区和Channel
							countDown-=1;
							continue;
						}
						// 将数据发送给前端
						System.out.println(entry.getKey() + ": " + line);
					} catch (AsyncThreadException e) {
						System.err.println("异步线程异常");
					}
				}
			}
		}

		// 安装Ceph
		{
			Map<String, AsyncEntity> entityMap = new HashMap<>(hosts.size());
			try {
				for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.cephNodeInstallPkgs().entrySet()) {
					if (entry.getValue().getStatusCode() == StatusCode.SUCCESS) {
						entityMap.put(entry.getKey(), (AsyncEntity) entry.getValue().getData());
					} else {
						System.out.println("接口调用失败");
					}
				}
			} catch (GlobalCacheSDKException e) {
				System.out.println("接口调用失败");
				e.printStackTrace();
			}

			// 未完成节点计数
			int countDown = entityMap.size();
			while (countDown > 0) {
				for (Map.Entry<String, AsyncEntity> entry : entityMap.entrySet()) {
					AsyncEntity entity = entry.getValue();
					try {
						String line = entity.readLine();
						if (line == null) {
							// 结果读取完毕
							entityMap.remove(entry.getKey());
							entity.waitFinish(); // 此时线程已经读取完毕，关闭缓冲区和Channel
							countDown-=1;
							continue;
						}
						// 将数据发送给前端
						System.out.println(entry.getKey() + ": " + line);
					} catch (AsyncThreadException e) {
						System.err.println("异步线程异常");
					}
				}
			}
		}

		// 部署Ceph
		{
			Map<String, AsyncEntity> entityMap = new HashMap<>(hosts.size());
			try {
				for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.ceph1NodeDeployCeph().entrySet()) {
					if (entry.getValue().getStatusCode() == StatusCode.SUCCESS) {
						entityMap.put(entry.getKey(), (AsyncEntity) entry.getValue().getData());
					} else {
						System.out.println("接口调用失败");
					}
				}
			} catch (GlobalCacheSDKException e) {
				System.out.println("接口调用失败");
				e.printStackTrace();
			}

			// 未完成节点计数
			int countDown = entityMap.size();
			while (countDown > 0) {
				for (Map.Entry<String, AsyncEntity> entry : entityMap.entrySet()) {
					AsyncEntity entity = entry.getValue();
					try {
						String line = entity.readLine();
						if (line == null) {
							// 结果读取完毕
							entityMap.remove(entry.getKey());
							entity.waitFinish(); // 此时线程已经读取完毕，关闭缓冲区和Channel
							countDown-=1;
							continue;
						}
						// 将数据发送给前端
						System.out.println(entry.getKey() + ": " + line);
					} catch (AsyncThreadException e) {
						System.err.println("异步线程异常");
					}
				}
			}
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

	private static void deployGlobalCacheDemo() {
		System.out.println("==============deploy Global Cache Demo==============");

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
				GlobalCacheSDK.createSession(hosts.get(i), users.get(i), passwords.get(i), 22);
				System.out.println(hosts.get(i) + " SSH会话创建成功");
			} catch (GlobalCacheSDKException e) {
				System.out.println(hosts.get(i) + " SSH会话创建失败");
				e.printStackTrace();
			}
		}

		// 配置GlobalCache服务端环境
		{
			Map<String, AsyncEntity> entityMap = new HashMap<>(hosts.size());
			try {
				for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.serverNodeConfEnv().entrySet()) {
					if (entry.getValue().getStatusCode() == StatusCode.SUCCESS) {
						entityMap.put(entry.getKey(), (AsyncEntity) entry.getValue().getData());
					} else {
						System.out.println("接口调用失败");
					}
				}
			} catch (GlobalCacheSDKException e) {
				System.out.println("接口调用失败");
				e.printStackTrace();
			}

			// 未完成节点计数
			int countDown = entityMap.size();
			while (countDown > 0) {
				for (Map.Entry<String, AsyncEntity> entry : entityMap.entrySet()) {
					AsyncEntity entity = entry.getValue();
					try {
						String line = entity.readLine();
						if (line == null) {
							// 结果读取完毕
							entityMap.remove(entry.getKey());
							entity.waitFinish(); // 此时线程已经读取完毕，关闭缓冲区和Channel
							countDown-=1;
							continue;
						}
						// 将数据发送给前端
						System.out.println(entry.getKey() + ": " + line);
					} catch (AsyncThreadException e) {
						System.err.println("异步线程异常");
					}
				}
			}
		}

		// 配置GlobalCache客户端环境
		{
			Map<String, AsyncEntity> entityMap = new HashMap<>(hosts.size());
			try {
				for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.clientNodeConfEnv().entrySet()) {
					if (entry.getValue().getStatusCode() == StatusCode.SUCCESS) {
						entityMap.put(entry.getKey(), (AsyncEntity) entry.getValue().getData());
					} else {
						System.out.println("接口调用失败");
					}
				}
			} catch (GlobalCacheSDKException e) {
				System.out.println("接口调用失败");
				e.printStackTrace();
			}

			// 未完成节点计数
			int countDown = entityMap.size();
			while (countDown > 0) {
				for (Map.Entry<String, AsyncEntity> entry : entityMap.entrySet()) {
					AsyncEntity entity = entry.getValue();
					try {
						String line = entity.readLine();
						if (line == null) {
							// 结果读取完毕
							entityMap.remove(entry.getKey());
							entity.waitFinish(); // 此时线程已经读取完毕，关闭缓冲区和Channel
							countDown-=1;
							continue;
						}
						// 将数据发送给前端
						System.out.println(entry.getKey() + ": " + line);
					} catch (AsyncThreadException e) {
						System.err.println("异步线程异常");
					}
				}
			}
		}

		// 安装GlobalCache服务端
		{
			Map<String, AsyncEntity> entityMap = new HashMap<>(hosts.size());
			try {
				for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.serverNodeInstallPkgs().entrySet()) {
					if (entry.getValue().getStatusCode() == StatusCode.SUCCESS) {
						entityMap.put(entry.getKey(), (AsyncEntity) entry.getValue().getData());
					} else {
						System.out.println("接口调用失败");
					}
				}
			} catch (GlobalCacheSDKException e) {
				System.out.println("接口调用失败");
				e.printStackTrace();
			}

			// 未完成节点计数
			int countDown = entityMap.size();
			while (countDown > 0) {
				for (Map.Entry<String, AsyncEntity> entry : entityMap.entrySet()) {
					AsyncEntity entity = entry.getValue();
					try {
						String line = entity.readLine();
						if (line == null) {
							// 结果读取完毕
							entityMap.remove(entry.getKey());
							entity.waitFinish(); // 此时线程已经读取完毕，关闭缓冲区和Channel
							countDown-=1;
							continue;
						}
						// 将数据发送给前端
						System.out.println(entry.getKey() + ": " + line);
					} catch (AsyncThreadException e) {
						System.err.println("异步线程异常");
					}
				}
			}
		}

		// 安装GlobalCache客户端
		{
			Map<String, AsyncEntity> entityMap = new HashMap<>(hosts.size());
			try {
				for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.clientNodeInstallPkgs().entrySet()) {
					if (entry.getValue().getStatusCode() == StatusCode.SUCCESS) {
						entityMap.put(entry.getKey(), (AsyncEntity) entry.getValue().getData());
					} else {
						System.out.println("接口调用失败");
					}
				}
			} catch (GlobalCacheSDKException e) {
				System.out.println("接口调用失败");
				e.printStackTrace();
			}

			// 未完成节点计数
			int countDown = entityMap.size();
			while (countDown > 0) {
				for (Map.Entry<String, AsyncEntity> entry : entityMap.entrySet()) {
					AsyncEntity entity = entry.getValue();
					try {
						String line = entity.readLine();
						if (line == null) {
							// 结果读取完毕
							entityMap.remove(entry.getKey());
							entity.waitFinish(); // 此时线程已经读取完毕，关闭缓冲区和Channel
							countDown-=1;
							continue;
						}
						// 将数据发送给前端
						System.out.println(entry.getKey() + ": " + line);
					} catch (AsyncThreadException e) {
						System.err.println("异步线程异常");
					}
				}
			}
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

	public static void gcServiceControlStartDemo() {
		System.out.println("==============gcServiceControlStart Demo==============");

		ArrayList<String> hosts = new ArrayList<>();
		hosts.add("175.34.8.36");
		hosts.add("175.34.8.36");
		hosts.add("175.34.8.36");

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

		Map<String, AsyncEntity> entityMap = new HashMap<>(hosts.size());
		try {
			for (Map.Entry<String, CommandExecuteResult> entry : GlobalCacheSDK.gcServiceControl(hosts, "start").entrySet()) {
				if (entry.getValue().getStatusCode() == StatusCode.SUCCESS) {
					entityMap.put(entry.getKey(), (AsyncEntity) entry.getValue().getData());
				} else {
					System.out.println("接口调用失败");
				}
			}
		} catch (GlobalCacheSDKException e) {
			System.out.println("接口调用失败");
			e.printStackTrace();
		}

		int countDown = entityMap.size();
		while (countDown > 0) {
			for (Map.Entry<String, AsyncEntity> entry : entityMap.entrySet()) {
				AsyncEntity entity = entry.getValue();
				try {
					String line = entity.readLine();
					if (line == null) {
						// 结果读取完毕
						entityMap.remove(entry.getKey());
						entity.waitFinish(); // 此时线程已经读取完毕，关闭缓冲区和Channel
						countDown-=1;
						continue;
					}
					// 将数据发送给前端
					System.out.println(entry.getKey() + ": " + line);
				} catch (AsyncThreadException e) {
					System.err.println("异步线程异常");
				}
			}
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

	public static void autoDeployDemo() {
		// 集群配置初始化
		initClusterSettingsDemo();

		// 编译依赖包
		compilePkgsDemo();

		// 部署Ceph
		deployCephDemo();

		// 部署GlobalCache
		deployGlobalCacheDemo();

		// 初始化GlobalCache
		gcServiceControlStartDemo();
	}

	public static void main(String[] args) {
//		queryCpuInfoDemo();
//		queryMemInfoDemo();
//		queryUptimeInfoDemo();
//		queryNodePgInfoDemo();
//		queryDiskPgInfoDemo();
//		queryAllPgInfoDemo();
//		queryAllPtInfoDemo();
//		gcServiceControlDemo();
//		queryDynamicNetInfoDemo();
		autoDeployDemo();
	}
}
