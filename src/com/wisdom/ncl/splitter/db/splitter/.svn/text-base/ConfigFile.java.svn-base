package com.wisdom.ncl.splitter.db.splitter;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description: 获取.conf文件中的配置信息
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * 
 * <p>
 * Company: 东方般若
 * </p>
 * 
 * @author : Jacky Wang 2007-01-12
 * @version 1.0
 */

public class ConfigFile {
	private Hashtable cofing_list = new Hashtable();

	// private int m_splitter_way = 0;
	// private int m_splitter_length = 64;
	/**
	 * 读取配置文件
	 */
	public ConfigFile() {

		Map dbMap = new HashMap();
		String file_path = System.getProperty("user.dir")
				+ "/config/splitter.ini";

		try {
			// ==================sms配置文件信息=========================
			DataSourceConfig smsConfig = new DataSourceConfig();
			String sms_db_name = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "SMSDBName", "");
			String sms_dbType = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "SMSDBTYPE", "");
			String sms_dbhost = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "SMSIP", "");
			String sms_Catalog = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "SMSInitCatalog", "");
			String sms_UserID = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "SMSUserID", "");
			String sms_Password = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "SMSPassword", "");
			String sms_count = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "SMSConnectionCount", "");
			String sms_port = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "SMSPORT", "");

			smsConfig.setDBName(sms_Catalog);
			smsConfig.setDBCount(Integer.parseInt(sms_count));
			smsConfig.setDBType(Integer.parseInt(sms_dbType));
			smsConfig.setPort(sms_port);
			smsConfig.setServerIP(sms_dbhost);
			smsConfig.setUserName(sms_UserID);
			smsConfig.setPassword(sms_Password);
			dbMap.put(sms_db_name, smsConfig);
			// db_list.add(smsDatadesc);

			// ==================smcs配置文件信息=========================
			DataSourceConfig smcsConfig = new DataSourceConfig();
			String smcs_db_name = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "SMCSDBName", "");
			String smcs_dbType = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "SMCSDBTYPE", "");
			String smcs_dbhost = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "SMCSDBIP", "");
			String smcs_Catalog = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "SMCSInitCatalog", "");
			String smcs_UserID = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "SMCSUserID", "");
			String smcs_Password = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "SMCSPassword", "");
			String smcs_count = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "SMCSConnectionCount", "");
			String smcs_port = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "SMCSPORT", "");

			smcsConfig.setDBName(smcs_Catalog);
			smcsConfig.setDBCount(Integer.parseInt(smcs_count));
			smcsConfig.setDBType(Integer.parseInt(smcs_dbType));
			smcsConfig.setPort(smcs_port);
			smcsConfig.setServerIP(smcs_dbhost);
			smcsConfig.setUserName(smcs_UserID);
			smcsConfig.setPassword(smcs_Password);
			dbMap.put(smcs_db_name, smcsConfig);

			// ==================smcs2配置文件信息=========================
			DataSourceConfig smcs2Config = new DataSourceConfig();
			String smcs2_db_name = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "SMCS2DBName", "");
			String smcs2_dbType = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "SMCS2DBTYPE", "");
			String smcs2_dbhost = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "SMCS2IP", "");
			String smcs2_Catalog = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "SMCS2InitCatalog", "");
			String smcs2_UserID = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "SMCS2UserID", "");
			String smcs2_Password = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "SMCS2Password", "");
			String smcs2_count = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "SMCS2ConnectionCount", "");
			String smcs2_port = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "SMCS2PORT", "");
			smcs2Config.setDBName(smcs2_Catalog);
			smcs2Config.setDBCount(Integer.parseInt(smcs2_count));
			smcs2Config.setDBType(Integer.parseInt(smcs2_dbType));
			smcs2Config.setPort(smcs2_port);
			smcs2Config.setServerIP(smcs2_dbhost);
			smcs2Config.setUserName(smcs2_UserID);
			smcs2Config.setPassword(smcs2_Password);
			dbMap.put(smcs2_db_name, smcs2Config);

			// ==================stand配置文件信息=========================
			DataSourceConfig standConfig = new DataSourceConfig();
			String stand_db_name = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "STANDDBName", "");
			String stand_dbType = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "STANDDBTYPE", "");
			String stand_dbhost = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "STANDIP", "");
			String stand_Catalog = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "STANDInitCatalog", "");
			String stand_UserID = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "STANDUserID", "");
			String stand_Password = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "STANDPassword", "");
			String stand_count = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "STANDConnectionCount", "");
			String stand_port = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "STANDPORT", "");
			standConfig.setDBName(stand_Catalog);
			standConfig.setDBCount(Integer.parseInt(stand_count));
			standConfig.setDBType(Integer.parseInt(stand_dbType));
			standConfig.setPort(stand_port);
			standConfig.setServerIP(stand_dbhost);
			standConfig.setUserName(stand_UserID);
			standConfig.setPassword(stand_Password);
			dbMap.put(stand_db_name, standConfig);

			// ==================mms配置文件信息=========================

			DataSourceConfig mmsConfig = new DataSourceConfig();
			String mms_db_name = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "MMSDBName", "");
			String mms_dbType = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "MMSDBTYPE", "");
			String mms_dbhost = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "MMSIP", "");
			String mms_Catalog = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "MMSInitCatalog", "");
			String mms_UserID = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "MMSUserID", "");
			String mms_Password = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "MMSPassword", "");
			String mms_count = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "MMSConnectionCount", "");
			String mms_port = ConfigurationFile.getProfile(file_path,
					"SQLSERVER", "MMSPORT", "");
			mmsConfig.setDBName(mms_Catalog);
			mmsConfig.setDBCount(Integer.parseInt(mms_count));
			mmsConfig.setDBType(Integer.parseInt(mms_dbType));
			mmsConfig.setPort(mms_port);
			mmsConfig.setServerIP(mms_dbhost);
			mmsConfig.setUserName(mms_UserID);
			mmsConfig.setPassword(mms_Password);
			dbMap.put(mms_db_name, mmsConfig);

			if (dbMap != null && dbMap.size() > 0) {

				Set dbNameSet = dbMap.keySet();
				Iterator it = dbNameSet.iterator();
				while (it.hasNext()) {
					String dbName = (String) it.next();
					cofing_list.put(dbName, dbMap.get(dbName));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取数据源配置
	 * 
	 * @param data_source_name
	 *            String
	 * @return DataSourceConfig
	 */
	public DataSourceConfig getConfig(String data_source_name) {
		return (DataSourceConfig) cofing_list.get((String) data_source_name);
	}

	/**
	 * 获取数据源名称
	 * 
	 * @return Enumeration
	 */
	public Enumeration getDataSourceNames() {
		return cofing_list.keys();
	}

}
