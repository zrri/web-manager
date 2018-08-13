/**
 * Special Declaration: These technical material reserved as the technical 
 * secrets by Bankit TECHNOLOGY have been protected by the "Copyright Law" 
 * "ordinances on Protection of Computer Software" and other relevant 
 * administrative regulations and international treaties. Without the written 
 * permission of the Company, no person may use (including but not limited to 
 * the illegal copy, distribute, display, image, upload, and download) and 
 * disclose the above technical documents to any third party. Otherwise, any 
 * infringer shall afford the legal liability to the company.
 *
 * 特别声明：本技术材料受《中华人民共和国著作权法》、《计算机软件保护条例》
 * 等法律、法规、行政规章以及有关国际条约的保护，浙江宇信班克信息技术有限公
 * 司享有知识产权、保留一切权利并视其为技术秘密。未经本公司书面许可，任何人
 * 不得擅自（包括但不限于：以非法的方式复制、传播、展示、镜像、上载、下载）使
 * 用，不得向第三方泄露、透露、披露。否则，本公司将依法追究侵权者的法律责任。
 * 特此声明！
 *
 * Copyright(C) 2012 Bankit Tech, All rights reserved.
 */
package both.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * SQL参数实体类
 * 
 * @author 董国伟
 * @date 2012-11-01
 * 
 * @history
 * 
 * 2012-11-23 董国伟 代码重构 
 */
public class DbParam
{
	protected List<Object> mainParaList;
	protected List<Object> otherParaList;

	private String tableName;// 表名
	private String orderBy;// 排序字段
	private String topConut;// 查询限制数量
	private String sqlString;// sql语句
	private Integer pageSize;// 分页查询的页面显示数量
	private Integer pageNum;// 分页查询的第几页

	private Map<String, Object> whereMap;// 条件参数map
	private Map<String, Object> columnMap;// 表字段map
	private Map<String, Object> valueMap;// 更新字段值map
	private Map<String, Object> setMap;// 更新字段名map
	Map<String, Class<?>> requiredTypeMap;// 表字段类型

	/**
	 * 数据库操作类型 Insert 增 Delete 删 Update 改 Query 查
	 * 
	 */
	public enum DbSqlType
	{
		Insert, Delete, Update, Query
	}

	/**
	 * 初始化
	 * 
	 */
	public DbParam()
	{
		mainParaList = new ArrayList<Object>();
		otherParaList = new ArrayList<Object>();
	}

	/**
	 * 添加事务参数
	 * 
	 * @param sender
	 *            事务实体类实例
	 * @param sqlType
	 *            事务操作类型
	 */
	public void add(Object sender, DbSqlType sqlType)
	{
		mainParaList.add(sender);
		otherParaList.add(sqlType);
	}

	/**
	 * 添加sql和对应的paraMap
	 * 
	 * @param sql
	 *            例如： "delete from tellerinfo where TELLERID=:TELLERIDKEY"
	 * @param paraMap
	 *            对应的：paraMap("TELLERIDKEY","11005275")
	 */
	public void add(String sql, Map<String, Object> paraMap)
	{
		mainParaList.add(sql);
		otherParaList.add(paraMap);
	}

	/**
	 * 添加事务参数(Update)
	 * 
	 * @param setSender
	 *            对象实例（Set值）
	 * @param whereSender
	 *            对象实例（Where条件）
	 * @param sqlType
	 *            目前必须传SqlType.Update
	 * @throws Exception
	 */
	public void add(Object setSender, Object whereSender, DbSqlType sqlType)
	{
		List<Object> senderList = new ArrayList<Object>();
		senderList.add(setSender);
		senderList.add(whereSender);
		mainParaList.add(senderList);
		otherParaList.add(DbSqlType.Update);
	}

	/**
	 * 事务sql语句集合取值
	 * 
	 * @return 事务sql语句集合
	 */
	public List<Object> getMainParaList()
	{
		return mainParaList;
	}

	/**
	 * 事务参数集合取值
	 * 
	 * @return 事务参数集合
	 */
	public List<Object> getOtherParaList()
	{
		return otherParaList;
	}

