package service.cm.core.db.NodeInfo;

import both.annotation.Column;
import both.annotation.Table;
import service.cm.core.db.*;


/**
 * 节点信息表
 * 
 */
// public static final String FOX_CM_NODEINFO = "FOX_CM_NODEINFO"; // 节点信息表
@Table(Name = ICMConst.FOX_MGR_CM_NODEINFO)
public class FOX_MGR_CM_NODEINFO_DBO {
		/**
		 * 主机IP
		 */
		@Column(Name="HOST_IP")
	    private String HOSTIP;	
		/**
		 * 节点类型
		 */
		@Column(Name="NODE_TYPE")
	    private String NODETYPE;	
		/**
		 * 应用路径
		 */
		@Column(Name="APPLY_PATH")
	    private String APPPATH;	
		/**
		 * 节点名称
		 */
		@Column(Name="NODE_NAME")
	    private String NAME;	
		/**
		 * 描述
		 */
		@Column(Name="DESCRIPTION")
	    private String DESCRIPTION;	
		/**
		 * 是否软连接
		 */
		@Column(Name="ISLINK")
	    private String ISLINK;	
		/**
		 * 软连接目录
		 */
		@Column(Name="LINK_DIRECTORY")
	    private String LINKDIRECTORY;	
		/**
		 * 更新目录
		 */
		@Column(Name="UPDATE_DIRECTORY")
	    private String UPDATEDIRECTORY;	
		/**
		 * 应用端口
		 */
		@Column(Name="APPLY_PORT")
	    private String APPPORT;	
		/**
		 * HTTP端口
		 */
		@Column(Name="HTTP_PORT")
	    private String HTTPPORT;	
		/**
		 * JVM端口
		 */
		@Column(Name="JVM_PORT")
	    private String JVMPORT;	
	    
		/**
		 * 设置主机IP
		 */
	    public void set_HOSTIP(String HOSTIP) 
	    {
	        this.HOSTIP = HOSTIP;
	    }
		/**
		 * 获取主机IP
		 */
		public String get_HOSTIP()
	    {
	        return HOSTIP;
	    }
	    
		/**
		 * 设置节点类型
		 */
	    public void set_NODETYPE(String NODETYPE) 
	    {
	        this.NODETYPE = NODETYPE;
	    }
		/**
		 * 获取节点类型
		 */
		public String get_NODETYPE()
	    {
	        return NODETYPE;
	    }
	    
		/**
		 * 设置应用路径
		 */
	    public void set_APPPATH(String APPPATH) 
	    {
	        this.APPPATH = APPPATH;
	    }
		/**
		 * 获取应用路径
		 */
		public String get_APPPATH()
	    {
	        return APPPATH;
	    }
	    
		/**
		 * 设置节点名称
		 */
	    public void set_NAME(String NAME) 
	    {
	        this.NAME = NAME;
	    }
		/**
		 * 获取节点名称
		 */
		public String get_NAME()
	    {
	        return NAME;
	    }
	    
		/**
		 * 设置描述
		 */
	    public void set_DESCRIPTION(String DESCRIPTION) 
	    {
	        this.DESCRIPTION = DESCRIPTION;
	    }
		/**
		 * 获取描述
		 */
		public String get_DESCRIPTION()
	    {
	        return DESCRIPTION;
	    }
	    
		/**
		 * 设置是否软连接
		 */
	    public void set_ISLINK(String ISLINK) 
	    {
	        this.ISLINK = ISLINK;
	    }
		/**
		 * 获取是否软连接
		 */
		public String get_ISLINK()
	    {
	        return ISLINK;
	    }
	    
		/**
		 * 设置软连接目录
		 */
	    public void set_LINKDIRECTORY(String LINKDIRECTORY) 
	    {
	        this.LINKDIRECTORY = LINKDIRECTORY;
	    }
		/**
		 * 获取软连接目录
		 */
		public String get_LINKDIRECTORY()
	    {
	        return LINKDIRECTORY;
	    }
	    
		/**
		 * 设置更新目录
		 */
	    public void set_UPDATEDIRECTORY(String UPDATEDIRECTORY) 
	    {
	        this.UPDATEDIRECTORY = UPDATEDIRECTORY;
	    }
		/**
		 * 获取更新目录
		 */
		public String get_UPDATEDIRECTORY()
	    {
	        return UPDATEDIRECTORY;
	    }
	    
		/**
		 * 设置应用端口
		 */
	    public void set_APPPORT(String APPPORT) 
	    {
	        this.APPPORT = APPPORT;
	    }
		/**
		 * 获取应用端口
		 */
		public String get_APPPORT()
	    {
	        return APPPORT;
	    }
	    
		/**
		 * 设置HTTP端口
		 */
	    public void set_HTTPPORT(String HTTPPORT) 
	    {
	        this.HTTPPORT = HTTPPORT;
	    }
		/**
		 * 获取HTTP端口
		 */
		public String get_HTTPPORT()
	    {
	        return HTTPPORT;
	    }
	    
		/**
		 * 设置JVM端口
		 */
	    public void set_JVMPORT(String JVMPORT) 
	    {
	        this.JVMPORT = JVMPORT;
	    }
		/**
		 * 获取JVM端口
		 */
		public String get_JVMPORT()
	    {
	        return JVMPORT;
	    }
	    
}