package service.auth.services.f06.f0605;

import both.common.util.LoggerUtil;
import both.common.util.StringUtilEx;
import both.constants.IResponseConstant;
import both.db.DBService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.fastjson.JSON;

import service.auth.core.f06.FOX_MGR_AUTH_FUNCMENUTREE_DAO;
import service.auth.core.f06.FOX_MGR_AUTH_FUNCMENUTREE_DBO;
import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;
import cn.com.bankit.phoenix.trade.Service;

/**
 * 查询-FOX_AUTH_FUNCMENUTREE 功能菜单树信息表
 */
public class F0605 extends Service<JsonRequest, JsonResponse> {

	/**
	 * 构造函数
	 */
	public F0605() {
		super();
	}

	/**
	 * execute
	 */
	public JsonResponse execute(JsonRequest request) throws Exception {
		// 从请求报文中解析参数
		// 功能编号
		String FUNCID = request.getAsString("FUNCID");
		// 菜单名称
		String DISPLAYNAME = request.getAsString("DISPLAYNAME");
		// 节点类型（F-目录节点，L-叶子节点即菜单节点）
		String NODETYPE = request.getAsString("NODETYPE");
		// 排序序号（小到大排序）
		String NODEORDER = request.getAsString("NODEORDER");
		// 节点级别
		String NODELEVEL = request.getAsString("NODELEVEL");
		// 父节点ID
		String PARENTID = request.getAsString("PARENTID");
		// 菜单ID
		String MENUID = request.getAsString("MENUID");

		// 校验

		// 生成DBO
		FOX_MGR_AUTH_FUNCMENUTREE_DBO dbo = new FOX_MGR_AUTH_FUNCMENUTREE_DBO();
		// 功能编号
		if (!StringUtilEx.isNullOrEmpty(FUNCID)) {
			dbo.set_FUNCID(FUNCID);
		}
		// 菜单名称
		if (!StringUtilEx.isNullOrEmpty(DISPLAYNAME)) {
			dbo.set_DISPLAYNAME(DISPLAYNAME);
		}
		// 节点类型（F-目录节点，L-叶子节点即菜单节点）
		if (!StringUtilEx.isNullOrEmpty(NODETYPE)) {
			dbo.set_NODETYPE(NODETYPE);
		}
		// 排序序号（小到大排序）
		if (!StringUtilEx.isNullOrEmpty(NODEORDER)) {
			dbo.set_NODEORDER(NODEORDER);
		}
		// 节点级别
		if (!StringUtilEx.isNullOrEmpty(NODELEVEL)) {
			dbo.set_NODELEVEL(NODELEVEL);
		}
		// 父节点ID
		if (!StringUtilEx.isNullOrEmpty(PARENTID)) {
			dbo.set_PARENTID(PARENTID);
		}

		if (!StringUtilEx.isNullOrEmpty(MENUID)) {
			dbo.set_MENUID(MENUID);
		}
		// 读取数据库
		List<FOX_MGR_AUTH_FUNCMENUTREE_DBO> result = FOX_MGR_AUTH_FUNCMENUTREE_DAO
				.queryTable(dbo);
		if (result == null) {
			// 失败
		} else {
			// 成功
		}

		// 拼装应答报文
		JsonResponse response = new JsonResponse();

		return response;
	}
	
