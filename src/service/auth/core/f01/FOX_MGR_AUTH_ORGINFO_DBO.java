package service.auth.core.f01;

import both.annotation.Column;
import both.annotation.Table;
import both.db.constant.ITableNameConst;

/**
 * 机构信息表
 * 
 */
// public static final String FOX_AUTH_ORGINFO = "FOX_AUTH_ORGINFO"; // 机构信息表
@Table(Name = ITableNameConst.FOX_MGR_AUTH_ORGINFO)
public class FOX_MGR_AUTH_ORGINFO_DBO {
	/**
	 * 机构编号
	 */
	@Column(Name="ORGID")
	private String ORGID;
	/**
	 * 机构中文名
	 */
	@Column(Name="ORG_CN_NAME")
	private String ORGCHINAME;
	/**
	 * 机构英文名
	 */
	@Column(Name="ORG_ENG_NAME")
	private String ORGENGNAME;
	/**
	 * 上级机构
	 */
	@Column(Name="PARENT_ORGID")
	private String PARENTORGID;
	/**
	 * 上级机构名称
	 */
	@Column
	private String PARENTORGNAME;
	/**
	 * 机构级别
	 */
	@Column(Name="ORG_LEVEL")
	private String ORGLVL;
	/**
	 * 机构类型
	 */
	@Column(Name="ORG_TYPE")
	private String ORGTYPE;
	/**
	 * 创建日期
	 */
	@Column(Name="CREATE_DATE")
	private String CREATEDATE;
	/**
	 * 创建时间
	 */
	@Column(Name="CREATE_TIME")
	private String CREATETIME;
	/**
	 * 更新日期
	 */
	@Column(Name="UPDATE_DATE")
	private String UPDATEDATE;
	/**
	 * 更新时间
	 */
	@Column(Name="UPDATE_TIME")
	private String UPDATETIME;
	/**
	 * 状态（1-可用，0-不可用）
	 */
	@Column(Name="STATUS")
	private String STATUS;

	/**
	 * 设置机构编号
	 */
	public void set_ORGID(String ORGID) {
		this.ORGID = ORGID;
	}

	/**
	 * 获取机构编号
	 */
	public String get_ORGID() {
		return ORGID;
	}

	/**
	 * 设置机构中文名
	 */
	public void set_ORGCHINAME(String ORGCHINAME) {
		this.ORGCHINAME = ORGCHINAME;
	}

	/**
	 * 获取机构中文名
	 */
	public String get_ORGCHINAME() {
		return ORGCHINAME;
	}

	/**
	 * 设置机构英文名
	 */
	public void set_ORGENGNAME(String ORGENGNAME) {
		this.ORGENGNAME = ORGENGNAME;
	}

	/**
	 * 获取机构英文名
	 */
	public String get_ORGENGNAME() {
		return ORGENGNAME;
	}

	/**
	 * 设置上级机构
	 */
	public void set_PARENTORGID(String PARENTORGID) {
		this.PARENTORGID = PARENTORGID;
	}

	/**
	 * 获取上级机构
	 */
	public String get_PARENTORGID() {
		return PARENTORGID;
	}
	/**
	 * 设置上级机构名称
	 */
	public void set_PARENTORGNAME(String PARENTORGNAME) {
		this.PARENTORGNAME = PARENTORGNAME;
	}

	/**
	 * 获取上级机构名称
	 */
	public String get_PARENTORGNAME() {
		return PARENTORGNAME;
	}
	/**
	 * 设置机构级别
	 */
	public void set_ORGLVL(String ORGLVL) {
		this.ORGLVL = ORGLVL;
	}

	/**
	 * 获取机构级别
	 */
	public String get_ORGLVL() {
		return ORGLVL;
	}

	/**
	 * 设置机构类型
	 */
	public void set_ORGTYPE(String ORGTYPE) {
		this.ORGTYPE = ORGTYPE;
	}

	/**
	 * 获取机构类型
	 */
	public String get_ORGTYPE() {
		return ORGTYPE;
	}

	/**
	 * 设置创建日期
	 */
	public void set_CREATEDATE(String CREATEDATE) {
		this.CREATEDATE = CREATEDATE;
	}

	/**
	 * 获取创建日期
	 */
	public String get_CREATEDATE() {
		return CREATEDATE;
	}

	/**
	 * 设置创建时间
	 */
	public void set_CREATETIME(String CREATETIME) {
		this.CREATETIME = CREATETIME;
	}

	/**
	 * 获取创建时间
	 */
	public String get_CREATETIME() {
		return CREATETIME;
	}

	/**
	 * 设置更新日期
	 */
	public void set_UPDATEDATE(String UPDATEDATE) {
		this.UPDATEDATE = UPDATEDATE;
	}

	/**
	 * 获取更新日期
	 */
	public String get_UPDATEDATE() {
		return UPDATEDATE;
	}

	/**
	 * 设置更新时间
	 */
	public void set_UPDATETIME(String UPDATETIME) {
		this.UPDATETIME = UPDATETIME;
	}

	/**
	 * 获取更新时间
	 */
	public String get_UPDATETIME() {
		return UPDATETIME;
	}

	/**
	 * 设置状态（1-可用，0-不可用）
	 */
	public void set_STATUS(String STATUS) {
		this.STATUS = STATUS;
	}

	/**
	 * 获取状态（1-可用，0-不可用）
	 */
	public String get_STATUS() {
		return STATUS;
	}

	@Override
	public String toString() {
		return "FOX_MGR_AUTH_ORGINFO_DBO [ORGID=" + ORGID + ", ORGCHINAME="
				+ ORGCHINAME + ", ORGENGNAME=" + ORGENGNAME + ", PARENTORGID="
				+ PARENTORGID + ", PARENTORGNAME=" + PARENTORGNAME
				+ ", ORGLVL=" + ORGLVL + ", ORGTYPE=" + ORGTYPE
				+ ", CREATEDATE=" + CREATEDATE + ", CREATETIME=" + CREATETIME
				+ ", UPDATEDATE=" + UPDATEDATE + ", UPDATETIME=" + UPDATETIME
				+ ", STATUS=" + STATUS + "]";
	}

}