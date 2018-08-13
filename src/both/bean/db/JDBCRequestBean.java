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
/*
 * org.ailab.bean.InBean.java
 * Created by jc @ 2011-12-17 下午3:29:27
 */

package both.bean.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.bankit.phoenix.trade.IBean;

public class JDBCRequestBean implements IBean {

	/**
	 * 单条sql语句
	 */
	private String sql;

	/**
	 * 表名
	 */
	private String tableName;

	/**
	 * // * 转入参数
	 */
	private Map<String, Object> paraMap = new HashMap<String, Object>();

	/**
	 * 返回数据 单一数据
	 */
	private Map<String, Object> resultMap = new HashMap<String, Object>();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 错误信息
	 */
	private String errorMessage;

	/**
	 * 错误码
	 */
	private String errorCode;

	/**
	 * 排序字段
	 */
	private String orderby;//

	/**
	 * 更新数据库操作 列map
	 */
	private Map<String, Object> updateColMap;

	/**
	 * 更新数据库操作 条件map
	 */
	private Map<String, Object> updateConditionMap;

	/**
	 * sql语句集合（用于事务操作，和List<Map<String, Object>>结对使用）
	 */
	private List<String> sqlList;

	/**
	 * sql的参数值集合（用于事务操作，和List<String>结对使用）
	 */
	private List<Map<String, Object>> transMapList;

	/**
	 * 存储过程名称
	 */
	private String procedureName = null;

	/**
	 * 期望数据库类型
	 */
	private Map<String, String> requiredTypeMap = null;

	public Map<String, String> getRequiredTypeMap() {
		return requiredTypeMap;
	}

	public void setRequiredTypeMap(Map<String, String> requiredTypeMap) {
		this.requiredTypeMap = requiredTypeMap;
	}

	public String getProcedureName() {
		return procedureName;
	}

	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}

	public List<Map<String, Object>> getTransMapList() {
		return transMapList;
	}

	public List<String> getSqlList() {
		return sqlList;
	}

	public void setTransMapList(List<Map<String, Object>> transMapList) {
		this.transMapList = transMapList;
	}

	public void setSqlList(List<String> sqlList) {
		this.sqlList = sqlList;
	}

	public Map<String, Object> getUpdateColMap() {
		return updateColMap;
	}

	public void setUpdateColMap(Map<String, Object> updateColMap) {
		this.updateColMap = updateColMap;
	}

	public Map<String, Object> getUpdateConditionMap() {
		return updateConditionMap;
	}

	public void setUpdateConditionMap(Map<String, Object> conditionMap) {
		this.updateConditionMap = conditionMap;
	}

	public JDBCRequestBean() {
		// TODO Auto-generated constructor stub
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Map<String, Object> getParaMap() {
		return paraMap;
	}

	public void setParaMap(Map<String, Object> paraMap) {
		this.paraMap = paraMap;
	}

	public Map<String, Object> getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map<String, Object> resultMap) {
		this.resultMap = resultMap;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}
}
