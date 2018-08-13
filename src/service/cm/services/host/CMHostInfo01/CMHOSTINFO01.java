package service.cm.services.host.CMHostInfo01;

import javax.xml.ws.Response;

import both.common.util.StringUtilEx;
import both.constants.IResponseConstant;
import service.cm.core.db.CMHostInfo.FOX_MGR_CM_HOSTINFO_DAO;
import service.cm.core.db.CMHostInfo.FOX_MGR_CM_HOSTINFO_DBO;
import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;
import cn.com.bankit.phoenix.trade.Service;

/**
 * 新增-FOX_CM_HOSTINFO 主机信息表
 */
public class CMHOSTINFO01 extends Service<JsonRequest, JsonResponse> {

	/**
	 * 构造函数
	 */
	public CMHOSTINFO01() {
		super();
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
		if(!StringUtilEx.isNullOrEmpty(HOSTIP)){
			dbo.set_HOSTIP(HOSTIP);
		}
		// 文件传输方式，01-ftp方式 02-sftp方式
		if(!StringUtilEx.isNullOrEmpty(FILEMODE)){
			dbo.set_FILEMODE(FILEMODE);
		}
		// 文件传输端口
		if(!StringUtilEx.isNullOrEmpty(FILEPORT)){
			dbo.set_FILEPORT(FILEPORT);
		}
		// 文件传输用户名
		if(!StringUtilEx.isNullOrEmpty(FILEUSERNAME)){
			dbo.set_FILEUSERNAME(FILEUSERNAME);
		}
		// 文件传输密码
		if(!StringUtilEx.isNullOrEmpty(FILEPASSWORD)){
			dbo.set_FILEPASSWORD(FILEPASSWORD);
		}
		// 登录方式
		if(!StringUtilEx.isNullOrEmpty(LOGINMODE)){
			dbo.set_LOGINMODE(LOGINMODE);
		}
		// 登录端口
		if(!StringUtilEx.isNullOrEmpty(LOGINPORT)){
			dbo.set_LOGINPORT(LOGINPORT);
		}
		// 登录用户名
		if(!StringUtilEx.isNullOrEmpty(LOGINUSERNAME)){
			dbo.set_LOGINUSERNAME(LOGINUSERNAME);
		}
		// 登录密码
		if(!StringUtilEx.isNullOrEmpty(LOGINPASSWORD)){
			dbo.set_LOGINPASSWORD(LOGINPASSWORD);
		}
		// 拼装应答报文
		JsonResponse response = new JsonResponse();

		// 读取数据库
		int result = FOX_MGR_CM_HOSTINFO_DAO.insert(dbo);
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
}