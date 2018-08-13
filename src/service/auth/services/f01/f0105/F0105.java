package service.auth.services.f01.f0105;

import java.util.ArrayList;
import java.util.List;

import service.auth.core.f01.FOX_MGR_AUTH_ORGINFO_DAO;
import service.auth.core.f01.FOX_MGR_AUTH_ORGINFO_DBO;
import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;
import both.common.util.DateUtil;
import both.common.util.LoggerUtil;
import both.common.util.StringUtilEx;
import both.constants.IResponseConstant;
import both.db.DBService;
import both.db.util.DbServiceUtil;
import cn.com.bankit.phoenix.trade.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * 新增-FOX_AUTH_ORGINFO 机构信息表
 */
public class F0105 extends Service<JsonRequest, JsonResponse> {

	/**
	 * 构造函数
	 */
	public F0105() {
		super();
	}

	/**
	 * execute
	 */
	public JsonResponse execute(JsonRequest request) throws Exception {

		JsonResponse response = new JsonResponse();

		return response;
	}

	// 删除
	public JsonResponse deleteOrgInfoList(JsonRequest request) throws Exception {
		JsonResponse response = new JsonResponse();
		JSONArray jsonArray = request.getData().getJSONArray("list");

		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			buffer.append("'" + jsonObject.getString("orgId") + "',");
		}
		if (buffer.length() > 0) {
			buffer.deleteCharAt(buffer.length() - 1);
		}
		String sql = "delete from FOX_MGR_AUTH_ORGINFO where orgid in("
				+ buffer.toString() + ")";
		List<String> list = new ArrayList<String>();
		list.add(sql);
		int[] results = DBService.dbAccessor.batchExec(list);
		if (results[0] == -1) {
			response.put(IResponseConstant.retCode, IResponseConstant.FAILED);
			response.put(IResponseConstant.retMsg, "删除失败");
		} else {
			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			response.put(IResponseConstant.retMsg, "删除成功");
		}

