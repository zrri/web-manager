package both.constants;

/**
 * web socket 消息名称常量表
 * 
 * @author liaozhijie
 * 
 */ 
public interface IWebSocketMessageName {
	/**
	 * 重复登陆前者强制登出通知
	 */
	public final static String MSG_LOGOUTED = "_MSG_LOGOUTED";

	/**
	 * 二维码状态变更通知
	 */
	public final static String MSG_STANDTEXT = "_MSG_STANDTEXT";
	/**
	 * 柜员消息通知
	 */
	public final static String MSG_TELLER_MESSAGE = "_MSG_TELLER_MESSAGE";
}