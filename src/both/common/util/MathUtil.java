package both.common.util;

import java.math.BigDecimal;

public class MathUtil {

	/**
	 * 
	 * 函数名称：加法
	 * <p>
	 * 函数功能：加法
	 * 
	 * @param decimalLiteral1
	 *            被加数
	 * @param decimalLiteral2
	 *            加数
	 * @return decimalLiteral1＋decimalLiteral2
	 *         <p>
	 *         编写时间：2010-8-26 下午07:39:15 <br>
	 *         修改人： (函数的修改者) <br>
	 *         修改时间：(函数的修改时间，与上面的修改人相对应。) <br>
	 *         函数备注：
	 */
	public static String addBigDecimal(String decimalLiteral1,
			String decimalLiteral2) {
		return getDecimal(decimalLiteral1).add(getDecimal(decimalLiteral2))
				.toPlainString();
	}

	/**
	 * 
	 * 函数名称：比较两个数字的大小
	 * <p>
	 * 函数功能：比较两个数字的大小，相等返回0，前者大于后者返回1，若小于返回-1
	 * 
	 * @param decimalLiteral1
	 *            String
	 * @param decimalLiteral2
	 *            String
	 * @return int
	 *         <p>
	 *         编写时间：2010-8-26 下午07:39:15 <br>
	 *         修改人： (函数的修改者) <br>
	 *         修改时间：(函数的修改时间，与上面的修改人相对应。) <br>
	 *         函数备注：
	 */
	public static int compare(String decimalLiteral1, String decimalLiteral2) {
		return getDecimal(decimalLiteral1).compareTo(
				getDecimal(decimalLiteral2));
	}

	/**
	 * 
	 * 函数名称：除法
	 * <p>
	 * 函数功能：除法. 小数点位数缺省2位
	 * 
	 * @param decimalLiteral1
	 *            被除数
	 * @param decimalLiteral2
	 *            除数
	 * @return decimalLiteral1/decimalLiteral2
	 *         <p>
	 *         编写时间：2010-8-26 下午07:39:15 <br>
	 *         修改人： (函数的修改者) <br>
	 *         修改时间：(函数的修改时间，与上面的修改人相对应。) <br>
	 *         函数备注：
	 */
	public static String divideBigDecimal(String decimalLiteral1,
			String decimalLiteral2) {
		return divideBigDecimal(decimalLiteral1, decimalLiteral2, 2);
	}

	/**
	 * 
	 * 函数名称：除法
	 * <p>
	 * 函数功能：除法
	 * 
	 * @param decimalLiteral1
	 *            被除数
	 * @param decimalLiteral2
	 *            除数
	 * @return decimalLiteral1/decimalLiteral2
	 *         <p>
	 *         编写时间：2010-8-26 下午07:39:15 <br>
	 *         修改人： (函数的修改者) <br>
	 *         修改时间：(函数的修改时间，与上面的修改人相对应。) <br>
	 *         函数备注：
	 */
	public static String divideBigDecimal(String decimalLiteral1,
			String decimalLiteral2, int 小数点位数) {
		return getDecimal(decimalLiteral1).divide(getDecimal(decimalLiteral2),
				小数点位数, BigDecimal.ROUND_HALF_UP).toPlainString();
	}

	/**
	 * 
	 * 函数名称：除法
	 * <p>
	 * 函数功能：除法. 小数点位数缺省2位,不做四舍五入
	 * 
	 * @param decimalLiteral1
	 *            被除数
	 * @param decimalLiteral2
	 *            除数
	 * @return decimalLiteral1/decimalLiteral2
	 *         <p>
	 *         编写时间：2010-8-26 下午07:39:15 <br>
	 *         修改人： (函数的修改者) <br>
	 *         修改时间：(函数的修改时间，与上面的修改人相对应。) <br>
	 *         函数备注：
	 */
	public static String divideBigDecimalN(String decimalLiteral1,
			String decimalLiteral2) {
		return divideBigDecimalN(decimalLiteral1, decimalLiteral2, 2);
	}

