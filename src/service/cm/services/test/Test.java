package service.cm.services.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;

import com.alibaba.fastjson.JSONObject;

/**
 * 测试
 * 
 * @author 江成
 * 
 */
public class Test {

	public JsonResponse execute(JsonRequest request) {

		return null;
	}

	/**
	 * 获取随机id
	 * 
	 * @param length
	 *            需要生成的ID长度
	 * @return
	 */
	public String getRandomId(int length) {
		String[] str = new String[] { "1", "2", "3", "4", "5", "6", "7", "8",
				"9", "0" };
		StringBuilder sb = new StringBuilder();
		sb.append("V");
		int strLen = str.length;
		Random r = new Random(System.currentTimeMillis());
		for (int i = 1; i < length; i++) {
			int index = r.nextInt(strLen);
			sb.append(str[index]);
		}
		System.out.println(sb.toString());
		return sb.toString();
	}

	public static void compareDateTest() throws ParseException {
		String start = "2016-07-29 00:00:00";
		String end = "2016-07-29 23:59:59";

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = sdf.parse(start);
		Date endDate = sdf.parse(end);

		Date now = new Date(System.currentTimeMillis());

		System.out.println(now.compareTo(startDate));
		System.out.println(now.compareTo(endDate));

		System.out.println(now.before(endDate));

	}

	/**
	 * main函数
	 * 
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		String invokeServiceIp = "http://guangzhou.btop.mobi:9191/services/serviceInvoke?";
		String serviceName = "cm/host/addHostInfo";
		serviceName = "cm/node/queryNodeInfoList";
		serviceName = "cm/node/getNodeDetailInfo";
		serviceName = "cm/node/startNodes";
		serviceName = "cm/node/getNodeStatus";
		serviceName = "cm/node/stopNodes";
//		serviceName = "stm/demo1/updateSubSerial";
//		serviceName = "stm/voucher/voucherManagerInsert";
//		serviceName = "stm/demo1/addStock";
		StringBuilder sb = new StringBuilder();

		Map<String, String> header = new HashMap<String, String>();
		Map<String,Object> data = new HashMap<String, Object>();
//		data.put("HOSTIP", "10");
//		data.put("FILEMODE", "1");
//		data.put("FILEPORT", "10");
//		
//		data.put("FILEUSERNAME", "10");
//		data.put("FILEPASSWORD", "10");
//		data.put("LOGINMODE", "1");
//		data.put("LOGINPORT", "10");
//		data.put("LOGINUSERNAME", "10");
//		data.put("LOGINPASSWORD", "10");
		
//		data.put("HOSTIP", "172.16.0.4");
//		data.put("NAME", "BIPS_A");
		
		data.put("userId", "999999");
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map<String,String> map1 = new HashMap<String, String>();
		map1.put("hostip", "172.16.0.4");
		map1.put("nodeName", "BIPS_A");
		list.add(map1);
		Map<String,String> map2= new HashMap<String, String>();
		map2.put("hostip", "172.16.0.4");
		map2.put("nodeName", "BIPS_B");
		
		
		list.add(map2);
		
		data.put("list",list);
		
		// 从请求报文中解析参数
	

		JSONObject json = new JSONObject();
		json.put("header", header);
		json.put("data", data);

		sb.append(invokeServiceIp);
		sb.append("_$id=1&");
		sb.append("_$service=");
		sb.append(serviceName);
		sb.append("&_$sessionid=1-23232&");
		sb.append("_$data=");
		sb.append(json.toJSONString());
		System.out.println(sb.toString());
	}
}