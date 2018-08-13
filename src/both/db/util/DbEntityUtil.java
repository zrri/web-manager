/**
 * Special Declaration: These technical material reserved as the technical 
 * secrets by Bankit TECHNOLOGY have been protected by the "Copyright Law" 
 * "ordinances on Protection of Computer Software" and other relevant 
 * administrative regulations and international treaties. Without the written 
 * permission of the Company, no person may use (including but not limited to 
 * the illegal copy, distribute, display, image, upload, and download) and 
 * disclose the above technical documents to any third party. Otherwise, any 
 * infringer shall afford the legal liability to the company.
 *
 * 特别声明：本技术材料受《中华人民共和国著作权法》、《计算机软件保护条例》
 * 等法律、法规、行政规章以及有关国际条约的保护，浙江宇信班克信息技术有限公
 * 司享有知识产权、保留一切权利并视其为技术秘密。未经本公司书面许可，任何人
 * 不得擅自（包括但不限于：以非法的方式复制、传播、展示、镜像、上载、下载）使
 * 用，不得向第三方泄露、透露、披露。否则，本公司将依法追究侵权者的法律责任。
 * 特此声明！
 *
 * Copyright(C) 2012 Bankit Tech, All rights reserved.
 */
package both.db.util;

import java.lang.annotation.IncompleteAnnotationException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import both.annotation.Column;
import both.annotation.Table;
import both.common.util.LoggerUtil;
import both.db.DbParam;
import both.db.DbParam.DbSqlType;
import both.db.constant.IDBErrorMessage;

/**
 * 数据库实体工具类
 * 
 * @author 朱劼晨
 * @date 2012-11-01
 * 
 * @history
 * 
 *          2012-11-23 董国伟 代码重构
 */
public class DbEntityUtil {
	/**
	 * 获取对象类型
	 * 
	 * @param sender
	 *            对象实例
	 * @return 对象类型
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getSenderClass(T sender) {
		return ((Class<T>) sender.getClass());
	}

	/**
	 * 获取对象映射数据库表名
	 * 
	 * @param sender
	 *            对象实例
	 * @return 数据库表名
	 */
	public static <T> String getTableName(T sender) {
		AnnotatedElement annElement = sender.getClass();
		Table table = annElement.getAnnotation(Table.class);
		if (table != null)
			return ("").equals(table.Name()) ? getSenderClass(sender).getName()
					: table.Name();
		else
			throw new IncompleteAnnotationException(Table.class, "Name");
	}

	/**
	 * 获取字段对应数据库列名
	 * 
	 * @param field
	 *            字段
	 * @return 数据库列名
	 */
	public static <T> String getColunmName(Field field) {
		AnnotatedElement annElement = field;
		Column column = annElement.getAnnotation(Column.class);
		if (column != null)
			return ("").equals(column.Name()) ? field.getName() : column.Name();
		else
			return null;
	}

	/**
	 * 生成对象参数字典
	 * 
	 * @param sender
	 *            对象实例
	 * @return 对象参数字典
	 */
	public static <T> Map<String, Object> getParaMap(T sender) {
		HashMap<String, Object> paraMap = new HashMap<String, Object>();
		Field[] fields = getSenderClass(sender).getDeclaredFields();
		// 遍历字段类型
		for (Field field : fields) {
			AnnotatedElement annElement = field;
			Column column = annElement.getAnnotation(Column.class);
			// 对象实例字段注解非空处理
			if (column != null) {
				String colName = ("").equals(column.Name()) ? field.getName()
						: column.Name();
				Object colValue = getSenderValue(sender, field);
				if (colValue instanceof Boolean) {
					if ((Boolean) colValue) {
						colValue = 1;
					} else {
						colValue = 0;
					}
				}
				paraMap.put(colName, colValue);
			}
		}
		return paraMap;
	}

	/**
	 * 获取对象字段的类型字典
	 * 
	 * @param sender
	 *            对象实例
	 * @return 对象字段的类型字典
	 */
	public static <T> Map<String, Class<?>> getRequiredTypeMap(T sender) {
		HashMap<String, Class<?>> requiredTypeMap = new HashMap<String, Class<?>>();
		Field[] fields = getSenderClass(sender).getDeclaredFields();
		// 遍历字段类型
		for (Field field : fields) {
			AnnotatedElement annElement = field;
			Column column = annElement.getAnnotation(Column.class);
			// 对象实例字段注解非空处理
			if (column != null) {
				String colName = ("").equals(column.Name()) ? field.getName()
						: column.Name();
				Class<?> clz = field.getType();
				requiredTypeMap.put(colName, clz);
			}
		}
		return requiredTypeMap;
	}

