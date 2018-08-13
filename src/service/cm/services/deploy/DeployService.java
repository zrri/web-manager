package service.cm.services.deploy;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.bankit.phoenix.resource.ResourceManager;
import cn.com.bankit.phoenix.trade.Service;
import both.common.util.LoggerUtil;
import both.common.util.StringUtilEx;
import both.constants.IResponseConstant;
import service.cm.core.deploy.IProcessCallback;
import service.cm.core.deploy.NodeDeployer;
import service.cm.core.deploy.NodeRollbacker;
import service.cm.core.deploy.ProcedureInfo;
import service.cm.core.deploy.task.DeployTask;
import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;
import service.common.websocket.NotifyUtil;

public class DeployService extends Service<JsonRequest, JsonResponse> {

	/**
	 * 每一个线程获取节点版本号列表的个数
	 */
	private final int nodeVersionPerThreadNeadNum = 5;

	/**
	 * 
	 */
	public DeployService() {
		super();
	}

	public JsonResponse startDeploy(JsonRequest request) {
		JsonResponse response = new JsonResponse();

		// "ids":"10.2.12.1_BIPS_A,10.2.12.2_BIPS_A"
		// "list":[{"HOSTIP":"10.2.12.1","NAME":"BIPS_A"},{"HOSTIP":"10.2.12.2","NAME":"BIPS_A"}]
		String[] hostip = null;
		String[] nodeName = null;
		String ids = request.getAsString("ids");
		final String userId = request.getAsString("userId");

		if (ids != null && !StringUtilEx.isNullOrEmpty(ids)) {
			String[] arrId = ids.split(",");
			hostip = new String[arrId.length];
			nodeName = new String[arrId.length];
			for (int i = 0; i < nodeName.length; i++) {
				String id = arrId[i].replaceFirst("_", ",");
				hostip[i] = id.split(",")[0];
				nodeName[i] = id.split(",")[1];
			}
		} else {
			response.put(IResponseConstant.retCode, IResponseConstant.FAILED);
			response.put(IResponseConstant.retMsg, "无ids");
			return response;
		}

		String version = request.getAsString("version");
		String needRestart = request.getAsString("needRestart");
		boolean needre = "true".equals(needRestart) ? true : false;

		try {
			String workspace = ResourceManager.getInstance().getWorkspaceRoot();

			File file = new File(workspace, "versionpath");
			file = new File(file, version);
			final Service<JsonRequest, JsonResponse> service = this;
			NodeDeployer.startTask(hostip, nodeName, file.getAbsolutePath(),
					needre, new IProcessCallback() {

						public void callback(ProcedureInfo procedureInfo) {
							LoggerUtil.debug("=========>procedureInfo:=="
									+ procedureInfo.toJSON());

							try {
								NotifyUtil.push(service, userId, "deploy",
										procedureInfo.toJSONObject());
							} catch (Exception e) {
								LoggerUtil.error(e.getMessage(), e);
							}
						}
					});
		} catch (Exception e) {
			LoggerUtil.error(e.getMessage(), e);
		}

		response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
		response.put(IResponseConstant.retMsg, "成功");

		return response;
	}

	public JsonResponse startUnDeploy(JsonRequest request) {
		JsonResponse response = new JsonResponse();

		return response;
	}

