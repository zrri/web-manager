package service.cm.core.deploy;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import service.cm.core.common.node.NodeInfo;
import service.cm.core.common.node.NodeQueryUtil;
import service.cm.core.deploy.task.DeployTask;

/**
 * 节点部署器
 * 
 * @author 江成
 * 
 */
public class NodeDeployer {

	/**
	 * 种子
	 */
	private static AtomicLong seed = new AtomicLong(0);

	/**
	 * 部署任务注册表
	 */
	private static Map<String, DeployTask> deployTaskMap = new HashMap<String, DeployTask>();

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
	 * @param versionFilePath
	 * @param needRestart
	 * @param processCallBack
	 * @return
	 * @throws Exception
	 */
	public static String startTask(String[] hostIps, String[] nodeNames,
			String versionFilePath, boolean needRestart,
			IProcessCallback processCallback) throws Exception {
		// 获取节点信息
		NodeInfo[] nodeInfos = NodeQueryUtil.query(hostIps, nodeNames);
		// 开始任务
		String taskId = startTask(nodeInfos, versionFilePath, needRestart,
				processCallback);
		return taskId;
	}

	/**
	 * 开始任务
	 * 
	 * @param nodeInfos
	 * @param versionFilePath
	 * @param needRestart
	 * @param processCallBack
	 * @return
	 * @throws Exception
	 */
	public static String startTask(NodeInfo[] nodeInfos,
			String versionFilePath, boolean needRestart,
			IProcessCallback processCallback) throws Exception {
		if (nodeInfos.length == 0) {
			throw new Exception("无法找到对应的节点信息");
		}
		// 定义版本更新文件
		File versionFile = new File(versionFilePath);
		if (!versionFile.isFile()) {
			throw new Exception("版本更新文件不存在");
		}
		// 获取任务ID
		String taskId = getTaskId();
		// 定义部署任务
		DeployTask deployTask = new DeployTask(taskId, nodeInfos, versionFile,
				needRestart, processCallback);
		// 加入列表
		deployTaskMap.put(taskId, deployTask);
		// 执行任务
		deployTask.startTask();
		return taskId;
	}

	/**
	 * 继续任务
	 * 
	 * @param taskid
	 * 
	 * @throws Exception
	 */
	public static void continueTask(String taskId) throws Exception {
		// 获取部署任务
		DeployTask deployTask = deployTaskMap.get(taskId);
		if (deployTask == null) {
			throw new Exception("没找到对应的部署任务");
		}
		// 继续任务
		deployTask.continueTask();
	}

	/**
	 * 完成任务
	 * 
	 * @param taskid
	 * 
	 * @throws Exception
	 */
	public static void finishTask(String taskid) throws Exception {
		deployTaskMap.remove(taskid);
	}

	/**
	 * 测试
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		IProcessCallback callback = new IProcessCallback() {

			/**
			 * call
			 */
			public void callback(ProcedureInfo procedureInfo) {
				System.out.println("---------> " + procedureInfo.getDetail());
			}

		};

		String[] hostip = new String[] { "158.222.2.83" };
		String[] nodeName = new String[] { "test" };
		String versionFilePath = "test/bip_update.201401020932.tar";
		NodeDeployer.startTask(hostip, nodeName, versionFilePath, true,
				callback);
	}
}
