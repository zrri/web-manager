package service.cm.core.node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import service.cm.core.common.monitor.MonitorClient;
import service.cm.core.common.node.NodeInfo;
import service.cm.core.common.node.NodeQueryUtil;
import service.cm.core.node.filesystem.ServerFileSystemAccessor;
import both.common.constant.ConnectionState;
import both.common.constant.OS;
import both.common.constant.Protocol;
import both.common.jmx.JmxAccessor;
import both.common.shell.ConnectorFactory;
import both.common.shell.IConnector;
import both.common.shell.LangGetter;
import both.common.shell.NetArgs;
import both.common.shell.PromptGetter;
import both.common.shell.ResultExtractor;
import both.common.shell.ShellExcutor;
import both.common.shell.ShellScriptManager;
import both.common.shell.SystemGetter;
import both.common.util.LoggerUtil;
import cn.com.bankit.phoenix.commons.util.StringUtil;

/**
 * 节点
 * 
 * @author 江成
 * 
 */
public class Node {

	/**
	 * 日志
	 */
	private static Logger logger = LoggerFactory.getLogger(Node.class);

	/**
	 * 超时为10秒
	 */
	private int timeout = 10000;

	/**
	 * 节点所在服务器的语言
	 */
	private String lang;

	/**
	 * 提示符
	 */
	private String prompt;

	/**
	 * 获取系统类型
	 */
	private OS os;

	/**
	 * 节点信息
	 */
	private NodeInfo nodeInfo;

	/**
	 * monitor访问器
	 */
	private MonitorClient monitorClient;

	/**
	 * JVM访问器
	 */
	private JmxAccessor jvmAccessor;

	/**
	 * 环境是否已经准备完成
	 */
	private boolean isPrepareEnvironment = false;

	/**
	 * JVM是否已经准备完成
	 */
	private boolean isPrepareJvm = false;

	/**
	 * 上一次CPU时间
	 */
	private long preCpuTime;

	/**
	 * 上一次时间
	 */
	private long preTime;

	/**
	 * HTTP port
	 */
	private int httpPort = -1;

	/**
	 * 秒单位
	 */
	private long secondUnit = 1000;

	/**
	 * 分单位
	 */
	private long minuteUnit = secondUnit * 60;

	/**
	 * 小时单位
	 */
	private long hourUnit = minuteUnit * 60;

	/**
	 * 天单位
	 */
	private long dayUnit = hourUnit * 24;

	/**
	 * 构造函数
	 * 
	 * @param nodeInfo
	 */
	public Node(NodeInfo nodeInfo) {
		this.nodeInfo = nodeInfo;
		
		try {
			prepareEnvironment();
		} catch (Exception e) {
			LoggerUtil.error(e.getMessage(),e);
		}
	}

	/**
	 * 获取节点名称
	 * 
	 * @return
	 */
	public String getName() {
		return this.nodeInfo.getName();
	}

	/**
	 * 获取描述信息
	 * 
	 * @return
	 */
	public String getDescription() {
		return this.nodeInfo.getDescription();
	}

	/**
	 * 获取节点地址
	 * 
	 * @return
	 */
	public String getHost() {
		return nodeInfo.getHost();
	}

	/**
	 * 获取节点端口
	 * 
	 * @return
	 */
	public int getPort() {
		return nodeInfo.getAppPort();
	}

	/**
	 * 启动节点
	 */
	public boolean start() throws Exception {
		// 准备环境
		if (!this.prepareEnvironment()) {
			return false;
		}

		// 获取当前时间
		long s = System.currentTimeMillis();
		logger.info("开始启动服务器");
		try {
			// 获取协议
			Protocol protocol = nodeInfo.getProtocol();
			// 获取解压命令
			String script = ShellScriptManager.getScript(this.os.getName(),
					"startup.sh", nodeInfo.getAppDir());
			String command = script;
			command += "exit\n";

			// 定义网络参数
			NetArgs netArgs = new NetArgs();
			netArgs.ip = nodeInfo.getHost();
			netArgs.port = nodeInfo.getPort();
			netArgs.userName = nodeInfo.getUserName();
			netArgs.password = nodeInfo.getPassword();
			netArgs.timeout = this.timeout;
			// 获取连接器
			IConnector connector = ConnectorFactory.getConnector(protocol,
					netArgs);
			try {
				// 获取编码格式
				String encoding = this.getEncoding(this.lang);
				// 连接服务器
				connector.connect();
				// 执行命令
				ShellExcutor.execute(command, encoding, connector,
						this.timeout * 3);
			} finally {
				connector.disconnect();
			}
		} catch (Exception e) {
			String msg = "启动服务器出错，错误服务器[host:" + this.nodeInfo.getHost()
					+ " ,port:" + this.nodeInfo.getPort() + "]";
			logger.error(msg, e);
			throw e;
		}
		logger.info("完成启动服务器，耗时:" + (System.currentTimeMillis() - s) + "毫秒");
		return true;
	}

