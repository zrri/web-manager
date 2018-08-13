package service.auth.services.f02.f0205;

import java.util.List;
import java.util.Map;

import service.auth.core.f02.FOX_MGR_AUTH_TELLERINFO_DAO;
import service.auth.core.f02.FOX_MGR_AUTH_TELLERINFO_DBO;
import service.auth.core.f03.FOX_MGR_AUTH_TELLERROLEMAPPING_DBO;
import service.auth.core.f10.FOX_MGR_AUTH_TELLEREXTENDINFO_DAO;
import service.auth.core.f10.FOX_MGR_AUTH_TELLEREXTENDINFO_DBO;
import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;
import both.common.util.LoggerUtil;
import both.common.util.StringUtilEx;
import both.constants.IResponseConstant;
import both.db.DBService;
import both.db.DbParam;
import both.db.DbParam.DbSqlType;
import both.db.util.DbEntityUtil;
import both.db.util.DbServiceUtil;
import both.db.util.SqlBuildUtil;
import ch.qos.logback.core.db.dialect.SQLDialectCode;
import cn.com.bankit.phoenix.jdbc.tool.DBAccessor;
import cn.com.bankit.phoenix.trade.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 新增-FOX_AUTH_USERINFO 用户基础信息表
 */
public class F0205 extends Service<JsonRequest, JsonResponse> {

	/**
	 * 构造函数
	 */
	public F0205() {
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

	// 查询
	public JsonResponse queryUserInfoList(JsonRequest request) throws Exception {

		String USERID = request.getAsString("userId");// 用户代号
		String USERNAME = request.getAsString("username");// 柜面姓名
		String identifyNO = request.getAsString("identifyNO");// 识别号
		String SEX = request.getAsString("sex");// 性别（1-男，2-女）
		String orgChiName = request.getAsString("orgChiName");// 归属机构
		String STATUS = request.getAsString("status");// 用户状态（0-启用，1-停用，2-注销，8-锁定，9-被交接）

		StringBuffer buffer = new StringBuffer(" where 1=1 ");
		if (!StringUtilEx.isNullOrEmpty(USERID)) {
			buffer.append(" and t.TELLER_ID like '%" + USERID.trim() + "%' ");
		}
		if (!StringUtilEx.isNullOrEmpty(USERNAME)) {
			buffer.append(" and t.TELLER_NAME like '%" + USERNAME.trim() + "%' ");
		}
		if (!StringUtilEx.isNullOrEmpty(identifyNO)) {
			buffer.append(" and t.IDENTIFY_NO like '%" + identifyNO.trim()
					+ "%' ");
		}
		if (!StringUtilEx.isNullOrEmpty(orgChiName)) {
			// 根据机构名称查询机构id
			String sql = "select o.orgid from FOX_MGR_AUTH_ORGINFO o where o.org_CN_name like '%"
					+ orgChiName + "%'";
			LoggerUtil.error("sql==================="+sql);
			List<Map<String, Object>> orgIdList = DBService.executeQuery(sql);
			if (orgIdList.size() == 1) {
				buffer.append(" and t.ORGID ='" + orgIdList.get(0).get("ORGID") + "' ");
			} else if (orgIdList.size() > 1) {
				StringBuffer orgIdBuffer = new StringBuffer();
				for (Map<String, Object> map : orgIdList) {
					orgIdBuffer.append(map.get("ORGID") + ",");
				}
				String temp = orgIdBuffer.substring(0,orgIdBuffer.length()-1);
				buffer.append(" and t.ORGID in (" + temp + ") ");
			}

		}
		if (!StringUtilEx.isNullOrEmpty(STATUS)) {
			buffer.append(" and t.STATUS ='" + STATUS.trim() + "' ");
		}
		if (!StringUtilEx.isNullOrEmpty(SEX)) {
			buffer.append(" and e.SEX ='" + SEX.trim() + "' ");
		}

		/*
		 * String whereString = ""; if (buffer.length() > 0) { whereString =
		 * buffer.substring(0, buffer.length() - 5); }
		 */

		JsonResponse response = new JsonResponse();

		// 读取数据库
		String sql = "select r.ROLE_ID,r.ROLE_NAME,t.TELLER_ID,t.TELLER_NAME,e.SEX,o.ORG_CN_NAME,t.IDENTIFY_NO,t.STATUS,e.ENG_NAME,e.MOBILE_PHONE,e.FIXED_PHONE,e.EMAIL " +
				" from FOX_MGR_AUTH_TELLERINFO t " +
				" left join FOX_MGR_AUTH_TELLEREXTENDINFO e on t.TELLER_ID = e.TELLER_ID" +
				" left join FOX_MGR_AUTH_ORGINFO o on t.orgid=o.orgid" +
				" left join Fox_MGR_AUTH_TELLERROLEMAPPING m on m.teller_id = t.teller_id " +
				" left join Fox_MGR_AUTH_ROLEINFO r on r.ROLE_ID = m.ROLE_ID"
				+ buffer.toString();
		LoggerUtil.error("sql=================" + sql);
		List<Map<String, Object>> resultList = DBService.executeQuery(sql);

		if (resultList == null) {
			// 失败
			response.put(IResponseConstant.retCode,
					IResponseConstant.FAILED_DB_ERROR);
			response.put(IResponseConstant.retMsg, "查询失败");
		} else {
			// 成功
			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			response.put(IResponseConstant.retMsg, "查询成功");

			JSONArray jsonArray = new JSONArray();
			for (Map<String, Object> result : resultList) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("userId", result.get("TELLER_ID"));
				jsonObject.put("username", result.get("TELLER_NAME"));
//				jsonObject.put("userType", result.get("USERTYPE"));
				jsonObject.put("identifyNO", result.get("IDENTIFY_NO"));
//				jsonObject.put("orgId", result.get("ORGID"));
				jsonObject.put("orgChiName", result.get("ORG_CN_NAME"));
//				jsonObject.put("loginType", result.get("LOGINTYPE"));
				jsonObject.put("status", result.get("STATUS"));

				jsonObject.put("englishName", result.get("ENG_NAME"));
				jsonObject.put("sex", result.get("SEX"));
				jsonObject.put("cellphone", result.get("MOBILE_PHONE"));
				jsonObject.put("phone", result.get("FIXED_PHONE"));
//				jsonObject.put("address", result.get("ADDRESS"));
				jsonObject.put("email", result.get("EMAIL"));
				jsonObject.put("roleId", result.get("ROLE_ID"));
				jsonObject.put("roleName", result.get("ROLE_NAME"));
//				jsonObject.put("userLevel", result.get("USERLEVEL"));
//				jsonObject.put("lastLoginTime", result.get("LASTLOGINTIME"));
//				jsonObject.put("lastLogoutTime", result.get("LASTLOGOUTTIME"));

				jsonArray.add(jsonObject);
			}

			response.put("list", jsonArray);
		}

		return response;
	}

