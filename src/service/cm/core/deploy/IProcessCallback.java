package service.cm.core.deploy;

/**
 * 回调函数
 * 
 * @author 江成
 * 
 */
public interface IProcessCallback {

	/**
	 * 回调
	 * 
	 * @param procedureInfo
	 */
	public void callback(ProcedureInfo procedureInfo);

}
