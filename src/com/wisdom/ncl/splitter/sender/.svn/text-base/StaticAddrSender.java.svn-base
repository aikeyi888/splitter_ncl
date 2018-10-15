package com.wisdom.ncl.splitter.sender;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Callable;

import com.wisdom.ncl.splitter.Sender;
import com.wisdom.ncl.splitter.data.AVS_Send_SM;
import com.wisdom.ncl.splitter.data.SendTarget;
import com.wisdom.ncl.splitter.db.splitter.SplitterConnnectionPool;
import com.wisdom.ncl.splitter.tools.DataTools;
import com.wisdom.ncl.splitter.tools.Log;

/**
 * 静态通讯录发送
 * 
 * @author xiaozhen
 * 
 */
@SuppressWarnings("unchecked")
public class StaticAddrSender extends Sender implements Callable {

	// 数据处理工具类
	private DataTools m_data_tools = new DataTools();
	// 任务信息类
	private AVS_Send_SM m_send_sm;

	// 载入任务信息
	public boolean loadTask(AVS_Send_SM avs_send_sm) {
		this.m_send_sm = avs_send_sm;
		return super.loadTask(avs_send_sm);
	}

	public Object call() throws Exception {
		Connection m_smcs_conn = SplitterConnnectionPool.getSMCSConn();
		if (m_send_sm != null) {
			// 获取发送目标的SQL语句
			String target = m_send_sm.getSend_target();
			if (target != null) {
				PreparedStatement psmt = null;
				ResultSet rs = null;
				try {
					psmt = m_smcs_conn.prepareStatement(target);
					// 执行SQL，获取静态通讯录信息
					rs = psmt.executeQuery();

					// 获取发送比例
					int sendTargetRatio = m_send_sm.getSend_target_ratio();
					if (sendTargetRatio >= 100) {
						while (rs.next()) {
							subHandleSendTarget(target, m_smcs_conn, rs);
						}
					} else {
						int compressionRatio = (int) Math
								.ceil((double) sendTargetRatio / 10);// 压缩发送比例
						long count = 0;// 循环次数
						long nextTarget = 0;// 下一个发送目标
						int stepSize = 10;// 步长为10
						int innerLoopIndex = 0;// 内部小循环
						while (rs.next()) {
							if (nextTarget <= count
									&& count < (nextTarget + compressionRatio)) {
								subHandleSendTarget(target, m_smcs_conn, rs);
							}
							count++;
							if (innerLoopIndex + 1 == stepSize) {
								innerLoopIndex = 0;
								nextTarget += stepSize;
							} else {
								innerLoopIndex++;
							}
						}
					}
				} catch (Exception e) {
					Log.LogInfo(StaticAddrSender.class + ".call方法错误！");
				} finally {
					m_data_tools.closeResources(rs, psmt, m_smcs_conn);
				}

				// 修改ReleaseTime
				finish();
			}
		}
		return null;
	}

	/**
	 * 处理发送目标
	 * 
	 * @param rs
	 *            结果集
	 */
	private void subHandleSendTarget(String target, Connection conn,
			ResultSet rs) {
		String mobilePhone = "";
		String strName = "";
		String strCustomerID = "";
		String strInsurID = "";
		try {
			mobilePhone = rs.getString("MobilePhone");
		} catch (SQLException e) {
			Log.LogInfo(StaticAddrSender.class + ".subHandleSendTarget方法错误！");
		}
		String strResult;

		strResult = getFieldName(target, "姓名", conn);
		if (strResult != null) {
			strName = strResult;
		}

		strResult = getFieldName(target, "客户号", conn);
		if (strResult != null) {
			strCustomerID = strResult;
		}

		strResult = getFieldName(target, "保单号", conn);
		if (strResult != null) {
			strInsurID = strResult;
		}

		// 替换标签内容
		String smContent = m_send_sm.getSmcontent();
		// String strChineseName = getFieldChineseName(
		// target, conn);

		String[] fields = target.split("\\|");
		for (int i = 0; i < fields.length; i++) {
			strResult = getFieldName(target, fields[i], conn);
			String strOld = "<" + fields[i] + ">";
			smContent.replace(strOld, strResult);
		}
		SendTarget sendTarget = new SendTarget();
		sendTarget.setMobilephone(mobilePhone);
		sendTarget.setSendTargetName(strName);
		sendTarget.setSMContent(smContent);
		sendTarget.setCustomerID(strCustomerID);
		sendTarget.setInsurID(strInsurID);
		sendTarget.setProvinceID(m_send_sm.getProvince_id());
		sendTarget.setTableName(m_send_sm.getTab_name());
		handleSendTarget(sendTarget);
	}

	/**
	 * 获取列名
	 * 
	 * @param sendTarget
	 * @param label
	 * @param conn
	 * @return
	 */
	public String getFieldName(String sendTarget, String label,
			Connection smcsConn) {
		String tableName = "";
		String strReturn = "";
		// 根据sendTarget得到表名
		int start = sendTarget.indexOf("Static_Addr_");
		int end = sendTarget.indexOf(" ", start);
		if (end == -1) {
			tableName = sendTarget.substring(start);
		} else {
			tableName = sendTarget.substring(start, end - start + 1);
		}
		String sql = "select FieldName from SM_Static_Address_Fields where ChineseName= '"
				+ label + "' and TableName='" + tableName + "'";
		if (tableName != "") {
			Statement st;
			try {
				st = smcsConn.createStatement();
				ResultSet strRs = st.executeQuery(sql);
				while (strRs.next()) {
					strReturn = strRs.getString("FieldName");
				}
				if (strRs != null) {
					strRs.close();
				}
				if (st != null) {
					st.close();
				}
			} catch (Exception e) {
				Log
				.LogInfo(StaticAddrSender.class
						+ ".getFieldName方法错误！");
			}
		}
		return strReturn;
	}

	/**
	 * 获取列名所对应的中文名
	 * 
	 * @param sendTarget
	 * @param conn
	 * @return
	 */
	public String getFieldChineseName(String sendTarget, Connection smcsConn) {
		String strReturn = "";
		String tableName;
		// 根据sendTarget得到表名
		int start = sendTarget.indexOf("Static_Addr_");
		int end = sendTarget.indexOf(" ", start);
		if (end == -1) {
			tableName = sendTarget.substring(start);
		} else {
			tableName = sendTarget.substring(start, end - start + 1);
		}
		String sql = "select ChineseName from SM_Static_Address_Fields where TableName='"
				+ tableName + "'";
		if (tableName != "") {
			Statement st;
			try {
				st = smcsConn.createStatement();
				ResultSet strRs = st.executeQuery(sql);
				while (strRs.next()) {
					strReturn = strRs.getString("ChineseName");
					strReturn += "|";
				}
				if (strRs != null) {
					strRs.close();
				}
				if (st != null) {
					st.close();
				}
			} catch (Exception e) {
				Log
				.LogInfo(StaticAddrSender.class
						+ ".getFieldChineseName方法错误！");
			}
		}
		return strReturn;

	}
}
