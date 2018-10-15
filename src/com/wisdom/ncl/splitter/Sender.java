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

	// 连接以成员变量 的形式存在
	AVS_Send_SM m_send_task;
	// 日期处理类
	private DataTools m_dataTools = new DataTools();
	// 缓存工具类
	private EhcacheTools m_eh_tools = new EhcacheTools();

	// 每个线程默认执行的方法
	public abstract Object call() throws Exception;

	/**
	 * 
	 * 功能说明: 载入任务信息
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

	// 处理发送目标
	public final void handleSendTarget(SendTarget send_target) {

		// 是否进行白名单过滤： ON代表使用 OFF代表关闭
		String isDueByWhiteList = new IniTools().get("handleSenderTarget",
				"isOpenWhiteList").get(0);

		// 是否进行白名单过滤： ON代表使用 OFF代表关闭
		String isDueByBlackList = new IniTools().get("handleSenderTarget",
				"isOpenBlackList").get(0);

		// 重复号码标识
		boolean repeatFlag = checkRepeated(send_target);
		// 不重复
		if (repeatFlag) {
			// 白名单开关
			if ("ON".equalsIgnoreCase(isDueByWhiteList)
					&& "ON".equalsIgnoreCase(isDueByBlackList)) {// 白名单 、黑名单同时处理
				// 不在黑名单
				boolean blackFlag = isInBlackList(send_target);
				if (!blackFlag) {
					// 在白名单
					boolean whiteFlag = isInWhiteList(send_target);
					if (whiteFlag) {
						saveSMSToDB(send_target);
					}
				}
			} else if ("ON".equalsIgnoreCase(isDueByWhiteList)
					&& !"ON".equalsIgnoreCase(isDueByBlackList)) {// 白名单处理，黑名单不处理
				// 在白名单
				boolean whiteFlag = isInWhiteList(send_target);
				if (whiteFlag) {
					saveSMSToDB(send_target);
				}
			} else if (!"ON".equalsIgnoreCase(isDueByWhiteList)
					&& "ON".equalsIgnoreCase(isDueByBlackList)) {// 白名单不处理、黑名单处理

				// 不在黑名单
				boolean blackFlag = isInBlackList(send_target);
				if (!blackFlag) {
					saveSMSToDB(send_target);
				}
			} else {// 黑、白名单都不处理
				saveSMSToDB(send_target);
			}
		}
	}

	/**
	 * 
	 * 功能说明: 保存短信信息入库
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
			// 2012年1月19日 By 侯松芳添加
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
			Log.LogInfo(Sender.class + "saveSMSToDB方法执行失败。");
		} finally {
			m_dataTools.closeResources(stmt, save_smcs2_conn);
		}
	}

	/**
	 * 
	 * 功能说明: 过滤重复短信信息
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
			// 红利任务的sendType为1，非红利任务的sendType为空（过滤表设置为0）
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
					Log.LogInfo(Sender.class + "checkRepeated方法执行成功.");
				} finally {
					m_dataTools.closeResources(stmt, conn);
				}
			}
		}
		return flag;
	}

	/**
	 * 
	 * 功能说明: 白名单过滤
	 * 
	 * @param conn
	 *            数据连接
	 * @param list
	 *            待发短信列表
	 * @param provinceID
	 *            省公司ID
	 * @return 过滤后的待发短信列表
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
		// 获取缓存
		Element whitelist_element = m_eh_tools.getElement("whiteList",
				"whiteListMap");
		Map<String, String> white_list_map = (Map) whitelist_element.getValue();
		//map键的值
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
				Log.LogInfo(Sender.class + "isInWhiteList方法错误。");
			} finally {
				m_dataTools.closeResources(rs, pstmt, whiteList_conn);
			}
		}
		return flag;
	}

	/**
	 * 功能说明: 黑名单过滤
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
				Log.LogInfo(Sender.class + ".isInBlackList方法错误。");
			} finally {
				m_dataTools.closeResources(rs, psmt, blackList_conn);
			}
		}
		return flag;
	}

	/**
	 * 功能说明: 修改 ReleaseTime
	 * 
	 * @param send_target
	 */
	public void finish() {
		// 发送目标类型
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
			Log.LogInfo(Sender.class + "finish方法错误");
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
	 * 功能说明: 结束红利任务
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
			Log.LogInfo(Sender.class + "finishMelonTask方法错误");
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
	 * 功能说明: 非红利待发短信描述
	 * 
	 * @param sendTarget
	 *            发送目标
	 * @return 待发短信描述
	 */
	public String createSendTargetDesc(SendTarget sendTarget) {
		// 发送目标描述
		StringBuffer sendTargetDesc = new StringBuffer();
		// 当前时间
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
	 * 功能说明: 创建红利目标描述
	 * 
	 * @param sendTarget
	 * @return
	 */
	public String createMelonSendTargetDesc(SendTarget sendTarget) {
		// 发送目标描述
		StringBuffer sendTargetDesc = new StringBuffer();
		// 当前时间
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