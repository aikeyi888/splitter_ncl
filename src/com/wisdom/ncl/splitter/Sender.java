package com.wisdom.ncl.splitter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.Callable;

import net.sf.ehcache.Element;

import com.wisdom.ncl.splitter.base.DateUtil;
import com.wisdom.ncl.splitter.data.AVS_Send_SM;
import com.wisdom.ncl.splitter.data.SendTarget;
import com.wisdom.ncl.splitter.db.splitter.SplitterConnnectionPool;
import com.wisdom.ncl.splitter.tools.CommonTools;
import com.wisdom.ncl.splitter.tools.DataTools;
import com.wisdom.ncl.splitter.tools.DateTools;
import com.wisdom.ncl.splitter.tools.EhcacheTools;
import com.wisdom.ncl.splitter.tools.IniTools;
import com.wisdom.ncl.splitter.tools.Log;
import com.wisdom.ncl.splitter.tools.StringTools;

public abstract class Sender implements Callable {

	// �����Գ�Ա���� ����ʽ����
	AVS_Send_SM m_send_task;
	// ���ڴ�����
	private DataTools m_dataTools = new DataTools();
	// ���湤����
	private EhcacheTools m_eh_tools = new EhcacheTools();

	// ÿ���߳�Ĭ��ִ�еķ���
	public abstract Object call() throws Exception;

	/**
	 * 
	 * ����˵��: ����������Ϣ
	 * 
	 * @param send_task
	 * @param smsConn
	 * @param smcsConn
	 * @param smcs2Conn
	 * @param standConn
	 * @param mmsConn
	 * @return
	 */
	public boolean loadTask(AVS_Send_SM send_task) {
		m_send_task = (AVS_Send_SM) send_task;
		return true;
	}

	// ������Ŀ��
	public final void handleSendTarget(SendTarget send_target) {

		// �Ƿ���а��������ˣ� ON����ʹ�� OFF����ر�
		String isDueByWhiteList = new IniTools().get("handleSenderTarget",
				"isOpenWhiteList").get(0);

		// �Ƿ���а��������ˣ� ON����ʹ�� OFF����ر�
		String isDueByBlackList = new IniTools().get("handleSenderTarget",
				"isOpenBlackList").get(0);

		// �ظ������ʶ
		boolean repeatFlag = checkRepeated(send_target);
		// ���ظ�
		if (repeatFlag) {
			// ����������
			if ("ON".equalsIgnoreCase(isDueByWhiteList)
					&& "ON".equalsIgnoreCase(isDueByBlackList)) {// ������ ��������ͬʱ����
				// ���ں�����
				boolean blackFlag = isInBlackList(send_target);
				if (!blackFlag) {
					// �ڰ�����
					boolean whiteFlag = isInWhiteList(send_target);
					if (whiteFlag) {
						saveSMSToDB(send_target);
					}
				}
			} else if ("ON".equalsIgnoreCase(isDueByWhiteList)
					&& !"ON".equalsIgnoreCase(isDueByBlackList)) {// ����������������������
				// �ڰ�����
				boolean whiteFlag = isInWhiteList(send_target);
				if (whiteFlag) {
					saveSMSToDB(send_target);
				}
			} else if (!"ON".equalsIgnoreCase(isDueByWhiteList)
					&& "ON".equalsIgnoreCase(isDueByBlackList)) {// ����������������������

				// ���ں�����
				boolean blackFlag = isInBlackList(send_target);
				if (!blackFlag) {
					saveSMSToDB(send_target);
				}
			} else {// �ڡ���������������
				saveSMSToDB(send_target);
			}
		}
	}

