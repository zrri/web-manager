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
package both.db.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import both.bean.db.JDBCRequestBean;
import both.common.util.CollectionUtil;
import both.common.util.LoggerUtil;
import both.db.DBService;
import both.db.DbParam;
import both.db.DbParam.DbSqlType;
import both.db.constant.IDBErrorMessage;

/**
 * 数据库服务工具类
 * 
 * @author 朱劼晨
 * @data 2012-06-05
 * 
 * @history
 * 
 *          2012-12-17 董国伟 代码重构
 * 
 */
public class DbServiceUtil {
	/**
	 * 事务执行（根据参数对象集合）
	 * 
	 * @param dbParam
	 *            数据库参数实体类
	 * @return 执行结果
	 * @throws Exception
	 */
	public static int[] executeTransaction(DbParam dbParam) throws Exception {
		List<String> sqlList = new ArrayList<String>();
		List<Map<String, Object>> paraMapList = new ArrayList<Map<String, Object>>();
		// 根据参数对象集合封装事务
		executeTransactionParam(dbParam, sqlList, paraMapList);
		JDBCRequestBean bean = new JDBCRequestBean();
		// 事务非空校验后装入JDBCRequestBean
		if (sqlList != null && sqlList.size() > 0) {
			bean.setSqlList(sqlList);
			bean.setTransMapList(paraMapList);
		}
		DBService dbService = new DBService();
		return dbService.executeTransCallback(bean).getRetArray();
	}

	/**
	 * 事务SQL封装
	 * 
	 * @param dbParam
	 *            数据库参数实体类
	 * @param sqlList
	 *            事务sql语句集合
	 * @param paraMapList
	 *            事务参数集合
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private static void executeTransactionParam(DbParam dbParam,
			List<String> sqlList, List<Map<String, Object>> paraMapList)
			throws Exception {
		Map<String, Object> tempMap = new HashMap<String, Object>();
		Map<String, Object> otherTempMap = new HashMap<String, Object>();
		for (int i = 0; i < dbParam.getMainParaList().size(); i++) {
			Object mainPara = dbParam.getMainParaList().get(i);
			Object otherPara = dbParam.getOtherParaList().get(i);
			// 传入参数为自定义sql语句的事务封装
			if (mainPara instanceof String && otherPara instanceof Map<?, ?>) {
				sqlList.add((String) mainPara);
				paraMapList.add((Map<String, Object>) otherPara);
			}
			// 传入参数为操作类型的事务封装
			else if (otherPara instanceof DbSqlType) {
				// 更新单独拼装2个map
				if (otherPara.equals(DbSqlType.Update)) {
					if (!(mainPara instanceof List<?>))
						throw new Exception("传入事务参数错误");
					List<Object> tempList = (List<Object>) mainPara;
					tempMap = DbEntityUtil.getParaMap(tempList.get(0));
					otherTempMap = DbEntityUtil.getParaMap(tempList.get(1));
				}
				// 生成对象参数字典
				else
					tempMap = DbEntityUtil.getParaMap(mainPara);
				// 根据数据操作类型参数封装事务
				switch ((DbSqlType) otherPara) {
				case Insert:
					dbParam.setTableName(DbEntityUtil.getTableName(mainPara));
					dbParam.setValueMap(tempMap);
					sqlList.add(SqlBuildUtil.createInsertSql(dbParam));
					paraMapList.add(tempMap);
					break;
				case Delete:
					dbParam.setTableName(DbEntityUtil.getTableName(mainPara));
					dbParam.setWhereMap(tempMap);
					sqlList.add(SqlBuildUtil.createDeleteSql(dbParam));
					paraMapList.add(tempMap);
					break;
				case Update:
					dbParam.setTableName(DbEntityUtil
							.getTableName(((List<Object>) mainPara).get(0)));
					dbParam.setSetMap(tempMap);
					dbParam.setWhereMap(otherTempMap);
					sqlList.add(SqlBuildUtil.createUpdateSql(dbParam));
					Map<String, Object> newMap = CollectionUtil
							.getRenameKeyMap(otherTempMap,
									SqlBuildUtil.PARAUPDATE);
					tempMap.putAll(newMap);
					paraMapList.add(tempMap);
					break;
				default:
					break;
				}
			}
		}
	}

	/**
	 * 
	 * @param sql
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public static int executeUpdate(String sql, Map<String, Object> paramMap)
			throws Exception {
		JDBCRequestBean bean = new JDBCRequestBean();
		if (paramMap == null)
			paramMap = new HashMap();
		bean.setParaMap(paramMap);
		bean.setSql(sql);
		DBService dbService = new DBService();
		return dbService.executeUpdate(bean).getRet();
	}

	/**
	 * 查询 （对象实例）
	 * 
	 * @param sender
	 *            对象实例（查询条件）
	 * @return 指定类型查询结果
	 * @throws Exception
	 */
	public static List<Map<String, Object>> executeQuery(String sql,
			Map<String, Object> paraMap) throws Exception {
		JDBCRequestBean bean = new JDBCRequestBean();
		bean.setSql(sql);
		if (paraMap == null)
			paraMap = new HashMap();
		bean.setParaMap(paraMap);
		DBService dbService = new DBService();
		return dbService.executeQueryListMapWithRequiredType(bean)
				.getResultList();
	}

