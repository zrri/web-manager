package service.auth.core.f07;

import both.annotation.Column;
import both.annotation.Table;
import both.db.constant.ITableNameConst;

/**
 * 功能定义表
 * 
 */
// public static final String FOX_AUTH_FUNC = "FOX_AUTH_FUNC"; // 功能定义表
@Table(Name = ITableNameConst.FOX_MGR_AUTH_FUNC)
public class FOX_MGR_AUTH_FUNC_DBO {
		/**
		 * 功能编号
		 */
		@Column(Name="FUNC_ID")
	    private String FUNCID;	
		/**
		 * 功能名称
		 */
		@Column(Name="FUNC_NAME")
	    private String NAME;	
		/**
		 * html路径
		 */
		@Column(Name="URL_HTML")
	    private String HTML;	
		/**
		 * js路径
		 */
		@Column(Name="URL_JS")
	    private String JS;	
		/**
		 * css路径
		 */
		@Column(Name="URL_CSS")
	    private String CSS;	
		/**
		 * 备注
		 */
		@Column(Name="REMARK")
	    private String REMARK;	
		 /**
			 * 操作
			 */
			 @Column
		    private String OPERATOR;	
	    
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
	    
		/**
		 * 设置功能名称
		 */
	    public void set_NAME(String NAME) 
	    {
	        this.NAME = NAME;
	    }
		/**
		 * 获取功能名称
		 */
		public String get_NAME()
	    {
	        return NAME;
	    }
	    
		/**
		 * 设置html路径
		 */
	    public void set_HTML(String HTML) 
	    {
	        this.HTML = HTML;
	    }
		/**
		 * 获取html路径
		 */
		public String get_HTML()
	    {
	        return HTML;
	    }
	    
		/**
		 * 设置js路径
		 */
	    public void set_JS(String JS) 
	    {
	        this.JS = JS;
	    }
		/**
		 * 获取js路径
		 */
		public String get_JS()
	    {
	        return JS;
	    }
	    
		/**
		 * 设置css路径
		 */
	    public void set_CSS(String CSS) 
	    {
	        this.CSS = CSS;
	    }
		/**
		 * 获取css路径
		 */
		public String get_CSS()
	    {
	        return CSS;
	    }
	    
		/**
		 * 设置备注
		 */
	    public void set_REMARK(String REMARK) 
	    {
	        this.REMARK = REMARK;
	    }
		/**
		 * 获取备注
		 */
		public String get_REMARK()
	    {
	        return REMARK;
	    }
		/**
		 * 设置备注
		 */
	    public void set_OPERATOR(String OPERATOR) 
	    {
	        this.OPERATOR = OPERATOR;
	    }
		/**
		 * 获取备注
		 */
		public String get_OPERATOR()
	    {
	        return OPERATOR;
	    }
		
}