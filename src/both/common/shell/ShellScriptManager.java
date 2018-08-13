package both.common.shell;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import cn.com.bankit.phoenix.resource.ResourceManager;

import both.common.util.LoggerUtil;

/**
 * shell管理器
 * 
 * @author 江成
 * 
 */
public class ShellScriptManager {

	/**
	 * 编码
	 */
	private static String ENCODING = "UTF-8";

	/**
	 * 获取脚本
	 * 
	 * @param os
	 * @param name
	 * @return
	 */
	public static String getScript(String os, String name, String... args) {
		String dir = "scripts/linux/";
		if ("AIX".equalsIgnoreCase(os)) {
			dir = "scripts/aix/";
		}

		StringBuilder sb = new StringBuilder();
		// 输入流
		BufferedReader reader = null;
		try {
			String path =  ResourceManager.getInstance().getProjectName(
							ShellScriptManager.class) + "/resource/" + dir
					+ name;
			LoggerUtil.debug("======>" + path);
			reader = new BufferedReader(new InputStreamReader(ResourceManager
					.getInstance().getResourceStream(path), ENCODING));
			// 读取内容
			String line = null;
			while ((line = reader.readLine()) != null) {
				// 去掉空格
				String item = line.trim();
				if (item.length() > 0) {
					char preChar = item.charAt(0);
					if (preChar == '#') {
						continue;
					}
				}
				sb.append(line);
				sb.append("\n");
			}

		} catch (Exception e) {
			LoggerUtil.error(e.getMessage(), e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {
					// do nothing
				}
			}
		}

		String script = sb.toString();
		if (args != null && args.length > 0) {
			// 替换参数
			for (int i = 0; i < args.length; i++) {
				String matchedMark = "$" + (i + 1);
				int index = script.indexOf(matchedMark);
				if (index != -1) {
					int len = matchedMark.length();
					String prePart = script.substring(0, index);
					String postPart = script.substring(index + len);
					script = prePart + args[i] + postPart;
				}
			}
		}
		return script;
	}

	/**
	 * 获取命令
	 * 
	 * @param os
	 * @param name
	 * @return
	 */
	public static String[] getCommands(String os, String name, String... args) {
		String dir = "scripts/linux/";
		if ("AIX".equalsIgnoreCase(os)) {
			dir = "scripts/aix/";
		}

		StringBuilder sb = new StringBuilder();
		// 输入流
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					ShellScriptManager.class.getClassLoader()
							.getResourceAsStream(dir + name), ENCODING));
			// 读取内容
			String line = null;
			while ((line = reader.readLine()) != null) {
				// 去掉空格
				String item = line.trim();
				if (item.length() > 0) {
					char preChar = item.charAt(0);
					if (preChar == '#') {
						continue;
					}
				}
				sb.append(line);
				sb.append("\n");
			}

		} catch (Exception e) {
			LoggerUtil.error(e.getMessage(), e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {
					// do nothing
				}
			}
		}

		String script = sb.toString();
		if (args != null && args.length > 0) {
			// 替换参数
			for (int i = 0; i < args.length; i++) {
				script = script.replace("$" + (i + 1), args[i]);
			}
		}
		String[] commands = script.split("\n");
		return commands;
	}
}
