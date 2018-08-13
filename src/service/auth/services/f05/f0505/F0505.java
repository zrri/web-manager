package service.auth.services.f05.f0505;

import both.common.util.DateUtil;
import both.common.util.LoggerUtil;
import both.common.util.StringUtilEx;
import both.constants.IResponseConstant;
import both.db.DBService;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;

import service.auth.core.f04.FOX_MGR_AUTH_ROLEINFO_DAO;
import service.auth.core.f04.FOX_MGR_AUTH_ROLEINFO_DBO;
import service.auth.core.f05.FOX_MGR_AUTH_FUNCGROUPINFO_DAO;
import service.auth.core.f05.FOX_MGR_AUTH_FUNCGROUPINFO_DBO;
import service.auth.core.f07.FOX_MGR_AUTH_FUNC_DAO;
import service.auth.core.f07.FOX_MGR_AUTH_FUNC_DBO;
import service.auth.core.f08.FOX_MGR_AUTH_ROLEFUNCMAPPING_DAO;
import service.auth.core.f08.FOX_MGR_AUTH_ROLEFUNCMAPPING_DBO;
import service.auth.core.f09.FOX_MGR_AUTH_GROUPFUNCMAPPING_DAO;
import service.auth.core.f09.FOX_MGR_AUTH_GROUPFUNCMAPPING_DBO;
import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;
import cn.com.bankit.phoenix.trade.Service;

/**
 * 新增-FOX_AUTH_FUNCGROUPINFO 功能组信息表
 */
public class F0505 extends Service<JsonRequest, JsonResponse> {

	/**
	 * 构造函数
	 */
	public F0505() {
		super();
	}
	public boolean isExistGroup(String GROUPID,String GROUPNAME) throws Exception{
		String sql = "select GROUP_ID,GROUP_NAME,GROUP_DESCRIBTION,CREATE_DATE,CREATE_TIME,UPDATE_DATE,UPDATE_TIME,STATUS from FOX_MGR_AUTH_FUNCGROUPINFO where group_id='"+GROUPID
				+"' or group_name='"+GROUPNAME+"'";  

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
        String GROUPID = request.getAsString("GROUPID");	
		// 交易组名称
		String GROUPNAME = request.getAsString("GROUPNAME");	
		 
	     boolean isexist= this.isExistGroup(GROUPID, GROUPNAME);
				 
		 if (isexist) {
					 
					response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
					response.put(IResponseConstant.retMsg, "查询成功");
					response.put("count", "1");
		 }
		 else{
			     response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
				response.put(IResponseConstant.retMsg, "查询成功");
				response.put("count", "0");
		 }

		return response;
	}
	
	//添加一个功能组
	public JsonResponse addGroupInfo(JsonRequest request) throws Exception {
		// 从请求报文中解析参数
				 
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
		dbo.set_CREATEDATE(DateUtil.getFormatDate());
		// 创建时间
		dbo.set_CREATETIME(DateUtil.getFormatDate());

		// 是否有效（1-是，0-否）默认1
		if(!StringUtilEx.isNullOrEmpty(STATUS)){
			dbo.set_STATUS(STATUS);
		}
		JsonResponse response = new JsonResponse();
	
		 boolean isexist= this.isExistGroup(GROUPID, GROUPNAME);
		 if (isexist) {
			 
				response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
				response.put(IResponseConstant.retMsg, "存在组编号或组名");
				response.put("count", "1");
				return response;
	       }
	       
         // 读取数据库
		int result = FOX_MGR_AUTH_FUNCGROUPINFO_DAO.insert(dbo);
	 
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
		
		FOX_MGR_AUTH_FUNCGROUPINFO_DBO wheredbo = new FOX_MGR_AUTH_FUNCGROUPINFO_DBO();
		if(!StringUtilEx.isNullOrEmpty(GROUPID)){
			wheredbo.set_GROUPID(GROUPID);
		}
		
		JsonResponse response = new JsonResponse();
		// 读取数据库
		int result = FOX_MGR_AUTH_FUNCGROUPINFO_DAO.update(dbo, wheredbo);
	 
	    if (result == -1) {
			response.put(IResponseConstant.retCode, IResponseConstant.FAILED);
					response.put(IResponseConstant.retMsg, "更新失败");
				} else {
					response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
					response.put(IResponseConstant.retMsg, "更新成功");
				}
				
		 return response;
}
	
	
	
	
	
