package service.cm.core.alarm;

import java.util.Date;

/**
 * 警告日志
 * 
 * @author 江成
 *
 */
public class WarnLog {

	/**
	 * 预警器名称
	 */
	public String alarmName;
	
	/**
	 * 属性名
	 */
	public String attrName;
	
	/**
	 * 开始时间
	 */
	public Date startTime;
	
	/**
	 * 结束时间
	 */
	public Date endTime;
	
	/**
	 * 最大上阀值
	 */
	public String maxUpThreshold;
	
	/**
	 * 当前上阀值
	 */
	public String curUpThreshold;
	
	/**
	 * 最小下阀值
	 */
	public String minLowThreshold;
	
	/**
	 * 当前下阀值
	 */
	public String curLowThreshold;
	
	/**
	 * 状态值
	 */
	public String stateValue;
}