	/**
	 * 表名取值
	 * 
	 * @return 表名
	 */
	public String getTableName()
	{
		return tableName;
	}

	/**
	 * 表名赋值
	 * 
	 * @param tableName
	 *            表名
	 */
	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	/**
	 * 条件参数集合取值
	 * 
	 * @return 条件参数集合
	 */
	public Map<String, Object> getWhereMap()
	{
		return whereMap;
	}

	/**
	 * 条件参数集合赋值
	 * 
	 * @param whereMap
	 *            条件参数
	 */
	public void setWhereMap(Map<String, Object> whereMap)
	{
		this.whereMap = whereMap;
	}

	/**
	 * 表字段集合取值
	 * 
	 * @return 表字段集合
	 */
	public Map<String, Object> getColumnMap()
	{
		return columnMap;
	}

	/**
	 * 表字段集合赋值
	 * 
	 * @param columnMap
	 *            表字段集合
	 */
	public void setColumnMap(Map<String, Object> columnMap)
	{
		this.columnMap = columnMap;
	}

	/**
	 * 更新字段值取值
	 * 
	 * @return 更新字段值
	 */
	public Map<String, Object> getValueMap()
	{
		return valueMap;
	}

	/**
	 * 更新字段值赋值
	 * 
	 * @param valueMap
	 *            更新字段值
	 */
	public void setValueMap(Map<String, Object> valueMap)
	{
		this.valueMap = valueMap;
	}

	/**
	 * 更新字段名取值
	 * 
	 * @return 更新字段名
	 */
	public Map<String, Object> getSetMap()
	{
		return setMap;
	}

	/**
	 * 更新字段名赋值
	 * 
	 * @param setMap
	 *            更新字段名
	 */
	public void setSetMap(Map<String, Object> setMap)
	{
		this.setMap = setMap;
	}

	/**
	 * 排序字段取值
	 * 
	 * @return 排序字段值
	 */
	public String getOrderBy()
	{
		return orderBy;
	}

	/**
	 * 排序字段赋值
	 * 
	 * @param orderBy
	 *            排序字段值
	 */
	public void setOrderBy(String orderBy)
	{
		this.orderBy = orderBy;
	}

	/**
	 * 查询限制数量取值
	 * 
	 * @return 查询限制数量
	 * 
	 */
	public String getTopConut()
	{
		return topConut;
	}

	/**
	 * 查询限制数量赋值
	 * 
	 * @param topConut
	 *            查询限制数量
	 */
	public void setTopConut(String topConut)
	{
		this.topConut = topConut;
	}

	/**
	 * 自定义Sql语句赋值
	 * 
	 * @return 自定义Sql语句
	 */
	public String getSqlString()
	{
		return sqlString;
	}

	/**
	 * 自定义Sql语句取值
	 * @param sqlString
	 *         自定义sql语句
	 */
	public void setSqlString(String sqlString)
	{
		this.sqlString = sqlString;
	}

	/**
	 * 表字段类型取值
	 * @return 表字段类型
	 */
	public Map<String, Class<?>> getRequiredTypeMap()
	{
		return requiredTypeMap;
	}

	/**
	 * 表字段类型赋值
	 * @param requiredTypeMap
	 *         表字段类型
	 */
	public void setRequiredTypeMap(Map<String, Class<?>> requiredTypeMap)
	{
		this.requiredTypeMap = requiredTypeMap;
	}

	/**
	 * 分页查询的页数取值
	 * @return 分页查询的页数
	 */
	public Integer getPageSize()
	{
		return pageSize;
	}

	/**
	 * 分页查询的页数取值
	 * @param pageSize
	 *          分页查询的页数
	 */
	public void setPageSize(Integer pageSize)
	{
		this.pageSize = pageSize;
	}

	/**
	 * 分页查询的每页条数取值
	 * @return 每页条数
	 */
	public Integer getPageNum()
	{
		return pageNum;
	}

	/**
	 * 分页查询的每页条数赋值
	 * @param pageNum
	 *         每页条数
	 */
	public void setPageNum(Integer pageNum)
	{
		this.pageNum = pageNum;
	}
}
