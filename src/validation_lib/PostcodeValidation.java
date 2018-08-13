package validation_lib;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.com.bankit.phoenix.commons.datatype.TextBoolean;

/**
 * 邮编校验器
 * 
 * @author guohy
 * @date 2012-5-11
 */
public class PostcodeValidation {

	public PostcodeValidation() {
	}

	/**
	 * @title 邮编校验
	 * @description 邮编由6位数字组成
	 * @param String
	 * @return
	 */
	public static TextBoolean verifyPostcode(String postcode) {
		TextBoolean result = new TextBoolean();
		if (postcode.isEmpty()){
			result.value = true;
			return result;
		}
		Pattern pattern = Pattern.compile("[1-9][0-9]{5}");
		Matcher isPostcode = pattern.matcher(postcode);
        if (isPostcode.matches()){
        	result.value = true;
        }else{
        	result.value = false;
        	result.text = "邮编由6位数字组成";
        }
        return result;
	}
	
	public static void main(String[] args) {
		//正确的邮编号码,通过
		System.out.println(PostcodeValidation.verifyPostcode("610000").value);
		//错误的邮编号码,不通过
		System.out.println(PostcodeValidation.verifyPostcode("010000").value);
		//为空不校验,通过
		System.out.println(PostcodeValidation.verifyPostcode("").value);
	}
}
