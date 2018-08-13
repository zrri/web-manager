package service.auth.services.f03.f0302;

import both.common.util.StringUtilEx;
import service.auth.core.f03.FOX_MGR_AUTH_TELLERROLEMAPPING_DAO;
import service.auth.core.f03.FOX_MGR_AUTH_TELLERROLEMAPPING_DBO;
import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;
import cn.com.bankit.phoenix.trade.Service;

/**
 * 新增-FOX_AUTH_USERROLEMAPPING 用户与角色映射关系表
 */
public class F0302 extends Service<JsonRequest, JsonResponse> {

	/**
	 * 构造函数
	 */
	public F0302() {
		super();
	}

	/**
	 * execute
	 */
	public JsonResponse execute(JsonRequest request) throws Exception {
		// 从请求报文中解析参数
		// 用户代号
		String USERID = request.getAsString("USERID");	
		// 角色号
		String ROLEID = request.getAsString("ROLEID");	
		
		// 校验

		// 生成DBO
		FOX_MGR_AUTH_TELLERROLEMAPPING_DBO dbo = new FOX_MGR_AUTH_TELLERROLEMAPPING_DBO();
		// 用户代号
		if(!StringUtilEx.isNullOrEmpty(USERID)){
			dbo.set_USERID(USERID);
		}
		// 角色号
		if(!StringUtilEx.isNullOrEmpty(ROLEID)){
			dbo.set_ROLEID(ROLEID);
		}
		
		// 生成条件DBO
		FOX_MGR_AUTH_TELLERROLEMAPPING_DBO wheredbo = new FOX_MGR_AUTH_TELLERROLEMAPPING_DBO();
		// 用户代号
		if(!StringUtilEx.isNullOrEmpty(USERID)){
			dbo.set_USERID(USERID);
		}
		// 角色号
		if(!StringUtilEx.isNullOrEmpty(ROLEID)){
			dbo.set_ROLEID(ROLEID);
		}

		// 读取数据库
		int result = FOX_MGR_AUTH_TELLERROLEMAPPING_DAO.update(dbo, wheredbo);
		if (result == -1) {
			// 失败
		} else {
			// 成功
		}

		// 拼装应答报文
		JsonResponse response = new JsonResponse();

		return response;
	}
}