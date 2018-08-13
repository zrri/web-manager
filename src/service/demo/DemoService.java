package service.demo;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;
import cn.com.bankit.phoenix.resource.ResourceManager;
import cn.com.bankit.phoenix.trade.Service;

/**
 * demo service
 * 
 * @author 江成
 * 
 */
public class DemoService extends Service<JsonRequest, JsonResponse> {

	/**
	 * 构造函数
	 */
	public DemoService() {
		super();
	}

	/**
	 * execute
	 */
	public JsonResponse execute(JsonRequest request) throws Exception {
		String s=request.getData().toJSONString();
		System.out.println("收到数据:"+s);
		//定义返回数据
		JsonResponse response=new JsonResponse();
		//获取名称
		String name=(String)request.get("name");
		if("江成".equals(name)){
			response.put("rp", "100 (哇，人品爆棚了good!!!)");
		}else{
			int rp=name.hashCode()%100;
			StringBuilder sb=new StringBuilder();
			sb.append(rp);
			if(rp>=80){
				sb.append(" (人品不错哦)");
			}else if(rp>=60){
				sb.append(" (人品一般般嘛)");
			}else if(rp>30){
				sb.append(" (人品真差)");
			}else{
				sb.append(" (人品那么差，兄弟你从前世就开始做坏事了)");
			}
			
			response.put("rp", sb.toString());
		}
		response.put("time", System.currentTimeMillis());
		//response.put("test", getData());
		System.out.println("返回数据:"+response.getData().toJSONString());
		return response;
	}
	
	/**
	 * 获取测试数据
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private String getData() throws Exception{
		ResourceManager resourceManager=ResourceManager.getInstance();
		String workspaceRoot=resourceManager.getWorkspaceRoot();
		String projectName=resourceManager.getProjectName(this.getClass());
		String path=workspaceRoot+"/"+projectName+"/resource/para/dataSource/test.txt";
		ByteArrayOutputStream byteOut=new ByteArrayOutputStream();
		InputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
		try{
			int len=-1;
			byte[] buffer=new byte[1024];
			
			while((len=in.read(buffer))!=-1){
				byteOut.write(buffer,0,len);
			}
		}finally{
		    in.close();
		}
		return byteOut.toString();
	}
	
}