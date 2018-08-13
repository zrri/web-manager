package service.auth.core.padteller;

import both.annotation.Column;
import both.annotation.Table;
import both.db.constant.ITableNameConst;
/**
 * PAD柜员基本信息实体类
 * @author Mr_Jiang
 *
 */
@Table(Name=ITableNameConst.FOX_PAD_TELLER_TELLERINFO)
public class FOX_PAD_TELLER_TELLERINFO_DBO {
	@Column
	private String TELLER_ID;
	@Column
	private String TELLER_NAME;
	@Column
	private String ORGID;
	@Column
	private String TELLER_TYPE;
	@Column
	private String PASSWORD;
	@Column
	private String TELLER_STATUS;
	@Column
	private String TELLER_LOGIN_TYPE;
	@Column
	private String IDCARD;
	@Column
	private String MOBILE_NUMBER;
	@Column
	private String CREATE_DATE;
	@Column
	private String UPDATE_DATE;
	@Column
	private String ISVOID;

	public String getTELLER_ID() {
		return TELLER_ID;
	}

	public void setTELLER_ID(String tELLER_ID) {
		TELLER_ID = tELLER_ID;
	}

	public String getTELLER_NAME() {
		return TELLER_NAME;
	}

	public void setTELLER_NAME(String tELLER_NAME) {
		TELLER_NAME = tELLER_NAME;
	}

	public String getORGID() {
		return ORGID;
	}

	public void setORGID(String oRGID) {
		ORGID = oRGID;
	}

	public String getTELLER_TYPE() {
		return TELLER_TYPE;
	}

	public void setTELLER_TYPE(String tELLER_TYPE) {
		TELLER_TYPE = tELLER_TYPE;
	}

	public String getPASSWORD() {
		return PASSWORD;
	}

	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}

	public String getTELLER_STATUS() {
		return TELLER_STATUS;
	}

	public void setTELLER_STATUS(String tELLER_STATUS) {
		TELLER_STATUS = tELLER_STATUS;
	}

	public String getTELLER_LOGIN_TYPE() {
		return TELLER_LOGIN_TYPE;
	}

	public void setTELLER_LOGIN_TYPE(String tELLER_LOGIN_TYPE) {
		TELLER_LOGIN_TYPE = tELLER_LOGIN_TYPE;
	}

	public String getIDCARD() {
		return IDCARD;
	}

	public void setIDCARD(String iDCARD) {
		IDCARD = iDCARD;
	}

	public String getMOBILE_NUMBER() {
		return MOBILE_NUMBER;
	}

	public void setMOBILE_NUMBER(String mOBILE_NUMBER) {
		MOBILE_NUMBER = mOBILE_NUMBER;
	}

	public String getCREATE_DATE() {
		return CREATE_DATE;
	}

	public void setCREATE_DATE(String cREATE_DATE) {
		CREATE_DATE = cREATE_DATE;
	}

	public String getUPDATE_DATE() {
		return UPDATE_DATE;
	}

	public void setUPDATE_DATE(String uPDATE_DATE) {
		UPDATE_DATE = uPDATE_DATE;
	}

	public String getISVOID() {
		return ISVOID;
	}

	public void setISVOID(String iSVOID) {
		ISVOID = iSVOID;
	}

}