	// 删除用户
	public JsonResponse deleteUserInfoList(JsonRequest request)
			throws Exception {

		JSONArray deleteJsonArray = (JSONArray) request.get("list");
		int filedResult=0;
		DbParam dbParam = new DbParam();
		for (int i = 0; i < deleteJsonArray.size(); i++) {
//			JSONObject deleteJsonObject = deleteJsonArray.getJSONObject(i);
			String userId = (String) deleteJsonArray.getJSONObject(i).get("userId");
			LoggerUtil.error("userId==================" + userId);
			

			FOX_MGR_AUTH_TELLERINFO_DBO userInfoDBO = new FOX_MGR_AUTH_TELLERINFO_DBO();
			userInfoDBO.set_USERID(userId);
			DbParam userInfoParam = DbEntityUtil.getDbParam(userInfoDBO, null, DbSqlType.Delete);
			
			FOX_MGR_AUTH_TELLEREXTENDINFO_DBO userExtendInfoDBO = new FOX_MGR_AUTH_TELLEREXTENDINFO_DBO();
			userExtendInfoDBO.set_USERID(userId);
			DbParam userExtendInfoParam = DbEntityUtil.getDbParam(userExtendInfoDBO, null, DbSqlType.Delete); 
			
			FOX_MGR_AUTH_TELLERROLEMAPPING_DBO tellerrolemappingDBO = new FOX_MGR_AUTH_TELLERROLEMAPPING_DBO();
			tellerrolemappingDBO.set_USERID(userId);
			DbParam tellerRoleMappingParam = DbEntityUtil.getDbParam(tellerrolemappingDBO, null, DbSqlType.Delete); 
			
			dbParam.add(SqlBuildUtil.createDeleteSql(userInfoParam), DbEntityUtil.getParaMap(userInfoDBO));
			dbParam.add(SqlBuildUtil.createDeleteSql(userExtendInfoParam), DbEntityUtil.getParaMap(userExtendInfoDBO));
			dbParam.add(SqlBuildUtil.createDeleteSql(tellerRoleMappingParam), DbEntityUtil.getParaMap(tellerrolemappingDBO));

			try {
				DbServiceUtil.executeTransaction(dbParam);
			} catch (Exception e) {
				LoggerUtil.error(e.getMessage(),e);
				filedResult++;
			}
		}

		JsonResponse response = new JsonResponse();
		if (filedResult != 0) {
			response.put(IResponseConstant.retCode, IResponseConstant.FAILED);
			response.put(IResponseConstant.retMsg, filedResult + "条数据删除失败");
		} else {
			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			response.put(IResponseConstant.retMsg, "删除成功");
		}
		return response;
	}

