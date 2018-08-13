package both.common.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import both.db.util.DbEntityUtil;


import cn.com.bankit.phoenix.context.ContextField;
import cn.com.bankit.phoenix.context.IContext;
import cn.com.bankit.phoenix.context.exception.ContextFieldNotExistException;

/**
 * 集合工具类
 * 
 */
public class CollectionUtil {

	/**
	 * 实体类集合以某一字段为键转化为实体类字典
	 * 
	 * @param senderList
	 *            实体类集合
	 * @param fieldName
	 *            字段名称
	 * @return 实体类字典
	 * @throws Exception
	 */
	public static <T> Map<Object, T> convertList2SingleMap(List<T> senderList,
			String fieldName) throws Exception {
		if (senderList == null)
			return null;
		Map<Object, T> map = new HashMap<Object, T>();
		for (T sender : senderList) {
			Field field = sender.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			map.put(field.get(sender), sender);
		}
		return map;
	}

	/**
	 * 实体类集合以某一字段为键转化为实体类组别字典
	 * 
	 * @param senderList
	 *            实体类集合
	 * @param fieldName
	 *            字段名称
	 * @return 实体类组别字典
	 * @throws Exception
	 */
	public static <T> Map<Object, List<T>> convertList2GroupMap(
			List<T> senderList, String fieldName) throws Exception {
		if (senderList == null)
			return null;
		Map<Object, List<T>> senderMap = new HashMap<Object, List<T>>();
		for (T sender : senderList) {
			Field field = sender.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			Object value = field.get(sender);
			if (!senderMap.keySet().contains(value)) {
				List<T> tempList = new ArrayList<T>();
				tempList.add(sender);
				senderMap.put(value, tempList);
			} else
				senderMap.get(value).add(sender);
		}
		return senderMap;
	}

	/**
	 * 克隆实体类List
	 * 
	 * @param list
	 *            实体类集合
	 * @return 克隆集合
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static <T> List<T> cloneEntityList(List<T> list)
			throws InstantiationException, IllegalAccessException {
		List<T> resultList = new ArrayList<T>();
		Class<T> cls = null;
		if (list.size() == 0)
			return resultList;
		for (T entity : list) {
			if (entity == null) {
				resultList.add(null);
				continue;
			}
			if (cls == null)
				cls = DbEntityUtil.getSenderClass(entity);
			T tempEntity = cls.newInstance();
			for (Field field : cls.getDeclaredFields())
				DbEntityUtil.setSenderValue(tempEntity, field,
						DbEntityUtil.getSenderValue(entity, field));
			resultList.add(tempEntity);
		}
		return resultList;
	}

	/**
	 * 获取重命名字典键值的字典
	 * 
	 * @param senderMap
	 *            原字典
	 * @param ext
	 *            后缀名
	 * @return
	 */
	public static <T> Map<String, Object> getRenameKeyMap(
			Map<String, Object> senderMap, String ext) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (String key : senderMap.keySet())
			map.put(key + ext, senderMap.get(key));
		return map;
	}

	/**
	 * 过滤value为null或者""的map
	 * 
	 * @param map
	 * @return
	 */
	public static Map<String, Object> filterMapValueIsNull(
			Map<String, Object> map) {
		Map<String, Object> filterMap = new HashMap<String, Object>();
		for (String key : map.keySet()) {
			Object value = map.get(key);
			if (value == null)
				continue;
			// if ((value instanceof String) && "".equals(value))
			// continue;
			filterMap.put(key, value);
		}
		return filterMap;
	}

	/**
	 * 将Map<String, Class<?>>转换为Map<String, String>
	 * 
	 * @param classMap
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> getStringMap(
			Map<String, Class<?>> classMap) throws Exception {
		Map<String, String> stringMap = new HashMap<String, String>();
		if (classMap != null && classMap.size() > 0) {
			for (String key : classMap.keySet()) {
				String className = classMap.get(key).toString();
				if (className.startsWith("class ")) {
					stringMap.put(key, className.substring(6));
				} else {
					stringMap.put(key, className);
				}
			}
		}
		return stringMap;
	}

	/**
	 * 将Map<String, Class<?>>转换为Map<String, String>
	 * 
	 * @param stringMap
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Class<?>> getClassMapUpperKey(
			Map<String, String> stringMap) throws Exception {
		Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
		if (stringMap != null && stringMap.size() > 0) {
			for (String key : stringMap.keySet()) {
				String className = stringMap.get(key);
				Class<?> classTemp = null;
				// 特殊处理八种值类型
				if (className.equals(boolean.class.getName())) {
					classTemp = boolean.class;
				} else if (className.equals(int.class.getName())) {
					classTemp = int.class;
				} else if (className.equals(double.class.getName())) {
					classTemp = double.class;
				} else if (className.equals(float.class.getName())) {
					classTemp = float.class;
				} else if (className.equals(byte.class.getName())) {
					classTemp = byte.class;
				} else if (className.equals(short.class.getName())) {
					classTemp = short.class;
				} else if (className.equals(long.class.getName())) {
					classTemp = long.class;
				} else if (className.equals(char.class.getName())) {
					classTemp = char.class;
				} else {
					classTemp = Class.forName(className);
				}
				classMap.put(key.toUpperCase(), classTemp);
			}
		}
		return classMap;
	}

	public interface ISelector<T, RESULT> {
		RESULT select(T source);
	}

	public interface IPredicate<T> {
		boolean is(T source);
	}

	public static <T, RESULT> List<RESULT> select(List<T> source,
			ISelector<T, RESULT> selector) {
		List<RESULT> result = new ArrayList<RESULT>();
		for (T o : source) {
			result.add(selector.select(o));
		}
		return result;
	}

	public static <T> List<T> where(List<T> source, IPredicate<T> predicate) {
		List<T> result = new ArrayList<T>();
		for (T o : source) {
			if (predicate.is(o))
				result.add(o);
		}
		return result;
	}

	public static <T> T first(Collection<T> source) {
		if (source.isEmpty())
			return null;

		return source.iterator().next();
	}

	public static Map<String, Object> convertContext2Map(IContext context)
			throws ContextFieldNotExistException {
		Map<String, Object> retMap = new HashMap<String, Object>();
		Map<String, ContextField> fields = context.getFields();
		for (String field : fields.keySet()) {
			retMap.put(field, context.getFieldValue(field));
		}
		return retMap;
	}
}
