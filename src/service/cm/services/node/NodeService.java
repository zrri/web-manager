package service.cm.services.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import service.cm.core.common.node.NodeInfo;
import service.cm.core.common.node.NodeQueryUtil;
import service.cm.core.db.CMHostInfo.FOX_MGR_CM_HOSTINFO_DAO;
import service.cm.core.db.CMHostInfo.FOX_MGR_CM_HOSTINFO_DBO;
import service.cm.core.db.NodeInfo.FOX_MGR_CM_NODEINFO_DAO;
import service.cm.core.db.NodeInfo.FOX_MGR_CM_NODEINFO_DBO;
import service.cm.core.node.Node;
import service.cm.core.node.NodeManager;
import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;
import both.common.util.LoggerUtil;
import both.common.util.StringUtilEx;
import both.constants.IResponseConstant;
import both.db.DBService;
import both.db.DbParam;
import both.db.DbParam.DbSqlType;
import both.db.util.DbServiceUtil;
import cn.com.bankit.phoenix.trade.Service;

import com.alibaba.fastjson.JSONArray;

/**
 * 节点信息  增删改查
 * @author liaozhijie
 *
 */
public class NodeService extends Service<JsonRequest, JsonResponse> {

	/**
	 * 
	 */
	public NodeService() {
		super();
	}

