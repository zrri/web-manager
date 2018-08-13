package service.auth.core.padrolefunc;

import both.annotation.Column;
import both.annotation.Table;
import both.db.constant.ITableNameConst;

@Table(Name = ITableNameConst.FOX_PAD_TELLER_ROLEFUNC)
public class FOX_PAD_TELLER_ROLEFUNC_DBO {
	@Column
	private String ROLE_ID;
	@Column
	private String FUNC_ID;
	@Column
	private String ISVOID;

	public String getROLE_ID() {
		return ROLE_ID;
	}

	public void setROLE_ID(String rOLE_ID) {
		ROLE_ID = rOLE_ID;
	}

	public String getFUNC_ID() {
		return FUNC_ID;
	}

	public void setFUNC_ID(String fUNC_ID) {
		FUNC_ID = fUNC_ID;
	}

	public String getISVOID() {
		return ISVOID;
	}

	public void setISVOID(String iSVOID) {
		ISVOID = iSVOID;
	}

}