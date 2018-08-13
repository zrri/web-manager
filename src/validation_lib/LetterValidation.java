package validation_lib;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.com.bankit.phoenix.commons.datatype.TextBoolean;

/**
 * 字母校验器
 * @author guohy
 * @date 2012-5-10
 */
public class LetterValidation {

	public LetterValidation() {
	}

	/**
	 * @title 字母校验
	 * @description 验证输入是否为字母
	 * @param String
	 * @return
	 */
	public static TextBoolean verifyLetter(String letter) {
		TextBoolean result = new TextBoolean();
		if(letter.isEmpty()){
			result.value = true;
			return result;
		}
		
		Pattern pattern = Pattern.compile("[a-zA-Z]*");
		Matcher isLetter = pattern.matcher(letter);
		if (isLetter.matches()) {
			result.value = true;
		} else {
			result.value = false;
			result.text = "只能输入字母";
		}
		return result;
	}
	
	public static void main(String[] args) {
		//输入全为字母的字符串,通过
		System.out.println(LetterValidation.verifyLetter("test").value);
		//输入不全为字母的字符串,不通过
		System.out.println(LetterValidation.verifyLetter("test1").value);
		//为空不校验,通过
		System.out.println(LetterValidation.verifyLetter("").value);
	}

}
