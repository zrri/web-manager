package service.auth.core.f09;

import both.annotation.Column;
import both.annotation.Table;
import both.db.constant.ITableNameConst;

/**
 * 功能组与功能映射关系表
 * 
 */
// public static final String FOX_AUTH_GROUPFUNCMAPPING = "FOX_AUTH_GROUPFUNCMAPPING"; // 功能组与功能映射关系表
@Table(Name = ITableNameConst.FOX_MGR_AUTH_GROUPFUNCMAPPING)
public class FOX_MGR_AUTH_GROUPFUNCMAPPING_DBO {
		/**
		 * 功能组编号
		 */
		@Column(Name="GROUP_ID")
	    private String GROUPID;	
		/**
		 * 功能编号
		 */
		@Column(Name="FUNC_ID")
	    private String FUNCID;	
	    
		/**
		 * 设置功能组编号
		 */
	    public void set_GROUPID(String GROUPID) 
	    {
	        this.GROUPID = GROUPID;
	    }
		/**
		 * 获取功能组编号
		 */
		public String get_GROUPID()
	    {
	        return GROUPID;
	    }
	    
		/**
		 * 设置功能编号
		 */
	    public void set_FUNCID(String FUNCID) 
	    {
	        this.FUNCID = FUNCID;
	    }
		/**
		 * 获取功能编号
		 */
		public String get_FUNCID()
	    {
	        return FUNCID;
	    }
	    
}