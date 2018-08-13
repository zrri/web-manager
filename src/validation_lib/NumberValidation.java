package validation_lib;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.com.bankit.phoenix.commons.datatype.TextBoolean;

/**
 * 数字校验器
 * @author guohy
 * @date 2012-5-10
 */
public class NumberValidation {

	public NumberValidation() {
	}

	/**
	 * @title 数字验证
	 * @description 验证输入是否为数字
	 * @param String
	 * @return 数字返回true,非数字返回false
	 */
	public static TextBoolean verifyNumber(String number) {
		TextBoolean result = new TextBoolean();
		if(number.isEmpty()){
			result.value = true;
			return result;
		}
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(number);
		if (isNum.matches()) {
			result.value = true;
		} else {
			result.value = false;
			result.text = "只能输入数字";
		}
		return result;
	}
	
	public static void main(String[] args) {
		//输入全为数字的字符串,通过
		System.out.println(NumberValidation.verifyNumber("123213").value);
		//输入不全为数字的字符串,不通过
		System.out.println(NumberValidation.verifyNumber("123123f").value);
		//输入小数的字符串,不通过
		System.out.println(NumberValidation.verifyNumber("12.3").value);
		//为空不校验,通过
		System.out.println(NumberValidation.verifyNumber("").value);
	}

}
