# Global Cache SDK

## 概述

​	Global Cache集群可视化软件数据接口（Global Cache SDK），其使用Shell脚本对集群查询数据命令进行封装，使用SSH协议对Web后端服务和集群数据传输进行加密，使用Java对查询数据脚本和自动化部署脚本调用进行封装，以Jar包形式为Web后端服务提供数据获取接口。

## 快速上手

* Global Cache SDK 使用者请参考<a href=#usage>使用</a>部分内容；
* Global Cache SDK 开发者请参考<a href=#develop>开发</a>部分内容。

## 特性

* **自动并发**：对于需要在多个节点上执行的命令，使用内部线程池将其转变为多个节点并发执行。
* **易配置**：用户可以使用XML文件对接口进行配置，同时也可以调用辅助配置接口在运行时动态修改接口配置。
* **易拓展**：接口的集成进行了依赖倒转，接口开发者只需要完成输入参数的处理的返回数据的解析，无需关注接口实例化和接口并发细节。
* **异步请求（todo）**：对于耗时较长的请求，通过异步防止阻塞当前线程，并支持从异步对象获取实时输出。

![image-20230124191003762](./assets/image-20230124191003762.png)

## <span id=usage>使用</span>

使用者调用Global Cache SDK的接口调用的主要流程如下：

![image-20230216162900061](./assets/image-20230216162900061.png)

### 阻塞接口调用

​	由于数据请求接口调用时间较短，因此将其设计为阻塞接口，即调用完函数即可获得输出。接下来以调用CPU信息请求接口为例，演示如何调用阻塞接口：

#### Step1：查看接口配置文件

​	查看`resource/configure`目录下对应接口的XML文件，并将其拷贝到项目中的`resource/configure`目录下（否则会出现**未知配置文件**的错误）。

接口配置文件格式说明如下：

```xml
<?xml version="1.0" encoding="utf-8" ?>
<description>
    <name>QueryCpuInfo</name>        // 命令名称（Java类名）
    <async>false</async>             // 是否为异步命令
    <args>false</args>               // 是否为带参命令
    <execute>ALL_NODES</execute>     // 命令执行节点
    <privilege>USER</privilege>      // 命令执行权限
    <timeout>2</timeout>             // 命令请求等待时间，单位：秒
    <comment>获取节点CPU信息</comment> // 备注
</description>
```

​	调用者只需要关注`execute`，`privilege`和`timeout`这三个值。

* 调用`createSession`传入的`host`需要参照`execute`；
* 传入的`user`需要参照`privilege`；
* 其中`timeout`是接口内部执行的SSH命令的**硬超时时间**（为了防止由于网络问题导致的长时间阻塞），超过该时间，对应节点的请求数据一定为`null`。

**tips**：有些时候由于网络连接不稳定，可能会出现接口调用超时，调用者可以调用`setCommandTimeout`**动态的调整接口的超时时间**。

#### Step2: 创建Session

​	根据接口配置文件，调用`createSession`创建Session，由于`execute`的值为`ALL_NODES`，所以需要添加集群所有的节点；由于`privilege`的值为`USER`，所以需要添加`globalcachesdk`用户。

​	可以通过调用`enumExecutePrivilegeName`函数或者权限下对应的用户名。

**tips**：由于创建Session时间较长，因此不建议重复调用，可以一次性创建全部需要的Session，多个调用接口进行Session的复用。

```java
public static void queryCpuInfoDemo() {
  ...

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
  ...
}
```

#### Step3：调用接口

调用对应的接口，所有接口的返回结果为`Map<String, CommandExecuteResult>`，其中`String`为命令执行节点，`CommandExecuteResult`为节点执行结果。当`CommandExecuteResult.getStatusCode`为`StatusCode.SUCCESS`时表示接口调用成功；接口调用失败时，`CommandExecuteResult.getData`的结果为**未定义**。

```java
public static void queryCpuInfoDemo() {
  ...
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
  ...
}
```

#### Step4: 释放Session

当不需要进行接口请求时，可以调用`releaseSession`时释放Session。

