package service.auth.core.f04;

import both.annotation.Column;
import both.annotation.Table;
import both.db.constant.ITableNameConst;

/**
 * 角色信息表
 * 
 */
// public static final String FOX_AUTH_ROLEINFO = "FOX_AUTH_ROLEINFO"; // 角色信息表
@Table(Name = ITableNameConst.FOX_MGR_AUTH_ROLEINFO)
public class FOX_MGR_AUTH_ROLEINFO_DBO {
		/**
		 * 角色编号
		 */
		@Column(Name="ROLE_ID")
	    private String ROLEID;	
		/**
		 * 角色名称
		 */
		@Column(Name="ROLE_NAME")
	    private String ROLENAME;	
		/**
		 * 角色描述
		 */
		@Column(Name="ROLE_DESCRIBTION")
	    private String ROLEDESCRIBTION;	
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
		 * 是否有效（1-是，0-否）默认1
		 */
		@Column(Name="STATUS")
	    private String STATUS;	
	    
		/**
		 * 设置角色编号
		 */
	    public void set_ROLEID(String ROLEID) 
	    {
	        this.ROLEID = ROLEID;
	    }
		/**
		 * 获取角色编号
		 */
		public String get_ROLEID()
	    {
	        return ROLEID;
	    }
	    
		/**
		 * 设置角色名称
		 */
	    public void set_ROLENAME(String ROLENAME) 
	    {
	        this.ROLENAME = ROLENAME;
	    }
		/**
		 * 获取角色名称
		 */
		public String get_ROLENAME()
	    {
	        return ROLENAME;
	    }
	    
		/**
		 * 设置角色描述
		 */
	    public void set_ROLEDESCRIBTION(String ROLEDESCRIBTION) 
	    {
	        this.ROLEDESCRIBTION = ROLEDESCRIBTION;
	    }
		/**
		 * 获取角色描述
		 */
		public String get_ROLEDESCRIBTION()
	    {
	        return ROLEDESCRIBTION;
	    }
	    
		/**
		 * 设置创建日期
		 */
	    public void set_CREATEDATE(String CREATEDATE) 
	    {
	        this.CREATEDATE = CREATEDATE;
	    }
		/**
		 * 获取创建日期
		 */
		public String get_CREATEDATE()
	    {
	        return CREATEDATE;
	    }
	    
		/**
		 * 设置创建时间
		 */
	    public void set_CREATETIME(String CREATETIME) 
	    {
	        this.CREATETIME = CREATETIME;
	    }
		/**
		 * 获取创建时间
		 */
		public String get_CREATETIME()
	    {
	        return CREATETIME;
	    }
	    
		/**
		 * 设置更新日期
		 */
	    public void set_UPDATEDATE(String UPDATEDATE) 
	    {
	        this.UPDATEDATE = UPDATEDATE;
	    }
		/**
		 * 获取更新日期
		 */
		public String get_UPDATEDATE()
	    {
	        return UPDATEDATE;
	    }
	    
		/**
		 * 设置更新时间
		 */
	    public void set_UPDATETIME(String UPDATETIME) 
	    {
	        this.UPDATETIME = UPDATETIME;
	    }
		/**
		 * 获取更新时间
		 */
		public String get_UPDATETIME()
	    {
	        return UPDATETIME;
	    }
	    
		/**
		 * 设置是否有效（1-是，0-否）默认1
		 */
	    public void set_STATUS(String STATUS) 
	    {
	        this.STATUS = STATUS;
	    }
		/**
		 * 获取是否有效（1-是，0-否）默认1
		 */
		public String get_STATUS()
	    {
	        return STATUS;
	    }
	    
}