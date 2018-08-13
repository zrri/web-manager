package service.common.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import both.common.util.StringUtilEx;
import both.db.DBService;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;

/**
 * 公共系统
 */
public class CommonSystem {

	/**
	 * 构造函数
	 */
	public CommonSystem() {
		super();
	}
	
	/**
	 * 登录操作
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public JsonResponse queryOrgInfo(JsonRequest request) throws Exception {
		JsonResponse response = new JsonResponse();
		
		String sql = "SELECT " +
					"ORGID," +//机构码
					"ORG_CN_NAME," +//机构名称
					"BRANCH_ID," +//
					"PARENT_ORGID " +//父机构码
//					"BANKFALG " +//银行标志
				"FROM " +
					"FOX_TELLER_ORGINFO " +
				"WHERE " +
					"1=1 ";

		List<Map<String, Object>> res = DBService.executeQuery(sql);
		
		Map<String,Map<String, Object>> dataTmp = new HashMap<String,Map<String, Object>>();
		
		Map<String,JSONObject> treeData = new HashMap<String,JSONObject>();
		
		JSONArray orginfo = new JSONArray();
		//遍历当前业务品种下的所有的产品信息
		if(null != res){
			//第一次遍历为了
			for(int i = 0;i < res.size();i++) {
				JSONObject object = new JSONObject();
				String orgid = StringUtilEx.convertNullToEmpty(res.get(i).get("ORGID"));
				//机构码
				object.put("ORGID", orgid);
				//机构名称
				object.put("ORGCHINAME", StringUtilEx.convertNullToEmpty(res.get(i).get("ORGCHINAME")));
				//
				object.put("BRANCHID", StringUtilEx.convertNullToEmpty(res.get(i).get("BRANCHID")));
				//父机构码
				object.put("PARENTORGID", StringUtilEx.convertNullToEmpty(res.get(i).get("PARENTORGID")));
				//银行标志
//				object.put("BANKFALG", StringUtilEx.convertNullToEmpty(res.get(i).get("BANKFALG")));

//				dataTmp.put(orgid, res.get(i));
//				treeData.put(orgid, object);
//				
				orginfo.add(object);
			}
			
//			for(int i=0;i<res.size();i++) {
//				JSONObject object = new JSONObject();
//				//机构码
//				String orgid = StringUtilEx.convertNullToEmpty(res.get(i).get("ORGID"));
//				//父机构码
//				String parentOrgId = StringUtilEx.convertNullToEmpty(res.get(i).get("PARENTORGID"));
//				//机构码
//				object.put("ORGID", orgid);
//				//机构名称
//				object.put("ORGCHINAME", StringUtilEx.convertNullToEmpty(res.get(i).get("ORGCHINAME")));
//				//
//				object.put("BRANCHID", StringUtilEx.convertNullToEmpty(res.get(i).get("BRANCHID")));
//				//父机构码
//				object.put("PARENTORGID", parentOrgId);
//				if(!"0".equals(parentOrgId)) {
//					
//				//0的话为跟节点
//				} else {
//					orginfo.add(object);
//				}
//			}
		}else {
//			response.getHeader().setRetCode("4444");
//			response.getHeader().setRetMsg("");
			response.put("errormsg", "查询数据异常。");
			response.put("errorcode", "4444");
		}

		response.put("data", orginfo);

		return response;
	}
	
	public void f () {
		
	}
	
	/**
	 * 更新机构信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public JsonResponse updateOrgInfo(JsonRequest request) throws Exception {
		JsonResponse response = new JsonResponse();
		
		String orgid = request.getAsString("orgid");
		String orgname = request.getAsString("orgname");
		String parentnode = request.getAsString("parentnode");
		
		String sql = "UPDATE FOX_TELLER_ORGINFO SET " +
//				"ORGID='"+orgid+"' " +//机构码
				"ORGCHINAME='"+ orgname+"' "+//机构名称
				",PARENTORGID='" +parentnode+"' " +//父机构码
//				"BANKFALG " +//银行标志
			"WHERE  " +
				"ORGID='" +orgid+"' AND 1=1 ";

		int size = DBService.update(sql);
		
		if(size ==0) {
			response.put("errormsg", "更新失败");
			response.put("errorcode", "4444");
		}
		
	
		return response;
	}
	
	/**
	 * 更新机构信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public JsonResponse addOrgInfo(JsonRequest request) throws Exception {
		JsonResponse response = new JsonResponse();
		
		String orgid = request.getAsString("orgid");
		String orgname = request.getAsString("orgname");
		String parentnode = request.getAsString("parentnode");
		
		String sql = "INSERT " +
				"INTO " +
					"FOX_TELLER_ORGINFO " +
					"(ORGID,ORGCHINAME,PARENTORGID,BANKFLAG) " +
				"VALUES " +
					"('"+orgid+"','"+orgname+"','"+parentnode+"','01')";
//				

		boolean res = DBService.execute(sql);
		
		if(!res) {
			response.put("errormsg", "插入失败");
			response.put("errorcode", "4444");
		} 
	
		return response;
	}
	
	/**
	 * 更新机构信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public JsonResponse removeOrgInfo(JsonRequest request) throws Exception {
		JsonResponse response = new JsonResponse();
		
		String orgid = request.getAsString("orgid");
		
		
		String sql = "DELETE FROM FOX_TELLER_ORGINFO WHERE ORGID='" + orgid +"'";
	
		boolean res = DBService.execute(sql);
		
	
		return response;
	}
}