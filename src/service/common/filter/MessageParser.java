package service.common.filter;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Map;

import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;
import both.common.util.LoggerUtil;
import cn.com.bankit.phoenix.communication.http.HttpMessage;
import cn.com.bankit.phoenix.trade.filter.Event;
import cn.com.bankit.phoenix.trade.filter.Filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 消息解析过滤器
 * 
 * @author 江成
 * 
 */
public class MessageParser extends Filter {

	/**
	 * 构造函数
	 */
	public MessageParser() {
		super();
	}

	/**
	 * 判断是否为JSON字符串
	 * 
	 * @param str
	 * @return
	 */
	private boolean isJsonString(Object obj) {
		if (obj instanceof String) {
			String str = (String) obj;
			// 判断字符串是否为空
			if (str == null || str.length() == 0) {
				return false;
			}

			int len = str.length();

			char firstChar = str.charAt(0);
			char lastChar = str.charAt(len - 1);

			if (firstChar == '{' && lastChar == '}') {
				return true;
			}

			if (firstChar == '[' && lastChar == ']') {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否为multipart/form-data or multipart/mixed stream
	 * 
	 * @param contentType
	 * @return
	 */
	private boolean isMultipart(String contentType) {
		if (contentType == null || contentType.length() == 0) {
			return false;
		}
		String s = contentType.toLowerCase();
		if (s.indexOf("multipart/form-data") != -1
				|| s.indexOf("multipart/mixed") != -1) {
			return true;
		}
		return false;
	}

	/**
	 * before
	 */
	public void before(Event e) throws Exception {
		// 获取入参
		Object inBean = e.getInBean();
		LoggerUtil.debug("before inBean-----------------> str:"+inBean.toString());
		if (inBean instanceof HttpMessage) {
			HttpMessage httpMessage = (HttpMessage) inBean;
			// 获取内容类型
			String contentType = httpMessage.getHeader("content-type");
			// 如果为MULTIPART不进行解析，直接发送到
			if (this.isMultipart(contentType)) {
				return;
			}
			
			Object content = httpMessage.getContent();
			LoggerUtil.debug("content:"+content.toString());
			// 如果入参为字符串转换为JSON对象
			if (this.isJsonString(content)) {
				String str = (String) content;
				try {
					// 解析JSON
					Object obj = JSON.parse(str);
					LoggerUtil.debug("----> "+obj.getClass().getName());
					if (obj instanceof JSONObject) {
						JSONObject jsonObj = (JSONObject) obj;
						// 获取请求头
						JSONObject header = jsonObj.getJSONObject("header");
						// 获取请求数据
						JSONObject data = jsonObj.getJSONObject("data");
						// 定义请求数据
						JsonRequest request = new JsonRequest();
						// 设置头
						if (header != null) {
							request.setHeaders(header);
						}
						// 设置内容
						if (data != null) {
							request.setData(data);
						} else {
							request.setData(jsonObj);
						}
						// 设置bean
						e.setInBean(request);
					} else {
						// 设置bean
						e.setInBean(obj);
					}
					return;
				} catch (Exception ex) {
					// 打印错误日志
					LoggerUtil.error(ex.getMessage(), ex);
					// 抛出异常
					throw ex;
				}
			} else if (content instanceof Map) {
				@SuppressWarnings("unchecked")
				Map<String, Object> map = (Map<String, Object>) content;
				// 定义请求数据
				JsonRequest request = new JsonRequest();
				// 赋值给request
				for (String key : map.keySet()) {
					request.put(key, map.get(key));
				}
				// 设置bean
				e.setInBean(request);

				return;
			}

		}
		// 为合法数据，抛出异常
		throw new Exception("Illegal data format");

	}

	/**
	 * after
	 */
	public void after(Event e) throws Exception {
		// 获取出参
		Object outBean = e.getOutBean();
		if(outBean instanceof HttpMessage){
			return;
		}
		
		// 如果出参为为JSON对象转换为字符串
		if (outBean instanceof JsonResponse) {
			// 转换对象
			JsonResponse rsp = (JsonResponse) outBean;
			// 获取header
			JSONObject header = rsp.getHeaders();
			// 获取data
			JSONObject data = rsp.getData();
			// 创建JSON对象
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("header", header);
			jsonObj.put("data", data);
			
			//获取服务器IP
			String IP = getServerIp();
			jsonObj.put("serverIP", IP);
			
			// 转换为JSON字符串
			Object jsonString = JSON.toJSONString(jsonObj);
			System.out.println("after-----------------> str:"+jsonString);
			//定义HTTP message
			HttpMessage httpMsg=new HttpMessage();
			httpMsg.setContent(jsonString);
			e.setOutBean(httpMsg);
			return;
		} else if (!(outBean instanceof String)) {
			// 转换为JSON字符串
			Object jsonString = JSON.toJSONString(outBean);
			//定义HTTP message
			HttpMessage httpMsg=new HttpMessage();
			httpMsg.setContent(jsonString);
			e.setOutBean(httpMsg);
			return;
		}

		// 为合法数据，抛出异常
		throw new Exception("Illegal data format");
	}
	
	/**
	 * 服务器IP
	 * @return
	 */
	public static String  getServerIp(){
		 String SERVER_IP = null;
	         try {
	              Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
	             InetAddress ip = null;
	             while (netInterfaces.hasMoreElements()) {
	                 NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
	                 ip = (InetAddress) ni.getInetAddresses().nextElement();
	                 SERVER_IP = ip.getHostAddress();
	                 if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
	                         && ip.getHostAddress().indexOf(":") == -1) {
	                     SERVER_IP = ip.getHostAddress();
	                     break;
	                } else {
	                     ip = null;
	                 }
	             }
	         } catch (SocketException e) {
	        	 LoggerUtil.error(e.getMessage(),e);
	         }
	     
         return SERVER_IP;
     }

}