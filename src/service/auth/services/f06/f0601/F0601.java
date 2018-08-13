package service.auth.services.f06.f0601;
//
import both.common.util.StringUtilEx;
import both.constants.IResponseConstant;
import service.auth.core.f06.FOX_MGR_AUTH_FUNCMENUTREE_DAO;
import service.auth.core.f06.FOX_MGR_AUTH_FUNCMENUTREE_DBO;
import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;
import cn.com.bankit.phoenix.trade.Service;

/**
 * 新增-FOX_AUTH_FUNCMENUTREE 功能菜单树信息表
 */
public class F0601 extends Service<JsonRequest, JsonResponse> {

	/**
	 * 构造函数
	 */
	public F0601() {
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
		int result = FOX_MGR_AUTH_FUNCMENUTREE_DAO.insert(dbo);
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
	 *添加菜单
	 */
	public JsonResponse addMenuInfo(JsonRequest request) throws Exception {
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
		// 菜单ID
		if (!StringUtilEx.isNullOrEmpty(MENUID)) {
			dbo.set_MENUID(MENUID);
		}
		
		
		// 拼装应答报文
		JsonResponse response = new JsonResponse();

		// 读取数据库
		int result = FOX_MGR_AUTH_FUNCMENUTREE_DAO.insert(dbo);
		
		
		if (result == -1) {
			//失败
			response.put(IResponseConstant.retCode, IResponseConstant.FAILED);
			response.put("message", "添加菜单失败");
			
		} else {
			// 成功
			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			response.put("message", "添加菜单成功");
			
		}
		//response.put("data", "");

		

		return response;
	}
	
}