	/**
	 * 关闭节点
	 * 
	 * @return
	 */
	public boolean stop() throws Exception {
		// 准备环境
		if (!this.prepareEnvironment()) {
			return false;
		}

		// 获取当前时间
		long s = System.currentTimeMillis();
		logger.info("开始关闭服务器");
		try {
			// 获取协议
			Protocol protocol = nodeInfo.getProtocol();
			// 获取关闭命令
			String script = ShellScriptManager.getScript(this.os.getName(),
					"shutdown.sh", nodeInfo.getAppDir());
			String command = script;
			command += "exit\n";

			// 定义网络参数
			NetArgs netArgs = new NetArgs();
			netArgs.ip = nodeInfo.getHost();
			netArgs.port = nodeInfo.getPort();
			netArgs.userName = nodeInfo.getUserName();
			netArgs.password = nodeInfo.getPassword();
			netArgs.timeout = this.timeout;
			// 获取连接器
			IConnector connector = ConnectorFactory.getConnector(protocol,
					netArgs);
			try {
				// 获取编码格式
				String encoding = this.getEncoding(this.lang);
				// 连接服务器
				connector.connect();
				// 执行命令
				ShellExcutor.execute(command, encoding, connector,
						this.timeout * 3);
			} finally {
				connector.disconnect();
			}
		} catch (Exception e) {
			String msg = "关闭服务器出错，错误服务器[host:" + this.nodeInfo.getHost()
					+ " ,port:" + this.nodeInfo.getPort() + "]";
			logger.error(msg, e);
			throw e;
		}
		logger.info("完成关闭服务器，耗时:" + (System.currentTimeMillis() - s) + "毫秒");
		return true;
	}

	/**
	 * 是否启动
	 * 
	 * @return
	 */
	public boolean isStarted() throws Exception {
		// 准备环境
		if (!this.prepareEnvironment()) {
			return false;
		}

		// 获取当前时间
		long s = System.currentTimeMillis();
		logger.info("判断是否启动");

		try {
			// 获取协议
			Protocol protocol = nodeInfo.getProtocol();
			// 获取关闭命令
			String script = ShellScriptManager.getScript(os.getName(),
					"checkServerState.sh",
					String.valueOf(nodeInfo.getAppPort()));
			String command = script;
			command += "exit\n";

			// 定义网络参数
			NetArgs netArgs = new NetArgs();
			netArgs.ip = nodeInfo.getHost();
			netArgs.port = nodeInfo.getPort();
			netArgs.userName = nodeInfo.getUserName();
			netArgs.password = nodeInfo.getPassword();
			netArgs.timeout = this.timeout;
			// 获取连接器
			IConnector connector = ConnectorFactory.getConnector(protocol,
					netArgs);
			try {
				// 获取编码格式
				String encoding = this.getEncoding(this.lang);
				// 连接服务器
				connector.connect();
				// 执行命令
				String data = ShellExcutor.execute(command, encoding,
						connector, this.timeout * 3);
				String res = ResultExtractor.extract(data, this.prompt, "exit");
				if (containsKey(res, "open")) {
					logger.debug("检查服务器状态成功，服务器处于启动状态，耗时:"
							+ (System.currentTimeMillis() - s) + "毫秒");
					return true;
				} else if (containsKey(res, "close")) {
					logger.debug("检查服务器状态成功，服务器处于关闭状态，耗时:"
							+ (System.currentTimeMillis() - s) + "毫秒");
					return false;
				}
			} finally {
				connector.disconnect();
			}
		} catch (Exception e) {
			String msg = "检查服务器状态出错，错误服务器[host:" + nodeInfo.getHost()
					+ " ,port:" + nodeInfo.getPort() + "]";
			Exception ex = new Exception(msg, e);
			throw ex;
		}
		logger.info("检查服务器状态成功，服务器处于未知状态，耗时:"
				+ (System.currentTimeMillis() - s) + "毫秒");
		return false;
	}

	/**
	 * 获取连接到该节点的所有客户端地址列表
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAllClientAddress() throws Exception {
		
		// 定义对象名称
		String objectName = "CommunicationServerMonitor";
		//定义方法名
		String methodName = "doGetAllRemoteAddress";
		// 获取客户端地址列表
		List<String> list = (List<String>) this.monitorClient.invoke(objectName, methodName);


		// 获取所有节点信息
		NodeInfo[] nodeInfos = NodeQueryUtil.queryAll();
		if (nodeInfos == null || nodeInfos.length == 0) {
			return list;
		}

		// 构建host地址集合
		Set<String> nodeHosts = new HashSet<String>();
		for (int i = 0; i < nodeInfos.length; i++) {
			nodeHosts.add(nodeInfos[i].getHost());
		}

		List<String> clientList = new ArrayList<String>();
		for (int i = 0, size = list.size(); i < size; i++) {
			String s = list.get(i);
			int index = s.indexOf(":");
			if (index != -1) {
				s = s.substring(0, index);
			}
			// 如果地址不为服务器地址，加入列表中
			if (!nodeHosts.contains(s)) {
				clientList.add(list.get(i));
			}

		}
		return clientList;

	}

	/**
	 * 获取连接到该节点的客户端数量
	 * 
	 * @return
	 */
	public int getClientCount() throws Exception {
		List<String> list = this.getAllClientAddress();
		if (list == null) {
			return 0;
		}
		return list.size();
	}

