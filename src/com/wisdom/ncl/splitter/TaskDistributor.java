package com.wisdom.ncl.splitter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.wisdom.ncl.splitter.data.AVS_Send_SM;
import com.wisdom.ncl.splitter.db.splitter.SplitterConnnectionPool;
import com.wisdom.ncl.splitter.tools.CommonTools;
import com.wisdom.ncl.splitter.tools.DataTools;
import com.wisdom.ncl.splitter.tools.IniTools;
import com.wisdom.ncl.splitter.tools.Log;

/**
 * 
 * 功能说明: 任务分发器 作者: 于浩 创建时间: 2012-1-12 上午08:59:41 最后修改时间: 2012-1-12 上午08:59:41
 * 最后修改人: 于浩 版本 1.0
 */
public class TaskDistributor {

	// 任务处理器工厂
	@SuppressWarnings("unchecked")
	private Hashtable<String, Class> m_sender_class_factory = new Hashtable<String, Class>();
	// 任务集合
	private List<AVS_Send_SM> m_task_list = new ArrayList<AVS_Send_SM>();
	// 数据处理类
	private DataTools m_dataTools = new DataTools();
	// 线程的集合
	@SuppressWarnings("unchecked")
	private List<Future> m_future_List = new ArrayList<Future>();
	//表名集合
	private List<String> m_table_name_list = new ArrayList<String>();
	
