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
 * Copyright(C) 2011 Bankit Tech, All rights reserved.
 *
 */
package both.db.util;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;

import both.annotation.Column;
import both.annotation.Table;
import both.common.util.CollectionUtil;
import both.common.util.LoggerUtil;
import both.common.util.Requires;
import both.common.util.StringUtilEx;
import both.db.DbParam;
import both.db.constant.DbModeConst;
import both.db.constant.IDBErrorMessage;

/**
 * SQL语句构造工具类
 * 
 * @author 周洲
 * @date 2012-11-21
 * 
 * 
 * @history 2013—01—08 董国伟 将原先DbClient封装的SqlBuildUtil方法并入 2013-01-21 郭婷
 *          修改buildStringFieldCondition的参数为sqlBuilder
 * 
 */
public class SqlBuildUtil {

	public SqlBuildUtil() {

	}

	/**
	 * 构建String类型字段条件（逻辑与/等值判断/字段名参数名相同）
	 * 
	 * @param whereClause
	 *            待构建SQL条件子句
	 * @param paramMap
	 *            待构建条件参数
	 * @param fieldValue
	 *            字段值
	 * @param fieldName
	 *            字段名称
	 */
	public static void buildStringFieldCondition(StringBuilder whereClause,
			Map<String, Object> paramMap, String fieldValue, String fieldName) {
		if (fieldValue != null)
			buildStringFieldCondition(whereClause, paramMap, "AND", fieldValue,
					fieldName, "=", fieldName);
	}

	/**
	 * 构建String类型字段条件
	 * 
	 * @param whereClause
	 *            待构建SQL条件子句
	 * @param paramMap
	 *            待构建条件参数
	 * @param logicOperator
	 *            逻辑条件
	 * @param fieldValue
	 *            字段值
	 * @param fieldName
	 *            字段名称
	 * @param compareOperator
	 *            比较条件
	 * @param parameterName
	 *            参数名称
	 */
	public static void buildStringFieldCondition(StringBuilder whereClause,
			Map<String, Object> paramMap, String logicOperator,
			String fieldValue, String fieldName, String compareOperator,
			String parameterName) {
		Requires.notNull(whereClause, "whereClause");
		Requires.notNull(paramMap, "paramMap");
		Requires.notNullOrEmpty(logicOperator, "logicOperator");
		// Requires.notNullOrEmpty(fieldValue, "fieldValue");
		Requires.notNullOrEmpty(fieldName, "fieldName");
		Requires.notNullOrEmpty(compareOperator, "compareOperator");
		Requires.notNullOrEmpty(parameterName, "parameterName");

		if (!StringUtilEx.isNullOrEmpty(fieldValue)) {
			whereClause.append(String.format(" %s %s%s:%s", logicOperator,
					fieldName, compareOperator, parameterName));
			paramMap.put(parameterName, fieldValue);
		}
	}

	/**
	 * 构建Date类型字段条件（逻辑与/等值判断/字段名参数名相同）
	 * 
	 * @param whereClause
	 *            待构建SQL条件子句
	 * @param paramMap
	 *            待构建条件参数
	 * @param fieldValue
	 *            字段值
	 * @param fieldName
	 *            字段名称
	 */
	public static void buildDateFieldCondition(StringBuilder whereClause,
			Map<String, Object> paramMap, Date fieldValue, String fieldName) {
		if (fieldValue != null)
			buildDateFieldCondition(whereClause, paramMap, "AND", fieldValue,
					fieldName, "=", fieldName);
	}

	/**
	 * 构建Date类型字段条件
	 * 
	 * @param whereClause
	 *            待构建SQL条件子句
	 * @param paramMap
	 *            待构建条件参数
	 * @param logicOperator
	 *            逻辑条件
	 * @param fieldValue
	 *            字段值
	 * @param fieldName
	 *            字段名称
	 * @param compareOperator
	 *            比较条件
	 * @param parameterName
	 *            参数名称
	 */
	public static void buildDateFieldCondition(StringBuilder whereClause,
			Map<String, Object> paramMap, String logicOperator,
			Date fieldValue, String fieldName, String compareOperator,
			String parameterName) {
		Requires.notNull(whereClause, "whereClause");
		Requires.notNull(paramMap, "paramMap");
		Requires.notNullOrEmpty(logicOperator, "logicOperator");
		Requires.notNull(fieldValue, "fieldValue");
		Requires.notNullOrEmpty(fieldName, "fieldName");
		Requires.notNullOrEmpty(compareOperator, "compareOperator");
		Requires.notNullOrEmpty(parameterName, "parameterName");

		if (null != fieldValue) {
			whereClause.append(String.format(" %s %s%s:%s", logicOperator,
					fieldName, compareOperator, parameterName));
			paramMap.put(parameterName, fieldValue);
		}
	}

