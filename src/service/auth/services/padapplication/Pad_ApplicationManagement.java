package service.auth.services.padapplication;

import both.common.util.DateUtil;
import both.common.util.LoggerUtil;
import both.common.util.StringUtilEx;
import both.constants.IResponseConstant;
import both.db.DBService;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;

import service.auth.core.f07.FOX_MGR_AUTH_FUNC_DAO;
import service.auth.core.f07.FOX_MGR_AUTH_FUNC_DBO;
import service.auth.core.padapplication.FOX_PAD_FUNCTION_DBO;
import service.auth.core.padapplication.FOX_PAD_FUNCTION_DAO;
import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;
import cn.com.bankit.phoenix.trade.Service;

/**
 * 新增-FOX_PAD_FUNCTION 功能组信息表
 */
public class Pad_ApplicationManagement extends Service<JsonRequest, JsonResponse> {

	/**
	 * 构造函数
	 */
	public Pad_ApplicationManagement() {
		super();
	}
	public boolean isExistGroup(String GROUPID,String GROUPNAME) throws Exception{
		String sql = "select FUNC_ID,FUNC_NAME from FOX_PAD_FUNCTION where func_id='"+GROUPID
				+"' or func_name='"+GROUPNAME+"'";  

				List<Map<String, Object>> resultList = DBService.executeQuery(sql);
		 
		       if (resultList == null) {
					 return false;
				} else {
					return resultList.size()>0;
			 }
	}
    //判断组名或组编号是否已经存在
	public JsonResponse isExistGroupIDorName(JsonRequest request) throws Exception {
	
        JsonResponse response = new JsonResponse();
		
		String FUNC_ID = request.getAsString("FUNC_ID");			
		String FUNC_NAME = request.getAsString("FUNC_NAME");						
	 
		StringBuffer buffer = new StringBuffer(" where ");

		if (!StringUtilEx.isNullOrEmpty(FUNC_ID)) {
			buffer.append("FUNC_ID='" + FUNC_ID.trim() + "' or ");
		}
		
		if (!StringUtilEx.isNullOrEmpty(FUNC_NAME)) {
			buffer.append("FUNC_NAME='" + FUNC_NAME.trim() + "' or ");
		}
		
		String whereString="";
		
		if (buffer.length() > 8) {
			whereString = buffer.substring(0, buffer.length() - 4);
		} 
		 
		String sql = "select FUNC_ID,FUNC_NAME from FOX_PAD_FUNCTION"+whereString;
		
		List<Map<String, Object>> resultList = DBService.executeQuery(sql);
				 
		 if (resultList != null) {				 
				response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
				response.put(IResponseConstant.retMsg, "已存在");
				response.put("count", resultList.size());
		 }
		 else{
			    response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
				response.put(IResponseConstant.retMsg, "不存在");
				response.put("count", "0");
		 }
	
		return response;
	}
	
