package service.auth.core.f08;

import both.annotation.Column;
import both.annotation.Table;
import both.db.constant.ITableNameConst;

/**
 * 角色与功能映射表
 * 
 */
// public static final String FOX_AUTH_ROLEFUNCMAPPING = "FOX_AUTH_ROLEFUNCMAPPING"; // 角色与功能映射表
@Table(Name = ITableNameConst.FOX_MGR_AUTH_ROLEFUNCMAPPING)
public class FOX_MGR_AUTH_ROLEFUNCMAPPING_DBO {
		/**
		 * 角色编号
		 */
		@Column(Name="ROLE_ID")
	    private String ROLEID;	
		/**
		 * 功能编号
		 */
		@Column(Name="FUNC_ID")
	    private String FUNCID;	
	    
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