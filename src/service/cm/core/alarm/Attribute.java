package service.cm.core.alarm;

/**
 * 属性
 * 
 * @author 江成
 * 
 */
public class Attribute {

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 类型
	 */
	private Class<?> classType;

	/**
	 * 属性类型
	 */
	private AttributeType attributeType;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 是否有上阀值
	 */
	private boolean upThresholdEnable;

	/**
	 * 是否有下阀值
	 */
	private boolean lowThresholdEnable;

	/**
	 * 是否有状态值
	 */
	private boolean stateValueEnable;

	/**
	 * 上阀值是否启用
	 * 
	 * @return
	 */
	public boolean isUpThresholdEnable() {
		return upThresholdEnable;
	}

	/**
	 * 设置上阀值是否启用
	 * @param upThresholdEnable
	 */
	public void setUpThresholdEnable(boolean upThresholdEnable) {
		this.upThresholdEnable = upThresholdEnable;
	}

	/**
	 * 下阀值是否启用
	 * @return
	 */
	public boolean isLowThresholdEnable() {
		return lowThresholdEnable;
	}

	/**
	 * 设置下阀值启用
	 * @param lowThresholdEnable
	 */
	public void setLowThresholdEnable(boolean lowThresholdEnable) {
		this.lowThresholdEnable = lowThresholdEnable;
	}

	/**
	 * 是否状态值启用
	 * @return
	 */
	public boolean isStateValueEnable() {
		return stateValueEnable;
	}

	/**
	 * 设置状态值启用
	 * @param stateValueEnable
	 */
	public void setStateValueEnable(boolean stateValueEnable) {
		this.stateValueEnable = stateValueEnable;
	}

	/**
	 * 获取属性名
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置属性名
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取类型
	 * 
	 * @return
	 */
	public Class<?> getClassType() {
		return classType;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 */
	public void setClassType(Class<?> type) {
		this.classType = type;
	}

	/**
	 * 获取属性类型
	 * 
	 * @return
	 */
	public AttributeType getAttributeType() {
		return this.attributeType;
	}

	/**
	 * 设置属性类型
	 * 
	 * @param type
	 */
	public void setAttributeType(AttributeType type) {
		this.attributeType = type;
	}

	/**
	 * 获取描述
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 设置描述
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
