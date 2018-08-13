package service.auth.core.padapplication;

import both.annotation.Column;
import both.annotation.Table;
import both.db.constant.ITableNameConst;

/**
 * pad功能组信息表
 * 
 */
@Table(Name = ITableNameConst.FOX_PAD_FUNCTION)
public class FOX_PAD_FUNCTION_DBO {
		/**
		 * 功能组编号
		 */
		@Column(Name="FUNC_ID")
	    private String FUNC_ID;	
		/**
		 * 功能组名称
		 */
		@Column(Name="FUNC_NAME")
	    private String FUNC_NAME;	
		/**
		 * 功能组描述
		 */
		@Column(Name="FUNC_DESCRIPTION")
	    private String FUNC_DESCRIPTION;	
		/**
		 * 路由ID
		 */
		@Column(Name="ROUTE_ID")
	    private String ROUTE_ID;	
		/**
		 * 是否功能组
		 */
		@Column(Name="ISROOT")
	    private String ISROOT;	
		/**
		 * 父ID
		 */
		@Column(Name="PARENT_ID")
	    private String PARENT_ID;	
		/**
		 * 功能组ID
		 */
		@Column(Name="FUNC_GROUP_ID")
	    private String FUNC_GROUP_ID;	
		/**
		 * 排序号
		 */
		@Column(Name="SORT_NO")
	    private String SORT_NO;	
	    
		/**
		 * 设置功能组编号
		 */
	    public void set_FUNCID(String FUNC_ID) 
	    {
	        this.FUNC_ID = FUNC_ID;
	    }
		/**
		 * 获取功能组编号
		 */
		public String get_FUNCID()
	    {
	        return FUNC_ID;
	    }
	    
		/**
		 * 设置功能组名称
		 */
	    public void set_FUNCNAME(String FUNC_NAME) 
	    {
	        this.FUNC_NAME = FUNC_NAME;
	    }
		/**
		 * 获取功能组名称
		 */
		public String get_FUNCNAME()
	    {
	        return FUNC_NAME;
	    }
	    
		/**
		 * 设置功能组描述
		 */
	    public void set_FUNCDESCRIPTION(String FUNC_DESCRIPTION) 
	    {
	        this.FUNC_DESCRIPTION = FUNC_DESCRIPTION;
	    }
		/**
		 * 获取功能组描述
		 */
		public String get_FUNCDESCRIPTION()
	    {
	        return FUNC_DESCRIPTION;
	    }
	    
		/**
		 * 设置路由ID
		 */
	    public void set_ROUTEID(String ROUTE_ID) 
	    {
	        this.ROUTE_ID = ROUTE_ID;
	    }
		/**
		 * 获取路由ID
		 */
		public String get_ROUTEID()
	    {
	        return ROUTE_ID;
	    }
	    
		/**
		 * 设置是否功能组
		 */
	    public void set_ISROOT(String ISROOT) 
	    {
	        this.ISROOT = ISROOT;
	    }
		/**
		 * 获取是否功能组
		 */
		public String get_ISROOT()
	    {
	        return ISROOT;
	    }
	    
		/**
		 * 设置父ID
		 */
	    public void set_PARENTID(String PARENT_ID) 
	    {
	        this.PARENT_ID = PARENT_ID;
	    }
		/**
		 * 获取父ID
		 */
		public String get_PARENTID()
	    {
	        return PARENT_ID;
	    }
	    
		/**
		 * 设置功能组ID
		 */
	    public void set_FUNCGROUPID(String FUNC_GROUP_ID) 
	    {
	        this.FUNC_GROUP_ID = FUNC_GROUP_ID;
	    }
		/**
		 * 获取功能组ID
		 */
		public String get_FUNCGROUPID()
	    {
	        return FUNC_GROUP_ID;
	    }
	    
		/**
		 * 设置排序号
		 */
	    public void set_SORTNO(String SORT_NO) 
	    {
	        this.SORT_NO = SORT_NO;
	    }
		/**
		 * 获取排序号
		 */
		public String get_SORTNO()
	    {
	        return SORT_NO;
	    }
	    
}