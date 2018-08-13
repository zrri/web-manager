package service.demo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

//import lib.both.common.util.LoggerUtil;

import cn.com.bankit.phoenix.communication.http.HttpMessage;
import cn.com.bankit.phoenix.resource.ResourceManager;

/**
 * 下载服务
 * @author 江成
 *
 */
public class DownloadService {

	/**
	 * 构造函数
	 */
	public DownloadService() {
		super();
	}
	
	/**
	 * 执行
	 */
	public HttpMessage downloadFile(HttpMessage httpMessage) throws Exception {
		// 获取请求参数
		@SuppressWarnings("unchecked")
		Map<String, Object> reqMap = (Map<String, Object>) httpMessage
				.getContent();
		String fileName = (String) reqMap.get("fileName");
	
		ResourceManager resourceManager=ResourceManager.getInstance();
		String workspaceRoot=resourceManager.getWorkspaceRoot();
		String projectName=resourceManager.getProjectName(this.getClass());
		String path=workspaceRoot+"/"+projectName+"/resource/images/"+fileName;
//		LoggerUtil.debug("path====++++"+path);
		InputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
		HttpMessage respMsg = new HttpMessage();
		respMsg.setHeader("Content-Type", "text/html");
		respMsg.setContent(in);
		return respMsg;
	}
}