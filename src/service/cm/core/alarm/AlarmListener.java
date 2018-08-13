package service.cm.core.alarm;

/**
 * 警告监听器
 * 
 * @author 江成
 * 
 */
public interface AlarmListener {

	/**
	 * 处理消息
	 * 
	 * @param alarmName
	 * @param message
	 */
	public void handle(String alarmName, Message message);
}
