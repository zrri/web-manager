package validation_lib;

import cn.com.bankit.phoenix.commons.datatype.TextBoolean;

/**
 * 字符串长度校验器
 * @author guohy
 * @date 2012-5-14
 */
public class StringLengthValidation {

	public StringLengthValidation() {
	}

	/**
	 * @title 字符串校验-长度最大最小值校验
	 * @description 长度最大最小值校验
	 * @param String
	 * @param Integer
	 * @param Integer
	 * @return
	 */
	public static TextBoolean verifyLength(String inputString,
			Integer minLength, Integer maxLength) {
		TextBoolean result = new TextBoolean();
		int inputLength = inputString.length();
		if (inputLength < minLength) {
			result.text = "输入长度不能小于" + minLength;
			result.value = false;
		} else if (inputLength > maxLength) {
			result.text = "输入长度不能大于" + minLength;
			result.value = false;
		} else {
			result.value = true;
		}

		return result;
	}


	/**
	 * @title 字符串校验-输入长度恒定校验
	 * @description 输入长度恒定校验
	 * @param String
	 * @param Integer
	 * @return
	 */
	public static TextBoolean verifyConstLength(String inputString,
			Integer constLength) {
		TextBoolean result = new TextBoolean();
		int inputLength = inputString.length();
		if (inputLength != constLength) {
			result.text = "输入长度必须等于" + constLength;
			result.value = false;
		} else {
			result.value = true;
		}

		return result;
	}

}