	// 添加
	public JsonResponse addUserInfo(JsonRequest request) throws Exception {

		JSONObject jsonObject = request.getData();

		/*
		 * DbParam param = new DbParam(); param.add(FOX_AUTH_USEREXTENDINFO_DAO
		 * .insert(json2UserExtendInfo(jsonObject)), DbSqlType.Insert);
		 * param.add(FOX_AUTH_USERINFO_DAO.insert(json2UserInfo(jsonObject)),
		 * DbSqlType.Insert); int[] results =
		 * DbServiceUtil.executeTransaction(param);
		 */
		
//		int result1 = FOX_MGR_AUTH_TELLERINFO_DAO.insert(json2UserInfo(jsonObject));
//		int result2 = FOX_MGR_AUTH_TELLEREXTENDINFO_DAO
//				.insert(json2UserExtendInfo(jsonObject));
		
		
		DBAccessor dbAccessor = DBAccessor.getDBAccessor();
//		List<String> sql = new ArrayList<String>();
		
		DbParam dbParam = new DbParam();
		
		
		
		DbParam tellerInfo = DbEntityUtil.getDbParam(json2UserInfo(jsonObject), null, DbSqlType.Insert);
		dbParam.add(SqlBuildUtil.createInsertSql(tellerInfo), DbEntityUtil.getParaMap(json2UserInfo(jsonObject)));
		
		DbParam tellerExtendInfo = DbEntityUtil.getDbParam(json2UserExtendInfo(jsonObject), null, DbSqlType.Insert);
//		sql.add(SqlBuildUtil.createInsertSql(tellerExtendInfo));
		dbParam.add(SqlBuildUtil.createInsertSql(tellerExtendInfo), DbEntityUtil.getParaMap(json2UserExtendInfo(jsonObject)));
		
		if (!StringUtilEx.isNullOrEmpty(jsonObject.getString("roleId"))) {
			FOX_MGR_AUTH_TELLERROLEMAPPING_DBO dbo = new FOX_MGR_AUTH_TELLERROLEMAPPING_DBO();
			
			dbo.set_ROLEID(jsonObject.getString("roleId"));
			dbo.set_USERID(jsonObject.getString("userId"));
			
			DbParam tellerRoleMapping = DbEntityUtil.getDbParam(dbo, null, DbSqlType.Insert);
//			sql.add(SqlBuildUtil.createInsertSql(tellerRoleMapping));
			dbParam.add(SqlBuildUtil.createInsertSql(tellerRoleMapping), DbEntityUtil.getParaMap(dbo));
		}
		
		JsonResponse response = new JsonResponse();
		
		try {
			DbServiceUtil.executeTransaction(dbParam);
			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			response.put(IResponseConstant.retMsg, "新增成功");
		} catch (Exception e) {
			LoggerUtil.error(e.getMessage(),e);
			response.put(IResponseConstant.retCode, IResponseConstant.FAILED);
			response.put(IResponseConstant.retMsg, "新增失败");
		}
		
		return response;
	}

