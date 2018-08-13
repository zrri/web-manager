package service.cm.core.deploy.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import service.cm.core.common.node.NodeInfo;
import service.cm.core.deploy.IProcessCallback;
import service.cm.core.deploy.ProcedureInfo;
import service.cm.core.deploy.constant.Action;
import both.common.constant.OS;
import both.common.constant.Protocol;
import both.common.shell.ConnectorFactory;
import both.common.shell.IConnector;
import both.common.shell.LangGetter;
import both.common.shell.NetArgs;
import both.common.shell.PromptGetter;
import both.common.shell.ResultExtractor;
import both.common.shell.ShellExcutor;
import both.common.shell.ShellScriptManager;
import both.common.shell.SystemGetter;

/**
 * 回退任务
 * 
 * @author 江成
 * 
 */
public class RollbackTask {

	/**
	 * 日志
	 */
	private static Logger logger = LoggerFactory.getLogger(RollbackTask.class);

	/**
	 * 超时为10秒
	 */
	private int timeout = 10000;

	/**
	 * ID
	 */
	private String id;

	/**
	 * 节点列表
	 */
	private NodeInfo[] nodeInfos;

	/**
	 * 回退文件
	 */
	private String rollbackVersion;

	/**
	 * 历史版本列表
	 */
	private List<String> historyVersions;

	/**
	 * 记录步骤
	 */
	private int step;

	/**
	 * 是否处于运行状态
	 */
	private boolean running;

	/**
	 * 更新节点信息
	 */
	private List<Item> items = new ArrayList<Item>();

	/**
	 * 回调函数
	 */
	private IProcessCallback processCallback;

	/**
	 * 构造函数
	 * 
	 * @param id
	 * @param nodeInfos
	 * @param rollbackVersion
	 * @param historyVersions
	 * @param processCallback
	 */
	public RollbackTask(String id, NodeInfo[] nodeInfos,
			String rollbackVersion, List<String> historyVersions,
			IProcessCallback processCallback) {
		this.id = id;
		this.nodeInfos = nodeInfos;
		this.rollbackVersion = rollbackVersion;
		this.historyVersions = historyVersions;
		this.processCallback = processCallback;
		this.running = false;
	}

	/**
	 * 准备工作
	 */
	private boolean prepare() {
		// 获取当前时间
		long s = System.currentTimeMillis();
		logger.info("开始环境准备");
		// 定义过程信息

		int i = 0;
		try {
			// 已经准备完成映射表
			Map<String, Item> preparedMap = new HashMap<String, Item>();

			ProcedureInfo procedureInfo = new ProcedureInfo();
			procedureInfo.setTaskid(this.id);
			procedureInfo.setAction(Action.prepare);
			// 获取环境信息
			for (i = 0; i < this.nodeInfos.length; i++) {

				Item item = new Item();
				item.nodeInfo = this.nodeInfos[i];

				// 生产ID
				String id = nodeInfos[i].getHost() + "_"
						+ nodeInfos[i].getPort();

				Item sameNodeItem = preparedMap.get(id);
				if (sameNodeItem == null) {
					// 定义网络参数
					NetArgs netArgs = new NetArgs();
					netArgs.ip = this.nodeInfos[i].getHost();
					netArgs.port = this.nodeInfos[i].getPort();
					netArgs.userName = this.nodeInfos[i].getUserName();
					netArgs.password = this.nodeInfos[i].getPassword();
					netArgs.timeout = this.timeout;
					// 获取协议
					Protocol protocol = this.nodeInfos[i].getProtocol();
					// 获取提示符
					String prompt = PromptGetter.getPrompt(protocol, netArgs);
					logger.debug("获取提示符：" + prompt);
					// 获取系统类型
					OS os = SystemGetter.getSystemType(protocol, netArgs,
							prompt);
					logger.debug("获取系统类型：" + os);
					// 获取系统语言
					String lang = LangGetter.getUsedLang(prompt, os, protocol,
							netArgs);
					logger.debug("获取系统语言：" + lang);
					item.prompt = prompt;
					item.os = os;
					item.lang = lang;

					// 加入队列
					this.items.add(item);
					// 加入已经准备注册表
					preparedMap.put(id, item);
				} else {
					item.prompt = sameNodeItem.prompt;
					item.os = sameNodeItem.os;
					item.lang = sameNodeItem.lang;
					// 加入队列
					this.items.add(item);
				}

				// 报告状态
				StringBuilder sb = new StringBuilder();
				sb.append("准备环境完成，服务器[host:");
				sb.append(this.nodeInfos[i].getHost());
				sb.append(" ,port:");
				sb.append(this.nodeInfos[i].getPort());
				sb.append(" ,name:");
				sb.append(this.nodeInfos[i].getName());
				sb.append("]");
				String msg = sb.toString();
				procedureInfo.setStatus("01");
				procedureInfo.setDetail(msg);
				// 报告状态
				this.processCallback.callback(procedureInfo);

			}
		} catch (Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append("准备环境出错，服务器[host:");
			sb.append(this.nodeInfos[i].getHost());
			sb.append(" ,port:");
			sb.append(this.nodeInfos[i].getPort());
			sb.append(" ,name:");
			sb.append(this.nodeInfos[i].getName());
			sb.append("]");
			String msg = sb.toString();
			// 打印日志
			logger.error(msg, e);

			ProcedureInfo procedureInfo = new ProcedureInfo();
			procedureInfo.setTaskid(this.id);
			procedureInfo.setAction(Action.prepare);
			procedureInfo.setStatus("02");
			procedureInfo.setDetail(msg);
			// 报告错误
			this.processCallback.callback(procedureInfo);
			return false;
		}
		logger.info("完成环境准备，耗时:" + (System.currentTimeMillis() - s) + "毫秒");

		ProcedureInfo procedureInfo = new ProcedureInfo();
		procedureInfo.setTaskid(this.id);
		procedureInfo.setAction(Action.finishPrepare);
		procedureInfo.setStatus("01");
		procedureInfo.setDetail("完成环境准备");
		// 报告错误
		this.processCallback.callback(procedureInfo);
		return true;
	}