	/**
	 * 获取实体类关联数据库表名
	 * 
	 * @param entity
	 *            实体类对象
	 * @return 数据库表名
	 */
	public static String getEntityTableName(Object entity) {
		Requires.notNull(entity, "entity");

		return entity.getClass().getAnnotation(Table.class).Name();
	}

	/**
	 * 使用实体类类型构建查询字段
	 * 
	 * @param entityClass
	 *            实体类类型
	 * @return 查询字段
	 */
	public static String buildSelectFieldsClauseByDBO(Class<?> entityClass) {
		Requires.notNull(entityClass, "entityClass");

		String sqlFields = "";

		Field[] fields = entityClass.getDeclaredFields();
		// 遍历实体类声明字段
		for (Field field : fields) {
			String fieldName = null;
			// 若有标注则使用
			if (field.isAnnotationPresent(Column.class))
				fieldName = field.getAnnotation(Column.class).Name();
			// 无标注使用字段名
			if (fieldName == null || fieldName.equals(""))
				fieldName = field.getName();
			// 不是第一个字段，增加分隔符
			if (!sqlFields.isEmpty())
				sqlFields += ",";
			// 构建字段
			sqlFields += fieldName;
		}

		return sqlFields;
	}

	/**
	 * 使用实体类类型构建查询字段
	 * 
	 * @param entityClass
	 *            实体类类型
	 * @param prfix
	 *            前缀
	 * @return 查询字段
	 */
	public static String buildSelectFieldsClauseByDBO(Class<?> entityClass,
			String prefix) {
		Requires.notNull(entityClass, "entityClass");

		String sqlFields = "";

		Field[] fields = entityClass.getDeclaredFields();
		// 遍历实体类声明字段
		for (Field field : fields) {
			String fieldName = null;
			// 若有标注则使用
			if (field.isAnnotationPresent(Column.class))
				fieldName = field.getAnnotation(Column.class).Name();
			// 无标注使用字段名
			if (fieldName == null || fieldName.equals(""))
				fieldName = field.getName();
			// 不是第一个字段，增加分隔符
			if (!sqlFields.isEmpty())
				sqlFields += ",";
			// 构建字段
			sqlFields += prefix + "." + fieldName;
		}

		return sqlFields;
	}

