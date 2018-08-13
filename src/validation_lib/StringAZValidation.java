package validation_lib;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.com.bankit.phoenix.commons.datatype.TextBoolean;

/**
 * 英文名字校验器
 * @author guohy
 * @date 2012-5-11
 */
public class StringAZValidation {

	public StringAZValidation() {
	}

	/**
	 * @title 英文名字大写校验
	 * @description 除首字符以外可以带空格，长度必须小于20，符合如：“ZH ANG”
	 * @param String
	 * @return
	 */
	public static TextBoolean verifyEnglishName(String englishName) {
		TextBoolean result = new TextBoolean();
		if (englishName.isEmpty()){
			result.value = true;
			return result;
		}
		Pattern pattern = Pattern.compile("[A-Z ]*");
		Matcher isEnglishName = pattern.matcher(englishName);
        if (!isEnglishName.matches()){
        	result.value = false;
        	result.text = "英文名字应由大写字母和空格组成";
        }else if(englishName.charAt(0)==' '){
        	result.value = false;
        	result.text = "首字符不能为空格";
        }else if(englishName.length()>20){
        	result.value = false;
        	result.text = "英文名字长度必须小于20位";
        }else{
        	result.value = true;
        }
		return result;
	}
	
	public static void main(String[] args) {
		//输入正确的大写英文名,通过
		System.out.println(StringAZValidation.verifyEnglishName("ZH ANG").value);
		//输入大小写混合英文名,不通过
		System.out.println(StringAZValidation.verifyEnglishName("ZH ang").value);
		//为空不校验,通过
		System.out.println(StringAZValidation.verifyEnglishName("").value);
	}

}