	/**
	 * 查询菜单列表
	 */
	public JsonResponse queryMenuInfo(JsonRequest request) throws Exception {
		LoggerUtil.error("---------查询菜单列表------------");
		// 从请求报文中解析参数
		// 功能编号
		String FUNCID = request.getAsString("FUNCID");	
		// 菜单名称
		String DISPLAYNAME = request.getAsString("DISPLAYNAME");	
		// 节点类型（F-目录节点，L-叶子节点即菜单节点）
		String NODETYPE = request.getAsString("NODETYPE");	
		// 排序序号（小到大排序）
		String NODEORDER = request.getAsString("NODEORDER");	
		// 节点级别
		String NODELEVEL = request.getAsString("NODELEVEL");	
		// 父节点ID
		String PARENTID = request.getAsString("PARENTID");	
		
		// 父节点ID
		String MENUID = request.getAsString("MENUID");	
				
		// 校验

		// 生成DBO
		FOX_MGR_AUTH_FUNCMENUTREE_DBO dbo = new FOX_MGR_AUTH_FUNCMENUTREE_DBO();
		// 功能编号
		if(!StringUtilEx.isNullOrEmpty(FUNCID)){
			dbo.set_FUNCID(FUNCID);
		}
		// 菜单名称
		if(!StringUtilEx.isNullOrEmpty(DISPLAYNAME)){
			dbo.set_DISPLAYNAME(DISPLAYNAME);
		}
		// 节点类型（F-目录节点，L-叶子节点即菜单节点）
		if(!StringUtilEx.isNullOrEmpty(NODETYPE)){
			dbo.set_NODETYPE(NODETYPE);
		}
		// 排序序号（小到大排序）
		if(!StringUtilEx.isNullOrEmpty(NODEORDER)){
			dbo.set_NODEORDER(NODEORDER);
		}
		// 节点级别
		if(!StringUtilEx.isNullOrEmpty(NODELEVEL)){
			dbo.set_NODELEVEL(NODELEVEL);
		}
		// 父节点ID
		if(!StringUtilEx.isNullOrEmpty(PARENTID)){
			dbo.set_PARENTID(PARENTID);
		}
		// 菜单ID
		if(!StringUtilEx.isNullOrEmpty(MENUID)){
			dbo.set_MENUID(MENUID);
		}
		
		
		// 拼装应答报文
		JsonResponse response = new JsonResponse();

		// 读取数据库
//		List<FOX_AUTH_FUNCMENUTREE_DBO> result = FOX_AUTH_FUNCMENUTREE_DAO.queryTable(dbo);
		
//		if (result == null) {
//			//失败
//			response.put(IResponseConstant.retCode, IResponseConstant.FAILED);
//			response.put("message", "查询失败");
//			response.put("data", "");
//		} else {
//			// 成功
//			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
//			response.put("message", "查询成功");
//			String data = JSON.toJSONString(result.toArray());  
//			response.put("data", data);
//		}
		
		
		
		StringBuffer buffer = new StringBuffer(" where ");
		// 功能编号
		if(!StringUtilEx.isNullOrEmpty(FUNCID)){
			buffer.append("f.FUNC_ID ='" + FUNCID.trim() + "' and ");
		}
		// 菜单名称
		if(!StringUtilEx.isNullOrEmpty(DISPLAYNAME)){
			buffer.append("f.MENU_NAME ='" + DISPLAYNAME.trim() + "' and ");
		}
		// 节点类型（F-目录节点，L-叶子节点即菜单节点）
		if(!StringUtilEx.isNullOrEmpty(NODETYPE)){
			buffer.append("f.NODE_TYPE ='" + NODETYPE.trim() + "' and ");
		}
		// 排序序号（小到大排序）
		if(!StringUtilEx.isNullOrEmpty(NODEORDER)){
			buffer.append("f.SORT_NO ='" + NODEORDER.trim() + "' and ");
		}
		// 节点级别
		if(!StringUtilEx.isNullOrEmpty(NODELEVEL)){
			buffer.append("f.NODE_LEVEL ='" + NODELEVEL.trim() + "' and ");
		}
		// 父节点ID
		if(!StringUtilEx.isNullOrEmpty(PARENTID)){
			buffer.append("f.PARENT_ID ='" + PARENTID.trim() + "' and ");
		}
		// 菜单ID
		if(!StringUtilEx.isNullOrEmpty(MENUID)){
			buffer.append("f.MENU_ID ='" + MENUID.trim() + "' and ");
		}
		
		String whereString;
		if (buffer.length() > 8) {
			whereString = buffer.substring(0, buffer.length() - 5);
		} else if (buffer.length() == 7) {
			whereString = buffer.replace(0, 7, "").toString();
		} else {
			whereString = buffer.toString();
		}

		whereString = buffer.toString();
		
		
		// 
		String sql = "select  f.MENU_ID,f.FUNC_ID,f.MENU_NAME,f.NODE_TYPE,f.SORT_NO,f.NODE_LEVEL,f.PARENT_ID,t.MENU_NAME,t.MENU_ID  FROM FOX_MGR_AUTH_FUNCMENUTREE " +
				"f left JOIN  FOX_MGR_AUTH_FUNCMENUTREE a  " +
				"on f.MENU_ID=a.MENU_ID left join " +
				" FOX_MGR_AUTH_FUNCMENUTREE t on t.MENU_ID=a.MENU_ID"  

						+ whereString;
				//LoggerUtil.error("sql=================" + sql);
				List<Map<String, Object>> resultList = DBService.executeQuery(sql);
		 
		       if (resultList == null) {
					// 查询失败
					response.put(IResponseConstant.retCode,
							IResponseConstant.FAILED_DB_ERROR);
					response.put(IResponseConstant.retMsg, "查询失败");
				} else {
					// 查询成功
					response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
					response.put(IResponseConstant.retMsg, "查询成功");
					response.put("list", resultList);
				}
		
		
		return response;
	}
	
	
	
	
	
}