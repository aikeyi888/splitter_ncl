package com.wisdom.ncl.splitter.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.wisdom.ncl.splitter.db.splitter.SplitterConnnectionPool;
import com.wisdom.ncl.splitter.tools.DataTools;
import com.wisdom.ncl.splitter.tools.IniTools;
import com.wisdom.ncl.splitter.tools.Log;

public class TempTable {
	// 数据处理工具类
	private DataTools m_dt_tools = new DataTools();

	/**
	 * 
	 * 功能说明: 加载过滤表名称 
	 * @return
	 */
	public List<String> loadTempTable(){
		// 查询过滤表数量
		int int_tab_count = getTempTableCount();
		int taskCount = Integer.parseInt(new IniTools().get("TaskCount",
				"taskCount").get(0));
		int int_taskcount = taskCount * 2;
		// 新增少于最大线程的数据表
		if (int_tab_count < int_taskcount) {
			for (int i = int_tab_count+1; i <= int_taskcount; i++) {
				createTempTable(i);
			}
		}		
		String str_sql = "SELECT NAME FROM SYSOBJECTS WHERE XTYPE='U'  AND NAME LIKE 'SM_TEMP_GROUPSENDING_SM_LIST%' AND NAME <>'SM_TEMP_GROUPSENDING_SM_LIST'";
		Connection smcs_conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String> lst_tabname = new ArrayList<String>();
		try {
			smcs_conn = SplitterConnnectionPool.getSMCSConn();
			stmt = smcs_conn.prepareStatement(str_sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String str_tabname = rs.getString("NAME");
				lst_tabname.add(str_tabname);
			}
		} catch (Exception e) {
			Log.LogInfo(TempTable.class + "loadTempTable()方法错误!");
		} finally {
			m_dt_tools.closeResources(rs, stmt, smcs_conn);
		}
		return lst_tabname;
	}
	/**
	 * 
	 * 功能说明: 查询过滤表数量 
	 * @return
	 */
	public int getTempTableCount(){
		String str_sql = "SELECT count(*) TAB_COUNT FROM SYSOBJECTS WHERE XTYPE='U'  AND NAME LIKE 'SM_TEMP_GROUPSENDING_SM_LIST%' AND NAME <>'SM_TEMP_GROUPSENDING_SM_LIST'";
		Connection smcs_conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int int_tab_count = 0;
		try {
			smcs_conn = SplitterConnnectionPool.getSMCSConn();
			stmt = smcs_conn.prepareStatement(str_sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				int_tab_count = rs.getInt("TAB_COUNT");
			}
		} catch (Exception e) {
			Log.LogInfo(TempTable.class + "getTempTableCount()方法错误!");
		} finally {
			m_dt_tools.closeResources(rs, stmt, smcs_conn);
		}
		return int_tab_count;
	}
	public void createTempTable(int i){
		// 建表语句
		StringBuffer sb_sql = new StringBuffer();
		sb_sql.append("CREATE TABLE ");
		sb_sql.append("[dbo].[SM_Temp_GroupSending_SM_List" + i + "](");
		sb_sql.append("[MobilePhone] [varchar](20) NOT NULL,");
		sb_sql.append("[Pihao] [varchar](50) NOT NULL,");
		sb_sql.append("[SMContent] [varchar](800) NOT NULL,");
		sb_sql.append("[SendType] [varchar](50) NOT NULL,");
		sb_sql.append("CONSTRAINT [PK_SM_Temp_GS_SM_List" + i);
		sb_sql.append("] PRIMARY KEY CLUSTERED");
		sb_sql.append("([MobilePhone] ASC,[Pihao] ASC,[SMContent] ASC");
		sb_sql.append(")WITH (PAD_INDEX = OFF,");
		sb_sql.append("STATISTICS_NORECOMPUTE = OFF,");
		sb_sql.append("IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ");
		sb_sql.append("ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY])");
		sb_sql.append(" ON [PRIMARY]");
		Connection smcs_conn = null;
		Statement stmt = null;
		try {
			smcs_conn = SplitterConnnectionPool.getSMCSConn();
			stmt = smcs_conn.createStatement();
			stmt.executeUpdate(sb_sql.toString());
			
		} catch (Exception e) {
			Log.LogInfo(TempTable.class + "createTempTable()方法错误!");
		} finally {
			m_dt_tools.closeResources(stmt, smcs_conn);
		}
	}
	
}
