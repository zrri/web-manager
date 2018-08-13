package service.auth.services.f01.f0103;

import both.common.util.StringUtilEx;
import service.auth.core.f01.FOX_MGR_AUTH_ORGINFO_DAO;
import service.auth.core.f01.FOX_MGR_AUTH_ORGINFO_DBO;
import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;
import cn.com.bankit.phoenix.trade.Service;

/**
 * 新增-FOX_AUTH_ORGINFO 机构信息表
 */
public class F0103 extends Service<JsonRequest, JsonResponse> {

	/**
	 * 构造函数
	 */
	public F0103() {
		super();
	}

	/**
	 * execute
	 */
	public JsonResponse execute(JsonRequest request) throws Exception {
		// 从请求报文中解析参数
		// 机构编号
		String ORGID = request.getAsString("ORGID");	
		// 机构中文名
		String ORGCHINAME = request.getAsString("ORGCHINAME");	
		// 机构英文名
		String ORGENGNAME = request.getAsString("ORGENGNAME");	
		// 上级机构
		String PARENTORGID = request.getAsString("PARENTORGID");	
		// 机构级别
		String ORGLVL = request.getAsString("ORGLVL");	
		// 机构类型
		String ORGTYPE = request.getAsString("ORGTYPE");	
		// 创建日期
		String CREATEDATE = request.getAsString("CREATEDATE");	
		// 创建时间
		String CREATETIME = request.getAsString("CREATETIME");	
		// 更新日期
		String UPDATEDATE = request.getAsString("UPDATEDATE");	
		// 更新时间
		String UPDATETIME = request.getAsString("UPDATETIME");	
		// 状态（1-可用，0-不可用）
		String STATUS = request.getAsString("STATUS");	
		
		// 校验

		// 生成DBO
		FOX_MGR_AUTH_ORGINFO_DBO dbo = new FOX_MGR_AUTH_ORGINFO_DBO();
		// 机构编号
		if(!StringUtilEx.isNullOrEmpty(ORGID)){
			dbo.set_ORGID(ORGID);
		}
		// 机构中文名
		if(!StringUtilEx.isNullOrEmpty(ORGCHINAME)){
			dbo.set_ORGCHINAME(ORGCHINAME);
		}
		// 机构英文名
		if(!StringUtilEx.isNullOrEmpty(ORGENGNAME)){
			dbo.set_ORGENGNAME(ORGENGNAME);
		}
		// 上级机构
		if(!StringUtilEx.isNullOrEmpty(PARENTORGID)){
			dbo.set_PARENTORGID(PARENTORGID);
		}
		// 机构级别
		if(!StringUtilEx.isNullOrEmpty(ORGLVL)){
			dbo.set_ORGLVL(ORGLVL);
		}
		// 机构类型
		if(!StringUtilEx.isNullOrEmpty(ORGTYPE)){
			dbo.set_ORGTYPE(ORGTYPE);
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
		// 状态（1-可用，0-不可用）
		if(!StringUtilEx.isNullOrEmpty(STATUS)){
			dbo.set_STATUS(STATUS);
		}

		// 读取数据库
		int result = FOX_MGR_AUTH_ORGINFO_DAO.delete(dbo);
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