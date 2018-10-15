package com.wisdom.ncl.splitter.tools;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;


public class CommonTools {

	/**
	 * 
	 * ����˵��:  �����㷨  
	 * @param src
	 * @return
	 */
	public static String encrypt(String src) {

		char[] srcs = src.toCharArray();
		StringBuffer buffer = new StringBuffer();
		int[] keys = { 1, 2, 5, 3, 4 };
		int j = 0;
		for (int i = 0; i < srcs.length; i++) {
			buffer.append((char) (srcs[i] + keys[j]));
			j++;
			j = j % 5;
		}
		return buffer.toString();

	}

	/**
	 * 
	 * ����˵��:  �����㷨  
	 * @param src
	 * @return
	 */
	public static String decrypt(String src) {
		char[] srcs = src.toCharArray();
		StringBuffer buffer = new StringBuffer();
		int[] keys = { 1, 2, 5, 3, 4 };
		int j = 0;
		for (int i = 0; i < srcs.length; i++) {
			buffer.append((char) (srcs[i] - keys[j]));
			j++;
			j = j % 5;
		}
		return buffer.toString();
	}
	
	/**
	 * ����˵��:  �����ļ���  
	 * @param packagePath
	 */
	public static void createFilePackage(String packagePath) {
		try {
			FileTools.createDirectory(packagePath);
		} catch (Exception e) {
			Log.LogInfo("������ʱ�ļ��ļ��д���");
		}
	}

	public static boolean isEmpty(String str) {
		boolean flag = false;
		if (str == null || "".equals(str.trim())) {
			flag = true;
		}
		return flag;
	}
	
	public static int getNowMinute() {
		Date date = new Date();
		int hour = date.getHours();
		int minute = date.getMinutes();
		return (hour * 60 + minute);
	}

	////////////////////////////////////////����ʱ��Ҫɾ��///////////////////////////////////////////
	public void setStartTime(Connection conn, int sendID){
		String currentTime = DateTools.getDate("yyyy-MM-dd HH:mm:ss.SSS");
		String sql = "update AVS_Send_SM set finishTime='" + currentTime + "' where sendID="+sendID;
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
