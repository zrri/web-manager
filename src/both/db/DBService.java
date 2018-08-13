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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



import both.bean.db.JDBCRequestBean;
import both.bean.db.JDBCResultBean;
import both.common.util.CollectionUtil;
import both.common.util.LoggerUtil;
import both.db.constant.IDBErrorMessage;

import cn.com.bankit.phoenix.jdbc.tool.DBAccessor;
import cn.com.bankit.phoenix.trade.Service;

/**
 * 提供公用的数据库操作
 * 
 * @author linli,王洪桥
 * @date 2012-11-01
 * 
 * @history 2012-12-17 董国伟 代码重构
 */
public class DBService extends Service<JDBCRequestBean, JDBCResultBean> {

	/**
	 * 数据库访问管理器
	 */
	public static DBAccessor dbAccessor;

	static {
		dbAccessor = DBAccessor.getDBAccessor("phoenix");
	}
	
	/**
	 * 获取数据库连接器
	 * @return
	 */
	public static DBAccessor getJdbcTool() {
		return dbAccessor;
	}

	@Override
	public JDBCResultBean execute(JDBCRequestBean arg0) throws Exception {

		return null;
	}

	/**
	 * 获取数据库类型
	 * 
	 * @return 数据库类型
	 */
	public String detectDbType(String test) {
		Connection connection = null;
		try {
			connection = dbAccessor.getConnection();
			DatabaseMetaData metaData = connection.getMetaData();
			String name = metaData.getDatabaseProductName();
			if (name.toUpperCase().contains("MYSQL")) {
				return "MySQL";
			} else if (name.toUpperCase().contains("ORACLE")) {
				return "ORACLE";
			} else if (name.contains("Microsoft")) {
				return "MSSQL";
			} else if (name.toUpperCase().contains("DB2")) {
				return "DB2";
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	/**
	 * 执行增、删、改操作(单条sql语句)
	 * 
	 * @param bean
	 *            JDBCBean参数集合
	 * @return （ret 影响行数目）
	 * @throws Exception
	 */
	public JDBCResultBean executeUpdate(JDBCRequestBean bean) throws Exception {
		try {
			JDBCResultBean outBean = new JDBCResultBean();
			int ret = 0;
			LoggerUtil.debug("SQL Start...");
			LoggerUtil.debug(String.format("SQL=[%s]", bean.getSql()));
			LoggerUtil.debug(String.format("PARAM=[%s]", bean.getParaMap()
					.toString()));
			if (bean.getParaMap() == null || bean.getParaMap().isEmpty())
				ret = dbAccessor.update(bean.getSql());
			else
				ret = dbAccessor.update(bean.getSql(), bean.getParaMap());
			LoggerUtil.debug(String.format("SQL End [%d]\n", ret));
			outBean.setRet(ret);
			return outBean;
		} catch (Exception e) {
			LoggerUtil.error(IDBErrorMessage.访问数据库异常, e);
			bean.setErrorMessage(IDBErrorMessage.访问数据库异常);
			throw new Exception(IDBErrorMessage.访问数据库异常);
		}
	}

	/**
	 * 执行查询操作（单条sql语句）
	 * 
	 * @param bean
	 *            JDBCBean参数集合
	 * @return 单条map查询结果
	 * @throws Exception
	 */
	public JDBCResultBean executeQueryMap(JDBCRequestBean bean)
			throws Exception {
		try {
			JDBCResultBean outBean = new JDBCResultBean();
			List<Map<String, Object>> resultMap = dbAccessor.query(
					bean.getSql(), bean.getParaMap(), null);
			if (resultMap.size() > 0) {
				outBean.setResultMap(resultMap.get(0));
			}
			return outBean;
		} catch (Exception e) {
			LoggerUtil.error(IDBErrorMessage.访问数据库异常, e);
			bean.setErrorMessage(IDBErrorMessage.访问数据库异常);
			throw new Exception(IDBErrorMessage.访问数据库异常);
		}
	}

	/**
	 * 执行查询操作（单条sql语句）
	 * 
	 * @param bean
	 *            JDBCBean参数集合
	 * @return 多条list查询结果
	 * @throws Exception
	 */
	public JDBCResultBean executeQueryListMap(JDBCRequestBean bean)
			throws Exception {
		JDBCResultBean outBean = new JDBCResultBean();
		try {
			LoggerUtil.info("executeQueryListMap 执行 : " + bean.getSql());
			LoggerUtil.debug("executeQueryListMap "
					+ bean.getParaMap().toString());
			List<Map<String, Object>> resultList = dbAccessor.query(
					bean.getSql(), bean.getParaMap(), null);
			LoggerUtil.debug("resultList :" + resultList.size());
			outBean.setResultList(resultList);
			return outBean;
		} catch (Exception e) {
			LoggerUtil.error(IDBErrorMessage.访问数据库异常, e);
			LoggerUtil.error(String.format("SQL Start =[%s]", bean.getSql()));
			LoggerUtil.error("executeQueryMap " + bean.getParaMap().toString());
			outBean.setErrorMessage(IDBErrorMessage.访问数据库异常);
			throw new Exception(IDBErrorMessage.访问数据库异常);
		}
	}

	/**
	 * 执行事务操作（多条sql语句，不支持返回值和回滚）
	 * 
	 * @param bean
	 *            JDBCBean参数集合
	 * @return 事务执行结果
	 * @throws Exception
	 */
	// public JDBCBean executeTransRunnable(final JDBCBean bean) throws
	// Exception {
	// try {
	// dbAccessor.doInTransaction(new Runnable() {
	//
	// public void run() {
	// List<String> sqlList = bean.getSqlList();
	// if (sqlList != null && sqlList.size() > 0)
	// // 循环执行sql语句
	// for (String sql : sqlList)
	// dbAccessor.update(sql);
	// }
	// });
	// return bean;
	// } catch (Exception e) {
	// LoggerUtil.error(IErrorMessage.访问数据库异常, e);
	// bean.setErrorMessage(IErrorMessage.访问数据库异常);
	// throw new Exception(IErrorMessage.访问数据库异常);
	// }
	// }

	/**
	 * 执行事务操作（多条sql语句，不支持返回值和回滚）
	 * 
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public JDBCResultBean executeTransRunnable(JDBCRequestBean bean)
			throws Exception {
		JDBCResultBean outBean = new JDBCResultBean();
		try {
			List<String> sqlList = bean.getSqlList();
			dbAccessor.batchExecInTransaction(sqlList);
			return outBean;
		} catch (Exception e) {
			LoggerUtil.error(IDBErrorMessage.访问数据库异常, e);
			outBean.setErrorMessage(IDBErrorMessage.访问数据库异常);
			throw new Exception(IDBErrorMessage.访问数据库异常);
		}
	}

	/**
	 * 执行事务操作（多条sql语句,支持返回值和回滚）
	 * 
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public JDBCResultBean executeTransCallback(final JDBCRequestBean bean)
			throws Exception {
		JDBCResultBean outBean = new JDBCResultBean();
		try {
			final List<String> sqlList = bean.getSqlList();
			for (Iterator iterator = sqlList.iterator(); iterator.hasNext();) {
				String string = (String) iterator.next();
				LoggerUtil.info(string);
			}
			final List<Map<String, Object>> mapList = bean.getTransMapList();
			if (sqlList == null || sqlList.size() == 0) {
				return null;
			}

			if (mapList == null)// 无参数sql
				outBean.setRetArray(dbAccessor.batchExecInTransaction(sqlList));
			else {// 有参数sql
				if (sqlList.size() == mapList.size()) {
					final int[] retArray = dbAccessor.batchExecInTransaction(
							sqlList, mapList);
					outBean.setRetArray(retArray);
				}
			}
			return outBean;
		} catch (Exception e) {
			LoggerUtil.error(IDBErrorMessage.访问数据库异常, e);
			outBean.setErrorMessage(IDBErrorMessage.访问数据库异常);
			throw new Exception(IDBErrorMessage.访问数据库异常);
		}
	}

	/**
	 * 执行存储过程
	 * 
	 * @param bean
	 *            JDBCBean参数集合
	 * @return 返回影响的行数
	 * @throws Exception
	 */
	public JDBCResultBean executeProcedure(JDBCRequestBean bean)
			throws Exception {
		JDBCResultBean outBean = new JDBCResultBean();
		try {
			Map<String, Object> retMap = getResultByProcedure(
					bean.getProcedureName(), bean.getParaMap());
			int ret = -1;
			// 存储过程参数传入
			if (retMap != null && retMap.size() > 0)
				for (Object key : retMap.keySet())
					ret = (Integer) retMap.get(key);
			outBean.setRet(ret);
		} catch (Exception e) {
			LoggerUtil.error(IDBErrorMessage.访问数据库异常, e);
			outBean.setErrorMessage(IDBErrorMessage.访问数据库异常);
			throw new Exception(IDBErrorMessage.访问数据库异常);
		}
		return outBean;
	}

	/**
	 * 执行存储过程
	 * 
	 * @param bean
	 *            JDBCBean参数集合
	 * @return 返回影响的行数
	 * @throws Exception
	 */
	public JDBCResultBean executeProcedureMap(JDBCRequestBean bean)
			throws Exception {
		JDBCResultBean outBean = new JDBCResultBean();
		try {
			bean.setResultMap(getResultByProcedure(bean.getProcedureName(),
					bean.getParaMap()));
		} catch (Exception e) {
			LoggerUtil.error(IDBErrorMessage.访问数据库异常, e);
			outBean.setErrorMessage(IDBErrorMessage.访问数据库异常);
			throw new Exception(IDBErrorMessage.访问数据库异常);
		}
		return outBean;
	}

	/**
	 * 执行存储过程
	 * 
	 * @param bean
	 *            JDBCBean参数集合
	 * @return 返回影响的行数
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public JDBCResultBean executeProcedureListMap(JDBCRequestBean bean)
			throws Exception {
		JDBCResultBean outBean = new JDBCResultBean();
		try {
			Map<String, Object> resultMap = getResultByProcedure(
					bean.getProcedureName(), bean.getParaMap());
			List<Map<String, Object>> resultList = null;
			if (resultMap != null && resultMap.size() > 0)
				for (Object key : resultMap.keySet())
					resultList = (List<Map<String, Object>>) resultMap.get(key);
			outBean.setResultList(resultList);
		} catch (Exception e) {
			LoggerUtil.error(IDBErrorMessage.访问数据库异常, e);
			outBean.setErrorMessage(IDBErrorMessage.访问数据库异常);
			throw new Exception(IDBErrorMessage.访问数据库异常);
		}
		return outBean;
	}

	/**
	 * 执行存储过程
	 * 
	 * @param procedureName
	 *            存储过程名称
	 * @param paraMap
	 *            存储过程参数
	 * @return 存储过程执行结果
	 * 
	 * @throws Exception
	 */
	private Map<String, Object> getResultByProcedure(String procedureName,
			Map<String, Object> paraMap) throws Exception {
		try {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			// 有参数的存储过程执行
			if (paraMap == null)
				paraMap = new HashMap<String, Object>();
			dbAccessor.callStoredProcedure(procedureName, paraMap, resultMap);
			return resultMap;
		} catch (Exception e) {
			LoggerUtil.error(IDBErrorMessage.访问数据库异常, e);
			throw new Exception(IDBErrorMessage.访问数据库异常);
		}
	}

	/**
	 * 执行存储过程
	 * 
	 * @param procedureName
	 *            存储过程名称
	 * @param paraMap
	 *            存储过程参数
	 * @return 存储过程执行结果
	 * 
	 * @throws Exception
	 */
	public Object executeFunction(String functionName, int outType,
			Map<String, Object> paraMap) throws Exception {
		try {
			// 有参数的存储过程执行
			if (paraMap == null)
				paraMap = new HashMap<String, Object>();
			Object ret = dbAccessor
					.callFunction(functionName, outType, paraMap);
			return ret;
		} catch (Exception e) {
			LoggerUtil.error(IDBErrorMessage.访问数据库异常, e);
			throw new Exception(IDBErrorMessage.访问数据库异常);
		}
	}

	/**
	 * 查询操作（返回期望数据类型）
	 * 
	 * @param bean
	 *            JDBCBean参数集合
	 * @return 查询结果集合
	 * @throws Exception
	 */
	public JDBCResultBean executeQueryListMapWithRequiredType(
			JDBCRequestBean bean) throws Exception {
		JDBCResultBean outBean = new JDBCResultBean();
		try {
			LoggerUtil.info("executeQueryListMapWithRequiredType 执行："
					+ bean.getSql());
			LoggerUtil.info("executeQueryListMapWithRequiredType 参数："
					+ bean.getParaMap());
			List<Map<String, Object>> resultList = dbAccessor.query(bean
					.getSql(), bean.getParaMap(), CollectionUtil
					.getClassMapUpperKey(bean.getRequiredTypeMap()));
			outBean.setResultList(resultList);
		} catch (Exception e) {
			LoggerUtil.error(IDBErrorMessage.访问数据库异常, e);
			outBean.setErrorMessage(IDBErrorMessage.访问数据库异常);
			throw new Exception(IDBErrorMessage.访问数据库异常);
		}
		return outBean;
	}

	public JDBCResultBean executeQuery2Map(JDBCRequestBean bean)
			throws Exception {
		JDBCResultBean outBean = new JDBCResultBean();
		try {
			LoggerUtil.info("executeQuery2Map 执行 : " + bean.getSql());
			LoggerUtil
					.debug("executeQuery2Map " + bean.getParaMap().toString());
			List<Map<String, Object>> resultList = dbAccessor.query(
					bean.getSql(), bean.getParaMap(), null);
			String sql = bean.getSql().toUpperCase();
			String mapvalue = sql.substring(sql.indexOf("SELECT") + 6,
					sql.indexOf("FROM"));
			mapvalue = mapvalue.trim();
			LoggerUtil.info(mapvalue);
			Map<String, Object> retMap = new HashMap<String, Object>();
			for (Map<String, Object> map : resultList) {
				retMap.put((String) map.get(mapvalue.split(",")[0].trim()),
						map.get(mapvalue.split(",")[1].trim()));
			}
			outBean.setResultMap(retMap);

		} catch (Exception e) {
			LoggerUtil.error(IDBErrorMessage.访问数据库异常, e);
			LoggerUtil.error(String.format("SQL Start =[%s]", bean.getSql()));
			LoggerUtil.error("executeQueryMap " + bean.getParaMap().toString());
			outBean.setErrorMessage(IDBErrorMessage.访问数据库异常);
			throw new Exception(IDBErrorMessage.访问数据库异常);
		}
		return outBean;
	}
	
	/**
	 * 使用PrepareStatement执行批处理
	 * 
	 * @param tempSql
	 *            模板sql语句（批处理按照此模板进行执行），需要回填数据的Values用“?”表示。 如：insert into
	 *            tableName (columnName) values(?);
	 * @param datas
	 *            数据，List<List> 数据集合，List<String> 单条数据，和tempSql需要赋值的数据一一对应
	 *            SUCCESS_NO_INFO： -2
	 *            EXECUTE_FAILED： -3
	 *            @see Statement
	 * @param batchSize
	 *            batch池size，当池中的sql条数到达指定值时，进行execute。-1表示一次性执行所有数据
	 * @return
	 * @throws Exception
	 */
	public int[] executeBatch(String tempSql, List<List<String>> datas,int batchSize) throws Exception{
		// 连接
		Connection con = null;
		PreparedStatement stmt = null;
		int[] rets = new int[0];
		int index = 0; // 记录数据异常
		try {
			// 获取连接
			con = dbAccessor.getConnection();
			// 创建statement
			stmt = con.prepareStatement(tempSql);
			// 加入批量执行队列
			int count = 0;
			for (int i = 0, size = datas.size(); i < size; i++) {
				count++;
				index++;
				List<String> list = datas.get(i);
				for (int j = 0; j < list.size(); j++) {
					stmt.setString(j + 1, list.get(j));
				}
				stmt.addBatch();
				if (batchSize>0 && count % batchSize == 0) {
					int[] batchRes = stmt.executeBatch();
					LoggerUtil.debug("测试结果：" + Arrays.toString(batchRes));
					// 追加
					rets = arrayAppend(rets, batchRes);
					con.commit();
					stmt.clearBatch();
				}
			}
			// 批量执行SQL
			int[] res = stmt.executeBatch();
			rets = arrayAppend(rets, res);
			return rets;
		} catch (Exception e) {
			LoggerUtil.error("批处理执行异常：" + tempSql + "effect values：" + datas.get(index) + e.getMessage());
			throw e;
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					LoggerUtil.error(e.getMessage(), e);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					LoggerUtil.error(e.getMessage(), e);
				}
			}
		}
	}
	/**
	 * 数组追加
	 * 
	 * @param destination 目标数组
	 * @param source 源数组（追加到目标数组中）
	 * @return
	 */
	private int[] arrayAppend(int[] destination, int[] source) {
		int[] temp = new int[destination.length + source.length];
		System.arraycopy(destination, 0, temp, 0, destination.length);
		System.arraycopy(source, 0, temp, destination.length, source.length);
		return temp;
	}
	
	public static int executeInsertSql(String sql, Object[] obj)
			throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		long a = System.currentTimeMillis();
		try {
			connection = getJdbcTool().getConnection();
			statement = connection.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			for (int i = 0; i < obj.length; i++) {
				statement.setObject(i + 1, obj[i]);
			}
			statement.execute();
			rs = statement.getGeneratedKeys();
			if (rs != null && rs.next()) {
				return 1;
			} else {
				throw new Exception("不存在自增值");
			}
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(),e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
				}
			}
			long b = System.currentTimeMillis();
		}
	}

	/**
	 * 查询
	 * @param sql
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> executeQuery(String sql,
			Map<String, Object> map) throws Exception {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		long a = System.currentTimeMillis();
		dataList = getJdbcTool().query(sql, map, null);
		long b = System.currentTimeMillis();
		LoggerUtil.debug("executeQuery===sql:[sql:" + sql + "][map:" + map
				+ "]");
		return dataList;
	}

	/**
	 * 查询
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> executeQuery(String sql)
			throws Exception {
		return executeQuery(sql, new HashMap<String, Object>());
	}
	
	/**
	 * 更新
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public static int update(String sql)
			throws Exception {
		long a = System.currentTimeMillis();
		int size = getJdbcTool().update(sql);
		long b = System.currentTimeMillis();
		LoggerUtil.debug("update===sql:[sql:" + sql + "]");
		return size;
	}
	
	/**
	 * 更新
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public static boolean execute(String sql)
			throws Exception {
		long a = System.currentTimeMillis();
		boolean res = getJdbcTool().execute(sql);
		long b = System.currentTimeMillis();
		LoggerUtil.debug("update===sql:[sql:" + sql + "]");
		return res;
	}


}