	public JsonResponse queryGroupsFullTreeList(JsonRequest request) throws Exception {
		 

		// 鎷艰搴旂瓟鎶ユ枃
		JsonResponse response = new JsonResponse();
		
		// 璇诲彇鏁版嵁搴?
		String sql = "select  f.FUNC_ID,f.FUNC_NAME,f.URL_HTML,f.URL_JS,f.URL_CSS,f.REMARK,t.group_name,t.group_id  FROM FOX_MGR_AUTH_FUNC " +
				"f full JOIN  FOX_MGR_AUTH_GROUPFUNCMAPPING a  " +
				"on f.func_id=a.func_id full join " +
				" FOX_MGR_AUTH_FUNCGROUPINFO t on t.group_id=a.group_id";  

				List<Map<String, Object>> resultList = DBService.executeQuery(sql);
		LoggerUtil.error(sql);
		       if (resultList == null) {
					// 澶辫触
					response.put(IResponseConstant.retCode,
							IResponseConstant.FAILED_DB_ERROR);
					response.put(IResponseConstant.retMsg, "查询失败");
				} else {
					// 鎴愬姛
					response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
					response.put(IResponseConstant.retMsg, "查询成功");
					response.put("list", resultList);
				}

		return response;
	}
	//把一个未分组的功能添加到某个分组
	public JsonResponse addFunctionToGroup(JsonRequest request) throws Exception {
		 
				// 功能组编号
				String GROUPID = request.getAsString("GROUPID");	
				// 功能编号
				String FUNCID = request.getAsString("FUNCID");	
				// 生成DBO
				FOX_MGR_AUTH_GROUPFUNCMAPPING_DBO dbo = new FOX_MGR_AUTH_GROUPFUNCMAPPING_DBO();
				// 功能组编号
				if(!StringUtilEx.isNullOrEmpty(GROUPID)){
					dbo.set_GROUPID(GROUPID);
				}
				// 功能编号
				if(!StringUtilEx.isNullOrEmpty(FUNCID)){
					dbo.set_FUNCID(FUNCID);
				}
				JsonResponse response = new JsonResponse();
				// 读取数据库
				int result = FOX_MGR_AUTH_GROUPFUNCMAPPING_DAO.insert(dbo);
				if (result == -1) {
					response.put(IResponseConstant.retCode, IResponseConstant.FAILED);
			    	response.put(IResponseConstant.retMsg, "删除失败");
				} else {
					response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			    	response.put(IResponseConstant.retMsg, "删除成功");
				}
				return response;
	}
	//吧一个功能从他属于的分组中移除
	public JsonResponse deleteFunctionFromGroup(JsonRequest request) throws Exception {
		
		String GROUPID =request.getAsString("GROUPID");
		String FUNCID=request.getAsString("FUNCID");
		// 删除映射表  不知道为什么 返回-1，但是能删除
//		String sql="delete from fox_auth_groupfuncmapping where group_id='"
//		+GROUPID+"' and func_id='"+FUNCID+"'";
//	    boolean result2=DBService.execute(sql);
//	     
//	     LoggerUtil.error("-----------sql语句="+sql+"result= "+result2);
	     
		FOX_MGR_AUTH_GROUPFUNCMAPPING_DBO deletedbo = new FOX_MGR_AUTH_GROUPFUNCMAPPING_DBO();
		if(!StringUtilEx.isNullOrEmpty(FUNCID)){
			deletedbo.set_FUNCID(FUNCID);
		}
		if(!StringUtilEx.isNullOrEmpty(GROUPID)){
			deletedbo.set_GROUPID(GROUPID);
		}
		int result2=FOX_MGR_AUTH_GROUPFUNCMAPPING_DAO.delete(deletedbo);
	     
		JsonResponse response = new JsonResponse();
	    if(result2!=-1){
	    	response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
	    	response.put(IResponseConstant.retMsg, "删除成功");
	    }
	    else{
	    	response.put(IResponseConstant.retCode, IResponseConstant.FAILED);
	    	response.put(IResponseConstant.retMsg, "删除失败");
	    }

		return response;
		}
		
	
	
	
	