	/**
	 * 
	 * ����˵��: ���������Ϣ���
	 * 
	 * @param send_target
	 */
	private void saveSMSToDB(SendTarget send_target) {
		Connection save_smcs2_conn = SplitterConnnectionPool.getSMCS2Conn();
		String now_datetime = DateTools.getDate("yyyy-MM-dd HH:mm:ss:SSS");
		StringBuffer sqlBuffer = new StringBuffer();
		String desc = "";
		if ("14".equals(m_send_task.getSend_target_type())) {
			desc = StringTools
					.changeSqToDq(createMelonSendTargetDesc(send_target));
			// 2012��1��19�� By ���ɷ����
			sqlBuffer
					.append(" insert into SM_Send_SM_List (SendTarget,SMContent,ServiceID,SendTargetDesc,Priority,RCompleteTimeBegin,RCompleteTimeEnd,RCompleteHourBegin,RCompleteHourEnd,RoadBy,pad2,RequestTime,FeeValue,pad3,pad4,pad5) ");
			sqlBuffer.append("values ('" + send_target.getMobilephone() + "','"
					+ StringTools.changeSqToDq(send_target.getSMContent())
					+ "','" + m_send_task.getSend_type() + "','" + desc + "' ,"
					+ m_send_task.getPriority() + ",'"
					+ send_target.getRCompleteTimeBegin() + "' ,'"
					+ send_target.getRCompleteTimeEnd() + "','"
					+ send_target.getRCompleteHourBegin() + "','"
					+ send_target.getRCompleteHourEnd() + "',"
					+ m_send_task.getRoad_used() + ",'"
					+ m_send_task.getSend_id() + "','" + now_datetime + "',0,'"
					+ m_send_task.getService_no() + "','','')");

		} else {
			desc = StringTools.changeSqToDq(createSendTargetDesc(send_target));

			sqlBuffer
					.append(" insert into SM_Send_SM_List (SendTarget,SMContent,ServiceID,SendTargetDesc,Priority,RCompleteTimeBegin,RCompleteTimeEnd,RCompleteHourBegin,RCompleteHourEnd,RoadBy,pad2,RequestTime,FeeValue,pad3,pad4,pad5) ");
			sqlBuffer.append("values ('" + send_target.getMobilephone() + "','"
					+ StringTools.changeSqToDq(send_target.getSMContent())
					+ "','" + m_send_task.getSend_type() + "','" + desc + "' ,"
					+ m_send_task.getPriority() + ",'"
					+ m_send_task.getRsend_time_begin() + "' ,'"
					+ m_send_task.getRsend_time_end() + "','"
					+ m_send_task.getRcomplete_hour_begin() + "','"
					+ m_send_task.getRcomplete_hour_end() + "',"
					+ m_send_task.getRoad_used() + ",'"
					+ m_send_task.getSend_id() + "','" + now_datetime + "',0,'"
					+ m_send_task.getService_no() + "','','')");
		}
		Statement stmt = null;
		try {
			stmt = save_smcs2_conn.createStatement();
			stmt.executeUpdate(sqlBuffer.toString());
		} catch (Exception e) {
			Log.LogInfo(Sender.class + "saveSMSToDB����ִ��ʧ�ܡ�");
		} finally {
			m_dataTools.closeResources(stmt, save_smcs2_conn);
		}
	}

	/**
	 * 
	 * ����˵��: �����ظ�������Ϣ
	 * 
	 * @param send_target
	 * @return
	 */
	private boolean checkRepeated(SendTarget send_target) {
		boolean flag = false;
		Connection conn = null;
		Statement stmt = null;
		if (send_target != null) {
			String filter_table_name = m_send_task.getTab_name();
			String mobilephone = send_target.getMobilephone();
			String pihao = "" + m_send_task.getSend_id();
			String smsContent = send_target.getSMContent();
			// ���������sendTypeΪ1���Ǻ��������sendTypeΪ�գ����˱�����Ϊ0��
			String sendType = "14".equals(m_send_task.getSend_target_type())?"1":"0";
			if (mobilephone != null && !"".equals(mobilephone) && pihao != null
					&& !"".equals(pihao) && smsContent != null
					&& !"".equals(smsContent) && !CommonTools.isEmpty(filter_table_name)) {
				StringBuffer sql = new StringBuffer();
				sql.append("insert into ").append(filter_table_name).append(
						"(MobilePhone,Pihao,SMContent,SendType) values ('")
						.append(mobilephone).append("','").append(pihao)
						.append("','").append(smsContent).append("','").append(
								sendType).append("')");

				try {
					conn = SplitterConnnectionPool.getSMCSConn();
					stmt = conn.createStatement();
					stmt.executeUpdate(sql.toString());
					flag = true;
				} catch (SQLException e) {
					Log.LogInfo(Sender.class + "checkRepeated����ִ�гɹ�.");
				} finally {
					m_dataTools.closeResources(stmt, conn);
				}
			}
		}
		return flag;
	}

