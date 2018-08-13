package service.auth.services.f04.f0401;

import both.common.util.StringUtilEx;
import service.auth.core.f04.FOX_MGR_AUTH_ROLEINFO_DAO;
import service.auth.core.f04.FOX_MGR_AUTH_ROLEINFO_DBO;
import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;
import cn.com.bankit.phoenix.trade.Service;

/**
 * 新增-FOX_AUTH_ROLEINFO 角色信息表
 */
public class F0401 extends Service<JsonRequest, JsonResponse> {

	/**
	 * 构造函数
	 */
	public F0401() {
		super();
	}

	/**
	 * execute
	 */
	public JsonResponse execute(JsonRequest request) throws Exception {
		// 从请求报文中解析参数
		// 角色编号
		String ROLEID = request.getAsString("ROLEID");	
		// 角色名称
		String ROLENAME = request.getAsString("ROLENAME");	
		// 角色描述
		String ROLEDESCRIBTION = request.getAsString("ROLEDESCRIBTION");	
		// 创建日期
		String CREATEDATE = request.getAsString("CREATEDATE");	
		// 创建时间
		String CREATETIME = request.getAsString("CREATETIME");	
		// 更新日期
		String UPDATEDATE = request.getAsString("UPDATEDATE");	
		// 更新时间
		String UPDATETIME = request.getAsString("UPDATETIME");	
		// 是否有效（1-是，0-否）默认1
		String STATUS = request.getAsString("STATUS");	
		
		// 校验

		// 生成DBO
		FOX_MGR_AUTH_ROLEINFO_DBO dbo = new FOX_MGR_AUTH_ROLEINFO_DBO();
		// 角色编号
		if(!StringUtilEx.isNullOrEmpty(ROLEID)){
			dbo.set_ROLEID(ROLEID);
		}
		// 角色名称
		if(!StringUtilEx.isNullOrEmpty(ROLENAME)){
			dbo.set_ROLENAME(ROLENAME);
		}
		// 角色描述
		if(!StringUtilEx.isNullOrEmpty(ROLEDESCRIBTION)){
			dbo.set_ROLEDESCRIBTION(ROLEDESCRIBTION);
		}
		// 创建日期
		if(!StringUtilEx.isNullOrEmpty(CREATEDATE)){
			dbo.set_CREATEDATE(CREATEDATE);
		}
		// 创建时间
		if(!StringUtilEx.isNullOrEmpty(CREATETIME)){
			dbo.set_CREATETIME(CREATETIME);
		}
		// 更新日期
		if(!StringUtilEx.isNullOrEmpty(UPDATEDATE)){
			dbo.set_UPDATEDATE(UPDATEDATE);
		}
		// 更新时间
		if(!StringUtilEx.isNullOrEmpty(UPDATETIME)){
			dbo.set_UPDATETIME(UPDATETIME);
		}
		// 是否有效（1-是，0-否）默认1
		if(!StringUtilEx.isNullOrEmpty(STATUS)){
			dbo.set_STATUS(STATUS);
		}

		// 读取数据库
		int result = FOX_MGR_AUTH_ROLEINFO_DAO.insert(dbo);
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