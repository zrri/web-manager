package service.cm.core.node;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import both.common.util.LoggerUtil;

import service.cm.core.common.node.NodeInfo;
import service.cm.core.common.node.NodeQueryUtil;



/**
 * 节点管理器
 * 
 * @author 江成
 * 
 */
public class NodeManager {

	/**
	 * Node
	 */
	private static ConcurrentHashMap<String, Node> nodes = new ConcurrentHashMap<String, Node>();

	/**
	 * 同步锁
	 */
	private static Lock lock = new ReentrantLock();
	
	/**
	 * 获取节点
	 * 
	 * @param ip
	 * @param nodeName
	 * @return
	 */
	public static Node getNode(String ip, String nodeName) {
		// 获取节点信息
		NodeInfo nodeInfo;
		//加锁
		lock.lock();
		try {
			//获取当前
			nodeInfo = getNodeInfo(ip, nodeName);
			if(nodeInfo==null)
				//throw new Exception("未获取的nodeInfo");
				return null;
			Node node = nodes.get(ip+"_"+nodeName);
			if(node == null) {
				node = new Node(nodeInfo);
				nodes.put(ip+"_"+nodeName, node);
			} else {
				if(node.isChange(nodeInfo)) {
					//释放node连接
					node.release();
					//创建新的node节点
					node = new Node(nodeInfo);
					nodes.put(ip+"_"+nodeName, node);
				}
			}
			return node;
		} catch (Exception e) {
			LoggerUtil.error(e.getMessage(), e);
		}finally{
			//解锁
			lock.unlock();
		}
		return null;

	}
	
	/**
	 * 获取节信息
	 * 
	 * @param hostip
	 * @param nodeName
	 * @return
	 */
	private static NodeInfo getNodeInfo(String hostip, String nodeName)
			throws Exception {
		NodeInfo[] infos = NodeQueryUtil.query(new String[] { hostip },
				new String[] { nodeName });
		if (infos != null && infos.length > 0) {
			return infos[0];
		}
		return null;
	}
}