	/**
	 * 启动服务器
	 */
	private boolean startup() {
		// 获取当前时间
		long s = System.currentTimeMillis();
		logger.info("开始启动服务器");

		// 定义过程信息
		ProcedureInfo procedureInfo = new ProcedureInfo();
		procedureInfo.setTaskid(this.id);
		procedureInfo.setAction(Action.startup);
		int i = 0;
		int size = this.items.size();
		try {
			// 获取环境信息
			for (; i < size; i++) {
				// 获取更新节点信息
				Item item = this.items.get(i);

				// 获取协议
				Protocol protocol = item.nodeInfo.getProtocol();
				// 获取解压命令
				String script = ShellScriptManager.getScript(item.os.getName(),
						"startup.sh", item.nodeInfo.getAppDir());
				String command = script;
				command += "exit\n";

				// 定义网络参数
				NetArgs netArgs = new NetArgs();
				netArgs.ip = item.nodeInfo.getHost();
				netArgs.port = item.nodeInfo.getPort();
				netArgs.userName = item.nodeInfo.getUserName();
				netArgs.password = item.nodeInfo.getPassword();
				netArgs.timeout = this.timeout;
				// 获取连接器
				IConnector connector = ConnectorFactory.getConnector(protocol,
						netArgs);
				try {
					// 获取编码格式
					String encoding = this.getEncoding(item.lang);
					// 连接服务器
					connector.connect();
					// 执行命令
					ShellExcutor.execute(command, encoding, connector,
							this.timeout * 3);
				} finally {
					connector.disconnect();
				}

				StringBuilder sb = new StringBuilder();
				sb.append("启动服务器成功，服务器[host:");
				sb.append(item.nodeInfo.getHost());
				sb.append(" ,port:");
				sb.append(item.nodeInfo.getPort());
				sb.append(" ,name:");
				sb.append(item.nodeInfo.getName());
				sb.append("]");
				String msg = sb.toString();
				procedureInfo.setStatus("01");
				procedureInfo.setDetail(msg);
				// 报告状态
				this.processCallback.callback(procedureInfo);
			}
		} catch (Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append("启动服务器失败，服务器[host:");
			sb.append(nodeInfos[i].getHost());
			sb.append(" ,port:");
			sb.append(nodeInfos[i].getPort());
			sb.append(" ,name:");
			sb.append(nodeInfos[i].getName());
			sb.append("]");
			String msg = sb.toString();
			logger.error(msg, e);
			procedureInfo.setStatus("02");
			procedureInfo.setDetail(msg);
			// 报告错误
			this.processCallback.callback(procedureInfo);
			return false;
		}
		logger.info("完成启动服务器，耗时:" + (System.currentTimeMillis() - s) + "毫秒");
		return true;
	}

