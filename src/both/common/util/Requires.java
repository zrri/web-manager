/**
 * Special Declaration: These technical material reserved as the technical 
 * secrets by Bankit TECHNOLOGY have been protected by the "Copyright Law" 
 * "ordinances on Protection of Computer Software" and other relevant 
 * administrative regulations and international treaties. Without the written 
 * permission of the Company, no person may use (including but not limited to 
 * the illegal copy, distribute, display, image, upload, and download) and 
 * disclose the above technical documents to any third party. Otherwise, any 
 * infringer shall afford the legal liability to the company.
 *
 * 特别声明：本技术材料受《中华人民共和国著作权法》、《计算机软件保护条例》
 * 等法律、法规、行政规章以及有关国际条约的保护，浙江宇信班克信息技术有限公
 * 司享有知识产权、保留一切权利并视其为技术秘密。未经本公司书面许可，任何人
 * 不得擅自（包括但不限于：以非法的方式复制、传播、展示、镜像、上载、下载）使
 * 用，不得向第三方泄露、透露、披露。否则，本公司将依法追究侵权者的法律责任。
 * 特此声明！
 *
 * Copyright(C) 2011 Bankit Tech, All rights reserved.
 *
 */
package both.common.util;

/**
 * 参数检查
 * 
 */
public class Requires {

	/**
	 * 检查非空（null）
	 * 
	 * @param target
	 *            参数对象
	 * @param parameterName
	 *            参数名称
	 */
	public static void notNull(Object target, String parameterName) {
		if (parameterName == null)
			throw new NullPointerException("parameterName");
		if (target == null)
			throw new NullPointerException(parameterName);
	}

	/**
	 * 检查非空（null&empty）
	 * 
	 * @param target
	 *            参数对象
	 * @param parameterName
	 *            参数名称
	 */
	public static void notNullOrEmpty(String target, String parameterName) {
		Requires.notNull(parameterName, "parameterName");

		if (target == null)
			throw new NullPointerException(String.format("%s is Null",
					parameterName));

		if (target.isEmpty())
			throw new NullPointerException(String.format("%s is Empty",
					parameterName));
	}

	/**
	 * 检查非空（null&WhiteSpace）
	 * 
	 * @param target
	 *            参数对象
	 * @param parameterName
	 *            参数名称
	 */
	public static void notNullOrWhiteSpace(String target, String parameterName) {
		Requires.notNull(parameterName, "parameterName");

		if (target == null)
			throw new NullPointerException(String.format("%s is Null",
					parameterName));

		if (target.isEmpty())
			throw new NullPointerException(String.format("%s is Empty",
					parameterName));

		if (target.trim().isEmpty())
			throw new NullPointerException(String.format(
					"%s is all whitespace", parameterName));
	}
}
