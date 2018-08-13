package service.common.websocket;

import com.alibaba.fastjson.JSONObject;

import both.common.util.LoggerUtil;
import both.common.util.StringUtilEx;
import both.constants.IResponseConstant;
import both.db.util.DbServiceUtil;
import service.cm.core.db.FOX_MGR_AUTH_ONLINETELLER_DBO;
import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;
import cn.com.bankit.phoenix.session.Session;
import cn.com.bankit.phoenix.session.SessionManager;
import cn.com.bankit.phoenix.trade.Service;
import cn.com.bankit.phoenix.trade.delegate.IMessageSenderDelegate;

public class WebSocketService extends Service<JsonRequest, JsonResponse> {

	@Override
	public JsonResponse execute(JsonRequest arg0) throws Exception {
		return null;
	}

	/**
	 * 注册websocket地址
	 * 
	 * @param request
	 * @return
	 */
	public JsonResponse registWebSocket(JsonRequest request) {

		JsonResponse response = new JsonResponse();

		String userId = request.getAsString("userId");
		String address = request.getAsString("address");

		LoggerUtil.debug("------>开始注册websocket");
		LoggerUtil.debug("接受数据:" + request + " , address:" + address);

		if (StringUtilEx.isNullOrEmpty(userId)) {
			response.put(IResponseConstant.retCode, IResponseConstant.FAILED);
			response.put(IResponseConstant.retMsg, "注册websocket，userId为空");
			return response;
		}
		if (StringUtilEx.isNullOrEmpty(address)) {
			response.put(IResponseConstant.retCode, IResponseConstant.FAILED);
			response.put(IResponseConstant.retMsg, "注册websocket，address为空");
			return response;
		}

		FOX_MGR_AUTH_ONLINETELLER_DBO where = new FOX_MGR_AUTH_ONLINETELLER_DBO();
		where.set_USERID(userId);

		FOX_MGR_AUTH_ONLINETELLER_DBO set = new FOX_MGR_AUTH_ONLINETELLER_DBO();
		set.set_CLIENTIP(address);

		try {
			DbServiceUtil.executeUpdate(set, where);
		} catch (Exception e) {
			response.put(IResponseConstant.retCode, IResponseConstant.FAILED);
			response.put(IResponseConstant.retMsg, "更新在线用户表数据表失败");
			return response;
		}
		
		Session session = SessionManager.getInstance().getSessionFromCurrentThread();
		session.setData("clientIp", address);
		
		// 获取message sender
		IMessageSenderDelegate messageSender = this
				.getDelegate(IMessageSenderDelegate.class);
		// address="H_192.168.1.106:8871|H_127.0.0.1:16288";
		// address="H_*";
		// 发送消息给web socket client端

		JSONObject jsonObj = new JSONObject();

		jsonObj.put("msgName", "first");
		jsonObj.put("msg", "{}");

		try {
			messageSender.sendMessage2Trade(address, "http", "http",
					jsonObj.toString());
		} catch (Exception e1) {
			response.put(IResponseConstant.retCode, IResponseConstant.FAILED);
			response.put(IResponseConstant.retMsg, "向客户端发送消息失败,address="
					+ address);
			return response;
		}

		response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
		response.put(IResponseConstant.retMsg, "成功");
		return response;	
	}

}