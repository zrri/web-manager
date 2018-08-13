package validation_lib;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import cn.com.bankit.phoenix.commons.datatype.TextBoolean;

/**
 * 时间校验器
 * @author guohy
 * @date 2012-5-11
 */
public class TimeValidation {

	/**
	 * 解析器缓存
	 */
	private static Map<String, SimpleDateFormat> formats = new HashMap<String, SimpleDateFormat>();

	public TimeValidation() {
	}

	/**
	 * 日期校验
	 * @param inputString
	 * @param pattern
	 * @return
	 */
	private static TextBoolean verifyTime(String inputString, String pattern) {
		TextBoolean result = new TextBoolean();
		if(inputString.isEmpty()){
			result.value=true;
			return result;
		}
		SimpleDateFormat format = getSimpleDateFormat(pattern);

		try {
			format.parse(inputString);
			result.value = true;
		} catch (ParseException e) {
			result.value = false;
			result.text = "传入的时间格式不为"+pattern;
		}
		return result;
	}

	/**
	 * 取得解析器
	 * @param pattern
	 * @return 解析器
	 */
	private static SimpleDateFormat getSimpleDateFormat(String pattern) {
		SimpleDateFormat format = null;
		if (formats.get(pattern) == null) {
			format = new SimpleDateFormat(pattern);
			//解析过程是严格的
			format.setLenient(false);
			formats.put(pattern, format);
		} else {
			format = formats.get(pattern);
		}
		return format;
	}

	/**
	 * @title 4位时间校验.格式为HHmm
	 * @description 校验4位时间(二十四制),格式为HHmm
	 * @param String
	 * @return
	 */
	public static TextBoolean verifyTime4Digit(String inputString) {
		return verifyTime(inputString,"HHmm");
	}
	
	/**
	 * @title 6位时间校验,格式为HHmmss
	 * @description 校验6位时间(二十四制),格式为HHmmss
	 * @param String
	 * @return
	 */
	public static TextBoolean verifyTime6Digit(String inputString) {
		return verifyTime(inputString,"HHmmss");
	}
	
	/**
	 * @title 日期校验,格式为yyyyMMdd
	 * @description 校验日期 格式为yyyyMMdd
	 * @param String
	 * @return
	 */
	public static TextBoolean verifyDate(String inputString) {
		return verifyTime(inputString,"yyyyMMdd");
	}
	
	public static void main(String[] args) {
		//调用4位时间验证,输入格式为HHmm
		//输入13点24分,通过
		System.out.println(TimeValidation.verifyTime4Digit("1324").value);
		//输入25点12分,不通过
		System.out.println(TimeValidation.verifyTime4Digit("2512").value);
		//为空不校验,通过
		System.out.println(TimeValidation.verifyTime4Digit("").value);
		
		//调用6位时间校验,格式为HHmmss
		//输入13点01分01秒,通过
		System.out.println(TimeValidation.verifyTime6Digit("130101").value);
		//输入13点61分01秒,不通过
		System.out.println(TimeValidation.verifyTime6Digit("136101").value);
		//为空不校验,通过
		System.out.println(TimeValidation.verifyTime6Digit("").value);
		
		//调用日期校验,输入格式为yyyyMMdd
		//输入2011年1月1日,通过
		System.out.println(TimeValidation.verifyTime4Digit("20110101").value);
		//输入2011年2月30日,不通过
		System.out.println(TimeValidation.verifyTime4Digit("20110230").value);
		//为空不校验,通过
		System.out.println(TimeValidation.verifyTime4Digit("").value);
		
	}

}
