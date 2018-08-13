package both.common.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import cn.com.bankit.phoenix.resource.ResourceManager;

/**
 * 获取配置文件内容工具类
 * 
 * @author Administrator
 * 
 */
public class ReadConfigUtil {

	/**
	 * 配置文件映射表
	 */
	public static Map<String, ConfigNode> cacheMap = new HashMap<String, ConfigNode>();

	/**
	 * 加载配置文件内容
	 * 
	 * @param file
	 */
	private synchronized static void initPropertiesMap(File file) {
		// 获取文件名
		String name = file.getAbsolutePath();
		// 判断缓存中是否已经存在配置
		ConfigNode node = cacheMap.get(name);
		if (node != null) {
			// 获取文件最后修改时间
			long lastModified = file.lastModified();
			// 如果文件没有修改，不再处理直接返回
			if (lastModified == node.lastModified) {
				return;
			}
		}

		// 定义输入流
		InputStream in = null;
		try {
			// 定义配置
			node = new ConfigNode();

			// 创建输入流
			in = new BufferedInputStream(new FileInputStream(file));
			Properties prop = new Properties();
			// 加载配置
			prop.load(in);
			// 读取配置
			for (Object key : prop.keySet()) {
				String s = key.toString();
				node.properties.put(s, prop.getProperty(s));
			}
			node.lastModified= file.lastModified();
			cacheMap.put(name, node);
		} catch (FileNotFoundException e) {
			String errorMsg = String.format("配置文件[%s]不存在：%s", file.getName(),
					e.getMessage());
			LoggerUtil.error(errorMsg, e);
		} catch (IOException e) {
			String errorMsg = String.format("配置文件[%s]不存在：%s", file.getName(),
					e.getMessage());
			LoggerUtil.error(errorMsg, e);
		} catch (Exception e) {
			String errorMsg = String.format("记取配置文件[%s]异常：%s", file.getName(),
					e.getMessage());
			LoggerUtil.error(errorMsg, e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					String errorMsg = "配置文件关闭异常：" + e.getMessage();
					LoggerUtil.error(errorMsg, e);
				}
			}
		}
	}

	/**
	 * 获取配置文件的信息
	 * 
	 * @param configFile
	 *            (bips/configuration/或bipc/configuration/目录下的properties文件名称)
	 * @param key
	 * @return
	 */
	public static String getPropertyString(String configFile, String key,
			String defaultValue) {
		String value = getPropertyString(configFile, key);
		if (value == null || "".equals(value))
			return defaultValue;

		return value;
	}

	public static Map<String, String> getProperties(String configFile) {
		// 获取配置目录路径
		String configPath = ResourceManager.getInstance()
				.getConfigurationRoot();
		// 拼装路径
		StringBuilder sb = new StringBuilder();
		sb.append(configPath);
		sb.append("/");
		sb.append(configFile);
		File file = new File(sb.toString());
		// 加载文件
		initPropertiesMap(file);
		// 获取文件名称
		String name = file.getAbsolutePath();

		Map<String, String> properties = new HashMap<String, String>();
		// 获取配置
		ConfigNode node = cacheMap.get(name);
		if (node != null) {
			properties.putAll(node.properties);
		}
		return properties;
	}

	/** 读取指定路径的配置文件 */
	public static Map<String, String> getFileProperties(String configFile) {
		File file = new File(configFile);
		// 加载文件
		initPropertiesMap(file);
		// 获取文件名称
		String name = file.getAbsolutePath();

		Map<String, String> properties = new HashMap<String, String>();
		// 获取配置
		ConfigNode node = cacheMap.get(name);
		if (node != null) {
			properties.putAll(node.properties);
		}
		return properties;
	}

	/**
	 * 获取配置文件的信息
	 * 
	 * @param configFile
	 *            (bips/configuration/或bipc/configuration/目录下的properties文件名称)
	 * @param key
	 * @return
	 */
	public static String getPropertyString(String configFile, String key) {
		// 获取配置目录路径
		String configPath = ResourceManager.getInstance()
				.getConfigurationRoot();
		// 拼装路径
		StringBuilder sb = new StringBuilder();
		sb.append(configPath);
		sb.append("/");
		sb.append(configFile);
		return getPropertyStringByDir(sb.toString(), key);
	}

	/**
	 * 获取配置文件的信息
	 * 
	 * @param configFile
	 *            文件的绝对路径
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getPropertyStringByDir(String configFile, String key,
			String defaultValue) {
		String value = getPropertyStringByDir(configFile, key);
		if (value == null || "".equals(value))
			return defaultValue;
		return value;
	}

	/**
	 * 获取配置文件的信息
	 * 
	 * @param configFile
	 *            文件的绝对路径
	 * @param key
	 * @return
	 */
	public static String getPropertyStringByDir(String configFile, String key) {
		// 获取配置
		Map<String, String> properties = getFileProperties(configFile);

		if (!properties.containsKey(key)) {
			LoggerUtil.warn(String.format("配置文件[%s]中不存在项[%s]的值!", configFile,
					key));
			return null;
		}
		String value = (String) properties.get(key);
		return value;
	}

	/**
	 * 配置信息
	 */
	private static class ConfigNode {

		/**
		 * 记录配置文件最后修改时间
		 */
		public long lastModified = -1;

		/**
		 * 配置信息
		 */
		public Map<String, String> properties = new HashMap<String, String>();
	}

	/**
	 * main
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		File file = new File(
				"E:/BankitWork/dev/BIPC_DEV/workspace/zjrc/resource/config/zjrc.properties");
		initPropertiesMap(file);

	}
}
