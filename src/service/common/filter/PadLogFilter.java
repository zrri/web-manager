package service.common.filter;

import service.common.bean.JsonRequest;
import service.common.bean.JsonResponse;
import both.common.util.LoggerUtil;
import cn.com.bankit.phoenix.trade.filter.Event;
import cn.com.bankit.phoenix.trade.filter.Filter;

import com.alibaba.fastjson.JSONObject;

/**
 * 交易日志过滤器
 * @author 江成
 *
 */
public class PadLogFilter extends Filter {

	/**
	 * 构造函数
	 */
	public PadLogFilter() {
		super();
	}
	

	/**
	 * 调用前过滤
	 */
	public void before(Event e) throws Exception {
		//获取入参
		Object inBean = e.getInBean();
		
		//如果入参类型不为JsonRequest不进行处理
		if(!(inBean instanceof JsonRequest)){
			return;
		}
		
		//获取请求数据
		JsonRequest request=(JsonRequest)inBean;
		//获取头
	    JSONObject header=request.getHeaders();
	    //获取数据
		JSONObject body = request.getData();
		
		
		StringBuilder sb=new StringBuilder();
		sb.append("PAD in header:");
		sb.append(header.toJSONString());
		sb.append("\nPAD in data:");
		sb.append(body.toJSONString());
		//打印日志
		LoggerUtil.debug(sb.toString());
		
	}
	


	/**
	 * 调用后过滤
	 */
	public void after(Event e) throws Exception {
		//获取出参
		Object outBean = e.getInBean();
		
		//如果入参类型不为JsonResponse不进行处理
		if(!(outBean instanceof JsonResponse)){
			return;
		}
		
		//获取请求数据
		JsonResponse response=(JsonResponse)outBean;
		//获取头
	    JSONObject header=response.getHeaders();
	    //获取数据
		JSONObject body = response.getData();
		
		
		StringBuilder sb=new StringBuilder();
		sb.append("PAD out header:");
		sb.append(header.toJSONString());
		sb.append("\nPAD out data:");
		sb.append(body.toJSONString());
		//打印日志
		LoggerUtil.debug(sb.toString());
	}

}