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
 * 个人群组发送
 * @author nana
 *
 */
@SuppressWarnings("unchecked")
public class PersonalGroupSender extends Sender implements Callable {

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
		if(m_send_sm!=null){
			// 获取发送目标：以分号分隔的群组信息
			String target = m_send_sm.getSend_target();
			if(target!=null){
				// 根据群组信息形成获取个人群组信息的SQL语句  
				String sql = getPersonalGroupSQL(target);
				PreparedStatement psmt = null;
				ResultSet rs = null;
				try{
					psmt = m_smcs_conn.prepareStatement(sql);
					// 执行SQL，获取个人信息
					rs = psmt.executeQuery();
					// 获取发送比例
					int sendTargetRatio = m_send_sm.getSend_target_ratio();
					// 全部发送
					if(sendTargetRatio >= 100){
						while(rs.next()){	
							subHandleSendTarget(m_smcs_conn, psmt, rs);
						}
					}else{ // 部分发送
						int compressionRatio = (int)Math.ceil((double)sendTargetRatio/10);//压缩发送比例
						long count = 0;//循环次数
						long nextTarget = 0;//下一个发送目标
						int stepSize = 10;//步长为10
						int innerLoopIndex = 0;//内部小循环
						while(rs.next()){	
							if(nextTarget<=count && count<(nextTarget + compressionRatio)){
								subHandleSendTarget(m_smcs_conn, psmt, rs);
							}
							count ++;
							if (innerLoopIndex+1 == stepSize){
								innerLoopIndex = 0;
								nextTarget += stepSize;
							}else{
								innerLoopIndex ++;
							}
						}
					}
				} catch (Exception e) {
					Log.LogInfo(PersonalGroupSender.class + ".call方法错误！");
				} finally{
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
	 * @param rs 结果集
	 */
	private void subHandleSendTarget(Connection conn, PreparedStatement psmt, ResultSet rs){
		String getPersonalByID;
		try {
			//根据个人编号获取个人信息
			getPersonalByID = "select MobilePhone,ClientName from TXL_Personal where ID=" + rs.getString("ID");
			psmt = conn.prepareStatement(getPersonalByID);
			ResultSet personalResultSet = psmt.executeQuery();
			if(personalResultSet.next()){
				SendTarget sendTarget = new SendTarget();
				sendTarget.setMobilephone(personalResultSet.getString("MobilePhone"));
				sendTarget.setSendTargetName(personalResultSet.getString("ClientName"));
				sendTarget.setSMContent(m_send_sm.getSmcontent());
				sendTarget.setProvinceID(m_send_sm.getProvince_id());
				sendTarget.setTableName(m_send_sm.getTab_name());
				handleSendTarget(sendTarget);						
			}
			if(personalResultSet != null){
				personalResultSet.close();
			}
		} catch (SQLException e) {
			Log.LogInfo(PersonalGroupSender.class + ".subHandleSendTarget方法错误！");
		}
	}
	
	/**
	 * 
	 * 功能说明:  根据群组信息形成获取个人群组信息的SQL语句  
	 * @param group
	 * 		以分号分隔的群组信息
	 * @return
	 * 		获取个人群组信息的SQL语句
	 */
	public String getPersonalGroupSQL(String group){
		StringBuffer sql = new StringBuffer();
		sql.append("select ID from TXL_Personal_Group_Schema where ");
		String[] groups = group.split(";");
		for(int i=0; i<groups.length; i++){
			String[] ids = groups[i].split("->");
			sql.append("ProvinceID='" + ids[0] + "'" +
				" and BranchID = '" + ids[1] + "' and GroupName = '" + ids[2] + "'"); 
			if(i != groups.length-1){
				sql.append(" or ");
			}
		}
		
		return sql.toString();
	}
}