```java
public static void queryCpuInfoDemo() {
  ...
  for (String host : hosts) {
  try {
 			GlobalCacheSDK.releaseSession(host, "globalcachesdk");
      System.out.println(host + " SSH会话释放成功");
    } catch (GlobalCacheSDKException e) {
      System.out.println(host + " SSH会话释放失败");
      e.printStackTrace();
    }
  }
  ...
}
```

### 异步接口调用

​	由于自动化部署调用时间较长，因此将其设计为异步接口，其返回一个异步对象，可以从异步对象中获得实时输出。

待补充...

## <span id=develop>开发</span>

Global Cache SDK项目目录结构如下：

```bash
src
└── main
    ├── java
    │   └── com
    │       └── example
    │           └── globalcachesdk
    │               ├── entity       # 命令返回结果实体
    │               ├── exception
    │               ├── executor
    │               ├── executorImpl # 命令执行与解析具体实现
    │               ├── pool
    │               └── utils
    └── resources
        └── configure # 接口配置文件
```

接口配置文件格式说明如下：

```xml
<?xml version="1.0" encoding="utf-8" ?>
<description>
    <name>QueryCpuInfo</name>        // 命令名称（Java类名）
    <async>false</async>             // 是否为异步命令
    <args>false</args>               // 是否为带参命令
    <execute>ALL_NODES</execute>     // 命令执行节点
    <privilege>USER</privilege>      // 命令执行权限
    <timeout>2</timeout>             // 命令请求等待时间，单位：秒
    <comment>获取节点CPU信息</comment> // 备注
</description>
```

### 阻塞接口开发

​	由于数据请求接口调用时间较短，因此将其设计为阻塞接口，即调用完函数即可获得输出。接下来以调用CPU信息请求接口为例，演示如何开发阻塞接口：

#### Step1：抽象结果实体

​	根据需求，对Shell脚本返回的命令进行抽象，在`entity`目录下创建一个`CpuInfo`的类，**该类继承自`AbstractEntity`**，并实现对应的`getter/setter`方法。

```java
public class CpuInfo extends AbstractEntity {

    private double totalUsage;

    private ArrayList<Double> coreUsage;

    public double getTotalUsage() {
        return totalUsage;
    }

    public void setTotalUsage(double totalUsage) {
        this.totalUsage = totalUsage;
    }

    public ArrayList<Double> getCoreUsage() {
        return coreUsage;
    }

    public void setCoreUsage(ArrayList<Double> coreUsage) {
        this.coreUsage = coreUsage;
    }
}

```

#### Step2：实现命令执行类代码

​	在`executorImpl`目录下创建一个`QueryCpuInfo`类，**该类继承自`AbstractCommandExecutor`**，在子类中，需要**实现子类的构造函数**，**重写父类的`exec`方法**。

​	子类构造函数**必须通过`super`调用父类的构造函数**，参数必须为`子类名.class`，以便父类能够通过子类名称，根据XML配置文件反转生成命令的配置。

​	在`exec`中需要完成以下5个工作：

* 对于带参命令，在`exec`中对需要执行的命令和参数进行拼接；
* 调用`execInternal`，以发起SSH请求；

* 实例化对应的返回结果实体；
* 对SSH请求的返回结果字符串进行解析，并调用`setter`方法给实体赋值。

