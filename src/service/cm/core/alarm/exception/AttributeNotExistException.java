package service.cm.core.alarm.exception;

/**
 * 属性不存在异常
 * 
 * @author 江成
 * 
 */
public class AttributeNotExistException extends Exception {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6725016523585600373L;

	/**
	 * 默认构造函数
	 */
	public AttributeNotExistException() {
		super();
	}

	/**
	 * 构造函数
	 * 
	 * @param message
	 */
	public AttributeNotExistException(String message) {
		super(message);
	}

	/**
	 * 构造函数
	 * 
	 * @param message
	 * @param cause
	 */
	public AttributeNotExistException(String message, Throwable cause) {
		super(message, cause);
	}

}
