package com.wisdom.ncl.splitter.db.splitter;

import java.sql.Connection;
import java.util.Enumeration;

import com.wisdom.ncl.splitter.db.ConnectionPool;
import com.wisdom.ncl.splitter.tools.IniTools;
import com.wisdom.ncl.splitter.tools.Log;

public class SplitterConnnectionPool {

	// sms数据库名称
	static String sms_db_name = new IniTools().get("SQLSERVER", "SMSDBName")
			.get(0);
	// smcs数据库
	static String smcs_db_name = new IniTools().get("SQLSERVER", "SMCSDBName")
			.get(0);
	// smcs2数据库
	static String smcs2_db_name = new IniTools()
			.get("SQLSERVER", "SMCS2DBName").get(0);
	// stand数据库
	static String stand_db_name = new IniTools()
			.get("SQLSERVER", "STANDDBName").get(0);
	// mms数据库
	static String mms_db_name = new IniTools().get("SQLSERVER", "MMSDBName")
			.get(0);

	// static String sms_db_name = "";
	// static String smcs_db_name = "";
	// static String smcs2_db_name = "";
	// static String stand_db_name = "";
	// static String mms_db_name = "";

	// sms数据库连接池
	public static Connection getSMSConn() {
		ConnectionPool pool = ConnectionPool.get(sms_db_name);
		if (pool != null) {
			return pool.getConnection();
		} else {
			Log.LogInfo(sms_db_name + "连接池创建失败。");
			return null;
		}
	}

	// smcs数据库连接池
	public static Connection getSMCSConn() {
		ConnectionPool pool = ConnectionPool.get(smcs_db_name);
		if (pool != null) {
			return pool.getConnection();
		} else {
			return null;
		}
	}

	// smcs2数据库连接池
	public static Connection getSMCS2Conn() {
		ConnectionPool pool = ConnectionPool.get(smcs2_db_name);
		if (pool != null) {
			return pool.getConnection();
		} else {
			return null;
		}
	}

	// stand数据库连接池
	public static Connection getSTANDConn() {
		ConnectionPool pool = ConnectionPool.get(stand_db_name);
		if (pool != null) {
			return pool.getConnection();
		} else {
			return null;
		}
	}

	// mms数据库连接池
	public static Connection getMMSConn() {
		ConnectionPool pool = ConnectionPool.get(mms_db_name);
		if (pool != null) {
			return pool.getConnection();
		} else {
			return null;
		}
	}

	/**
	 * 
	 * 功能说明: 创建数据库连接
	 */
	public static void create() {

		// 读取配置文件信息
		ConfigFile config = new ConfigFile();

		Enumeration enumm = config.getDataSourceNames();
		while (enumm.hasMoreElements()) {
			String data_source_name = (String) enumm.nextElement();
			DataSourceConfig conf = config.getConfig(data_source_name);
			String driver = conf.getDriver();
			String url = conf.getUrl();
			String user = conf.getUserName();
			String pwd = conf.getPassword();
			// TODO 上线密码需要加密
			// pwd = CommonTools.encrypt(pwd);
			int db_count = conf.getDBCount();
			// 初始化数据库连接池
			if (!ConnectionPool.create(data_source_name, driver, url, user,
					pwd, db_count)) {
				Log.LogInfo(data_source_name + "数据库连接池初始化失败!");
			} else {
				Log.LogInfo(data_source_name + "数据库连接池初始化成功,初始连接数:" + db_count);
			}
		}
	}

}
