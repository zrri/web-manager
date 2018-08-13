package service.cm.core.deploy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import service.cm.core.common.node.NodeInfo;
import service.cm.core.common.node.NodeQueryUtil;
import service.cm.core.deploy.task.RollbackTask;
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
 * 节点版本回退器
 * 
 * @author 江成
 * 
 */
public class NodeRollbacker {

	/**
	 * 日志
	 */
	private static Logger logger = LoggerFactory
			.getLogger(NodeRollbacker.class);

	/**
	 * 种子
	 */
	private static AtomicLong seed = new AtomicLong(0);

	/**
	 * 回退任务注册表
	 */
	private static Map<String, RollbackTask> rollbackTaskMap = new HashMap<String, RollbackTask>();

	/**
	 * 获取任务ID
	 * 
	 * @return
	 */
	private static String getTaskId() {
		long id = seed.addAndGet(1);
		return String.valueOf(id);
	}

	/**
	 * 开始任务
	 * 
	 * @param hostIps
	 * @param nodeNames
	 * @param versionFile
	 * @param historyVersions
	 * @param processCallback
	 * @return
	 * @throws Exception
	 */
	public static String startTask(String[] hostIps, String[] nodeNames,
			String versionFile, List<String> historyVersions,
			IProcessCallback processCallback) throws Exception {
		// 获取节点信息
		NodeInfo[] nodeInfos = NodeQueryUtil.query(hostIps, nodeNames);
		// 开始任务
		String taskId = startTask(nodeInfos, versionFile, historyVersions,
				processCallback);
		return taskId;
	}

	/**
	 * 开始任务
	 * 
	 * @param nodeInfos
	 * @param versionFile
	 * @param historyVersions
	 * @param processCallback
	 * @return
	 * @throws Exception
	 */
	public static String startTask(NodeInfo[] nodeInfos, String versionFile,
			List<String> historyVersions, IProcessCallback processCallback)
			throws Exception {
		if (nodeInfos.length == 0) {
			throw new Exception("无法找到对应的节点信息");
		}
		// 判断历史版本是否存在
		if (!historyVersions.contains(versionFile)) {
			throw new Exception("回退版本文件不存在");
		}
		// 获取任务ID
		String taskId = getTaskId();
		// 定义回退任务
		RollbackTask rollbackTask = new RollbackTask(taskId, nodeInfos,
				versionFile, historyVersions, processCallback);
		// 加入列表
		rollbackTaskMap.put(taskId, rollbackTask);
		// 执行任务
		rollbackTask.startTask();
		return taskId;
	}

	/**
	 * 继续任务
	 * 
	 * @param taskId
	 * 
	 * @throws Exception
	 */
	public static void continueTask(String taskId) throws Exception {
		// 获取部署任务
		RollbackTask rollbackTask = rollbackTaskMap.get(taskId);
		if (rollbackTask == null) {
			throw new Exception("没找到对应的回退任务");
		}
		// 继续任务
		rollbackTask.continueTask();
	}

	/**
	 * 完成任务
	 * 
	 * @param taskid
	 * 
	 * @throws Exception
	 */
	public static void finishTask(String taskid) throws Exception {
		rollbackTaskMap.remove(taskid);
	}

	/**
	 * 获取版本列表
	 * 
	 * @param hostIps
	 * @param nodeNames
	 * @return
	 */
	public static List<String> getVersionList(String hostIp, String nodeName)
			throws Exception {
		// 获取节点信息
		NodeInfo[] nodeInfos = NodeQueryUtil.query(new String[] { hostIp },
				new String[] { nodeName });
		if (nodeInfos.length == 0) {
			throw new Exception("无法找到对应的节点信息");
		}
		List<String> versionList = getVersionList(nodeInfos[0]);
		return versionList;
	}

	/**
	 * 获取版本列表
	 * 
	 * @param ndoeInfo
	 * @return
	 * @throws Exception
	 */
	public static List<String> getVersionList(NodeInfo nodeInfo)
			throws Exception {
		// 定义网络参数
		NetArgs netArgs = new NetArgs();
		netArgs.ip = nodeInfo.getHost();
		netArgs.port = nodeInfo.getPort();
		netArgs.userName = nodeInfo.getUserName();
		netArgs.password = nodeInfo.getPassword();
		netArgs.timeout = 1000 * 30;
		// 获取协议
		Protocol protocol = nodeInfo.getProtocol();
		// 获取提示符
		String prompt = PromptGetter.getPrompt(protocol, netArgs);
		logger.debug("获取提示符：" + prompt);
		// 获取系统类型
		OS os = SystemGetter.getSystemType(protocol, netArgs, prompt);
		logger.debug("获取系统类型：" + os);
		// 获取系统语言
		String lang = LangGetter.getUsedLang(prompt, os, protocol, netArgs);
		logger.debug("获取系统语言：" + lang);

		// 获取解压命令
		String script = ShellScriptManager.getScript(os.getName(), "list.sh",
				nodeInfo.getWorkDir() + "/backup");
		String command = script;
		command += "exit\n";

		List<String> versionList = new ArrayList<String>();
		// 获取连接器
		IConnector connector = ConnectorFactory.getConnector(protocol, netArgs);
		try {
			// 获取编码格式
			String encoding = getEncoding(lang);
			// 连接服务器
			connector.connect();
			// 执行命令
			String data = ShellExcutor.execute(command, encoding, connector,
					netArgs.timeout * 3);
			String res = ResultExtractor.extract(data, prompt, "exit");
			// 定义备份文件前缀
			String prefix = "backup_";

			int startIndex = res.indexOf(prefix);
			if (startIndex == -1) {
				return versionList;
			}

			String postfix = ".jar";
			int endIndex = res.indexOf(postfix);
			if (endIndex == -1) {
				postfix = ".tar";
				endIndex = res.indexOf(postfix);
				if (endIndex == -1) {
					return versionList;
				}
			}
			// 计算后缀长度
			int postfixLen = postfix.length();

			for (int len = res.length(); startIndex != -1 && startIndex < len;) {
				// 计算后缀
				endIndex = res.indexOf(postfix, startIndex) + postfixLen;
				String s = res.substring(startIndex, endIndex);

				// 倒序插入
				int size = versionList.size();
				int index = -1;
				for (int j = 0; j < size; j++) {
					String name = versionList.get(j);
					if (s.compareTo(name) >= 0) {
						index = j;
						break;
					}
				}
				if (index == -1) {
					versionList.add(s);
				} else {
					versionList.add(index, s);
				}
				// 计算前缀
				startIndex = res.indexOf(prefix, endIndex);

			}
		} finally {
			connector.disconnect();
		}
		return versionList;
	}

	/**
	 * 获取编码类型
	 * 
	 * @param encoding
	 * @return
	 */
	private static String getEncoding(String lang) {
		// 大写格式
		lang = lang.toUpperCase();
		if (lang.indexOf("UTF-8") != -1) {
			return "UTF-8";
		} else {
			return "GBK";
		}
	}

}