	/**
	 * 
	 * ����˵��: ����������
	 * 
	 * @param conn
	 *            ��������
	 * @param list
	 *            ���������б�
	 * @param provinceID
	 *            ʡ��˾ID
	 * @return ���˺�Ĵ��������б�
	 */
	public boolean isInWhiteList(SendTarget send_target) {
		String mobilephone = "";
		String provinceID = "";
		boolean flag = false;
		if (send_target != null) {
			mobilephone = send_target.getMobilephone();
			provinceID = send_target.getProvinceID();
		} else {
			return flag;
		}
		// ��ȡ����
		Element whitelist_element = m_eh_tools.getElement("whiteList",
				"whiteListMap");
		Map<String, String> white_list_map = (Map) whitelist_element.getValue();
		//map����ֵ
		String key = provinceID + "|" + mobilephone;
		if (white_list_map.containsKey(key)) {
			flag = true;
		} else {
			Connection whiteList_conn = SplitterConnnectionPool.getSMSConn();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql = null;
			if ("0".equals(provinceID)) {
				sql = " select top 1 1 from AVS_White_List where Phone='"
						+ mobilephone + "' and ProvinceID='0'";
			} else {
				sql = " select top 1 1 from AVS_White_List where Phone='"
						+ mobilephone + "' and ProvinceID='" + provinceID + "'";
			}
			try {
				pstmt = whiteList_conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					flag = true;
				}
			} catch (Exception e) {
				Log.LogInfo(Sender.class + "isInWhiteList��������");
			} finally {
				m_dataTools.closeResources(rs, pstmt, whiteList_conn);
			}
		}
		return flag;
	}

	/**
	 * ����˵��: ����������
	 */
	public boolean isInBlackList(SendTarget send_target) {
		boolean flag = false;
		if (send_target != null) {
			String mobilephone = send_target.getMobilephone();
			String provinceID = send_target.getProvinceID();

			Connection blackList_conn = SplitterConnnectionPool.getSMSConn();
			PreparedStatement psmt = null;
			ResultSet rs = null;
			String sql = null;
			if ("0".equals(provinceID)) {
				sql = " select top 1 1 from AVS_Black_List where Phone='"
						+ mobilephone + "' and ProvinceID='0'";
			} else {
				sql = " select top 1 1 from AVS_Black_List where Phone='"
						+ mobilephone + "' and ProvinceID='" + provinceID + "'";
			}
			try {
				psmt = blackList_conn.prepareStatement(sql);
				rs = psmt.executeQuery();
				if (rs.next()) {
					flag = true;
				}
			} catch (Exception e) {
				Log.LogInfo(Sender.class + ".isInBlackList��������");
			} finally {
				m_dataTools.closeResources(rs, psmt, blackList_conn);
			}
		}
		return flag;
	}

	/**
	 * ����˵��: �޸� ReleaseTime
	 * 
	 * @param send_target
	 */
	public void finish() {
		// ����Ŀ������
		String send_target_type = m_send_task.getSend_target_type();
		String filter_table_name = m_send_task.getTab_name();
		String pihao = "" + m_send_task.getSend_id();
		if (pihao == null || "".equals(pihao)) {
			return;
		}
		String now_datatime = DateTools.getDate("yyyy-MM-dd HH:mm:ss:SSS");
		Connection finish_conn = SplitterConnnectionPool.getSMCSConn();
		int sendID = m_send_task.getSend_id();
		String sql = "update AVS_Send_SM set ReleaseTime = '" + now_datatime
				+ "' where sendID= " + sendID + "";
		String tempSql = "TRUNCATE TABLE " + m_send_task.getTab_name();
		String txlSql = "DELETE from AVS_Txl_MobileList WHERE Pihao=" + pihao
				+ "";

		Statement stmt = null;
		try {
			finish_conn.setAutoCommit(false);
			stmt = finish_conn.createStatement();
			stmt.addBatch(sql);
			stmt.addBatch(tempSql);
			if ("8".equals(send_target_type) || "9".equals(send_target_type)
					|| "10".equals(send_target_type)
					|| "11".equals(send_target_type)
					|| "12".equals(send_target_type)) {
				stmt.addBatch(txlSql);
			}
			stmt.executeBatch();
			finish_conn.commit();
		} catch (Exception e) {
			Log.LogInfo(Sender.class + "finish��������");
			try {
				finish_conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				finish_conn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			m_dataTools.closeResources(stmt, finish_conn);
		}
	}

	/**
	 * 
	 * ����˵��: ������������
	 */
	public void finishMelonTask() {
		String now_datatime = DateTools.getDate("yyyy-MM-dd HH:mm:ss:SSS");
		String pihao = "" + m_send_task.getSend_id();
		if (pihao == null || "".equals(pihao)) {
			return;
		}
		String tempSql = "TRUNCATE TABLE " + m_send_task.getTab_name();
		Connection melon_conn = SplitterConnnectionPool.getSMCSConn();
		String sql = "update AVS_Melon_SM set SendTime = '" + now_datatime
				+ "' where ID = " + m_send_task.getSend_id() + "";
		Statement stmt = null;
		try {
			melon_conn.setAutoCommit(false);
			stmt = melon_conn.createStatement();
			stmt.addBatch(sql);
			stmt.addBatch(tempSql);
			stmt.executeBatch();
			melon_conn.commit();
		} catch (Exception e) {
			try {
				melon_conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			Log.LogInfo(Sender.class + "finishMelonTask��������");
		} finally {
			try {
				melon_conn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			m_dataTools.closeResources(stmt, melon_conn);
		}
	}

	/**
	 * ����˵��: �Ǻ���������������
	 * 
	 * @param sendTarget
	 *            ����Ŀ��
	 * @return ������������
	 */
	public String createSendTargetDesc(SendTarget sendTarget) {
		// ����Ŀ������
		StringBuffer sendTargetDesc = new StringBuffer();
		// ��ǰʱ��
		String currentTime = DateUtil
				.getCurrentDateString(DateUtil.DATE_FORMAT);
		sendTargetDesc.append("SMCS|").append(currentTime + "|");
		if (!CommonTools.isEmpty(m_send_task.getRequest_branch_id())) {
			sendTargetDesc.append(m_send_task.getRequest_branch_id()
					.replaceAll("_", "$")
					+ "|");
		} else {
			sendTargetDesc.append(m_send_task.getProvince_id() + "|");
		}
		if (!CommonTools.isEmpty(sendTarget.getSendTargetName())) {
			sendTargetDesc.append(sendTarget.getSendTargetName());
		}
		sendTargetDesc.append("|||").append(
				m_send_task.getSend_description() + "|").append(
				m_send_task.getRequest_user_id());
		return sendTargetDesc.toString();
	}

	/**
	 * 
	 * ����˵��: ��������Ŀ������
	 * 
	 * @param sendTarget
	 * @return
	 */
	public String createMelonSendTargetDesc(SendTarget sendTarget) {
		// ����Ŀ������
		StringBuffer sendTargetDesc = new StringBuffer();
		// ��ǰʱ��
		String currentTime = DateUtil
				.getCurrentDateString(DateUtil.DATE_FORMAT);
		sendTargetDesc.append("SMCS|").append(currentTime + "|");
		if (!CommonTools.isEmpty(m_send_task.getRequest_branch_id())) {
			sendTargetDesc.append(m_send_task.getProvince_id() + "|");
		} else {
			sendTargetDesc.append(m_send_task.getProvince_id() + "$");
			sendTargetDesc.append(m_send_task.getRequest_branch_id() + "|");
		}
		sendTargetDesc.append(sendTarget.getName() + "|||").append(
				m_send_task.getSend_description() + "|").append(
				m_send_task.getRequest_user_id());
		return sendTargetDesc.toString();
	}
}