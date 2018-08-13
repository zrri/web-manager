package service.auth.services.f01.f0101;

import java.text.SimpleDateFormat;

import service.auth.core.f01.FOX_MGR_AUTH_ORGINFO_DAO;
import service.auth.core.f01.FOX_MGR_AUTH_ORGINFO_DBO;
import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;
import both.common.util.LoggerUtil;
import both.common.util.StringUtilEx;
import both.constants.IResponseConstant;
import cn.com.bankit.phoenix.trade.Service;

/**
 * 新增-FOX_AUTH_ORGINFO 机构信息表
 */
public class F0101 extends Service<JsonRequest, JsonResponse> {

	/**
	 * 构造函数
	 */
	public F0101() {
		super();
	}

	/**
	 * execute
	 */
	public JsonResponse execute(JsonRequest request) throws Exception {
		// 拼装应答报文
		JsonResponse response = new JsonResponse();

		return response;
	}

	// 添加机构
	public JsonResponse addOrgInfo(JsonRequest request) throws Exception {
		JsonResponse response = new JsonResponse();
		String ORGID = request.getAsString("orgId");
		// 机构中文名
		String ORGCHINAME = request.getAsString("orgChiName");
		// 机构英文名
		String ORGENGNAME = request.getAsString("orgEngName");
		// 上级机构
		String PARENTORGID = request.getAsString("parentOrgId");
		// 机构级别
		String ORGLVL = request.getAsString("orgLvl");
		// 机构类型
		String ORGTYPE = request.getAsString("orgType");
		// 创建日期
		String CREATEDATE = request.getAsString("createDate");
		// 状态（1-可用，0-不可用）
		String STATUS = request.getAsString("status");

		FOX_MGR_AUTH_ORGINFO_DBO dbo = new FOX_MGR_AUTH_ORGINFO_DBO();
		dbo.set_ORGID(ORGID);
		dbo.set_ORGCHINAME(ORGCHINAME);
		dbo.set_ORGENGNAME(ORGENGNAME);
		dbo.set_PARENTORGID(PARENTORGID);
		dbo.set_ORGLVL(ORGLVL);
		dbo.set_ORGTYPE(ORGTYPE);
		SimpleDateFormat dfDate = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat dfTime = new SimpleDateFormat("HHmmss");
		if (StringUtilEx.isNullOrEmpty(CREATEDATE)) {
			//如果创建日期为空，说明第一次添加
			dbo.set_CREATEDATE(dfDate.format(System.currentTimeMillis()));
			dbo.set_CREATETIME(dfTime.format(System.currentTimeMillis()));
		}else{
			dbo.set_UPDATEDATE(dfDate.format(System.currentTimeMillis()));
			dbo.set_UPDATETIME(dfTime.format(System.currentTimeMillis()));
		}
		dbo.set_STATUS(STATUS);
		LoggerUtil.error(dbo.toString());
		
		int result = FOX_MGR_AUTH_ORGINFO_DAO.insert(dbo);
		if (result == -1) {
			response.put(IResponseConstant.retCode, IResponseConstant.FAILED);
			response.put(IResponseConstant.retMsg, "添加失败");
		} else {
			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			response.put(IResponseConstant.retMsg, "添加成功");
		}
		return response;
	}
}