	//添加一个功能组
	public JsonResponse addGroupInfo(JsonRequest request) throws Exception {
		// 从请求报文中解析参数
		//功能组编号		 
		String FUNC_ID = request.getAsString("FUNC_ID");	
		// 功能组名称
		String FUNC_NAME = request.getAsString("FUNC_NAME");
		// 功能组描述
		String FUNC_DESCRIPTION = request.getAsString("FUNC_DESCRIPTION");
		// 路由编号
		String ROUTE_ID = request.getAsString("ROUTE_ID");	
		//是否功能组	 
		String ISROOT = request.getAsString("ISROOT");
		//父ID
		String PARENT_ID = request.getAsString("PARENT_ID");
		//功能组ID
		String FUNC_GROUP_ID = request.getAsString("FUNC_GROUP_ID");
		// 功能组排序号
		String SORT_NO = request.getAsString("SORT_NO");	
					

		// 生成DBO
		FOX_PAD_FUNCTION_DBO dbo=new FOX_PAD_FUNCTION_DBO();
		// 功能组编号
		if(!StringUtilEx.isNullOrEmpty(FUNC_ID)){
			dbo.set_FUNCID(FUNC_ID);
		}
		// 功能组名称
		if(!StringUtilEx.isNullOrEmpty(FUNC_NAME)){
			dbo.set_FUNCNAME(FUNC_NAME);
		}
		// 功能组描述
		if(!StringUtilEx.isNullOrEmpty(FUNC_DESCRIPTION)){
			dbo.set_FUNCDESCRIPTION(FUNC_DESCRIPTION);
		}
		// 路由编号
		if(!StringUtilEx.isNullOrEmpty(ROUTE_ID)){
			dbo.set_ROUTEID(ROUTE_ID);
		}
		// 是否功能组
		if(!StringUtilEx.isNullOrEmpty(ISROOT)){
			dbo.set_ISROOT(ISROOT);
		}

		// 父ID
		if(!StringUtilEx.isNullOrEmpty(PARENT_ID)){
			dbo.set_PARENTID(PARENT_ID);
		}

		// 功能组ID
		if(!StringUtilEx.isNullOrEmpty(FUNC_GROUP_ID)){
			dbo.set_FUNCGROUPID(FUNC_GROUP_ID);
		}
		// 排序号
		if(!StringUtilEx.isNullOrEmpty(SORT_NO)){
			dbo.set_SORTNO(SORT_NO);
		}
		
		JsonResponse response = new JsonResponse();
	
		 boolean isexist= this.isExistGroup(FUNC_ID, FUNC_NAME);
		 if (isexist) {
			 
				response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
				response.put(IResponseConstant.retMsg, "存在组编号或组名");
				response.put("count", "1");
				return response;
	       }
	       
         // 读取数据库
		int result = FOX_PAD_FUNCTION_DAO.insert(dbo);
	 
	    if (result == -1) {
			response.put(IResponseConstant.retCode, IResponseConstant.FAILED);
					response.put(IResponseConstant.retMsg, "新增失败");
				} else {
					response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
					response.put(IResponseConstant.retMsg, "新增成功");
					response.put("count", "0");
				}
				
	    return response;
	}
	
	public JsonResponse editGroupInfo(JsonRequest request) throws Exception {
		// 从请求报文中解析参数
		//功能组编号		 
		String FUNC_ID = request.getAsString("FUNC_ID");	
		// 功能组名称
		String FUNC_NAME = request.getAsString("FUNC_NAME");
		// 功能组描述
		String FUNC_DESCRIPTION = request.getAsString("FUNC_DESCRIPTION");
		// 路由编号
		String ROUTE_ID = request.getAsString("ROUTE_ID");	
		//是否功能组	 
		String ISROOT = request.getAsString("ISROOT");
		//父ID
		String PARENT_ID = request.getAsString("PARENT_ID");
		//功能组ID
		String FUNC_GROUP_ID = request.getAsString("FUNC_GROUP_ID");
		// 功能组排序号
		String SORT_NO = request.getAsString("SORT_NO");	
					

		// 生成DBO
		FOX_PAD_FUNCTION_DBO dbo=new FOX_PAD_FUNCTION_DBO();
		// 功能组编号
		if(!StringUtilEx.isNullOrEmpty(FUNC_ID)){
			dbo.set_FUNCID(FUNC_ID);
		}
		// 功能组名称
		if(!StringUtilEx.isNullOrEmpty(FUNC_NAME)){
			dbo.set_FUNCNAME(FUNC_NAME);
		}
		// 功能组描述
		if(!StringUtilEx.isNullOrEmpty(FUNC_DESCRIPTION)){
			dbo.set_FUNCDESCRIPTION(FUNC_DESCRIPTION);
		}
		// 路由编号
		if(!StringUtilEx.isNullOrEmpty(ROUTE_ID)){
			dbo.set_ROUTEID(ROUTE_ID);
		}
		// 是否功能组
		if(!StringUtilEx.isNullOrEmpty(ISROOT)){
			dbo.set_ISROOT(ISROOT);
		}

		// 父ID
		if(!StringUtilEx.isNullOrEmpty(PARENT_ID)){
			dbo.set_PARENTID(PARENT_ID);
		}

		// 功能组ID
		if(!StringUtilEx.isNullOrEmpty(FUNC_GROUP_ID)){
			dbo.set_FUNCGROUPID(FUNC_GROUP_ID);
		}
		// 排序号
		if(!StringUtilEx.isNullOrEmpty(SORT_NO)){
			dbo.set_SORTNO(SORT_NO);
		}
		
		FOX_PAD_FUNCTION_DBO wheredbo = new FOX_PAD_FUNCTION_DBO();
		if(!StringUtilEx.isNullOrEmpty(FUNC_ID)){
			wheredbo.set_FUNCID(FUNC_ID);
		}
		
		JsonResponse response = new JsonResponse();
		// 读取数据库
		int result = FOX_PAD_FUNCTION_DAO.update(dbo, wheredbo);
	 
	    if (result == -1) {
			response.put(IResponseConstant.retCode, IResponseConstant.FAILED);
					response.put(IResponseConstant.retMsg, "更新失败");
				} else {
					response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
					response.put(IResponseConstant.retMsg, "更新成功");
				}
				
		 return response;
	}
				
