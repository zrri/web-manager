package validation_lib;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.com.bankit.phoenix.commons.datatype.TextBoolean;

/**
 * 电话号码校验器
 * 
 * @author guohy
 * @date 2012-5-11
 */
public class TellNoValidation {

	public TellNoValidation() {
	}

	/**
	 * @title 电话号码校验
	 * @description 如果为空不校验，不为空校验电话号码格式
	 * @param String
	 * @return
	 */
	public static TextBoolean verifyTellNo(String tellNo) {
		TextBoolean result = new TextBoolean();

		if (tellNo.isEmpty()) {
			result.value = true;
			return result;
		}
		Pattern pattern = Pattern.compile("\\d{3,4}-\\d{7,8}|\\d{7,8}$");
		Matcher isTellNo = pattern.matcher(tellNo);
		if (isTellNo.matches()) {
			result.value = true;
		}else{
			result.value = false;
			result.text = "电话号码格式错误,应为‘区号-号码’或’号码‘";
		}

		return result;
	}
	
	public static void main(String[] args) {
		//输入正确电话号码,'区号-号码',通过
		System.out.println(TellNoValidation.verifyTellNo("028-88888888").value);
		//输入正确电话号码,'号码',通过
		System.out.println(TellNoValidation.verifyTellNo("88888888").value);
		//输入错误的电话号码,不通过
		System.out.println(TellNoValidation.verifyTellNo("888888").value);
		//为空不校验,通过
		System.out.println(TellNoValidation.verifyTellNo("").value);
	}

}
