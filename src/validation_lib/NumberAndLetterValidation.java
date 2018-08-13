package validation_lib;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.com.bankit.phoenix.commons.datatype.TextBoolean;

/**
 * 数字字母校验器
 * @author guohy
 * @date 2012-5-11
 */
public class NumberAndLetterValidation {

	public NumberAndLetterValidation() {
	}

	/**
	 * @title 数字字母校验
	 * @description 验证输入是否全为数字字母
	 * @param String
	 * @return 通过返回true,不通过返回false
	 */
	public static TextBoolean verifyNumberAndLetter(String inputString) {
		TextBoolean result = new TextBoolean();
		if(inputString.isEmpty()){
			result.value = true;
			return result;
		}
		
		Pattern pattern = Pattern.compile("[0-9a-zA-Z]*");
		Matcher isNum = pattern.matcher(inputString);
		if (isNum.matches()) {
			result.value = true;
		} else {
			result.value = false;
			result.text = "只能输入数字和字母";
		}
		return result;
	}
	
	public static void main(String[] args) {
		//输入全为字母与数字的字符串,通过
		System.out.println(NumberAndLetterValidation.verifyNumberAndLetter("test1").value);
		//输入不全为字母与数字的字符串,不通过
		System.out.println(NumberAndLetterValidation.verifyNumberAndLetter("test1.3").value);
		//为空不校验,通过
		System.out.println(NumberAndLetterValidation.verifyNumberAndLetter("").value);
	}

}
