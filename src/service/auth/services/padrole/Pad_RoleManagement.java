package service.auth.services.padrole;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;


import both.common.util.DateUtil;
import both.common.util.LoggerUtil;
import both.common.util.StringUtilEx;
import both.constants.IResponseConstant;
import both.db.DBService;
import both.db.DbParam;
import both.db.DbParam.DbSqlType;
import both.db.util.DbEntityUtil;
import both.db.util.DbServiceUtil;
import both.db.util.SqlBuildUtil;

import service.auth.core.padrole.FOX_PAD_TELLER_ROLEINFO_DBO;
import service.auth.core.padrolefunc.FOX_PAD_TELLER_ROLEFUNC_DBO;
import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;
import cn.com.bankit.phoenix.trade.Service;

public class Pad_RoleManagement extends Service<JsonRequest, JsonResponse>{
	@Override
	public JsonResponse execute(JsonRequest arg0) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	//添加角色
	public JsonResponse addRoleInfo(JsonRequest request) throws Exception{
		LoggerUtil.error("开始添加PAD角色树服务---------------------------");
		
		DbParam dbParam = new DbParam();
		//获取前台传来的数据
		String ROLE_ID=request.getAsString("ROLE_ID");
		
		String ROLE_NAME = request.getAsString("ROLE_NAME");
		
		String ROLE_DESCRIPTION = request.getAsString("ROLE_DESCRIPTION");
		
		JSONArray checkedArr = (JSONArray) request.get("checkedArr");
		
		
		FOX_PAD_TELLER_ROLEINFO_DBO roleInfo = new FOX_PAD_TELLER_ROLEINFO_DBO();
		
		if(!StringUtilEx.isNullOrEmpty(ROLE_ID)){
			roleInfo.setROLE_ID(ROLE_ID);
		};
		if(!StringUtilEx.isNullOrEmpty(ROLE_NAME)){
			roleInfo.setROLE_NAME(ROLE_NAME);
		};
		if(!StringUtilEx.isNullOrEmpty(ROLE_DESCRIPTION)){
			roleInfo.setROLE_DESCRIPTION(ROLE_DESCRIPTION);
		};
		roleInfo.setCREATE_DATE(DateUtil.dataToString(new Date()));
		
		roleInfo.setISVOID("1");
		
		DbParam roleParam = DbEntityUtil.getDbParam(roleInfo, null, DbSqlType.Insert);
		
		dbParam.add(SqlBuildUtil.createInsertSql(roleParam), DbEntityUtil.getParaMap(roleInfo));
		
		if(checkedArr!=null){
			for(Object funcID   : checkedArr){
				FOX_PAD_TELLER_ROLEFUNC_DBO roleFunc = new FOX_PAD_TELLER_ROLEFUNC_DBO();
				
				roleFunc.setROLE_ID(ROLE_ID);
				roleFunc.setFUNC_ID((String) funcID);
				
				DbParam roleFuncParam = DbEntityUtil.getDbParam(roleFunc, null, DbSqlType.Insert);
				
				dbParam.add(SqlBuildUtil.createInsertSql(roleFuncParam), DbEntityUtil.getParaMap(roleFunc));
			}
		}
		JsonResponse response = new JsonResponse();
		try {
			DbServiceUtil.executeTransaction(dbParam);
			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			response.put(IResponseConstant.retMsg, "添加成功");
		} catch (Exception e) {
			LoggerUtil.error(e.getMessage(),e);
			response.put(IResponseConstant.retCode, IResponseConstant.FAILED_DB_ERROR);
			response.put(IResponseConstant.retMsg, "添加失败");
		}
		LoggerUtil.error("结束添加PAD角色树服务---------------------------");
		return response;
	}
	//更新角色
	public JsonResponse updateRoleInfo(JsonRequest request) throws Exception{
		LoggerUtil.error("开始添加PAD角色树服务---------------------------");
		
		DbParam dbParam = new DbParam();
		//获取前台传来的数据
		String ROLE_ID=request.getAsString("ROLE_ID");
		
		String ROLE_NAME = request.getAsString("ROLE_NAME");
		
		String ROLE_DESCRIPTION = request.getAsString("ROLE_DESCRIPTION");
		
		JSONArray checkedArr = (JSONArray) request.get("checkedArr");
		
		
		FOX_PAD_TELLER_ROLEINFO_DBO roleInfo = new FOX_PAD_TELLER_ROLEINFO_DBO();
		
		if(!StringUtilEx.isNullOrEmpty(ROLE_ID)){
			roleInfo.setROLE_ID(ROLE_ID);
		};
		if(!StringUtilEx.isNullOrEmpty(ROLE_NAME)){
			roleInfo.setROLE_NAME(ROLE_NAME);
		};
		if(!StringUtilEx.isNullOrEmpty(ROLE_DESCRIPTION)){
			roleInfo.setROLE_DESCRIPTION(ROLE_DESCRIPTION);
		};
		roleInfo.setUPDATE_DATE(DateUtil.dataToString(new Date()));
		
		FOX_PAD_TELLER_ROLEINFO_DBO whereRoleInfo = new FOX_PAD_TELLER_ROLEINFO_DBO();
		
		whereRoleInfo.setROLE_ID(ROLE_ID);
		
		DbParam roleParam = DbEntityUtil.getDbParam(whereRoleInfo, roleInfo, DbSqlType.Update);
		
		dbParam.add(SqlBuildUtil.createUpdateSql(roleParam), DbEntityUtil.getParaMap(roleInfo));
		
		//删除原有的关联信息
		FOX_PAD_TELLER_ROLEFUNC_DBO deleteRoleFunc = new FOX_PAD_TELLER_ROLEFUNC_DBO();
		deleteRoleFunc.setROLE_ID(ROLE_ID);
		DbParam deleteRoleFuncParam = DbEntityUtil.getDbParam(deleteRoleFunc, null, DbSqlType.Delete);
		dbParam.add(SqlBuildUtil.createDeleteSql(deleteRoleFuncParam), DbEntityUtil.getParaMap(deleteRoleFunc));
		
		//新增新的关联信息
		if(checkedArr!=null){
			for(Object funcID   : checkedArr){
				FOX_PAD_TELLER_ROLEFUNC_DBO roleFunc = new FOX_PAD_TELLER_ROLEFUNC_DBO();
				
				roleFunc.setROLE_ID(ROLE_ID);
				roleFunc.setFUNC_ID((String) funcID);
				
				DbParam roleFuncParam = DbEntityUtil.getDbParam(roleFunc, null, DbSqlType.Insert);
				
				dbParam.add(SqlBuildUtil.createInsertSql(roleFuncParam), DbEntityUtil.getParaMap(roleFunc));
			}
		}
		JsonResponse response = new JsonResponse();
		try {
			DbServiceUtil.executeTransaction(dbParam);
			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			response.put(IResponseConstant.retMsg, "更新成功");
		} catch (Exception e) {
			LoggerUtil.error(e.getMessage(),e);
			response.put(IResponseConstant.retCode, IResponseConstant.FAILED_DB_ERROR);
			response.put(IResponseConstant.retMsg, "更新失败");
		}
		LoggerUtil.error("结束添加PAD角色树服务---------------------------");
		return response;
	}
	