	/**
	 * 获取CPU使用率
	 * 
	 * @return
	 */
	public float getCpuUsage() throws Exception {
		// 准备JVM环境
		if (!this.prepareJvm()) {
			return 0;
		}

		// 获取当前时间
		long s = System.currentTimeMillis();
		logger.info("开始获取cup使用率");
		// cpu使用率
		float cpusage = -1;
		try {
			// 获取解压命令
			String script = "";
			String osName = this.os.getName();
			script = ShellScriptManager.getScript(osName,
					"getCpuUsage.sh",
					String.valueOf(this.nodeInfo.getJvmPort()));
			// script = ShellScriptManager.getScript(this.os.getName(),
			// "getCloseWaitCounter.sh",
			// Integer.toString(nodeInfo.getBipsPort()));
			String command = script;
			command += "exit\n";

			// 定义网络参数
			NetArgs netArgs = new NetArgs();
			netArgs.ip = nodeInfo.getHost();
			netArgs.port = nodeInfo.getPort();
			netArgs.userName = nodeInfo.getUserName();
			netArgs.password = nodeInfo.getPassword();
			netArgs.timeout = this.timeout;
			// 获取协议
			Protocol protocol = nodeInfo.getProtocol();
			// 获取连接器
			IConnector connector = ConnectorFactory.getConnector(protocol,
					netArgs);
			try {
				// 获取编码格式
				String encoding = this.getEncoding(this.lang);
				// 连接服务器
				connector.connect();

				if (this.os.getName().equals("linux")) {
					// 执行命令
					String data = ShellExcutor.execute(command, encoding,
							connector, this.timeout * 3);
					String res = ResultExtractor.extract(data, this.prompt,
							"exit");
					String[] items = StringUtil.split(res, "\n");
					for (int i = 0; i < items.length; i++) {
						try {
							float f = Float.parseFloat(items[i]);
							cpusage = f;
							break;
						} catch (Exception e) {
							// do nothing
						}
					}

					if (cpusage == -1) {
						logger.error("获取cpu使用率失败，返回结果为 :" + data);
					}
				} else if (this.os.getName().equals("aix")) {
					try {
						// 定义对象名称
						String objectName = "java.lang:type=OperatingSystem";
						// 记录当前可用进程
						int availableProcessors = (Integer) jvmAccessor
								.getAttribute(objectName, "AvailableProcessors");
						// 记录当前时间
						long time = System.currentTimeMillis();
						// 记录当前CPU使用时间
						long cpuTime = (Long) jvmAccessor.getAttribute(
								objectName, "ProcessCpuTime");

						long elapsedCpu = cpuTime - preCpuTime;
						long elapsedTime = time - preTime;
						// 计算CPU使用率
						float cpuUsage = 0;
						if (this.preCpuTime > 0) {
							cpuUsage = Math
									.min(99F,
											elapsedCpu
													/ (elapsedTime * 10000F * availableProcessors));
						}
						// 记录时间
						this.preCpuTime = cpuTime;
						this.preTime = time;
						return cpuUsage;
					} catch (Exception e) {
						this.releaseJvm();
						throw e;
					}
				}
			} finally {
				connector.disconnect();
			}

			logger.info("完成获取cpu使用率，耗时:" + (System.currentTimeMillis() - s)
					+ "毫秒");
			return cpusage;

		} catch (Exception e) {
			String msg = "获取cpu使用率，错误服务器[host:" + this.nodeInfo.getHost()
					+ " ,port:" + this.nodeInfo.getPort() + "]";
			logger.error(msg, e);
			throw e;
		}
		// try {
		// // 定义对象名称
		// String objectName = "java.lang:type=OperatingSystem";
		// // 记录当前可用进程
		// int availableProcessors = (Integer) jvmAccessor.getAttribute(
		// objectName, "AvailableProcessors");
		// // 记录当前时间
		// long time = System.currentTimeMillis();
		// // 记录当前CPU使用时间
		// long cpuTime = (Long) jvmAccessor.getAttribute(objectName,
		// "ProcessCpuTime");
		//
		// long elapsedCpu = cpuTime - preCpuTime;
		// long elapsedTime = time - preTime;
		// // 计算CPU使用率
		// float cpuUsage = 0;
		// if (this.preCpuTime > 0) {
		// cpuUsage = Math.min(99F, elapsedCpu
		// / (elapsedTime * 10000F * availableProcessors));
		// }
		// // 记录时间
		// this.preCpuTime = cpuTime;
		// this.preTime = time;
		// return cpuUsage;
		// } catch (Exception e) {
		// this.releaseJvm();
		// throw e;
		// }
	}

	/**
	 * 获取总内存量(单位KB)
	 * 
	 * @return
	 */
	public long getTotalMemorySize() throws Exception {
		// 准备JMX环境
		if (!this.prepareJvm()) {
			return 0;
		}
		// 准备环境
		if (!this.prepareEnvironment()) {
			return 0;
		}

		try {
			// 定义对象名称
			String objectName = "java.lang:type=Memory";
			if (OS.LINUX == this.os) {
				// 获取堆内存使用情况
				javax.management.openmbean.CompositeData heapMemoryData = (javax.management.openmbean.CompositeData) jvmAccessor
						.getAttribute(objectName, "HeapMemoryUsage");
				// 获取非堆内存使用情况
				javax.management.openmbean.CompositeData nonHeapMemoryData = (javax.management.openmbean.CompositeData) jvmAccessor
						.getAttribute(objectName, "NonHeapMemoryUsage");
				long heapMemorySize = (Long) heapMemoryData.get("max");
				long nonHeapMemorySize = (Long) nonHeapMemoryData.get("max");

				// 计算总内存
				long size = (heapMemorySize + nonHeapMemorySize) / 1024;
				return size;
			} else {
				// 获取堆内存使用情况
				javax.management.openmbean.CompositeData heapMemoryData = (javax.management.openmbean.CompositeData) jvmAccessor
						.getAttribute(objectName, "HeapMemoryUsage");
				// 获取非堆内存使用情况
				javax.management.openmbean.CompositeData nonHeapMemoryData = (javax.management.openmbean.CompositeData) jvmAccessor
						.getAttribute(objectName, "NonHeapMemoryUsage");
				long heapMemorySize = (Long) heapMemoryData.get("committed");
				long nonHeapMemorySize = (Long) nonHeapMemoryData
						.get("committed");

				// 计算总内存
				long size = (heapMemorySize + nonHeapMemorySize) / 1024;
				return size;
			}
		} catch (Exception e) {
			this.releaseJvm();
			throw e;
		}
	}

