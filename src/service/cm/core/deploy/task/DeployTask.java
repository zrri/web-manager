package service.cm.core.deploy.task;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import both.common.ftp.FtpFactory;
import both.common.ftp.IFtpClient;
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
 * 部署任务
 * 
 * @author 江成
 * 
 */
public class DeployTask {

	/**
	 * 日志
	 */
	private static Logger logger = LoggerFactory.getLogger(DeployTask.class);

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
	 * 版本文件
	 */
	private File versionFile;

	/**
	 * 更新文件名
	 */
	private String updateFileName = "bip_update.tar";

	/**
	 * 是否需要重启
	 */
	private boolean needRestart;

	/**
	 * 回调函数
	 */
	private IProcessCallback processCallback;

	/**
	 * 更新节点信息
	 */
	private List<Item> items = new ArrayList<Item>();

	/**
	 * 备份文件
	 */
	private String backFileName;

	/**
	 * 日期格式化
	 */
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd-HH-mm-ss");

	/**
	 * 记录步骤
	 */
	private int step;

	/**
	 * 标志是否执行中
	 */
	private boolean running = false;

	/**
	 * 构造函数
	 * 
	 * @param nodeInfos
	 * @param versionFile
	 * @param needRestart
	 * @param processCallBack
	 */
	public DeployTask(String id, NodeInfo[] nodeInfos, File versionFile,
			boolean needRestart, IProcessCallback processCallback) {
		this.id = id;
		this.nodeInfos = nodeInfos;
		this.versionFile = versionFile;
		this.needRestart = needRestart;
		this.processCallback = processCallback;

		String name = versionFile.getName();
		if (name.endsWith(".jar")) {
			this.updateFileName = "bip_update.jar";
			this.backFileName = "backup_" + dateFormat.format(new Date())
					+ ".jar";
		} else {
			this.updateFileName = "bip_update.tar";
			this.backFileName = "backup_" + dateFormat.format(new Date())
					+ ".tar";
		}

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
				}else{
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
	 * 传送文件
	 */
	private boolean transmit() {
		// 记录当前时间
		long s = System.currentTimeMillis();
		logger.info("开始上传文件");

		// 定义过程信息
		ProcedureInfo procedureInfo = new ProcedureInfo();
		procedureInfo.setTaskid(this.id);
		procedureInfo.setAction(Action.transmit);

		int i = 0;
		int size = this.items.size();
		try {
			// 已经传输完成映射表
			Map<String, Item> transmitedMap = new HashMap<String, Item>();
			
			// 传送文件
			for (; i < size; i++) {
				// 获取部署服务器信息
				Item item = this.items.get(i);
				
				//生产ID
				String id=item.nodeInfo.getHost()+"_"+item.nodeInfo.getPort()+"_"+item.nodeInfo
						.getWorkDir();
				if(transmitedMap.containsKey(id)){
					continue;
				}
				
				// 获取FTP客户端
				IFtpClient ftpClient = FtpFactory.getFtpClient(item.nodeInfo
						.getTransmitProtocol());
				try {
					// 连接FTP服务器
					boolean success = ftpClient.connect(
							item.nodeInfo.getHost(),
							item.nodeInfo.getTransmitPort(),
							item.nodeInfo.getTransmitUserName(),
							item.nodeInfo.getTransmitPassword(), this.timeout);
					if (!success) {
						String msg = "无法连接服务器[host:" + item.nodeInfo.getHost()
								+ " ,port:" + item.nodeInfo.getTransmitPort()
								+ "]";
						logger.error(msg);
						procedureInfo.setStatus("02");
						procedureInfo.setDetail(msg);
						// 报告状态
						this.processCallback.callback(procedureInfo);
						break;
					}
					// 改变工作目录
					success = ftpClient.changeWorkingDirectory(item.nodeInfo
							.getWorkDir());
					if (!success) {
						String msg = "改变工作目录失败[host:" + item.nodeInfo.getHost()
								+ " ,port:" + item.nodeInfo.getPort()
								+ " ,workDir:" + item.nodeInfo.getWorkDir()
								+ "]";
						logger.error(msg);
						procedureInfo.setStatus("02");
						procedureInfo.setDetail(msg);
						// 报告状态
						this.processCallback.callback(procedureInfo);
						break;
					}
					// 上传文件
					BufferedInputStream in = null;
					try {
						// 获取名称
						String name = this.versionFile.getName();
						// 读取文件流
						in = new BufferedInputStream(new FileInputStream(
								this.versionFile));
						success = ftpClient.uploadFile(name, in);
						if (!success) {
							String msg = "上传失败[host:" + item.nodeInfo.getHost()
									+ " ,port:" + item.nodeInfo.getPort()
									+ " ,workDir:" + item.nodeInfo.getWorkDir()
									+ "]";
							logger.error(msg);
							procedureInfo.setStatus("02");
							procedureInfo.setDetail(msg);
							// 报告状态
							this.processCallback.callback(procedureInfo);
							break;
						}
					} finally {
						if (in != null) {
							in.close();
						}
					}
				} finally {
					// 断开FTP客户端
					if (ftpClient != null) {
						ftpClient.disconnect();
					}
				}

				//加入已经传输列表
				transmitedMap.put(id, item);
				
				StringBuilder sb = new StringBuilder();
				sb.append("上传成功，服务器[host:");
				sb.append(item.nodeInfo.getHost());
				sb.append(" ,port:");
				sb.append(item.nodeInfo.getPort());
				sb.append(" ,workDir:");
				sb.append(item.nodeInfo.getWorkDir());
				sb.append("]");
				String msg = sb.toString();
				procedureInfo.setStatus("01");
				procedureInfo.setDetail(msg);
				// 报告状态
				this.processCallback.callback(procedureInfo);

			}

			if (i == size) {
				procedureInfo.setStatus("01");
				procedureInfo.setAction(Action.finishTransmit);
				procedureInfo.setDetail("完成上传文件");
				// 报告
				this.processCallback.callback(procedureInfo);
				logger.info("完成上传文件，耗时:" + (System.currentTimeMillis() - s)
						+ "毫秒");
				return true;
			}
			return false;
		} catch (Exception e) {
			String msg = "上传文件出错，服务器[host:"
					+ this.items.get(i).nodeInfo.getHost() + " ,port:"
					+ this.items.get(i).nodeInfo.getTransmitPort() + "]";
			logger.error(msg, e);
			procedureInfo.setStatus("02");
			procedureInfo.setDetail(msg);
			// 报告错误
			this.processCallback.callback(procedureInfo);
			return false;
		}

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
	 * 解压文件
	 */
	private boolean uncompress() {
		// 获取当前时间
		long s = System.currentTimeMillis();
		logger.info("开始解压文件");

		// 定义过程信息
		ProcedureInfo procedureInfo = new ProcedureInfo();
		procedureInfo.setTaskid(this.id);
		procedureInfo.setAction(Action.uncompress);
		int i = 0;
		int size = this.items.size();
		try {
			// 已经解压完成映射表
			Map<String, Item> uncompressedMap = new HashMap<String, Item>();
			
			// 获取环境信息
			for (; i < size; i++) {
				// 获取更新节点信息
				Item item = this.items.get(i);
				
				//生产ID
				String id=item.nodeInfo.getHost()+"_"+item.nodeInfo.getPort()+"_"+item.nodeInfo
						.getWorkDir();
				if(uncompressedMap.containsKey(id)){
					continue;
				}

				// 获取协议
				Protocol protocol = item.nodeInfo.getProtocol();
				// 获取解压命令
				String script = ShellScriptManager.getScript(item.os.getName(),
						"unpackage.sh", item.nodeInfo.getWorkDir(),
						this.versionFile.getName());
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
				
				//加入已经解压完成列表
				uncompressedMap.put(id, item);

				StringBuilder sb = new StringBuilder();
				sb.append("解压文件成功，服务器[host:");
				sb.append(item.nodeInfo.getHost());
				sb.append(" ,port:");
				sb.append(item.nodeInfo.getPort());
				sb.append(" ,workDir:");
				sb.append(item.nodeInfo.getWorkDir());
				sb.append("]");
				String msg = sb.toString();
				procedureInfo.setStatus("01");
				procedureInfo.setDetail(msg);
				// 报告状态
				this.processCallback.callback(procedureInfo);

			}
		} catch (Exception e) {
			String msg = "解压文件出错，服务器[host:"
					+ this.items.get(i).nodeInfo.getHost() + " ,port:"
					+ this.items.get(i).nodeInfo.getPort() + "]";
			logger.error(msg, e);
			procedureInfo.setStatus("02");
			procedureInfo.setDetail(msg);
			// 报告错误
			this.processCallback.callback(procedureInfo);
			return false;
		}
		logger.info("完成解压文件，耗时:" + (System.currentTimeMillis() - s) + "毫秒");
		procedureInfo.setStatus("01");
		procedureInfo.setAction(Action.finishUncompress);
		procedureInfo.setDetail("完成解压文件");
		// 报告
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
					StringBuilder sb=new StringBuilder();
					sb.append("检查服务器状态成功，服务器处于启动状态[host:");
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
					return ServerState.OPEN;
				} else if (containsKey(res, "close")) {
					StringBuilder sb=new StringBuilder();
					sb.append("检查服务器状态成功，服务器处于关闭状态[host:");
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
					return ServerState.CLOSE;
				}
			} finally {
				connector.disconnect();
			}
		} catch (Exception e) {
			StringBuilder sb=new StringBuilder();
			sb.append("检查服务器状态错误，服务器[host:");
			sb.append(item.nodeInfo.getHost());
			sb.append(" ,port:");
			sb.append(item.nodeInfo.getPort());
			sb.append(" ,name:");
			sb.append(item.nodeInfo.getName());
			sb.append("]");
			String msg=sb.toString();
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
	 * 备份文件
	 */
	private boolean backup() {
		// 获取当前时间
		long s = System.currentTimeMillis();
		logger.info("开始备份");

		// 定义过程信息
		ProcedureInfo procedureInfo = new ProcedureInfo();
		procedureInfo.setTaskid(this.id);
		procedureInfo.setAction(Action.backup);
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
						"backup.sh", this.backFileName,
						item.nodeInfo.getWorkDir(), this.updateFileName,
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
						sb.append("备份成功，服务器[host:");
						sb.append(item.nodeInfo.getHost());
						sb.append(" ,port:");
						sb.append(item.nodeInfo.getPort());
						sb.append(" ,updateDir:");
						sb.append(item.nodeInfo.getUpateDir());
						sb.append("]");
						String msg=sb.toString();
						procedureInfo.setStatus("02");
						procedureInfo.setDetail(msg);
						// 报告错误
						this.processCallback.callback(procedureInfo);
						logger.error(msg + "\n" + arrays[0]);
						return false;
					}else{
						StringBuilder sb=new StringBuilder();
						sb.append("备份成功，服务器[host:");
						sb.append(item.nodeInfo.getHost());
						sb.append(" ,port:");
						sb.append(item.nodeInfo.getPort());
						sb.append(" ,updateDir:");
						sb.append(item.nodeInfo.getUpateDir());
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
			sb.append("备份出错，服务器[host:");
			sb.append(items.get(i).nodeInfo.getHost());
			sb.append(" ,port:");
			sb.append(items.get(i).nodeInfo.getPort());
			sb.append(" ,updateDir:");
			sb.append(items.get(i).nodeInfo.getUpateDir());
			sb.append("]");
			String msg=sb.toString();
			logger.error(msg, e);
			procedureInfo.setStatus("02");
			procedureInfo.setDetail(msg);
			// 报告错误
			this.processCallback.callback(procedureInfo);
			return false;
		}
		logger.info("完成备份，耗时:" + (System.currentTimeMillis() - s) + "毫秒");
		procedureInfo.setStatus("01");
		procedureInfo.setAction(Action.finishBackup);
		procedureInfo.setDetail("完成备份");
		// 报告
		this.processCallback.callback(procedureInfo);
		return true;
	}

