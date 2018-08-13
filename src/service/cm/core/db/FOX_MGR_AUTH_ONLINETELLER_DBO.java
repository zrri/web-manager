package service.cm.core.db;

import both.annotation.Column;
import both.annotation.Table;
import both.db.constant.ITableNameConst;

/**
 * 用户在线表
 * 
 */
// public static final String FOX_AUTH_ONLINEUSER = "FOX_AUTH_ONLINEUSER"; // 用户在线表
@Table(Name = ITableNameConst.FOX_MGR_AUTH_ONLINETELLER)
public class FOX_MGR_AUTH_ONLINETELLER_DBO {
		/**
		 * 用户代号
		 */
		@Column(Name="TELLER_ID")
	    private String USERID;	
		/**
		 * 用户名称
		 */
		@Column(Name="TELLER_NAME")
	    private String USERNAME;	
		/**
		 * 机构号
		 */
		@Column(Name="ORGID")
	    private String ORGID;	
		/**
		 * 机构名称
		 */
		@Column(Name="ORG_NAME")
	    private String ORGNAME;	
		/**
		 * 客户端IP
		 */
		@Column(Name="CLIENT_IP")
	    private String CLIENTIP;	
		/**
		 * 服务端IP
		 */
		@Column(Name="SERVER_IP")
	    private String SERVERIP;	
		/**
		 * 登录日期
		 */
		@Column(Name="LOGIN_DATE")
	    private String LOGINDATE;	
		/**
		 * 登录时间
		 */
		@Column(Name="LOGIN_TIME")
	    private String LOGINTIME;	
		 
//		/**
//		 * 会话id
//		 */
//		 @Column
//	    private String SESSIONID;	
//	    
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
		 * 设置用户名称
		 */
	    public void set_USERNAME(String USERNAME) 
	    {
	        this.USERNAME = USERNAME;
	    }
		/**
		 * 获取用户名称
		 */
		public String get_USERNAME()
	    {
	        return USERNAME;
	    }
	    
		/**
		 * 设置机构号
		 */
	    public void set_ORGID(String ORGID) 
	    {
	        this.ORGID = ORGID;
	    }
		/**
		 * 获取机构号
		 */
		public String get_ORGID()
	    {
	        return ORGID;
	    }
	    
		/**
		 * 设置机构名称
		 */
	    public void set_ORGNAME(String ORGNAME) 
	    {
	        this.ORGNAME = ORGNAME;
	    }
		/**
		 * 获取机构名称
		 */
		public String get_ORGNAME()
	    {
	        return ORGNAME;
	    }
	    
		/**
		 * 设置客户端IP
		 */
	    public void set_CLIENTIP(String CLIENTIP) 
	    {
	        this.CLIENTIP = CLIENTIP;
	    }
		/**
		 * 获取客户端IP
		 */
		public String get_CLIENTIP()
	    {
	        return CLIENTIP;
	    }
	    
		/**
		 * 设置服务端IP
		 */
	    public void set_SERVERIP(String SERVERIP) 
	    {
	        this.SERVERIP = SERVERIP;
	    }
		/**
		 * 获取服务端IP
		 */
		public String get_SERVERIP()
	    {
	        return SERVERIP;
	    }
	    
		/**
		 * 设置登录日期
		 */
	    public void set_LOGINDATE(String LOGINDATE) 
	    {
	        this.LOGINDATE = LOGINDATE;
	    }
		/**
		 * 获取登录日期
		 */
		public String get_LOGINDATE()
	    {
	        return LOGINDATE;
	    }
	    
		/**
		 * 设置登录时间
		 */
	    public void set_LOGINTIME(String LOGINTIME) 
	    {
	        this.LOGINTIME = LOGINTIME;
	    }
		/**
		 * 获取登录时间
		 */
		public String get_LOGINTIME()
	    {
	        return LOGINTIME;
	    }
//	    
//		/**
//		 * 设置会话id
//		 */
//	    public void set_SESSIONID(String SESSIONID) 
//	    {
//	        this.SESSIONID = SESSIONID;
//	    }
//		/**
//		 * 获取会话id
//		 */
//		public String get_SESSIONID()
//	    {
//	        return SESSIONID;
//	    }
	    
}