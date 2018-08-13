package both.common.util;

import cn.com.bankit.phoenix.resource.ResourceManager;

/**
 * 文件系统工具类
 * 
 * @author 江成
 * 
 */
public class FileSystemUtil {

	/**
	 * workspace root
	 */
	private static String workspaceRoot;

	/**
	 * 程序的安装目录
	 */
	private static String installRoot;

	/**
	 * 配置目录
	 */
	private static String configurationRoot;

	/**
	 * 标志是否初始化完成
	 */
	private static boolean initialized = false;

	/**
	 * 初始化
	 */
	private synchronized static void init() {
		// 如果已经初始化完成，不再重复处理
		if (initialized) {
			return;
		}
		// =获取resource manager
		ResourceManager resourceManager = ResourceManager.getInstance();
		// 获取workspace root
		workspaceRoot = resourceManager.getWorkspaceRoot();
		//获取配置目录
		configurationRoot = resourceManager.getConfigurationRoot();
		// 获取程序安装目录
		installRoot = resourceManager.getInstallRoot();
		// 标志初始化完成
		initialized = true;
	}

	/**
	 * 获取configuration的路径
	 * 
	 * @return
	 */
	public static String getConfigurationRoot() {
		// 初始化
		init();
		return configurationRoot;
	}

	/**
	 * 获取workspace的路径
	 * 
	 * @return
	 */
	public static String getWorkspaceRoot() {
		// 初始化
		init();
		return workspaceRoot;
	}

	/**
	 * 获取程序安装目录
	 * 
	 * @return
	 */
	public static String getInstallRoot() {
		// 初始化
		init();
		return installRoot;
	}

}
