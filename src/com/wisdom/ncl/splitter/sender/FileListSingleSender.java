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
 * 单列文件发送
 * 
 * @author xiaozhen
 * 
 */
@SuppressWarnings("unchecked")
public class FileListSingleSender extends Sender implements Callable {

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
		// 获取发送目标
		String target = m_send_sm.getSend_target();
		if (target != null) {
			PreparedStatement psmt = null;
			ResultSet rs = null;
			// 根据单列文件信息形成获取单列文件信息列表的SQL语句
			String sql = "select MobilePhone from avs_txl_mobilelist where pihao='"
					+ target + "'";
			try {
				psmt = m_smcs_conn.prepareStatement(sql);
				// 执行SQL，获取单列文件信息
				rs = psmt.executeQuery();
				// 获取发送比例
				int sendTargetRatio = m_send_sm.getSend_target_ratio();
				// 全部发送
				if (sendTargetRatio >= 100) {
					while (rs.next()) {
						subHandleSendTarget(rs);
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
				Log.LogInfo(FileListSingleSender.class + ".call方法错误！");
			} finally {
				m_data_tools.closeResources(rs, psmt, m_smcs_conn);
			}

			// 修改ReleaseTime
			finish();
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
		} catch (SQLException e) {
			Log.LogInfo(AgentGroupSender.class + ".subHandleSendTarget方法错误！");
		}
		sendTarget.setSMContent(m_send_sm.getSmcontent());
		sendTarget.setProvinceID(m_send_sm.getProvince_id());
		sendTarget.setTableName(m_send_sm.getTab_name());
		handleSendTarget(sendTarget);
	}
}
