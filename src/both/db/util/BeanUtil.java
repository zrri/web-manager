package both.db.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import both.common.util.LoggerUtil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author KevinFan
 *
 */
public class BeanUtil {

	/**
	 * 将Map传入的数据转化成javaBean
	 * @param bean
	 * @param data
	 */
	public static <T> T convertToBean(T bean,Map<String,Object> data) throws Exception {
		try {
			//遍历所有的数据
			for(Iterator<String> iter=data.keySet().iterator();iter.hasNext();) {
				//属性名称
				String fieldName =  iter.next();
				
				//获取当前属性的数据
				Object fieldData = data.get(fieldName);
				
				//首字母大写的属性名称
				String fieldNameTmp=fieldName.substring(0,1).toUpperCase().concat(fieldName.substring(1));
				
				try {
					//获取javabean当前属性的set方法
					Method setMethod = bean.getClass().getDeclaredMethod("set"+fieldNameTmp, fieldData.getClass());
					//调用属性设置方法
					setMethod.invoke(bean, fieldData);
				} catch (NoSuchMethodException e) {
					LoggerUtil.error(e.getMessage(),e);
				}
			}
		}catch (Exception e) {
			LoggerUtil.error(e.getMessage(),e);
		}
		return bean;
	}
	
	/**
	 * 将Map传入的数据转化成javaBean
	 * @param bean
	 * @param data
	 */
	public static <T> T convertToBean(T bean,JSONObject data) {
		
		try {
			//遍历所有的数据
			for(Iterator<String> iter=data.keySet().iterator();iter.hasNext();) {
				//属性名称
				String fieldName =  iter.next();
				
				//获取当前属性的数据
				Object fieldData = data.get(fieldName);
				if(fieldData==null || fieldData.equals("")){
					continue;
				}
				
				//首字母大写的属性名称
				String fieldNameTmp=fieldName.substring(0,1).toUpperCase().concat(fieldName.substring(1));
				
				try {
					//获取javabean当前属性的set方法
					Method setMethod = bean.getClass().getDeclaredMethod("set"+fieldNameTmp, fieldData.getClass());
					//调用属性设置方法
					setMethod.invoke(bean, fieldData);
				} catch (NoSuchMethodException e) {
					LoggerUtil.error(e.getMessage(),e);
				}
			}
		}catch (Exception e) {
			LoggerUtil.error(e.getMessage(),e);
		}
		return bean;
	}
	
	/**
	 * 将javaBean数据转化成JSONObject
	 */
	public static <T> JSONObject convertToJsonObject(T bean) {
		JSONObject data = new JSONObject();
		try {
			//获取当前javaBean的所有属性
			Field[] fields = bean.getClass().getDeclaredFields();
			//遍历属性
			for(int i=0; i<fields.length; i++) {
				//属性
				Field field = fields[i];
				//属性名称
				String fielddName = field.getName();
				//首字母大写的属性名称
				String fieldNameTmp=fielddName.substring(0,1).toUpperCase().concat(fielddName.substring(1));
				try {
					//获取javabean当前属性的get方法
					Method getMethod = bean.getClass().getDeclaredMethod("get"+fieldNameTmp);
					//调用获取属性方法
					Object object = getMethod.invoke(bean);
					
					if(object != null) {
						data.put(fielddName, object);
					}
					
				} catch (NoSuchMethodException e) {
					LoggerUtil.error(e.getMessage(),e);
				}
			}
		}catch (Exception e) {
			LoggerUtil.error(e.getMessage(),e);
		}
		return data;
	}
	
	/**
	 * 将javaBean数据转化成Map
	 */
	public static <T> Map<String,Object> convertToMap(T bean) {
		Map<String,Object> data = new HashMap<String,Object>();
		try {
			//获取当前javaBean的所有属性
			Field[] fields = bean.getClass().getDeclaredFields();
			//遍历属性
			for(int i=0; i<fields.length; i++) {
				//属性
				Field field = fields[i];
				//属性名称
				String fielddName = field.getName();
				//首字母大写的属性名称
				String fieldNameTmp=fielddName.substring(0,1).toUpperCase().concat(fielddName.substring(1));
				try {
					//获取javabean当前属性的get方法
					Method getMethod = bean.getClass().getDeclaredMethod("get"+fieldNameTmp);
					//调用获取属性方法
					Object object = getMethod.invoke(bean);
					
					if(object != null) {
						data.put(fielddName, object);
					}
					
				} catch (NoSuchMethodException e) {
					LoggerUtil.error(e.getMessage(),e);
				}
			}
		}catch (Exception e) {
			LoggerUtil.error(e.getMessage(),e);
		}
		return data;
	}
	
	/**
	 * 将list<JavaBean>转化成JsonArray
	 */
	public static <T> JSONArray convertToJsonArray(List<T> data) {
		//返回数据
		JSONArray res  = new JSONArray();
		//遍历list对象
		for(int i=0;i<data.size();i++ ) {
			JSONObject obj = convertToJsonObject(data.get(i));
			res.add(obj);
		}
		
		return res;
	}
}
