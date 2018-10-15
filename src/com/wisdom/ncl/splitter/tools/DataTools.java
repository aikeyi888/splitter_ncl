package com.wisdom.ncl.splitter.tools;


/**
 * Copyright (C), 2011-2012, beijing ow Co., Ltd.
 * 文件名:     DataTools.java
 */


import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * 类描述: 数据处理工具类 修改历史:
 * 
 * @author yuhao
 * @version 1.0
 * @date 2011-12-1 上午10:26:28
 * @description 其它:
 */

public class DataTools {
	// =============第一部分： resultToBean===================
	/**
	 * 
	 * 函数名: resultSetToList 功能描述: resultSet转化成List集合 修改历史:
	 * 
	 * @date 2011-12-1 上午10:32:13
	 * @author yuhao
	 * @version 1.0
	 * @description
	 * 
	 *              输入、输出参数:
	 * @param objclass
	 * @param rs
	 * @return
	 */
	public List<Object> resultSetToList(Class objclass, ResultSet rs) {
		List<Object> list = null;
		// rs 不为空的时候执行
		if (rs != null) {
			list = new ArrayList<Object>();
			Field[] field = objclass.getDeclaredFields();
			try {
				// 从rs中取值
				while (rs.next()) {
					// 反射获取对象本身 注意：这里当构建对象时会为对象的属性进行初始化
					Object addObject = objclass.getConstructor().newInstance();
					// 循环对属性赋值
					for (int i = 0; i < field.length; i++) {
						String fieldName = field[i].getName();
						// 根据属性进行内省
						PropertyDescriptor pd = new PropertyDescriptor(
								fieldName, addObject.getClass());
						Method method = pd.getWriteMethod();
						// 获取数据库对应的每一个值
						Object fieldValue = rs.getObject(fieldName);
						// 当对象的值为null时不执行反射操作
						if (fieldValue != null) {
							// 反射执行set方法
							method.invoke(addObject, fieldValue);
						}
					}
					list.add(addObject);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * 
	 * 函数名: resultSetToList 功能描述: resultSet转化成List 修改历史:
	 * 
	 * @date 2011-12-1 上午10:31:57
	 * @author yuhao
	 * @version 1.0
	 * @description 输入、输出参数:
	 * @param rs
	 * @return
	 */
	public List resultSetToList(ResultSet rs) {
		// Container
		List list = null;
		try {
			list = new ArrayList();
			// Get the element of resultSet
			ResultSetMetaData md = rs.getMetaData();
			// Map rowData;
			int columnCount = md.getColumnCount();
			// Use Map
			while (rs.next()) {
				Map rowData = new HashMap();
				// Fill up
				for (int i = 1; i <= columnCount; i++) {
					Object object = rs.getObject(i);
					// 避免出现类型不匹配的问题
					if (object != null) {
						rowData.put(md.getColumnName(i), object);
					}
				}
				// Fill up
				list.add(rowData);
			}
		} catch (Exception e) {
		}
		// back
		return list;
	}

	/**
	 * 
	 * 函数名: resultSetToObject 功能描述: resultSet转化成Object 修改历史:
	 * 
	 * @date 2011-12-1 上午10:34:38
	 * @author yuhao
	 * @version 1.0
	 * @description 输入、输出参数:
	 * @param objectClass
	 * @param rs
	 * @return
	 */
	public Object resultSetToObject(Class objectClass, ResultSet rs) {
		if (rs == null) {
			return null;
		}
		Field[] field = objectClass.getDeclaredFields();
		Object addObject = null;
		try {
			// 注意指针是否下拨
			// rs.next();
			addObject = objectClass.getConstructor().newInstance();
			for (int i = 0; i < field.length; i++) {
				String fieldName = field[i].getName();
				PropertyDescriptor pd = new PropertyDescriptor(fieldName,
						addObject.getClass());
				Method method = pd.getWriteMethod();
				// 获取数据库对应的每一个值
				Object fieldValue = rs.getObject(fieldName);
				if (fieldValue != null) {
					// 反射执行set方法
					method.invoke(addObject, fieldValue);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return addObject;
	}

	// ==================第二部分：beqanToSql=====================
	/**
	 * 
	 * 函数名: getInsertSql 功能描述: 得到插入sql语句 修改历史:
	 * 
	 * @date 2011-11-30 下午02:21:17
	 * @author yuhao
	 * @version 1.0
	 * @description 输入、输出参数:
	 * @param object
	 * @return
	 */
	public String getInsertSql(Object object) {
		return this.getInsertSql(object, null, null);
	}

	/**
	 * 
	 * 函数名: getInsertSql 功能描述: 根据表名得到插入sql语句 修改历史:
	 * 
	 * @date 2011-11-30 下午02:21:41
	 * @author yuhao
	 * @version 1.0
	 * @description 输入、输出参数:
	 * @param object
	 * @param tableName
	 * @return
	 */
	public String getInsertSql(Object object, String tableName) {
		return this.getInsertSql(object, tableName, null);
	}

	/**
	 * 
	 * 函数名: getInsertSql 功能描述: 得到插入sql语句 修改历史:
	 * 
	 * @date 2011-12-1 上午10:17:48
	 * @author yuhao
	 * @version 1.0
	 * @description 输入、输出参数:
	 * @param object
	 * @param lostFieldList
	 * @return
	 */
	public String getInsertSql(Object object, List<String> lostFieldList) {
		return this.getInsertSql(object, null, lostFieldList);
	}

	/**
	 * 
	 * 函数名: getInsertSql 功能描述: 得到插入语句 修改历史:
	 * 
	 * @date 2011-11-30 下午02:22:16
	 * @author yuhao
	 * @version 1.0
	 * @description 输入、输出参数:
	 * @param object
	 * @param tableName
	 * @param lostFieldList
	 * @return
	 */
	public String getInsertSql(Object object, String tableName,
			List<String> lostFieldList) {
		StringBuffer sqlInseretBuffer = new StringBuffer();
		StringBuffer sqlValueBuffer = new StringBuffer();
		String insertTableName = "";
		List<FieldHelp> fieldList = null;
		// 对象不为空才有意义
		if (object != null) {
			// 没有提供tableName的信息
			if (tableName == null || "".equals(tableName)) {
				insertTableName = object.getClass().getSimpleName();
			} else {
				insertTableName = tableName;
			}
			sqlInseretBuffer.append("insert into " + insertTableName + " (");
			sqlValueBuffer.append(" values( ");
			// 屏蔽属性集合不为空的情况
			if (lostFieldList != null && lostFieldList.size() > 0) {
				fieldList = getFieldNameTypeValue(object, lostFieldList);
			} else {
				fieldList = getFieldNameTypeValue(object);
			}
			if (fieldList != null && fieldList.size() > 0) {
				for (int i = 0; i < fieldList.size(); i++) {
					FieldHelp fieldHelp = fieldList.get(i);
					String fieldName = fieldHelp.getFieldName();
					String fieldType = fieldHelp.getFieldType();
					String fieldValue = fieldHelp.getFieldValue();
					// 得到声明部分
					if (i == fieldList.size() - 1) {
						sqlInseretBuffer.append(fieldName + " ) ");
					} else {
						sqlInseretBuffer.append(fieldName + ",");
					}
					// 得到value部分
					// 判断属性类型 暂时支持这几种 以后再加
					if (fieldType.contains("int")) {
						if (i == fieldList.size() - 1) {
							sqlValueBuffer.append(fieldValue + " ) ");
						} else {
							sqlValueBuffer.append(fieldValue + ",");
						}
					} else if (fieldType.contains("long")) {
						if (i == fieldList.size() - 1) {
							sqlValueBuffer.append(fieldValue + " ) ");
						} else {
							sqlValueBuffer.append(fieldValue + ",");
						}
					} else if (fieldType.contains("Timestamp")) {
						if (i == fieldList.size() - 1) {
							if (fieldValue == null || "".equals(fieldValue)) {
								sqlValueBuffer.append("'"
										+ new Timestamp(System
												.currentTimeMillis())
												.toString() + " ') ");
							} else {

								sqlValueBuffer
										.append("'" + fieldValue + " ') ");
							}
						} else {
							if (fieldValue == null || "".equals(fieldValue)
									|| "null".equals(fieldValue)) {
								sqlValueBuffer.append("'"
										+ new Timestamp(System
												.currentTimeMillis())
												.toString() + "',");
							} else {
								sqlValueBuffer.append("'" + fieldValue + "',");
							}
						}
					} else {
						if (i == fieldList.size() - 1) {
							sqlValueBuffer.append("'" + fieldValue + "' ) ");
						} else {
							sqlValueBuffer.append("'" + fieldValue + "',");
						}
					}
				}
			}
		}
		return sqlInseretBuffer.append(sqlValueBuffer).toString();
	}

	/**
	 * 
	 * 函数名: getUpdateSql 功能描述: 得到修改语句 修改历史:
	 * 
	 * @date 2011-11-30 下午02:24:50
	 * @author yuhao
	 * @version 1.0
	 * @description 输入、输出参数:
	 * @param object
	 * @return
	 */
	public String getUpdateSql(Object object, List<String> pkList) {
		return this.getUpdateSql(object, null, null, pkList);
	}

	/**
	 * 
	 * 函数名: getUpdateSql 功能描述: 得到修改语句 修改历史:
	 * 
	 * @date 2011-11-30 下午02:25:10
	 * @author yuhao
	 * @version 1.0
	 * @description 输入、输出参数:
	 * @param object
	 * @param tableName
	 * @return
	 */
	public String getUpdateSql(Object object, String tableName,
			List<String> pkList) {

		return this.getUpdateSql(object, tableName, null, pkList);
	}

	/**
	 * 
	 * 函数名: getUpdateSql 功能描述: 得到修改的sql语句 修改历史:
	 * 
	 * @date 2011-12-1 上午10:19:13
	 * @author yuhao
	 * @version 1.0
	 * @description 输入、输出参数:
	 * @param object
	 * @param lostFieldList
	 * @param pkList
	 * @return
	 */
	public String getUpdateSql(Object object, List<String> lostFieldList,
			List<String> pkList) {
		return this.getUpdateSql(object, null, lostFieldList, pkList);
	}

	/**
	 * 
	 * 函数名: getUpdateSql 功能描述: 得到修改语句 修改历史:
	 * 
	 * @date 2011-11-30 下午02:25:25
	 * @author yuhao
	 * @version 1.0
	 * @description 输入、输出参数:
	 * @param object
	 * @param tableName
	 * @param lostFieldList
	 * @return
	 */
	public String getUpdateSql(Object object, String tableName,
			List<String> lostFieldList, List<String> pkList) {
		StringBuffer sqlUpdateBuffer = new StringBuffer();
		StringBuffer sqlCondationBuffer = new StringBuffer();
		String updateTableName = "";
		List<FieldHelp> fieldList = null;
		// 对象不为空 有必要执行
		if (object != null && pkList != null && pkList.size() > 0) {
			sqlUpdateBuffer.append("update ");
			sqlCondationBuffer.append(" where ");
			// 没有提供tableName的信息
			if (tableName == null || "".equals(tableName)) {
				updateTableName = object.getClass().getSimpleName();
			} else {
				updateTableName = tableName;
			}
			sqlUpdateBuffer.append(updateTableName + " set ");
			// 屏蔽属性集合不为空的情况
			if (lostFieldList != null && lostFieldList.size() > 0) {
				fieldList = getFieldNameTypeValue(object, lostFieldList);
			} else {
				fieldList = getFieldNameTypeValue(object);
			}
			// 属性不为空的情况下
			if (fieldList != null && fieldList.size() > 0) {
				for (int i = 0; i < fieldList.size(); i++) {
					FieldHelp fieldHelp = fieldList.get(i);
					String fieldName = fieldHelp.getFieldName();
					String fieldType = fieldHelp.getFieldType();
					String fieldValue = fieldHelp.getFieldValue();
					// 判断属性类型 暂时支持这几种 以后再加
					if (fieldType.contains("int")) {// 短整
						if (i == fieldList.size() - 1) {
							sqlUpdateBuffer.append(fieldName + " = "
									+ fieldValue);
							if (pkList.contains(fieldName)) {
								sqlCondationBuffer.append(fieldName + " = "
										+ fieldValue + " and ");
							}
						} else {
							sqlUpdateBuffer.append(fieldName + " = "
									+ fieldValue + " , ");
							if (pkList.contains(fieldName)) {
								sqlCondationBuffer.append(fieldName + " = "
										+ fieldValue + " and ");
							}
						}
					} else if (fieldType.contains("long")) {// 长整
						if (i == fieldList.size() - 1) {
							sqlUpdateBuffer.append(fieldName + " = "
									+ fieldValue);
							if (pkList.contains(fieldName)) {
								sqlCondationBuffer.append(fieldName + " = "
										+ fieldValue + " and ");
							}
						} else {
							sqlUpdateBuffer.append(fieldName + " = "
									+ fieldValue + " , ");
							if (pkList.contains(fieldName)) {
								sqlCondationBuffer.append(fieldName + " = "
										+ fieldValue + " and ");
							}
						}
					} else if (fieldType.contains("Timestamp")) {// 时间
						if (i == fieldList.size() - 1) {
							if (fieldValue == null || "".equals(fieldValue)) {// 判断时间是否为空
								sqlUpdateBuffer.append(fieldName
										+ " = '"
										+ new Timestamp(System
												.currentTimeMillis())
												.toString() + "'");
								if (pkList.contains(fieldName)) {
									sqlCondationBuffer.append(fieldName
											+ " = '"
											+ new Timestamp(System
													.currentTimeMillis())
													.toString() + "' and ");
								}
							} else {
								sqlUpdateBuffer.append(fieldName + " = '"
										+ fieldValue + "'");
								if (pkList.contains(fieldName)) {
									sqlCondationBuffer.append(fieldName
											+ " = '" + fieldValue + "' and ");
								}
							}
						} else {
							if (fieldValue == null || "".equals(fieldValue)) {
								sqlUpdateBuffer.append(fieldName
										+ " = '"
										+ new Timestamp(System
												.currentTimeMillis())
												.toString() + "' , ");
								if (pkList.contains(fieldName)) {
									sqlCondationBuffer.append(fieldName
											+ " = '"
											+ new Timestamp(System
													.currentTimeMillis())
													.toString() + "' and ");
								}
							} else {
								sqlUpdateBuffer.append(fieldName + " = '"
										+ fieldValue + "' , ");
								if (pkList.contains(fieldName)) {
									sqlCondationBuffer.append(fieldName
											+ " = '" + fieldValue + "' and ");
								}
							}
						}
					} else {// 字符串
						if (i == fieldList.size() - 1) {
							sqlUpdateBuffer.append(fieldName + " = '"
									+ fieldValue + "'");
							if (pkList.contains(fieldName)) {
								sqlCondationBuffer.append(fieldName + " = '"
										+ fieldValue + "' and ");
							}
						} else {
							sqlUpdateBuffer.append(fieldName + " = '"
									+ fieldValue + "' , ");
							if (pkList.contains(fieldName)) {
								sqlCondationBuffer.append(fieldName + " = '"
										+ fieldValue + "' and ");
							}
						}
					}
				}
			}
		}
		// 如果条件中含有and 则取出最后的and
		if (sqlCondationBuffer.toString().contains("and")) {
			String condation = sqlCondationBuffer.substring(0,
					sqlCondationBuffer.lastIndexOf("and") - 1);
			sqlUpdateBuffer.append(condation);
		}
		return sqlUpdateBuffer.toString();
	}

	/**
	 * 
	 * 函数名: getDeleteSql 功能描述: 得到删除语句 修改历史:
	 * 
	 * @date 2011-11-30 下午02:26:18
	 * @author yuhao
	 * @version 1.0
	 * @description 输入、输出参数:
	 * @param object
	 * @return
	 */
	public String getDeleteSql(Object object, List<String> pkList) {
		StringBuffer deleteSqlBuffer = new StringBuffer();
		StringBuffer condationBuffer = new StringBuffer();
		List<FieldHelp> fieldList = null;
		if (object != null) {
			deleteSqlBuffer.append(" delete  from ");
			String tableName = object.getClass().getSimpleName();
			deleteSqlBuffer.append(tableName);
			if (pkList != null && pkList.size() > 0) {
				fieldList = getFieldNameTypeValue(object);
				if (fieldList != null && fieldList.size() > 0) {
					condationBuffer.append(" where ");
					for (int i = 0; i < fieldList.size(); i++) {
						FieldHelp fieldHelp = fieldList.get(i);
						String fieldName = fieldHelp.getFieldName();
						String fieldType = fieldHelp.getFieldType();
						String fieldValue = fieldHelp.getFieldValue();
						// 判断属性类型 暂时支持这几种 以后再加
						if (fieldType.contains("int")) {
							if (pkList.contains(fieldName)) {
								condationBuffer.append(fieldName + " = "
										+ fieldValue + " and ");
							}
						} else if (fieldType.contains("long")) {
							if (pkList.contains(fieldName)) {
								condationBuffer.append(fieldName + " = "
										+ fieldValue + " and ");
							}
						} else if (fieldType.contains("double")) {
							if (pkList.contains(fieldName)) {
								condationBuffer.append(fieldName + " = "
										+ fieldValue + " and ");
							}
						} else {
							if (pkList.contains(fieldName)) {
								condationBuffer.append(fieldName + " = '"
										+ fieldValue + "' and ");
							}
						}
					}
				}
			}
		}
		if (condationBuffer.toString().contains("and")) {
			String condation = condationBuffer.substring(0, condationBuffer
					.lastIndexOf("and") - 1);
			deleteSqlBuffer.append(condation);
		}
		return deleteSqlBuffer.toString();
	};

	/**
	 * 
	 * 函数名: getDeleteSql 功能描述: 得到删除语句 修改历史:
	 * 
	 * @date 2011-12-1 上午09:43:18
	 * @author yuhao
	 * @version 1.0
	 * @description 输入、输出参数:
	 * @param object
	 * @param condation
	 * @return
	 */
	public String getDeleteSql(Object object, String condation) {
		StringBuffer deleteSqlBuffer = new StringBuffer();
		if (object != null) {
			deleteSqlBuffer.append(" delete from ");
			String tableName = object.getClass().getSimpleName();
			deleteSqlBuffer.append(tableName);
			if (condation != null && !"".equals(condation)) {
				deleteSqlBuffer.append(" where " + condation);
			}
		}
		return deleteSqlBuffer.toString();
	};

	/**
	 * 
	 * 函数名: getFieldAndValue 功能描述: 得到所有的属性及其方法 修改历史:
	 * 
	 * @date 2011-11-30 下午02:28:10
	 * @author yuhao
	 * @version 1.0
	 * @description 输入、输出参数:
	 * @param object
	 * @param lostFieldList
	 * @return
	 */
	public List<FieldHelp> getFieldNameTypeValue(Object object,
			List<String> lostFieldList) {
		List<FieldHelp> list = null;
		Field[] fields = null;
		if (object == null) {
			return list;
		} else {
			// 不反射属性集合不为空
			if (lostFieldList != null && lostFieldList.size() > 0) {
				fields = object.getClass().getDeclaredFields();
				if (fields.length > 0) {
					list = new ArrayList<FieldHelp>();
					FieldHelp sqlHelp = null;
					for (int i = 0; i < fields.length; i++) {
						sqlHelp = new FieldHelp();
						Field field = fields[i];
						String fieldName = field.getName();
						if (!lostFieldList.contains(fieldName)) {
							sqlHelp.setFieldName(fieldName);
							sqlHelp.setFieldType(field.getType().getName());
							try {

								Object resultObject = PropertyUtils
										.getProperty(object, fieldName);
								if (resultObject == null) {
									sqlHelp.setFieldValue("");
								} else {
									sqlHelp.setFieldValue("" + resultObject);
								}

							} catch (Exception e) {
								e.printStackTrace();
							}
							list.add(sqlHelp);
						} else {
							continue;
						}
					}
				}
			} else {
				this.getFieldNameTypeValue(object);
			}
		}
		return list;
	}

	/**
	 * 
	 * 函数名: getFieldAndValue 功能描述: 得到属性及其值 修改历史:
	 * 
	 * @date 2011-11-30 下午02:28:37
	 * @author yuhao
	 * @version 1.0
	 * @description 输入、输出参数:
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public List<FieldHelp> getFieldNameTypeValue(Object object) {
		List<FieldHelp> list = null;
		Field[] fields = null;
		if (object == null) {
			return list;
		} else {
			fields = object.getClass().getDeclaredFields();
			if (fields.length > 0) {
				list = new ArrayList<FieldHelp>();
				FieldHelp sqlHelp = null;
				for (int i = 0; i < fields.length; i++) {
					sqlHelp = new FieldHelp();
					Field field = fields[i];
					sqlHelp.setFieldName(field.getName());
					sqlHelp.setFieldType(field.getType().getName());
					try {
						Object rersultObject = PropertyUtils.getProperty(
								object, fields[i].getName());
						if (rersultObject == null) {
							sqlHelp.setFieldValue("");
						} else {
							sqlHelp.setFieldValue(""
									+ PropertyUtils.getProperty(object,
											fields[i].getName()));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					list.add(sqlHelp);
				}
			}
		}
		return list;
	}

	/**
	 * 
	 * 函数名: getDivicePageSql 功能描述: 得到分页查询语句 修改历史:
	 * 
	 * @date 2011-12-1 上午11:09:25
	 * @author yuhao
	 * @version 1.0
	 * @description 输入、输出参数:
	 * @param tableName
	 * @param pageSize
	 * @param curPage
	 * @param condation
	 * @return
	 */
	public String getDivicePageSql(String tableName, int pageSize, int curPage,
			String condation) {
		if (tableName != null && !"".equals(tableName)) {
			if (pageSize < 0) {
				pageSize = 0;
			}
			if (curPage < 0) {
				curPage = 0;
			}
			StringBuffer sqlBefore = new StringBuffer();
			StringBuffer sqlAfter = new StringBuffer();
			sqlBefore
					.append(" select * from ( select row_number()over(order by tempColumn) tempRowNumber,* from ");
			sqlBefore.append(" ( select top " + (pageSize * curPage)
					+ " tempColumn=0,* from " + tableName + " where 1 = 1 ");
			sqlAfter.append(" ) t ) tt ");
			sqlAfter.append(" where tempRowNumber >= "
					+ ((curPage - 1) * pageSize + 1) + " ");
			if (condation != null && !"".equals(condation)) {
				if (condation.trim().startsWith("and")
						|| condation.trim().startsWith("order")) {
					sqlBefore.append(condation);
					return sqlBefore.toString() + sqlAfter.toString();
				} else {
					return null;
				}
			} else {
				return sqlBefore.toString() + sqlAfter.toString();

			}
		} else {
			return null;
		}
	}

	/**
	 * 
	 * 函数名: getDivicePageSql 功能描述: 得到分页查询语句 修改历史:
	 * 
	 * @date 2011-12-1 上午11:11:32
	 * @author yuhao
	 * @version 1.0
	 * @description 输入、输出参数:
	 * @param tableName
	 * @param pageSize
	 * @param curPage
	 * @param condation
	 * @param orderByStatement
	 * @return
	 */
	public String getDivicePageSql(String tableName, int pageSize, int curPage,
			String condation, String orderByStatement) {
		StringBuffer sqlBefore = new StringBuffer();
		StringBuffer sqlAfter = new StringBuffer();
		if (tableName != null && !"".equals(tableName)) {
			if (pageSize < 0) {
				pageSize = 0;
			}
			if (curPage < 0) {
				curPage = 0;
			}
			sqlBefore
					.append(" select * from ( select row_number()over(order by tempColumn) tempRowNumber,* from ");
			sqlBefore.append(" ( select top " + (pageSize * curPage)
					+ " tempColumn=0,* from " + tableName + " where 1 = 1 ");

			if (!isEmpty(condation) && condation.trim().startsWith("and")) {
				sqlBefore.append(condation);
			}
			if (!isEmpty(orderByStatement)
					&& orderByStatement.trim().startsWith("order")) {
				sqlBefore.append(" " + orderByStatement);
			}
			sqlAfter.append(" ) t ) tt ");
			sqlAfter.append(" where tempRowNumber >= "
					+ ((curPage - 1) * pageSize + 1) + " ");
		}
		return sqlBefore.toString() + sqlAfter.toString();
	}

	/**
	 * 
	 * 函数名: getTotalCountSql 功能描述: 查询某表的总记录数 修改历史:
	 * 
	 * @date 2011-12-1 上午11:04:12
	 * @author yuhao
	 * @version 1.0
	 * @description 输入、输出参数:
	 * @param tableName
	 * @param condition
	 * @return
	 */
	public String getTotalCountSql(String tableName, String condition) {
		StringBuffer sql = new StringBuffer();
		if (tableName != null && !"".equals(tableName)) {
			sql.append(" select count(*) from " + tableName + " where 1 = 1 ");
			if (condition != null && !"".equals(condition)) {
				sql.append(condition);
			}
		}
		return sql.toString();
	}

	// 第三部分：=======================关闭流=============================
	/**
	 * 
	 * 函数名: closeConnection 功能描述: 关闭资源 修改历史:
	 * 
	 * @date 2011-10-19 下午02:44:37
	 * @author yuhao
	 * @version 1.0
	 * @description 输入、输出参数:
	 * @param rs
	 * @param psmt
	 * @param conn
	 */
	public void closeResources(ResultSet rs, PreparedStatement psmt,
			Connection conn) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (psmt != null) {
				psmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 函数名: closeConnection 功能描述: 关闭资源 修改历史:
	 * 
	 * @date 2011-10-19 下午02:44:59
	 * @author yuhao
	 * @version 1.0
	 * @description 输入、输出参数:
	 * @param rs
	 * @param psmt
	 * @param conn
	 */
	public void closeResources(ResultSet rs, Statement stmt, Connection conn) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 函数名: closeResources 功能描述: 关闭资源 修改历史:
	 * 
	 * @date 2011-10-19 下午02:47:28
	 * @author yuhao
	 * @version 1.0
	 * @description 输入、输出参数:
	 * @param psmt
	 * @param conn
	 */
	public void closeResources(PreparedStatement psmt, Connection conn) {
		try {
			if (psmt != null) {
				psmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 函数名: closeResources 功能描述: 关闭资源 修改历史:
	 * 
	 * @date 2011-10-19 下午02:47:44
	 * @author yuhao
	 * @version 1.0
	 * @description 输入、输出参数:
	 * @param psmt
	 * @param conn
	 */
	public void closeResources(Statement stmt, Connection conn) {
		try {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 函数名: isEmpty 功能描述: 判断字符串是否为空 修改历史:
	 * 
	 * @date 2011-9-9 上午10:25:15
	 * @author heweina
	 * @version 1.0
	 * @description 输入、输出参数:
	 * @param str
	 *            字符串
	 * @return 为空返回true，否则返回false
	 */
	public boolean isEmpty(String str) {
		boolean flag = false;
		if (str == null || "".equals(str.trim())) {
			flag = true;
		}
		return flag;
	}

	// ==============第四部分 其他 ===============
	public String getValueString(Object object) {
		StringBuffer valueBuffder = new StringBuffer();
		if (object != null) {
			try {
				Field[] field = object.getClass().getDeclaredFields();
				if (field != null && field.length > 0) {
					// 循环对属性赋值
					for (int i = 0; i < field.length; i++) {
						String fieldName = field[i].getName();
						// 根据属性进行内省
						PropertyDescriptor pd = new PropertyDescriptor(
								fieldName, object.getClass());
						Method method = pd.getReadMethod();
						if (i == field.length - 1) {
							Object methodObject = method.invoke(object);
							if (methodObject == null) {
								valueBuffder.append("");
							} else {
								valueBuffder.append(methodObject.toString());
							}
						} else {
							Object methodObject = method.invoke(object);
							if (methodObject == null) {
								valueBuffder.append(",");
							} else {
								valueBuffder.append(methodObject.toString()
										+ ",");
							}
						}
					}
				}
			} catch (Exception e) {
			}

		}
		return valueBuffder.toString();
	}

	public static void main(String[] args) {
		// System.out.println(new DataTools().getInsertSql(new AvsSendSM()));

		// System.out.println((int) 'A' + "    :  " + (int) 'Z');
		// System.out.println((int) 'a' + "    :  " + (int) 'z');

	}
}