```java
...
public class QueryCpuInfo extends AbstractCommandExecutor {

    private static final Pattern CPU_INFO_PATTERN = Pattern.compile("\\d+\\.\\d+");

    public QueryCpuInfo() {
        // 通过super调用父类的构造函数，参数为"子类名.class"
        super(QueryCpuInfo.class);
    }

    @Override
    public AbstractEntity exec(Session sshSession, String args) throws CommandExecException {
        // step1 对于带参命令，对需要执行的命令和参数进行拼接
        String command = "bash /home/GlobalCacheScripts/SDK/cpu_usage.sh";
			
      	// step2 调用"execInternal"
        String returnValue = execInternal(sshSession, command);
				
        // step3 实例化对应的返回结果实体
	      CpuInfo cpuInfo = new CpuInfo();
  			
        // step4 对SSH请求的返回结果字符串进行解析，并调用"setter"方法给实体赋值
        Matcher matcher = CPU_INFO_PATTERN.matcher(returnValue);
        if (matcher.find()) {
            cpuInfo.setTotalUsage(100 - Double.parseDouble(matcher.group(0)));
        }
        ArrayList<Double> coreUsage = new ArrayList<>();
        while (matcher.find()) {
            coreUsage.add(100 - Double.parseDouble(matcher.group(0)));
        }
        cpuInfo.setCoreUsage(coreUsage);

        return cpuInfo;
    }
}
```

#### Step3：映射命令配置文件

​	在`resources/configure`目录下创建一个`QueryCpuInfo.xml`文件，**建议该文件名与接口名称一致**。该文件描述了与命令执行有关的信息（**注意：配置文件内容需要根据需求文档进行编写**），文件内容如下，：

```xml
<?xml version="1.0" encoding="utf-8" ?>
<description>
    <name>QueryCpuInfo</name>
    <async>false</async>
    <args>false</args>
    <execute>ALL_NODES</execute>
    <privilege>USER</privilege>
    <timeout>2</timeout>
    <comment>获取节点CPU信息</comment>
</description>
```

​	在`QueryCpuInfo`类中添加`@Configure`注解，注解参数`path`为XML文件的相对路径。此时，该命令执行类的默认配置和对应的XML文件已经进行了映射，可以通过修改XML文件实现修改命令执行类的默认配置。

```java
...
@Configure(path= "/configure/QueryCpuInfo.xml")
public class QueryCpuInfo extends AbstractCommandExecutor {
...
}
...
```

#### Step4：注册命令执行类

​	在`SupportedCommand.java`中，添加一个名为`QUERY_CPU_INFO`的枚举值，**枚举值为命令执行类的名称的大写下划线格式**，并为该枚举值添加`@Registry`注解。

**注意：`CommandExecutorFactory`内部通过枚举值反转生成对应的命令执行类，如果名称不一致则找不到相应的命令执行类**

```java
public enum SupportedCommand {
		...
    @Registry
    QUERY_CPU_INFO,
		...
}
```

#### Step5：实现接口

​	在`GlobalCacheSDK.java`中，添加对应的**静态方法**且**方法必须使用public修饰**，方法的参数根据需求拟定，返回值默认为`HashMap<String, CommandExecuteResult>`。

​	在该方法中需要完成以下3个工作：

* 调用`CommandExecutorFactory.getCommandExecutor`，根据对应的`SupportedCommand`获得相应的executor实例
* 对于带参命令，将参数按照指定规则拼接为字符串
* 调用`SSHSessionPool.execute`让内部线程池并发执行executor

```java
public static HashMap<String, CommandExecuteResult> queryCpuInfo(ArrayList<String> hosts) throws GlobalCacheSDKException {
  // step1 根据"SupportedCommand.QUERY_CPU_INFO"获得QueryCpuInfo的
	AbstractCommandExecutor executor = getInstance().commandExecutorFactory.getCommandExecutor(SupportedCommand.QUERY_CPU_INFO);
  try {
  	ArrayList<String> users = new ArrayList<>(hosts.size());
    String user = Utils.enumExecutePrivilegeName(executor.getDes().getExecutePrivilege());
    for (String host : hosts) {
    	users.add(user);
    }
    // step3 调用"SSHSessionPool.execute"让内部线程池并发执行QueryCpuInfo的exec方法
    return getInstance().sshSessionPool.execute(hosts, users, executor);
  } catch (SSHSessionPoolException e) {
  	throw new GlobalCacheSDKException("SSH会话池异常", e);
  }
}
...
```

### 异步接口开发

待补充...

## TODO List

- [x] 支持动态配置接口
- [x] 支持接口自动实例化
- [x] 支持从XML文件反转生成接口配置
- [ ] 支持异步请求