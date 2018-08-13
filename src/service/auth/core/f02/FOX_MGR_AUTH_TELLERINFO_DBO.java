package service.auth.core.f02;

import both.annotation.Column;
import both.annotation.Table;
import both.db.constant.ITableNameConst;

/**
 * 用户基础信息表
 * 
 */
// public static final String FOX_AUTH_USERINFO = "FOX_AUTH_USERINFO"; // 用户基础信息表
@Table(Name = ITableNameConst.FOX_MGR_AUTH_TELLERINFO)
public class FOX_MGR_AUTH_TELLERINFO_DBO {
		/**
		 * 用户代号
		 */
		@Column(Name="TELLER_ID")
	    private String USERID;	
		/**
		 * 姓名
		 */
		@Column(Name="TELLER_NAME")
	    private String USERNAME;	
		/**
		 * 用户类型（0-普通用户，1-BST自动用户，2-POS自动用户,3-移动营销自动用户,4-ATM自动用户,5-特色业务自动用户）
		 */
		@Column(Name="TELLER_TYPE")
	    private String USERTYPE;	
		/**
		 * 识别号（身份证号，或者设备编号）
		 */
		@Column(Name="IDENTIFY_NO")
	    private String IDENTIFYNO;	
		/**
		 * 归属机构
		 */
		@Column(Name="ORGID")
	    private String ORGID;	
		/**
		 * 登录方式（0-密码身份证，1-密码）
		 */
		@Column(Name="LOGIN_TYPE")
	    private String LOGINTYPE;	
		/**
		 * 用户状态（0-启用，1-停用，2-注销，8-锁定，9-被交接）
		 */
		@Column(Name="STATUS")
	    private String STATUS;	
	    
		/**
		 * 设置用户代号
		 */
	    public void set_USERID(String USERID) 
	    {
	        this.USERID = USERID;
	    }
		/**
		 * 获取用户代号
		 */
		public String get_USERID()
	    {
	        return USERID;
	    }
	    
		/**
		 * 设置姓名
		 */
	    public void set_USERNAME(String USERNAME) 
	    {
	        this.USERNAME = USERNAME;
	    }
		/**
		 * 获取姓名
		 */
		public String get_USERNAME()
	    {
	        return USERNAME;
	    }
	    
		/**
		 * 设置用户类型（0-普通用户，1-BST自动用户，2-POS自动用户,3-移动营销自动用户,4-ATM自动用户,5-特色业务自动用户）
		 */
	    public void set_USERTYPE(String USERTYPE) 
	    {
	        this.USERTYPE = USERTYPE;
	    }
		/**
		 * 获取用户类型（0-普通用户，1-BST自动用户，2-POS自动用户,3-移动营销自动用户,4-ATM自动用户,5-特色业务自动用户）
		 */
		public String get_USERTYPE()
	    {
	        return USERTYPE;
	    }
	    
		/**
		 * 设置识别号（身份证号，或者设备编号）
		 */
	    public void set_IDENTIFYNO(String IDENTIFYNO) 
	    {
	        this.IDENTIFYNO = IDENTIFYNO;
	    }
		/**
		 * 获取识别号（身份证号，或者设备编号）
		 */
		public String get_IDENTIFYNO()
	    {
	        return IDENTIFYNO;
	    }
	    
		/**
		 * 设置归属机构
		 */
	    public void set_ORGID(String ORGID) 
	    {
	        this.ORGID = ORGID;
	    }
		/**
		 * 获取归属机构
		 */
		public String get_ORGID()
	    {
	        return ORGID;
	    }
	    
		/**
		 * 设置登录方式（0-密码身份证，1-密码）
		 */
	    public void set_LOGINTYPE(String LOGINTYPE) 
	    {
	        this.LOGINTYPE = LOGINTYPE;
	    }
		/**
		 * 获取登录方式（0-密码身份证，1-密码）
		 */
		public String get_LOGINTYPE()
	    {
	        return LOGINTYPE;
	    }
	    
		/**
		 * 设置用户状态（0-启用，1-停用，2-注销，8-锁定，9-被交接）
		 */
	    public void set_STATUS(String STATUS) 
	    {
	        this.STATUS = STATUS;
	    }
		/**
		 * 获取用户状态（0-启用，1-停用，2-注销，8-锁定，9-被交接）
		 */
		public String get_STATUS()
	    {
	        return STATUS;
	    }
		@Override
		public String toString() {
			return "FOX_MGR_AUTH_TELLERINFO_DBO [USERID=" + USERID + ", USERNAME="
					+ USERNAME + ", USERTYPE=" + USERTYPE + ", IDENTIFYNO="
					+ IDENTIFYNO + ", ORGID=" + ORGID + ", LOGINTYPE="
					+ LOGINTYPE + ", STATUS=" + STATUS + "]";
		}
	    
}