package service.demo;

import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;
import cn.com.bankit.phoenix.trade.Service;

public class TestService extends Service<JsonRequest, JsonResponse>{

	
	public TestService(){
		super();
	}
	
	@Override
	public JsonResponse execute(JsonRequest request) throws Exception {
//		String name = (String) request.get("name");
//		String age = (String) request.get("age");
		JsonResponse response=new JsonResponse();
//		response.put("name", name+",hello");
//		response.put("age", age);
		return response;
	}

	public JsonResponse testResponse(JsonRequest request){
		String name = (String) request.get("name");
		String age = (String) request.get("age");
		JsonResponse response=new JsonResponse();
		response.put("name", name+",hello");
		response.put("age", age);
		return response;
	}
	

}