package validation_lib;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.com.bankit.phoenix.commons.datatype.TextBoolean;

/**
 * 移动电话号码校验器
 * 
 * @author guohy
 * @date 2012-5-10
 */
public class MobilePhoneNoValidation {

	public MobilePhoneNoValidation() {
	}

	/**
	 * @title 移动电话号码校验
	 * @description 如果为空不校验，不为空校验手机号码格式
	 * @param String
	 * @return
	 */
	public static TextBoolean verifyMobilePhoneNo(String phoneNo) {
		TextBoolean result = new TextBoolean();
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(phoneNo);
		if (!phoneNo.isEmpty() && !m.matches()) {
			result.value = false;
			result.text = "手机号码格式错误";
		} else {
			result.value = true;
		}
		return result;
	}
	
	public static void main(String[] args) {
		//手机号码验证通过
		System.out.println(MobilePhoneNoValidation.verifyMobilePhoneNo("18036672086").value);
		//不是手机号码验证不通过
		System.out.println(MobilePhoneNoValidation.verifyMobilePhoneNo("11136672086").value);
		//为空不校验,通过
		System.out.println(MobilePhoneNoValidation.verifyMobilePhoneNo("").value);
	}

}
