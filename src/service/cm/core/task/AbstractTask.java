package service.cm.core.task;

import java.util.List;
import java.util.Map;

import cn.com.bankit.phoenix.trade.Service;

public abstract class AbstractTask implements ITask {

	protected String userId = "";
	protected Service service;
	protected List<Map<String, String>> list;

	/**
	 * 节点已启动
	 */
	protected String NodeStarted = "01";

	/**
	 * 节点已关闭
	 */
	protected String NodeStoped = "02";

	/**
	 * 节点不存在
	 */
	protected String NodeEmpty = "03";

	/**
	 * 节点连接异常
	 */
	protected String NodeLinkError = "04";

	public abstract void run();

}