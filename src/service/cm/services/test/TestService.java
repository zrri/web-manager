package service.cm.services.test;

import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;
import cn.com.bankit.phoenix.trade.Service;

public class TestService extends Service<JsonRequest, JsonResponse> {

	/**
	 * 构造函数
	 */
	public TestService() {
		super();
	}
	
	/**
	 * 执行
	 */
	public JsonResponse execute(JsonRequest request) throws Exception {
		String s=request.getData().toJSONString();
		System.out.println("收到数据:"+s);
		
		TestClient.test(new String[0]);
		
		//定义返回数据
		JsonResponse response=new JsonResponse();
		//获取名称
		String name=(String)request.get("name");
        response.put("name", name);
		response.put("time", System.currentTimeMillis());
		//response.put("test", getData());
		System.out.println("返回数据:"+response.getData().toJSONString());
		return response;
	}
}