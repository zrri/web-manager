package service.common.filter;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

import service.common.base.AuthGuard;
import service.common.bean.JsonRequest;

import both.common.util.LoggerUtil;
import both.db.util.DbServiceUtil;
import both.entity.pad.FoxTellerSession;
import cn.com.bankit.phoenix.session.Session;
import cn.com.bankit.phoenix.session.SessionManager;
import cn.com.bankit.phoenix.trade.filter.Event;
import cn.com.bankit.phoenix.trade.filter.Filter;

/**
 * 校验登录解析过滤器
 * 
 * @author 林立
 * 
 */
public class CheckLoginFilter extends Filter {

	/**
	 * 构造函数
	 */
	public CheckLoginFilter() {
		super();
	}

	/**
	 * before
	 */
	public void before(Event e) throws Exception {
		// 获取session
		Session session = SessionManager.getInstance()
				.getSessionFromCurrentThread();
		JsonRequest jreq = (JsonRequest) e.getInBean();
		JSONObject job = jreq.getData();
		String oldsession = job.getString("sessionid");

		// LoggerUtil.debug("进入过滤器3.0"+oldsession);
		// // 判断session是否存在
		// if (session == null) {
		// LoggerUtil.error("session不存在");
		// // 抛出异常
		// throw new Exception("unlogin");
		// }
		//
		// // 获取登录状态
		// Object loginStatus = session.getData("loginStatus");
		//
		// if (loginStatus == null) {
		// querySession(session);
		// loginStatus = session.getData("loginStatus");
		// }
		//
		// if (!Boolean.TRUE.equals(loginStatus)) {
		// LoggerUtil.error("用户尚未登录");
		// // 抛出异常
		// throw new Exception("unlogin");
		// }
		//
		// Boolean isTimeOut = AuthGuard.getInstance().isLoginTimeOut();
		//
		// if (isTimeOut == null ? true : isTimeOut) {
		// LoggerUtil.error("用户登录超出时间");
		// // 抛出异常
		// throw new Exception("unlogin");
		// }

		session.setData("userId", "999999");
		session.setData("loginStatus", true);
	}

	private void querySession(Session session) throws Exception {
		try {
			if (session == null)
				return;
			String sessionId = session.getId();
			FoxTellerSession foxTellerSession = new FoxTellerSession();
			foxTellerSession.setSessionid(sessionId);
			List<FoxTellerSession> list = DbServiceUtil
					.executeQuery(foxTellerSession);
			if (list == null || list.isEmpty()) {
				return;
			}
			FoxTellerSession tellerSession = list.get(0);
			LoggerUtil.debug("CheckLoginFilter"
					+ tellerSession.getSessiondata());
			// 设置session
			session.setData("UserInfo", tellerSession.getSessiondata());
			session.setData("loginStatus", true);
		} catch (Exception e) {
			LoggerUtil.error(e.getMessage(), e);
		}

	}

	/**
	 * after
	 */
	public void after(Event e) throws Exception {
		// do nothing
	}

}