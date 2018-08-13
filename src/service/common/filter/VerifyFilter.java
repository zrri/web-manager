package service.common.filter;

import java.util.Map;

import service.common.bean.JsonRequest;
import cn.com.bankit.phoenix.commons.datatype.TextBoolean;
import cn.com.bankit.phoenix.trade.filter.Event;
import cn.com.bankit.phoenix.trade.filter.Filter;

/**
 * 交易过滤器
 * @author 江成
 *
 */
public class VerifyFilter extends Filter {

	/**
	 * 构造函数
	 */
	public VerifyFilter() {
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
		
		//获取service附件属性集合
		Map<String,Object> properties=e.getContext().getServiceProperties();
		//判断调用的service是否需要校验处理
		if(!properties.containsKey("verify")){
			return;
		}
		
		//获取交易属性集合
		@SuppressWarnings("unchecked")
		Map<String,String> verifyProprties=(Map<String,String>)properties.get("verify");
		//检查
		for(String key:verifyProprties.keySet()){
			//获取检查类型
			String checkType=verifyProprties.get(key);
			//检查
			TextBoolean checkRes=this.check(request, key, checkType);
			
			if(!checkRes.getValue()){
				String msg= checkRes.getText();
				//校验不通过，抛出异常
				throw new Exception(msg);
			}
			
		}
		
	}
	
	/**
	 * 检查
	 * @param request
	 * @return
	 */
	private TextBoolean check(JsonRequest request,String key, String checkType){
		if("require".equalsIgnoreCase(checkType)){
			System.out.println("======> "+request.getData().toJSONString());
			//获取value
			Object value=request.get(key);
			//判断是否为空
			if(value==null){
				StringBuilder sb=new StringBuilder();
				sb.append(key);
				sb.append(" not allow empty");
				TextBoolean res=new TextBoolean(sb.toString(),false);
				return res;
			}
			
			if(value instanceof String){
				String s=(String)value;
				if(s.length()==0){
					StringBuilder sb=new StringBuilder();
					sb.append(key);
					sb.append(" not allow empty");
					TextBoolean res=new TextBoolean(sb.toString(),false);
					return res;
				}
			}
			return new TextBoolean(true);
		}
		
		return new TextBoolean(true);
		
	}

	/**
	 * 调用后过滤
	 */
	public void after(Event e) throws Exception {
	    //do nothing
	}

}