	/**
	 * 更新文件
	 */
	private boolean update() {
		// 获取当前时间
		long s = System.currentTimeMillis();
		logger.info("开始更新");

		// 定义过程信息
		ProcedureInfo procedureInfo = new ProcedureInfo();
		procedureInfo.setTaskid(this.id);
		procedureInfo.setAction(Action.update);
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
				Protocol protocol = this.nodeInfos[i].getProtocol();
				// 获取关闭命令
				String script = ShellScriptManager.getScript(item.os.getName(),
						"update.sh", item.nodeInfo.getWorkDir(),
						this.updateFileName, updateDir);
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
					if (!containsKey(res, "success")) {
						StringBuilder sb=new StringBuilder();
						sb.append("更新失败，服务器[host:");
						sb.append(item.nodeInfo.getHost());
						sb.append(" ,port:");
						sb.append(item.nodeInfo.getPort());
						sb.append(" ,updateDir:");
						sb.append(item.nodeInfo.getUpateDir());
						sb.append("]");
						String msg=sb.toString();
						procedureInfo.setStatus("02");
						procedureInfo.setDetail(msg);
						// 报告错误
						this.processCallback.callback(procedureInfo);
						logger.error(msg + "\n" + res);
						return false;
					}else{
						StringBuilder sb=new StringBuilder();
						sb.append("更新成功，服务器[host:");
						sb.append(item.nodeInfo.getHost());
						sb.append(" ,port:");
						sb.append(item.nodeInfo.getPort());
						sb.append(" ,updateDir:");
						sb.append(item.nodeInfo.getUpateDir());
						sb.append("]");
						String msg=sb.toString();
						procedureInfo.setStatus("01");
						procedureInfo.setDetail(msg);
						// 报告错误
						this.processCallback.callback(procedureInfo);
					}

				} finally {
					connector.disconnect();
				}
			}
		} catch (Exception e) {
			StringBuilder sb=new StringBuilder();
			sb.append("更新出错，服务器[host:");
			sb.append(items.get(i).nodeInfo.getHost());
			sb.append(" ,port:");
			sb.append(items.get(i).nodeInfo.getPort());
			sb.append(" ,updateDir:");
			sb.append(items.get(i).nodeInfo.getUpateDir());
			sb.append("]");
			String msg=sb.toString();
			logger.error(msg, e);
			procedureInfo.setStatus("02");
			procedureInfo.setDetail(msg);
			// 报告错误
			this.processCallback.callback(procedureInfo);
			return false;
		}
		logger.info("完成备份，耗时:" + (System.currentTimeMillis() - s) + "毫秒");
		procedureInfo.setStatus("01");
		procedureInfo.setAction(Action.finishUpdate);
		procedureInfo.setDetail("完成更新");
		// 报告
		this.processCallback.callback(procedureInfo);
		return true;
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
					StringBuilder sb=new StringBuilder();
					sb.append("杀进程成功，服务器[host:");
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
					return true;
				}else{
					StringBuilder sb=new StringBuilder();
					sb.append("杀进程失败，服务器[host:");
					sb.append(item.nodeInfo.getHost());
					sb.append(" ,port:");
					sb.append(item.nodeInfo.getPort());
					sb.append(" ,name:");
					sb.append(item.nodeInfo.getName());
					sb.append("]");
					String msg=sb.toString();
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
			StringBuilder sb=new StringBuilder();
			sb.append("杀进程出错，服务器[host:");
			sb.append(item.nodeInfo.getHost());
			sb.append(" ,port:");
			sb.append(item.nodeInfo.getPort());
			sb.append(" ,name:");
			sb.append(item.nodeInfo.getName());
			sb.append("]");
			String msg=sb.toString();
			logger.error(msg, e);
			procedureInfo.setStatus("02");
			procedureInfo.setDetail(msg);
			// 报告错误
			this.processCallback.callback(procedureInfo);
			return false;
		}
	}

	/**
	 * 校验是否更新成功
	 */
	private boolean check() {
		// 获取当前时间
		long s = System.currentTimeMillis();
		logger.info("开始校验更新是否成功");

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
				Protocol protocol = this.nodeInfos[i].getProtocol();

				// 获取检查命令
				String script = ShellScriptManager.getScript(item.os.getName(),
						"check.sh", item.nodeInfo.getWorkDir(),
						this.updateFileName, updateDir);
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
		
		logger.info("校验更新完成，耗时:" + (System.currentTimeMillis() - s) + "毫秒");
		return true;
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
			StringBuilder sb=new StringBuilder();
			sb.append("关闭BIPS失败，服务器[host:");
			sb.append(item.nodeInfo.getHost());
			sb.append(" ,port:");
			sb.append(item.nodeInfo.getPort());
			sb.append(" ,name:");
			sb.append(item.nodeInfo.getName());
			sb.append("]");
			String msg=sb.toString();

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
			procedureInfo.setDetail("BIPS启动成功");
			procedureInfo.setStatus("01");
			this.processCallback.callback(procedureInfo);
			return true;
		} else {
			for (int i = 0, size = checkItems.size(); i < size; i++) {
				Item item = checkItems.get(i);
				StringBuilder sb=new StringBuilder();
				sb.append("启动BIPS失败，服务器[host:");
				sb.append(item.nodeInfo.getHost());
				sb.append(" ,port:");
				sb.append(item.nodeInfo.getPort());
				sb.append(" ,name:");
				sb.append(item.nodeInfo.getName());
				sb.append("]");
				String msg=sb.toString();

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
					// 传送文件到对应的服务器
					res = this.transmit();
					break;
				case 3:
					if (this.needRestart) {
						// 关闭服务器
						res = this.shutdown();
					} else {
						res = true;
					}

					break;
				case 4:
					if (this.needRestart) {
						// 判断服务器是否已经关闭
						res = this.isShutdown();
					} else {
						res = true;
					}
					break;
				case 5:
					// 解压文件
					res = this.uncompress();
					break;
				case 6:
					// 备份文件
					res = this.backup();
					break;
				case 7:
					// 更新文件
					res = this.update();
					break;
				case 8:
					// 检查是否更新成功
					res = this.check();
					break;
				case 9:
					if (this.needRestart) {
						// 启动服务器
						res = this.startup();
					} else {
						res = true;
					}
					break;
				case 10:
					if (this.needRestart) {
						// 检查是否启动成功
						res = this.isStartup();
					} else {
						res = true;
					}
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
			procedureInfo.setDetail("部署任务结束");

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