	//删除一个分组和属于分组下的所有功能
	public JsonResponse deleteGroupsAndGroupFuncs(JsonRequest request) throws Exception {
	
	String GROUPID =request.getAsString("GROUPID");
	//删除 组功能映射表，删除功能表，删除组功能表
	
	String sql = "select GROUP_ID,FUNC_ID from FOX_MGR_AUTH_GROUPFUNCMAPPING where group_id='"+GROUPID+"'";
	List<Map<String, Object>> resultList = DBService.executeQuery(sql);
	if(resultList!=null){
		 for (int i = 0,len = resultList.size(); i < len; i++) {
			 Map<String, Object> map=resultList.get(i);
			 String funcid=(String) map.get("FUNCID");
			 if(funcid!=null){
//				 //删除
//				  String sql2=" delete from fox_auth_func where func_id='"+funcid+"'";
//				  DBService.execute(sql2);
			
				  FOX_MGR_AUTH_FUNC_DBO deletedbo = new  FOX_MGR_AUTH_FUNC_DBO();
					if(!StringUtilEx.isNullOrEmpty(funcid)){
						deletedbo.set_FUNCID(funcid);
					}
					FOX_MGR_AUTH_FUNC_DAO.delete(deletedbo);
			
			 }
			 
		 }
	}
	//删除 mapping
//	sql="delete from fox_auth_groupfuncmapping where groupid='"+GROUPID+"'";
//	boolean result= DBService.execute(sql);
	FOX_MGR_AUTH_GROUPFUNCMAPPING_DBO deletedbo = new FOX_MGR_AUTH_GROUPFUNCMAPPING_DBO();
	if(!StringUtilEx.isNullOrEmpty(GROUPID)){
		deletedbo.set_GROUPID(GROUPID);
	}
	int result= FOX_MGR_AUTH_GROUPFUNCMAPPING_DAO.delete(deletedbo);
	 
	//删除group
//	sql="delete from fox_auth_funcgroupinfo where groupid='"+GROUPID+"'";
//    boolean result2=DBService.execute(sql);
	FOX_MGR_AUTH_FUNCGROUPINFO_DBO deletedbo2 = new FOX_MGR_AUTH_FUNCGROUPINFO_DBO();
	if(!StringUtilEx.isNullOrEmpty(GROUPID)){
		deletedbo2.set_GROUPID(GROUPID);
	}
     int result2=	FOX_MGR_AUTH_FUNCGROUPINFO_DAO.delete(deletedbo2);

	
	JsonResponse response = new JsonResponse();
    if(result2!=-1&&result!=-1){
    	response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
    	response.put(IResponseConstant.retMsg, "删除成功");
    }
    else{
    	response.put(IResponseConstant.retCode, IResponseConstant.FAILED);
    	response.put(IResponseConstant.retMsg, "删除失败");
    }

	return response;
	}
	
	/**
	 * execute
	 */
	public JsonResponse queryGroupInfoList(JsonRequest request) throws Exception {
		// 从请求报文中解析参数
		// 交易组编号
		String GROUPID = request.getAsString("GROUPID");	
		// 交易组名称
		String GROUPNAME = request.getAsString("GROUPNAME");	
		// 交易组描述
		String GROUPDESCRIBTION = request.getAsString("GROUPDESCRIBTION");	
		 
		// 是否有效（1-是，0-否）默认1
		String STATUS = request.getAsString("STATUS");	
		
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
		
	 
		// 是否有效（1-是，0-否）默认1
		if(!StringUtilEx.isNullOrEmpty(STATUS)){
			dbo.set_STATUS(STATUS);
		}
		
		StringBuffer buffer = new StringBuffer(" where ");
		// 鐢ㄦ埛浠ｅ彿
		if (!StringUtilEx.isNullOrEmpty(GROUPID)) {
			buffer.append("GROUP_ID ='" + GROUPID.trim() + "' and ");
		}
		// 鏌滈潰濮撳悕
		if (!StringUtilEx.isNullOrEmpty(GROUPNAME)) {
			buffer.append("GROUP_NAME ='" + GROUPNAME.trim() + "' and ");
		}
		// 鐢ㄦ埛绫诲瀷锛?-鏅€氱敤鎴凤紝1-BST鑷姩鐢ㄦ埛锛?-POS鑷姩鐢ㄦ埛,3-绉诲姩钀ラ攢鑷姩鐢ㄦ埛,4-ATM鑷姩鐢ㄦ埛,5-鐗硅壊涓氬姟鑷姩鐢ㄦ埛锛?
		if (!StringUtilEx.isNullOrEmpty(GROUPDESCRIBTION)) {
			buffer.append("GROUP_DESCRIBTION ='" +GROUPDESCRIBTION.trim() + "' and ");
		}
		// 褰掑睘鏈烘瀯
		if (!StringUtilEx.isNullOrEmpty(STATUS)) {
			buffer.append("STATUS ='" + STATUS.trim() + "' and ");
		}
		
		String whereString;
		
		if (buffer.length() > 8) {
			whereString = buffer.substring(0, buffer.length() - 5);
		} else if (buffer.length() == 7) {
			whereString = buffer.replace(0, 7, "").toString();
		} else {
			whereString = buffer.toString();
		}

		// 鎷艰搴旂瓟鎶ユ枃
		JsonResponse response = new JsonResponse();
		
		String sql = "select GROUP_ID,GROUP_NAME,GROUP_DESCRIBTION,CREATE_DATE,CREATE_TIME,UPDATE_DATE,UPDATE_TIME,STATUS from FOX_MGR_AUTH_FUNCGROUPINFO "+whereString;  

		List<Map<String, Object>> resultList = DBService.executeQuery(sql);
	    if (resultList == null) {
			// 澶辫触
			response.put(IResponseConstant.retCode,
					IResponseConstant.FAILED_DB_ERROR);
			response.put(IResponseConstant.retMsg, "查询失败");
		} else {
			// 鎴愬姛
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