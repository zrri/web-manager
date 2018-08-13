package service.cm.core.alarm;

/**
 * 属性类型
 * 
 * @author 江成
 * 
 */
public enum AttributeType {

	/**
	 * 百分比
	 */
	PERCENT("百分比"),

	/**
	 * 数值
	 */
	NUMBER("数值"),

	/**
	 * 状态
	 */
	STATE("状态");

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 构造函数
	 * 
	 * @param name
	 */
	private AttributeType(String name) {
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
}