	/**
	 * 获取使用内存量(单位KB)
	 * 
	 * @return
	 */
	public long getUsedMemorySize() throws Exception {
		// 准备JMX环境
		if (!this.prepareJvm()) {
			return 0;
		}
		// 准备环境
		if (!this.prepareEnvironment()) {
			return 0;
		}

		try {
			// 定义对象名称
			String objectName = "java.lang:type=Memory";
			if (OS.LINUX == this.os) {
				// 获取堆内存使用情况
				javax.management.openmbean.CompositeData heapMemoryData = (javax.management.openmbean.CompositeData) jvmAccessor
						.getAttribute(objectName, "HeapMemoryUsage");
				// 获取非堆内存使用情况
				javax.management.openmbean.CompositeData nonHeapMemoryData = (javax.management.openmbean.CompositeData) jvmAccessor
						.getAttribute(objectName, "NonHeapMemoryUsage");
				long heapMemorySize = (Long) heapMemoryData.get("used");
				long nonHeapMemorySize = (Long) nonHeapMemoryData.get("used");

				// 计算总内存
				long size = (heapMemorySize + nonHeapMemorySize) / 1024;
				return size;
			} else {
				// 获取堆内存使用情况
				javax.management.openmbean.CompositeData heapMemoryData = (javax.management.openmbean.CompositeData) jvmAccessor
						.getAttribute(objectName, "HeapMemoryUsage");
				// 获取非堆内存使用情况
				javax.management.openmbean.CompositeData nonHeapMemoryData = (javax.management.openmbean.CompositeData) jvmAccessor
						.getAttribute(objectName, "NonHeapMemoryUsage");
				long heapMemorySize = (Long) heapMemoryData.get("used");
				long nonHeapMemorySize = (Long) nonHeapMemoryData.get("used");

				// 计算总内存
				long size = (heapMemorySize + nonHeapMemorySize) / 1024;
				return size;
			}
		} catch (Exception e) {
			this.releaseJvm();
			throw e;
		}
	}

	/**
	 * 获取运行时间
	 * 
	 * @return(毫秒)
	 */
	public long getRunningTime() throws Exception {
		// 准备JMX环境
		this.prepareJvm();
		try {
			// 定义对象名称
			String objectName = "java.lang:type=Runtime";
			// 获取JVM运行时间情况
			long time = (Long) jvmAccessor.getAttribute(objectName, "Uptime");
			return time;
		} catch (Exception e) {
			this.releaseJvm();
			throw e;
		}
	}

	/**
	 * 获取运行时间(带格式(天时分秒))
	 * 
	 * @return
	 */
	public String getRunningTimeWithFormat() throws Exception {
		// 获取运行时间
		long time = this.getRunningTime();
		int day = (int) (time / dayUnit);
		time = time - day * dayUnit;
		int hour = (int) (time / hourUnit);
		time = time - hour * hourUnit;
		int minute = (int) (time / minuteUnit);
		time = time - minute * minuteUnit;
		int second = (int) (time / secondUnit);

		StringBuilder sb = new StringBuilder();
		if (second != 0) {
			sb.append(second);
			sb.append("秒");
		}

		if (minute != 0) {
			sb.insert(0, "分钟");
			sb.insert(0, minute);
		}

		if (hour != 0) {
			sb.insert(0, "小时");
			sb.insert(0, hour);
		}

		if (day != 0) {
			sb.insert(0, "天");
			sb.insert(0, day);
		}
		return sb.toString();
	}

	/**
	 * 获取JVM输入参数
	 * 
	 * @return
	 */
	public String[] getJvmInputArguments() throws Exception {
		// 准备JMX环境
		this.prepareJvm();
		try {
			// 定义对象名称
			String objectName = "java.lang:type=Runtime";
			// 获取JVM输入参数
			String[] args = (String[]) jvmAccessor.getAttribute(objectName,
					"InputArguments");
			return args;
		} catch (Exception e) {
			this.releaseJvm();
			throw e;
		}
	}

	/**
	 * 获取当前加载的类数量
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getLoadedClassCount() throws Exception {
		// 准备JMX环境
		this.prepareJvm();
		try {
			// 定义对象名称
			String objectName = "java.lang:type=ClassLoading";
			// 获取当前加载的类数量
			int count = (Integer) jvmAccessor.getAttribute(objectName,
					"LoadedClassCount");
			return count;
		} catch (Exception e) {
			this.releaseJvm();
			throw e;
		}
	}

	/**
	 * 获取已经加载的类数量
	 * 
	 * @return
	 * @throws Exception
	 */
	public long getTotalLoadedClassCount() throws Exception {
		// 准备JMX环境
		this.prepareJvm();
		try {
			// 定义对象名称
			String objectName = "java.lang:type=ClassLoading";
			// 获取已经加载的类数量
			long count = (Long) jvmAccessor.getAttribute(objectName,
					"TotalLoadedClassCount");
			return count;
		} catch (Exception e) {
			this.releaseJvm();
			throw e;
		}
	}

