package both.common.util;

/**
 * 格式化工具
 * 
 * @author 江成
 * 
 */
public class FormatUtil {

	/**
	 * 格式化路径
	 * 
	 * @param path
	 * @return
	 */
	public static String formatPath(String path) {
		if (path == null || path.length() == 0) {
			return path;
		}
		//去掉空格
		path=path.trim();
        //替换分割符
		path=path.replace("\\", "/");
		return path;
	}
}
