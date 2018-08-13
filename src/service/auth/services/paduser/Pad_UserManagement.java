package service.auth.services.paduser;

import java.util.List;
import java.util.Map;

import both.common.util.LoggerUtil;
import both.common.util.StringUtilEx;
import both.constants.IResponseConstant;
import both.db.DBService;
import both.db.util.DbServiceUtil;
import service.auth.core.padteller.FOX_PAD_TELLER_TELLERINFO_DBO;
import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;
import cn.com.bankit.phoenix.trade.Service;

public class Pad_UserManagement extends Service<JsonRequest, JsonResponse>{


	@Override
	public JsonResponse execute(JsonRequest arg0) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 根据条件查询符合条件的User信息
	 * @param request
	 * @return 返回JsonRespone的响应信息
	 * @throws Exception
	 */
	public JsonResponse queryPadTellerInfoList(JsonRequest request) throws Exception{
		LoggerUtil.debug("开始查询PAD员工信息服务-----------------------------");

		String TELLER_ID = request.getAsString("TELLER_ID");
		
		String TELLER_NAME = request.getAsString("TELLER_NAME");
		
		String ORG_CN_NAME = request.getAsString("ORG_CN_NAME");
		
		String TELLER_TYPE = request.getAsString("TELLER_TYPE");
		
		String TELLER_STATUS = request.getAsString("TELLER_STATUS");
		
		String TELLER_LOGIN_TYPE = request.getAsString("TELLER_LOGIN_TYPE");
		
		String selectType = request.getAsString("SELECTTYPE");

		StringBuffer whereString = new StringBuffer();
		
		if(!StringUtilEx.isNullOrEmpty(TELLER_ID)){
			if("query".equals(selectType)){
				whereString.append(" and t.TELLER_ID like '%'||'"+TELLER_ID+"'||'%'");
			}else{
				whereString.append(" and t.TELLER_ID = '"+TELLER_ID+"'");
			}
		};
		if(!StringUtilEx.isNullOrEmpty(TELLER_NAME)){
			whereString.append(" and t.TELLER_NAME like '%'||'"+TELLER_NAME+"'||'%'");
		};
		if(!StringUtilEx.isNullOrEmpty(ORG_CN_NAME)){
			whereString.append(" and t.ORG_CN_NAME like '%'||'"+ORG_CN_NAME+"'||'%'");
		};
		if(!StringUtilEx.isNullOrEmpty(TELLER_TYPE)){
			whereString.append(" and t.TELLER_TYPE = '"+TELLER_TYPE+"'");
		};
		if(!StringUtilEx.isNullOrEmpty(TELLER_STATUS)){
			whereString.append(" and t.TELLER_STATUS = '"+TELLER_STATUS+"'");
		};
		if(!StringUtilEx.isNullOrEmpty(TELLER_LOGIN_TYPE)){
			whereString.append(" and t.TELLER_LOGIN_TYPE = '"+TELLER_LOGIN_TYPE+"'");
		};
		
		String sql = "select t.TELLER_ID,t.TELLER_NAME,t.ORGID,t.TELLER_TYPE,t.PASSWORD,t.TELLER_STATUS,t.TELLER_LOGIN_TYPE,t.IDCARD,t.MOBILE_NUMBER,t.CREATE_DATE,t.UPDATE_DATE,t.ISVOID,r.ROLE_ID,r.ROLE_NAME,o.ORG_CN_NAME "
				+" from FOX_PAD_TELLER_TELLERINFO t "
				+" left join FOX_PAD_TELLER_TELLERROLE tr on t.TELLER_ID=tr.TELLER_ID "
				+" left join FOX_PAD_TELLER_ROLEINFO r on tr.ROLE_ID=r.ROLE_ID "
				+" left join FOX_PAD_TELLER_ORGINFO o on o.ORGID=t.ORGID "+whereString;
		
		List<Map<String, Object>> tellerInfoList = DBService.executeQuery(sql);
		
		JsonResponse response = new JsonResponse();
		
		if (tellerInfoList!=null) {
			response.put(IResponseConstant.retCode,IResponseConstant.SUCCESS);
			response.put(IResponseConstant.retMsg, "查询成功");
			response.put("list", tellerInfoList);
		}else{
			response.put(IResponseConstant.retCode, IResponseConstant.FAILED_DB_ERROR);
			response.put(IResponseConstant.retMsg, "查询失败");
		}
		LoggerUtil.debug("结束查询PAD员工信息服务-----------------------------");
		return response;
	}
	
}