	/**
	 * 获取已经卸载的类数量
	 * 
	 * @return
	 * @throws Exception
	 */
	public long getUnloadedClassCount() throws Exception {
		// 准备JMX环境
		this.prepareJvm();
		try {
			// 定义对象名称
			String objectName = "java.lang:type=ClassLoading";
			// 获取已经卸载的类数量
			long count = (Long) jvmAccessor.getAttribute(objectName,
					"UnloadedClassCount");
			return count;
		} catch (Exception e) {
			this.releaseJvm();
			throw e;
		}
	}

	/**
	 * 获取当前活动线程数
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getThreadCount() throws Exception {
		// 准备JMX环境
		this.prepareJvm();
		try {
			// 定义对象名称
			String objectName = "java.lang:type=Threading";
			// 获取当前活动线程数
			int count = (Integer) jvmAccessor.getAttribute(objectName,
					"ThreadCount");
			return count;
		} catch (Exception e) {
			this.releaseJvm();
			throw e;
		}
	}

	/**
	 * 获取峰值线程数
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getPeakThreadCount() throws Exception {
		// 准备JMX环境
		this.prepareJvm();
		try {
			// 定义对象名称
			String objectName = "java.lang:type=Threading";
			// 获取峰值线程数
			int count = (Integer) jvmAccessor.getAttribute(objectName,
					"PeakThreadCount");
			return count;
		} catch (Exception e) {
			this.releaseJvm();
			throw e;
		}
	}

	/**
	 * 获取守护线程数
	 * 
	 * @return
	 * @throws Exception
	 */
	public long getDaemonThreadCount() throws Exception {
		// 准备JMX环境
		this.prepareJvm();
		try {
			// 定义对象名称
			String objectName = "java.lang:type=Threading";
			// 获取守护线程数
			int count = (Integer) jvmAccessor.getAttribute(objectName,
					"DaemonThreadCount");
			return count;
		} catch (Exception e) {
			this.releaseJvm();
			throw e;
		}
	}

	/**
	 * 获取已经启动过的线程数
	 * 
	 * @return
	 * @throws Exception
	 */
	public long getTotalStartedThreadCount() throws Exception {
		// 准备JMX环境
		this.prepareJvm();
		try {
			// 定义对象名称
			String objectName = "java.lang:type=Threading";
			// 获取已经启动的线程数
			long count = (Long) jvmAccessor.getAttribute(objectName,
					"TotalStartedThreadCount");
			return count;
		} catch (Exception e) {
			this.releaseJvm();
			throw e;
		}
	}

	/**
	 * 获取数据库池名称列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String[] getDBSourceNames() throws Exception {
		// 定义对象名称
		String objectName = "DBMonitor";
		// 定义方法名
		String methodName="getDBSourceNames";
		// 获取DBSource名称列表
		String[] dbSourceNames = (String[]) this.monitorClient.invoke(objectName, methodName);
		return dbSourceNames;
	}

	/**
	 * 根据数据池名称获取数据池类型
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public String getDBSourceType(String name) throws Exception {
		// 定义对象名称
		String objectName = "DBMonitor";
		//定义方法名
		String methodName ="doGetDBSourceType";
		// 获取数据库池类型
		String dbSourceType = (String) this.monitorClient.invoke(objectName,
							methodName, name);
		return dbSourceType;
	}

	/**
	 * 根据数据池名称获取数据库类型
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public String getDBType(String name) throws Exception {
		// 定义对象名称
		String objectName = "DBMonitor";
		// 获取数据库类型
		String dbType = (String) monitorClient.invoke(objectName,
							"doGetDBType", name);
		return dbType;
	}

	/**
	 * 获取数据库池连接数量
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public int getDBSourceConnectionNum(String name) throws Exception {
		// 定义对象名称
		String objectName = "DBMonitor";
		// 获取数据库池当前连接数
		int num = (Integer) monitorClient.invoke(objectName,
				"doGetConnectionNum",  name );
		return num;
	}

	/**
	 * 获取数据库池连接数量（通过端口）
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getDBCount() throws Exception {
		// 准备JVM环境
		if (!this.prepareJvm()) {
			return 0;
		}

		// 获取当前时间
		long s = System.currentTimeMillis();
		logger.info("开始获取数据库连接数");
		// cpu使用率
		int DBCount = -1;
		try {
			// 获取解压命令
			String script = "";
			script = ShellScriptManager.getScript(this.os.getName(), "getDBCount.sh",
					"1521");
			String command = script;
			command += "exit\n";

			// 定义网络参数
			NetArgs netArgs = new NetArgs();
			netArgs.ip = nodeInfo.getHost();
			netArgs.port = nodeInfo.getPort();
			netArgs.userName = nodeInfo.getUserName();
			netArgs.password = nodeInfo.getPassword();
			netArgs.timeout = this.timeout;
			// 获取协议
			Protocol protocol = nodeInfo.getProtocol();
			// 获取连接器
			IConnector connector = ConnectorFactory.getConnector(protocol,
					netArgs);
			try {
				// 获取编码格式
				String encoding = this.getEncoding(this.lang);
				// 连接服务器
				connector.connect();

				if (this.os.getName().equals("linux")) {
					// 执行命令
					String data = ShellExcutor.execute(command, encoding,
							connector, this.timeout * 3);
					String res = ResultExtractor.extract(data, this.prompt,
							"exit");
					String[] items = StringUtil.split(res, "\n");
					for (int i = 0; i < items.length; i++) {
						try {
							int n = Integer.parseInt(items[i]);
							DBCount = n;
							break;
						} catch (Exception e) {
							// do nothing
						}
					}

					if (DBCount == -1) {
						logger.error("获取数据库连接数失败，返回结果为 :" + data);
					}
				}
			} finally {
				connector.disconnect();
			}

			logger.info("完成获取数据库连接数，耗时:" + (System.currentTimeMillis() - s)
					+ "毫秒");
			return DBCount;

		} catch (Exception e) {
			String msg = "获取数据库连接数，错误服务器[host:" + this.nodeInfo.getHost()
					+ " ,port:1521]";
			logger.error(msg, e);
			throw e;
		}

	}

	/**
	 * 获取数据库池最大连接数量
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public int getMaxDBSourceConnectionNum(String name) throws Exception {
		// 定义对象名称
		String objectName = "DBMonitor";
		// 获取数据库池最大连接数
		int num = (Integer) monitorClient.invoke(objectName,
				  "doGetMaxConnectionNum", name);
		return num;
	}

	/**
	 * 获取service监控数据
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<String, Map<String, Map<String, Object>>> getServiceCounterData()
			throws Exception {
		// 定义对象名称
		String objectName = "ServiceMonitor";
		// 获取service统计数据
		@SuppressWarnings("unchecked")
		Map<String, Map<String, Map<String, Object>>> data = (Map<String, Map<String, Map<String, Object>>>) monitorClient
				.invoke(objectName, "doGetTransCounterData");
		return data;
	}

	/**
	 * 清空service监控数据
	 */
	public void clearServiceCounterData() throws Exception {
		// 定义对象名称
		String objectName = "ServiceMonitor";
		// 获取service统计数据
		@SuppressWarnings("unchecked")
		List<String> names = (List<String>) monitorClient.invoke(objectName,
							"doGetTransCounterNames");
		if (names == null || names.size() == 0) {
			return;
		}

		for (int i = 0, size = names.size(); i < size; i++) {
		    String name = names.get(i);
		    monitorClient.invoke(objectName, "doClearTransCounterData",name);
		}
	}

