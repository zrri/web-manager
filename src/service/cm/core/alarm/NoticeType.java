package service.cm.core.alarm;

/**
 * 通知类型
 * 
 * @author 江成
 * 
 */
public enum NoticeType {

	/**
	 * 提示
	 */
	TIP("alarmTip"),

	/**
	 * 短信
	 */
	MESSAGE("alarmMessage"),

	/**
	 * 邮件
	 */
	MAIL("alarmMail");

	/**
	 * 描述
	 */
	private String name;

	/**
	 * 构造函数
	 * 
	 * @param name
	 */
	private NoticeType(String name) {
		this.name = name;
	}

	/**
	 * 获取名称
	 * 
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 解析
	 * @param name
	 * @return
	 */
	public static NoticeType parse(String name) {
		if ("alarmTip".equals(name)) {
			return NoticeType.TIP;
		} else if ("alarmMessage".equals(name)) {
			return NoticeType.MESSAGE;
		} else {
			return NoticeType.MAIL;
		}
	}

}
