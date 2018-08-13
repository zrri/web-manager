package service.demo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import service.auth.core.f02.FOX_TELLER_TELLERINFO_DBO;
import service.auth.core.f02.FoxTellerInfoBean;
import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

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
		String invokeServiceIp = "http://guangzhou.btop.mobi:9191/action/serviceInvoke?";
		String serviceName = "trade/demo/demoService";
		// serviceName = "stm/demo1/updateSerial";
		// serviceName = "stm/demo1/updateSubSerial";
		// serviceName = "stm/voucher/voucherManagerInsert";
		// serviceName = "stm/demo1/addStock";
		StringBuilder sb = new StringBuilder();

		Map<String, String> header = new HashMap<String, String>();
		Map<String, String> data = new HashMap<String, String>();
		// header.put("arg", "1");
		// data.put("pinBlock", "5D6E639A2B0983A94ACA14B9B6B8AF13");
		// data.put("deviceId", "00001");
		// data.put("accountNo", "6231039901000917236");
		// header.put("deviceId", "IST10001");
		// data.put("serialM", "ISTDL00000001");
		// header.put("orgId", "903100100");
		data.put("num", "10");
		// header.put("mac", "00-18-7D-9C-64-41");

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

		Gson gson = new Gson();
		String bean = "{\"WORKERNO\":\"10001\"}";
		FoxTellerInfoBean tellerInfoBean = gson.fromJson(bean,
				FoxTellerInfoBean.class);
		List<FoxTellerInfoBean> beanList = new ArrayList<FoxTellerInfoBean>();
		beanList.add(tellerInfoBean);
		String s = gson.toJson(tellerInfoBean);
		System.out.println(s);
	}
}