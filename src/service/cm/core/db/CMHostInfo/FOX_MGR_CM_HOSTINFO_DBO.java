package service.cm.core.db.CMHostInfo;

import service.cm.core.db.ICMConst;
import both.annotation.Column;
import both.annotation.Table;

/**
 * 主机信息表
 * 
 */
// public static final String FOX_CM_HOSTINFO = "FOX_CM_HOSTINFO"; // 主机信息表
@Table(Name = ICMConst.FOX_MGR_CM_HOSTINFO)
public class FOX_MGR_CM_HOSTINFO_DBO {
		/**
		 * 主机ip
		 */
		@Column(Name="HOST_IP")
	    private String HOSTIP;	
		/**
		 * 文件传输方式，01-ftp方式 02-sftp方式
		 */
		@Column(Name="FILE_TRANSPORT_WAY")
	    private String FILEMODE;	
		/**
		 * 文件传输端口
		 */
		@Column(Name="FILE_TRANSPORT_PORT")
	    private String FILEPORT;	
		/**
		 * 文件传输用户名
		 */
		@Column(Name="FILE_TRANSPORT_USERNAME")
	    private String FILEUSERNAME;	
		/**
		 * 文件传输密码
		 */
		@Column(Name="FILE_TRANSPORT_PASSWORD")
	    private String FILEPASSWORD;	
		/**
		 * 登录方式
		 */
		@Column(Name="LOGIN_WAY")
	    private String LOGINMODE;	
		/**
		 * 登录端口
		 */
		@Column(Name="LOGIN_PORT")
	    private String LOGINPORT;	
		/**
		 * 登录用户名
		 */
		@Column(Name="LOGIN_USERNAME")
	    private String LOGINUSERNAME;	
		/**
		 * 登录密码
		 */
		@Column(Name="LOGIN_PASSWORD")
	    private String LOGINPASSWORD;	
	    
		/**
		 * 设置主机ip
		 */
	    public void set_HOSTIP(String HOSTIP) 
	    {
	        this.HOSTIP = HOSTIP;
	    }
		/**
		 * 获取主机ip
		 */
		public String get_HOSTIP()
	    {
	        return HOSTIP;
	    }
	    
		/**
		 * 设置文件传输方式，01-ftp方式 02-sftp方式
		 */
	    public void set_FILEMODE(String FILEMODE) 
	    {
	        this.FILEMODE = FILEMODE;
	    }
		/**
		 * 获取文件传输方式，01-ftp方式 02-sftp方式
		 */
		public String get_FILEMODE()
	    {
	        return FILEMODE;
	    }
	    
		/**
		 * 设置文件传输端口
		 */
	    public void set_FILEPORT(String FILEPORT) 
	    {
	        this.FILEPORT = FILEPORT;
	    }
		/**
		 * 获取文件传输端口
		 */
		public String get_FILEPORT()
	    {
	        return FILEPORT;
	    }
	    
		/**
		 * 设置文件传输用户名
		 */
	    public void set_FILEUSERNAME(String FILEUSERNAME) 
	    {
	        this.FILEUSERNAME = FILEUSERNAME;
	    }
		/**
		 * 获取文件传输用户名
		 */
		public String get_FILEUSERNAME()
	    {
	        return FILEUSERNAME;
	    }
	    
		/**
		 * 设置文件传输密码
		 */
	    public void set_FILEPASSWORD(String FILEPASSWORD) 
	    {
	        this.FILEPASSWORD = FILEPASSWORD;
	    }
		/**
		 * 获取文件传输密码
		 */
		public String get_FILEPASSWORD()
	    {
	        return FILEPASSWORD;
	    }
	    
		/**
		 * 设置登录方式
		 */
	    public void set_LOGINMODE(String LOGINMODE) 
	    {
	        this.LOGINMODE = LOGINMODE;
	    }
		/**
		 * 获取登录方式
		 */
		public String get_LOGINMODE()
	    {
	        return LOGINMODE;
	    }
	    
		/**
		 * 设置登录端口
		 */
	    public void set_LOGINPORT(String LOGINPORT) 
	    {
	        this.LOGINPORT = LOGINPORT;
	    }
		/**
		 * 获取登录端口
		 */
		public String get_LOGINPORT()
	    {
	        return LOGINPORT;
	    }
	    
		/**
		 * 设置登录用户名
		 */
	    public void set_LOGINUSERNAME(String LOGINUSERNAME) 
	    {
	        this.LOGINUSERNAME = LOGINUSERNAME;
	    }
		/**
		 * 获取登录用户名
		 */
		public String get_LOGINUSERNAME()
	    {
	        return LOGINUSERNAME;
	    }
	    
		/**
		 * 设置登录密码
		 */
	    public void set_LOGINPASSWORD(String LOGINPASSWORD) 
	    {
	        this.LOGINPASSWORD = LOGINPASSWORD;
	    }
		/**
		 * 获取登录密码
		 */
		public String get_LOGINPASSWORD()
	    {
	        return LOGINPASSWORD;
	    }
	    
}