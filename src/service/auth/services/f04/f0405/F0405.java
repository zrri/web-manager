package service.auth.services.f04.f0405;

import both.common.util.DateUtil;
import both.common.util.LoggerUtil;
import both.common.util.StringUtilEx;
import both.constants.IResponseConstant;
import both.db.DBService;
import both.db.util.DbServiceUtil;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import service.auth.core.f04.FOX_MGR_AUTH_ROLEINFO_DAO;
import service.auth.core.f04.FOX_MGR_AUTH_ROLEINFO_DBO;
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
 * 新增-FOX_AUTH_ROLEINFO 角色信息表
 */
public class F0405 extends Service<JsonRequest, JsonResponse> {

	/**
	 * 构造函数
	 */
	public F0405() {
		super();
	}

	public boolean isExistRoleID(String ROLEID) throws Exception {
		String sql = "select ROLE_ID,ROLE_NAME,ROLE_DESCRIBTION,CREATE_DATE,CREATE_TIME,UPDATE_DATE,UPDATE_TIME,STATUS from FOX_MGR_AUTH_ROLEINFO where ROLE_ID='"
				+ ROLEID + "'";

		List<Map<String, Object>> resultList = DBService.executeQuery(sql);

		if (resultList == null) {
			return false;
		} else {
			return resultList.size() > 0;
		}
	}

	// 判断组名或组编号是否已经存在
	public JsonResponse isExistRoleID(JsonRequest request) throws Exception {

		JsonResponse response = new JsonResponse();
		String ROLEID = request.getAsString("ROLEID");

		boolean isexist = this.isExistRoleID(ROLEID);

		if (isexist) {

			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			response.put(IResponseConstant.retMsg, "查询成功");
			response.put("count", "1");
		} else {
			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			response.put(IResponseConstant.retMsg, "查询成功");
			response.put("count", "0");
		}

		return response;
	}

