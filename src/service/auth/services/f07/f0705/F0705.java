package service.auth.services.f07.f0705;

import both.common.util.LoggerUtil;
import both.common.util.StringUtilEx;
import both.constants.IResponseConstant;
import both.db.DBService;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;

import service.auth.core.f07.FOX_MGR_AUTH_FUNC_DAO;
import service.auth.core.f07.FOX_MGR_AUTH_FUNC_DBO;
import service.auth.core.f09.FOX_MGR_AUTH_GROUPFUNCMAPPING_DAO;
import service.auth.core.f09.FOX_MGR_AUTH_GROUPFUNCMAPPING_DBO;
import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;
import cn.com.bankit.phoenix.jdbc.tool.DBAccessor;
import cn.com.bankit.phoenix.trade.Service;

/**
 * 新增-FOX_AUTH_FUNC 功能定义表
 */
public class F0705 extends Service<JsonRequest, JsonResponse> {

	/**
	 * 构造函数
	 */
	public F0705() {
		super();
	}

	// 返回操作选项列表
	public JsonResponse queryOperatorList(JsonRequest request) throws Exception {
		// 鎷艰搴旂瓟鎶ユ枃
		JsonResponse response = new JsonResponse();

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		Map<String, String> map = new HashMap<String, String>();
		map.put("1", "添加");
		map.put("2", "删除");
		map.put("3", "编辑");
		map.put("4", "查看");
		map.put("5", "冻结");
		resultList.add(map);
		response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
		response.put(IResponseConstant.retMsg, "查询成功");
		response.put("list", resultList);

		return response;
	}

	// 返回分组列表