	/**
	 * 插入（对象实例）
	 * 
	 * @param sender
	 *            对象实例
	 * @return 插入结果
	 * @throws Exception
	 */
	public static <T> int executeInsert(T sender) throws Exception {
		DbParam dbParam = DbEntityUtil.getDbParam(sender, null,
				DbSqlType.Insert);
		JDBCRequestBean bean = new JDBCRequestBean();
		bean.setParaMap(dbParam.getValueMap());
		bean.setSql(SqlBuildUtil.createInsertSql(dbParam));
		DBService dbService = new DBService();
		return dbService.executeUpdate(bean).getRet();
	}

	/**
	 * 事务添加实体类集合
	 * 
	 * @param senderList
	 *            实体类集合
	 * @return 事务结果
	 * @throws Exception
	 */
	public static <T> int[] executeInsert(List<T> senderList) throws Exception {
		DbParam dbParam = new DbParam();
		// 循环拼装事务的Insert语句
		for (T sender : senderList)
			dbParam.add(sender, DbSqlType.Insert);
		return executeTransaction(dbParam);
	}

	/**
	 * 删除 （对象实例）
	 * 
	 * @param sender
	 *            对象实例（删除条件）
	 * @return 删除结果
	 * @throws Exception
	 */
	public static <T> int executeDelete(T sender) throws Exception {
		DbParam dbParam = DbEntityUtil.getDbParam(sender, null,
				DbSqlType.Delete);
		JDBCRequestBean bean = new JDBCRequestBean();
		bean.setParaMap(dbParam.getWhereMap());
		bean.setSql(SqlBuildUtil.createDeleteSql(dbParam));
		DBService dbService = new DBService();
		return dbService.executeUpdate(bean).getRet();
	}

	/**
	 * 事务删除实体类集合
	 * 
	 * @param senderList
	 *            实体类集合
	 * @return 事务结果
	 * @throws Exception
	 */
	public static <T> int[] executeDelete(List<T> senderList) throws Exception {
		DbParam dbParam = new DbParam();
		// 循环拼装事务的Delete语句
		for (T sender : senderList)
			dbParam.add(sender, DbSqlType.Delete);
		return executeTransaction(dbParam);
	}

	/**
	 * 更新 （对象实例）
	 * 
	 * @param setSender
	 *            对象实例（Set值）
	 * @param whereSender
	 *            对象实例（Where条件）
	 * @return 更新结果
	 * @throws Exception
	 */
	public static <T> int executeUpdate(T setSender, T whereSender)
			throws Exception {
		DbParam dbParam = DbEntityUtil.getDbParam(whereSender, setSender,
				DbSqlType.Update);
		Map<String, Object> newValMap = new HashMap<String, Object>();
		// 拼装where条件参数
		if (dbParam.getWhereMap() != null && dbParam.getWhereMap().size() > 0) {
			// where条件字段值拼装
			for (String key : dbParam.getWhereMap().keySet())
				newValMap.put(key + SqlBuildUtil.PARAUPDATE, dbParam
						.getWhereMap().get(key));
		}
		newValMap.putAll(dbParam.getSetMap());
		JDBCRequestBean bean = new JDBCRequestBean();
		bean.setParaMap(newValMap);
		bean.setSql(SqlBuildUtil.createUpdateSql(dbParam));
		DBService dbService = new DBService();
		return dbService.executeUpdate(bean).getRet();
	}

	/**
	 * 查询 （Sql语句）
	 * 
	 * @param sql
	 *            Sql语句
	 * @param cls
	 *            返回对象类型
	 * @param paraMap
	 *            参数字典
	 * @return 指定类型查询结果
	 * @throws Exception
	 */
	public static <T> List<T> executeQuery(String sql, Class<T> cls,
			Map<String, Object> paraMap) throws Exception {
		JDBCRequestBean bean = new JDBCRequestBean();
		bean.setSql(sql);
		bean.setParaMap(paraMap);
		DBService dbService = new DBService();
		return DbEntityUtil.getEntityList(dbService.executeQueryListMap(bean)
				.getResultList(), cls);
	}

	/**
	 * 查询 （Sql语句）
	 * 
	 * @param sql
	 *            Sql语句
	 * @param cls
	 *            返回对象类型
	 * @return 指定类型查询结果
	 * @throws Exception
	 */
	public static <T> List<T> executeQuery(String sql, Class<T> cls)
			throws Exception {
		JDBCRequestBean bean = new JDBCRequestBean();
		bean.setSql(sql);
		DBService dbService = new DBService();
		return DbEntityUtil.getEntityList(dbService.executeQueryListMap(bean)
				.getResultList(), cls);
	}

