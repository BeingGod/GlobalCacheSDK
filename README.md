# Global Cache SDK

## 概述

​	Global Cache集群可视化软件数据接口（Global Cache SDK），其使用Shell脚本对集群查询数据命令进行封装，使用SSH协议对Web后端服务和集群数据传输进行加密，使用Java对查询数据脚本和自动化部署脚本调用进行封装，以Jar包形式为Web后端服务提供数据获取接口。

## 快速上手

* Global Cache SDK 使用者请参考<a href=#usage>使用</a>部分内容；
* Global Cache SDK 开发者请参考<a href=#develop>开发</a>部分内容。

## 特性

* **自动并发：**对于需要在多个节点上执行的命令，使用内部线程池将其转变为多个节点并发执行。
* **易配置**：用户可以使用XML文件对接口进行配置，同时也可以调用辅助配置接口在运行时动态修改接口配置。
* **易拓展**：接口的集成进行了依赖倒转，接口开发者只需要完成输入参数的处理的返回数据的解析，无需关注接口实例化和接口并发细节。
* **异步请求（todo）**：对于耗时较长的请求，通过异步防止阻塞当前线程，并支持从异步对象获取实时输出。

![image-20230124191003762](https://github.com/BeingGod/GlobalCacheSDK/assets/image-20230124191003762.png)

## <span id=usage>使用</span>

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

接下来以添加节点CPU信息请求接口为例，演示如何添加接口。

### Step1：抽象结果实体

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

### Step2：实现命令执行类代码

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

### Step3：映射命令配置文件

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

### Step4：注册命令执行类

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

### Step5：实现接口

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

## TODO List

- [x] 支持动态配置接口
- [x] 支持接口自动实例化
- [x] 支持从XML文件反转生成接口配置
- [ ] 支持异步请求