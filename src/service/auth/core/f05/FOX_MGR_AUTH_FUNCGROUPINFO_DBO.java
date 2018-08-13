package service.auth.core.f05;

import both.annotation.Column;
import both.annotation.Table;
import both.db.constant.ITableNameConst;

/**
 * 功能组信息表
 * 
 */
// public static final String FOX_AUTH_FUNCGROUPINFO = "FOX_AUTH_FUNCGROUPINFO"; // 功能组信息表
@Table(Name = ITableNameConst.FOX_MGR_AUTH_FUNCGROUPINFO)
public class FOX_MGR_AUTH_FUNCGROUPINFO_DBO {
		/**
		 * 交易组编号
		 */
		@Column(Name="GROUP_ID")
	    private String GROUPID;	
		/**
		 * 交易组名称
		 */
		@Column(Name="GROUP_NAME")
	    private String GROUPNAME;	
		/**
		 * 交易组描述
		 */
		@Column(Name="GROUP_DESCRIBTION")
	    private String GROUPDESCRIBTION;	
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
		 * 设置交易组编号
		 */
	    public void set_GROUPID(String GROUPID) 
	    {
	        this.GROUPID = GROUPID;
	    }
		/**
		 * 获取交易组编号
		 */
		public String get_GROUPID()
	    {
	        return GROUPID;
	    }
	    
		/**
		 * 设置交易组名称
		 */
	    public void set_GROUPNAME(String GROUPNAME) 
	    {
	        this.GROUPNAME = GROUPNAME;
	    }
		/**
		 * 获取交易组名称
		 */
		public String get_GROUPNAME()
	    {
	        return GROUPNAME;
	    }
	    
		/**
		 * 设置交易组描述
		 */
	    public void set_GROUPDESCRIBTION(String GROUPDESCRIBTION) 
	    {
	        this.GROUPDESCRIBTION = GROUPDESCRIBTION;
	    }
		/**
		 * 获取交易组描述
		 */
		public String get_GROUPDESCRIBTION()
	    {
	        return GROUPDESCRIBTION;
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