package both.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	/**
	 * 柜员密码PIN MD5加密
	 * 
	 * @param gyh
	 *            柜员号
	 * @param pin
	 *            柜员密码
	 * @return MD5加密串
	 * @throws NoSuchAlgorithmException
	 */
	public static String md5s(String gyh, String pin)
			throws NoSuchAlgorithmException {
		// gyh="0200";//暂时写死先
		String plainText = gyh + pin;
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(plainText.getBytes());
		byte b[] = md.digest();
		int i;
//		System.out.println(new String(b));
		StringBuffer buf = new StringBuffer("");
		for (int offset = 0; offset < b.length; offset++) {
			i = b[offset];
			if (i < 0)
				i += 256;
			if (i < 16)
				buf.append("0");
			buf.append(Integer.toHexString(i));
		}
		System.out.println("result: " + buf.toString().toUpperCase());
		return buf.toString().toUpperCase();
		// 32位的加密
	}

	public static void main(String agrs[]) {
		try {
			MD5Util.md5s("21169", "888888");
			//84FF5F536A53ADB641014C78363ADEFA
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 加密4 }}
	}
}