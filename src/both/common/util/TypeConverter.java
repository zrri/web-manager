package both.common.util;

import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;

/**
 * 类型转换器
 * 
 * @author 江成
 *
 */
public class TypeConverter {
	
	/**
	 * 日期格式
	 */
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd");
	
	/**
	 * 数组开始标志
	 */
	private static char ARRAY_START = '[';

	/**
	 * 数组结束标志
	 */
	private static char ARRAY_END = ']';

	/**
	 * 数组分割标志
	 */
	private static String ARRAY_SPLITOR = ",";

	/**
	 * 转换为对象
	 * 
	 * @param param
	 * @param clazz
	 * 
	 */
	public static Object convert(Object src, Class<?> clazz) throws Exception {
		// 入参为NULL，直接返回
		if (src == null) {
			return null;
		}

		// 获取
		Class<?> srcClazz = src.getClass();

		// 如果目标class和源class一致，直接返回对象
		if (clazz == srcClazz) {
			return src;
		}

		Object res = null;
		// 根据类型转换数据
		if (byte.class == clazz || Byte.class == clazz) {
			if (String.class == srcClazz) {
				res = Byte.valueOf((String) src);
			} else {
				res = (Byte) res;
			}
		} else if (short.class == clazz || Short.class == clazz) {
			if (String.class == srcClazz) {
				res = Short.valueOf((String) src);
			} else {
				res = (Short) res;
			}
		} else if (int.class == clazz || Integer.class == clazz) {
			if (String.class == srcClazz) {
				res = Integer.valueOf((String) src);
			} else {
				res = (Integer) res;
			}
		} else if (long.class == clazz || Long.class == clazz) {
			if (String.class == srcClazz) {
				res = Long.valueOf((String) src);
			} else {
				res = (Long) res;
			}
		} else if (float.class == clazz || Float.class == clazz) {
			if (String.class == srcClazz) {
				res = Float.valueOf((String) src);
			} else {
				res = (Float) res;
			}
		} else if (double.class == clazz || Double.class == clazz) {
			if (String.class == srcClazz) {
				res = Double.valueOf((String) src);
			} else {
				res = (Double) res;
			}
		} else if (char.class == clazz || Character.class == clazz) {
			if (String.class == srcClazz) {
				res = Double.valueOf((String) src);
			} else {
				res = (Double) res;
			}
		} else if (boolean.class == clazz || Boolean.class == clazz) {
			if (String.class == srcClazz) {
				res = Boolean.valueOf((String) src);
			} else {
				res = (Boolean) res;
			}
		} else if (CharSequence.class.isAssignableFrom(clazz)) {
			res = src.toString();
		} else if (java.util.Date.class.isAssignableFrom(clazz)) {
			if (String.class == srcClazz) {
				try {
					long time = Long.valueOf((String) src);
					Constructor<?> constructor = clazz
							.getConstructor(long.class);
					res = constructor.newInstance(time);
				} catch (Exception e) {
					res = dateFormat.parse((String) src);
				}
			} else {
				res = (java.util.Date) src;
			}
		} else if (clazz.isArray()) {
			String s = src.toString();
			int startIndex = s.indexOf(ARRAY_START);
			int endIndex = s.indexOf(ARRAY_END);
			String content = s.substring(startIndex + 1, endIndex);
			String[] array = content.split(ARRAY_SPLITOR);
			// 获取数组类型
			Class<?> componentType = clazz.getComponentType();

			int size = array.length;
			Object[] resArray = (Object[]) java.lang.reflect.Array.newInstance(
					componentType, size);
			for (int i = 0; i < size; i++) {
				resArray[i] = convert(array[i], componentType);
			}
			res = resArray;
		}
		return res;
	}

}