	/**
	 * 使用实体类构建条件子句和条件参数（逻辑与/等值判断/字段名参数名相同）
	 * 
	 * @param entity
	 *            实体类对象
	 * @param paramMap
	 *            待构建条件参数
	 * @return 条件子句
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static <T> String buildWhereClauseAndParamMap(T entity,
			Map<String, Object> paramMap, String openType) throws Exception {
		Requires.notNull(entity, "entity");
		Requires.notNull(paramMap, "paramMap");

		// 默认条件
		String whereClause = "WHERE 1=1";

		whereClause += buildConditionAndParamMap(entity, "", paramMap);

		if ("delete".equals(openType) && "WHERE 1=1".equals(whereClause)) {
			throw new RuntimeException("刪除数据，条件不能为空");
		}

		return whereClause;
	}

	/**
	 * 使用实体类构建条件子句和条件参数不包含where（逻辑与/等值判断/字段名参数名相同）
	 * 
	 * @param entity
	 *            实体类对象
	 * @param prefix
	 *            前缀
	 * @param paramMap
	 *            待构建条件参数
	 * @return 条件子句
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static <T> String buildConditionAndParamMap(T entity, String prefix,
			Map<String, Object> paramMap) throws IllegalArgumentException,
			IllegalAccessException {
		Requires.notNull(entity, "entity");
		Requires.notNull(paramMap, "paramMap");

		StringBuilder builder = new StringBuilder();
		Field[] fields = entity.getClass().getDeclaredFields();
		// 遍历实体类声明字段
		for (Field field : fields) {
			String fieldName = null;
			// 若有标注则使用
			if (field.isAnnotationPresent(Column.class))
				fieldName = field.getAnnotation(Column.class).Name();
			// 无标注使用字段名
			if (fieldName == null || fieldName.equals(""))
				fieldName = field.getName();
			// 获取字段数据
			field.setAccessible(true);
			Object fieldValue = field.get(entity);
			// 数据有效
			if (fieldValue != null) {
				// 字符串类型非空
				if ((fieldValue instanceof String)
						&& ((String) fieldValue).trim().equals(""))
					continue;

				if (StringUtilEx.isNullOrEmpty(prefix)) {
					builder.append(String.format(" AND %1$s=:%1$s", fieldName));
					// 构建条件参数
					paramMap.put(fieldName, fieldValue);
				} else {
					// 构建条件子句
					builder.append(String.format(" AND %1$s.%2$s=:%1$s.%2$s",
							prefix, fieldName));
					// 构建条件参数
					paramMap.put(String.format("%s.%s", prefix, fieldName),
							fieldValue);
				}
			}
		}

		return builder.toString();
	}

	/**
	 * 生成 delete的sql语句 (DbParam参数化)
	 * 
	 * @param tableName
	 *            表名
	 * @param paraMap
	 *            条件参数集合
	 * @return deletesql语句
	 * @throws Exception
	 */
	public static String createDeleteSql(DbParam dbParam) throws Exception {
		try {
			StringBuffer deleteSql = new StringBuffer();
			deleteSql.append(String.format("DELETE FROM %s WHERE 1=1 ",
					dbParam.getTableName()));
			// 循环拼接删除条件集合
			if (dbParam.getWhereMap() != null) {
				Map<String, Object> filterMap = CollectionUtil
						.filterMapValueIsNull(dbParam.getWhereMap());
				Object[] col = filterMap.keySet().toArray();
				// 拼接条件字段名+字段值
				for (int i = 0; i < col.length; i++) {
					String s = String.format(" AND %s=:%s ", (String) col[i],
							(String) col[i]);
					deleteSql.append(s);
				}
			}
			return deleteSql.toString();
		} catch (Exception e) {
			LoggerUtil.error(IDBErrorMessage.创建SQL语句出错, e);
			throw new Exception(IDBErrorMessage.访问数据库异常, e);
		}
	}

	/**
	 * 生成 Insert的SQL语句(DbParam参数化)
	 * 
	 * @param tableName
	 *            表名
	 * @param paraMap
	 *            条件参数集合
	 * @return Insertsql语句
	 * @throws Exception
	 */
	public static String createInsertSql(DbParam dbParam) throws Exception {
		try {
			// 非空校验
			if (dbParam.getValueMap() == null
					|| dbParam.getValueMap().size() == 0) {
				return null;
			}
			Map<String, Object> filterMap = CollectionUtil
					.filterMapValueIsNull(dbParam.getValueMap());
			StringBuffer colBuffers = new StringBuffer();// 列
			StringBuffer valBuffers = new StringBuffer();// 值
			// 循环拼入Insert的列字段和插入值
			for (String key : filterMap.keySet()) {
				// 非字符串类型的数据，不需添加单引号
				String valBuffer = String.format(":%s,", key);
				String colBuffer = String.format("%s,", key);
				valBuffers.append(valBuffer);
				colBuffers.append(colBuffer);
			}
			// Insert列的非空校验
			if (colBuffers.length() == 0 || colBuffers.length() == 0)
				return null;
			colBuffers = colBuffers.deleteCharAt(colBuffers.lastIndexOf(","));
			valBuffers = valBuffers.deleteCharAt(valBuffers.lastIndexOf(","));
			// 拼Insert语句
			String insertSql = String.format(
					"INSERT INTO %s (%s) VALUES (%s) ", dbParam.getTableName(),
					colBuffers.toString(), valBuffers.toString());
			return insertSql;
		} catch (Exception e) {
			LoggerUtil.error(IDBErrorMessage.创建SQL语句出错, e);
			throw new Exception(IDBErrorMessage.访问数据库异常, e);
		}
	}

	public static final String PARAUPDATE = "_U";

