package service.cm.core.alarm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.bankit.phoenix.plugin.IConfigElement;
import cn.com.bankit.phoenix.plugin.IExtension;
import cn.com.bankit.phoenix.plugin.IExtensionPoint;
import cn.com.bankit.phoenix.trade.boot.AbstractBoot;
import cn.com.bankit.phoenix.trade.boot.IProgressMonitor;
import cn.com.bankit.phoenix.trade.boot.Status;


/**
 * 预警器管理器
 * 
 * @author 江成
 * 
 */
public class AlarmManager extends AbstractBoot {

	/**
	 * 插件ID
	 */
	public final static String PLUGIN_ID = "cm.alarm";

	/**
	 * 日志
	 */
	private static Logger logger = LoggerFactory.getLogger(AlarmManager.class);

	/**
	 * 是否启动
	 */
	private boolean started;

	/**
	 * 预警器注册表
	 */
	private Map<String, Alarm> alarmMap = new LinkedHashMap<String, Alarm>();

	/**
	 * 预警监听器注册表
	 */
	private List<AlarmListenerNode> alarmListenerNodes = new ArrayList<AlarmListenerNode>();

	/**
	 * alarm lock
	 */
	private ReadWriteLock alarmLock = new ReentrantReadWriteLock();

	/**
	 * alarm listener lock
	 */
	private ReadWriteLock alarmListenerLock = new ReentrantReadWriteLock();

	/**
	 * 默认通知方式
	 */
	private NoticeType[] DEFAULT_NOTICETYPES = new NoticeType[] {
			NoticeType.TIP, NoticeType.MESSAGE, NoticeType.MAIL };

	/**
	 * 实例
	 */
	private static AlarmManager instance;

	/**
	 * 获取实例
	 * 
	 * @return
	 */
	public static AlarmManager getInstance() {
		return instance;
	}

	/**
	 * 启动
	 * 
	 */
	public Status start(IProgressMonitor monitor) {
		long s = System.currentTimeMillis();
		try {
			// 加载预警器
			this.loadAlarm();
			// 标示已经启动
			this.started = true;
			// 保持实例
			instance = this;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new Status(Status.FAIL,
					"AlarmManager启动失败 cause by " + e.getMessage());
		}
		logger.info("AlarmManager启动成功,耗时:" + (System.currentTimeMillis() - s)
				+ "毫秒");
		return new Status(Status.SUCCESS);
	}

	/**
	 * 加载预警器
	 */
	private void loadAlarm() throws Exception {	
		//获取class loader
		ClassLoader classLoader=this.getClass().getClassLoader();
		// 解析扩展点
		IExtensionPoint point = this.getServiceContext().getExtensionRegistry()
				.getExtensionPoint(PLUGIN_ID);
		if(point == null){
			return;
		}
		IExtension[] ext = point.getExtensions();
		for (IExtension e : ext) {
			IConfigElement[] elements = e.getConfigElements();
			for (IConfigElement se : elements) {
				String name = se.getAttribute("name");
				try {
					String className=se.getAttribute("class");
					Class<?> clazz=classLoader.loadClass(className);
					// 获取预警器
					Alarm alarm = (Alarm) clazz.newInstance();
					// 初始化预警器
					alarm.init();
					// 加入列表
					this.addAlarm(alarm);
				} catch (Exception ex) {
					logger.error("创建预警器失败,name[" + name + "]", ex);
					throw ex;
				}
			}
		}
	}

	/**
	 * 关闭
	 */
	public Status stop(IProgressMonitor monitor) {
		// 销毁预警器
		this.alarmLock.writeLock().lock();
		try {
			for (Alarm alarm : this.alarmMap.values()) {
				try {
					alarm.destroy();
				} catch (Throwable e) {
					logger.error(e.getMessage(), e);
				}

			}
		} finally {
			this.alarmLock.writeLock().unlock();
		}

		this.started = false;
		instance = null;
		return new Status(Status.SUCCESS);
	}

	/**
	 * 是否启动
	 */
	public boolean isStarted() {
		return this.started;
	}

	/**
	 * 加入alarm
	 * 
	 * @param alarm
	 */
	public void addAlarm(Alarm alarm) {
		this.alarmLock.writeLock().lock();
		try {
			this.alarmMap.put(alarm.getName(), alarm);
		} finally {
			this.alarmLock.writeLock().unlock();
		}
	}

