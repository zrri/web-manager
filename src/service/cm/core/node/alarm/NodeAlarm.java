package service.cm.core.node.alarm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import service.cm.core.alarm.Alarm;
import service.cm.core.alarm.Attribute;
import service.cm.core.alarm.AttributeType;
import service.cm.core.alarm.Message;
import service.cm.core.alarm.NoticeType;
import service.cm.core.alarm.WarnLog;
import service.cm.core.alarm.exception.AttributeNotExistException;
import service.cm.core.alarm.util.AlarmDBUtil;
import service.cm.core.common.node.NodeInfo;
import service.cm.core.common.node.NodeQueryUtil;
import service.cm.core.node.Node;
import both.common.util.LoggerUtil;
import both.common.util.TypeConverter;

public class NodeAlarm extends Alarm {

	/**
	 * 名称
	 */
	private String name = "App预警器";

	/**
	 * 属性注册表
	 */
	private Map<String, Recorder> recorderMap = new HashMap<String, Recorder>();

	/**
	 * 监控集合
	 */
	private Set<String> monitorSet = new HashSet<String>();

	/**
	 * 互斥锁
	 */
	private Lock lock = new ReentrantLock();

	/**
	 * 是否可用
	 */
	private boolean enable = true;

	/**
	 * 通知方式
	 */
	private NoticeType[] noticeTypes;

	/**
	 * 检查线程
	 */
	private Thread checker;

	/**
	 * 间隔时间
	 */
	private long interval = 10000;

	/**
	 * node队列
	 */
	private List<Node> nodes = new ArrayList<Node>();

	/**
	 * 无法连通节点
	 */
	private Set<String> unconnectedNodes = new HashSet<String>();

	/**
	 * 日期格式化
	 */
	private static SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * 警告信息Map
	 */
	private Map<String, Message> warnMessageMap = new LinkedHashMap<String, Message>();

	/**
	 * 历史队列大小
	 */
	private int maxSize = 50;

	/**
	 * 历史警告信息队列
	 */
	private List<Message> historyWarnMessageList = new ArrayList<Message>();

	/**
	 * 初始化
	 */
	public void init() {
		try {
			Map<String, Object> alarmInfoMap = AlarmDBUtil.getAlarm(this.name);
			if (alarmInfoMap != null) {
				String s = (String) alarmInfoMap.get("enable");
				this.enable = "true".equalsIgnoreCase(s);
				s = (String) alarmInfoMap.get("noticetypes");
				String[] items = s.split(",");
				if (items.length > 0) {
					this.noticeTypes = new NoticeType[items.length];
					for (int i = 0; i < items.length; i++) {
						this.noticeTypes[i] = NoticeType.parse(items[i]);
					}
				}
			}
		} catch (Exception e) {
			LoggerUtil.error(e.getMessage(), e);
		}
		// 初始化属性
		this.initAttribute();

		// 定义检查器
		this.checker = new Thread() {

			/**
			 * run
			 */
			public void run() {

				while (true) {
					if (enable) {
						// 检查
						check();
					}
					try {
						Thread.sleep(interval);
					} catch (InterruptedException e) {
						// do nothing
					}
				}
			}
		};
		checker.start();

		// try {
		// this.setEnable(true);
		// this.setNoticeType(new NoticeType[] { NoticeType.TIP,
		// NoticeType.MESSAGE });
		// this.setUpThreshold("clientCount", "5");
		// this.setUpThreshold("cpuUsage", "0.7");
		// this.setUpThreshold("moneryUsage", "0.6");
		// } catch (Exception e) {
		// logger.error(e.getMessage(), e);
		// }
	}

