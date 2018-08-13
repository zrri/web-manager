package service.auth.services.f10.f1005;

import both.common.util.StringUtilEx;
import java.util.List;
import service.auth.core.f10.FOX_MGR_AUTH_TELLEREXTENDINFO_DAO;
import service.auth.core.f10.FOX_MGR_AUTH_TELLEREXTENDINFO_DBO;
import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;
import cn.com.bankit.phoenix.trade.Service;

/**
 * 新增-FOX_AUTH_USEREXTENDINFO 用户扩展信息表
 */
public class F1005 extends Service<JsonRequest, JsonResponse> {

	/**
	 * 构造函数
	 */
	public F1005() {
		super();
	}

	/**
	 * execute
	 */
	public JsonResponse execute(JsonRequest request) throws Exception {
		// 从请求报文中解析参数
		// 用户代号
		String USERID = request.getAsString("USERID");	
		// 英文名
		String ENGLISHNAME = request.getAsString("ENGLISHNAME");	
		// 性别（1-男，2-女）
		String SEX = request.getAsString("SEX");	
		// 手机号
		String CELLPHONE = request.getAsString("CELLPHONE");	
		// 固定电话
		String PHONE = request.getAsString("PHONE");	
		// 家庭住址
		String ADDRESS = request.getAsString("ADDRESS");	
		// 用户级别
		String LEVEL = request.getAsString("LEVEL");	
		// 最后一次登陆时间
		String LASTLOGINTIME = request.getAsString("LASTLOGINTIME");	
		// 最后一次退出时间
		String LASTLOGOUTTIME = request.getAsString("LASTLOGOUTTIME");	
		// 电子邮箱
		String EMAIL = request.getAsString("EMAIL");	
		
		// 校验

		// 生成DBO
		FOX_MGR_AUTH_TELLEREXTENDINFO_DBO dbo = new FOX_MGR_AUTH_TELLEREXTENDINFO_DBO();
		// 用户代号
		if(!StringUtilEx.isNullOrEmpty(USERID)){
			dbo.set_USERID(USERID);
		}
		// 英文名
		if(!StringUtilEx.isNullOrEmpty(ENGLISHNAME)){
			dbo.set_ENGLISHNAME(ENGLISHNAME);
		}
		// 性别（1-男，2-女）
		if(!StringUtilEx.isNullOrEmpty(SEX)){
			dbo.set_SEX(SEX);
		}
		// 手机号
		if(!StringUtilEx.isNullOrEmpty(CELLPHONE)){
			dbo.set_CELLPHONE(CELLPHONE);
		}
		// 固定电话
		if(!StringUtilEx.isNullOrEmpty(PHONE)){
			dbo.set_PHONE(PHONE);
		}
		// 家庭住址
		if(!StringUtilEx.isNullOrEmpty(ADDRESS)){
			dbo.set_ADDRESS(ADDRESS);
		}
		// 用户级别
		if(!StringUtilEx.isNullOrEmpty(LEVEL)){
			dbo.set_USERLEVEL(LEVEL);
		}
		// 最后一次登陆时间
		if(!StringUtilEx.isNullOrEmpty(LASTLOGINTIME)){
			dbo.set_LASTLOGINTIME(LASTLOGINTIME);
		}
		// 最后一次退出时间
		if(!StringUtilEx.isNullOrEmpty(LASTLOGOUTTIME)){
			dbo.set_LASTLOGOUTTIME(LASTLOGOUTTIME);
		}
		// 电子邮箱
		if(!StringUtilEx.isNullOrEmpty(EMAIL)){
			dbo.set_EMAIL(EMAIL);
		}

		// 读取数据库
		List<FOX_MGR_AUTH_TELLEREXTENDINFO_DBO> result = FOX_MGR_AUTH_TELLEREXTENDINFO_DAO.queryTable(dbo);
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