	/**
	 * 
	 * 函数名称：除法
	 * <p>
	 * 函数功能：除法,不做四舍五入
	 * 
	 * @param decimalLiteral1
	 *            被除数
	 * @param decimalLiteral2
	 *            除数
	 * @return decimalLiteral1/decimalLiteral2
	 *         <p>
	 *         编写时间：2010-8-26 下午07:39:15 <br>
	 *         修改人： (函数的修改者) <br>
	 *         修改时间：(函数的修改时间，与上面的修改人相对应。) <br>
	 *         函数备注：
	 */
	public static String divideBigDecimalN(String decimalLiteral1,
			String decimalLiteral2, int 小数点位数) {
		return getDecimal(decimalLiteral1).divide(getDecimal(decimalLiteral2),
				小数点位数, BigDecimal.ROUND_DOWN).toPlainString();
	}


	public static BigDecimal getDecimal(String literal) {
		if (literal == null || literal.length() == 0) {
			return new BigDecimal("0");// 改成"0"，为了兼容Java 1.4
		}
		if (literal.charAt(0) == '.') {
			return new BigDecimal("0" + literal);
		}
		return new BigDecimal(literal);
	}

	/**
	 * 
	 * 函数名称：乘法
	 * <p>
	 * 函数功能：乘法, 保留小数点为两个参数保留的位数相加
	 * 
	 * @param decimalLiteral1
	 *            被乘数
	 * @param decimalLiteral2
	 *            乘数
	 * @return decimalLiteral1×decimalLiteral2
	 *         <p>
	 *         编写时间：2010-8-26 下午07:39:15 <br>
	 *         修改人： (函数的修改者) <br>
	 *         修改时间：(函数的修改时间，与上面的修改人相对应。) <br>
	 *         函数备注：
	 */
	public static String multiplyBigDecimal(String decimalLiteral1,
			String decimalLiteral2) {
		return getDecimal(decimalLiteral1)
				.multiply(getDecimal(decimalLiteral2)).toPlainString();
	}

	/**
	 * 
	 * 函数名称：乘法
	 * <p>
	 * 函数功能：乘法, 保留小数点为两个参数保留的位数相加
	 * 
	 * @param decimalLiteral1
	 *            被乘数
	 * @param decimalLiteral2
	 *            乘数
	 * @param 小数位数
	 * @return decimalLiteral1×decimalLiteral2
	 *         <p>
	 *         编写时间：2010-8-26 下午07:39:15 <br>
	 *         修改人： (函数的修改者) <br>
	 *         修改时间：(函数的修改时间，与上面的修改人相对应。) <br>
	 *         函数备注：
	 */
	public static String multiplyBigDecimal(String decimalLiteral1,
			String decimalLiteral2, int 小数位数) {
		BigDecimal bd = getDecimal(decimalLiteral1).multiply(
				getDecimal(decimalLiteral2));
		bd = bd.setScale(小数位数, BigDecimal.ROUND_HALF_UP);
		return bd.toPlainString();
	}

	public static String setScale(String decimalLiteral, int 小数位数) {
		BigDecimal d = getDecimal(decimalLiteral);
		d = d.setScale(小数位数, BigDecimal.ROUND_HALF_UP);
		return d.toPlainString();
	}

	/**
	 * 
	 * 函数名称： 减法
	 * <p>
	 * 函数功能： 减法
	 * 
	 * @param decimalLiteral1
	 *            被减数
	 * @param decimalLiteral2
	 *            减数
	 * @return decimalLiteral1- decimalLiteral2
	 *         <p>
	 *         编写时间：2010-8-26 下午07:39:15 <br>
	 *         修改人： (函数的修改者) <br>
	 *         修改时间：(函数的修改时间，与上面的修改人相对应。) <br>
	 *         函数备注：
	 */
	public static String subtractBigDecimal(String decimalLiteral1,
			String decimalLiteral2) {
		return getDecimal(decimalLiteral1)
				.subtract(getDecimal(decimalLiteral2)).toPlainString();
	}

	/**
	 * 
	 * 函数名称：求余
	 * <p>
	 * 函数功能： 求余
	 * 
	 * @param decimalLiteral1
	 *            被余数
	 * @param decimalLiteral2
	 *            余数(要求不为0)
	 * @return decimalLiteral1 % decimalLiteral2
	 *         <p>
	 *         编写时间：2010-8-26 下午07:39:15 <br>
	 *         修改人： (函数的修改者) <br>
	 *         修改时间：(函数的修改时间，与上面的修改人相对应。) <br>
	 *         函数备注：
	 */
	public static String remainderBigDecimal(String decimalLiteral1,
			String decimalLiteral2) throws Exception {
		return getDecimal(decimalLiteral1).remainder(
				getDecimal(decimalLiteral2)).toPlainString();
	}

}
