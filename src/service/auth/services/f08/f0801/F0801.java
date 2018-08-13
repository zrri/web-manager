package service.auth.services.f08.f0801;

import both.common.util.StringUtilEx;
import service.auth.core.f08.FOX_MGR_AUTH_ROLEFUNCMAPPING_DAO;
import service.auth.core.f08.FOX_MGR_AUTH_ROLEFUNCMAPPING_DBO;
import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;
import cn.com.bankit.phoenix.trade.Service;

/**
 * 新增-FOX_AUTH_ROLEFUNCMAPPING 角色与功能映射表
 */
public class F0801 extends Service<JsonRequest, JsonResponse> {

	/**
	 * 构造函数
	 */
	public F0801() {
		super();
	}

	/**
	 * execute
	 */
	public JsonResponse execute(JsonRequest request) throws Exception {
		// 从请求报文中解析参数
		// 角色编号
		String ROLEID = request.getAsString("ROLEID");	
		// 功能编号
		String FUNCID = request.getAsString("FUNCID");	
		
		// 校验

		// 生成DBO
		FOX_MGR_AUTH_ROLEFUNCMAPPING_DBO dbo = new FOX_MGR_AUTH_ROLEFUNCMAPPING_DBO();
		// 角色编号
		if(!StringUtilEx.isNullOrEmpty(ROLEID)){
			dbo.set_ROLEID(ROLEID);
		}
		// 功能编号
		if(!StringUtilEx.isNullOrEmpty(FUNCID)){
			dbo.set_FUNCID(FUNCID);
		}

		// 读取数据库
		int result = FOX_MGR_AUTH_ROLEFUNCMAPPING_DAO.insert(dbo);
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