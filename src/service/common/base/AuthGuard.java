package service.common.base;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import both.common.util.DateUtil;
import both.common.util.LoggerUtil;
import both.db.util.DbServiceUtil;
import both.entity.pad.FoxTellerSession;

import cn.com.bankit.phoenix.communication.CommunicationController;
import cn.com.bankit.phoenix.communication.IMessageService;
import cn.com.bankit.phoenix.communication.IReportMonitor;
import cn.com.bankit.phoenix.communication.MessageHandler;
import cn.com.bankit.phoenix.communication.RequestMessage;
import cn.com.bankit.phoenix.communication.ResponseMessage;
import cn.com.bankit.phoenix.communication.constant.ContentType;
import cn.com.bankit.phoenix.communication.constant.MessageType;
import cn.com.bankit.phoenix.communication.server.util.ClusterAddressLoader;
import cn.com.bankit.phoenix.session.Session;
import cn.com.bankit.phoenix.session.SessionManager;

/**
 * 权限守卫
 * 
 * @author 江成
 * 
 */
public class AuthGuard {

	/**
	 * 服务名
	 */
	private static String SERVER_NAME = "authServer";

	/**
	 * 连接超时时间
	 */
	private long connectedTimeout = 5000;

	/**
	 * 通信超时时间
	 */
	private long timeout = 15000;

	/**
	 * 登录信息有效时间(5分钟)
	 */
	private long validTimetout = 1000 * 60 * 5;

	/**
	 * 实例
	 */
	private static AuthGuard instance = new AuthGuard();

	/**
	 * 获取实例
	 * 
	 * @return
	 */
	public static AuthGuard getInstance() {
		return instance;
	}

	/**
	 * 构造函数
	 */
	public AuthGuard() {
		super();
		// 获取通信控制器
		CommunicationController commController = CommunicationController
				.getInstance();
		// 获取登录服务
		MessageHandler messageHandler = commController
				.getMessageHandler(SERVER_NAME);
		
		LoggerUtil.debug("正在注销session  auth");
		// 注册注销任务
		messageHandler.addMessageService("unlogin", new IMessageService() {

			/**
			 * run
			 */
			public ResponseMessage run(String source, RequestMessage reqMsg,
					IReportMonitor monitor) throws Exception {
				// 获取session Id
				String sessionId = (String) reqMsg.getContent();
				LoggerUtil.info("收到通知：" + source + " sessionId:" + sessionId);
				// 获取session管理器
				SessionManager sessionManager = SessionManager.getInstance();
				// 根据ID获取session
				Session session = sessionManager.getSessionById(sessionId);
				if (session != null) {
					// 清空session 数据
					session.clearData();
					LoggerUtil.info("接收到消息 source[" + source
							+ "],清空session数据成功 id[" + sessionId + "]");
					System.out.println("接收到消息 source[" + source
							+ "],清空session数据成功 id[" + sessionId + "]");// debug
				} else {
					LoggerUtil.info("接收到消息 source[" + source
							+ "],session不存在 id[" + sessionId + "]");
					System.out.println("接收到消息 source[" + source
							+ "],session不存在 id[" + sessionId + "]");// debug
				}

				// 返回成功处理
				ResponseMessage rspMsg = new ResponseMessage("success",
						ContentType.String_UTF8);
				return rspMsg;
			}

		});
	}

	/**
	 * 获取登录信息有效时间
	 * 
	 * @return
	 */
	public long getValidTimetout() {
		return validTimetout;
	}

	/**
	 * 设置登录有效时间
	 * 
	 * @param validTimetout
	 */
	public void setValidTimetout(long validTimetout) {
		this.validTimetout = validTimetout;
	}

	/**
	 * 获取连接超时时间
	 * 
	 * @return
	 */
	public long getConnectedTimeout() {
		return connectedTimeout;
	}

	/**
	 * 设置连接超时时间
	 * 
	 * @param connectedTimeout
	 */
	public void setConnectedTimeout(long connectedTimeout) {
		this.connectedTimeout = connectedTimeout;
	}

	/**
	 * 获取通信超时时间
	 * 
	 * @return
	 */
	public long getTimeout() {
		return timeout;
	}

	/**
	 * 设置通信超时时间
	 * 
	 * @param timeout
	 */
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	/**
	 * 广播
	 * 
	 * @param data
	 * @return
	 */
	public boolean broadcast(final String data) throws Exception {
		// 获取集群地址加载器
		ClusterAddressLoader clusterAddrLoader = ClusterAddressLoader
				.getInstance();
		// 获取集群地址加载器
		List<String> list = clusterAddrLoader.getAddressList();

		LoggerUtil.error("登出通知，list.size():" + list.size());

		// 获取通信控制器
		CommunicationController commController = CommunicationController
				.getInstance();

		// 获取登录服务
		final MessageHandler messageHandler = commController
				.getMessageHandler(SERVER_NAME);

		LoggerUtil.error("登出通知，getLocalAddress:"
				+ messageHandler.getLocalAddress());

		// 循环通知
		for (int i = 0, size = list.size(); i < size; i++) {

			try {
				// 获取地址
				final String address = list.get(i);
				new Thread(new Runnable() {
					public void run() {
						try {
							LoggerUtil.error("登出通知，address:" + address
									+ " data:" + data);
							// 发送消息
							messageHandler.asyncSend(MessageType.Request,
									address, "authServer", "unlogin", data,
									ContentType.String_UTF8);
						} catch (Exception e) {
							LoggerUtil.error(e.getMessage(), e);
						}
					}
				}).start();

				// String s = (String) messageHandler.syncSend(
				// MessageType.Request, address, "authServer", "unlogin",
				// data, ContentType.String_UTF8, connectedTimeout,
				// timeout);
				// if (!"success".equals(s)) {
				// LoggerUtil.error("登出通知失败，address:" + address);
				// }
			} catch (Exception e) {
				LoggerUtil.error(e.getMessage(), e);
			}
		}
		return true;
	}

