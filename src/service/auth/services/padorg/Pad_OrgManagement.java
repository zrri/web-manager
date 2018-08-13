package service.auth.services.padorg;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import both.common.util.DateUtil;
import both.common.util.LoggerUtil;
import both.common.util.StringUtilEx;
import both.constants.IResponseConstant;
import both.db.DbParam;
import both.db.DbParam.DbSqlType;
import both.db.util.BeanUtil;
import both.db.util.DbEntityUtil;
import both.db.util.DbServiceUtil;
import both.db.util.SqlBuildUtil;


import service.auth.core.padorg.FOX_PAD_TELLER_ORGINFO_DBO;
import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;

public class Pad_OrgManagement {

	/**
	 * 
	 */
	public Pad_OrgManagement() {
		super();
	}
	/**
	 * 添加PAD机构信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public JsonResponse addPadOrgInfo(JsonRequest request) throws Exception{
		LoggerUtil.debug("添加PAD端机构信息服务开始---------------------------");
		//获取请求的数据参数
		String ORGID = request.getAsString("ORGID");
		String ORG_CN_NAME = request.getAsString("ORG_CN_NAME");
		String ORG_ENG_NAME = request.getAsString("ORG_ENG_NAME");
		String BRANCH_BANK = request.getAsString("BRANCH_BANK");
		String PARENT_ORGID = request.getAsString("PARENT_ORGID");
		String ORG_LEVEL = request.getAsString("ORG_LEVEL");
		String ORG_TYPE = request.getAsString("ORG_TYPE");
		String SORT_NO = request.getAsString("SORT_NO");
		String ISVOID = request.getAsString("ISVOID");
		
		FOX_PAD_TELLER_ORGINFO_DBO dbo = new FOX_PAD_TELLER_ORGINFO_DBO();
		if(!StringUtilEx.isNullOrEmpty(ORGID)){
			dbo.setORGID(ORGID);
		}
		if(!StringUtilEx.isNullOrEmpty(ORG_CN_NAME)){
			dbo.setORG_CN_NAME(ORG_CN_NAME);
		}
		if(!StringUtilEx.isNullOrEmpty(ORG_ENG_NAME)){
			dbo.setORG_ENG_NAME(ORG_ENG_NAME);
		}
		if(!StringUtilEx.isNullOrEmpty(BRANCH_BANK)){
			dbo.setBRANCH_BANK(BRANCH_BANK);
		}
		if(!StringUtilEx.isNullOrEmpty(PARENT_ORGID)){
			dbo.setPARENT_ORGID(PARENT_ORGID);
		}
		if(!StringUtilEx.isNullOrEmpty(ORG_LEVEL)){
			dbo.setORG_LEVEL(ORG_LEVEL);
		}
		if(!StringUtilEx.isNullOrEmpty(ORG_TYPE)){
			dbo.setORG_TYPE(ORG_TYPE);
		}
		if(!StringUtilEx.isNullOrEmpty(SORT_NO)){
			dbo.setSORT_NO(SORT_NO);
		}
		if(!StringUtilEx.isNullOrEmpty(ISVOID)){
			dbo.setISVOID(ISVOID);
		}
		
		dbo.setCREATE_DATE(DateUtil.dataToString(new Date()));
		
		int result = DbServiceUtil.executeInsert(dbo);
		
		JsonResponse response = new JsonResponse();
		
		if(result!=1){
			response.put(IResponseConstant.retCode, IResponseConstant.FAILED_DB_ERROR);
			response.put(IResponseConstant.retMsg, "添加机构信息失败!");
		}else{
			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			response.put(IResponseConstant.retMsg, "添加机构信息成功!");
		}
		LoggerUtil.debug("添加PAD端机构信息服务结束---------------------------");
		return response;
	}
	/**
	 * 更新机构信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public JsonResponse updatePadOrgInfo(JsonRequest request) throws Exception{
		LoggerUtil.debug("更新PAD端机构信息服务开始---------------------------");
		
		FOX_PAD_TELLER_ORGINFO_DBO newOrgInfo = new FOX_PAD_TELLER_ORGINFO_DBO();
		
		newOrgInfo = BeanUtil.convertToBean(newOrgInfo, (JSONObject) request.get("reqData"));
		
		FOX_PAD_TELLER_ORGINFO_DBO whereOrgInfo = new FOX_PAD_TELLER_ORGINFO_DBO();
		
		whereOrgInfo.setORGID(newOrgInfo.getORGID());
		
		int result = DbServiceUtil.executeUpdate(newOrgInfo, whereOrgInfo);
		
		
		JsonResponse response = new JsonResponse();
		
		if(result!=1){
			response.put(IResponseConstant.retCode, IResponseConstant.FAILED_DB_ERROR);
			response.put(IResponseConstant.retMsg, "添加机构信息失败!");
		}else{
			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			response.put(IResponseConstant.retMsg, "添加机构信息成功!");
		}
		LoggerUtil.debug("更新PAD端机构信息服务结束---------------------------");
		return response;
	}
	/**
	 * 删除机构信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public JsonResponse deleteOrgInfoList(JsonRequest request) throws Exception{
		LoggerUtil.debug("删除PAD端机构信息服务开始---------------------------");
		
		JSONArray jsonArray = (JSONArray) request.get("list");
		DbParam dbParam = new DbParam();
		for(Object obj : jsonArray){
			FOX_PAD_TELLER_ORGINFO_DBO dbo = new FOX_PAD_TELLER_ORGINFO_DBO();
			
			dbo = BeanUtil.convertToBean(dbo, (JSONObject)obj);
			//将PARENT_ORG_NAME设为空值,过滤该字段
			dbo.setPARENT_ORG_NAME(null);
			
			DbParam objParam = DbEntityUtil.getDbParam(dbo, null, DbSqlType.Delete);
			
			dbParam.add(SqlBuildUtil.createDeleteSql(objParam), DbEntityUtil.getParaMap(dbo));
		}
		JsonResponse response = new JsonResponse();
		try {
			DbServiceUtil.executeTransaction(dbParam);
			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			response.put(IResponseConstant.retMsg, "删除机构信息成功!");
		} catch (Exception e) {
			LoggerUtil.error(e.getMessage(),e);
			response.put(IResponseConstant.retCode, IResponseConstant.FAILED_DB_ERROR);
			response.put(IResponseConstant.retMsg, "删除机构信息失败!");
		}
		LoggerUtil.debug("删除PAD端机构信息服务结束---------------------------");
		return response;
	}
	/**
	 * 查询机构信息,并调用toTree将数据转化为树状返回
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public JsonResponse queryPadOrgInfos(JsonRequest request) throws Exception{
		LoggerUtil.debug("查询PAD端机构信息服务开始---------------------------");
		
		String sql = "select o.*,p.ORG_CN_NAME as PARENT_ORG_NAME "+
				"from FOX_PAD_TELLER_ORGINFO o "+
				"left join FOX_PAD_TELLER_ORGINFO p on p.ORGID = o.PARENT_ORGID "+
				"order by cast(o.ORG_LEVEL as integer),cast(o.ORGID as integer)";
		
		List<FOX_PAD_TELLER_ORGINFO_DBO> orgInfos = DbServiceUtil.executeQuery(sql, FOX_PAD_TELLER_ORGINFO_DBO.class);
		
		JsonResponse response = new JsonResponse();
		
		if (orgInfos==null) {
			response.put(IResponseConstant.retCode, IResponseConstant.FAILED_DB_ERROR);
			response.put(IResponseConstant.retMsg, "查询失败");
		}else{
			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			response.put(IResponseConstant.retMsg, "查询成功");
			
			JsonArray jsonArray = toTree(new JsonObject(), "-1", orgInfos, 0, 1);
			String padOrgTreeData = jsonArray.toString();
			response.put("padOrgTreeData", padOrgTreeData);
		}
		LoggerUtil.debug("查询PAD端机构信息服务结束---------------------------");
		return response;
	}
	/**
	 * 将查询到的数据转化为树状
	 * @param jsonObject 
	 * @param parentId 父节点的编号
	 * @param list 	数据信息
	 * @param start 开始位置
	 * @param startLevel 初始级别
	 * @return
	 */
	private JsonArray toTree(JsonObject jsonObject, String parentId,
			List<FOX_PAD_TELLER_ORGINFO_DBO> list, int start, int startLevel) {
		JsonArray jsonArray = new JsonArray();
		Gson gson = new Gson();
		while (start < list.size()) {
			FOX_PAD_TELLER_ORGINFO_DBO dbo = list.get(start);
			if (dbo.getORG_LEVEL().equals(String.valueOf(startLevel))) {
				if (parentId.equals(dbo.getPARENT_ORGID())) {
					JsonObject jsonObject1 = gson.toJsonTree(dbo)
							.getAsJsonObject();
					//jsonObject1.get("CREATEDATE");
					/*if(jsonObject1.get("STATUS").toString().equals("2")){
						//状态不可用
						jsonObject1.addProperty("disabled", true);
						LoggerUtil.error("jsonObject1.get============="+jsonObject1.toString());
					}*/
					jsonArray.add(jsonObject1);
				}
				start++;
			} else {
				break;
			}
		}
		if (jsonArray.size() != 0) {
			jsonObject.add("children", jsonArray);
		}
		for (int i = 0; i < jsonArray.size(); i++) {
			JsonObject jsonObject2 = jsonArray.get(i).getAsJsonObject();
			String parentId1 = jsonObject2.get("ORGID").getAsString();
			toTree(jsonObject2, parentId1, list, start, startLevel + 1);
		}
		return jsonArray;
	}
	
	
	
	
	
}