package service.auth.core.f10;

import both.annotation.Column;
import both.annotation.Table;
import both.db.constant.ITableNameConst;

/**
 * 用户扩展信息表
 * 
 */
// public static final String FOX_AUTH_USEREXTENDINFO = "FOX_AUTH_USEREXTENDINFO"; // 用户扩展信息表
@Table(Name = ITableNameConst.FOX_MGR_AUTH_TELLEREXTENDINFO)
public class FOX_MGR_AUTH_TELLEREXTENDINFO_DBO {
		/**
		 * 用户代号
		 */
		@Column(Name="TELLER_ID")
	    private String USERID;	
		/**
		 * 英文名
		 */
		@Column(Name="ENG_NAME")
	    private String ENGLISHNAME;	
		/**
		 * 性别（1-男，2-女）
		 */
		@Column(Name="SEX")
	    private String SEX;	
		/**
		 * 手机号
		 */
		@Column(Name="MOBILE_PHONE")
	    private String CELLPHONE;	
		/**
		 * 固定电话
		 */
		@Column(Name="FIXED_PHONE")
	    private String PHONE;	
		/**
		 * 家庭住址
		 */
		@Column(Name="ADDRESS")
	    private String ADDRESS;	
		/**
		 * 用户级别
		 */
		@Column(Name="TELLER_LEVEL")
	    private String USERLEVEL;	
		/**
		 * 最后一次登陆时间
		 */
		@Column(Name="LAST_LOGIN_DATE")
	    private String LASTLOGINTIME;	
		/**
		 * 最后一次退出时间
		 */
		@Column(Name="LAST_LOGOUT_DATE")
	    private String LASTLOGOUTTIME;	
		/**
		 * 电子邮箱
		 */
		@Column(Name="EMAIL")
	    private String EMAIL;	
	    
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
		 * 设置英文名
		 */
	    public void set_ENGLISHNAME(String ENGLISHNAME) 
	    {
	        this.ENGLISHNAME = ENGLISHNAME;
	    }
		/**
		 * 获取英文名
		 */
		public String get_ENGLISHNAME()
	    {
	        return ENGLISHNAME;
	    }
	    
		/**
		 * 设置性别（1-男，2-女）
		 */
	    public void set_SEX(String SEX) 
	    {
	        this.SEX = SEX;
	    }
		/**
		 * 获取性别（1-男，2-女）
		 */
		public String get_SEX()
	    {
	        return SEX;
	    }
	    
		/**
		 * 设置手机号
		 */
	    public void set_CELLPHONE(String CELLPHONE) 
	    {
	        this.CELLPHONE = CELLPHONE;
	    }
		/**
		 * 获取手机号
		 */
		public String get_CELLPHONE()
	    {
	        return CELLPHONE;
	    }
	    
		/**
		 * 设置固定电话
		 */
	    public void set_PHONE(String PHONE) 
	    {
	        this.PHONE = PHONE;
	    }
		/**
		 * 获取固定电话
		 */
		public String get_PHONE()
	    {
	        return PHONE;
	    }
	    
		/**
		 * 设置家庭住址
		 */
	    public void set_ADDRESS(String ADDRESS) 
	    {
	        this.ADDRESS = ADDRESS;
	    }
		/**
		 * 获取家庭住址
		 */
		public String get_ADDRESS()
	    {
	        return ADDRESS;
	    }
	    
		/**
		 * 设置用户级别
		 */
	    public void set_USERLEVEL(String USERLEVEL) 
	    {
	        this.USERLEVEL = USERLEVEL;
	    }
		/**
		 * 获取用户级别
		 */
		public String get_USERLEVEL()
	    {
	        return USERLEVEL;
	    }
	    
		/**
		 * 设置最后一次登陆时间
		 */
	    public void set_LASTLOGINTIME(String LASTLOGINTIME) 
	    {
	        this.LASTLOGINTIME = LASTLOGINTIME;
	    }
		/**
		 * 获取最后一次登陆时间
		 */
		public String get_LASTLOGINTIME()
	    {
	        return LASTLOGINTIME;
	    }
	    
		/**
		 * 设置最后一次退出时间
		 */
	    public void set_LASTLOGOUTTIME(String LASTLOGOUTTIME) 
	    {
	        this.LASTLOGOUTTIME = LASTLOGOUTTIME;
	    }
		/**
		 * 获取最后一次退出时间
		 */
		public String get_LASTLOGOUTTIME()
	    {
	        return LASTLOGOUTTIME;
	    }
	    
		/**
		 * 设置电子邮箱
		 */
	    public void set_EMAIL(String EMAIL) 
	    {
	        this.EMAIL = EMAIL;
	    }
		/**
		 * 获取电子邮箱
		 */
		public String get_EMAIL()
	    {
	        return EMAIL;
	    }
		@Override
		public String toString() {
			return "FOX_MGR_AUTH_TELLEREXTENDINFO_DBO [USERID=" + USERID
					+ ", ENGLISHNAME=" + ENGLISHNAME + ", SEX=" + SEX
					+ ", CELLPHONE=" + CELLPHONE + ", PHONE=" + PHONE
					+ ", ADDRESS=" + ADDRESS + ", USERLEVEL=" + USERLEVEL
					+ ", LASTLOGINTIME=" + LASTLOGINTIME + ", LASTLOGOUTTIME="
					+ LASTLOGOUTTIME + ", EMAIL=" + EMAIL + "]";
		}
		
		
	    
}