package service.common.bean;

import java.util.Map;



import both.constants.IResponseConstant;

import com.alibaba.fastjson.JSONObject;

/**
 * JSON响应数据
 * @author 江成
 *
 */
public class JsonResponse extends JsonMessage{

	/**
	 * 默认构造函数
	 */
	public JsonResponse(){
		super();
	}

	/**
	 * 构造函数
	 * 
	 * @param header
	 * @param data
	 */
	public JsonResponse(JSONObject header,JSONObject data) {
	    super(header,data);
	}
	
	
	/**
	 * 从bsp中拆分数据
	 * 
	 * @param result
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static JsonResponse dealDataFromBSP(Map<String, Object> result, String tellerSeq) {
		JsonResponse response = new JsonResponse();
		
		// 判断业务操作是否成功
		Boolean success = (Boolean) result.get("success");
		response.put(IResponseConstant.retCode, result.get(IResponseConstant.retCode));
		if (success) {
			response.put(IResponseConstant.retMsg, "成功");
		} else {
			response.put(IResponseConstant.retMsg, result.get(IResponseConstant.retMsg));
		}
		//先把response中date替换为BSP反馈的body中的Map
		response.setData(new JSONObject(result));
		//再在response的date中增加额外的数据
		response.put(IResponseConstant.reqCode, tellerSeq);
		return response;
	}
}