package service.cm.core.alarm.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import service.cm.core.alarm.Alarm;
import service.cm.core.alarm.Attribute;
import service.cm.core.alarm.Message;
import service.cm.core.alarm.NoticeType;
import service.cm.core.alarm.WarnLog;
import cn.com.bankit.phoenix.jdbc.tool.DBAccessor;

/**
 * 
 * @author 江成
 * 
 */
public class AlarmDBUtil {

	/**
	 * 日志格式化
	 */
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * 保存或更新预警器信息（如果预警器信息已经存在就更新信息，否则保存信息）
	 * 
	 * @param alarm
	 * @return
	 */
	public synchronized static boolean saveOrUpdateAlarm(Alarm alarm)
			throws Exception {
		// 获取数据库访问器
		DBAccessor dbAccessor = DBAccessor.getDBAccessor();

		StringBuilder sb = new StringBuilder();
		sb.append("select name from t_alarm where name='");
		sb.append(alarm.getName());
		sb.append("'");
		// 探测记录是否存在
		List<?> res = dbAccessor.query(sb.toString());
		// 如果list大小为1，代表记录已经存在
		if (res.size() == 1) {
			sb = new StringBuilder();
			sb.append("update t_alarm set enable='");
			sb.append(alarm.isEnable());
			sb.append("'");
			NoticeType[] noticeTypes = alarm.getNoticeType();
			if (noticeTypes!=null&&noticeTypes.length > 0) {
				sb.append(",noticetypes='");
				for (int i = 0; i < noticeTypes.length; i++) {
					sb.append(noticeTypes[i].getName());
					if (i < noticeTypes.length - 1) {
						sb.append(",");
					}
				}
				sb.append("'");
			}
			sb.append(" where name='");
			sb.append(alarm.getName());
			sb.append("'");
			int n = dbAccessor.update(sb.toString());
			return n == 1;
		} else {
			sb = new StringBuilder();
			sb.append("insert into t_alarm(name,enable,noticetypes) values('");
			sb.append(alarm.getName());
			sb.append("','");
			sb.append(alarm.isEnable());
			sb.append("'");
			NoticeType[] noticeTypes = alarm.getNoticeType();
			if (noticeTypes!=null && noticeTypes.length > 0) {
				sb.append(",'");
				for (int i = 0; i < noticeTypes.length; i++) {
					sb.append(noticeTypes[i].getName());
					if (i < noticeTypes.length - 1) {
						sb.append(",");
					}
				}
				sb.append("'");
			}else{
				sb.append(", null");
			}
			sb.append(")");
			dbAccessor.execute(sb.toString());
			return true;
		}
	}