	/**
	 * 执行存储过程
	 * 
	 * @param trade
	 *            交易
	 * @param procedureName
	 *            存储过程名称
	 * @param paraMap
	 *            存储过程参数集合
	 * @return 返回执行结果
	 * @throws Exception
	 */
	public static List<Map<String, Object>> executeProcedureListMap(
			String procedureName, Map<String, Object> paraMap) throws Exception {
		try {
			// 存储过程名称非空校验
			if (procedureName == null || procedureName.length() == 0)
				throw new RuntimeException("存储过程名称不允许为空 ");
			JDBCRequestBean bean = new JDBCRequestBean();
			bean.setParaMap(paraMap);
			bean.setProcedureName(procedureName);
			DBService dbService = new DBService();
			return dbService.executeProcedureListMap(bean).getResultList();
		} catch (Exception e) {
			LoggerUtil.error(IDBErrorMessage.访问数据库异常, e);
			throw new Exception(IDBErrorMessage.访问数据库异常, e);
		}
	}

	/**
	 * 执行存储过程
	 * 
	 * @param trade
	 *            交易
	 * @param procedureName
	 *            存储过程名称
	 * @param paraMap
	 *            存储过程参数集合
	 * @return 返回执行结果
	 * @throws Exception
	 */
	public static Object executeFunction(String procedureName, int outType,
			Map<String, Object> paraMap) throws Exception {
		try {
			// 存储过程名称非空校验
			if (procedureName == null || procedureName.length() == 0)
				throw new RuntimeException("存储过程名称不允许为空 ");
			DBService dbService = new DBService();
			return dbService.executeFunction(procedureName, outType, paraMap);
		} catch (Exception e) {
			LoggerUtil.error(IDBErrorMessage.访问数据库异常, e);
			throw new Exception(IDBErrorMessage.访问数据库异常, e);
		}
	}

	/**
	 * 获取数据库类型
	 * 
	 * @param trade
	 *            交易
	 * @return 数据库类型
	 * @throws Exception
	 */
	public static String getDbMode() throws Exception {
		DBService dbService = new DBService();
		return dbService.detectDbType("");
	}

	/**
	 * 查询 （对象实例）
	 * 
	 * @param sender
	 *            对象实例（查询条件）
	 * @return 指定类型查询结果
	 * @throws Exception
	 */
	public static <T> List<T> executeQuery(T sender) throws Exception {
		DbParam dbParam = DbEntityUtil
				.getDbParam(sender, null, DbSqlType.Query);
		JDBCRequestBean bean = new JDBCRequestBean();
		bean.setSql(SqlBuildUtil.createQuerySql(dbParam, null));
		bean.setParaMap(dbParam.getWhereMap());
		bean.setRequiredTypeMap(CollectionUtil.getStringMap(DbEntityUtil
				.getRequiredTypeMap(sender)));
		DBService dbService = new DBService();
		return DbEntityUtil.getEntityList(dbService
				.executeQueryListMapWithRequiredType(bean).getResultList(),
				DbEntityUtil.getSenderClass(sender));
	}

	/**
	 * 查询 （对象实例）
	 * 
	 * @param sender
	 *            对象实例（查询条件）
	 * @param orderby
	 *            排序字段(数据库列名)
	 * @return 指定类型查询结果
	 * @throws Exception
	 */
	public static <T> List<T> executeQuery(T sender, String orderby)
			throws Exception {
		DbParam dbParam = DbEntityUtil
				.getDbParam(sender, null, DbSqlType.Query);
		dbParam.setOrderBy(orderby);
		JDBCRequestBean bean = new JDBCRequestBean();
		bean.setSql(SqlBuildUtil.createQuerySql(dbParam, null));
		bean.setParaMap(dbParam.getWhereMap());
		bean.setRequiredTypeMap(CollectionUtil.getStringMap(DbEntityUtil
				.getRequiredTypeMap(sender)));
		DBService dbService = new DBService();
		return DbEntityUtil.getEntityList(dbService
				.executeQueryListMapWithRequiredType(bean).getResultList(),
				DbEntityUtil.getSenderClass(sender));
	}
	
	/**
	 * 查询 （对象实例）
	 * 
	 * @param sender
	 *            对象实例（查询条件）
	 * @return 指定类型查询结果
	 * @throws Exception
	 */
	public static List<Map<String, Object>> executeQuery2Map(String sql,
			Map<String, Object> paraMap) throws Exception {
		JDBCRequestBean bean = new JDBCRequestBean();
		bean.setSql(sql);
		if (paraMap == null)
			paraMap = new HashMap<String, Object>();
		bean.setParaMap(paraMap);
		DBService dbService = new DBService();
		return dbService.executeQueryListMapWithRequiredType(bean)
				.getResultList();
	}
}
