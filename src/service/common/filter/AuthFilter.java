package service.common.filter;

import both.common.util.LoggerUtil;
import cn.com.bankit.phoenix.session.Session;
import cn.com.bankit.phoenix.session.SessionManager;
import cn.com.bankit.phoenix.trade.filter.Event;
import cn.com.bankit.phoenix.trade.filter.Filter;

/**
 * 消息解析过滤器
 * 
 * @author 江成
 * 
 */
public class AuthFilter extends Filter {

	/**
	 * 构造函数
	 */
	public AuthFilter() {
		super();
	}

	/**
	 * before
	 */
	public void before(Event e) throws Exception {
		// 获取session
		Session session = SessionManager.getInstance()
				.getSessionFromCurrentThread();

		// 判断session是否存在
		if (session == null) {
			LoggerUtil.error("session不存在");
			// 抛出异常
			throw new Exception("unlogin");
		}

		// 获取登录状态
		Object loginStatus = session.getData("loginStatus");

		if (!Boolean.TRUE.equals(loginStatus)) {
			LoggerUtil.error("用户尚未登录");
			// 抛出异常
			throw new Exception("unlogin");
		}

		session.setData("tellerId", session.getData("tellerId"));
		session.setData("orgId", session.getData("orgId"));
	}

	/**
	 * after
	 */
	public void after(Event e) throws Exception {
		// do nothing
	}

}