	/**
	 * 获取预警器信息
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public synchronized static Map<String, Object> getAlarm(String name)
			throws Exception {
		// 获取数据库访问器
		DBAccessor dbAccessor = DBAccessor.getDBAccessor();

		StringBuilder sb = new StringBuilder();
		sb.append("select enable,noticetypes from t_alarm where name='");
		sb.append(name);
		sb.append("'");

		List<Map<String, Object>> res = dbAccessor.query(sb.toString());
		if (res.size() == 1) {
			return res.get(0);
		}
		return null;
	}

	/**
	 * 保存或更新预警器属性信息（如果预警器属性信息已经存在就更新信息，否则保存信息）
	 * 
	 * @param alarmName
	 * @param attribute
	 * @return
	 */
	public synchronized static boolean saveOrUpdateAlarmAttribute(Alarm alarm,
			Attribute attribute) throws Exception {
		// 获取数据库访问器
		DBAccessor dbAccessor = DBAccessor.getDBAccessor();

		String name = attribute.getName();
		StringBuilder sb = new StringBuilder();
		sb.append("select name from t_alarm_attribute where name='");
		sb.append(attribute.getName());
		sb.append("' and alarmname='");
		sb.append(alarm.getName());
		sb.append("'");
		// 探测记录是否存在
		List<?> res = dbAccessor.query(sb.toString());
		// 如果list大小为1，代表记录已经存在
		if (res.size() == 1) {
			sb = new StringBuilder();
			sb.append("update t_alarm_attribute set upthreshold='");
			String attrVal = alarm.getUpThreshold(name);
			if (attrVal == null) {
				attrVal = "";
			}
			sb.append(attrVal);
			sb.append("',lowthreshold='");
			attrVal = alarm.getLowThreshold(name);
			if (attrVal == null) {
				attrVal = "";
			}
			sb.append(attrVal);
			sb.append("',statevalue='");
			Boolean stateVal = alarm.getStateValue(name);
			if (stateVal == null) {
				sb.append("");
			}else{
				sb.append(stateVal);
			}
			sb.append("'");
			sb.append(" where name='");
			sb.append(attribute.getName());
			sb.append("' and alarmname='");
			sb.append(alarm.getName());
			sb.append("'");
			int n = dbAccessor.update(sb.toString());
			return n == 1;
		} else {
			sb = new StringBuilder();
			sb.append("insert into t_alarm_attribute(name,alarmname,upthreshold,lowthreshold,statevalue) values('");
			sb.append(name);
			sb.append("','");
			sb.append(alarm.getName());
			sb.append("','");
			String attrVal = alarm.getUpThreshold(name);
			if (attrVal == null) {
				attrVal = "";
			}
			sb.append(attrVal);
			sb.append("','");
			attrVal = alarm.getLowThreshold(name);
			if (attrVal == null) {
				attrVal = "";
			}
			sb.append(attrVal);
			sb.append("','");
			Boolean val = alarm.getStateValue(name);
			if (val == null) {
				sb.append("");
			} else {
				sb.append(alarm.getStateValue(name));
			}
			sb.append("')");
			dbAccessor.execute(sb.toString());
		}
		return true;
	}

	/**
	 * 获取预警器属性信息
	 * 
	 * @param alarmName
	 * @param name
	 * @return
	 */
	public synchronized static Map<String, Object> getAlarmAttribute(
			String alarmName, String name) throws Exception {
		// 获取数据库访问器
		DBAccessor dbAccessor = DBAccessor.getDBAccessor();

		StringBuilder sb = new StringBuilder();
		sb.append("select upthreshold,lowthreshold,statevalue from t_alarm_attribute where name='");
		sb.append(name);
		sb.append("' and alarmname='");
		sb.append(alarmName);
		sb.append("'");

		List<Map<String, Object>> res = dbAccessor.query(sb.toString());
		if (res.size() == 1) {
			return res.get(0);
		}
		return null;
	}

