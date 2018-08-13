package service.cm.core.task;

import java.util.List;
import java.util.Map;

import service.cm.core.node.Node;
import service.cm.core.node.NodeManager;
import service.common.websocket.NotifyUtil;
import both.common.util.LoggerUtil;

import com.alibaba.fastjson.JSONObject;

import cn.com.bankit.phoenix.trade.Service;

public class GetNodesStatusTask extends AbstractTask {

	public enum NodeOperType {
		Status, Start, Stop
	}
	
	private NodeOperType operType;

	/**
	 * 
	 */
	public GetNodesStatusTask(String userId, Service service,
			List<Map<String, String>> list, NodeOperType type) {
		super();

		this.userId = userId;
		this.service = service;
		this.list = list;
		this.operType = type;
	}

	@Override
	public void run() {
		if (this.operType == NodeOperType.Status)
			getNodesStatus();
		else if (this.operType == NodeOperType.Start)
			startNodes();
		else if (this.operType == NodeOperType.Stop)
			stopNodes();
	}

	/**
	 * 获取指定节点起停状态
	 */
	private void getNodesStatus() {
		for (int i = 0, len = list.size(); i < len; i++) {
			Map<String, String> map = (Map<String, String>) list.get(i);

			String hostip = map.get("hostip");
			String nodeName = map.get("nodeName");

			JSONObject json = new JSONObject();
			try {
				// 获取节点信息
				Node node = NodeManager.getNode(hostip, nodeName);
				// 节点为空
				if (node == null) {

					json.put("hostip", hostip);
					json.put("nodeName", nodeName);
					json.put("status", NodeEmpty);
				} else {
					Boolean status = node.isStarted();
					json.put("hostip", hostip);
					json.put("nodeName", nodeName);
					json.put("status", status ? NodeStarted : NodeStoped);

				}
			} catch (Exception e) {
				json.put("hostip", hostip);
				json.put("nodeName", nodeName);
				json.put("status", NodeLinkError);
			} finally {
				try {
					json.put("id",
							String.valueOf(Thread.currentThread().getId()));

					NotifyUtil.push(service, userId, "nodestatus", json);
				} catch (Exception e) {
					LoggerUtil.error(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * 启动节点
	 */
	private void startNodes() {
		for (int i = 0, len = list.size(); i < len; i++) {
			Map<String, String> map = (Map<String, String>) list.get(i);

			String hostip = map.get("hostip");
			String nodeName = map.get("nodeName");

			JSONObject json = new JSONObject();
			try {
				// 获取节点信息
				Node node = NodeManager.getNode(hostip, nodeName);
				// 节点为空
				if (node == null) {

					json.put("hostip", hostip);
					json.put("nodeName", nodeName);
					json.put("status", NodeEmpty);
				} else {
					Boolean status = node.start();
					json.put("hostip", hostip);
					json.put("nodeName", nodeName);
					json.put("status", status ? NodeStarted : NodeStoped);

				}
			} catch (Exception e) {
				json.put("hostip", hostip);
				json.put("nodeName", nodeName);
				json.put("status", NodeLinkError);
			} finally {
				try {
					json.put("id",
							String.valueOf(Thread.currentThread().getId()));

					NotifyUtil.push(service, userId, "nodestart", json);
				} catch (Exception e) {
					LoggerUtil.error(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * 关闭节点
	 */
	private void stopNodes() {
		for (int i = 0, len = list.size(); i < len; i++) {
			Map<String, String> map = (Map<String, String>) list.get(i);

			String hostip = map.get("hostip");
			String nodeName = map.get("nodeName");

			JSONObject json = new JSONObject();
			try {
				// 获取节点信息
				Node node = NodeManager.getNode(hostip, nodeName);
				// 节点为空
				if (node == null) {

					json.put("hostip", hostip);
					json.put("nodeName", nodeName);
					json.put("status", NodeEmpty);
				} else {
					Boolean status = node.stop();
					json.put("hostip", hostip);
					json.put("nodeName", nodeName);
					json.put("status", !status ? NodeStarted : NodeStoped);

				}
			} catch (Exception e) {
				json.put("hostip", hostip);
				json.put("nodeName", nodeName);
				json.put("status", NodeLinkError);
			} finally {
				try {
					json.put("id",
							String.valueOf(Thread.currentThread().getId()));

					NotifyUtil.push(service, userId, "nodestop", json);
				} catch (Exception e) {
					LoggerUtil.error(e.getMessage(), e);
				}
			}
		}
	}
}