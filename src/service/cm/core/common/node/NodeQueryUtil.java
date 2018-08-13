package service.cm.core.common.node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import both.common.constant.Protocol;
import cn.com.bankit.phoenix.jdbc.tool.DBAccessor;

/**
 * 节点查询工具
 * 
 * @author 江成
 * 
 */
public class NodeQueryUtil {

	/**
	 * 创建节点信息
	 * 
	 * @param nodeMap
	 * @param serverMap
	 * @return
	 */
	private static NodeInfo createNodeInfo(Map<String, Object> nodeMap,
			Map<String, Object> serverMap) {
		String name = (String) nodeMap.get("NAME");
		String description = (String) nodeMap.get("DESCRIPTION");
		String host = (String) serverMap.get("HOSTIP");
		String port = (String) serverMap.get("LOGINPORT");
		String userName = (String) serverMap.get("LOGINUSERNAME");
		String password = (String) serverMap.get("LOGINPASSWORD");
		String protocolType = (String) serverMap.get("LOGINMODE");
		String transmitUserName = (String) serverMap.get("FILEUSERNAME");
		String transmitPassword = (String) serverMap.get("FILEPASSWORD");
		String transmitProtocolType = (String) serverMap.get("FILEMODE");
		String transmitPort = (String) serverMap.get("FILEPORT");
		String jvmPort = (String) nodeMap.get("JVMPORT");
		String appPort = (String) nodeMap.get("APPPORT");
		String workDir = (String) nodeMap.get("UPDATEDIRECTORY");
		String updateDir = (String) nodeMap.get("LINKDIRECTORY");
		String appDir = (String) nodeMap.get("APPPATH");
		if (updateDir == null || "".equals(updateDir)) {
			updateDir = appDir;
		}

		NodeInfo nodeInfo = new NodeInfo();
		nodeInfo.setName(name);
		nodeInfo.setDescription(description);
		nodeInfo.setHost(host);
		nodeInfo.setPort(Integer.parseInt(port));
		nodeInfo.setUserName(userName);
		nodeInfo.setPassword(password);
		if ("01".equals(protocolType)) {
			nodeInfo.setProtocol(Protocol.TELNET);
		} else {
			nodeInfo.setProtocol(Protocol.SSH);
		}
		nodeInfo.setTransmitUserName(transmitUserName);
		nodeInfo.setTransmitPassword(transmitPassword);
		nodeInfo.setTransmitPort(Integer.parseInt(transmitPort));
		if ("01".equals(transmitProtocolType)) {
			nodeInfo.setTransmitProtocol(Protocol.TELNET);
		} else {
			nodeInfo.setTransmitProtocol(Protocol.SSH);
		}

		nodeInfo.setJvmPort(Integer.parseInt(jvmPort));
		nodeInfo.setAppPort(Integer.parseInt(appPort));
		nodeInfo.setWorkDir(workDir);
		nodeInfo.setUpateDir(updateDir);
		nodeInfo.setAppDir(appDir);
		return nodeInfo;
	}

	/**
	 * 获取节点信息
	 * 
	 * @param hostip
	 * @param nodeName
	 * @return
	 */
	public static NodeInfo[] query(String[] hostip, String[] nodeName)
			throws Exception {
		// 获取数据库访问器
		DBAccessor dbAccessor = DBAccessor.getDBAccessor();
		// 获取结果集合
		List<NodeInfo> nodeInfos = new ArrayList<NodeInfo>();
		for (int i = 0; i < hostip.length; i++) {
			StringBuilder sb = new StringBuilder();
			sb.append("select * from FOX_MGR_CM_NODEINFO where HOSTIP='");
			sb.append(hostip[i]);
			sb.append("' and ");
			sb.append("NAME='");
			sb.append(nodeName[i]);
			sb.append("'");
			// 构造SQL
			String sql = sb.toString();
			// 获取结果
			List<Map<String, Object>> list = dbAccessor.query(sql);

			if (list != null && list.size() == 1) {
				Map<String, Object> nodeInfo = list.get(0);
				String host = (String) nodeInfo.get("HOSTIP");

				sb = new StringBuilder();
				sb.append("select * from FOX_MGR_CM_HOSTINFO where HOSTIP='");
				sb.append(host);
				sb.append("'");
				// 查询服务器信息
				List<Map<String, Object>> serverInfos = dbAccessor.query(sb
						.toString());
				if (serverInfos == null || serverInfos.size() != 1) {
					throw new Exception("查找对应的服务节点出错 host:" + host);
				}

				Map<String, Object> serverInfo = serverInfos.get(0);
				// 创建节点信息
				NodeInfo node = createNodeInfo(nodeInfo, serverInfo);
				// 加入列表
				nodeInfos.add(node);
			}
		}
		NodeInfo[] res = nodeInfos.toArray(new NodeInfo[0]);
		return res;
	}

	/**
	 * 获取所以节点信息
	 * 
	 * @param hostip
	 * @param nodeName
	 * @return
	 */
	public static NodeInfo[] queryAll() throws Exception {
		// 获取数据库访问器
		DBAccessor dbAccessor = DBAccessor.getDBAccessor();
		// 获取结果集合
		List<NodeInfo> nodeInfos = new ArrayList<NodeInfo>();
		StringBuilder sb = new StringBuilder();
		sb.append("select * from FOX_MGR_CM_NODEINFO");
		// 构造SQL
		String sql = sb.toString();
		// 获取结果
		List<Map<String, Object>> list = dbAccessor.query(sql);

		for (int i = 0, size = list.size(); i < size; i++) {
			Map<String, Object> nodeInfo = list.get(i);
			String host = (String) nodeInfo.get("HOSTIP");

			sb = new StringBuilder();
			sb.append("SELECT * FROM FOX_MGR_CM_HOSTINFO WHERE HOSTIP='");
			sb.append(host);
			sb.append("'");
			// 查询服务器信息
			List<Map<String, Object>> serverInfos = dbAccessor.query(sb
					.toString());
			if (serverInfos == null || serverInfos.size() != 1) {
				throw new Exception("查找对应的服务节点出错 host:" + host);
			}

			Map<String, Object> serverInfo = serverInfos.get(0);
			// 创建节点信息
			NodeInfo node = createNodeInfo(nodeInfo, serverInfo);
			// 加入列表
			nodeInfos.add(node);
		}
		NodeInfo[] res = nodeInfos.toArray(new NodeInfo[0]);
		return res;
	}
}
