package service.auth.core.padrole;

import both.annotation.Column;
import both.annotation.Table;
import both.db.constant.ITableNameConst;

@Table(Name = ITableNameConst.FOX_PAD_TELLER_ROLEINFO)
public class FOX_PAD_TELLER_ROLEINFO_DBO {
	@Column
	private String ROLE_ID;
	@Column
	private String ROLE_NAME;
	@Column
	private String ROLE_DESCRIPTION;
	@Column
	private String CREATE_DATE;
	@Column
	private String UPDATE_DATE;
	@Column
	private String ISVOID;

	public String getROLE_ID() {
		return ROLE_ID;
	}

	public void setROLE_ID(String rOLE_ID) {
		ROLE_ID = rOLE_ID;
	}

	public String getROLE_NAME() {
		return ROLE_NAME;
	}

	public void setROLE_NAME(String rOLE_NAME) {
		ROLE_NAME = rOLE_NAME;
	}

	public String getROLE_DESCRIPTION() {
		return ROLE_DESCRIPTION;
	}

	public void setROLE_DESCRIPTION(String rOLE_DESCRIPTION) {
		ROLE_DESCRIPTION = rOLE_DESCRIPTION;
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