package service.auth.core.f06;

import both.annotation.Column;
import both.annotation.Table;
import both.db.constant.ITableNameConst;

/**
 * 功能菜单树信息表
 * 
 */
// public static final String FOX_AUTH_FUNCMENUTREE = "FOX_AUTH_FUNCMENUTREE";
// // 功能菜单树信息表
@Table(Name = ITableNameConst.FOX_MGR_AUTH_FUNCMENUTREE)
public class FOX_MGR_AUTH_FUNCMENUTREE_DBO {
	/**
	 * 功能编号
	 */
	@Column(Name="FUNC_ID")
	private String FUNCID;
	/**
	 * 菜单名称
	 */
	@Column(Name="MENU_NAME")
	private String DISPLAYNAME;
	/**
	 * 节点类型（F-目录节点，L-叶子节点即菜单节点）
	 */
	@Column(Name="NODE_TYPE")
	private String NODETYPE;
	/**
	 * 排序序号（小到大排序）
	 */
	@Column(Name="SORT_NO")
	private String NODEORDER;
	/**
	 * 节点级别
	 */
	@Column(Name="NODE_LEVEL")
	private String NODELEVEL;
	/**
	 * 父节点ID
	 */
	@Column(Name="PARENT_ID")
	private String PARENTID;
	/**
	 * 菜单ID
	 */
	@Column(Name="MENU_ID")
	private String MENUID;

	/**
	 * 设置功能编号
	 */
	public void set_FUNCID(String FUNCID) {
		this.FUNCID = FUNCID;
	}

	/**
	 * 获取功能编号
	 */
	public String get_FUNCID() {
		return FUNCID;
	}

	/**
	 * 设置菜单名称
	 */
	public void set_DISPLAYNAME(String DISPLAYNAME) {
		this.DISPLAYNAME = DISPLAYNAME;
	}

	/**
	 * 获取菜单名称
	 */
	public String get_DISPLAYNAME() {
		return DISPLAYNAME;
	}

	/**
	 * 设置节点类型（F-目录节点，L-叶子节点即菜单节点）
	 */
	public void set_NODETYPE(String NODETYPE) {
		this.NODETYPE = NODETYPE;
	}

	/**
	 * 获取节点类型（F-目录节点，L-叶子节点即菜单节点）
	 */
	public String get_NODETYPE() {
		return NODETYPE;
	}

	/**
	 * 设置排序序号（小到大排序）
	 */
	public void set_NODEORDER(String NODEORDER) {
		this.NODEORDER = NODEORDER;
	}

	/**
	 * 获取排序序号（小到大排序）
	 */
	public String get_NODEORDER() {
		return NODEORDER;
	}

	/**
	 * 设置节点级别
	 */
	public void set_NODELEVEL(String NODELEVEL) {
		this.NODELEVEL = NODELEVEL;
	}

	/**
	 * 获取节点级别
	 */
	public String get_NODELEVEL() {
		return NODELEVEL;
	}

	/**
	 * 设置父节点ID
	 */
	public void set_PARENTID(String PARENTID) {
		this.PARENTID = PARENTID;
	}

	/**
	 * 获取父节点ID
	 */
	public String get_PARENTID() {
		return PARENTID;
	}

	/**
	 * 设置菜单ID
	 */
	public void set_MENUID(String MENUID) {
		this.MENUID = MENUID;
	}

	/**
	 * 获取菜单ID
	 */
	public String get_MENUID() {
		return MENUID;
	}

}