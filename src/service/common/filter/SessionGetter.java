package service.common.filter;

import cn.com.bankit.phoenix.communication.http.HttpMessage;
import cn.com.bankit.phoenix.session.Session;
import cn.com.bankit.phoenix.session.SessionManager;

/**
 * SessionGetter
 */
public class SessionGetter {
	
	/**
	 * 获取session
	 */
	public static Session get(Object object) {
		if(object instanceof HttpMessage) {
			HttpMessage obj = (HttpMessage)object;
			String id = obj.getHeader("sessionid");
			return SessionManager.getInstance().getSessionById(id);
		} else if (object instanceof String) {
			String id = (String)object;
			return SessionManager.getInstance().getSessionById(id);
		} else {
			return SessionManager.getInstance().getSessionFromCurrentThread();
		}
	}
}
