package both.common.config;

import cn.com.bankit.phoenix.resource.ResourceManager;


/**
 * 配置获取工具
 * 
 * @author 江成
 * 
 */
public class PreferenceUtil {
	
	/**
	 * 配置文件名
	 */
    private static String CONFIG_NAME="resource/config/pref.properties";
	
	/**
	 * 配置
	 */
	private static ConfigPreference pref;
	static{
		//获取资源管理器
		ResourceManager resManager=ResourceManager.getInstance();
	    //获取workspace root
        String workspaceRoot=resManager.getWorkspaceRoot();
		//获取工程名
        String projectName=resManager.getProjectName(PreferenceUtil.class);
	    //定义路径
        String path=workspaceRoot+"/"+projectName+"/"+CONFIG_NAME;
        //创建对象
        pref=new ConfigPreference(path);

	}

	/**
	 *  获取配置
	 * @param mark
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String get(String mark, String key, String defaultValue) {
		return pref.getString(mark, key, defaultValue);
	}
	
	/**
	 * 获取配置
	 * @param mark
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static int getInt(String mark, String key, int defaultValue) {
		return pref.getInt(mark, key, defaultValue);
	}
	
	/**
	 * 获取配置
	 * @param mark
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static long getLong(String mark, String key, long defaultValue) {
	   return pref.getLong(mark, key, defaultValue);
	}
	
	/**
	 * 获取配置
	 * @param mark
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static boolean getBoolean(String mark, String key, boolean defaultValue) {
		return pref.getBoolean(mark, key, defaultValue);
	}
}
