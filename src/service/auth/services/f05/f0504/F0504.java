package service.auth.services.f05.f0504;

import both.common.util.StringUtilEx;
import service.auth.core.f05.FOX_MGR_AUTH_FUNCGROUPINFO_DAO;
import service.auth.core.f05.FOX_MGR_AUTH_FUNCGROUPINFO_DBO;
import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;
import cn.com.bankit.phoenix.trade.Service;

/**
 * 新增-FOX_AUTH_FUNCGROUPINFO 功能组信息表
 */
public class F0504 extends Service<JsonRequest, JsonResponse> {

	/**
	 * 构造函数
	 */
	public F0504() {
		super();
	}

	/**
	 * execute
	 */
	public JsonResponse execute(JsonRequest request) throws Exception {
		// 从请求报文中解析参数
		// 交易组编号
		String GROUPID = request.getAsString("GROUPID");	
		// 交易组名称
		String GROUPNAME = request.getAsString("GROUPNAME");	
		// 交易组描述
		String GROUPDESCRIBTION = request.getAsString("GROUPDESCRIBTION");	
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
		FOX_MGR_AUTH_FUNCGROUPINFO_DBO dbo = new FOX_MGR_AUTH_FUNCGROUPINFO_DBO();
		// 交易组编号
		if(!StringUtilEx.isNullOrEmpty(GROUPID)){
			dbo.set_GROUPID(GROUPID);
		}
		// 交易组名称
		if(!StringUtilEx.isNullOrEmpty(GROUPNAME)){
			dbo.set_GROUPNAME(GROUPNAME);
		}
		// 交易组描述
		if(!StringUtilEx.isNullOrEmpty(GROUPDESCRIBTION)){
			dbo.set_GROUPDESCRIBTION(GROUPDESCRIBTION);
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
		FOX_MGR_AUTH_FUNCGROUPINFO_DBO result = FOX_MGR_AUTH_FUNCGROUPINFO_DAO.query(dbo);
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