	/**
	 * 获取事务监控数据
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<String, Map<String, Map<String, Object>>> getTransCounterData()
			throws Exception {
		// 定义对象名称
		String objectName = "CustomMonitor";
		// 获取自定义事务统计数据
		@SuppressWarnings("unchecked")
		Map<String, Map<String, Map<String, Object>>> data = (Map<String, Map<String, Map<String, Object>>>) monitorClient
				.invoke(objectName, "doGetTransCounterData");
		return data;
	}

	/**
	 * 清空自定义事务统计数据
	 */
	public void clearTransCounterData() throws Exception {
		// 定义对象名称
		String objectName = "CustomMonitor";
		// 获取自定义事务统计数据
		@SuppressWarnings("unchecked")
		List<String> names = (List<String>) monitorClient.invoke(objectName,
				"doGetTransCounterNames");
		if (names == null || names.size() == 0) {
			return;
		}

		for (int i = 0, size = names.size(); i < size; i++) {
			String name = names.get(i);
			monitorClient.invoke(objectName, "doClearTransCounterData",name);
		}
	}

	/**
	 * 获取事务记录数据
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<String, Map<String, Map<String, String>>> getTransStateRecorderData()
			throws Exception {
		// 定义对象名称
		String objectName = "CustomMonitor";
		// 获取自定义事务记录数据
		@SuppressWarnings("unchecked")
		Map<String, Map<String, Map<String, String>>> data = (Map<String, Map<String, Map<String, String>>>) monitorClient
				.invoke(objectName, "doGetTransStateRecorderData");
		return data;
	}

	/**
	 * 清空指定自定义事务状态记录器数据
	 */
	public void clearTransStateRecorderData() throws Exception {
		// 定义对象名称
		String objectName = "CustomMonitor";
		// 获取自定义事务状态记录器数据
		@SuppressWarnings("unchecked")
		List<String> names = (List<String>) monitorClient.invoke(objectName,
				"doGetTransStateRecorderNames");
		if (names == null || names.size() == 0) {
			return;
		}

		for (int i = 0, size = names.size(); i < size; i++) {
			String name = names.get(i);
			monitorClient.invoke(objectName, "doClearTransStateRecorderData",name);
		}
	}

	/**
	 * 设置节点使用允许新的客户端连接上去
	 * 
	 * @param allow
	 * @return
	 * @throws Exception
	 */
	public boolean allowNewConnection(boolean allow) throws Exception {
		// 定义对象名称
		String objectName = "CommunicationServerMonitor";
		// 设置节点使用允许新的客户端连接上去
		monitorClient.invoke(objectName, "doSetAllowNewConnection",allow);
		return true;
	}