	/**
	 * 关闭服务器
	 */
	private boolean shutdown() {
		// 获取当前时间
		long s = System.currentTimeMillis();
		logger.info("开始关闭服务器");

		// 定义过程信息
		ProcedureInfo procedureInfo = new ProcedureInfo();
		procedureInfo.setTaskid(this.id);
		procedureInfo.setAction(Action.shutdown);
		int i = 0;
		int size = this.items.size();
		try {
			// 获取环境信息
			for (; i < size; i++) {
				// 获取更新节点信息
				Item item = this.items.get(i);

				// 获取协议
				Protocol protocol = item.nodeInfo.getProtocol();
				// 获取关闭命令
				String script = ShellScriptManager.getScript(item.os.getName(),
						"shutdown.sh", item.nodeInfo.getAppDir());
				String command = script;
				command += "exit\n";

				// 定义网络参数
				NetArgs netArgs = new NetArgs();
				netArgs.ip = item.nodeInfo.getHost();
				netArgs.port = item.nodeInfo.getPort();
				netArgs.userName = item.nodeInfo.getUserName();
				netArgs.password = item.nodeInfo.getPassword();
				netArgs.timeout = this.timeout;
				// 获取连接器
				IConnector connector = ConnectorFactory.getConnector(protocol,
						netArgs);
				try {
					// 获取编码格式
					String encoding = this.getEncoding(item.lang);
					// 连接服务器
					connector.connect();
					// 执行命令
					ShellExcutor.execute(command, encoding, connector,
							this.timeout * 3);
				} finally {
					connector.disconnect();
				}

				StringBuilder sb = new StringBuilder();
				sb.append("关闭服务器成功，服务器[host:");
				sb.append(item.nodeInfo.getHost());
				sb.append(" ,port:");
				sb.append(item.nodeInfo.getPort());
				sb.append(" ,name:");
				sb.append(item.nodeInfo.getName());
				sb.append("]");
				String msg = sb.toString();
				procedureInfo.setStatus("01");
				procedureInfo.setDetail(msg);
				// 报告状态
				this.processCallback.callback(procedureInfo);
			}
		} catch (Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append("关闭服务器失败，服务器[host:");
			sb.append(nodeInfos[i].getHost());
			sb.append(" ,port:");
			sb.append(nodeInfos[i].getPort());
			sb.append(" ,name:");
			sb.append(nodeInfos[i].getName());
			sb.append("]");
			String msg = sb.toString();
			logger.error(msg, e);
			procedureInfo.setStatus("02");
			procedureInfo.setDetail(msg);
			// 报告错误
			this.processCallback.callback(procedureInfo);
			return false;
		}
		logger.info("完成关闭服务器，耗时:" + (System.currentTimeMillis() - s) + "毫秒");
		return true;
	}

	/**
	 * 检查服务器状态
	 * 
	 * @return
	 */
	private ServerState checkServerState(Item item) {
		// 获取当前时间
		long s = System.currentTimeMillis();
		logger.info("判断是否启动");

		// 定义过程信息
		ProcedureInfo procedureInfo = new ProcedureInfo();
		procedureInfo.setTaskid(this.id);
		procedureInfo.setAction(Action.checkServerState);
		try {
			// 获取协议
			Protocol protocol = item.nodeInfo.getProtocol();
			// 获取关闭命令
			String script = ShellScriptManager.getScript(item.os.getName(),
					"checkServerState.sh",
					String.valueOf(item.nodeInfo.getAppDir()));
			String command = script;
			command += "exit\n";

			// 定义网络参数
			NetArgs netArgs = new NetArgs();
			netArgs.ip = item.nodeInfo.getHost();
			netArgs.port = item.nodeInfo.getPort();
			netArgs.userName = item.nodeInfo.getUserName();
			netArgs.password = item.nodeInfo.getPassword();
			netArgs.timeout = this.timeout;
			// 获取连接器
			IConnector connector = ConnectorFactory.getConnector(protocol,
					netArgs);
			try {
				// 获取编码格式
				String encoding = this.getEncoding(item.lang);
				// 连接服务器
				connector.connect();
				// 执行命令
				String data = ShellExcutor.execute(command, encoding,
						connector, this.timeout * 3);
				String res = ResultExtractor.extract(data, item.prompt, "exit");
				if (containsKey(res, "open")) {
					StringBuilder sb = new StringBuilder();
					sb.append("检查服务器状态成功，服务器处于启动状态[host:");
					sb.append(item.nodeInfo.getHost());
					sb.append(" ,port:");
					sb.append(item.nodeInfo.getPort());
					sb.append(" ,name:");
					sb.append(item.nodeInfo.getName());
					sb.append("]");
					String msg = sb.toString();
					procedureInfo.setStatus("01");
					procedureInfo.setDetail(msg);
					// 报告
					this.processCallback.callback(procedureInfo);
					return ServerState.OPEN;
				} else if (containsKey(res, "close")) {
					StringBuilder sb = new StringBuilder();
					sb.append("检查服务器状态成功，服务器处于关闭状态[host:");
					sb.append(item.nodeInfo.getHost());
					sb.append(" ,port:");
					sb.append(item.nodeInfo.getPort());
					sb.append(" ,name:");
					sb.append(item.nodeInfo.getName());
					sb.append("]");
					String msg = sb.toString();
					procedureInfo.setStatus("01");
					procedureInfo.setDetail(msg);
					// 报告
					this.processCallback.callback(procedureInfo);
					return ServerState.CLOSE;
				}
			} finally {
				connector.disconnect();
			}
		} catch (Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append("检查服务器状态错误，服务器[host:");
			sb.append(item.nodeInfo.getHost());
			sb.append(" ,port:");
			sb.append(item.nodeInfo.getPort());
			sb.append(" ,name:");
			sb.append(item.nodeInfo.getName());
			sb.append("]");
			String msg = sb.toString();
			logger.error(msg, e);
			procedureInfo.setStatus("02");
			procedureInfo.setDetail(msg);
			// 报告错误
			this.processCallback.callback(procedureInfo);
			return ServerState.UNKNOWN;
		}
		logger.info("完成服务器状态检查，耗时:" + (System.currentTimeMillis() - s) + "毫秒");
		procedureInfo.setStatus("02");
		procedureInfo.setDetail("检查服务器状态出错");
		// 报告
		this.processCallback.callback(procedureInfo);
		return ServerState.UNKNOWN;
	}