	/**
	 * 获取对象属性值
	 * 
	 * @param sender
	 *            对象实例
	 * @param field
	 *            字段
	 * @return 对象属性值
	 */
	public static <T> Object getSenderValue(T sender, Field field) {
		field.setAccessible(true);
		Object result = null;
		try {
			result = field.get(sender);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return result;
	}

	/**
	 * 设置对象属性值
	 * 
	 * @param sender
	 *            对象实例
	 * @param field
	 *            对象字段
	 * @param value
	 *            字段属性值
	 */
	public static <T> void setSenderValue(T sender, Field field, Object value) {
		field.setAccessible(true);

		if (value != null) {
			Type type = field.getType();
			// 转换为String类型
			if (type == java.lang.String.class) {
				if (!(value instanceof String)) {
					value = value.toString();
				}
			}
			// 转换为boolean类型
			else if (type == java.lang.Boolean.class || type == boolean.class) {
				if (!(value instanceof Boolean)) {
					value = "0".equals(value.toString()) ? false : true;
				}
			}
			// 转换为Integer类型
			else if (type == java.lang.Integer.class || type == int.class) {
				if (!(value instanceof Integer)) {
					value = Integer.parseInt(value.toString());
				}
			}
			// 转换为Short类型
			else if (type == java.lang.Short.class || type == short.class) {
				if (!(value instanceof Short)) {
					value = Short.parseShort(value.toString());
				}
			}
			// 转换为BigDecimal类型
			else if (type == java.math.BigDecimal.class) {
				if (!(value instanceof BigDecimal)) {
					value = BigDecimal.valueOf(Double.parseDouble(value
							.toString()));
				}
			}
		}
		try {
			field.set(sender, value);
		} catch (Exception e) {
			LoggerUtil.error(IDBErrorMessage.交易处理异常, e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取对象类型集合
	 * 
	 * @param resultList
	 *            查询结果对象集合
	 * @param cls
	 *            对象类型
	 * @return 对象类型集合
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static <T> List<T> getEntityList(
			List<Map<String, Object>> resultList, Class<T> cls)
			throws InstantiationException, IllegalAccessException {
		List<T> senderList = new ArrayList<T>();
		if (resultList == null || resultList.size() == 0)
			return senderList;
		Field[] fields = cls.getDeclaredFields();
		// 遍历数据，构建泛型对象数据
		for (Map<String, Object> result : resultList) {
			T resultSender = cls.newInstance();
			// 列名遍历
			for (String columnName : result.keySet()) {
				for (Field field : fields) {
					String annColName = getColunmName(field);
					if (annColName == null)
						continue;
					if (columnName.equalsIgnoreCase(annColName))
						setSenderValue(resultSender, field,
								result.get(columnName));
				}
			}
			senderList.add(resultSender);
		}
		return senderList;
	}

	/**
	 * 获取对象类型的主键字段
	 * 
	 * @param cls
	 *            对象类型
	 * @return 主键字段
	 */
	public static <T> Field getPrimaryField(Class<T> cls) {
		Field[] fields = cls.getDeclaredFields();
		// 遍历所有申明的字段
		for (Field field : fields) {
			AnnotatedElement annElement = field;
			Column column = annElement.getAnnotation(Column.class);
			if (column != null) {
				if (column.isPrimary())
					return field;
			}
		}
		return null;
	}

	/**
	 * 转化成只含主键类的实体类实例
	 * 
	 * @param sender
	 *            实体类实例
	 * @return 只含主键类的实体类实例
	 * @throws Exception
	 */
	public static <T> T getSenderOnlyWithPrimary(T sender) throws Exception {
		try {
			Class<T> cls = DbEntityUtil.getSenderClass(sender);
			T primarySender = cls.newInstance();
			Field primaryField = DbEntityUtil.getPrimaryField(cls);
			// 主键字段处理
			if (primaryField != null) {
				setSenderValue(primarySender, primaryField,
						getSenderValue(sender, primaryField));
				return primarySender;
			}
			throw new Exception("未找到主键注解的实体类");
		} catch (Exception e) {

			LoggerUtil.error(IDBErrorMessage.交易处理异常, e);
			throw new Exception(IDBErrorMessage.交易处理异常, e);
		}
	}

	/**
	 * 构造DbParam对象
	 * 
	 * @param colSender
	 *            字段对象集合
	 * @param valSender
	 *            字段值对象集合
	 * @param sqlType
	 *            数据操作类型
	 * @return
	 */
	public static <T> DbParam getDbParam(T colSender, T valSender,
			DbSqlType sqlType) {
		DbParam dbParam = new DbParam();
		dbParam.setTableName(getTableName(colSender));
		switch (sqlType) {
		case Insert:
			dbParam.setValueMap(getParaMap(colSender));
			break;
		case Delete:
			dbParam.setWhereMap(getParaMap(colSender));
			break;
		case Update:
			dbParam.setSetMap(getParaMap(valSender));
			dbParam.setWhereMap(getParaMap(colSender));
			break;
		case Query:
			dbParam.setWhereMap(getParaMap(colSender));
			dbParam.setRequiredTypeMap(getRequiredTypeMap(colSender));
			break;
		}
		Field[] fields = colSender.getClass().getDeclaredFields();
		HashMap<String, Object> map = new HashMap<String, Object>();
		// 遍历填充列字段集合
		for (Field field : fields) {
			String annColName = getColunmName(field);
			map.put(annColName, annColName);
		}
		dbParam.setColumnMap(map);
		return dbParam;
	}

	/**
	 * 校正属性值
	 * 
	 * @param value
	 *            初始属性值
	 * @param defaultValue
	 *            默认值
	 * @return 校正属性值
	 */
	public static <T> T checkPropertyValue(T value, T defaultValue) {
		if (value == null)
			return defaultValue;
		else
			return value;
	}

	/**
	 * 把map转成对象
	 * 
	 * @param result
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public static <T> T map2Object(Map<String, Object> result, Class<T> cls)
			throws Exception {

		Field[] fields = ((Class<T>) cls).getDeclaredFields();
		T resultSender = cls.newInstance();
		// 列名遍历
		for (String columnName : result.keySet()) {
			for (Field field : fields) {
				String annColName = getColunmName(field);
				if (annColName == null)
					continue;
				if (columnName.equalsIgnoreCase(annColName))
					setSenderValue(resultSender, field, result.get(columnName));
			}
		}
		return resultSender;
	}

	/**
	 * 获取对象类型集合
	 * 
	 * @param resultList
	 * @param cls
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static <T> List<Map<String, Object>> EntityList(List<T> resultList,
			Class<T> cls) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Iterator iterator = resultList.iterator(); iterator.hasNext();) {
			T t = (T) iterator.next();
			HashMap<String, Object> paraMap = new HashMap<String, Object>();
			Field[] fields = getSenderClass(t).getDeclaredFields();
			for (Field field : fields) {
				AnnotatedElement annElement = field;
				Column column = annElement.getAnnotation(Column.class);
				if (column != null) {
					String colName = ("").equals(column.Name()) ? field
							.getName() : column.Name();
					Object colValue = getSenderValue(t, field);
					paraMap.put(colName.toUpperCase(), colValue);
				}
			}
			list.add(paraMap);
		}
		// List<Map<String, Object>> resultList
		return list;
	}

	/**
	 * 获取对象类型集合
	 * 
	 * @param resultList
	 * @param cls
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static <T> JSONArray entity2JSONArray(List<T> resultList,
			Class<T> cls) {
		JSONArray jsonArray = new JSONArray();
		for (Iterator<T> iterator = resultList.iterator(); iterator.hasNext();) {
			T t = (T) iterator.next();
			JSONObject paraMap = new JSONObject();
			Field[] fields = getSenderClass(t).getDeclaredFields();
			for (Field field : fields) {
				AnnotatedElement annElement = field;
				Column column = annElement.getAnnotation(Column.class);
				if (column != null) {
					String colName = ("").equals(column.Name()) ? field
							.getName() : column.Name();
					Object colValue = getSenderValue(t, field);
					paraMap.put(colName.toUpperCase(), colValue);
				}
			}
			jsonArray.add(paraMap);
		}
		// List<Map<String, Object>> resultList
		return jsonArray;
	}

	/**
	 * 获取对象
	 * 
	 * @param result
	 *            数据
	 * @param cls
	 *            对象实例类型
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static <T> T getEntity(Map<String, Object> result, Class<T> cls)
			throws InstantiationException, IllegalAccessException {
		T resultSender = cls.newInstance();
		Field[] fields = cls.getDeclaredFields();
		for (String columnName : result.keySet()) {
			for (Field field : fields) {
				String annColName = getColunmName(field);
				if (annColName == null)
					continue;
				if (columnName.equalsIgnoreCase(annColName))
					setSenderValue(resultSender, field, result.get(columnName));
			}
		}
		return resultSender;
	}
}
