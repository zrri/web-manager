package service.auth.services.f06.f0602;

import both.common.util.LoggerUtil;
import both.common.util.StringUtilEx;
import both.constants.IResponseConstant;
import service.auth.core.f06.FOX_MGR_AUTH_FUNCMENUTREE_DAO;
import service.auth.core.f06.FOX_MGR_AUTH_FUNCMENUTREE_DBO;
import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;
import cn.com.bankit.phoenix.trade.Service;

/**
 * 修改-FOX_AUTH_FUNCMENUTREE 功能菜单树信息表
 */
public class F0602 extends Service<JsonRequest, JsonResponse> {

	/**
	 * 构造函数
	 */
	public F0602() {
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

		// 生成条件DBO
		FOX_MGR_AUTH_FUNCMENUTREE_DBO wheredbo = new FOX_MGR_AUTH_FUNCMENUTREE_DBO();
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

		// 读取数据库
		int result = FOX_MGR_AUTH_FUNCMENUTREE_DAO.update(dbo, wheredbo);
		if (result == -1) {
			// 失败
		} else {
			// 成功
		}

		// 拼装应答报文
		JsonResponse response = new JsonResponse();

		return response;
	}
	
	
	
	/**
	 * 修改菜单
	 */
	public JsonResponse updateMenuInfo(JsonRequest request) throws Exception {
		
		LoggerUtil.error("---------修改菜单列表------------");
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
		//菜单id
		if (!StringUtilEx.isNullOrEmpty(MENUID)) {
			dbo.set_MENUID(MENUID);
		}

		// 生成条件DBO
		FOX_MGR_AUTH_FUNCMENUTREE_DBO wheredbo = new FOX_MGR_AUTH_FUNCMENUTREE_DBO();
		//菜单id
		if (!StringUtilEx.isNullOrEmpty(MENUID)) {
			wheredbo.set_MENUID(MENUID);
		}		
//		// 功能编号
//		if (!StringUtilEx.isNullOrEmpty(FUNCID)) {
//			dbo.set_FUNCID(FUNCID);
//		}
		// 菜单名称
//		if (!StringUtilEx.isNullOrEmpty(DISPLAYNAME)) {
//			dbo.set_DISPLAYNAME(DISPLAYNAME);
//		}
//		// 节点类型（F-目录节点，L-叶子节点即菜单节点）
//		if (!StringUtilEx.isNullOrEmpty(NODETYPE)) {
//			dbo.set_NODETYPE(NODETYPE);
//		}
//		// 排序序号（小到大排序）
//		if (!StringUtilEx.isNullOrEmpty(NODEORDER)) {
//			dbo.set_NODEORDER(NODEORDER);
//		}
//		// 节点级别
//		if (!StringUtilEx.isNullOrEmpty(NODELEVEL)) {
//			dbo.set_NODELEVEL(NODELEVEL);
//		}
//		// 父节点ID
//		if (!StringUtilEx.isNullOrEmpty(PARENTID)) {
//			dbo.set_PARENTID(PARENTID);
//		}
		
		LoggerUtil.error("输出----"+dbo.toString());

		// 拼装应答报文
		JsonResponse response = new JsonResponse();
		
		// 读取数据库
		int result = FOX_MGR_AUTH_FUNCMENUTREE_DAO.update(dbo, wheredbo);
		if (result == -1) {
			//失败
			response.put(IResponseConstant.retCode, IResponseConstant.FAILED);
			response.put("message", "修改菜单失败");
			
		} else {
			// 成功
			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			response.put("message", "修改菜单成功");
			
		}

		
		
		

		return response;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}