	/**
	 * 查询版本
	 * 
	 * @param request
	 * @return
	 * @throws InterruptedException 
	 */
	public JsonResponse listVersion(JsonRequest request) throws InterruptedException {
		JsonResponse response = new JsonResponse();

		// 部署的版本文件还是回退的版本文件
		String type = request.getAsString("type");
		String ids = request.getAsString("ids");
		List<String> list = new ArrayList<String>();
		// 获取部署版本文件
		if ("deploy".equalsIgnoreCase(type)) {
			String workspace = ResourceManager.getInstance().getWorkspaceRoot();

			File file = new File(workspace, "versionpath");

			response.put("debug", file.getAbsolutePath());
			if (file.exists()) {
				File[] files = file.listFiles();
				if (files.length == 0) {
					response.put(IResponseConstant.retCode,
							IResponseConstant.FAILED_NO_UPDATEFILE);
					response.put(IResponseConstant.retMsg, "没有版本文件");
					return response;
				}
				for (File file2 : files) {
					list.add(file2.getName());
				}
				// 排序
				Collections.sort(list, new Comparator<String>() {
					public int compare(String o1, String o2) {
						return o2.compareToIgnoreCase(o1);
					}
				});
				response.put("version_list", list);

			} else {
				file.mkdir();
				response.put(IResponseConstant.retCode,
						IResponseConstant.FAILED_NO_UPDATEFILE);
				response.put(IResponseConstant.retMsg, "没有版本文件");
				return response;
			}
		} else {

			Map<String, List<String>> map = new HashMap<String, List<String>>();
			String[] arrId = null;

			if (ids != null && !StringUtilEx.isNullOrEmpty(ids)) {
				arrId = ids.split(",");
				// 获取版本号
				getNodeVersionList(arrId, map);
			}

			// 只有一台回退，或者版本一致
			if (arrId != null && (arrId.length == 1 || isVersionSame(map))) {
				list = map.get(arrId[0]);
				// 排序
				Collections.sort(list, new Comparator<String>() {
					public int compare(String o1, String o2) {
						return o2.compareToIgnoreCase(o1);
					}
				});
				response.put("version_list", list);
			} else {
				response.put(IResponseConstant.retCode,
						IResponseConstant.FAILED_VERSION_ERROR);
				response.put(IResponseConstant.retMsg, "节点版本不一致!");
			}

			// 获取回退版本文件
		}
		response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
		response.put(IResponseConstant.retMsg, "成功");

		return response;
	}

	/**
	 * 版本回退：获取节点版本号列表
	 * 
	 * @param arrId
	 * @param map
	 * @throws InterruptedException
	 */
	private void getNodeVersionList(final String[] arrId,
			final Map<String, List<String>> map) throws InterruptedException {
		final int size = arrId.length;

		// 计算线程数
		int threadCount = size / this.nodeVersionPerThreadNeadNum;
		if (size % this.nodeVersionPerThreadNeadNum > 0) {
			threadCount += 1;
		}

		final Integer[] count = new Integer[] { threadCount };
		final Lock lock = new ReentrantLock();
		final Condition con = lock.newCondition();

		// 启线程
		while (threadCount > 0) {
			final int threadIndex = threadCount - 1;
			Thread t = new Thread() {

				@Override
				public void run() {
					int index = 0;
					// 每个线程获取nodeVersionPerThreadNeadNum个，不足nodeVersionPerThreadNeadNum个的获取剩下全部
					int n = 0;
					int startIndex = threadIndex * nodeVersionPerThreadNeadNum;
					while (n < nodeVersionPerThreadNeadNum) {
						try {
							index = startIndex + n;
							if (index > size - 1) {// 大于总数，跳出
								break;
							}
							// 获取版本号列表
							List<String> list = new ArrayList<String>();
							String id = arrId[index].replaceFirst("_", ",");
							list = NodeRollbacker.getVersionList(
									id.split(",")[0], id.split(",")[1]);
							synchronized (map) {
								map.put(arrId[index], list);
							}
						} catch (Exception e) {
						}
						n++;
					}

					lock.lock();
					try {
						// 当前线程数减1，唤醒等待
						count[0]--;
						con.signalAll();
					} finally {
						lock.unlock();
					}
				}
			};
			t.setName("RollbackerVersionThread-" + threadIndex);
			t.setDaemon(true);
			t.start();

			threadCount--;
		}
		;

		// 等待所有获取版本号线程执行完成
		lock.lock();
		try {
			while (count[0] > 0) {
				con.await();
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 判断版本是否一致，只要有一个不一致返回false
	 * 
	 * @param map
	 * @return
	 */
	private boolean isVersionSame(Map<String, List<String>> map) {
		// 是否第一个列表
		boolean isFirst = true;
		// 是否一致
		boolean isSame = false;
		List<String> firstList = new ArrayList<String>();
		for (String id : map.keySet()) {
			// 记录第一个列表
			if (isFirst) {
				firstList = map.get(id);
				isFirst = false;
				continue;
			}
			List<String> list = map.get(id);
			if (firstList.size() == list.size()) {
				for (String string : list) {
					if (firstList.contains(string)) {
						isSame = true;
					} else {// 内容不一致
						return false;
					}
				}
			} else {// 数量不一致
				return false;
			}

		}
		return isSame;
	}

	@Override
	public JsonResponse execute(JsonRequest arg0) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}