	/**
	 * 保存或更新预警器属性信息（如果预警器属性信息已经存在就更新信息，否则保存信息）
	 * 
	 * @param alarmName
	 * @param attribute
	 * @return
	 */
	public synchronized static boolean saveOrUpdateWarnLog(Message message)
			throws Exception {
		// 获取数据库访问器
		DBAccessor dbAccessor = DBAccessor.getDBAccessor();
		// 获取警告信息
		WarnLog warnLog = message.getWarnLog();
		StringBuilder sb = new StringBuilder();
		sb.append("select name from t_alarm_log where id='");
		sb.append(message.getId());
		sb.append("'");

		// 拼装消息
		StringBuilder msg = new StringBuilder();
		Map<String, Object> metadata = message.getMetadata();
		for (String key : metadata.keySet()) {
			msg.append(key);
			msg.append(":");
			msg.append(metadata.get(key));
			msg.append("\n");
		}
		msg.append(message.getContent());

		// 探测记录是否存在
		List<?> res = dbAccessor.query(sb.toString());
		// 如果list大小为1，代表记录已经存在
		if (res.size() == 1) {
			sb = new StringBuilder();
			sb.append("update t_alarm_log set lastupdatetime='");
			sb.append(dateFormat.format(new Date()));
			sb.append("',message='");
			sb.append(msg.toString());
			sb.append("'");

			if (warnLog.maxUpThreshold != null) {
				sb.append(" ,maxupthreshold='");
				sb.append(warnLog.maxUpThreshold);
				sb.append("',curupthreshold='");
				sb.append(warnLog.curUpThreshold);
				sb.append("'");
			}

			if (warnLog.minLowThreshold != null) {
				sb.append(" ,minlowthreshold='");
				sb.append(warnLog.minLowThreshold);
				sb.append("',curlowthreshold='");
				sb.append(warnLog.curLowThreshold);
				sb.append("'");
			}

			if (warnLog.stateValue != null) {
				sb.append(" ,statevalue='");
				sb.append(warnLog.stateValue);
				sb.append("'");
			}
			sb.append(" where id='");
		    sb.append(message.getId());
		    sb.append("'");
			int n = dbAccessor.update(sb.toString());
			return n == 1;
		} else {
			String curTime = dateFormat.format(new Date());
			sb = new StringBuilder();
			if (warnLog.maxUpThreshold != null) {
				sb.append("insert into t_alarm_log(id,name,alarmname,starttime,lastupdatetime,maxupthreshold,curupthreshold,message) values('");
				sb.append(message.getId());
				sb.append("','");
				sb.append(warnLog.attrName);
				sb.append("','");
				sb.append(warnLog.alarmName);
				sb.append("','");
				sb.append(dateFormat.format(warnLog.startTime));
				sb.append("','");
				sb.append(curTime);
				sb.append("','");
				sb.append(warnLog.maxUpThreshold);
				sb.append("','");
				sb.append(warnLog.curUpThreshold);
				sb.append("','");
				sb.append(msg.toString());
				sb.append("'");
			} else if (warnLog.minLowThreshold != null) {
				sb.append("insert into t_alarm_log(id,name,alarmname,starttime,lastupdatetime,minlowthreshold,curlowthreshold,message) values('");
				sb.append(message.getId());
				sb.append("','");
				sb.append(warnLog.attrName);
				sb.append("','");
				sb.append(warnLog.alarmName);
				sb.append("','");
				sb.append(curTime);
				sb.append("','");
				sb.append(curTime);
				sb.append("','");
				sb.append(warnLog.minLowThreshold);
				sb.append("','");
				sb.append(warnLog.curLowThreshold);
				sb.append("','");
				sb.append(msg.toString());
				sb.append("'");
			} else if (warnLog.stateValue != null) {
				sb.append("insert into t_alarm_log(id,name,alarmname,starttime,lastupdatetime,statevalue,message) values('");
				sb.append(message.getId());
				sb.append("','");
				sb.append(warnLog.attrName);
				sb.append("','");
				sb.append(warnLog.alarmName);
				sb.append("','");
				sb.append(curTime);
				sb.append("','");
				sb.append(curTime);
				sb.append("','");
				sb.append(warnLog.stateValue);
				sb.append("','");
				sb.append(msg.toString());
				sb.append("'");
			}
			sb.append(")");
			dbAccessor.execute(sb.toString());
		}
		return true;
	}
	
	/**
	 * 关闭警告日志记录
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public synchronized static boolean closeWarnLog(Message message) throws Exception{
		
		// 获取数据库访问器
		DBAccessor dbAccessor = DBAccessor.getDBAccessor();
		
		StringBuilder sb = new StringBuilder();
		sb.append("select name from t_alarm_log where id='");
		sb.append(message.getId());
		sb.append("'");
		

		// 探测记录是否存在
		List<?> res = dbAccessor.query(sb.toString());
		// 如果list大小为1，代表记录已经存在
		if (res.size() == 1) {
			sb = new StringBuilder();
			sb.append("update t_alarm_log set endtime='");
			sb.append(dateFormat.format(message.getWarnLog().endTime));
			sb.append("'");
			sb.append(" where id='");
		    sb.append(message.getId());
		    sb.append("'");
			int n = dbAccessor.update(sb.toString());
		    return n==1;
		}
		return false;
	}
}