	/**
	 * 通知
	 * 
	 * @param nodeInfo
	 * @param rec
	 * @param value
	 * @param content
	 * @throws Exception
	 */
	private void warn(NodeInfo nodeInfo, Recorder rec, String value,
			String content) throws Exception {
		// 生成Key
		StringBuilder sb = new StringBuilder();
		sb.append(nodeInfo.getHost());
		sb.append("-");
		sb.append(nodeInfo.getName());
		sb.append("-");
		sb.append(rec.attribute.getName());
		String key = sb.toString();

		Message message = null;
		synchronized (this.warnMessageMap) {
			message = this.warnMessageMap.get(key);
		}
		if (message == null) {
			// 定义message
			message = new Message();
			// 设置ID
			message.setId(key + System.currentTimeMillis());
			// 定义元信息
			Map<String, Object> metadata = new HashMap<String, Object>();
			metadata.put("主机", nodeInfo.getHost());
			metadata.put("BIPS名称", nodeInfo.getName());
			metadata.put("发生时间", format.format(new Date()));
			message.setMetadata(metadata);
			message.setContent(content);
			// 定义警告信息
			WarnLog warnLog = new WarnLog();
			warnLog.alarmName = this.name;
			warnLog.attrName = rec.attribute.getName();
			warnLog.startTime = new Date();
			if (rec.attribute.isUpThresholdEnable()) {
				warnLog.maxUpThreshold = value;
				warnLog.curUpThreshold = value;
			}

			if (rec.attribute.isLowThresholdEnable()) {
				warnLog.minLowThreshold = value;
				warnLog.curLowThreshold = value;
			}

			if (rec.attribute.isStateValueEnable()) {
				warnLog.stateValue = value;
			}
			message.setWarnLog(warnLog);
			// 记录数据库
			AlarmDBUtil.saveOrUpdateWarnLog(message);
			// 加入列表
			synchronized (this.warnMessageMap) {
				this.warnMessageMap.put(key, message);
			}
		} else {
			WarnLog warnLog = message.getWarnLog();
			if (rec.attribute.isUpThresholdEnable()) {
				warnLog.curUpThreshold = value;
				double newValue = Double.parseDouble(value);
				double oldValue = Double.parseDouble(warnLog.maxUpThreshold);
				if (newValue > oldValue) {
					warnLog.maxUpThreshold = value;
				}
			}

			if (rec.attribute.isLowThresholdEnable()) {
				warnLog.curLowThreshold = value;
				double newValue = Double.parseDouble(value);
				double oldValue = Double.parseDouble(warnLog.minLowThreshold);
				if (newValue < oldValue) {
					warnLog.minLowThreshold = value;
				}
			}

			if (rec.attribute.isStateValueEnable()) {
				warnLog.stateValue = value;
			}
			// 记录数据库
			AlarmDBUtil.saveOrUpdateWarnLog(message);
		}
	}

	/**
	 * 关闭警告如果警告信息存在
	 * 
	 * @param nodeInfo
	 * @param rec
	 * @throws Exception
	 */
	private void closeWarnIfExist(NodeInfo nodeInfo, Recorder rec)
			throws Exception {
		// 生成Key
		StringBuilder sb = new StringBuilder();
		sb.append(nodeInfo.getHost());
		sb.append("-");
		sb.append(nodeInfo.getName());
		sb.append("-");
		sb.append(rec.attribute.getName());
		String key = sb.toString();

		Message message = null;
		synchronized (this.warnMessageMap) {
			message = this.warnMessageMap.remove(key);
		}
		if (message != null) {
			synchronized (this.historyWarnMessageList) {
				if (this.historyWarnMessageList.size() > this.maxSize) {
					int index = this.historyWarnMessageList.size() - 1;
					this.historyWarnMessageList.remove(index);
				}
				this.historyWarnMessageList.add(message);
			}
			message.warnLog.endTime = new Date();
			AlarmDBUtil.closeWarnLog(message);
		}
	}

	/**
	 * 检查
	 */
	private void check() {
		// 检查列表
		List<String> checkList = new ArrayList<String>();
		lock.lock();
		try {
			checkList.addAll(this.monitorSet);
		} finally {
			lock.unlock();
		}

		int size = checkList.size();
		if (size == 0) {
			return;
		}

		// 刷新节点信息
		refreshNode();

		try {
			for (int i = 0; i < size; i++) {
				String name = checkList.get(i);
				// 获取记录
				Recorder rec = this.recorderMap.get(name);

				// 获取属性名
				String attrName = rec.attribute.getName();
				if ("clientCount".equals(attrName)) {
					// 检查客户端数量
					this.checkClientCount(rec);
				} else if ("moneryUsage".equals(attrName)) {
					// 检查内存使用率
					this.checkMemoryUsage(rec);
				} else if ("cpuUsage".equals(name)) {
					// 检查CPU使用率
					this.checkCpuUsage(rec);
				} else if ("databaseCount".equals(name)) {
					// 检查数据库连接数
					this.checkDatabaseCount(rec);
				}
			}
		} catch (Exception e) {
			LoggerUtil.error(e.getMessage(), e);
		}

	}

	/**
	 * 获取node id
	 * 
	 * @param node
	 * @return
	 */
	private String getNodeId(Node node) {
		StringBuilder sb = new StringBuilder();
		sb.append(node.getHost());
		sb.append("_");
		sb.append(node.getPort());
		return sb.toString();
	}