	//获取pad功能列表
	public JsonResponse queryGroupsFullTreeList(JsonRequest request) throws Exception {
		
		String FUNC_ID = request.getAsString("FUNC_ID");					
	 
		StringBuffer buffer = new StringBuffer(" where ");

		if (!StringUtilEx.isNullOrEmpty(FUNC_ID)) {
			buffer.append("FUNC_ID='" + FUNC_ID.trim() + "' and ");
		}		
		
		String whereString="";
		
		if (buffer.length() > 8) {
			whereString = buffer.substring(0, buffer.length() - 5);
		} 
		 
		JsonResponse response = new JsonResponse();
		
		String sql = "select FUNC_ID,FUNC_NAME,FUNC_DESCRIPTION,ROUTE_ID,ISROOT,PARENT_ID,FUNC_GROUP_ID,SORT_NO FROM FOX_PAD_FUNCTION"+whereString;				

		List<Map<String, Object>> resultList = DBService.executeQuery(sql);
		LoggerUtil.error(sql);
        if (resultList == null) {
			
			response.put(IResponseConstant.retCode,IResponseConstant.FAILED_DB_ERROR);
			response.put(IResponseConstant.retMsg, "查询失败");
		} else {
			
			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			response.put(IResponseConstant.retMsg, "查询成功");
			response.put("list", resultList);
		}

		return response;
	}
	
	//把一个未分组的功能添加到某个分组
	public JsonResponse addFunctionToGroup(JsonRequest request) throws Exception {
		 
		// 父ID（功能组编号）
		String PARENT_ID = request.getAsString("PARENT_ID");	
		// 功能编号
		String FUNC_ID = request.getAsString("FUNC_ID");	
		// 生成DBO
		FOX_PAD_FUNCTION_DBO dbo = new FOX_PAD_FUNCTION_DBO();
		// 功能组编号
		if(!StringUtilEx.isNullOrEmpty(PARENT_ID)){
			dbo.set_PARENTID(PARENT_ID);
		}		
		
		FOX_PAD_FUNCTION_DBO wheredbo = new FOX_PAD_FUNCTION_DBO();
		if(!StringUtilEx.isNullOrEmpty(FUNC_ID)){
			wheredbo.set_FUNCID(FUNC_ID);
		}
		
		JsonResponse response = new JsonResponse();
		// 读取数据库
		int result = FOX_PAD_FUNCTION_DAO.update(dbo,wheredbo);
		if (result == -1) {
			response.put(IResponseConstant.retCode, IResponseConstant.FAILED);
	    	response.put(IResponseConstant.retMsg, "加入失败");
		} else {
			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
	    	response.put(IResponseConstant.retMsg, "加入成功");
		}
		return response;
	}
	
