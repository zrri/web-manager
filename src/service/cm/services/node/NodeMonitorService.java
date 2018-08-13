package service.cm.services.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import service.cm.core.node.Node;
import service.cm.core.node.NodeManager;
import service.cm.core.node.PartitionState;
import service.cm.core.task.GetNodesStatusTask;
import service.cm.core.task.GetNodesStatusTask.NodeOperType;
import service.cm.core.task.TaskManager;
import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;
import service.common.websocket.NotifyUtil;
import both.constants.IResponseConstant;
import cn.com.bankit.phoenix.trade.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 节点运行信息， 操作服务
 * 
 * @author liaozhijie
 * 
 */
public class NodeMonitorService extends Service<JsonRequest, JsonResponse> {

	/**
	 * 节点已启动
	 */
	private static String NodeStarted = "01";

	/**
	 * 节点已关闭
	 */
	private static String NodeStoped = "02";

	/**
	 * 节点不存在
	 */
	private static String NodeEmpty = "03";

	/**
	 * 节点连接异常
	 */
	private static String NodeLinkError = "04";

	private int countNodeOfOneThread = 5;

	/**
	 * 启动节点
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public JsonResponse nodeStart(JsonRequest request) throws Exception {
		JsonResponse response = new JsonResponse();

		JSONArray nodeList = (JSONArray) request.get("list");
		String userId = request.getAsString("userId");
		//
		if (nodeList == null)
			return response;
		// {"list":"[{"hostip":"10.10.1.3","nodeName":"BIPS_A"},{"hostip":"10.10.1.4","nodeName":"BIPS_B"}]"}

		// 关闭节点
		for (int i = 0, len = nodeList.size(); i < len; i = i
				+ countNodeOfOneThread) {
			List<Map<String, String>> tmpList = new ArrayList<Map<String, String>>();
			for (int j = 0; j < countNodeOfOneThread; j++) {

				if (i + j >= len)
					break;
				@SuppressWarnings("unchecked")
				Map<String, String> map = (Map<String, String>) nodeList.get(i
						+ j);

				tmpList.add(map);

			}

			TaskManager.getInstance().put(
					new GetNodesStatusTask(userId, this, tmpList,
							NodeOperType.Start));
			TaskManager.getInstance().execute();
		}

		response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
		response.put(IResponseConstant.retMsg, "成功");

		return response;
	}

	/**
	 * 关闭节点
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public JsonResponse nodeStop(JsonRequest request) throws Exception {
		JsonResponse response = new JsonResponse();

		JSONArray nodeList = (JSONArray) request.get("list");
		String userId = request.getAsString("userId");
		//
		if (nodeList == null)
			return response;
		// {"list":"[{"hostip":"10.10.1.3","nodeName":"BIPS_A"},{"hostip":"10.10.1.4","nodeName":"BIPS_B"}]"}

		// 关闭节点
		for (int i = 0, len = nodeList.size(); i < len; i = i
				+ countNodeOfOneThread) {
			List<Map<String, String>> tmpList = new ArrayList<Map<String, String>>();
			for (int j = 0; j < countNodeOfOneThread; j++) {

				if (i + j >= len)
					break;
				@SuppressWarnings("unchecked")
				Map<String, String> map = (Map<String, String>) nodeList.get(i
						+ j);

				tmpList.add(map);

			}

			TaskManager.getInstance().put(
					new GetNodesStatusTask(userId, this, tmpList,
							NodeOperType.Stop));
			TaskManager.getInstance().execute();
		}

		response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
		response.put(IResponseConstant.retMsg, "成功");

		return response;
	}

	/**
	 * 获取单个节点详细信息
	 * 
	 * @param request
	 * @return
	 */
	public JsonResponse getNodeDetailInfo(JsonRequest request) {
		JsonResponse response = new JsonResponse();
		// {"data":{"HOSTIP":"172.16.0.4","NAME":"BIPS_A"},"header":{}}

		String hostip = request.getAsString("HOSTIP");
		String name = request.getAsString("NAME");

		try {
			Node node = NodeManager.getNode(hostip, name);
			response.put("CPUUSAGE", String.valueOf(node.getCpuUsage()));
			response.put("TOTALMEMORY",
					String.valueOf(node.getTotalMemorySize()));
			response.put("RUNNINGTIME", String.valueOf(node.getRunningTime()));
			response.put("PEEKTHREADCOUNT",
					String.valueOf(node.getPeakThreadCount()));
			response.put("DAEMONTHREADCOUNT",
					String.valueOf(node.getDaemonThreadCount()));
			response.put("THREADCOUNT", String.valueOf(node.getThreadCount()));
			response.put("STARTEDTHREDCOUNT",
					String.valueOf(node.getTotalStartedThreadCount()));
			String[] args = node.getJvmInputArguments();
			StringBuilder sb = new StringBuilder();
			for (int i = 0, len = args.length; i < len; i++) {
				sb.append(args[i]);
				sb.append("|");
			}

			response.put("JvmInputArguments", sb.toString());
			response.put("LoadedClassCount",
					String.valueOf(node.getLoadedClassCount()));

			PartitionState[] partitionState = node.getPartitionState();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

			for (int i = 0, len = partitionState.length; i < len; i++) {
				list.add(partitionState[i].toMap());
			}
			response.put("PartitionState", list);
			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			response.put(IResponseConstant.retMsg, "成功");
		} catch (Exception e) {
			response.put(IResponseConstant.retCode, IResponseConstant.FAILED);
			response.put(IResponseConstant.retMsg, e.getMessage());
		}
		return response;
	}

	/**
	 * 获取多个节点的启动状态
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public JsonResponse getNodeStatus(JsonRequest request) throws Exception {
		JsonResponse response = new JsonResponse();

		JSONArray nodeList = (JSONArray) request.get("list");
		String userId = request.getAsString("userId");
		//
		if (nodeList == null)
			return response;
		// {"list":"[{"hostip":"10.10.1.3","nodeName":"BIPS_A"},{"hostip":"10.10.1.4","nodeName":"BIPS_B"}]"}

		List<JSONObject> infoList = new ArrayList<JSONObject>();

		// 获取对应的应用节点，关闭
		for (int i = 0, len = nodeList.size(); i < len; i = i
				+ countNodeOfOneThread) {
			List<Map<String, String>> tmpList = new ArrayList<Map<String, String>>();
			for (int j = 0; j < countNodeOfOneThread; j++) {

				if (i + j >= len)
					break;
				@SuppressWarnings("unchecked")
				Map<String, String> map = (Map<String, String>) nodeList.get(i
						+ j);

				tmpList.add(map);

			}

			TaskManager.getInstance().put(
					new GetNodesStatusTask(userId, this, tmpList,
							NodeOperType.Status));
			TaskManager.getInstance().execute();
		}

		response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
		response.put(IResponseConstant.retMsg, "成功");

		return response;
	}

	@Override
	public JsonResponse execute(JsonRequest arg0) throws Exception {
		return null;
	}
}