	/**
	 * 检查客户端连接
	 * 
	 * @param rec
	 */
	private void checkClientCount(Recorder rec) throws Exception {
		// 上下阀值没设置，不做检查
		if (rec.upThreshold == null && rec.lowThreshold == null) {
			return;
		}

		// 遍历检查
		for (int i = 0, size = this.nodes.size(); i < size; i++) {
			// 获取节点
			Node node = this.nodes.get(i);
			// 获取节点ID
			String id = this.getNodeId(node);
			// 不连通节点不进行判断
			if (this.unconnectedNodes.contains(id)) {
				continue;
			}

			// 获取连接数
			int count = node.getClientCount();

			if (rec.upThreshold != null) {
				if (count >= (Integer) rec.upThreshold) {
					this.warn(node.getNodeInfo(), rec, String.valueOf(count),
							"BIPS接入客户端数量过多,当前客户端数量为:" + count);
				} else {
					this.closeWarnIfExist(node.getNodeInfo(), rec);
				}
			}
		}
	}

	/**
	 * 检查数据库连接数
	 * 
	 * @param rec
	 */
	private void checkDatabaseCount(Recorder rec) throws Exception {
		// 上下阀值没设置，不做检查
		if (rec.upThreshold == null && rec.lowThreshold == null) {
			return;
		}

		// 遍历检查
		for (int i = 0, size = this.nodes.size(); i < size; i++) {
			// 获取节点
			Node node = this.nodes.get(i);
			// 获取节点ID
			String id = this.getNodeId(node);
			// 不连通节点不进行判断
			if (this.unconnectedNodes.contains(id)) {
				continue;
			}

			// 获取连接数
			int count = node.getDBSourceConnectionNum("DS_BTOP");

			if (rec.upThreshold != null) {
				if (count >= (Integer) rec.upThreshold) {
					this.warn(node.getNodeInfo(), rec, String.valueOf(count),
							"BIPS连接数据库数量过多,当前连接数为:" + count);
				} else {
					this.closeWarnIfExist(node.getNodeInfo(), rec);
				}
			}
		}
	}

	/**
	 * 检查CPU使用率
	 * 
	 * @param rec
	 */
	private void checkCpuUsage(Recorder rec) throws Exception {
		// 上下阀值没设置，不做检查
		if (rec.upThreshold == null && rec.lowThreshold == null) {
			return;
		}

		// 遍历检查
		for (int i = 0, size = this.nodes.size(); i < size; i++) {
			// 获取节点
			Node node = this.nodes.get(i);
			// 获取节点ID
			String id = this.getNodeId(node);
			// 不连通节点不进行判断
			if (this.unconnectedNodes.contains(id)) {
				continue;
			}

			// 获取CPU使用率
			float cpuUsage = node.getCpuUsage();

			if (rec.upThreshold != null) {
				if (cpuUsage >= (Float) rec.upThreshold * 100) {
					this.warn(node.getNodeInfo(), rec,
							String.valueOf(cpuUsage), "BIPS CPU使用率过高,当前CPU使用率:"
									+ (cpuUsage) + "%");
				} else {
					this.closeWarnIfExist(node.getNodeInfo(), rec);
				}
			}
		}
	}

	/**
	 * 检查内存使用率
	 * 
	 * @param rec
	 */
	private void checkMemoryUsage(Recorder rec) throws Exception {
		// 上下阀值没设置，不做检查
		if (rec.upThreshold == null && rec.lowThreshold == null) {
			return;
		}

		// 遍历检查
		for (int i = 0, size = this.nodes.size(); i < size; i++) {
			// 获取节点
			Node node = this.nodes.get(i);
			// 获取节点ID
			String id = this.getNodeId(node);
			// 不连通节点不进行判断
			if (this.unconnectedNodes.contains(id)) {
				continue;
			}

			// 获取连接数
			float totalMemorySize = node.getTotalMemorySize();
			float usedMemorySize = node.getUsedMemorySize();
			float memoryUsage = usedMemorySize / totalMemorySize;

			if (rec.upThreshold != null) {
				if (memoryUsage >= (Float) rec.upThreshold) {
					this.warn(node.getNodeInfo(), rec,
							String.valueOf(memoryUsage), "BIPS内存使用率过高,当前内存使用率:"
									+ (memoryUsage * 100) + "%");
				} else {
					this.closeWarnIfExist(node.getNodeInfo(), rec);
				}

			}
		}
	}

