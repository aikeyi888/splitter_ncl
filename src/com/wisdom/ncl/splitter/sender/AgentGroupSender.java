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
import com.wisdom.ncl.splitter.tools.CommonTools;
import com.wisdom.ncl.splitter.tools.DataTools;
import com.wisdom.ncl.splitter.tools.Log;

/**
 * 代理人群组发送
 * 
 * @author nana
 * 
 */
@SuppressWarnings("unchecked")
public class AgentGroupSender extends Sender implements Callable {

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
		Connection m_stand_conn = SplitterConnnectionPool.getSTANDConn();
		if (m_send_sm != null) {
			// 获取发送目标：以分号分隔的群组信息
			String target = m_send_sm.getSend_target();
			if (target != null) {
				// 根据群组信息形成获取代理人信息列表的SQL语句
				String sql = getAgentInfoSQL(m_data_tools, m_smcs_conn, target);
				PreparedStatement psmt = null;
				ResultSet rs = null;
				try {
					psmt = m_stand_conn.prepareStatement(sql);
					// 执行SQL，获取代理人信息
					rs = psmt.executeQuery();
					// 获取发送比例
					int sendTargetRatio = m_send_sm.getSend_target_ratio();
					// 全部发送
					if (sendTargetRatio >= 100) {
						while (rs.next()) {
							subHandleSendTarget(rs);
						}
					} else {// 部分发送
						int compressionRatio = (int) Math
								.ceil((double) sendTargetRatio / 10);// 压缩发送比例
						long count = 0;// 循环次数
						long nextTarget = 0;// 下一个发送目标
						int stepSize = 10;// 步长为10
						int innerLoopIndex = 0;// 内部小循环
						while (rs.next()) {
							if (nextTarget <= count
									&& count < (nextTarget + compressionRatio)) {
								subHandleSendTarget(rs);
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
					Log.LogInfo(AgentGroupSender.class + ".call方法错误！");
				} finally {
					m_data_tools.closeResources(rs, psmt, m_stand_conn);
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
	private void subHandleSendTarget(ResultSet rs) {
		SendTarget sendTarget = new SendTarget();
		try {
			sendTarget.setMobilephone(rs.getString("MobilePhone"));
			sendTarget.setSendTargetName(rs.getString("Name"));
		} catch (SQLException e) {
			Log.LogInfo(AgentGroupSender.class + ".subHandleSendTarget方法错误！");
		}
		sendTarget.setSMContent(m_send_sm.getSmcontent());
		sendTarget.setProvinceID(m_send_sm.getProvince_id());
		sendTarget.setTableName(m_send_sm.getTab_name());
		handleSendTarget(sendTarget);
	}

	/**
	 * 
	 * 功能说明: 根据省份编号和支公司编号获取代理人群组的SQL语句
	 * 
	 * @param group
	 *            以";"分隔的群组信息
	 * 
	 * @return 获取代理人群组信息的SQL语句
	 */
	private String getAgentGroupSQL(String group) {
		StringBuffer sql = new StringBuffer();
		sql
				.append(
						"select "
								+ "ProvinceID,BranchID,Name,Description,Sex,AgeBegin,AgeEnd,"
								+ "InTimeBegin,InTimeEnd,Education,AgentLevel,AgentDeptID,AgentCount")
				.append(" from AVS_Agent_Group").append(" where ");
		String[] groups = group.split(";");
		for (int i = 0; i < groups.length; i++) {
			String[] ids = groups[i].split("->");
			sql.append("ProvinceID='" + ids[0] + "'" + " and BranchID = '"
					+ ids[1] + "' and Name = '" + ids[2] + "'");
			if (i != groups.length - 1) {
				sql.append(" or ");
			}
		}
		return sql.toString();
	}

	/**
	 * 获取代理人相关信息的SQL语句
	 * 
	 * @param conn
	 *            数据库连接
	 * @param group
	 *            以";"分隔的群组信息
	 * 
	 * @return 获取代理人相关信息的SQL语句
	 */
	private String getAgentInfoSQL(DataTools dataTools, Connection conn,
			String group) {
		String sql = getAgentGroupSQL(group);
		StringBuffer getAgentInfo = new StringBuffer();
		getAgentInfo
				.append("select a.ProvinceID,a.BranchID,Name,Sex,MobilePhone")
				.append(" from AgentInfo a,InsurInfo b")
				.append(
						" where a.AgentID = b.AgentID and (DutyStatus='01' or DutyStatus='02') and ")
				.append("MobilePhone is not null and MobilePhone <> ''");
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery();
			if (rs.next()) {
				String provinceID = rs.getString("ProvinceID");
				String branchID = rs.getString("BranchID");
				String sex = rs.getString("Sex");
				String ageBegin = rs.getString("AgeBegin");
				String ageEnd = rs.getString("AgeEnd");
				String inTimeBegin = rs.getString("InTimeBegin");
				String inTimeEnd = rs.getString("InTimeEnd");
				String education = rs.getString("Education");
				String agentDeptID = rs.getString("AgentDeptID");
				// String clientType = rs.getString("ClientType");
				if (!"0".equals(provinceID)) {
					getAgentInfo.append(" and a.ProvinceID ='" + provinceID
							+ "'");
				}
				if (!CommonTools.isEmpty(branchID)) {
					getAgentInfo.append(" and a.BranchID ='"
							+ branchID.substring(0, 2) + "'");
				}
				if (!CommonTools.isEmpty(sex)) {
					getAgentInfo.append(" and Sex ='" + sex + "'");
				}
				if (!CommonTools.isEmpty(ageBegin)) {
					getAgentInfo.append(" and Birthday >='" + ageBegin + "'");
				}
				if (!CommonTools.isEmpty(ageEnd)) {
					getAgentInfo.append(" and Birthday <='" + ageEnd + "'");
				}
				if (!CommonTools.isEmpty(inTimeBegin)) {
					getAgentInfo.append(" and InTime >='" + inTimeBegin + "'");
				}
				if (!CommonTools.isEmpty(inTimeEnd)) {
					getAgentInfo.append(" and InTime <='" + inTimeEnd + "'");
				}
				if (!CommonTools.isEmpty(education)) {
					education = education.trim().replaceAll(",", "','");
					education = "'" + education + "'";
					getAgentInfo.append(" and EducationID in (" + education
							+ ")");
				}
				if (!CommonTools.isEmpty(agentDeptID)) {
					agentDeptID = agentDeptID.trim().replaceAll(",", "','");
					agentDeptID = "'" + agentDeptID + "'";
					if (!"0".equals(provinceID)) {
						getAgentInfo.append(" and a.BranchID in ("
								+ agentDeptID + ")");
					} else {
						getAgentInfo.append(" and a.ProvinceID in ("
								+ agentDeptID + ")");
					}
				}
				// if (!CommonTools.isEmpty(clientType)) {
				// clientType = clientType.trim().replaceAll(",", "','");
				// clientType = "'" + clientType + "'";
				// getAgentInfo.append(" and b.CustomerType in (" + clientType
				// + ")");
				// }
			}
		} catch (SQLException e) {
			Log.LogInfo(AgentGroupSender.class + ".getAgentInfoSQL方法错误！");
		} finally {
			dataTools.closeResources(rs, psmt, conn);
		}
		return getAgentInfo.toString();
	}
}