	/**
	 * execute
	 */
	public JsonResponse queryRoleInfoList(JsonRequest request) throws Exception {
		// 从请求报文中解析参数
		// 角色编号
		String ROLEID = request.getAsString("ROLEID");
		// 角色名称
		String ROLENAME = request.getAsString("ROLENAME");
		// 角色描述
		String ROLEDESCRIBTION = request.getAsString("ROLEDESCRIBTION");
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

		StringBuffer buffer = new StringBuffer(" where ");

		if (!StringUtilEx.isNullOrEmpty(ROLEID)) {
			buffer.append("r.ROLE_ID like '%'||'" + ROLEID.trim()
					+ "'||'%' and ");
		}
		if (!StringUtilEx.isNullOrEmpty(ROLENAME)) {
			buffer.append("r.ROLE_NAME like '%'||'" + ROLENAME.trim()
					+ "'||'%' and ");
		}
		if (!StringUtilEx.isNullOrEmpty(STATUS)) {
			buffer.append("r.STATUS ='" + STATUS.trim() + "' and ");
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

		// 璇诲彇鏁版嵁搴?
		String sql = "select r.ROLE_ID,r.ROLE_NAME,r.ROLE_DESCRIBTION,r.CREATE_DATE,r.CREATE_TIME,r.UPDATE_DATE,r.UPDATE_TIME,r.STATUS,f.FUNC_ID,f.FUNC_NAME "
				+ " from FOX_MGR_AUTH_ROLEINFO r "
				+ " left join FOX_MGR_AUTH_ROLEFUNCMAPPING m on r.ROLE_ID=m.ROLE_ID "
				+ " left join FOX_MGR_AUTH_FUNC f on m.FUNC_ID=f.FUNC_ID"
				+ whereString;

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

	// 添加一个角色
	// 插入数据
	public JsonResponse addRoleInfo(JsonRequest request) throws Exception {
		// 从请求报文中解析参数

		String ROLEID = request.getAsString("ROLEID");

		String ROLENAME = request.getAsString("ROLENAME");
		//
		String ROLEDESCRIBTION = request.getAsString("ROLEDESCRIBTION");

		JSONArray funcArray = (JSONArray) request.get("FUNCIDARR");

		// 生成DBO
		FOX_MGR_AUTH_ROLEINFO_DBO dbo = new FOX_MGR_AUTH_ROLEINFO_DBO();

		if (!StringUtilEx.isNullOrEmpty(ROLEID)) {
			dbo.set_ROLEID(ROLEID);
		}

		if (!StringUtilEx.isNullOrEmpty(ROLENAME)) {
			dbo.set_ROLENAME(ROLENAME);
		}
		dbo.set_CREATEDATE(DateUtil.getFormatDate());
		dbo.set_CREATETIME(DateUtil.getFormatTime());
		if (!StringUtilEx.isNullOrEmpty(ROLEDESCRIBTION)) {
			dbo.set_ROLEDESCRIBTION(ROLEDESCRIBTION);
		}
		dbo.set_STATUS("1");

		// 拼装应答报文
		JsonResponse response = new JsonResponse();

		boolean isexist = this.isExistRoleID(ROLEID);
		if (isexist) {

			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			response.put(IResponseConstant.retMsg, "存在RoleID");
			response.put("count", "1");
			return response;
		}

		int result = FOX_MGR_AUTH_ROLEINFO_DAO.insert(dbo);

		if (result == -1) {
			response.put(IResponseConstant.retCode, IResponseConstant.FAILED);
			response.put(IResponseConstant.retMsg, "新增失败");
			response.put("count", "0");
		} else {
			int size = funcArray.size();
			for (int i = 0; i < funcArray.size(); i++) {
				String funcId = (String) ((JSONObject) funcArray.get(i))
						.get("id");
				FOX_MGR_AUTH_ROLEFUNCMAPPING_DBO tellerInfoDbo = new FOX_MGR_AUTH_ROLEFUNCMAPPING_DBO();

				tellerInfoDbo.set_FUNCID(funcId);
				tellerInfoDbo.set_ROLEID(ROLEID);

				FOX_MGR_AUTH_ROLEFUNCMAPPING_DAO.insert(tellerInfoDbo);
			}
			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			response.put(IResponseConstant.retMsg, "新增成功");
			response.put("count", "0");
		}

		return response;
	}

	// 更新数据

	public JsonResponse editRoleInfo(JsonRequest request) throws Exception {
		// 从请求报文中解析参数

		String ROLEID = request.getAsString("ROLEID");

		String ROLENAME = request.getAsString("ROLENAME");
		//
		String ROLEDESCRIBTION = request.getAsString("ROLEDESCRIBTION");

		String STATUS = request.getAsString("STATUS");

		JSONArray funcArray = (JSONArray) request.get("FUNCIDARR");

		// 生成DBO
		FOX_MGR_AUTH_ROLEINFO_DBO dbo = new FOX_MGR_AUTH_ROLEINFO_DBO();

		if (!StringUtilEx.isNullOrEmpty(ROLEID)) {
			dbo.set_ROLEID(ROLEID);
		}

		if (!StringUtilEx.isNullOrEmpty(ROLENAME)) {
			dbo.set_ROLENAME(ROLENAME);
		}

		if (!StringUtilEx.isNullOrEmpty(ROLEDESCRIBTION)) {
			dbo.set_ROLEDESCRIBTION(ROLEDESCRIBTION);
		}
		dbo.set_STATUS(STATUS);

		// 拼装应答报文
		JsonResponse response = new JsonResponse();

		FOX_MGR_AUTH_ROLEINFO_DBO wheredbo = new FOX_MGR_AUTH_ROLEINFO_DBO();

		wheredbo.set_ROLEID(ROLEID);

		int result = FOX_MGR_AUTH_ROLEINFO_DAO.update(dbo, wheredbo);

		// 先删除role以前所有的功能
		FOX_MGR_AUTH_ROLEFUNCMAPPING_DBO mappingDbo = new FOX_MGR_AUTH_ROLEFUNCMAPPING_DBO();
		mappingDbo.set_ROLEID(ROLEID);
		FOX_MGR_AUTH_ROLEFUNCMAPPING_DAO.delete(mappingDbo);
		// 插入新的
		for (int i = 0; i < funcArray.size(); i++) {

			String funcId = (String) funcArray.get(i);
			FOX_MGR_AUTH_ROLEFUNCMAPPING_DBO tellerInfoDbo = new FOX_MGR_AUTH_ROLEFUNCMAPPING_DBO();

			tellerInfoDbo.set_FUNCID(funcId);
			tellerInfoDbo.set_ROLEID(ROLEID);

			FOX_MGR_AUTH_ROLEFUNCMAPPING_DAO.insert(tellerInfoDbo);
		}

		if (result == -1) {
			response.put(IResponseConstant.retCode, IResponseConstant.FAILED);
			response.put(IResponseConstant.retMsg, "新增失败");
		} else {
			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			response.put(IResponseConstant.retMsg, "新增成功");
		}

		return response;
	}

	// 查询功能组树

	public JsonResponse queryFuncGroupTreeList(JsonRequest request)
			throws Exception {

		// 鎷艰搴旂瓟鎶ユ枃
		JsonResponse response = new JsonResponse();

		// 璇诲彇鏁版嵁搴?
		String sql = "select  f.FUNC_ID,f.FUNC_NAME,f.URL_HTML,f.URL_JS,f.URL_CSS,f.REMARK,t.GROUP_NAME,t.GROUP_ID  FROM FOX_MGR_AUTH_FUNC "
				+ "f left JOIN  FOX_MGR_AUTH_GROUPFUNCMAPPING a  "
				+ "on f.FUNC_ID=a.FUNC_ID left join "
				+ " FOX_MGR_AUTH_FUNCGROUPINFO t on t.GROUP_ID=a.GROUP_ID";

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

	// 删除n条数据
	public JsonResponse deleteRoleInfoList(JsonRequest request)
			throws Exception {
		LoggerUtil.error("-------------开始删除------------");
		JSONArray deleteJsonArray = (JSONArray) request.get("list");

		for (int i = 0; i < deleteJsonArray.size(); i++) {

			String modcode = (String) deleteJsonArray.get(i);
			FOX_MGR_AUTH_ROLEINFO_DBO tellerInfoDbo = new FOX_MGR_AUTH_ROLEINFO_DBO();
			tellerInfoDbo.set_ROLEID(modcode);
			FOX_MGR_AUTH_ROLEINFO_DAO.delete(tellerInfoDbo);
			// 角色删除之后,删除相关的角色功能映射关系
			FOX_MGR_AUTH_ROLEFUNCMAPPING_DBO roleFuncMapping = new FOX_MGR_AUTH_ROLEFUNCMAPPING_DBO();
			roleFuncMapping.set_ROLEID(modcode);
			FOX_MGR_AUTH_ROLEFUNCMAPPING_DAO.delete(roleFuncMapping);
		}

		JsonResponse response = new JsonResponse();
		response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
		response.put(IResponseConstant.retMsg, "删除成功");
		return response;
	}

	@Override
	public JsonResponse execute(JsonRequest request) throws Exception {
		LoggerUtil.debug("-------开始服务-------");
		JsonResponse response = new JsonResponse();
		String sql = "select ROLE_ID,ROLE_NAME,ROLE_DESCRIBTION,CREATE_DATE,CREATE_TIME,UPDATE_DATE,UPDATE_TIME,STATUS from FOX_MGR_AUTH_ROLEINFO ";
		
		
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
		LoggerUtil.debug("-------结束服务-------");
		return response;
	}

}