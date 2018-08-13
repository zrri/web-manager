package validation_lib;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.com.bankit.phoenix.commons.datatype.TextBoolean;

/**
 * 邮箱地址校验器
 * @author guohy
 * @date 2012-5-10
 */
public class EmailAddressValidation {

	public EmailAddressValidation() {
	}

	/**
	 * @title 邮件地址校验
	 * @description 如果为空不校验,不为空按照邮件格式校验
	 * @param String
	 * @return 合法返回true,不合法返回false
	 */
	public static TextBoolean verifyEMailAddress(String address) {
		TextBoolean result = new TextBoolean();
		// 账号包括字母、数字、下划线，以字母开头，字母或数字结尾
		Pattern bundleArchivePattern = Pattern
				.compile("([A-Z]|[a-z])([A-Z]|[a-z]|[0-9])*@([A-Z]|[a-z]|[0-9])([A-Z]|[a-z]|[0-9]|[.])+([A-Z]|[a-z]|[0-9])");
		Matcher m = bundleArchivePattern.matcher(address);
		if (m.matches()||address.equals("")) {
			result.value = true;
		} else {
			result.value = false;
			result.text = "邮件地址错误";
		}
		return result;
	}
	
	public static void main(String[] args) {
		//输入正确邮箱,通过
		System.out.println(EmailAddressValidation.verifyEMailAddress("bullsasd@qq.com").value);
		//输入错误邮箱,不通过
		System.out.println(EmailAddressValidation.verifyEMailAddress("www.163.com").value);
		//输入空串,通过
		System.out.println(EmailAddressValidation.verifyEMailAddress("").value);
	}

}
