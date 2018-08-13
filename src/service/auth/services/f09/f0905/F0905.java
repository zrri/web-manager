package service.auth.services.f09.f0905;

import both.common.util.StringUtilEx;
import java.util.List;
import service.auth.core.f09.FOX_MGR_AUTH_GROUPFUNCMAPPING_DAO;
import service.auth.core.f09.FOX_MGR_AUTH_GROUPFUNCMAPPING_DBO;
import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;
import cn.com.bankit.phoenix.trade.Service;

/**
 * 新增-FOX_AUTH_GROUPFUNCMAPPING 功能组与功能映射关系表
 */
public class F0905 extends Service<JsonRequest, JsonResponse> {

	/**
	 * 构造函数
	 */
	public F0905() {
		super();
	}

	/**
	 * execute
	 */
	public JsonResponse execute(JsonRequest request) throws Exception {
		// 从请求报文中解析参数
		// 功能组编号
		String GROUPID = request.getAsString("GROUPID");	
		// 功能编号
		String FUNCID = request.getAsString("FUNCID");	
		
		// 校验

		// 生成DBO
		FOX_MGR_AUTH_GROUPFUNCMAPPING_DBO dbo = new FOX_MGR_AUTH_GROUPFUNCMAPPING_DBO();
		// 功能组编号
		if(!StringUtilEx.isNullOrEmpty(GROUPID)){
			dbo.set_GROUPID(GROUPID);
		}
		// 功能编号
		if(!StringUtilEx.isNullOrEmpty(FUNCID)){
			dbo.set_FUNCID(FUNCID);
		}

		// 读取数据库
		List<FOX_MGR_AUTH_GROUPFUNCMAPPING_DBO> result = FOX_MGR_AUTH_GROUPFUNCMAPPING_DAO.queryTable(dbo);
		if (result == null) {
			// 失败
		} else {
			// 成功
		}

		// 拼装应答报文
		JsonResponse response = new JsonResponse();

		return response;
	}
}