	/**
	 * 生成update的Sql语句 (DbParam参数化)
	 * 
	 * @param tableName
	 *            表名
	 * @param colMap
	 *            更新字段
	 * @param valMap
	 *            条件字段
	 * @return update的sql语句
	 * @throws Exception
	 */
	public static String createUpdateSql(DbParam dbParam) throws Exception {
		try {
			// 更新参数非空校验
			if (dbParam.getTableName() == null || dbParam.getWhereMap() == null
					|| dbParam.getSetMap() == null)
				return null;
			Map<String, Object> filterSetMap = CollectionUtil
					.filterMapValueIsNull(dbParam.getSetMap());
			Map<String, Object> filterWhereValMap = CollectionUtil
					.filterMapValueIsNull(dbParam.getWhereMap());
			Object[] setColName = filterSetMap.keySet().toArray();
			Object[] whereColName = filterWhereValMap.keySet().toArray();

			StringBuffer updateSql = new StringBuffer();
			// 拼update语句
			String update = String.format("UPDATE %s SET ",
					dbParam.getTableName());
			updateSql.append(update);
			// 循环拼入update字段值
			for (int i = 0; i < setColName.length; i++) {
				String sqlCol = String.format("%s=:%s", setColName[i],
						setColName[i]);
				updateSql.append(sqlCol);
				if (i < setColName.length - 1)
					updateSql.append(", ");
			}
			updateSql.append(" WHERE 1=1 ");

			if (whereColName == null || whereColName.length == 0) {
				LoggerUtil.error("update 条件不能全为空 ");
				throw new RuntimeException(IDBErrorMessage.访问数据库异常
						+ " update 条件不能全为空");
			}
			// 循环拼入update条件
			for (int i = 0; i < whereColName.length; i++) {
				updateSql.append(String.format(" AND %s=:%s%s ",
						whereColName[i], whereColName[i], PARAUPDATE));
			}
			return updateSql.toString();
		} catch (Exception e) {
			LoggerUtil.error(IDBErrorMessage.创建SQL语句出错, e);
			throw new Exception(IDBErrorMessage.访问数据库异常, e);
		}
	}

	/**
	 * 生成 Query SQL(DbParam参数化)
	 * 
	 * @param dbParam
	 *            sql参数实体类
	 * @return sql查询语句
	 * @throws Exception
	 */
	public static String createQuerySql(DbParam dbParam, String dbMode)
			throws Exception {
		try {
			StringBuffer querySqlBuffer = new StringBuffer();
			// 自定义查询语句的封装
			if (!StringUtilEx.isNullOrEmpty(dbParam.getSqlString())) {
				querySqlBuffer.append(dbParam.getSqlString());
			}
			// 非自定义查询语句的封装
			else {
				// 表名非空校验
				if (StringUtilEx.isNullOrEmpty(dbParam.getTableName())) {
					LoggerUtil.error(IDBErrorMessage.调用查询排序字段为空);
					throw new Exception(IDBErrorMessage.交易处理异常);
				}
				querySqlBuffer.append("SELECT ");
				// 查询字段map非空，则拼入所有查询的字段
				if (dbParam.getColumnMap() != null
						&& dbParam.getColumnMap().size() > 0) {
					Map<String, Object> filterMap = CollectionUtil
							.filterMapValueIsNull(dbParam.getColumnMap());
					Object[] colName = filterMap.keySet().toArray();
					// 循环拼接所查询的字段
					for (int i = filterMap.size() - 1; i >= 0; i--) {
						if (i == 0) {
							querySqlBuffer.append((String) colName[i]);
							continue;
						}
						querySqlBuffer.append((String) colName[i] + ", ");
					}
					// 否则默认为*全查询
				} else {
					querySqlBuffer.append(" * ");
				}
				querySqlBuffer.append(String.format(" FROM %s WHERE 1=1 ",
						dbParam.getTableName()));
				// 条件map非空，则拼入查询条件
				if (dbParam.getWhereMap() != null
						&& dbParam.getWhereMap().size() > 0) {
					Map<String, Object> filterMap = CollectionUtil
							.filterMapValueIsNull(dbParam.getWhereMap());
					Object[] colName = filterMap.keySet().toArray();
					// 循环拼接查询条件
					for (int i = 0; i < filterMap.size(); i++) {
						querySqlBuffer.append(String.format(" AND %s= :%s",
								(String) colName[i], (String) colName[i]));
					}
				}
			}
			String querySql = querySqlBuffer.toString();
			// 拼入排序字段
			if (!StringUtilEx.isNullOrEmpty(dbParam.getOrderBy())) {
				querySql = createQuerySqlOrderBy(querySql, dbParam.getOrderBy());
			}
			// 拼入查询数量限制字段
			if (!StringUtilEx.isNullOrEmpty(dbParam.getTopConut())) {
				return createQuerySqlTopCount(querySql, dbParam.getTopConut(),
						dbMode);
			}
			return querySql;
		} catch (Exception e) {
			LoggerUtil.error(IDBErrorMessage.创建SQL语句出错, e);
			throw new Exception(IDBErrorMessage.交易处理异常, e);
		}
	}

