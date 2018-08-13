package service.auth.core.padorg;

import both.annotation.Column;
import both.annotation.Table;
import both.db.constant.ITableNameConst;

@Table(Name=ITableNameConst.FOX_PAD_TELLER_ORGINFO)
public class FOX_PAD_TELLER_ORGINFO_DBO {
	@Column
	private String ORGID;
	@Column
	private String ORG_CN_NAME;
	@Column
	private String ORG_ENG_NAME;
	@Column
	private String BRANCH_BANK;
	@Column
	private String PARENT_ORGID;
	@Column
	private String PARENT_ORG_NAME;
	@Column
	private String ORG_LEVEL;
	@Column
	private String ORG_TYPE;
	@Column
	private String SORT_NO;
	@Column
	private String CREATE_DATE;
	@Column
	private String UPDATE_DATE;
	@Column
	private String ISVOID;

	public String getORGID() {
		return ORGID;
	}

	public void setORGID(String oRGID) {
		ORGID = oRGID;
	}

	public String getORG_CN_NAME() {
		return ORG_CN_NAME;
	}

	public void setORG_CN_NAME(String oRG_CN_NAME) {
		ORG_CN_NAME = oRG_CN_NAME;
	}

	public String getORG_ENG_NAME() {
		return ORG_ENG_NAME;
	}

	public void setORG_ENG_NAME(String oRG_ENG_NAME) {
		ORG_ENG_NAME = oRG_ENG_NAME;
	}

	public String getBRANCH_BANK() {
		return BRANCH_BANK;
	}

	public void setBRANCH_BANK(String bRANCH_BANK) {
		BRANCH_BANK = bRANCH_BANK;
	}

	public String getPARENT_ORGID() {
		return PARENT_ORGID;
	}

	public void setPARENT_ORGID(String pARENT_ORGID) {
		PARENT_ORGID = pARENT_ORGID;
	}

	public String getPARENT_ORG_NAME() {
		return PARENT_ORG_NAME;
	}

	public void setPARENT_ORG_NAME(String pARENT_ORG_NAME) {
		PARENT_ORG_NAME = pARENT_ORG_NAME;
	}

	public String getORG_LEVEL() {
		return ORG_LEVEL;
	}

	public void setORG_LEVEL(String oRG_LEVEL) {
		ORG_LEVEL = oRG_LEVEL;
	}

	public String getORG_TYPE() {
		return ORG_TYPE;
	}

	public void setORG_TYPE(String oRG_TYPE) {
		ORG_TYPE = oRG_TYPE;
	}

	public String getSORT_NO() {
		return SORT_NO;
	}

	public void setSORT_NO(String sORT_NO) {
		SORT_NO = sORT_NO;
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