	/**
	 * 获取HTTP服务端口
	 * 
	 * @return(return -1代表访问出错)
	 * @throws Exception
	 */
	public int getHttpServerPort() throws Exception {
		if (this.httpPort != -1) {
			return this.httpPort;
		}

		// 定义对象名称
		String objectName = "Phoenix Server:type=HttpServerMonitor";
		// 获取HTTP服务端口
		int port = (Integer) monitorClient.invoke(objectName,
							"getHttpServerPort");
		this.httpPort = port;
		return port;
	}

	/**
	 * 获取文件系统访问器
	 * 
	 * @return
	 */
	public IFileSystemAccessor getServerFileSysAccessor() throws Exception {
		// 获取HTTP端口
		int httpPort = this.getHttpServerPort();
		// 定义服务器文件系统访问器
		ServerFileSystemAccessor fileSystemAccessor = new ServerFileSystemAccessor(
				this.nodeInfo.getHost(), httpPort);
		return fileSystemAccessor;
	}


	/**
	 * 获取主机的分区使用情况
	 * 
	 * @return
	 */
	public PartitionState[] getPartitionState() throws Exception {
		// 准备JMX环境
		if (!this.prepareEnvironment()) {
			throw new Exception("准备环境失败");
		}

		// 获取当前时间
		long s = System.currentTimeMillis();
		logger.info("开始获取主机硬盘使用情况");
		try {
			// 获取协议
			Protocol protocol = nodeInfo.getProtocol();
			// 获取解压命令
			String script = ShellScriptManager.getScript(this.os.getName(),
					"getDiskState.sh");
			String command = script;
			command += "exit\n";

			// 定义网络参数
			NetArgs netArgs = new NetArgs();
			netArgs.ip = nodeInfo.getHost();
			netArgs.port = nodeInfo.getPort();
			netArgs.userName = nodeInfo.getUserName();
			netArgs.password = nodeInfo.getPassword();
			netArgs.timeout = this.timeout;
			// 获取连接器
			IConnector connector = ConnectorFactory.getConnector(protocol,
					netArgs);

			List<PartitionState> list = new ArrayList<PartitionState>();
			try {
				// 获取编码格式
				String encoding = this.getEncoding(this.lang);
				// 连接服务器
				connector.connect();
				// 执行命令
				String res = ShellExcutor.execute(command, encoding, connector,
						this.timeout * 3);
				// 提取结果
				res = ResultExtractor.extract(res, this.prompt, "exit");
				String[] lines = res.split("\n");

				for (int i = 0; i < lines.length; i++) {
					String line = lines[i];
					if (line.indexOf("%") == -1) {
						continue;
					}
					String[] items = line.split("[\t+|\\s]+");
					String lastItem = items[items.length - 1];
					if (lastItem.charAt(0) != '/') {
						continue;
					}

					PartitionState state = new PartitionState();

					if (OS.LINUX == os) {
						state.setFileSystem(items[0]);
						state.setMountedPoint(lastItem);
						double num = Double.parseDouble(items[1]);
						state.setTotalSpace((int) num);
						num = Double.parseDouble(items[2]);
						state.setUsedSpace((int) num);
						String str = items[4].trim();
						// 去掉%
						str = str.substring(0, str.length() - 1);
						double ratio = Double.parseDouble(str) / 100;
						state.setUsedRatio(ratio);

					} else {
						state.setFileSystem(items[0]);
						state.setMountedPoint(lastItem);
						double total = Double.parseDouble(items[1]);
						state.setTotalSpace((int) total);
						double free = Double.parseDouble(items[2]);
						state.setUsedSpace((int) (total - free + 0.5));
						String str = items[5].trim();
						// 去掉%
						str = str.substring(0, str.length() - 1);
						double ratio = Double.parseDouble(str) / 100;
						state.setUsedRatio(ratio);
					}
					list.add(state);
					System.err.println("fileSystemn:" + state.getFileSystem()
							+ " ,mountedPoint:" + state.getMountedPoint()
							+ " ,total:" + state.getTotalSpace() + " ,used:"
							+ state.getUsedSpace() + " ,比率"
							+ (state.getUsedRatio() * 100) + "%");
					System.err.println("--> " + lines[i]);
				}

			} finally {
				connector.disconnect();
			}
			logger.info("完成获取主机硬盘使用情况，耗时:" + (System.currentTimeMillis() - s)
					+ "毫秒");
			return list.toArray(new PartitionState[0]);

		} catch (Exception e) {
			String msg = "获取主机硬盘使用情况，错误服务器[host:" + this.nodeInfo.getHost()
					+ " ,port:" + this.nodeInfo.getPort() + "]";
			logger.error(msg, e);
			throw e;
		}
	}

	/**
	 * 是否资源
	 * 
	 * @return
	 */
	public boolean release() {
		// 释放JVM
		this.releaseJvm();
		this.isPrepareEnvironment = false;
		this.httpPort = -1;
		return true;
	}

	/**
	 * 判断内容中是否包含指定的关键字
	 * 
	 * @param content
	 * @param key
	 * @return
	 */
	private boolean containsKey(String content, String key) {
		if (content == null) {
			return false;
		}
		int index = content.indexOf(key);
		return (index != -1);
	}

	/**
	 * 获取编码个性
	 * 
	 * @param encoding
	 * @return
	 */
	private String getEncoding(String lang) {
		// 大写格式
		lang = lang.toUpperCase();
		if (lang.indexOf("UTF-8") != -1 || lang.indexOf("UTF8") != -1) {
			return "UTF-8";
		} else {
			return "GBK";
		}
	}

