package com.wisdom.ncl.splitter.sender;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Callable;

import com.wisdom.ncl.splitter.Sender;
import com.wisdom.ncl.splitter.data.AVS_Send_SM;
import com.wisdom.ncl.splitter.data.SendTarget;
import com.wisdom.ncl.splitter.db.splitter.SplitterConnnectionPool;
import com.wisdom.ncl.splitter.tools.DataTools;
import com.wisdom.ncl.splitter.tools.Log;

/**
 * 内员选择发送
 * 
 * @author xiaozhen
 * 
 */
@SuppressWarnings("unchecked")
public class EmployeeSelectSender extends Sender implements Callable {

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
			// 获取发送目标
			String target = m_send_sm.getSend_target();
			if (target != null) {
				// 根据内员选择信息形成获取内员选择信息列表的SQL语句
				String employeeSelectSql = "select a.ID from employee a,avs_txl_mobilelist b where a.mobilephone = b.mobilephone and b.pihao = '"
						+ target + "'";
				PreparedStatement psmt = null;
				ResultSet rs = null;
				try {
					psmt = m_smcs_conn.prepareStatement(employeeSelectSql);
					// 执行SQL，获取内员信息
					rs = psmt.executeQuery();
					// 获取发送比例
					int sendTargetRatio = m_send_sm.getSend_target_ratio();
					// 全部发送
					if (sendTargetRatio >= 100) {
						while (rs.next()) {
							subHandleSendTarget(m_smcs_conn, psmt, rs);
						}
					} else { // 部分发送
						int compressionRatio = (int) Math
								.ceil((double) sendTargetRatio / 10);// 压缩发送比例
						long count = 0;// 循环次数
						long nextTarget = 0;// 下一个发送目标
						int stepSize = 10;// 步长为10
						int innerLoopIndex = 0;// 内部小循环
						while (rs.next()) {
							if (nextTarget <= count
									&& count < (nextTarget + compressionRatio)) {
								subHandleSendTarget(m_smcs_conn, psmt, rs);
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
					Log.LogInfo(EmployeeSelectSender.class + ".call方法错误！");
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
	private void subHandleSendTarget(Connection conn, PreparedStatement psmt,
			ResultSet rs) {
		int employeeID;
		ResultSet emloyeeResultSet = null;
		try {
			employeeID = rs.getInt("ID");
			String strSql = "select MobilePhone,EmployeeName from Employee where ID = "
					+ employeeID;
			psmt = conn.prepareStatement(strSql);
			// 根据内员编号获取内员信息
			emloyeeResultSet = psmt.executeQuery();
			if (emloyeeResultSet.next()) {
				SendTarget sendTarget = new SendTarget();
				sendTarget.setMobilephone(emloyeeResultSet
						.getString("MobilePhone"));
				sendTarget.setSendTargetName(emloyeeResultSet
						.getString("EmployeeName"));
				sendTarget.setSMContent(m_send_sm.getSmcontent());
				sendTarget.setProvinceID(m_send_sm.getProvince_id());
				sendTarget.setTableName(m_send_sm.getTab_name());
				handleSendTarget(sendTarget);
			}
			if (emloyeeResultSet != null) {
				emloyeeResultSet.close();
			}
		} catch (SQLException e) {
			Log.LogInfo(EmployeeSelectSender.class
					+ ".subHandleSendTarget方法错误！");
		}
	}
}