	// 更新
	public JsonResponse updateUserInfo(JsonRequest request) throws Exception {

		JSONObject jsonObject = request.getData();
		DbParam dbParam = new DbParam();
		
		
		
		
		
		FOX_MGR_AUTH_TELLERINFO_DBO userInfoNew = json2UserInfo(jsonObject);
		FOX_MGR_AUTH_TELLERINFO_DBO userInfoOld = new FOX_MGR_AUTH_TELLERINFO_DBO();
		userInfoOld.set_USERID(userInfoNew.get_USERID());
		dbParam.add(userInfoNew, userInfoOld, DbSqlType.Update);
		
		
		FOX_MGR_AUTH_TELLEREXTENDINFO_DBO userExtendInfoNew = json2UserExtendInfo(jsonObject);
		FOX_MGR_AUTH_TELLEREXTENDINFO_DBO userExtendInfoOld = new FOX_MGR_AUTH_TELLEREXTENDINFO_DBO();
		userExtendInfoOld.set_USERID(userExtendInfoNew.get_USERID());
		dbParam.add(userExtendInfoNew, userExtendInfoOld, DbSqlType.Update);
		
		//先删除原来绑定的角色
		FOX_MGR_AUTH_TELLERROLEMAPPING_DBO deleteTellerRoleMapping = new FOX_MGR_AUTH_TELLERROLEMAPPING_DBO();
		deleteTellerRoleMapping.set_USERID(request.getAsString("userId"));
		DbParam deleteTellerRoleMappingParm = DbEntityUtil.getDbParam(deleteTellerRoleMapping, null, DbSqlType.Delete);
		dbParam.add(SqlBuildUtil.createDeleteSql(deleteTellerRoleMappingParm), DbEntityUtil.getParaMap(deleteTellerRoleMapping));
		
		//判断是否关联角色
		if (!StringUtilEx.isNullOrEmpty(request.getAsString("roleId"))) {
			//新建新的角色
			FOX_MGR_AUTH_TELLERROLEMAPPING_DBO insertTellerRoleMapping = new FOX_MGR_AUTH_TELLERROLEMAPPING_DBO();
			insertTellerRoleMapping.set_USERID(request.getAsString("userId"));
			insertTellerRoleMapping.set_ROLEID(request.getAsString("userId"));
			DbParam insertTellerRoleMappingParm = DbEntityUtil.getDbParam(insertTellerRoleMapping, null, DbSqlType.Insert);
			dbParam.add(SqlBuildUtil.createDeleteSql(insertTellerRoleMappingParm), DbEntityUtil.getParaMap(insertTellerRoleMapping));
			
		}
		
//		int result1 = FOX_MGR_AUTH_TELLERINFO_DAO.update(userInfoNew, userInfoOld);
//		int result2 = FOX_MGR_AUTH_TELLEREXTENDINFO_DAO.update(userExtendInfoNew,
//				userExtendInfoOld);
		JsonResponse response = new JsonResponse();
		try {
			DbServiceUtil.executeTransaction(dbParam);
			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			response.put(IResponseConstant.retMsg, "更新成功");
		} catch (Exception e) {
			// TODO: handle exception
			LoggerUtil.error(e.getMessage(),e);
			response.put(IResponseConstant.retCode, IResponseConstant.FAILED);
			response.put(IResponseConstant.retMsg, "更新失败");
		}
		
		return response;
	}

	private FOX_MGR_AUTH_TELLERINFO_DBO json2UserInfo(JSONObject jsonObject) {
		FOX_MGR_AUTH_TELLERINFO_DBO userInfoDBO = new FOX_MGR_AUTH_TELLERINFO_DBO();
		userInfoDBO.set_USERID(jsonObject.getString("userId"));
		userInfoDBO.set_USERNAME(jsonObject.getString("username"));
		userInfoDBO.set_USERTYPE("");
		userInfoDBO.set_IDENTIFYNO(jsonObject.getString("identifyNO"));
		userInfoDBO.set_ORGID(jsonObject.getString("orgId"));
		String loginType = jsonObject.getString("loginType");
		if (StringUtilEx.isNullOrEmpty(loginType)) {
			userInfoDBO.set_LOGINTYPE("1");
		} else {
			userInfoDBO.set_LOGINTYPE(loginType);
		}
		userInfoDBO.set_STATUS(jsonObject.getString("status"));
		return userInfoDBO;
	}

	private FOX_MGR_AUTH_TELLEREXTENDINFO_DBO json2UserExtendInfo(
			JSONObject jsonObject) {
		FOX_MGR_AUTH_TELLEREXTENDINFO_DBO userExtendInfoDBO = new FOX_MGR_AUTH_TELLEREXTENDINFO_DBO();
		userExtendInfoDBO.set_USERID(jsonObject.getString("userId"));
		userExtendInfoDBO.set_ENGLISHNAME(jsonObject.getString("englishName"));
		userExtendInfoDBO.set_SEX(jsonObject.getString("sex"));
		userExtendInfoDBO.set_CELLPHONE(jsonObject.getString("cellphone"));
		userExtendInfoDBO.set_PHONE(jsonObject.getString("phone"));
		userExtendInfoDBO.set_ADDRESS(jsonObject.getString("address"));
		userExtendInfoDBO.set_EMAIL(jsonObject.getString("email"));
		userExtendInfoDBO.set_USERLEVEL("");
		userExtendInfoDBO.set_LASTLOGINTIME("");
		userExtendInfoDBO.set_LASTLOGOUTTIME("");
		return userExtendInfoDBO;
	}

}
