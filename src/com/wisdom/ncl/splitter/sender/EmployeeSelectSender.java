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
 * ��Աѡ����
 * 
 * @author xiaozhen
 * 
 */
@SuppressWarnings("unchecked")
public class EmployeeSelectSender extends Sender implements Callable {

	// ���ݴ�������
	private DataTools m_data_tools = new DataTools();
	// ������Ϣ��
	private AVS_Send_SM m_send_sm;

	// ����������Ϣ
	public boolean loadTask(AVS_Send_SM avs_send_sm) {
		this.m_send_sm = avs_send_sm;
		return super.loadTask(avs_send_sm);

	}

	public Object call() throws Exception {
		Connection m_smcs_conn = SplitterConnnectionPool.getSMCSConn();
		if (m_send_sm != null) {
			// ��ȡ����Ŀ��
			String target = m_send_sm.getSend_target();
			if (target != null) {
				// ������Աѡ����Ϣ�γɻ�ȡ��Աѡ����Ϣ�б��SQL���
				String employeeSelectSql = "select a.ID from employee a,avs_txl_mobilelist b where a.mobilephone = b.mobilephone and b.pihao = '"
						+ target + "'";
				PreparedStatement psmt = null;
				ResultSet rs = null;
				try {
					psmt = m_smcs_conn.prepareStatement(employeeSelectSql);
					// ִ��SQL����ȡ��Ա��Ϣ
					rs = psmt.executeQuery();
					// ��ȡ���ͱ���
					int sendTargetRatio = m_send_sm.getSend_target_ratio();
					// ȫ������
					if (sendTargetRatio >= 100) {
						while (rs.next()) {
							subHandleSendTarget(m_smcs_conn, psmt, rs);
						}
					} else { // ���ַ���
						int compressionRatio = (int) Math
								.ceil((double) sendTargetRatio / 10);// ѹ�����ͱ���
						long count = 0;// ѭ������
						long nextTarget = 0;// ��һ������Ŀ��
						int stepSize = 10;// ����Ϊ10
						int innerLoopIndex = 0;// �ڲ�Сѭ��
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
					Log.LogInfo(EmployeeSelectSender.class + ".call��������");
				} finally {
					m_data_tools.closeResources(rs, psmt, m_smcs_conn);
				}

				// �޸�ReleaseTime
				finish();
			}
		}
		return null;
	}

	/**
	 * ������Ŀ��
	 * 
	 * @param rs
	 *            �����
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
			// ������Ա��Ż�ȡ��Ա��Ϣ
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
					+ ".subHandleSendTarget��������");
		}
	}
}
