package both.entity.pad;

import both.annotation.Column;
import both.annotation.Table;
import both.db.constant.ITableNameConst;

/**
 * @author Wolf
 * 
 */
@Table(Name = ITableNameConst.FOX_TELLER_SESSION)
public class FoxTellerSession {

	@Column
	private String sessionid;// VARCHAR2(32) not null,
	@Column
	private String userid; // VARCHAR2(32),
	@Column
	private String orgid; // VARCHAR2(32),
	@Column
	private String sessiondata; // VARCHAR2(32),
	@Column
	private String operationdate; // VARCHAR2(10),
	@Column
	private String operationtime;// VARCHAR2(10)

	/**
	 * 客户端地址
	 */
	@Column
	private String address;
	@Column
	private Boolean ispad;// VARCHAR2(4)

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getSessiondata() {
		return sessiondata;
	}

	public void setSessiondata(String sessiondata) {
		this.sessiondata = sessiondata;
	}

	public String getOperationdate() {
		return operationdate;
	}

	public void setOperationdate(String operationdate) {
		this.operationdate = operationdate;
	}

	public String getOperationtime() {
		return operationtime;
	}

	public void setOperationtime(String operationtime) {
		this.operationtime = operationtime;
	}

	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}

	public Boolean getIspad() {
		return ispad;
	}

	public void setIspad(Boolean ispad) {
		this.ispad = ispad;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

}