	/**
	 * 准备工作
	 */
	private synchronized boolean prepareEnvironment() throws Exception {
		// 如果已经准备完成，不再重复处理
		if (this.isPrepareEnvironment) {
			return true;
		}
		// 获取当前时间
		long s = System.currentTimeMillis();
		logger.debug("开始环境准备");
		try {
			// 定义网络参数
			NetArgs netArgs = new NetArgs();
			netArgs.ip = this.nodeInfo.getHost();
			netArgs.port = this.nodeInfo.getPort();
			netArgs.userName = this.nodeInfo.getUserName();
			netArgs.password = this.nodeInfo.getPassword();
			netArgs.timeout = this.timeout;
			// 获取协议
			Protocol protocol = this.nodeInfo.getProtocol();
			// 获取提示符
			this.prompt = PromptGetter.getPrompt(protocol, netArgs);
			logger.debug("获取提示符：" + prompt);
			// 获取系统类型
			this.os = SystemGetter.getSystemType(protocol, netArgs, prompt);
			logger.debug("获取系统类型：" + os);
			// 获取系统语言
			this.lang = LangGetter.getUsedLang(prompt, os, protocol, netArgs);
			logger.debug("获取系统语言：" + lang);
			// 是否已经准备完成
			this.isPrepareEnvironment = true;
		} catch (Exception e) {
			String msg = "准备环境出错，错误服务器[host:" + this.nodeInfo.getHost()
					+ " ,port:" + this.nodeInfo.getPort() + "]";
			Exception ex = new Exception(msg, e);
			throw ex;
		}
		logger.debug("完成环境准备，耗时:" + (System.currentTimeMillis() - s) + "毫秒");
		return true;
	}

	/**
	 * 准备JVM环境
	 * 
	 * @return
	 */
	private synchronized boolean prepareJvm() {
		if (isPrepareJvm) {
			return true;
		}
		// 定义JVM连接器
		jvmAccessor = new JmxAccessor();
		boolean connected = jvmAccessor.connect(nodeInfo.getHost(),
				nodeInfo.getJvmPort());
		if (connected) {
			this.isPrepareJvm = true;
		} else {
			jvmAccessor.disconnect();
		}
		return connected;
	}

	/**
	 * 释放JVM环境
	 */
	private synchronized void releaseJvm() {
		if (this.jvmAccessor != null) {
			this.jvmAccessor.disconnect();
			this.jvmAccessor = null;
		}
		this.isPrepareJvm = false;
	}

	/**
	 * 判断节点信息是否改变
	 * 
	 * @return
	 */
	public boolean isChange(NodeInfo nodeInfo) {
		return !this.nodeInfo.equals(nodeInfo);
	}

	/**
	 * 获取节点信息
	 * 
	 * @return
	 */
	public NodeInfo getNodeInfo() {
		return this.nodeInfo;
	}

	/**
	 * 获取指定状态数量
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getStateCounter(ConnectionState state) throws Exception {
		// 准备JMX环境
		if (!this.prepareEnvironment()) {
			throw new Exception("准备环境失败");
		}

		// 获取当前时间
		long s = System.currentTimeMillis();
		logger.info("开始获取close wait数量");
		// 保存数量
		int count = -1;
		try {
			String scriptname = "";
			if (state.equals(ConnectionState.CLOSE_WAIT)) {
				// 获取解压命令
				scriptname = "getCloseWaitCounter.sh";

			} else if (state.equals(ConnectionState.ESTABLISHED)) {
				// 获取解压命令
				scriptname = "getEstablishedCounter.sh";
			}
			// 获取解压命令
			String script = "";
			if (scriptname.equals("")) {
				return -1;
			} else {
				script = ShellScriptManager.getScript(this.os.getName(), scriptname,
						Integer.toString(nodeInfo.getAppPort()));
			}
			String command = script;
			command += "exit\n";

			// 定义网络参数
			NetArgs netArgs = new NetArgs();
			netArgs.ip = nodeInfo.getHost();
			netArgs.port = nodeInfo.getPort();
			netArgs.userName = nodeInfo.getUserName();
			netArgs.password = nodeInfo.getPassword();
			netArgs.timeout = this.timeout;
			// 获取协议
			Protocol protocol = nodeInfo.getProtocol();
			// 获取连接器
			IConnector connector = ConnectorFactory.getConnector(protocol,
					netArgs);
			try {
				// 获取编码格式
				String encoding = this.getEncoding(this.lang);
				// 连接服务器
				connector.connect();
				// 执行命令
				String data = ShellExcutor.execute(command, encoding,
						connector, this.timeout * 3);
				String res = ResultExtractor.extract(data, this.prompt, "exit");

				String[] items = StringUtil.split(res, "\n");
				for (int i = 0; i < items.length; i++) {
					try {
						int n = Integer.parseInt(items[i]);
						count = n;
						break;
					} catch (Exception e) {
						// do nothing
					}
				}

				if (count == -1) {
					logger.error("获取close wait状态失败，返回结果为 :" + data);
				}
			} finally {
				connector.disconnect();
			}

			logger.info("完成获取close wait数量，耗时:"
					+ (System.currentTimeMillis() - s) + "毫秒");
			return count;

		} catch (Exception e) {
			String msg = "获取close wait数量，错误服务器[host:" + this.nodeInfo.getHost()
					+ " ,port:" + this.nodeInfo.getPort() + "]";
			logger.error(msg, e);
			throw e;
		}
	}
}