	/**
	 * 登录
	 * 
	 * @param map
	 * @return
	 */
	public boolean login(Map<String, Object> map) throws Exception {

		// ----------步骤一-------------
		// 把登录消息保存到数据库(代码待添加)

		// ----------步骤二--------------
		// 获取session管理器
		SessionManager sessionManager = SessionManager.getInstance();
		// 根据当前线程获取session
		Session session = sessionManager.getSessionFromCurrentThread();
		// 把登录消息保存到session
		for (String key : map.keySet()) {
			Object value = map.get(key);
			session.setData(key, value);
		}
		return true;
	}

	/**
	 * 登出
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public boolean logout(Map<String, Object> map) throws Exception {

		// ----------步骤一-------------
		// 更新数据库(代码待添加)
		LoggerUtil.debug("正在注销session  logout");
		// ----------步骤二--------------
		// 获取session管理器
		SessionManager sessionManager = SessionManager.getInstance();
		// 根据当前线程获取session
		Session session = sessionManager.getSessionFromCurrentThread();
		// 清空登录数据
		session.clearData();
		// -----------步骤三-------------
		// 获取session id
		String sessionId = session.getId();
		this.broadcast(sessionId);

		// 返回正确结果
		return true;

	}

	/**
	 * 判断当前线程的请求是否已经登录
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public boolean isLoginTimeOut() throws Exception {

		// ------------步骤一-------------
		// 获取session管理器
		SessionManager sessionManager = SessionManager.getInstance();
		// 根据当前线程获取session
		Session session = sessionManager.getSessionFromCurrentThread();

		// 获取登录信息更新时间
		Long updateTime = (Long) session.getData("loginDataUpdateTime") == null ? System
				.currentTimeMillis() : (Long) session
				.getData("loginDataUpdateTime");
		// 相差时间
		Long validTime = System.currentTimeMillis() - updateTime;

		LoggerUtil.debug("validTime" + validTime);
		// 判断登录信息是否处于有效时间内
		if (updateTime != null && validTime < this.validTimetout) {

			// 获取登录状态
			boolean login = (Boolean) session.getData("loginStatus");
			// 设置登录信息更新时间
			session.setData("loginDataUpdateTime", System.currentTimeMillis());
			if (login) {
				return false;
			}
		} else if (updateTime != null && validTime >= this.validTimetout) {
			// 获取登录状态
			boolean login = (Boolean) session.getData("loginStatus");
			// 设置登录信息更新时间
			session.setData("loginDataUpdateTime", System.currentTimeMillis());

			if (login) {
				return true;
			}
		}

		// ------------步骤二--------------
		// 如果session中没有登录信息，则从数据库中获取，并
		// 更新session中的信息

		if (updateTime == null) {
			return loginTimeOutCheck(session);
		}

		return false;

	}

	/**
	 * 用户登录超时检查
	 * 
	 * @param foxTellerSession
	 * @return
	 * @throws Exception
	 * @throws ParseException
	 */
	private Boolean loginTimeOutCheck(Session session) throws Exception {
		String sessionId = session.getId();
		FoxTellerSession foxTellerSession = new FoxTellerSession();
		foxTellerSession.setSessionid(sessionId);
		List<FoxTellerSession> list = DbServiceUtil
				.executeQuery(foxTellerSession);
		if (list.size() == 0 || list.isEmpty()) {
			return true;
		}

		// session时间
		Long sessionTime = new SimpleDateFormat("yyyyMMddHHmm")
				.parse(list.get(0).getOperationdate()
						+ list.get(0).getOperationtime()).getTime();
		// 系统时间
		Long systemTime = System.currentTimeMillis();

		Long validTime = systemTime - sessionTime;

		if (validTime >= this.validTimetout) {
			// 清空登录数据
			LoggerUtil.debug("正在注销session  timeout");
			session.clearData();
			// 广播其它服务器
			AuthGuard.getInstance().logout(new HashMap<String,Object>());
			// 删除session
			DbServiceUtil.executeDelete(foxTellerSession);
			return true;
		} else {
			// 设置登录信息更新时间
			session.setData("loginDataUpdateTime", System.currentTimeMillis());
			// 更新session时间
			FoxTellerSession setSender = new FoxTellerSession();
			setSender.setOperationdate(DateUtil.getDateString());
			setSender.setOperationtime(DateUtil.getTimeString());
			DbServiceUtil.executeUpdate(setSender, foxTellerSession);
			return false;
		}
	}

}