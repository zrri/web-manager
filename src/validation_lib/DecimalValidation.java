package validation_lib;

import java.math.BigDecimal;

import cn.com.bankit.phoenix.commons.datatype.TextBoolean;

/**
 * 金额校验器
 * @author guohy
 * @date 2012-5-11
 */
public class DecimalValidation {

	public DecimalValidation() {
	}

	/**
	 * @title 金额校验
	 * @description 校验输入的是否为金额
	 * @param String
	 * @return 通过为true,不通过为false
	 */
	public static TextBoolean verifyMoney(String money) {
		TextBoolean result = new TextBoolean();
		if(money.isEmpty()){
			result.value = true;
			return result;
		}
		try {
			new BigDecimal(money);
			result.value = true;
		} catch (Exception e) {
			result.value = false;
			result.text = "输入金额错误";
		}
		return result;
	}
	

	public static void main(String[] args) {
		//输入正确,通过
		System.out.println(DecimalValidation.verifyMoney("5.232").value);
		//输入不正确,不通过
		System.out.println(DecimalValidation.verifyMoney("5.232.23").value);
		//输入不正确,不通过
		System.out.println(DecimalValidation.verifyMoney("5.232x").value);
	}
}
