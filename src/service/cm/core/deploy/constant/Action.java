package service.cm.core.deploy.constant;

/**
 * 动作
 * 
 * @author 江成
 * 
 */
public enum Action {

	/**
	 * 准备
	 */
	prepare("准备"),
	
	/**
	 * 完成准备
	 */
	finishPrepare("完成准备"),

	/**
	 * 传送文件
	 */
	transmit("传送文件"),
	
	/**
	 * 完成传送文件
	 */
	finishTransmit("完成传送文件"),

	/**
	 * 解压文件
	 */
	uncompress("解压文件"),
	
	/**
	 * 完成解压文件
	 */
	finishUncompress("完成解压文件"),

	/**
	 * 启动服务器
	 */
	startup("启动服务器"),
	
	/**
	 *  完成启动服务器
	 */
	finishStartup("完成启动服务器"),

	/**
	 * 关闭服务器
	 */
	shutdown("关闭服务器"),
	
	/**
	 * 完成关闭服务器
	 */
	finishShutdown("完成关闭服务器"),

	/**
	 * 备份文件
	 */
	backup("备份文件"),
	
	/**
	 * 完成备份文件
	 */
	finishBackup("完成备份文件"),

	/**
	 * 更新文件
	 */
	update("更新文件"),
	
	/**
	 * 完成更新文件
	 */
	finishUpdate("完成更新文件"),
	
	/**
	 * 检查服务器状态
	 */
	checkServerState("检查服务器状态"),
	
	/**
	 * 校验
	 */
	check("校验"),
	
	/**
	 * 校验完成
	 */
	finishCheck("完成校验"),
	
	/**
	 * 回退
	 */
	rollback("回退"),
	
	/**
	 * 完成回退
	 */
	finishRollback("完成回退"),
	
	/**
	 * 杀进程
	 */
	kill("杀进程"),
	
	/**
	 * 完成状态
	 */
	finish("完成状态");

	/**
	 * 动作名称
	 */
	private String name;

	/**
	 * 构造函数
	 * 
	 * @param name
	 */
	private Action(String name) {
		this.name = name;
	}

	/**
	 * 获取动作名称
	 * 
	 * @return
	 */
	public String getName() {
		return this.name;
	}

}
