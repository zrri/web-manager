package service.cm.core.alarm;

import java.util.Map;

/**
 * 消息
 * 
 * @author 江成
 * 
 */
public class Message {
	
	/**
	 * ID
	 */
	public String id;

	/**
	 * 元信息
	 */
	public Map<String, Object> metadata;

	/**
	 * 消息内容
	 */
	public String content;
	
	/**
	 * 警告日志
	 */
	public WarnLog warnLog;
	
	/**
	 * 设置ID
	 * @param id
	 */
	public void setId(String id){
		this.id=id;
	}
	
    /**
     * 获取ID
     * @return
     */
	public String getId(){
		return this.id;
	}

	/**
	 * 获取元信息
	 * 
	 * @return
	 */
	public Map<String, Object> getMetadata() {
		return metadata;
	}

	/**
	 * 设置元信息
	 * 
	 * @param metadata
	 */
	public void setMetadata(Map<String, Object> metadata) {
		this.metadata = metadata;
	}

	/**
	 * 获取内容
	 * 
	 * @return
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置内容
	 * 
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 获取警告日志
	 * @return
	 */
	public WarnLog getWarnLog() {
		return warnLog;
	}

	/**
	 * 设置预警日志
	 * @param warnLog
	 */
	public void setWarnLog(WarnLog warnLog) {
		this.warnLog = warnLog;
	}
}