	/**
	 * 生成分页查询SQL语句
	 * 
	 * @param sql
	 *            自定义sql查询语句
	 * @param orderBy
	 *            排序字段
	 * @param pageSize
	 *            每页查询数据量
	 * @param pageNum
	 *            查询第几页
	 * @return 分页查询SQL语句
	 * @throws Exception
	 */
	public static String createQuerySqlByPage(String sql, String orderBy,
			int pageSize, int pageNum, String dbMode) throws Exception {
		// 排序字段非空校验
		if (StringUtilEx.isNullOrEmpty(orderBy)) {
			LoggerUtil.error(IDBErrorMessage.调用查询排序字段为空);
			throw new Exception(IDBErrorMessage.交易处理异常);
		}
		// 数据库模式非空校验
		if (StringUtilEx.isNullOrEmpty(dbMode)) {
			LoggerUtil.error(IDBErrorMessage.数据库模式DBMODE不支持);
			throw new Exception(IDBErrorMessage.交易处理异常);
		}
		
		LoggerUtil.debug("-----------------"+dbMode+"-----------------");
		LoggerUtil.debug("-----------------"+dbMode+"-----------------");
		LoggerUtil.debug("-----------------"+dbMode+"-----------------");
		LoggerUtil.debug("-----------------"+dbMode+"-----------------");
		StringBuilder builder = new StringBuilder();
		if (sql.indexOf("t_tran_log") > -1) {
			builder.append("SELECT * FROM ( SELECT ROW_NUMBER() OVER (ORDER BY ");
			builder.append(orderBy);
			builder.append(" ) AS ROWNUMBER,A.* FROM ( ");
			builder.append(sql);
			builder.append(" ) AS A ) WHERE ROWNUMBER<=");
			builder.append(pageNum * pageSize);
			builder.append(" AND  ROWNUMBER>");
			builder.append((pageNum - 1) * pageSize);
			return builder.toString();
		}
		
		
		
		// 拼接oracle数据库的分页查询SQL语句
		if (StringUtilEx.equalsNotCL(dbMode, DbModeConst.ORACLE)) {
			builder.append("SELECT * FROM ( SELECT A.* ,ROWNUM R FROM (");
			builder.append(sql);
			builder.append(" ) A  WHERE ROWNUM <=");
			builder.append(pageNum * pageSize);
			builder.append(") B WHERE R>");
			builder.append((pageNum - 1) * pageSize);
			return builder.toString();
		}
		// 拼接mssql数据库的分页查询SQL语句
		if (StringUtilEx.equalsNotCL(dbMode, DbModeConst.MSSQL)) {
			builder.append("SELECT * FROM ( SELECT TOP ");
			builder.append(pageSize);
			builder.append(" * FROM ");
			builder.append("( SELECT TOP ");
			builder.append(pageNum * pageSize);
			builder.append(" * FROM ( ");
			builder.append(sql);
			builder.append(" ) A ORDER BY A.");
			builder.append(orderBy);
			builder.append(" ASC)  B ORDER BY B.");
			builder.append(orderBy);
			builder.append(" DESC ) C ORDER BY C.");
			builder.append(orderBy);
			builder.append("  ASC");
			return builder.toString();
		}
		// 拼接mysql数据库的分页查询SQL语句
		if (StringUtilEx.equalsNotCL(dbMode, DbModeConst.MYSQL)) {
			builder.append("SELECT * FROM ( ");
			builder.append(sql);
			builder.append(" ) A LIMIT ");
			builder.append(((pageNum - 1) * pageSize) + ","
					+ ((pageNum - 1) * pageSize + pageSize));
			return builder.toString();
		}
		// 拼接db2数据库的分页查询SQL语句
		if (StringUtilEx.equalsNotCL(dbMode, DbModeConst.DB2)) {

			builder.append("SELECT * FROM ( SELECT ROW_NUMBER() OVER (ORDER BY ");
			builder.append(orderBy);
			builder.append(" ) AS ROWNUMBER,A.* FROM ( ");
			builder.append(sql);
			builder.append(" ) AS A ) WHERE ROWNUMBER<=");
			builder.append(pageNum * pageSize);
			builder.append(" AND  ROWNUMBER>");
			builder.append((pageNum - 1) * pageSize);
			return builder.toString();
		}
		return sql;
	}

