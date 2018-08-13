package service.auth.services.f07.f0703;

import both.common.util.StringUtilEx;
import service.auth.core.f07.FOX_MGR_AUTH_FUNC_DAO;
import service.auth.core.f07.FOX_MGR_AUTH_FUNC_DBO;
import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;
import cn.com.bankit.phoenix.trade.Service;

/**
 * 新增-FOX_AUTH_FUNC 功能定义表
 */
public class F0703 extends Service<JsonRequest, JsonResponse> {

	/**
	 * 构造函数
	 */
	public F0703() {
		super();
	}

	/**
	 * execute
	 */
	public JsonResponse execute(JsonRequest request) throws Exception {
		// 从请求报文中解析参数
		// 功能编号
		String FUNCID = request.getAsString("FUNCID");	
		// 功能名称
		String NAME = request.getAsString("NAME");	
		// html路径
		String HTML = request.getAsString("HTML");	
		// js路径
		String JS = request.getAsString("JS");	
		// css路径
		String CSS = request.getAsString("CSS");	
		// 备注
		String REMARK = request.getAsString("REMARK");	
		
		// 校验

		// 生成DBO
		FOX_MGR_AUTH_FUNC_DBO dbo = new FOX_MGR_AUTH_FUNC_DBO();
		// 功能编号
		if(!StringUtilEx.isNullOrEmpty(FUNCID)){
			dbo.set_FUNCID(FUNCID);
		}
		// 功能名称
		if(!StringUtilEx.isNullOrEmpty(NAME)){
			dbo.set_NAME(NAME);
		}
		// html路径
		if(!StringUtilEx.isNullOrEmpty(HTML)){
			dbo.set_HTML(HTML);
		}
		// js路径
		if(!StringUtilEx.isNullOrEmpty(JS)){
			dbo.set_JS(JS);
		}
		// css路径
		if(!StringUtilEx.isNullOrEmpty(CSS)){
			dbo.set_CSS(CSS);
		}
		// 备注
		if(!StringUtilEx.isNullOrEmpty(REMARK)){
			dbo.set_REMARK(REMARK);
		}

		// 读取数据库
		int result = FOX_MGR_AUTH_FUNC_DAO.delete(dbo);
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