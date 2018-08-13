package service.common.filter;

import service.common.bean.JsonResponse;
import both.common.util.StringUtilEx;
import both.constants.IResponseConstant;
import cn.com.bankit.phoenix.trade.filter.Event;
import cn.com.bankit.phoenix.trade.filter.Filter;

/**
 * 
 * 
 */
public class RetCodeFilter extends Filter {

	/**
	 * 构造函数
	 */
	public RetCodeFilter() {
		super();
	}

	/**
	 * before
	 */
	public void before(Event e) throws Exception {

	}

	/**
	 * after
	 */
	public void after(Event e) throws Exception {
		
		Object outBean = e.getOutBean();
		if (!(outBean instanceof JsonResponse))
			return;
		// 转换对象
		JsonResponse rsp = (JsonResponse) outBean;
		// 检查是否有返回码字段
		if (StringUtilEx.isNullOrEmpty(rsp
				.getAsString(IResponseConstant.retCode))) {
			rsp.setHeader(IResponseConstant.retCode, IResponseConstant.FAILED);

		} else {
			//将data中的返回码提取到header中
			rsp.setHeader(IResponseConstant.retCode,
					rsp.get(IResponseConstant.retCode));
			//去除data中的返回码
			rsp.getData().remove(IResponseConstant.retCode);
		}

		// 检查是否有返回信息字段
		if (StringUtilEx.isNullOrEmpty(rsp
				.getAsString(IResponseConstant.retMsg))) {
			rsp.setHeader(IResponseConstant.retMsg, "服务未设置返回信息");
		} else {
			//将data中的返回信息提取到header中
			rsp.setHeader(IResponseConstant.retMsg,
					rsp.get(IResponseConstant.retMsg));
			//去除data中的返回信息
			rsp.getData().remove(IResponseConstant.retMsg);
		}

		e.setOutBean(rsp);
	}

}