	public boolean isExistNodeID(String HOSTIP) throws Exception{
		String sql = "select HOST_IP,NODE_TYPE,APPLY_PATH,NODE_NAME,DESCRIPTION,ISLINK,LINK_DIRECTORY,UPDATE_DIRECTORY,APPLY_PORT,HTTP_PORT,JVM_PORT from FOX_MGR_CM_NODEINFO where host_ip='"+HOSTIP+"'";
				

				List<Map<String, Object>> resultList = DBService.executeQuery(sql);
		 
		       if (resultList == null) {
		    	  
					 return false;
				} else {
					 
					 
					 return resultList.size()>0;
			 }
	}
    //判断组名或组编号是否已经存在
	public JsonResponse isExistNodeID(JsonRequest request) throws Exception {
	
        JsonResponse response = new JsonResponse();
        String HOSTIP = request.getAsString("HOSTIP");	
		 
		 
	    boolean isexist= this.isExistNodeID(HOSTIP);
				 
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
	
	
	
	
	public JsonResponse AddNodeInfo(JsonRequest request) throws Exception {
		 
		String HOSTIP = request.getAsString("HOSTIP");	
	 
		String NODETYPE = request.getAsString("NODETYPE");	
	 
		String APPPATH = request.getAsString("APPPATH");	
	 
		String NAME = request.getAsString("NAME");	
		 
		String ISLINK = request.getAsString("ISLINK");	
		 
		String LINKDIRECTORY = request.getAsString("LINKDIRECTORY");	
	 
		String APPPORT = request.getAsString("APPPORT");	
		 
		String HTTPPORT = request.getAsString("HTTPPORT");	
		 
		String JVMPORT = request.getAsString("JVMPORT");	
		
		// 校验

		// 生成DBO
		FOX_MGR_CM_NODEINFO_DBO dbo = new FOX_MGR_CM_NODEINFO_DBO();
		// 主机ip
		if(!StringUtilEx.isNullOrEmpty(HOSTIP)){
			dbo.set_HOSTIP(HOSTIP);
		}
		// 文件传输方式，01-ftp方式 02-sftp方式
		if(!StringUtilEx.isNullOrEmpty(NODETYPE)){
			dbo.set_NODETYPE(NODETYPE);
		}
		// 文件传输端口
		if(!StringUtilEx.isNullOrEmpty(APPPATH)){
			dbo.set_APPPATH(APPPATH);
		}
		// 文件传输用户名
		if(!StringUtilEx.isNullOrEmpty(NAME)){
			dbo.set_NAME(NAME);
		}
		// 文件传输密码
		if(!StringUtilEx.isNullOrEmpty(ISLINK)){
			dbo.set_ISLINK(ISLINK);
		}
		// 登录方式
		if(!StringUtilEx.isNullOrEmpty(LINKDIRECTORY)){
			dbo.set_LINKDIRECTORY(LINKDIRECTORY);
		}
		// 登录端口
		if(!StringUtilEx.isNullOrEmpty(APPPORT)){
			dbo.set_APPPORT(APPPORT);
		}
		// 登录用户名
		if(!StringUtilEx.isNullOrEmpty(HTTPPORT)){
			dbo.set_HTTPPORT(HTTPPORT);
		}
		// 登录密码
		if(!StringUtilEx.isNullOrEmpty(JVMPORT)){
			dbo.set_JVMPORT(JVMPORT);
		}
		// 拼装应答报文
		JsonResponse response = new JsonResponse();

		// 读取数据库
		int result = FOX_MGR_CM_NODEINFO_DAO.insert(dbo);
		if (result == -1) {
			response.put(IResponseConstant.retCode, IResponseConstant.FAILED_DB_ERROR);
			response.put(IResponseConstant.retMsg, "主机节点插入失败");
		} else {
			// 成功
			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			response.put(IResponseConstant.retMsg, "成功");
		}

	
		return response;
	}
	
	
	public JsonResponse editNodeInfo(JsonRequest request) throws Exception {
		 
		String HOSTIP = request.getAsString("HOSTIP");	
	 
		String NODETYPE = request.getAsString("NODETYPE");	
	 
		String APPPATH = request.getAsString("APPPATH");	
	 
		String NAME = request.getAsString("NAME");	
		 
		String ISLINK = request.getAsString("ISLINK");	
		 
		String LINKDIRECTORY = request.getAsString("LINKDIRECTORY");	
	 
		String APPPORT = request.getAsString("APPPORT");	
		 
		String HTTPPORT = request.getAsString("HTTPPORT");	
		 
		String JVMPORT = request.getAsString("JVMPORT");	
		
		// 校验

		// 生成DBO
		FOX_MGR_CM_NODEINFO_DBO dbo = new FOX_MGR_CM_NODEINFO_DBO();
		// 主机ip
		if(!StringUtilEx.isNullOrEmpty(HOSTIP)){
			dbo.set_HOSTIP(HOSTIP);
		}
		// 文件传输方式，01-ftp方式 02-sftp方式
		if(!StringUtilEx.isNullOrEmpty(NODETYPE)){
			dbo.set_NODETYPE(NODETYPE);
		}
		// 文件传输端口
		if(!StringUtilEx.isNullOrEmpty(APPPATH)){
			dbo.set_APPPATH(APPPATH);
		}
		// 文件传输用户名
		if(!StringUtilEx.isNullOrEmpty(NAME)){
			dbo.set_NAME(NAME);
		}
		// 文件传输密码
		if(!StringUtilEx.isNullOrEmpty(ISLINK)){
			dbo.set_ISLINK(ISLINK);
		}
		// 登录方式
		if(!StringUtilEx.isNullOrEmpty(LINKDIRECTORY)){
			dbo.set_LINKDIRECTORY(LINKDIRECTORY);
		}
		// 登录端口
		if(!StringUtilEx.isNullOrEmpty(APPPORT)){
			dbo.set_APPPORT(APPPORT);
		}
		// 登录用户名
		if(!StringUtilEx.isNullOrEmpty(HTTPPORT)){
			dbo.set_HTTPPORT(HTTPPORT);
		}
		// 登录密码
		if(!StringUtilEx.isNullOrEmpty(JVMPORT)){
			dbo.set_JVMPORT(JVMPORT);
		}
		
		// 生成条件DBO
		FOX_MGR_CM_NODEINFO_DBO wheredbo = new FOX_MGR_CM_NODEINFO_DBO();
				// 主机ip
				if (!StringUtilEx.isNullOrEmpty(HOSTIP)) {
					wheredbo.set_HOSTIP(HOSTIP);
				}
		// 拼装应答报文
		JsonResponse response = new JsonResponse();

	   // 读取数据库
		int result = FOX_MGR_CM_NODEINFO_DAO.update(dbo, wheredbo);
		if (result == -1) {
			// 失败
			response.put(IResponseConstant.retCode,
					IResponseConstant.FAILED_DB_ERROR);
			response.put(IResponseConstant.retMsg, "更新失败");
		} else {
			// 成功

			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			response.put(IResponseConstant.retMsg, "成功");
		}


	
		return response;
	}
	
	
	
	
	
	
	@Override
	public JsonResponse execute(JsonRequest arg0) throws Exception {
		return null;
	}

	/**
	 * 查询所有节点信息
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public JsonResponse queryAllNodes(JsonRequest request) throws Exception {
		
		
		LoggerUtil.debug("-----------------查询服务器子节点开始-------------------");
		
		FOX_MGR_CM_NODEINFO_DBO dbo = new FOX_MGR_CM_NODEINFO_DBO();

		String HOSTIP = request.getAsString("HOSTIP");
		if (!StringUtilEx.isNullOrEmpty(HOSTIP)) {
			dbo.set_HOSTIP(HOSTIP);
		}
		// 读取数据库
		List<FOX_MGR_CM_NODEINFO_DBO> result = FOX_MGR_CM_NODEINFO_DAO.queryTable(dbo);

		// 拼装应答报文
		JsonResponse response = new JsonResponse();
		if (result == null) {
			// 失败
			response.put(IResponseConstant.retCode,
					IResponseConstant.FAILED_DB_ERROR);
			response.put(IResponseConstant.retMsg, "查询数据库出错");
		} else {
			// 成功
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			for (int i = 0, len = result.size(); i < len; i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				FOX_MGR_CM_NODEINFO_DBO item = result.get(i);
				map.put("HOSTIP", item.get_HOSTIP());
				map.put("NODETYPE", item.get_NODETYPE());
				map.put("APPPATH", item.get_APPPATH());
				map.put("NAME", item.get_NAME());
				map.put("DESCRIPTION", item.get_DESCRIPTION());
				map.put("ISLINK", item.get_ISLINK());
				map.put("LINKDIRECTORY", item.get_LINKDIRECTORY());
				map.put("UPDATEDIRECTORY", item.get_UPDATEDIRECTORY());
				map.put("APPPORT", item.get_APPPORT());
				map.put("HTTPPORT", item.get_HTTPPORT());
				map.put("JVMPORT", item.get_JVMPORT());
				list.add(map);
			}

			response.put("list", list);
			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			response.put(IResponseConstant.retMsg, "成功");
		}
		
		LoggerUtil.debug("-----------------查询服务器子节点结束-------------------");

		return response;
	}
	public JsonResponse deleteNodeByList(JsonRequest request) throws Exception {
		JSONArray nodeArray = (JSONArray) request.get("list");

		DbParam param = new DbParam();

		for (int i = 0, len = nodeArray.size(); i < len; i++) {
			FOX_MGR_CM_NODEINFO_DBO dbo = new FOX_MGR_CM_NODEINFO_DBO();
			dbo.set_HOSTIP(nodeArray.getString(i));
			param.add(dbo, DbSqlType.Delete);
		}

		// 拼装应答报文
		JsonResponse response = new JsonResponse();

		try {
			DbServiceUtil.executeTransaction(param);

			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			response.put(IResponseConstant.retMsg, "成功");
		} catch (Exception e) {
			LoggerUtil.error(e.getMessage(), e);
			// 失败
			response.put(IResponseConstant.retCode,
					IResponseConstant.FAILED_DB_ERROR);
			response.put(IResponseConstant.retMsg, "查询数据库出错");

		}

		return response;
	}
	

}