	/**
	 * 移除alarm
	 */
	public void removeAlarm(Alarm alarm) {
		this.alarmLock.writeLock().lock();
		try {
			this.alarmMap.remove(alarm.getName());
		} finally {
			this.alarmLock.writeLock().unlock();
		}
	}

	/**
	 * 获取alarm
	 * 
	 * @param name
	 * @return
	 */
	public Alarm getAlarm(String name) {
		this.alarmLock.readLock().lock();
		try {
			Alarm alarm = this.alarmMap.get(name);
			return alarm;
		} finally {
			this.alarmLock.readLock().unlock();
		}
	}

	/**
	 * 判断指定alarm是否存在
	 * 
	 * @param alarm
	 * @return
	 */
	public boolean isAlarmExist(Alarm alarm) {
		String name = alarm.getName();
		this.alarmLock.readLock().lock();
		try {
			return alarmMap.containsKey(name);
		} finally {
			this.alarmLock.readLock().unlock();
		}
	}

	/**
	 * 获取所有alarm
	 * 
	 * @return
	 */
	public List<Alarm> getAllAlarm() {
		List<Alarm> alarms = new ArrayList<Alarm>();
		this.alarmLock.readLock().lock();
		try {
			alarms.addAll(this.alarmMap.values());
		} finally {
			this.alarmLock.readLock().unlock();
		}
		return alarms;
	}

	/**
	 * 加入alarm listener
	 * 
	 * @param listener
	 */
	public void addAlarmListener(AlarmListener listener,
			NoticeType[] noticeTypes) {
		this.alarmListenerLock.writeLock().lock();
		try {
			AlarmListenerNode node = new AlarmListenerNode();
			node.listener = listener;
			node.noticeTypes = new HashSet<NoticeType>();
			for (NoticeType type : noticeTypes) {
				node.noticeTypes.add(type);
			}
			// 加入记录
			this.alarmListenerNodes.add(node);
		} finally {
			this.alarmListenerLock.writeLock().unlock();
		}
	}

	/**
	 * 移除预警监听器
	 * 
	 * @param listener
	 */
	public void removeAlarmListener(AlarmListener listener) {
		this.alarmListenerLock.writeLock().lock();
		try {
			for (int i = 0, size = this.alarmListenerNodes.size(); i < size;) {
				AlarmListenerNode node = this.alarmListenerNodes.get(i);
				if (node.listener == listener) {
					this.alarmListenerNodes.remove(i);
					size--;
				} else {
					i++;
				}
			}
		} finally {
			this.alarmListenerLock.writeLock().unlock();
		}
	}

	/**
	 * 触发预警监听器
	 * 
	 * @param alarmName
	 * @param noticeTypes
	 * @param message
	 */
	private void fireAlarmListener(final String alarmName,
			final NoticeType[] noticeTypes, final Message message) {
		Thread thread = new Thread() {

			/**
			 * run
			 */
			public void run() {
				alarmListenerLock.readLock().lock();
				try {
					for (int i = 0, size = alarmListenerNodes.size(); i < size; i++) {
						AlarmListenerNode node = alarmListenerNodes.get(i);
						// 是否匹配
						boolean matched = false;
						for (NoticeType type : noticeTypes) {
							if (node.noticeTypes.contains(type)) {
								matched = true;
								break;
							}
						}

						if (matched) {
							try {
								node.listener.handle(alarmName, message);
							} catch (Exception e) {
								logger.error(e.getMessage(), e);
							}
						}

					}
				} finally {
					alarmListenerLock.readLock().unlock();
				}
			}
		};
		// 启动线程
		thread.start();
	}

	/**
	 * 通知
	 * 
	 * @param noticeTypes
	 * @param alarmName
	 * @param message
	 */
	void notify(NoticeType[] noticeTypes, String alarmName, Message message) {
		if (noticeTypes == null) {
			noticeTypes = DEFAULT_NOTICETYPES;
		}
		this.fireAlarmListener(alarmName, noticeTypes, message);
	}

	/**
	 * 预警监听器记录
	 * 
	 * @author 江成
	 * 
	 */
	private class AlarmListenerNode {

		/**
		 * 预警监听器
		 */
		public AlarmListener listener;

		/**
		 * 监听的支持类型
		 */
		public Set<NoticeType> noticeTypes;

	}

}