	/**
	 * 判断服务器是否已经关闭
	 * 
	 * @return
	 */
	private boolean isShutdown() {
		// 待检查列表
		List<Item> checkItems = new ArrayList<Item>();
		checkItems.addAll(this.items);

		try {
			// 检查6次，每次间隔3秒，如果第六还有没关闭的服务器，那么试图杀进程
			int time = 0;
			do {
				time++;
				for (int i = 0; i < checkItems.size();) {
					Item item = checkItems.get(i);
					// 检查服务器状态
					ServerState state = this.checkServerState(item);
					// 如果已经关闭，从列表中移除
					if (state == ServerState.CLOSE) {
						checkItems.remove(i);
						continue;
					}
					i++;
				}

				// 如果所有服务器已经关闭，返回true
				if (checkItems.size() == 0) {
					// 定义过程信息
					ProcedureInfo procedureInfo = new ProcedureInfo();
					procedureInfo.setTaskid(this.id);
					procedureInfo.setAction(Action.finishShutdown);
					procedureInfo.setDetail("BIPS关闭成功");
					procedureInfo.setStatus("01");
					this.processCallback.callback(procedureInfo);
					return true;
				}

				if (time == 6) {
					for (int j = 0; j < checkItems.size(); j++) {
						Item item = checkItems.get(j);
						this.kill(item);
					}
				}

				// 达到最大尝试次数，退出检查
				if (time == 7) {
					break;
				}

				// 等待3秒后再检查
				try {
					Thread.sleep(3000);
				} catch (Exception e) {
					// do nothing
				}
			} while (true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		for (int i = 0, size = checkItems.size(); i < size; i++) {
			Item item = checkItems.get(i);
			StringBuilder sb = new StringBuilder();
			sb.append("关闭BIPS失败，服务器[host:");
			sb.append(item.nodeInfo.getHost());
			sb.append(" ,port:");
			sb.append(item.nodeInfo.getPort());
			sb.append(" ,name:");
			sb.append(item.nodeInfo.getName());
			sb.append("]");
			String msg = sb.toString();

			// 定义过程信息
			ProcedureInfo procedureInfo = new ProcedureInfo();
			procedureInfo.setTaskid(this.id);
			procedureInfo.setAction(Action.shutdown);
			procedureInfo.setStatus("02");
			procedureInfo.setDetail(msg.toString());
			this.processCallback.callback(procedureInfo);
		}
		return false;
	}

	/**
	 * 杀进程
	 */
	private boolean kill(Item item) {
		// 定义过程信息
		ProcedureInfo procedureInfo = new ProcedureInfo();
		procedureInfo.setTaskid(this.id);
		procedureInfo.setAction(Action.kill);

		try {
			// 获取协议
			Protocol protocol = item.nodeInfo.getProtocol();

			// 获取杀进程命令
			String script = ShellScriptManager.getScript(item.os.getName(),
					"kill.sh", String.valueOf(item.nodeInfo.getJvmPort()));
			String command = script;
			command += "exit\n";

			// 定义网络参数
			NetArgs netArgs = new NetArgs();
			netArgs.ip = item.nodeInfo.getHost();
			netArgs.port = item.nodeInfo.getPort();
			netArgs.userName = item.nodeInfo.getUserName();
			netArgs.password = item.nodeInfo.getPassword();
			netArgs.timeout = this.timeout;
			// 获取连接器
			IConnector connector = ConnectorFactory.getConnector(protocol,
					netArgs);
			try {
				// 获取编码格式
				String encoding = this.getEncoding(item.lang);
				// 连接服务器
				connector.connect();
				// 执行命令
				String data = ShellExcutor.execute(command, encoding,
						connector, this.timeout * 3);
				String res = ResultExtractor.extract(data, item.prompt, "exit");
				if (containsKey(res, "success")) {
					StringBuilder sb = new StringBuilder();
					sb.append("杀进程成功，服务器[host:");
					sb.append(item.nodeInfo.getHost());
					sb.append(" ,port:");
					sb.append(item.nodeInfo.getPort());
					sb.append(" ,name:");
					sb.append(item.nodeInfo.getName());
					sb.append("]");
					String msg = sb.toString();
					procedureInfo.setStatus("01");
					procedureInfo.setDetail(msg);
					// 报告
					this.processCallback.callback(procedureInfo);
					return true;
				} else {
					StringBuilder sb = new StringBuilder();
					sb.append("杀进程失败，服务器[host:");
					sb.append(item.nodeInfo.getHost());
					sb.append(" ,port:");
					sb.append(item.nodeInfo.getPort());
					sb.append(" ,name:");
					sb.append(item.nodeInfo.getName());
					sb.append("]");
					String msg = sb.toString();
					procedureInfo.setStatus("02");
					procedureInfo.setDetail(msg);
					// 报告
					this.processCallback.callback(procedureInfo);
					return false;
				}
			} finally {
				connector.disconnect();
			}
		} catch (Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append("杀进程出错，服务器[host:");
			sb.append(item.nodeInfo.getHost());
			sb.append(" ,port:");
			sb.append(item.nodeInfo.getPort());
			sb.append(" ,name:");
			sb.append(item.nodeInfo.getName());
			sb.append("]");
			String msg = sb.toString();
			logger.error(msg, e);
			procedureInfo.setStatus("02");
			procedureInfo.setDetail(msg);
			// 报告错误
			this.processCallback.callback(procedureInfo);
			return false;
		}
	}

	/**
	 * 是否已经启动
	 * 
	 * @return
	 */
	private boolean isStartup() {
		// 待检查列表
		List<Item> checkItems = new ArrayList<Item>();
		checkItems.addAll(this.items);

		try {
			// 重复确认2次
			int time = 0;
			while (time < 2) {
				// 等待5秒后再检查
				try {
					Thread.sleep(5000);
				} catch (Exception e) {
					// do nothing
				}

				for (int i = 0; i < checkItems.size();) {
					Item item = checkItems.get(i);
					// 检查服务器状态
					ServerState state = this.checkServerState(item);
					// 如果已经关闭，从列表中移除
					if (state == ServerState.OPEN && time == 1) {
						checkItems.remove(i);
						continue;
					}
					i++;
				}
				time++;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		if (checkItems.size() == 0) {
			// 定义过程信息
			ProcedureInfo procedureInfo = new ProcedureInfo();
			procedureInfo.setTaskid(this.id);
			procedureInfo.setAction(Action.finishStartup);
			procedureInfo.setDetail("启动BIPS成功");
			procedureInfo.setStatus("01");
			this.processCallback.callback(procedureInfo);
			return true;
		} else {
			for (int i = 0, size = checkItems.size(); i < size; i++) {
				Item item = checkItems.get(i);
				StringBuilder sb = new StringBuilder();
				sb.append("启动BIPS失败，服务器[host:");
				sb.append(item.nodeInfo.getHost());
				sb.append(" ,port:");
				sb.append(item.nodeInfo.getPort());
				sb.append(" ,name:");
				sb.append(item.nodeInfo.getName());
				sb.append("]");
				String msg = sb.toString();

				// 定义过程信息
				ProcedureInfo procedureInfo = new ProcedureInfo();
				procedureInfo.setTaskid(this.id);
				procedureInfo.setAction(Action.startup);
				procedureInfo.setStatus("02");
				procedureInfo.setDetail(msg.toString());
				this.processCallback.callback(procedureInfo);
			}
			return false;
		}
	}

	/**
	 * 校验是否回退成功
	 */
	private boolean check(String name) {
		// 获取当前时间
		long s = System.currentTimeMillis();
		logger.info("开始校验回退是否成功");

		// 定义过程信息
		ProcedureInfo procedureInfo = new ProcedureInfo();
		procedureInfo.setTaskid(this.id);
		procedureInfo.setAction(Action.check);
		int i = 0;
		int size = this.items.size();
		try {
			// 已更新目录集合
			Map<String, Set<String>> updatedDirs = new HashMap<String, Set<String>>();
			// 获取环境信息
			for (; i < size; i++) {
				// 获取更新节点信息
				Item item = this.items.get(i);
				// 定义网络参数
				NetArgs netArgs = new NetArgs();
				netArgs.ip = item.nodeInfo.getHost();
				netArgs.port = item.nodeInfo.getPort();
				netArgs.userName = item.nodeInfo.getUserName();
				netArgs.password = item.nodeInfo.getPassword();
				netArgs.timeout = this.timeout;

				// 生成key
				String key = netArgs.ip + ":" + netArgs.port;
				// 获取已经回退集合
				Set<String> set = updatedDirs.get(key);
				if (set == null) {
					set = new HashSet<String>();
				}

				// 获取更新目录
				String updateDir = item.nodeInfo.getUpateDir();
				// 判断如果已经回退，那么不在进行回退
				if (set.contains(updateDir)) {
					continue;
				} else {
					// 加入列表
					set.add(updateDir);
				}
				updatedDirs.put(key, set);

				// 获取协议
				Protocol protocol = this.nodeInfos[i].getProtocol();

				// 获取检查命令
				String script = ShellScriptManager.getScript(item.os.getName(),
						"checkRollback.sh", item.nodeInfo.getWorkDir(), name,
						updateDir);
				String command = script;
				command += "exit\n";

				// 获取连接器
				IConnector connector = ConnectorFactory.getConnector(protocol,
						netArgs);
				try {
					// 获取编码格式
					String encoding = this.getEncoding(item.lang);
					// 连接服务器
					connector.connect();
					// 执行命令
					String data = ShellExcutor.execute(command, encoding,
							connector, this.timeout * 3);
					String res = ResultExtractor.extract(data, item.prompt,
							"exit");
					if (containsKey(res, "success")) {
						StringBuilder sb=new StringBuilder();
						sb.append("校验成功，服务器[host:");
						sb.append(item.nodeInfo.getHost());
						sb.append(" ,port:");
						sb.append(item.nodeInfo.getPort());
						sb.append(" ,name:");
						sb.append(item.nodeInfo.getName());
						sb.append("]");
						String msg=sb.toString();
						procedureInfo.setStatus("01");
						procedureInfo.setDetail(msg);
						// 报告
						this.processCallback.callback(procedureInfo);
						continue;
					} else {
						StringBuilder sb=new StringBuilder();
						sb.append("校验失败，服务器[host:");
						sb.append(item.nodeInfo.getHost());
						sb.append(" ,port:");
						sb.append(item.nodeInfo.getPort());
						sb.append(" ,name:");
						sb.append(item.nodeInfo.getName());
						sb.append("]");
						String msg=sb.toString();
						procedureInfo.setStatus("02");
						procedureInfo.setDetail(msg);
						// 报告错误
						this.processCallback.callback(procedureInfo);
						logger.error(msg + "\n" + res);
						return false;
					}
				} finally {
					connector.disconnect();
				}
			}
		} catch (Exception e) {
			StringBuilder sb=new StringBuilder();
			sb.append("校验出错，服务器[host:");
			sb.append(items.get(i).nodeInfo.getHost());
			sb.append(" ,port:");
			sb.append(items.get(i).nodeInfo.getPort());
			sb.append(" ,name:");
			sb.append(items.get(i).nodeInfo.getName());
			sb.append("]");
			String msg=sb.toString();
			logger.error(msg, e);
			procedureInfo.setStatus("02");
			procedureInfo.setDetail(msg);
			// 报告错误
			this.processCallback.callback(procedureInfo);
			return false;
		}
		
		procedureInfo.setAction(Action.finishCheck);
		procedureInfo.setStatus("01");
		procedureInfo.setDetail("校验完成");
		// 报告
		this.processCallback.callback(procedureInfo);

		logger.info("校验回退完成，耗时:" + (System.currentTimeMillis() - s) + "毫秒");
		return true;
	}

	/**
	 * 回退指定版本
	 * 
	 * @param name
	 * @return
	 */
	private boolean rollback(String name) {
		// 获取当前时间
		long s = System.currentTimeMillis();
		logger.info("开始回退 rollbackFile:" + name);

		ProcedureInfo procedureInfo = new ProcedureInfo();
		procedureInfo.setTaskid(this.id);
		procedureInfo.setAction(Action.rollback);
		
		int i = 0;
		int size = this.items.size();
		try {
			// 已更新目录集合
			Map<String, Set<String>> updatedDirs = new HashMap<String, Set<String>>();
			// 获取环境信息
			for (; i < size; i++) {
				// 获取更新节点信息
				Item item = this.items.get(i);

				// 定义网络参数
				NetArgs netArgs = new NetArgs();
				netArgs.ip = item.nodeInfo.getHost();
				netArgs.port = item.nodeInfo.getPort();
				netArgs.userName = item.nodeInfo.getUserName();
				netArgs.password = item.nodeInfo.getPassword();
				netArgs.timeout = this.timeout;

				// 生成key
				String key = netArgs.ip + ":" + netArgs.port;
				// 获取已经更新集合
				Set<String> set = updatedDirs.get(key);
				if (set == null) {
					set = new HashSet<String>();
				}

				// 获取更新目录
				String updateDir = item.nodeInfo.getUpateDir();
				// 判断如果已经更新，那么不在进行更新
				if (set.contains(updateDir)) {
					continue;
				} else {
					// 加入列表
					set.add(updateDir);
				}
				updatedDirs.put(key, set);

				// 获取协议
				Protocol protocol = item.nodeInfo.getProtocol();
				// 获取关闭命令
				String script = ShellScriptManager.getScript(item.os.getName(),
						"rollback.sh", item.nodeInfo.getWorkDir(), name,
						updateDir);
				String command = script;
				command += "exit\n";

				// 获取连接器
				IConnector connector = ConnectorFactory.getConnector(protocol,
						netArgs);
				try {
					// 获取编码格式(强制设置为UIF-8)
					String encoding = this.getEncoding(item.lang);
					// 连接服务器
					connector.connect();
					// 执行命令
					String data = ShellExcutor.execute(command, encoding,
							connector, this.timeout * 3);
					String res = ResultExtractor.extract(data, item.prompt,
							"exit");
					String[] arrays = res.split("\n");
					// 判断是否成功
					boolean finish = containsKey(arrays[0], "success")
							|| containsKey(arrays[arrays.length - 1], "success");
					if (!finish) {
						StringBuilder sb=new StringBuilder();
						sb.append("回退失败，服务器[host:");
						sb.append(item.nodeInfo.getHost());
						sb.append(" ,port:");
						sb.append(item.nodeInfo.getPort());
						sb.append(" ,udpteDir:");
						sb.append(item.nodeInfo.getUpateDir());
						sb.append(" ,rollbackFile:");
						sb.append(name);
						sb.append("]");
						String msg=sb.toString();
						procedureInfo.setStatus("02");
						procedureInfo.setDetail(msg);
						// 报告
						this.processCallback.callback(procedureInfo);
						return false;
					}else{
						StringBuilder sb=new StringBuilder();
						sb.append("回退成功，服务器[host:");
						sb.append(item.nodeInfo.getHost());
						sb.append(" ,port:");
						sb.append(item.nodeInfo.getPort());
						sb.append(" ,udpteDir:");
						sb.append(item.nodeInfo.getUpateDir());
						sb.append(" ,rollbackFile:");
						sb.append(name);
						sb.append("]");
						String msg=sb.toString();
						procedureInfo.setStatus("01");
						procedureInfo.setDetail(msg);
						// 报告
						this.processCallback.callback(procedureInfo);
					}
				} finally {
					connector.disconnect();
				}
			}
		} catch (Exception e) {
			StringBuilder sb=new StringBuilder();
			sb.append("回退出错，服务器[host:");
			sb.append(items.get(i).nodeInfo.getHost());
			sb.append(" ,port:");
			sb.append(items.get(i).nodeInfo.getPort());
			sb.append(" ,udpteDir:");
			sb.append(items.get(i).nodeInfo.getUpateDir());
			sb.append(" ,rollbackFile:");
			sb.append(name);
			sb.append("]");
			String msg=sb.toString();
			//打印日志
			logger.error(msg, e);
			
			procedureInfo.setStatus("02");
			procedureInfo.setDetail(msg);
			// 报告错误
			this.processCallback.callback(procedureInfo);
			return false;
		}
		logger.info("完成回退，耗时:" + (System.currentTimeMillis() - s) + "毫秒");
		return true;
	}

	/**
	 * 回退
	 */
	private boolean rollback() {
		// 获取当前时间
		long s = System.currentTimeMillis();
		logger.info("开始回退");

		// 定义过程信息
		ProcedureInfo procedureInfo = new ProcedureInfo();
		procedureInfo.setTaskid(this.id);
		procedureInfo.setAction(Action.finishRollback);

		try {
			// 获取回退位置
			int index = this.historyVersions.indexOf(this.rollbackVersion);
			for (int i = 0; i <= index; i++) {
				String name = this.historyVersions.get(i);
				// 回退版本
				boolean res = this.rollback(name);
				if (res) {
					res = this.check(name);
				}

				if (!res) {
					logger.info("回退失败，耗时:" + (System.currentTimeMillis() - s)
							+ "毫秒");
					procedureInfo.setAction(Action.rollback);
					procedureInfo.setStatus("02");
					procedureInfo.setDetail("回退失败");
					// 报告
					this.processCallback.callback(procedureInfo);
					return false;
				}

			}

		} catch (Exception e) {
			String msg = "回退失败," + e.getMessage();
			logger.error(e.getMessage(), e);
			procedureInfo.setAction(Action.rollback);
			procedureInfo.setStatus("02");
			procedureInfo.setDetail(msg);
			// 报告错误
			this.processCallback.callback(procedureInfo);
			return false;
		}
		logger.info("完成回退，耗时:" + (System.currentTimeMillis() - s) + "毫秒");
		procedureInfo.setStatus("01");
		procedureInfo.setAction(Action.finishRollback);
		procedureInfo.setDetail("完成回退");
		// 报告
		this.processCallback.callback(procedureInfo);
		return true;
	}

	/**
	 * 执行
	 */
	private void runTask() {
		// 记录执行结果
		boolean res = true;
		try {
			while (res) {
				step++;
				// 根据index，执行对应的步骤
				switch (step) {
				case 1:
					// 环境准备
					res = this.prepare();
					break;
				case 2:
					// 关闭服务器
					res = this.shutdown();
					break;
				case 3:
					// 判断是否关闭服务器
					res = this.isShutdown();
					break;
				case 4:
					// 回退
					res = this.rollback();
					break;
				case 5:
					// 启动服务器
					res = this.startup();
					break;
				case 6:
					// 判断是否启动服务器
					res = this.isStartup();
					break;
				default:
					return;
				}
			}
		} finally {
			// 定义过程信息
			ProcedureInfo procedureInfo = new ProcedureInfo();
			procedureInfo.setTaskid(this.id);
			procedureInfo.setAction(Action.finish);
			procedureInfo.setDetail("回退任务结束");

			// 执行失败，回退步骤
			if (!res) {
				this.step -= 1;
				procedureInfo.setStatus("02");
			} else {
				procedureInfo.setStatus("01");
			}
			// 报告状态
			this.processCallback.callback(procedureInfo);
		}

	}

	/**
	 * 开始任务
	 */
	public void startTask() {
		// 如果已经执行，不再执行
		if (running) {
			return;
		}
		this.step = 0;
		// 定义执行线程
		Thread thread = new Thread() {

			/**
			 * 执行
			 */
			public void run() {
				// 如果已经执行，不再执行
				if (running) {
					return;
				}
				running = true;
				try {
					// 执行任务
					runTask();
				} finally {
					running = false;
				}
			}
		};
		thread.start();
	}

	/**
	 * 继续任务
	 */
	public void continueTask() {
		// 如果已经执行，不再执行
		if (running) {
			return;
		}
		// 定义执行线程
		Thread thread = new Thread() {

			/**
			 * 执行
			 */
			public void run() {
				// 如果已经执行，不再执行
				if (running) {
					return;
				}
				running = true;
				try {
					// 执行任务
					runTask();
				} finally {
					running = false;
				}
			}
		};
		thread.start();
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
		if (lang.indexOf("UTF-8") != -1) {
			return "UTF-8";
		} else {
			return "GBK";
		}
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
	 * 更新节点
	 * 
	 */
	private class Item {

		/**
		 * 节点信息
		 */
		public NodeInfo nodeInfo;

		/**
		 * 提示符
		 */
		public String prompt;

		/**
		 * 系统语言
		 */
		public String lang;

		/**
		 * 系统类型
		 */
		public OS os;
	}

	/**
	 * 服务器状态
	 * 
	 * @author 江成
	 * 
	 */
	private enum ServerState {
		/**
		 * 打开状态
		 */
		OPEN,

		/**
		 * 关闭状态
		 */
		CLOSE,

		/**
		 * 未知状态
		 */
		UNKNOWN
	}

}