	//把一个功能从它属于的分组中移除
	public JsonResponse deleteFunctionFromGroup(JsonRequest request) throws Exception {
		
		String FUNC_ID=request.getAsString("FUNC_ID");
		
		String sql="update FOX_PAD_FUNCTION set PARENT_ID=null where FUNC_ID='"+FUNC_ID+"'";
		boolean result=DBService.execute(sql);
	     
		JsonResponse response = new JsonResponse();
	    if(!result){
	    	response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
	    	response.put(IResponseConstant.retMsg, "移除成功");
	    }
	    else{
	    	response.put(IResponseConstant.retCode, IResponseConstant.FAILED);
	    	response.put(IResponseConstant.retMsg, "移除失败");
	    }

		return response;
	}
			
	
	//删除一个分组
	public JsonResponse deleteGroupsAndGroupFuncs(JsonRequest request) throws Exception {
	
		String FUNC_ID =request.getAsString("FUNC_ID");
		
		String sql = "delete from FOX_PAD_FUNCTION where FUNC_ID='"+FUNC_ID+"'";	    
		boolean result=DBService.execute(sql);
		
		String sql2="update FOX_PAD_FUNCTION set PARENT_ID=null where PARENT_ID='"+FUNC_ID+"'";	    
		boolean result2=DBService.execute(sql2);
		
		JsonResponse response = new JsonResponse();
		
	    if(!result&&!result2){
	    	response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
	    	response.put(IResponseConstant.retMsg, "删除成功");
	    }
	    else{
	    	response.put(IResponseConstant.retCode, IResponseConstant.FAILED);
	    	response.put(IResponseConstant.retMsg, "删除失败");
	    }
	
		return response;
	}
	
	// 删除一条或多条数据
	public JsonResponse deleteFuncList(JsonRequest request) throws Exception {
		LoggerUtil.error("-------------开始删除------------");
		JSONArray deleteJsonArray = (JSONArray) request.get("list");

		for (int i = 0; i < deleteJsonArray.size(); i++) {
			String FUNC_ID = (String) deleteJsonArray.get(i);
			FOX_PAD_FUNCTION_DBO dbo = new FOX_PAD_FUNCTION_DBO();
			dbo.set_FUNCID(FUNC_ID);
			FOX_PAD_FUNCTION_DAO.delete(dbo);		
		}
		JsonResponse response = new JsonResponse();
		response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
		response.put(IResponseConstant.retMsg, "删除成功");
		return response;
	}
	
	//查询pad功能列表
	public JsonResponse queryGroupInfoList(JsonRequest request) throws Exception {
		
		// 功能编号
		String FUNC_ID = request.getAsString("FUNC_ID");	
		// 功能名称
		String FUNC_NAME = request.getAsString("FUNC_NAME");						
	 
		StringBuffer buffer = new StringBuffer(" where ");

		if (!StringUtilEx.isNullOrEmpty(FUNC_ID)) {
			buffer.append("e.FUNC_ID like '%" + FUNC_ID.trim() + "%' and ");
		}
		
		if (!StringUtilEx.isNullOrEmpty(FUNC_NAME)) {
			buffer.append("e.FUNC_NAME like '%" + FUNC_NAME.trim() + "%' and ");
		}
		
		String whereString="";
		
		if (buffer.length() > 8) {
			whereString = buffer.substring(0, buffer.length() - 5);
		} 
		
		JsonResponse response = new JsonResponse();
		
		String sql = "select  e.FUNC_ID,e.FUNC_NAME,e.FUNC_DESCRIPTION,e.ROUTE_ID,e.ISROOT,e.PARENT_ID,e.FUNC_GROUP_ID,e.SORT_NO,f.FUNC_NAME GROUP_NAME FROM FOX_PAD_FUNCTION e left join FOX_PAD_FUNCTION f on e.PARENT_ID=f.FUNC_ID"+whereString;  
		
		List<Map<String, Object>> resultList = DBService.executeQuery(sql);
	    if (resultList == null) {
			response.put(IResponseConstant.retCode,IResponseConstant.FAILED_DB_ERROR);
			response.put(IResponseConstant.retMsg, "查询失败");
		} else {
			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			response.put(IResponseConstant.retMsg, "查询成功");
			response.put("list", resultList);
		}	    

		return response;
	}

	@Override
	public JsonResponse execute(JsonRequest arg0) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}