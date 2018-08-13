package validation_lib;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.com.bankit.phoenix.commons.datatype.TextBoolean;

/**
 * IP地址合法性校验
 * @author guohy
 * @date 2012-5-10
 */
public class IPAddressValidation {

	public IPAddressValidation() {
	}

	/**
	 * @title IP地址校验
	 * @description IP地址合法检查
	 * @param String
	 * @return 合法返回true,不合法返回false
	 */
	public static TextBoolean verifyIPAddress(String address) {
		TextBoolean result = new TextBoolean();
		if(address.isEmpty()){
			result.value = true;
			return result;
		}
		Pattern bundleArchivePattern = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
		Matcher m = bundleArchivePattern.matcher(address);
		if (!m.matches()) {
			result.value = false;
			result.text = "IP地址错误";
		} else {
			result.value = true;
			String[] s = address.split("[.]");
			for (int i = 0; i < s.length; i++) {
				int t = Integer.parseInt(s[i]);
				if (t > 255) {
					result.value = false;
					result.text = "IP地址错误";
					break;
				}
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		//正确的ip地址,通过
		System.out.println(IPAddressValidation.verifyIPAddress("127.0.0.1").value);
		//不正确的ip地址,不通过
		System.out.println(IPAddressValidation.verifyIPAddress("127.0.256.1").value);
		//输入空串,通过
		System.out.println(IPAddressValidation.verifyIPAddress("").value);
	}

}
