package service.cm.core.alarm;

import java.util.List;

/**
 * 预警器
 * 
 * @author 江成
 * 
 */
abstract public class Alarm {

	/**
	 * 初始化
	 */
	public void init() {

	}

	/**
	 * 销毁
	 */
	public void destroy() {

	}

	/**
	 * 获取预警器名称
	 * 
	 * @return
	 */
	abstract public String getName();

	/**
	 * 获取预警器提供的属性名称列表
	 * 
	 * @return
	 */
	abstract public List<Attribute> getAttributes();

	/**
	 * 设置预警上阀值
	 * 
	 * @param name
	 * @param value
	 * 
	 * @throws Exception
	 */
	abstract public void setUpThreshold(String name, String value)
			throws Exception;

	/**
	 * 获取预警上阀值
	 * 
	 * @param name
	 * 
	 * @throws Exception
	 */
	abstract public String getUpThreshold(String name) throws Exception;

	/**
	 * 设置预警上阀值
	 * 
	 * @param name
	 * @param value
	 * 
	 * @throws Exception
	 */
	abstract public void setLowThreshold(String name, String value)
			throws Exception;

	/**
	 * 获取预警上阀值
	 * 
	 * @param name
	 * 
	 * @throws Exception
	 */
	abstract public String getLowThreshold(String name) throws Exception;

	/**
	 * 设置预警状态
	 * 
	 * @param name
	 * @param value
	 * 
	 * @throws Exception
	 */
	abstract public void setStateValue(String name, Boolean value)
			throws Exception;

	/**
	 * 设置预警状态
	 * 
	 * @param name
	 * 
	 * @throws Exception
	 */
	abstract public Boolean getStateValue(String name) throws Exception;

	/**
	 * 把属性移除出监控队列
	 * 
	 * @param name
	 */
	abstract public void removeAttribute(String name);

	/**
	 * 设置预警器是否启用
	 * 
	 * @param enable
	 */
	abstract public void setEnable(boolean enable);

	/**
	 * 判断预警器是否可用
	 * 
	 * @return
	 */
	abstract public boolean isEnable();

	/**
	 * 设置通知方式
	 * 
	 * @param types
	 */
	abstract public void setNoticeType(NoticeType[] types);
	
	/**
	 * 获取通知方式
	 * 
	 */
	abstract public NoticeType[] getNoticeType();
	
	/**
	 * 获取警告信息列表
	 * @return
	 */
	abstract public List<Message> getWarnMessages();
	
	/**
	 * 获取历史警告信息列表
	 * @return
	 */
	abstract public List<Message> getHistoryWarnMessages();
	
	/**
	 * 清空所有的警告历史信息
	 */
	abstract public void clearHistoryWarnMessages();
	
	/**
	 * 根据消息ID移除消息
	 * @param id
	 */
	abstract public void removeHistoryWarnMessage(String id);

}
