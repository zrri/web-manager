package both.constants;

public interface IResponseConstant {

	/**
	 * 返回码
	 */
	public static String retCode = "code";

	/**
	 * 返回信息
	 */
	public static String retMsg = "msg";

	/**
	 * 前台柜员流水号
	 */
	public static String reqCode = "tellerSeq";

	/**
	 * 成功
	 */
	public static String SUCCESS = "0";

	/**
	 * 失败
	 */
	public static String FAILED = "200000";

	/**
	 * 非最小号
	 */
	public static String FAILED_NOT_MIN_NUM = "200001";

	/**
	 * UKey绑定失败
	 */
	public static String FAILED_UKEY_BIND = "200002";

	/**
	 * UKEY销号失败
	 */
	public static String FAILED_NUMBER_ERASER = "200003";

	/**
	 * 无此设备记录
	 */
	public static String FAILED_NORECORD = "200004";

	/**
	 * 没有更新过密钥
	 */
	public static String FAILED_NOPIN = "200005";

	/**
	 * 设备停用状态
	 */
	public static String FAILED_DEV_STOP = "200006";

	/**
	 * 没有更新过密钥
	 */
	public static String FAILED_NOZMK = "200007";

	/**
	 * 没有更新过密钥
	 */
	public static String FAILED_NOZPK = "200008";
	/**
	 * 没有更新过密钥
	 */
	public static String FAILED_ZPK_OUTDAY = "200009";

	/**
	 * 字段为空
	 */
	public static String FAILED_NULL = "200010";

	/**
	 * 数据库错误
	 */
	public static String FAILED_DB_ERROR = "200011";

	/**
	 * 超时
	 */
	public static String FAILED_TIMEOUT = "200013";

	/**
	 * 交易时间
	 */
	public static String TIME = "time";

	/**
	 * 子交易不允许重复提交
	 */
	public static String FAILED_SUB_RESUBMIT = "200012";

	/**
	 * PAD登录成功
	 */
	public static String LOGIN_SUCCESS = "000000";
	
	/**
	 * 没有更新文件
	 */
	public static String FAILED_NO_UPDATEFILE ="300001";
	
	/**
	 * 版本不一致
	 */
	public static String FAILED_VERSION_ERROR ="300002";
	
	/**
	 * 获取节点错误
	 */
	public static String FAILED_NODE_ERROR ="300003";
}