	//删除角色
	public JsonResponse deleteRolesInfo(JsonRequest request) throws Exception{
		LoggerUtil.error("开始删除PAD角色服务---------------------------");
		JsonResponse response = new JsonResponse();
		
		JSONArray jsonArray = (JSONArray) request.get("deleteRolesArr");
		
		DbParam dbParam = new DbParam();
		
		for (int i = 0; i < jsonArray.size(); i++) {
			String ROLE_ID = (String) jsonArray.get(i);
			
			FOX_PAD_TELLER_ROLEINFO_DBO role = new FOX_PAD_TELLER_ROLEINFO_DBO();
			role.setROLE_ID(ROLE_ID);
			DbParam roleParm = DbEntityUtil.getDbParam(role, null, DbSqlType.Delete);
			
			dbParam.add(SqlBuildUtil.createDeleteSql(roleParm), DbEntityUtil.getParaMap(role));
			
			FOX_PAD_TELLER_ROLEFUNC_DBO roleFunc = new FOX_PAD_TELLER_ROLEFUNC_DBO();
			roleFunc.setROLE_ID(ROLE_ID);
			
			DbParam roleFuncParm = DbEntityUtil.getDbParam(roleFunc, null, DbSqlType.Delete);
			
			dbParam.add(SqlBuildUtil.createDeleteSql(roleFuncParm), DbEntityUtil.getParaMap(roleFunc));
		}
		try {
			DbServiceUtil.executeTransaction(dbParam);
			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			response.put(IResponseConstant.retMsg, "删除成功");
		} catch (Exception e) {
			LoggerUtil.error(e.getMessage(),e);
			response.put(IResponseConstant.retCode, IResponseConstant.FAILED_DB_ERROR);
			response.put(IResponseConstant.retMsg, "删除失败");
		}
		LoggerUtil.error("结束删除PAD角色服务---------------------------");
		return response;
	}
	//查询PAD角色信息
		public JsonResponse queryRoleInfoList(JsonRequest request) throws Exception{
			LoggerUtil.error("开始查询PAD角色服务---------------------------");
			JsonResponse response = new JsonResponse();

			String ROLE_ID = request.getAsString("ROLE_ID");
			String ROLE_NAME = request.getAsString("ROLE_NAME");
			
			String SELECTTYPE = request.getAsString("SELECTTYPE");
			
			StringBuffer whereString = new StringBuffer();
			if (!StringUtilEx.isNullOrEmpty(ROLE_ID)) {
				if("query".equals(SELECTTYPE)){
					whereString.append("and r.ROLE_ID like '%'||'"+ROLE_ID+"'||'%' ");
				}else{
					whereString.append("and r.ROLE_ID ='"+ROLE_ID+"'");
				}
			}
			if (!StringUtilEx.isNullOrEmpty(ROLE_NAME)) {
				whereString.append("and r.ROLE_NAME like '%'||'"+ROLE_NAME+"'||'%' ");
			}
			
			String sql = "select r.ROLE_ID,r.ROLE_NAME,r.ROLE_DESCRIPTION,r.CREATE_DATE,r.UPDATE_DATE,r.ISVOID ,f.FUNC_NAME,f.FUNC_ID "
					+" from FOX_PAD_TELLER_ROLEINFO r "
					+" left join FOX_PAD_TELLER_ROLEFUNC rf on r.ROLE_ID=rf.ROLE_ID "
					+" left join FOX_PAD_FUNCTION f on rf.FUNC_ID = f.FUNC_ID where 1=1 " + whereString;
			
			List<Map<String, Object>> result = DBService.executeQuery(sql);
			
			if (result ==null) {
				response.put(IResponseConstant.retCode, IResponseConstant.FAILED_DB_ERROR);
				response.put(IResponseConstant.retMsg, "查询失败");
			}else{
				response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
				response.put(IResponseConstant.retMsg, "查询成功");
				response.put("list", result);
			}
			LoggerUtil.error("结束查询PAD角色服务---------------------------");
			return response;
		}
	//查询功能树
	public JsonResponse queryPadFuncGroupTreeList(JsonRequest request) throws Exception{
		LoggerUtil.error("开始查询PAD功能树服务---------------------------");
		JsonResponse response = new JsonResponse();
		
		String sql = "select f.FUNC_ID,f.FUNC_NAME,f.FUNC_DESCRIPTION,f.ROUTE_ID,f.ISROOT,f.PARENT_ID,f.SORT_NO,p.FUNC_NAME as PARENT_NAME "
				+" from FOX_PAD_FUNCTION f "
				+" left join FOX_PAD_FUNCTION p on p.FUNC_ID=f.PARENT_ID "
				+" order by cast(f.ISROOT as integer),cast(f.Parent_Id as integer)";
		
		List<Map<String, Object>> result = DBService.executeQuery(sql);
		
		if (result ==null) {
			response.put(IResponseConstant.retCode, IResponseConstant.FAILED_DB_ERROR);
			response.put(IResponseConstant.retMsg, "查询失败");
		}else{
			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			response.put(IResponseConstant.retMsg, "查询成功");
			response.put("list", result);
		}
		LoggerUtil.error("结束查询PAD功能树服务---------------------------");
		return response;
	}

}