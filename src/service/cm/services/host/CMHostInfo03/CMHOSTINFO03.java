package service.cm.services.host.CMHostInfo03;

import com.alibaba.fastjson.JSONArray;

import both.common.util.LoggerUtil;
import both.common.util.StringUtilEx;
import both.constants.IResponseConstant;
import both.db.DbParam;
import both.db.DbParam.DbSqlType;
import both.db.util.DbServiceUtil;
import service.cm.core.db.CMHostInfo.FOX_MGR_CM_HOSTINFO_DAO;
import service.cm.core.db.CMHostInfo.FOX_MGR_CM_HOSTINFO_DBO;
import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;
import cn.com.bankit.phoenix.trade.Service;

/**
 * 新增-FOX_CM_HOSTINFO 主机信息表
 */
public class CMHOSTINFO03 extends Service<JsonRequest, JsonResponse> {

	/**
	 * 构造函数
	 */
	public CMHOSTINFO03() {
		super();
	}

	/**
	 * execute
	 */
	public JsonResponse execute(JsonRequest request) throws Exception {
		JSONArray hostArray = (JSONArray) request.get("list");

		DbParam param = new DbParam();

		for (int i = 0, len = hostArray.size(); i < len; i++) {
			FOX_MGR_CM_HOSTINFO_DBO dbo = new FOX_MGR_CM_HOSTINFO_DBO();
			dbo.set_HOSTIP(hostArray.getString(i));
			param.add(dbo, DbSqlType.Delete);
		}

		// 拼装应答报文
		JsonResponse response = new JsonResponse();

		try {
			DbServiceUtil.executeTransaction(param);

			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			response.put(IResponseConstant.retMsg, "成功");
		} catch (Exception e) {
			LoggerUtil.error(e.getMessage(), e);
			// 失败
			response.put(IResponseConstant.retCode,
					IResponseConstant.FAILED_DB_ERROR);
			response.put(IResponseConstant.retMsg, "查询数据库出错");

		}

		return response;
	}
}