package service.common.bean;

import com.alibaba.fastjson.JSONObject;

/**
 * JSON信息
 * 
 * @author 江成
 *
 */
public class JsonMessage {


	/**
	 * header
	 */
	private JSONObject header;
	
	/**
	 * data
	 */
	private JSONObject data;
	
	/**
	 * 默认构造函数
	 */
	public JsonMessage(){
		this.header=new JSONObject();
		this.data=new JSONObject();
	}

	/**
	 * 构造函数
	 * 
	 * @param header
	 * @param data
	 */
	public JsonMessage(JSONObject header,JSONObject data) {
		this.header=header;
		this.data=data;
	}
	
	/**
	 * 设置header
	 * @param header
	 */
	public void setHeaders(JSONObject header){
		this.header=header;
	}
	
	/**
	 * 获取header
	 * @return
	 */
	public JSONObject getHeaders(){
		return this.header;
	}
	
	/**
	 * 设置data
	 * @param header
	 */
	public void setData(JSONObject data){
		this.data=data;
	}
	
	/**
	 * 获取data
	 * @return
	 */
	public JSONObject getData(){
		return this.data;
	}
	
	/**
	 * 获取头信息
	 * @param key
	 * @return
	 */
	public Object getHeader(String key){
		if(header == null){
			return null;
		}
		return header.get(key);
	}
	
	/**
	 * 设头信息
	 * @param key
	 * @param value
	 */
	public void setHeader(String key,Object value){
		this.header.put(key, value);
	}
	
	/**
	 * 获取数据
	 * @param key
	 * @return
	 */
	public Object get(String key){
		if(data==null){
			return null;
		}
		return data.get(key);
	}
	
	/**
	 * 以字符串形式返回数据
	 * 
	 * @param key
	 * @return
	 */
	public String getAsString(String key){
		Object value=this.get(key);
		if(value==null){
			return null;
		}
		String str=String.valueOf(value);
		return str;
	}
	
	/**
	 * 以整数形式返回数据
	 * 
	 * @param key
	 * @return
	 */
	public Integer getAsInteger(String key){
		String str=this.getAsString(key);
		if(str==null){
			return null;
		}
		Integer value=Integer.parseInt(str);
		return value;
	}
	
	/**
	 * 以长整数形式返回数据
	 * 
	 * @param key
	 * @return
	 */
	public Long getAsLong(String key){
		String str=this.getAsString(key);
		if(str==null){
			return null;
		}
		Long value=Long.parseLong(str);
		return value;
	}
	
	/**
	 * 以float形式返回数据
	 * 
	 * @param key
	 * @return
	 */
	public Float getAsFloat(String key){
		String str=this.getAsString(key);
		if(str==null){
			return null;
		}
		Float value=Float.parseFloat(str);
		return value;
	}
	
	/**
	 * 以double形式返回数据
	 * 
	 * @param key
	 * @return
	 */
	public Double getAsDouble(String key){
		String str=this.getAsString(key);
		if(str==null){
			return null;
		}
		Double value=Double.parseDouble(str);
		return value;
	}
	
	/**
	 * 设置数据
	 * @param key
	 * @param value
	 */
	public void put(String key,Object value){
		this.data.put(key, value);
	}
}