	// 判断是否注册成功
	@SuppressWarnings("unchecked")
	public boolean registerSendType(String type, Class sender_class) {
		try {
			Object o = sender_class.newInstance();
			if (!(o instanceof Sender)) {
				return false;
			}

			m_sender_class_factory.put(type, sender_class);
			return true;
		} catch (Exception e) {
			Log.LogInfo(TaskDistributor.class + "注册任务处理器" + sender_class
					+ "失败。");
			return false;
		}
	}
	/**
	 * 功能说明:  获取未完成任务
	 */
	public void getUnFinishedTask()
	{
		if(m_table_name_list != null && m_table_name_list.size()>0){
			// 查找并处理未完成的任务
			for (int i = 0; i < m_table_name_list.size(); i++) {
				String str_tab_name = m_table_name_list.get(i);
				String str_tabcount_sql = "SELECT TOP 1 PIHAO,SENDTYPE FROM "+str_tab_name;
				Connection smcs_conn = null;
				Statement stmt = null;
				ResultSet rs = null;
				try {
					smcs_conn = SplitterConnnectionPool.getSMCSConn();
					stmt = smcs_conn.createStatement();
					rs = stmt.executeQuery(str_tabcount_sql);
					while (rs.next()) {
						String str_sendid = rs.getString("PIHAO");
						String str_sendtype = rs.getString("SENDTYPE");
						if("1".equals(str_sendtype)){
							getMelonTask(str_sendid,str_tab_name);
						}else{
							getTasks(str_sendid,str_tab_name);
						}
					}
				} catch (Exception e) {
					Log.LogInfo(TaskDistributor.class + "getUnFinishedTask方法错误!");
				} finally {
					m_dataTools.closeResources(rs, stmt, smcs_conn);
				}
			}
		}
	}
	/**
	 * 
	 * 功能说明: 查询待发送的短信任务信息
	 * 
	 * @return 待发送短信任务集合
	 */
	public void getTasks() {
		Connection tasks_conn = SplitterConnnectionPool.getSMCSConn();
		StringBuffer sqlBuffer = new StringBuffer();
		int now_time = CommonTools.getNowMinute();
		// 这里读取配置文件 获取每次读取的最大任务数
		int taskCount = Integer.parseInt(new IniTools().get("TaskCount",
				"taskCount").get(0));
		sqlBuffer.append(" select top " + taskCount + " * from AVS_Send_SM ");
		sqlBuffer.append(" where ( ReleaseTime is null and checked='1' ");
		sqlBuffer.append(" and RCompleteHourEnd >= '" + now_time + "'");
		sqlBuffer.append(" and RCompleteHourBegin <='" + now_time + "' ");
		sqlBuffer.append(" and SendTargetType <> 11) or (ReleaseTime is null ");
		sqlBuffer.append(" and checked='1' " + " and GroupName='Done' ");
		sqlBuffer.append(" and RCompleteHourEnd >= '" + now_time + "'");
		sqlBuffer.append(" and RCompleteHourBegin <='" + now_time + "' ");
		sqlBuffer.append(" and SendTargetType = 11) order by Priority");
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = tasks_conn.prepareStatement(sqlBuffer.toString());
			rs = stmt.executeQuery();
			AVS_Send_SM avsSendSM = null;
			while (rs.next()) {
				avsSendSM = new AVS_Send_SM();
				avsSendSM.setProvince_id(rs.getString("ProvinceID"));
				avsSendSM.setSend_id(rs.getInt("SendID"));
				avsSendSM.setSend_name(rs.getString("SendName"));
				avsSendSM.setSend_type(rs.getString("SendType"));
				avsSendSM.setSend_description(rs.getString("SendDescription"));
				avsSendSM.setSmcontent(rs.getString("SMContent"));
				avsSendSM.setRelease_time(rs.getString("ReleaseTime"));
				avsSendSM.setSend_target_type(rs.getString("SendTargetType"));
				avsSendSM.setSend_target(rs.getString("SendTarget"));
				avsSendSM.setSend_target_ratio(rs.getInt("SendTargetRatio"));
				avsSendSM.setSend_target_number(rs.getInt("SendTargetNumber"));
				avsSendSM.setGroup_name(rs.getString("GroupName"));
				avsSendSM.setSend_group_ratio(rs.getInt("SendGroupRatio"));
				avsSendSM.setSend_group_number(rs.getInt("SendGroupNumber"));
				avsSendSM.setPriority(rs.getInt("Priority"));
				avsSendSM.setSend_immediate(rs.getString("SendImmediate"));
				avsSendSM.setRequest_branch_id(rs.getString("RequestBranchID"));
				avsSendSM.setRequest_user_id(rs.getString("RequestUserID"));
				avsSendSM.setRsend_time_end(rs.getString("RSendTimeEnd"));
				avsSendSM.setRsend_time_begin(rs.getString("RSendTimeBegin"));
				avsSendSM.setRcomplete_hour_begin(rs
						.getInt("RCompleteHourBegin"));
				avsSendSM.setRcomplete_hour_end(rs.getInt("RCompleteHourEnd"));
				avsSendSM.setApplied(rs.getString("Applied"));
				avsSendSM.setApply_time(rs.getString("ApplyTime"));
				avsSendSM.setChecked(rs.getString("Checked"));
				avsSendSM.setCheck_time(rs.getString("CheckTime"));
				avsSendSM.setCheck_note(rs.getString("CheckNote"));
				avsSendSM.setPrompt_to_applier(rs.getString("PromptToApplier"));
				avsSendSM.setPrompt_to_applier_time(rs
						.getString("PromptToApplierTime"));
				avsSendSM.setFinish_time(rs.getString("FinishTime"));
				avsSendSM.setSend_target_count(rs.getInt("SendTargetCount"));
				avsSendSM.setRoad_used(rs.getInt("RoadUsed"));
				avsSendSM.setAbort_flag(rs.getInt("AbortFlag"));
				avsSendSM.setPrompt_by_sm(rs.getInt("PromptBySM"));
				avsSendSM.setService_no(rs.getString("ServiceNo"));

				m_task_list.add(avsSendSM);
			}
		} catch (Exception e) {
			Log.LogInfo(TaskDistributor.class + "getTasks方法错误!");
			e.printStackTrace();
		} finally {
			m_dataTools.closeResources(rs, stmt, tasks_conn);
		}
	}
	/**
	 * 
	 * 功能说明: 根据sendid查询待发送的短信任务信息 
	 * @param str_sendid
	 * @param str_tab_name
	 */
	public void getTasks(String str_sendid,String str_tab_name) {
		Connection tasks_conn = SplitterConnnectionPool.getSMCSConn();
		StringBuffer sqlBuffer = new StringBuffer();
		int now_time = CommonTools.getNowMinute();
		// 这里读取配置文件 获取每次读取的最大任务数
		sqlBuffer.append(" select * from AVS_Send_SM ");
		sqlBuffer.append(" where ( ReleaseTime is null and checked='1' ");
		sqlBuffer.append(" and RCompleteHourEnd >= '" + now_time + "'");
		sqlBuffer.append(" and RCompleteHourBegin <='" + now_time + "' ");
		sqlBuffer.append(" and SendTargetType <> 11) or (ReleaseTime is null ");
		sqlBuffer.append(" and checked='1' " + " and GroupName='Done' ");
		sqlBuffer.append(" and RCompleteHourEnd >= '" + now_time + "'");
		sqlBuffer.append(" and RCompleteHourBegin <='" + now_time + "' ");
		sqlBuffer.append(" and SendTargetType = 11");
		sqlBuffer.append(" and sendid = '"+str_sendid+"') order by Priority");
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = tasks_conn.prepareStatement(sqlBuffer.toString());
			rs = stmt.executeQuery();
			AVS_Send_SM avsSendSM = null;
			while (rs.next()) {
				avsSendSM = new AVS_Send_SM();
				avsSendSM.setProvince_id(rs.getString("ProvinceID"));
				avsSendSM.setSend_id(rs.getInt("SendID"));
				avsSendSM.setSend_name(rs.getString("SendName"));
				avsSendSM.setSend_type(rs.getString("SendType"));
				avsSendSM.setSend_description(rs.getString("SendDescription"));
				avsSendSM.setSmcontent(rs.getString("SMContent"));
				avsSendSM.setRelease_time(rs.getString("ReleaseTime"));
				avsSendSM.setSend_target_type(rs.getString("SendTargetType"));
				avsSendSM.setSend_target(rs.getString("SendTarget"));
				avsSendSM.setSend_target_ratio(rs.getInt("SendTargetRatio"));
				avsSendSM.setSend_target_number(rs.getInt("SendTargetNumber"));
				avsSendSM.setGroup_name(rs.getString("GroupName"));
				avsSendSM.setSend_group_ratio(rs.getInt("SendGroupRatio"));
				avsSendSM.setSend_group_number(rs.getInt("SendGroupNumber"));
				avsSendSM.setPriority(rs.getInt("Priority"));
				avsSendSM.setSend_immediate(rs.getString("SendImmediate"));
				avsSendSM.setRequest_branch_id(rs.getString("RequestBranchID"));
				avsSendSM.setRequest_user_id(rs.getString("RequestUserID"));
				avsSendSM.setRsend_time_end(rs.getString("RSendTimeEnd"));
				avsSendSM.setRsend_time_begin(rs.getString("RSendTimeBegin"));
				avsSendSM.setRcomplete_hour_begin(rs
						.getInt("RCompleteHourBegin"));
				avsSendSM.setRcomplete_hour_end(rs.getInt("RCompleteHourEnd"));
				avsSendSM.setApplied(rs.getString("Applied"));
				avsSendSM.setApply_time(rs.getString("ApplyTime"));
				avsSendSM.setChecked(rs.getString("Checked"));
				avsSendSM.setCheck_time(rs.getString("CheckTime"));
				avsSendSM.setCheck_note(rs.getString("CheckNote"));
				avsSendSM.setPrompt_to_applier(rs.getString("PromptToApplier"));
				avsSendSM.setPrompt_to_applier_time(rs
						.getString("PromptToApplierTime"));
				avsSendSM.setFinish_time(rs.getString("FinishTime"));
				avsSendSM.setSend_target_count(rs.getInt("SendTargetCount"));
				avsSendSM.setRoad_used(rs.getInt("RoadUsed"));
				avsSendSM.setAbort_flag(rs.getInt("AbortFlag"));
				avsSendSM.setPrompt_by_sm(rs.getInt("PromptBySM"));
				avsSendSM.setService_no(rs.getString("ServiceNo"));
				avsSendSM.setTab_name(str_tab_name);
				m_task_list.add(avsSendSM);
			}
		} catch (Exception e) {
			Log.LogInfo(TaskDistributor.class + "getTasks(String,String)方法错误!");
			e.printStackTrace();
		} finally {
			m_dataTools.closeResources(rs, stmt, tasks_conn);
		}
	}
	/**
	 * 
	 * 功能说明: 获取红利信息任务
	 * 
	 * @return
	 */
	public void getMelonTask() {
		int taskCount = Integer.parseInt(new IniTools().get("TaskCount",
				"taskCount").get(0));
		String sql = "select top " + taskCount
				+ " ID,ProvinceID,SendTargetObject,StartMelonDate,"
				+ "EndMelonDate,MelonAdvertiseDate,CustomerType,SendType,"
				+ "InsurCode,InsurCode,UserID,FiscalYear,SMTemplate,BranchID"
				+ " from AVS_Melon_SM  where SendTime is null";
		Connection smcs_conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		AVS_Send_SM avs_send_sm = null;
		try {
			smcs_conn = SplitterConnnectionPool.getSMCSConn();
			stmt = smcs_conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				avs_send_sm = new AVS_Send_SM();

				avs_send_sm.setSend_id(rs.getInt("ID"));
				StringBuffer sendTargetBuffer = new StringBuffer();
				avs_send_sm.setSend_type("客户红利派发通知");
				avs_send_sm.setProvince_id(rs.getString("ProvinceID"));
				avs_send_sm.setSend_description(rs
						.getString("SendTargetObject"));
				sendTargetBuffer.append(rs.getString("StartMelonDate")
						.substring(0, 10)
						+ "#");
				sendTargetBuffer.append(rs.getString("EndMelonDate").substring(
						0, 10)
						+ "#");
				sendTargetBuffer.append(rs.getString("MelonAdvertiseDate")
						.substring(0, 10)
						+ "#");
				sendTargetBuffer.append(rs.getString("CustomerType") + "#");
				avs_send_sm.setSend_target_type("14");
				avs_send_sm.setSend_target_ratio(100);
				avs_send_sm.setRequest_branch_id(rs.getString("ProvinceID"));
				sendTargetBuffer.append(rs.getInt("SendType") + "#");
				sendTargetBuffer.append(rs.getString("InsurCode") + "#");
				avs_send_sm.setRequest_user_id(rs.getString("UserID"));
				sendTargetBuffer.append(rs.getInt("FiscalYear") + "#");
				avs_send_sm.setSmcontent(rs.getString("SMTemplate"));
				avs_send_sm.setRoad_used(1);
				avs_send_sm.setPrompt_by_sm(1);
				sendTargetBuffer.append(rs.getString("BranchID") + "#");
				avs_send_sm.setSend_target(sendTargetBuffer.toString());
				m_task_list.add(avs_send_sm);
			}
		} catch (Exception e) {
			Log.LogInfo(TaskDistributor.class + "getMelonTask方法错误!");
		} finally {
			m_dataTools.closeResources(rs, stmt, smcs_conn);
		}
	}
	/**
	 * 
	 * 功能说明:  根据ID获取红利信息任务 
	 * @param str_sendid
	 * @param str_tab_name
	 */
	public void getMelonTask(String str_sendid,String str_tab_name) {
		String sql = "select ID,ProvinceID,SendTargetObject,StartMelonDate,"
				+ "EndMelonDate,MelonAdvertiseDate,CustomerType,SendType,"
				+ "InsurCode,InsurCode,UserID,FiscalYear,SMTemplate,BranchID"
				+ " from AVS_Melon_SM  where SendTime is null and id='"
				+ str_sendid + "'";
		Connection smcs_conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		AVS_Send_SM avs_send_sm = null;
		try {
			smcs_conn = SplitterConnnectionPool.getSMCSConn();
			stmt = smcs_conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				avs_send_sm = new AVS_Send_SM();
				avs_send_sm.setSend_id(rs.getInt("ID"));
				StringBuffer sendTargetBuffer = new StringBuffer();
				avs_send_sm.setSend_type("客户红利派发通知");
				avs_send_sm.setProvince_id(rs.getString("ProvinceID"));
				avs_send_sm.setSend_description(rs
						.getString("SendTargetObject"));
				sendTargetBuffer.append(rs.getString("StartMelonDate")
						.substring(0, 10)
						+ "#");
				sendTargetBuffer.append(rs.getString("EndMelonDate").substring(
						0, 10)
						+ "#");
				sendTargetBuffer.append(rs.getString("MelonAdvertiseDate")
						.substring(0, 10)
						+ "#");
				sendTargetBuffer.append(rs.getString("CustomerType") + "#");
				avs_send_sm.setSend_target_type("14");
				avs_send_sm.setSend_target_ratio(100);
				avs_send_sm.setRequest_branch_id(rs.getString("ProvinceID"));
				sendTargetBuffer.append(rs.getInt("SendType") + "#");
				sendTargetBuffer.append(rs.getString("InsurCode") + "#");
				avs_send_sm.setRequest_user_id(rs.getString("UserID"));
				sendTargetBuffer.append(rs.getInt("FiscalYear") + "#");
				avs_send_sm.setSmcontent(rs.getString("SMTemplate"));
				avs_send_sm.setRoad_used(1);
				avs_send_sm.setPrompt_by_sm(1);
				sendTargetBuffer.append(rs.getString("BranchID") + "#");
				avs_send_sm.setSend_target(sendTargetBuffer.toString());
				avs_send_sm.setTab_name(str_tab_name);
				m_task_list.add(avs_send_sm);
			}
		} catch (Exception e) {
			Log.LogInfo(TaskDistributor.class + "getMelonTask(String,String)方法错误!");
		} finally {
			m_dataTools.closeResources(rs, stmt, smcs_conn);
		}
	}
	/**
	 * 
	 * 功能说明: 任务分发
	 */
	@SuppressWarnings("unchecked")
	public int distributeTask(List<String> list_tabname) {
		this.m_table_name_list = list_tabname;
		int task_count = 0;
		getUnFinishedTask();
		//是否有未完成任务
		if (m_task_list == null || m_task_list.size() == 0) {
			// 获取非红利任务
			getTasks();
			// 获取红利任务
			getMelonTask();
		}
		// 创建无界线程池
		ExecutorService pool = Executors.newCachedThreadPool();
		if (m_task_list != null && m_task_list.size() > 0) {
			task_count += m_task_list.size();
			// 循环启动线程池
			for (int i = 0; i < m_task_list.size(); i++) {
				AVS_Send_SM avs_send_sm = (AVS_Send_SM) m_task_list.get(i);
				if(CommonTools.isEmpty(avs_send_sm.getTab_name())){
					String str_tab_name = m_table_name_list.get(0);
					avs_send_sm.setTab_name(str_tab_name);
					m_table_name_list.remove(0);
				}	
				Connection conn = SplitterConnnectionPool.getSMCSConn();
				new CommonTools().setStartTime(conn, avs_send_sm.getSend_id());
				// 服务种类类型
				String task_type = avs_send_sm.getSend_target_type();
				Class sender_class = m_sender_class_factory.get(task_type);
				if (sender_class == null) {
					continue;
				}
				try {
					Sender sender = (Sender) sender_class.newInstance();
					// 分发服务
					if (sender.loadTask(avs_send_sm)) {
						m_future_List.add((pool.submit(sender)));
					}
				} catch (Exception e) {
					Log.LogInfo(TaskDistributor.class
							+ "distributeTask方法，分发任务错误");
					continue;
				}
			}
		}
		if (m_future_List != null && m_future_List.size() > 0) {
			for (int j = 0; j < m_future_List.size(); j++) {
				Future future = m_future_List.get(j);
				try {
					// 此方法实现线程池的异步调用莫改动
					future.get();
				} catch (Exception e) {
					Log.LogInfo(TaskDistributor.class + "任务分发异步执行线程池异常。");
				}
			}
		}
		// 分发完任务清空集合
		m_future_List.clear();
		// 关闭线程池
		pool.shutdown();
		// 每次分发完任务清空集合
		m_task_list.clear();
		// 清空过滤表名集合
		m_table_name_list.clear();
		return task_count;
	}

}