	/**
	 * 刷新Node
	 */
	private void refreshNode() {
		lock.lock();
		try {
			if (!enable) {
				return;
			}
			// 获取所有NodeInfo
			NodeInfo[] nodeInfos = NodeQueryUtil.queryAll();
			// 新NodeInfo
			List<NodeInfo> checkNodeInfos = new ArrayList<NodeInfo>();
			for (NodeInfo nodeInfo : nodeInfos) {
				checkNodeInfos.add(nodeInfo);
			}
			List<Node> newNodes = new ArrayList<Node>();
			Set<String> unconnectedNodes = new HashSet<String>();
			for (int i = 0, size = checkNodeInfos.size(); i < size;) {
				// 判断是否找到对应的节点
				Boolean find = false;
				// 获取节点
				NodeInfo nodeInfo = checkNodeInfos.get(i);
				// 检查node
				for (int j = 0; j < nodes.size();) {
					Node node = nodes.get(j);
					if (nodeInfo.equals(node.getNodeInfo())) {
						newNodes.add(node);
						boolean isStarted = false;
						try {
							isStarted = node.isStarted();
						} catch (Exception e) {
							// do nothing
						}
						if (!isStarted) {
							// 获取node id
							String id = getNodeId(node);
							unconnectedNodes.add(id);
						}
						nodes.remove(j);
						find = true;
						break;
					} else {
						j++;
					}
				}

				if (find) {
					checkNodeInfos.remove(i);
					size--;
				} else {
					i++;
				}
			}

			// 销毁旧的节点
			for (int i = 0; i < nodes.size(); i++) {
				nodes.get(i).release();
			}

			// 创建新的节点
			for (int i = 0; i < checkNodeInfos.size(); i++) {
				NodeInfo nodeInfo = checkNodeInfos.get(i);
				Node node = new Node(nodeInfo);
				boolean isStarted = false;
				try {
					isStarted = node.isStarted();
				} catch (Exception e) {
					// do nothing
				}
				if (!isStarted) {
					// 获取node id
					String id = getNodeId(node);
					unconnectedNodes.add(id);
				}
				newNodes.add(node);
			}
			this.nodes = newNodes;
			this.unconnectedNodes = unconnectedNodes;
		} catch (Exception e) {
			LoggerUtil.error(e.getMessage(), e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 初始化属性
	 */
	private void initAttribute() {
		// 客户端连接数
		Attribute clientCountAttr = new Attribute();
		clientCountAttr.setName("clientCount");
		clientCountAttr.setClassType(Integer.class);
		clientCountAttr.setDescription("客户端连接数");
		clientCountAttr.setAttributeType(AttributeType.NUMBER);
		clientCountAttr.setUpThresholdEnable(true);

		Recorder clientRec = new Recorder();
		clientRec.attribute = clientCountAttr;
		recorderMap.put("clientCount", clientRec);
		loadAttributeValue(clientRec);

		// CPU使用率
		Attribute cpuUsageAttr = new Attribute();
		cpuUsageAttr.setName("cpuUsage");
		cpuUsageAttr.setClassType(Float.class);
		cpuUsageAttr.setDescription("CPU使用率");
		cpuUsageAttr.setAttributeType(AttributeType.PERCENT);
		cpuUsageAttr.setUpThresholdEnable(true);

		Recorder cpuRec = new Recorder();
		cpuRec.attribute = cpuUsageAttr;
		recorderMap.put("cpuUsage", cpuRec);
		loadAttributeValue(cpuRec);

		// 内存使用率
		Attribute moneryAttr = new Attribute();
		moneryAttr.setName("moneryUsage");
		moneryAttr.setClassType(Float.class);
		moneryAttr.setDescription("内存使用率");
		moneryAttr.setAttributeType(AttributeType.PERCENT);
		moneryAttr.setUpThresholdEnable(true);

		Recorder moneryRec = new Recorder();
		moneryRec.attribute = moneryAttr;
		recorderMap.put("moneryUsage", moneryRec);
		loadAttributeValue(moneryRec);

		// 数据库连接数
		Attribute databaseCountAttr = new Attribute();
		databaseCountAttr.setName("databaseCount");
		databaseCountAttr.setClassType(Integer.class);
		databaseCountAttr.setDescription("数据库连接数");
		databaseCountAttr.setAttributeType(AttributeType.NUMBER);
		databaseCountAttr.setUpThresholdEnable(true);

		Recorder datebaseRec = new Recorder();
		datebaseRec.attribute = databaseCountAttr;
		recorderMap.put("databaseCount", datebaseRec);
		loadAttributeValue(datebaseRec);
	}

	/**
	 * 加载属性值
	 * 
	 * @param attribute
	 */
	private void loadAttributeValue(Recorder rec) {
		try {
			Map<String, Object> attrInfoMap = AlarmDBUtil.getAlarmAttribute(
					name, rec.attribute.getName());
			if (attrInfoMap == null) {
				return;
			}
			String s = (String) attrInfoMap.get("upthreshold");
			if (s != null && s.length() > 0) {
				Class<?> expectType = rec.attribute.getClassType();
				Object val = TypeConverter.convert(s, expectType);
				rec.upThreshold = val;
			}

			s = (String) attrInfoMap.get("lowthreshold");
			if (s != null && s.length() > 0) {
				Class<?> expectType = rec.attribute.getClassType();
				Object val = TypeConverter.convert(s, expectType);
				rec.lowThreshold = val;
			}

			s = (String) attrInfoMap.get("statevalue");
			if (s != null && s.length() > 0) {
				rec.stateValue = "true".equalsIgnoreCase(s);
			}
			this.monitorSet.add(rec.attribute.getName());
		} catch (Exception e) {
			LoggerUtil.error(e.getMessage(), e);
		}
	}

	/**
	 * 保存或更新属性值
	 * 
	 * @param attribute
	 */
	private void flushAttributeValue(Attribute attribute) {
		try {
			AlarmDBUtil.saveOrUpdateAlarmAttribute(this, attribute);
		} catch (Exception e) {
			LoggerUtil.error(e.getMessage(), e);
		}
	}

	/**
	 * 保存或更新预警器值
	 */
	private void flush() {
		try {
			AlarmDBUtil.saveOrUpdateAlarm(this);
		} catch (Exception e) {
			LoggerUtil.error(e.getMessage(), e);
		}
	}

	/**
	 * 销毁
	 */
	public void destroy() {
		enable = false;
		// 中断线程
		checker.interrupt();
		lock.lock();
		try {
			// 销毁节点
			for (int i = 0; i < nodes.size(); i++) {
				Node node = nodes.get(i);
				node.release();
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 获取预警器名称
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 获取预警器提供的属性名称列表
	 * 
	 * @return
	 */
	public List<Attribute> getAttributes() {
		List<Attribute> attrList = new ArrayList<Attribute>();
		for (Recorder rec : this.recorderMap.values()) {
			attrList.add(rec.attribute);
		}
		return attrList;
	}

	/**
	 * 设置预警上阀值
	 * 
	 * @param name
	 * @param value
	 * 
	 * @throws Exception
	 */
	public void setUpThreshold(String name, String value) throws Exception {
		Recorder rec = this.recorderMap.get(name);
		if (rec == null) {
			throw new AttributeNotExistException("属性[" + name + "]不存在");
		}
		if (value == null || value.length() == 0) {
			rec.upThreshold = null;
		} else {
			Class<?> expectType = rec.attribute.getClassType();
			Object val = TypeConverter.convert(value, expectType);
			rec.upThreshold = val;
		}
		this.monitorSet.add(name);
		// 刷新属性值
		this.flushAttributeValue(rec.attribute);

	}

	/**
	 * 获取预警上阀值
	 * 
	 * @param name
	 * 
	 * @throws Exception
	 */
	public String getUpThreshold(String name) throws Exception {
		Recorder rec = this.recorderMap.get(name);
		if (rec == null) {
			throw new AttributeNotExistException("属性[" + name + "]不存在");
		}
		if (rec.upThreshold != null) {
			String s = String.valueOf(rec.upThreshold);
			return s;
		}
		return null;
	}

	/**
	 * 设置预警下阀值
	 * 
	 * @param name
	 * @param value
	 * 
	 * @throws Exception
	 */
	public void setLowThreshold(String name, String value) throws Exception {
		Recorder rec = this.recorderMap.get(name);
		if (rec == null) {
			throw new AttributeNotExistException("属性[" + name + "]不存在");
		}
		if (value == null || value.length() == 0) {
			rec.lowThreshold = null;
		} else {
			Class<?> expectType = rec.attribute.getClassType();
			Object val = TypeConverter.convert(value, expectType);
			rec.lowThreshold = val;
		}
		this.monitorSet.add(name);
		// 刷新属性值
		this.flushAttributeValue(rec.attribute);
	}

	/**
	 * 获取预警上阀值
	 * 
	 * @param name
	 * 
	 * @throws Exception
	 */
	public String getLowThreshold(String name) throws Exception {
		Recorder rec = this.recorderMap.get(name);
		if (rec == null) {
			throw new AttributeNotExistException("属性[" + name + "]不存在");
		}
		if (rec.lowThreshold != null) {
			String s = String.valueOf(rec.lowThreshold);
			return s;
		}
		return null;
	}

	/**
	 * 设置预警状态
	 * 
	 * @param name
	 * @param value
	 * 
	 * @throws Exception
	 */
	public void setStateValue(String name, Boolean value) throws Exception {
		Recorder rec = this.recorderMap.get(name);
		if (rec == null) {
			throw new AttributeNotExistException("属性[" + name + "]不存在");
		}
		rec.stateValue = value;
		this.monitorSet.add(name);
		// 刷新属性值
		this.flushAttributeValue(rec.attribute);
	}

	/**
	 * 设置预警状态
	 * 
	 * @param name
	 * 
	 * @throws Exception
	 */
	public Boolean getStateValue(String name) throws Exception {
		Recorder rec = this.recorderMap.get(name);
		if (rec == null) {
			throw new AttributeNotExistException("属性[" + name + "]不存在");
		}
		return rec.stateValue;
	}

	/**
	 * 把属性移除出监控队列
	 * 
	 * @param name
	 */
	public void removeAttribute(String name) {
		lock.lock();
		try {
			this.monitorSet.remove(name);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 设置通知方式
	 * 
	 * @param types
	 */
	public void setNoticeType(NoticeType[] types) {
		this.noticeTypes = types;
		// 刷新预警器值
		this.flush();
	}

	/**
	 * 获取通知方式
	 * 
	 */
	public NoticeType[] getNoticeType() {
		return this.noticeTypes;
	}

	/**
	 * 设置预警器是否启用
	 * 
	 * @param enable
	 */
	public void setEnable(boolean enable) {
		this.enable = enable;
		if (!enable) {
			this.clearWarnMessages();
			this.clearHistoryWarnMessages();
		}
		// 刷新预警器值
		this.flush();
	}

	/**
	 * 判断预警器是否可用
	 * 
	 * @return
	 */
	public boolean isEnable() {
		return this.enable;
	}

	/**
	 * 获取警告信息列表
	 * 
	 * @return
	 */
	public List<Message> getWarnMessages() {
		List<Message> list = new ArrayList<Message>();
		synchronized (this.warnMessageMap) {
			list.addAll(this.warnMessageMap.values());
		}
		return list;
	}

	/**
	 * 清空所有的当前警告信息
	 */
	public void clearWarnMessages() {
		synchronized (this.warnMessageMap) {
			this.warnMessageMap.clear();
		}
	}

	/**
	 * 根据消息ID移除消息
	 * 
	 * @param id
	 */
	public void removeWarnMessage(String id) {
		if (id == null) {
			return;
		}
		synchronized (this.warnMessageMap) {
			this.warnMessageMap.remove(id);
		}
	}

	/**
	 * 获取历史警告信息列表
	 * 
	 * @return
	 */
	public List<Message> getHistoryWarnMessages() {
		List<Message> list = new ArrayList<Message>();
		synchronized (this.historyWarnMessageList) {
			list.addAll(historyWarnMessageList);
		}
		return list;
	}

	/**
	 * 清空所有的警告历史信息
	 */
	public void clearHistoryWarnMessages() {
		synchronized (this.historyWarnMessageList) {
			this.historyWarnMessageList.clear();
		}
	}

	/**
	 * 根据消息ID移除消息
	 * 
	 * @param id
	 */
	public void removeHistoryWarnMessage(String id) {
		if (id == null) {
			return;
		}
		synchronized (this.historyWarnMessageList) {
			for (int i = 0, size = this.historyWarnMessageList.size(); i < size; i++) {
				Message message = this.historyWarnMessageList.get(i);
				if (id.equals(message.getId())) {
					this.historyWarnMessageList.remove(i);
					break;
				}

			}
		}
	}

	/**
	 * 记录
	 * 
	 * @author 江成
	 * 
	 */
	private class Recorder {

		/**
		 * 预警上阀值
		 */
		public Object upThreshold;

		/**
		 * 预警下阀值
		 */
		public Object lowThreshold;

		/**
		 * 状态值
		 */
		public Boolean stateValue;

		/**
		 * 属性
		 */
		public Attribute attribute;

	}

}
