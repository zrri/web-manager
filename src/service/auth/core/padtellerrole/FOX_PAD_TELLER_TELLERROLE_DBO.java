package service.auth.core.padtellerrole;

import both.annotation.Column;
import both.annotation.Table;
import both.db.constant.ITableNameConst;


@Table(Name=ITableNameConst.FOX_PAD_TELLER_TELLERROLE)
public class FOX_PAD_TELLER_TELLERROLE_DBO {
	@Column
	private String TELLER_ID;
	@Column
	private String ROLE_ID;
	@Column
	private String ISVOID;

	public String getTELLER_ID() {
		return TELLER_ID;
	}

	public void setTELLER_ID(String tELLER_ID) {
		TELLER_ID = tELLER_ID;
	}

	public String getROLE_ID() {
		return ROLE_ID;
	}

	public void setROLE_ID(String rOLE_ID) {
		ROLE_ID = rOLE_ID;
	}

	public String getISVOID() {
		return ISVOID;
	}

	public void setISVOID(String iSVOID) {
		ISVOID = iSVOID;
	}

}