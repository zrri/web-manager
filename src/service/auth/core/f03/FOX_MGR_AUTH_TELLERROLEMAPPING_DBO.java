package service.auth.core.f03;

import both.annotation.Column;
import both.annotation.Table;
import both.db.constant.ITableNameConst;

/**
 * 用户与角色映射关系表
 * 
 */
// public static final String FOX_AUTH_USERROLEMAPPING = "FOX_AUTH_USERROLEMAPPING"; // 用户与角色映射关系表
@Table(Name = ITableNameConst.FOX_MGR_AUTH_TELLERROLEMAPPING)
public class FOX_MGR_AUTH_TELLERROLEMAPPING_DBO {
		/**
		 * 用户代号
		 */
		@Column(Name="TELLER_ID")
	    private String USERID;	
		/**
		 * 角色号
		 */
		@Column(Name="ROLE_ID")
	    private String ROLEID;	
	    
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
		 * 设置角色号
		 */
	    public void set_ROLEID(String ROLEID) 
	    {
	        this.ROLEID = ROLEID;
	    }
		/**
		 * 获取角色号
		 */
		public String get_ROLEID()
	    {
	        return ROLEID;
	    }
	    
}