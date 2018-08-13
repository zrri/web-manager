package service.common.websocket;

import java.util.List;
import java.util.Map;

import service.cm.core.db.FOX_MGR_AUTH_ONLINETELLER_DBO;

import both.common.util.LoggerUtil;
import both.common.util.StringUtilEx;
import both.db.util.DbServiceUtil;

import com.alibaba.fastjson.JSONObject;

import cn.com.bankit.phoenix.session.Session;
import cn.com.bankit.phoenix.session.SessionManager;
import cn.com.bankit.phoenix.trade.Service;
import cn.com.bankit.phoenix.trade.delegate.IMessageSenderDelegate;

/**
 * websocket 消息通知工具
 * 
 * @author liaozhijie
 * 
 */
public class NotifyUtil {

	/**
	 * 
	 */
	public NotifyUtil() {
		super();
	}

	/**
	 * 消息推送
	 * 
	 * @param userId
	 *            用户号
	 * @param msgName
	 *            子服务名
	 * @param json
	 *            数据
	 * @throws Exception
	 */
	public static void push(Service service, String userId, String msgName,
			JSONObject args) throws Exception {

		JSONObject json = new JSONObject();
		json.put("msgName", msgName);
		json.put("msg", args);

		String msg = json.toString();
		// msg = "122";
		String address = "";
		// Session session = SessionManager.getInstance()
		// .getSessionFromCurrentThread();
		//
		// if ((Boolean) session.getData("loginStatus")
		// && userId.equals(session.getData("userId"))
		// && !StringUtilEx.isNullOrEmpty(session.getData("clientIp"))) {
		// address = (String) session.getData("clientIp");
		// } else {
		// 1.查询在线柜员表
		FOX_MGR_AUTH_ONLINETELLER_DBO onLineUser = new FOX_MGR_AUTH_ONLINETELLER_DBO();
		onLineUser.set_USERID(userId);
		LoggerUtil.debug("查询在线用户表");
		List<FOX_MGR_AUTH_ONLINETELLER_DBO> list = DbServiceUtil
				.executeQuery(onLineUser);
		// 查询无记录
		if (list == null || list.size() == 0) {
			String error = "用户" + userId + "不在线";
			LoggerUtil.error(msg);
			throw new Exception(msg);
		}

		onLineUser = list.get(0);
		address = onLineUser.get_CLIENTIP();
		// }

		if (StringUtilEx.isNullOrEmpty(address)) {
			String error = "用户" + userId + "地址未找到";
			LoggerUtil.error(msg);
			throw new Exception(msg);
		}
		LoggerUtil.debug("查询在线用户表完成:" + address);
		// 获取message sender
		IMessageSenderDelegate messageSender = (IMessageSenderDelegate) service
				.getDelegate(IMessageSenderDelegate.class);
		// 发送消息
		messageSender.sendMessage2Trade(address, "http", "http", msg);
		LoggerUtil.debug("发送消息完成:" + address);
	}
}