	/**
	 * 拼接查询sql语句的排序字段
	 * 
	 * @param sql
	 *            自定义sql查询语句
	 * @param orderBy
	 *            排序字段
	 * @return 排序的查询sql语句
	 * @throws Exception
	 */
	public static String createQuerySqlOrderBy(String sql, String orderBy)
			throws Exception {
		// 排序字段非空校验
		if (StringUtilEx.isNullOrEmpty(orderBy)) {
			LoggerUtil.error(IDBErrorMessage.调用查询排序字段为空);
			throw new Exception(IDBErrorMessage.交易处理异常);
		}
		sql = String.format("SELECT * FROM(%s) A ORDER BY %s", sql, orderBy);
		return sql;
	}

	/**
	 * 拼接查询sql语句的限制数量字段
	 * 
	 * @param sql
	 *            自定义sql语句
	 * @param topCount
	 *            查询数量
	 * @return 限制查询数量的查询语句
	 * @throws Exception
	 */
	public static String createQuerySqlTopCount(String sql, String topCount,
			String dbMode) throws Exception {
		// topCount非空校验
		if (StringUtilEx.isNullOrEmpty(topCount)) {
			LoggerUtil.error(IDBErrorMessage.调用查询限制数量字段为空);
			throw new Exception(IDBErrorMessage.交易处理异常);
		}
		// 数据库模式非空校验
		if (StringUtilEx.isNullOrEmpty(dbMode)) {
			LoggerUtil.error(IDBErrorMessage.数据库模式DBMODE不支持);
			throw new Exception(IDBErrorMessage.交易处理异常);
		}
		StringBuilder builder = new StringBuilder();
		// 拼接oracle数据的查询数量限制的sql查询语句
		if (StringUtilEx.equalsNotCL(dbMode, DbModeConst.ORACLE)) {
			builder.append("SELECT * FROM ");
			builder.append("( ");
			builder.append(sql);
			builder.append(" ) B WHERE ROWNUM <=");
			builder.append(topCount);
			return builder.toString();
		}
		// 拼接MSSQL数据的查询数量限制的sql查询语句
		if (StringUtilEx.equalsNotCL(dbMode, DbModeConst.MSSQL)) {
			builder.append("SELECT TOP");
			builder.append(topCount);
			builder.append(" * FROM ");
			builder.append("( ");
			builder.append(sql);
			builder.append(" ) B ");
			return builder.toString();
		}
		// 拼接mysql数据的查询数量限制的sql查询语句
		if (StringUtilEx.equalsNotCL(dbMode, DbModeConst.MYSQL)) {
			builder.append("SELECT * FROM ");
			builder.append("( ");
			builder.append(sql);
			builder.append(" ) B   LIMIT ");
			builder.append(topCount);
			return builder.toString();
		}
		// 拼接DB2数据的查询数量限制的sql查询语句
		if (StringUtilEx.equalsNotCL(dbMode, DbModeConst.DB2)) {
			builder.append("SELECT * FROM ");
			builder.append("( ");
			builder.append(sql);
			builder.append(" ) B WHERE FETCH  FIRST ");
			builder.append(topCount);
			builder.append(" ROWS ONLY");
			return builder.toString();
		}
		return null;
	}

	/**
	 * 获取总条数的sql
	 * 
	 * @param sql
	 *            sql语句
	 * @return 获取条数的sql查询语句
	 */
	public static String getCountSql(String sql, String Dbmode) {
		String ROWCOUNT = "ROWCOUNT";
		StringBuilder builder = new StringBuilder();
		if (Dbmode.equals(DbModeConst.DB2) | Dbmode.equals(DbModeConst.ORACLE)) {
			builder.append("SELECT COUNT(*) ");
			builder.append(ROWCOUNT);
			builder.append(" FROM (");
			builder.append(sql);
			builder.append(")  temptable");
			return builder.toString();
		}
		builder.append("SELECT COUNT(*) ");
		builder.append("'" + ROWCOUNT + "'");
		builder.append(" FROM (");
		builder.append(sql);
		builder.append(") as temptable");
		return builder.toString();
	}

}