	public JsonResponse queryFuncGroupList(JsonRequest request)
			throws Exception {

		// 鎷艰搴旂瓟鎶ユ枃
		JsonResponse response = new JsonResponse();

		// 璇诲彇鏁版嵁搴?
		String sql = "select GROUP_ID,GROUP_NAME,GROUP_DESCRIBTION,CREATE_DATE,CREATE_TIME,UPDATE_DATE,UPDATE_TIME,STATUS from FOX_MGR_AUTH_FUNCGROUPINFO ";

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

	public boolean isExistFuncID(String FUNCID) throws Exception {
		String sql = "select FUNC_ID,FUNC_NAME,URL_HTML,URL_JS,URL_CSS,REMARK from FOX_MGR_AUTH_FUNC where func_id='"
				+ FUNCID + "'";

		List<Map<String, Object>> resultList = DBService.executeQuery(sql);

		if (resultList == null) {

			return false;
		} else {

			LoggerUtil.error("sql=》》》》》》》》》》》》》》" + sql);
			return resultList.size() > 0;
		}
	}

	// 判断组名或组编号是否已经存在
	public JsonResponse isExistFuncID(JsonRequest request) throws Exception {

		JsonResponse response = new JsonResponse();
		String FUNCID = request.getAsString("FUNCID");

		boolean isexist = this.isExistFuncID(FUNCID);

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

	// 插入数据
	public JsonResponse addModuleInfo(JsonRequest request) throws Exception {
		// 从请求报文中解析参数
		DBAccessor dbAccessor = DBAccessor.getDBAccessor();
		Connection con = dbAccessor.getConnection();
		con.setAutoCommit(false);
		// 功能编号
		String FUNCID = request.getAsString("FUNCID");
		// 功能名称
		String NAME = request.getAsString("NAME");
		// html路径
		String HTML = request.getAsString("HTML");
		// js路径
		String JS = request.getAsString("JS");
		// css路径
		String CSS = request.getAsString("CSS");
		// 备注
		String REMARK = request.getAsString("REMARK");
		String OPERATOR = request.getAsString("OPERATOR");
		String GROUPID = request.getAsString("GROUPID");
		// 校验

		// 生成DBO
		FOX_MGR_AUTH_FUNC_DBO dbo = new FOX_MGR_AUTH_FUNC_DBO();
		// 功能编号
		if (!StringUtilEx.isNullOrEmpty(FUNCID)) {
			dbo.set_FUNCID(FUNCID);
		}
		// 功能名称
		if (!StringUtilEx.isNullOrEmpty(NAME)) {
			dbo.set_NAME(NAME);
		}
		// html路径
		if (!StringUtilEx.isNullOrEmpty(HTML)) {
			dbo.set_HTML(HTML);
		}
		// js路径
		if (!StringUtilEx.isNullOrEmpty(JS)) {
			dbo.set_JS(JS);
		}
		// css路径
		if (!StringUtilEx.isNullOrEmpty(CSS)) {
			dbo.set_CSS(CSS);
		}
		// 备注
		if (!StringUtilEx.isNullOrEmpty(REMARK)) {
			dbo.set_REMARK(REMARK);
		}
		if (!StringUtilEx.isNullOrEmpty(OPERATOR)) {
			dbo.set_OPERATOR(OPERATOR);
		}

		// 拼装应答报文
		JsonResponse response = new JsonResponse();

		boolean isexist = this.isExistFuncID(FUNCID);
		if (isexist) {

			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			response.put(IResponseConstant.retMsg, "存在功能编号");
			response.put("count", "1");
			return response;
		}
		int result = 0;
		int result2 = 0;
		try {
			result = FOX_MGR_AUTH_FUNC_DAO.insert(dbo);

			if (StringUtilEx.isNullOrEmpty(GROUPID)) {
				if (result == -1) {
					response.put(IResponseConstant.retCode,
							IResponseConstant.FAILED);
					response.put(IResponseConstant.retMsg, "新增失败");
					response.put("count", "0");
					con.rollback();
				} else {
					response.put(IResponseConstant.retCode,
							IResponseConstant.SUCCESS);
					response.put(IResponseConstant.retMsg, "新增成功");
					response.put("count", "0");
					con.commit();
				}
				return response;
			}
			// 把GroupID插入到功能映射关系表

			FOX_MGR_AUTH_GROUPFUNCMAPPING_DBO dbogroup = new FOX_MGR_AUTH_GROUPFUNCMAPPING_DBO();
			// 功能组编号
			if (!StringUtilEx.isNullOrEmpty(GROUPID)) {
				dbogroup.set_GROUPID(GROUPID);
			}
			// 功能编号
			if (!StringUtilEx.isNullOrEmpty(FUNCID)) {
				dbogroup.set_FUNCID(FUNCID);
			}

			// 读取数据库
			result2 = FOX_MGR_AUTH_GROUPFUNCMAPPING_DAO.insert(dbogroup);

			if (result == -1 || result2 == -1) {
				response.put(IResponseConstant.retCode,
						IResponseConstant.FAILED);
				response.put(IResponseConstant.retMsg, "新增失败");
				response.put("count", "0");
				con.rollback();
			} else {
				response.put(IResponseConstant.retCode,
						IResponseConstant.SUCCESS);
				response.put(IResponseConstant.retMsg, "新增成功");
				response.put("count", "0");
				con.commit();
			}
		} finally {
			con.setAutoCommit(true);
		}
		return response;
	}

	/**
	 * execute
	 */
	public JsonResponse queryModuleInfoList(JsonRequest request)
			throws Exception {
		LoggerUtil.error("-------------查询功能列表------------");
		// 浠庤姹傛姤鏂囦腑瑙ｆ瀽鍙傛暟
		// 鏌滃潡浠ｇ爜
		String MODCODE = request.getAsString("FUNCID");
		// 妯″潡鍚嶇О
		String MODNAME = request.getAsString("NAME");
		// htmlPATH
		String HTMLPATH = request.getAsString("HTML");
		// JSPATH
		String JSPATH = request.getAsString("JS");
		// CSSPATH
		String CSSPATH = request.getAsString("CSS");

		String REMARK = request.getAsString("REMARK");
		String OPERATOR = request.getAsString("OPERATOR");

		StringBuffer buffer = new StringBuffer(" where ");
		// 鐢ㄦ埛浠ｅ彿
		if (!StringUtilEx.isNullOrEmpty(MODCODE)) {
			buffer.append("f.FUNC_ID like '%'||'" + MODCODE.trim()
					+ "'||'%' and ");
		}
		// 鏌滈潰濮撳悕
		if (!StringUtilEx.isNullOrEmpty(MODNAME)) {
			buffer.append("f.FUNC_NAME like '%'||'" + MODNAME.trim()
					+ "'||'%' and ");
		}
		// 鐢ㄦ埛绫诲瀷锛?-鏅€氱敤鎴凤紝1-BST鑷姩鐢ㄦ埛锛?-POS鑷姩鐢ㄦ埛,3-绉诲姩钀ラ攢鑷姩鐢ㄦ埛,4-ATM鑷姩鐢ㄦ埛,5-鐗硅壊涓氬姟鑷姩鐢ㄦ埛锛?
		if (!StringUtilEx.isNullOrEmpty(HTMLPATH)) {
			buffer.append("f.URL_HTML ='" + HTMLPATH.trim() + "' and ");
		}
		// 褰掑睘鏈烘瀯
		if (!StringUtilEx.isNullOrEmpty(JSPATH)) {
			buffer.append("f.URL_JS ='" + JSPATH.trim() + "' and ");
		}
		// 鐢ㄦ埛鐘舵€侊紙0-鍚敤锛?-鍋滅敤锛?-娉ㄩ攢锛?-閿佸畾锛?-琚氦鎺ワ級
		if (!StringUtilEx.isNullOrEmpty(CSSPATH)) {
			buffer.append("f.URL_CSS ='" + CSSPATH.trim() + "' and ");
		}
		if (!StringUtilEx.isNullOrEmpty(REMARK)) {
			buffer.append("f.REMARK ='" + REMARK.trim() + "' and ");
		}
		if (!StringUtilEx.isNullOrEmpty(OPERATOR)) {
			buffer.append("f.OPERATOR ='" + OPERATOR.trim() + "' and ");
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
		String sql = "select  f.FUNC_ID,f.FUNC_NAME,f.URL_HTML,f.URL_JS,f.URL_CSS,f.REMARK,t.group_name,t.group_id  FROM FOX_MGR_AUTH_FUNC "
				+ "f left JOIN  FOX_MGR_AUTH_GROUPFUNCMAPPING a  "
				+ "on f.func_id=a.func_id left join "
				+ " FOX_MGR_AUTH_FUNCGROUPINFO t on t.group_id=a.group_id"

				+ whereString;
		LoggerUtil.error("sql SHOW=================" + sql);
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

	// 更新数据
	public JsonResponse updateModuleInfo(JsonRequest request) throws Exception {
		// 从请求报文中解析参数
		// 功能编号
		String FUNCID = request.getAsString("FUNCID");
		// 功能名称
		String NAME = request.getAsString("NAME");
		// html路径
		String HTML = request.getAsString("HTML");
		// js路径
		String JS = request.getAsString("JS");
		// css路径
		String CSS = request.getAsString("CSS");
		// 备注
		String REMARK = request.getAsString("REMARK");
		String OPERATOR = request.getAsString("OPERATOR");

		String GROUPID = request.getAsString("GROUPID");
		// 校验

		// 生成DBO
		FOX_MGR_AUTH_FUNC_DBO dbo = new FOX_MGR_AUTH_FUNC_DBO();
		// 功能编号
		if (!StringUtilEx.isNullOrEmpty(FUNCID)) {
			dbo.set_FUNCID(FUNCID);
		}
		// 功能名称
		if (!StringUtilEx.isNullOrEmpty(NAME)) {
			dbo.set_NAME(NAME);
		}
		// html路径
		if (!StringUtilEx.isNullOrEmpty(HTML)) {
			dbo.set_HTML(HTML);
		}
		// js路径
		if (!StringUtilEx.isNullOrEmpty(JS)) {
			dbo.set_JS(JS);
		}
		// css路径
//		if (!StringUtilEx.isNullOrEmpty(CSS)) {
		dbo.set_CSS(CSS);
//		}
		// 备注
		if (!StringUtilEx.isNullOrEmpty(REMARK)) {
			dbo.set_REMARK(REMARK);
		}
		if (!StringUtilEx.isNullOrEmpty(OPERATOR)) {
			dbo.set_OPERATOR(OPERATOR);
		}

		FOX_MGR_AUTH_FUNC_DBO wheredbo = new FOX_MGR_AUTH_FUNC_DBO();
		if (!StringUtilEx.isNullOrEmpty(FUNCID)) {
			wheredbo.set_FUNCID(FUNCID);
		}
		int result = FOX_MGR_AUTH_FUNC_DAO.update(dbo, wheredbo);

		// 删除funid以前的分组
		FOX_MGR_AUTH_GROUPFUNCMAPPING_DBO deletedbo = new FOX_MGR_AUTH_GROUPFUNCMAPPING_DBO();
		if (!StringUtilEx.isNullOrEmpty(FUNCID)) {
			deletedbo.set_FUNCID(FUNCID);
		}
		FOX_MGR_AUTH_GROUPFUNCMAPPING_DAO.delete(deletedbo);
		int result2 = 0;
		if (!StringUtilEx.isNullOrEmpty(GROUPID)) {
			// 插入新的分组
			FOX_MGR_AUTH_GROUPFUNCMAPPING_DBO insertdbo = new FOX_MGR_AUTH_GROUPFUNCMAPPING_DBO();
			insertdbo.set_FUNCID(FUNCID);
			insertdbo.set_GROUPID(GROUPID);
			result2 = FOX_MGR_AUTH_GROUPFUNCMAPPING_DAO.insert(insertdbo);
		}

		// 拼装应答报文
		JsonResponse response = new JsonResponse();
		if (result == -1 || result2 == -1) {
			response.put(IResponseConstant.retCode, IResponseConstant.FAILED);
			response.put(IResponseConstant.retMsg, "编辑失败");
		} else {
			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			response.put(IResponseConstant.retMsg, "编辑成功");
		}
		return response;
	}

	// 删除n条数据
	public JsonResponse deleteModuleInfoList(JsonRequest request)
			throws Exception {
		LoggerUtil.error("-------------开始删除------------");
		JSONArray deleteJsonArray = (JSONArray) request.get("list");

		for (int i = 0; i < deleteJsonArray.size(); i++) {
			String modcode = (String) deleteJsonArray.get(i);
			FOX_MGR_AUTH_FUNC_DBO tellerInfoDbo = new FOX_MGR_AUTH_FUNC_DBO();
			tellerInfoDbo.set_FUNCID(modcode);
			FOX_MGR_AUTH_FUNC_DAO.delete(tellerInfoDbo);
			FOX_MGR_AUTH_GROUPFUNCMAPPING_DBO groupFuncMappingDbo = new FOX_MGR_AUTH_GROUPFUNCMAPPING_DBO();
			groupFuncMappingDbo.set_FUNCID(modcode);
			FOX_MGR_AUTH_GROUPFUNCMAPPING_DAO.delete(groupFuncMappingDbo);
		}
		JsonResponse response = new JsonResponse();
		response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
		response.put(IResponseConstant.retMsg, "删除成功");
		return response;
	}

	// 返回某个功能所属的功能组下的所有功能
	public JsonResponse queryAllFuncFromGroup(JsonRequest request)
			throws Exception {

		String FUNCID = request.getAsString("FUNCID");

		JsonResponse response = new JsonResponse();

		// 璇诲彇鏁版嵁搴?
		String sql = " select f.FUNC_ID,f.FUNC_NAME,f.URL_HTML,f.URL_JS,f.URL_CSS,f.REMARK from FOX_MGR_AUTH_FUNC f where f.func_id in  (select  func_id from FOX_MGR_AUTH_GROUPFUNCMAPPING where group_id"
				+ " in (select group_id from FOX_MGR_AUTH_GROUPFUNCMAPPING where func_id='"
				+ FUNCID + "' )) ";

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

	/**
	 * execute
	 */
	// public JsonResponse execute(JsonRequest request) throws Exception {
	// // 从请求报文中解析参数
	// // 功能编号
	// String FUNCID = request.getAsString("FUNCID");
	// // 功能名称
	// String NAME = request.getAsString("NAME");
	// // html路径
	// String HTML = request.getAsString("HTML");
	// // js路径
	// String JS = request.getAsString("JS");
	// // css路径
	// String CSS = request.getAsString("CSS");
	// // 备注
	// String REMARK = request.getAsString("REMARK");
	//
	// // 校验
	//
	// // 生成DBO
	// FOX_AUTH_FUNC_DBO dbo = new FOX_AUTH_FUNC_DBO();
	// // 功能编号
	// if(!StringUtilEx.isNullOrEmpty(FUNCID)){
	// dbo.set_FUNCID(FUNCID);
	// }
	// // 功能名称
	// if(!StringUtilEx.isNullOrEmpty(NAME)){
	// dbo.set_NAME(NAME);
	// }
	// // html路径
	// if(!StringUtilEx.isNullOrEmpty(HTML)){
	// dbo.set_HTML(HTML);
	// }
	// // js路径
	// if(!StringUtilEx.isNullOrEmpty(JS)){
	// dbo.set_JS(JS);
	// }
	// // css路径
	// if(!StringUtilEx.isNullOrEmpty(CSS)){
	// dbo.set_CSS(CSS);
	// }
	// // 备注
	// if(!StringUtilEx.isNullOrEmpty(REMARK)){
	// dbo.set_REMARK(REMARK);
	// }
	//
	// // 读取数据库
	// List<FOX_AUTH_FUNC_DBO> result = FOX_AUTH_FUNC_DAO.queryTable(dbo);
	// if (result == null) {
	// // 失败
	// } else {
	// // 成功
	// }
	//
	// // 拼装应答报文
	// JsonResponse response = new JsonResponse();
	//
	// return response;
	// }
}