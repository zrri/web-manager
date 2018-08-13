package service.auth.services.f02.f0204;

import both.common.util.StringUtilEx;
import service.auth.core.f02.FOX_MGR_AUTH_TELLERINFO_DAO;
import service.auth.core.f02.FOX_MGR_AUTH_TELLERINFO_DBO;
import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;
import cn.com.bankit.phoenix.trade.Service;

/**
 * 新增-FOX_AUTH_USERINFO 用户基础信息表
 */
public class F0204 extends Service<JsonRequest, JsonResponse> {

	/**
	 * 构造函数
	 */
	public F0204() {
		super();
	}

	/**
	 * execute
	 */
	public JsonResponse execute(JsonRequest request) throws Exception {
		// 从请求报文中解析参数
		// 用户代号
		String USERID = request.getAsString("USERID");	
		// 姓名
		String USERNAME = request.getAsString("USERNAME");	
		// 用户类型（0-普通用户，1-BST自动用户，2-POS自动用户,3-移动营销自动用户,4-ATM自动用户,5-特色业务自动用户）
		String USERTYPE = request.getAsString("USERTYPE");	
		// 识别号（身份证号，或者设备编号）
		String IDENTIFYNO = request.getAsString("IDENTIFYNO");	
		// 归属机构
		String ORGID = request.getAsString("ORGID");	
		// 登录方式（0-密码身份证，1-密码）
		String LOGINTYPE = request.getAsString("LOGINTYPE");	
		// 用户状态（0-启用，1-停用，2-注销，8-锁定，9-被交接）
		String STATUS = request.getAsString("STATUS");	
		
		// 校验

		// 生成DBO
		FOX_MGR_AUTH_TELLERINFO_DBO dbo = new FOX_MGR_AUTH_TELLERINFO_DBO();
		// 用户代号
		if(!StringUtilEx.isNullOrEmpty(USERID)){
			dbo.set_USERID(USERID);
		}
		// 姓名
		if(!StringUtilEx.isNullOrEmpty(USERNAME)){
			dbo.set_USERNAME(USERNAME);
		}
		// 用户类型（0-普通用户，1-BST自动用户，2-POS自动用户,3-移动营销自动用户,4-ATM自动用户,5-特色业务自动用户）
		if(!StringUtilEx.isNullOrEmpty(USERTYPE)){
			dbo.set_USERTYPE(USERTYPE);
		}
		// 识别号（身份证号，或者设备编号）
		if(!StringUtilEx.isNullOrEmpty(IDENTIFYNO)){
			dbo.set_IDENTIFYNO(IDENTIFYNO);
		}
		// 归属机构
		if(!StringUtilEx.isNullOrEmpty(ORGID)){
			dbo.set_ORGID(ORGID);
		}
		// 登录方式（0-密码身份证，1-密码）
		if(!StringUtilEx.isNullOrEmpty(LOGINTYPE)){
			dbo.set_LOGINTYPE(LOGINTYPE);
		}
		// 用户状态（0-启用，1-停用，2-注销，8-锁定，9-被交接）
		if(!StringUtilEx.isNullOrEmpty(STATUS)){
			dbo.set_STATUS(STATUS);
		}

		// 读取数据库
		FOX_MGR_AUTH_TELLERINFO_DBO result = FOX_MGR_AUTH_TELLERINFO_DAO.query(dbo);
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