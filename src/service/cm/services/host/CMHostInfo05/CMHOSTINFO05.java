package service.cm.services.host.CMHostInfo05;

import both.common.util.LoggerUtil;
import both.common.util.StringUtilEx;
import both.constants.IResponseConstant;
import both.db.DBService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import service.cm.core.db.CMHostInfo.FOX_MGR_CM_HOSTINFO_DAO;
import service.cm.core.db.CMHostInfo.FOX_MGR_CM_HOSTINFO_DBO;
import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;
import cn.com.bankit.phoenix.trade.Service;

/**
 * 新增-FOX_CM_HOSTINFO 主机信息表
 */
public class CMHOSTINFO05 extends Service<JsonRequest, JsonResponse> {

	/**
	 * 构造函数
	 */
	public CMHOSTINFO05() {
		super();
	}
	public boolean isExistHostID(String HOSTIP) throws Exception{
		String sql = "select HOST_IP,FILE_TRANSPORT_WAY,FILE_TRANSPORT_PORT,FILE_TRANSPORT_USERNAME,FILE_TRANSPORT_PASSWORD,LOGIN_WAY,LOGIN_PORT,LOGIN_USERNAME,LOGIN_PASSWORD from FOX_MGR_CM_HOSTINFO where host_ip='"+HOSTIP+"'";
				

				List<Map<String, Object>> resultList = DBService.executeQuery(sql);
		 
		       if (resultList == null) {
		    	  
					 return false;
				} else {
					 
					 
					 return resultList.size()>0;
			 }
	}
    //判断组名或组编号是否已经存在
	public JsonResponse isExistHostID(JsonRequest request) throws Exception {
	
        JsonResponse response = new JsonResponse();
        String HOSTIP = request.getAsString("HOSTIP");	
		 
		 
	    boolean isexist= this.isExistHostID(HOSTIP);
				 
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
	/**
	 * execute
	 */
	public JsonResponse execute(JsonRequest request) throws Exception {
		// 从请求报文中解析参数
		// 主机ip
		String HOSTIP = request.getAsString("HOSTIP");
		// 文件传输方式，01-ftp方式 02-sftp方式
		String FILEMODE = request.getAsString("FILEMODE");
		// 文件传输端口
		String FILEPORT = request.getAsString("FILEPORT");
		// 文件传输用户名
		String FILEUSERNAME = request.getAsString("FILEUSERNAME");
		// 文件传输密码
		String FILEPASSWORD = request.getAsString("FILEPASSWORD");
		// 登录方式
		String LOGINMODE = request.getAsString("LOGINMODE");
		// 登录端口
		String LOGINPORT = request.getAsString("LOGINPORT");
		// 登录用户名
		String LOGINUSERNAME = request.getAsString("LOGINUSERNAME");
		// 登录密码
		String LOGINPASSWORD = request.getAsString("LOGINPASSWORD");

		// 校验

		// 生成DBO
		FOX_MGR_CM_HOSTINFO_DBO dbo = new FOX_MGR_CM_HOSTINFO_DBO();
		// 主机ip
		if (!StringUtilEx.isNullOrEmpty(HOSTIP)) {
			dbo.set_HOSTIP(HOSTIP);
		}
		// 文件传输方式，01-ftp方式 02-sftp方式
		if (!StringUtilEx.isNullOrEmpty(FILEMODE)) {
			dbo.set_FILEMODE(FILEMODE);
		}
		// 文件传输端口
		if (!StringUtilEx.isNullOrEmpty(FILEPORT)) {
			dbo.set_FILEPORT(FILEPORT);
		}
		// 文件传输用户名
		if (!StringUtilEx.isNullOrEmpty(FILEUSERNAME)) {
			dbo.set_FILEUSERNAME(FILEUSERNAME);
		}
		// 文件传输密码
		if (!StringUtilEx.isNullOrEmpty(FILEPASSWORD)) {
			dbo.set_FILEPASSWORD(FILEPASSWORD);
		}
		// 登录方式
		if (!StringUtilEx.isNullOrEmpty(LOGINMODE)) {
			dbo.set_LOGINMODE(LOGINMODE);
		}
		// 登录端口
		if (!StringUtilEx.isNullOrEmpty(LOGINPORT)) {
			dbo.set_LOGINPORT(LOGINPORT);
		}
		// 登录用户名
		if (!StringUtilEx.isNullOrEmpty(LOGINUSERNAME)) {
			dbo.set_LOGINUSERNAME(LOGINUSERNAME);
		}
		// 登录密码
		if (!StringUtilEx.isNullOrEmpty(LOGINPASSWORD)) {
			dbo.set_LOGINPASSWORD(LOGINPASSWORD);
		}

		// 读取数据库
		List<FOX_MGR_CM_HOSTINFO_DBO> result = FOX_MGR_CM_HOSTINFO_DAO.queryTable(dbo);

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
				FOX_MGR_CM_HOSTINFO_DBO item = result.get(i);
				map.put("FILEPORT", item.get_FILEPORT());
				map.put("HOSTIP", item.get_HOSTIP());
				map.put("FILEMODE", item.get_FILEMODE());
				map.put("FILEUSERNAME", item.get_FILEUSERNAME());
				map.put("FILEPASSWORD", item.get_FILEPASSWORD());
				map.put("LOGINMODE", item.get_LOGINMODE());
				map.put("LOGINPORT", item.get_LOGINPORT());
				map.put("LOGINUSERNAME", item.get_LOGINUSERNAME());
				map.put("LOGINPASSWORD", item.get_LOGINPASSWORD());
				list.add(map);
			}

			response.put("list", list);
			response.put(IResponseConstant.retCode, IResponseConstant.SUCCESS);
			response.put(IResponseConstant.retMsg, "成功");
		}

		return response;
	}
}