		return response;
	}

	// 添加机构
	public JsonResponse addOrgInfo(JsonRequest request) throws Exception {
		JsonResponse response = new JsonResponse();
		String ORGID = request.getAsString("orgId");
		// 机构中文名
		String ORGCHINAME = request.getAsString("orgChiName");
		// 机构英文名
		String ORGENGNAME = request.getAsString("orgEngName");
		// 上级机构
		String PARENTORGID = request.getAsString("parentOrgId");
		// 机构级别
		String ORGLVL = request.getAsString("orgLvl");
		// 机构类型
		String ORGTYPE = request.getAsString("orgType");
		// 创建日期
		String CREATEDATE = request.getAsString("createDate");
		// 状态（1-可用，0-不可用）
		String STATUS = request.getAsString("status");

		FOX_MGR_AUTH_ORGINFO_DBO dbo = new FOX_MGR_AUTH_ORGINFO_DBO();
		dbo.set_ORGID(ORGID);
		dbo.set_ORGCHINAME(ORGCHINAME);
		dbo.set_ORGENGNAME(ORGENGNAME);
		dbo.set_PARENTORGID(PARENTORGID);
		dbo.set_ORGLVL(ORGLVL);
		dbo.set_ORGTYPE(ORGTYPE);
		if (StringUtilEx.isNullOrEmpty(CREATEDATE)) {
			// 如果创建日期为空，说明第一次添加
			dbo.set_CREATEDATE(DateUtil.getFormatDate());
			dbo.set_CREATETIME(DateUtil.getFormatTime());
		}
		dbo.set_STATUS(STATUS);

		int result = FOX_MGR_AUTH_ORGINFO_DAO.insert(dbo);
		if (result == -1) {
			response.put(IResponseConstant.retCode, IResponseConstant.FAILED);
			response.put(IResponseConstant.retMsg, "添加失败");
		} else {
			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			response.put(IResponseConstant.retMsg, "添加成功");
		}
		return response;
	}

	// 更新
	public JsonResponse updateOrgInfo(JsonRequest request) throws Exception {
		JsonResponse response = new JsonResponse();
		String ORGID = request.getAsString("orgId");
		String ORGCHINAME = request.getAsString("orgChiName");
		String ORGENGNAME = request.getAsString("orgEngName");
		String PARENTORGID = request.getAsString("parentOrgId");
		String ORGLVL = request.getAsString("orgLvl");
		String ORGTYPE = request.getAsString("orgType");
		String CREATEDATE = request.getAsString("createDate");
		CREATEDATE = CREATEDATE.replace("年", "").replace("月", "")
				.replace("日", "");
		String CREATETIME = request.getAsString("createTime");
		CREATETIME = CREATETIME.replace(":", "");
		String STATUS = request.getAsString("status");

		FOX_MGR_AUTH_ORGINFO_DBO dbo = new FOX_MGR_AUTH_ORGINFO_DBO();
		dbo.set_ORGID(ORGID);
		dbo.set_ORGCHINAME(ORGCHINAME);
		dbo.set_ORGENGNAME(ORGENGNAME);
		dbo.set_PARENTORGID(PARENTORGID);
		dbo.set_ORGLVL(ORGLVL);
		dbo.set_ORGTYPE(ORGTYPE);
		dbo.set_CREATEDATE(CREATEDATE);
		dbo.set_CREATETIME(CREATETIME);

		// 更新 "更新日期" 字段
		dbo.set_UPDATEDATE(DateUtil.getFormatDate());
		dbo.set_UPDATETIME(DateUtil.getFormatTime());
		dbo.set_STATUS(STATUS);

		FOX_MGR_AUTH_ORGINFO_DBO dboOld = new FOX_MGR_AUTH_ORGINFO_DBO();
		dboOld.set_ORGID(ORGID);
		int result = DbServiceUtil.executeUpdate(dbo, dboOld);
		if (result == -1) {
			response.put(IResponseConstant.retCode, IResponseConstant.FAILED);
			response.put(IResponseConstant.retMsg, "添加失败");
		} else {
			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			response.put(IResponseConstant.retMsg, "添加成功");
		}

		return response;
	}

	// 查询
	public JsonResponse queryOrgTree(JsonRequest request) throws Exception {
		JsonResponse response = new JsonResponse();
		String sql = "select t.ORGID,t.ORG_CN_NAME,t.ORG_ENG_NAME,t.PARENT_ORGID,t.ORG_LEVEL,t.ORG_TYPE,t.CREATE_DATE,"
				+"t.CREATE_TIME,t.UPDATE_DATE,t.UPDATE_TIME,t.STATUS,t2.org_cn_name as parent_org_name "
				+"from FOX_MGR_AUTH_ORGINFO t left join FOX_MGR_AUTH_ORGINFO t2 "
				+"on t2.orgid=t.parent_orgid order by cast(t.org_level as integer),cast(t.orgid as integer)";

		List<FOX_MGR_AUTH_ORGINFO_DBO> list = DbServiceUtil.executeQuery(sql,
				FOX_MGR_AUTH_ORGINFO_DBO.class);

		if (list == null) {
			response.put(IResponseConstant.retCode, IResponseConstant.FAILED);
			response.put(IResponseConstant.retMsg, "更新失败");
		} else {

			LoggerUtil.error("list========="
					+ new Gson().toJson(list).toString());
			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			response.put(IResponseConstant.retMsg, "更新成功");

			JsonArray jsonArray = toTree(new JsonObject(), "-1", list, 0, 1);
			String orgTreeData = jsonArray.toString().replace("ORGID", "orgId")
					.replace("ORGCHINAME", "orgChiName")
					.replace("ORGENGNAME", "orgEngName")
					.replace("PARENTorgId", "parentOrgId")
					.replace("PARENTORGNAME", "parentOrgName")
					.replace("ORGLVL", "orgLvl").replace("ORGTYPE", "orgType")
					.replace("CREATEDATE", "createDate")
					.replace("CREATETIME", "createTime")
					.replace("UPDATEDATE", "updateDate")
					.replace("UPDATETIME", "updateTime")
					.replace("STATUS", "status");
			response.put("orgTreeData", orgTreeData);

		}
		return response;
	}

	private JsonArray toTree(JsonObject jsonObject, String parentId,
			List<FOX_MGR_AUTH_ORGINFO_DBO> list, int start, int startLevel) {
		JsonArray jsonArray = new JsonArray();
		Gson gson = new Gson();
		while (start < list.size()) {
			FOX_MGR_AUTH_ORGINFO_DBO dbo = list.get(start);
			if (dbo.get_ORGLVL().equals(String.valueOf(startLevel))) {
				if (parentId.equals(dbo.get_PARENTORGID())) {
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