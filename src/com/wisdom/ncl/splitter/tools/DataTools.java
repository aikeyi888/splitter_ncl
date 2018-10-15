package com.wisdom.ncl.splitter.tools;


/**
 * Copyright (C), 2011-2012, beijing ow Co., Ltd.
 * �ļ���:     DataTools.java
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
 * ������: ���ݴ������� �޸���ʷ:
 * 
 * @author yuhao
 * @version 1.0
 * @date 2011-12-1 ����10:26:28
 * @description ����:
 */

public class DataTools {
	// =============��һ���֣� resultToBean===================
	/**
	 * 
	 * ������: resultSetToList ��������: resultSetת����List���� �޸���ʷ:
	 * 
	 * @date 2011-12-1 ����10:32:13
	 * @author yuhao
	 * @version 1.0
	 * @description
	 * 
	 *              ���롢�������:
	 * @param objclass
	 * @param rs
	 * @return
	 */
	public List<Object> resultSetToList(Class objclass, ResultSet rs) {
		List<Object> list = null;
		// rs ��Ϊ�յ�ʱ��ִ��
		if (rs != null) {
			list = new ArrayList<Object>();
			Field[] field = objclass.getDeclaredFields();
			try {
				// ��rs��ȡֵ
				while (rs.next()) {
					// �����ȡ������ ע�⣺���ﵱ��������ʱ��Ϊ��������Խ��г�ʼ��
					Object addObject = objclass.getConstructor().newInstance();
					// ѭ�������Ը�ֵ
					for (int i = 0; i < field.length; i++) {
						String fieldName = field[i].getName();
						// �������Խ�����ʡ
						PropertyDescriptor pd = new PropertyDescriptor(
								fieldName, addObject.getClass());
						Method method = pd.getWriteMethod();
						// ��ȡ���ݿ��Ӧ��ÿһ��ֵ
						Object fieldValue = rs.getObject(fieldName);
						// �������ֵΪnullʱ��ִ�з������
						if (fieldValue != null) {
							// ����ִ��set����
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
	 * ������: resultSetToList ��������: resultSetת����List �޸���ʷ:
	 * 
	 * @date 2011-12-1 ����10:31:57
	 * @author yuhao
	 * @version 1.0
	 * @description ���롢�������:
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
					// ����������Ͳ�ƥ�������
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
	 * ������: resultSetToObject ��������: resultSetת����Object �޸���ʷ:
	 * 
	 * @date 2011-12-1 ����10:34:38
	 * @author yuhao
	 * @version 1.0
	 * @description ���롢�������:
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
			// ע��ָ���Ƿ��²�
			// rs.next();
			addObject = objectClass.getConstructor().newInstance();
			for (int i = 0; i < field.length; i++) {
				String fieldName = field[i].getName();
				PropertyDescriptor pd = new PropertyDescriptor(fieldName,
						addObject.getClass());
				Method method = pd.getWriteMethod();
				// ��ȡ���ݿ��Ӧ��ÿһ��ֵ
				Object fieldValue = rs.getObject(fieldName);
				if (fieldValue != null) {
					// ����ִ��set����
					method.invoke(addObject, fieldValue);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return addObject;
	}

	// ==================�ڶ����֣�beqanToSql=====================
	/**
	 * 
	 * ������: getInsertSql ��������: �õ�����sql��� �޸���ʷ:
	 * 
	 * @date 2011-11-30 ����02:21:17
	 * @author yuhao
	 * @version 1.0
	 * @description ���롢�������:
	 * @param object
	 * @return
	 */
	public String getInsertSql(Object object) {
		return this.getInsertSql(object, null, null);
	}

	/**
	 * 
	 * ������: getInsertSql ��������: ���ݱ����õ�����sql��� �޸���ʷ:
	 * 
	 * @date 2011-11-30 ����02:21:41
	 * @author yuhao
	 * @version 1.0
	 * @description ���롢�������:
	 * @param object
	 * @param tableName
	 * @return
	 */
	public String getInsertSql(Object object, String tableName) {
		return this.getInsertSql(object, tableName, null);
	}

	/**
	 * 
	 * ������: getInsertSql ��������: �õ�����sql��� �޸���ʷ:
	 * 
	 * @date 2011-12-1 ����10:17:48
	 * @author yuhao
	 * @version 1.0
	 * @description ���롢�������:
	 * @param object
	 * @param lostFieldList
	 * @return
	 */
	public String getInsertSql(Object object, List<String> lostFieldList) {
		return this.getInsertSql(object, null, lostFieldList);
	}

	/**
	 * 
	 * ������: getInsertSql ��������: �õ�������� �޸���ʷ:
	 * 
	 * @date 2011-11-30 ����02:22:16
	 * @author yuhao
	 * @version 1.0
	 * @description ���롢�������:
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
		// ����Ϊ�ղ�������
		if (object != null) {
			// û���ṩtableName����Ϣ
			if (tableName == null || "".equals(tableName)) {
				insertTableName = object.getClass().getSimpleName();
			} else {
				insertTableName = tableName;
			}
			sqlInseretBuffer.append("insert into " + insertTableName + " (");
			sqlValueBuffer.append(" values( ");
			// �������Լ��ϲ�Ϊ�յ����
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
					// �õ���������
					if (i == fieldList.size() - 1) {
						sqlInseretBuffer.append(fieldName + " ) ");
					} else {
						sqlInseretBuffer.append(fieldName + ",");
					}
					// �õ�value����
					// �ж��������� ��ʱ֧���⼸�� �Ժ��ټ�
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
	 * ������: getUpdateSql ��������: �õ��޸���� �޸���ʷ:
	 * 
	 * @date 2011-11-30 ����02:24:50
	 * @author yuhao
	 * @version 1.0
	 * @description ���롢�������:
	 * @param object
	 * @return
	 */
	public String getUpdateSql(Object object, List<String> pkList) {
		return this.getUpdateSql(object, null, null, pkList);
	}

	/**
	 * 
	 * ������: getUpdateSql ��������: �õ��޸���� �޸���ʷ:
	 * 
	 * @date 2011-11-30 ����02:25:10
	 * @author yuhao
	 * @version 1.0
	 * @description ���롢�������:
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
	 * ������: getUpdateSql ��������: �õ��޸ĵ�sql��� �޸���ʷ:
	 * 
	 * @date 2011-12-1 ����10:19:13
	 * @author yuhao
	 * @version 1.0
	 * @description ���롢�������:
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
	 * ������: getUpdateSql ��������: �õ��޸���� �޸���ʷ:
	 * 
	 * @date 2011-11-30 ����02:25:25
	 * @author yuhao
	 * @version 1.0
	 * @description ���롢�������:
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
		// ����Ϊ�� �б�Ҫִ��
		if (object != null && pkList != null && pkList.size() > 0) {
			sqlUpdateBuffer.append("update ");
			sqlCondationBuffer.append(" where ");
			// û���ṩtableName����Ϣ
			if (tableName == null || "".equals(tableName)) {
				updateTableName = object.getClass().getSimpleName();
			} else {
				updateTableName = tableName;
			}
			sqlUpdateBuffer.append(updateTableName + " set ");
			// �������Լ��ϲ�Ϊ�յ����
			if (lostFieldList != null && lostFieldList.size() > 0) {
				fieldList = getFieldNameTypeValue(object, lostFieldList);
			} else {
				fieldList = getFieldNameTypeValue(object);
			}
			// ���Բ�Ϊ�յ������
			if (fieldList != null && fieldList.size() > 0) {
				for (int i = 0; i < fieldList.size(); i++) {
					FieldHelp fieldHelp = fieldList.get(i);
					String fieldName = fieldHelp.getFieldName();
					String fieldType = fieldHelp.getFieldType();
					String fieldValue = fieldHelp.getFieldValue();
					// �ж��������� ��ʱ֧���⼸�� �Ժ��ټ�
					if (fieldType.contains("int")) {// ����
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
					} else if (fieldType.contains("long")) {// ����
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
					} else if (fieldType.contains("Timestamp")) {// ʱ��
						if (i == fieldList.size() - 1) {
							if (fieldValue == null || "".equals(fieldValue)) {// �ж�ʱ���Ƿ�Ϊ��
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
					} else {// �ַ���
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
		// ��������к���and ��ȡ������and
		if (sqlCondationBuffer.toString().contains("and")) {
			String condation = sqlCondationBuffer.substring(0,
					sqlCondationBuffer.lastIndexOf("and") - 1);
			sqlUpdateBuffer.append(condation);
		}
		return sqlUpdateBuffer.toString();
	}

	/**
	 * 
	 * ������: getDeleteSql ��������: �õ�ɾ����� �޸���ʷ:
	 * 
	 * @date 2011-11-30 ����02:26:18
	 * @author yuhao
	 * @version 1.0
	 * @description ���롢�������:
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
						// �ж��������� ��ʱ֧���⼸�� �Ժ��ټ�
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
	 * ������: getDeleteSql ��������: �õ�ɾ����� �޸���ʷ:
	 * 
	 * @date 2011-12-1 ����09:43:18
	 * @author yuhao
	 * @version 1.0
	 * @description ���롢�������:
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
	 * ������: getFieldAndValue ��������: �õ����е����Լ��䷽�� �޸���ʷ:
	 * 
	 * @date 2011-11-30 ����02:28:10
	 * @author yuhao
	 * @version 1.0
	 * @description ���롢�������:
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
			// ���������Լ��ϲ�Ϊ��
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
	 * ������: getFieldAndValue ��������: �õ����Լ���ֵ �޸���ʷ:
	 * 
	 * @date 2011-11-30 ����02:28:37
	 * @author yuhao
	 * @version 1.0
	 * @description ���롢�������:
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
	 * ������: getDivicePageSql ��������: �õ���ҳ��ѯ��� �޸���ʷ:
	 * 
	 * @date 2011-12-1 ����11:09:25
	 * @author yuhao
	 * @version 1.0
	 * @description ���롢�������:
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
	 * ������: getDivicePageSql ��������: �õ���ҳ��ѯ��� �޸���ʷ:
	 * 
	 * @date 2011-12-1 ����11:11:32
	 * @author yuhao
	 * @version 1.0
	 * @description ���롢�������:
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
	 * ������: getTotalCountSql ��������: ��ѯĳ����ܼ�¼�� �޸���ʷ:
	 * 
	 * @date 2011-12-1 ����11:04:12
	 * @author yuhao
	 * @version 1.0
	 * @description ���롢�������:
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

	// �������֣�=======================�ر���=============================
	/**
	 * 
	 * ������: closeConnection ��������: �ر���Դ �޸���ʷ:
	 * 
	 * @date 2011-10-19 ����02:44:37
	 * @author yuhao
	 * @version 1.0
	 * @description ���롢�������:
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
	 * ������: closeConnection ��������: �ر���Դ �޸���ʷ:
	 * 
	 * @date 2011-10-19 ����02:44:59
	 * @author yuhao
	 * @version 1.0
	 * @description ���롢�������:
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
	 * ������: closeResources ��������: �ر���Դ �޸���ʷ:
	 * 
	 * @date 2011-10-19 ����02:47:28
	 * @author yuhao
	 * @version 1.0
	 * @description ���롢�������:
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
	 * ������: closeResources ��������: �ر���Դ �޸���ʷ:
	 * 
	 * @date 2011-10-19 ����02:47:44
	 * @author yuhao
	 * @version 1.0
	 * @description ���롢�������:
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
	 * ������: isEmpty ��������: �ж��ַ����Ƿ�Ϊ�� �޸���ʷ:
	 * 
	 * @date 2011-9-9 ����10:25:15
	 * @author heweina
	 * @version 1.0
	 * @description ���롢�������:
	 * @param str
	 *            �ַ���
	 * @return Ϊ�շ���true�����򷵻�false
	 */
	public boolean isEmpty(String str) {
		boolean flag = false;
		if (str == null || "".equals(str.trim())) {
			flag = true;
		}
		return flag;
	}

	// ==============���Ĳ��� ���� ===============
	public String getValueString(Object object) {
		StringBuffer valueBuffder = new StringBuffer();
		if (object != null) {
			try {
				Field[] field = object.getClass().getDeclaredFields();
				if (field != null && field.length > 0) {
					// ѭ�������Ը�ֵ
					for (int i = 0; i < field.length; i++) {
						String fieldName = field[i].getName();
						// �������Խ�����ʡ
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
