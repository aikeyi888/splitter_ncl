package com.wisdom.ncl.splitter.sender;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.concurrent.Callable;

import com.wisdom.ncl.splitter.Sender;
import com.wisdom.ncl.splitter.data.AVS_Send_SM;
import com.wisdom.ncl.splitter.data.Melon;
import com.wisdom.ncl.splitter.data.SendTarget;
import com.wisdom.ncl.splitter.data.SysConfig;
import com.wisdom.ncl.splitter.db.splitter.SplitterConnnectionPool;
import com.wisdom.ncl.splitter.tools.CommonTools;
import com.wisdom.ncl.splitter.tools.DataTools;
import com.wisdom.ncl.splitter.tools.DateTools;
import com.wisdom.ncl.splitter.tools.Log;

/**
 * 客户红利派发通知
 * 
 * @author 侯松芳
 * 
 */
@SuppressWarnings("unchecked")
public class MelonDistributeSender extends Sender implements Callable {

	// 数据处理工具类
	private DataTools m_data_tools = new DataTools();
	// 任务信息类
	private AVS_Send_SM m_send_sm;
	// 系统配置
	private SysConfig sysConfig;
	// 发送目标
	private SendTarget sendSM;

	/**
	 * 加载任务
	 */
	public boolean loadTask(AVS_Send_SM send_sm) {
		this.m_send_sm = send_sm;
		return super.loadTask(m_send_sm);
	}

	public Object call() throws Exception {
		Connection m_stand_conn = SplitterConnnectionPool.getSTANDConn();
		if (m_send_sm != null) {
			// 获取待发送目标
			String sendTarget = m_send_sm.getSend_target();
			if (sendTarget != null) {
				// 获取红利信息的SQL语句
				String sql = getMelonSQL(sendTarget, m_send_sm.getProvince_id());
				PreparedStatement psmt = null;
				ResultSet rs = null;
				try {
					psmt = m_stand_conn.prepareStatement(sql);
					// 执行SQL
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
				} catch (SQLException e) {
					Log.LogInfo(MelonDistributeSender.class + ".call方法错误！");
				} finally {
					m_data_tools.closeResources(rs, psmt, m_stand_conn);
				}

				// 修改SendTime
				finishMelonTask();
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
		// 封装红利查询结果
		Melon melon = new Melon();
		String provinceID = null;
		String branchID = null;
		String name = null;
		String insurID = null;
		String sex = null;
		String insurName = null;
		String fiscalYear = null;
		String mobilePhone = null;
		try {
			provinceID = rs.getString("ProvinceID");
			branchID = rs.getString("BranchID");
			name = rs.getString("Name");
			insurID = rs.getString("InsurID");
			sex = rs.getString("Sex");
			insurName = rs.getString("InsurName");
			fiscalYear = rs.getString("FiscalYear");
			mobilePhone = rs.getString("MobilePhone");
		} catch (SQLException e) {
			Log.LogInfo(MelonDistributeSender.class
					+ ".subHandleSendTarget方法错误！");
		}

		if (!CommonTools.isEmpty(provinceID)) {
			melon.setProvinceID(provinceID.trim());
		}
		if (!CommonTools.isEmpty(branchID)) {
			melon.setBranchID(branchID.trim());
		}
		if (!CommonTools.isEmpty(name)) {
			melon.setName(name.trim());
		}
		if (!CommonTools.isEmpty(insurID)) {
			melon.setInsurID(insurID.trim());
		}
		if (!CommonTools.isEmpty(sex)) {
			melon.setSex(sex.trim());
		}
		if (!CommonTools.isEmpty(insurName)) {
			melon.setInsurName(insurName.trim());
		}
		if (!CommonTools.isEmpty(fiscalYear)) {
			melon.setFiscalYear(fiscalYear.trim());
		}
		if (!CommonTools.isEmpty(mobilePhone)) {
			melon.setMobilePhone(mobilePhone.trim());
		}

		sendSM = new SendTarget();
		// 替换短信内容
		String smContent = replaceSMContent(melon, m_send_sm);
		// 读取系统参数
		sysConfig = readFromDB();
		// 更新时间
		updateTime(m_send_sm);
		sendSM.setMobilephone(melon.getMobilePhone());
		sendSM.setProvinceID(melon.getProvinceID());
		sendSM.setBranchID(melon.getBranchID());
		sendSM.setName(melon.getName());
		sendSM.setSMContent(smContent);
		sendSM.setTableName(m_send_sm.getTab_name());
		// 过滤短信
		handleSendTarget(sendSM);
	}

	/**
	 * 修改待发送目标的发送开始时间和结束时间
	 * 
	 * @param m_send_sm
	 */
	private void updateTime(AVS_Send_SM m_send_sm) {
		String startHour = getParameter("SMSendBeginHour", m_send_sm
				.getProvince_id());
		String startMinute = getParameter("SMSendBeginMin", m_send_sm
				.getProvince_id());
		String endHour = getParameter("SMSendEndHour", m_send_sm
				.getProvince_id());
		String endMinute = getParameter("SMSendEndMin", m_send_sm
				.getProvince_id());

		if ("".equals(startHour) || "".equals(startMinute)) {
			sendSM.setRCompleteHourBegin(Integer.parseInt(sysConfig
					.getSmSendTimeBegin()));
		} else {
			sendSM.setRCompleteHourBegin(Integer.parseInt(startHour) * 60
					+ Integer.parseInt(startMinute));
		}
		if ("".equals(endHour) || "".equals(endMinute)) {
			sendSM.setRCompleteHourEnd(Integer.parseInt(sysConfig
					.getSmSendTimeEnd()));
		} else {
			sendSM.setRCompleteHourEnd(Integer.parseInt(endHour) * 60
					+ Integer.parseInt(endMinute));
		}
		long intervaltime = 7 * 24 * 60 * 60 * 1000;
		sendSM.setRCompleteTimeBegin(DateTools
				.getDate("yyyy-MM-dd HH:mm:ss:SSS"));
		sendSM.setRCompleteTimeEnd(DateTools.getAfterDateByMillisecond(
				new Date(), intervaltime, "yyyy-MM-dd HH:mm:ss:SSS"));
	}

	/**
	 * 读取系统参数
	 * 
	 * @return 系统配置
	 */
	private SysConfig readFromDB() {
		// 读取系统参数列表中的短信发送开始时间
		sysConfig = new SysConfig();
		String begin = getParameter("SMSendBeginHour", "0");
		if ("".equals(begin))
			begin = "480";
		else
			begin = Integer.parseInt(begin.trim()) * 60 + "";
		String end = getParameter("SMSendEndHour", "0");
		if ("".equals(end))
			end = "1200";
		else
			end = Integer.parseInt(end.trim()) * 60 + "";
		if (Integer.parseInt(end) > 23 * 60 + 59)
			end = 23 * 60 + 59 + "";
		if ("".equals(begin) && "".equals(end))
			end = 23 * 60 + 59 + "";

		sysConfig.setSmSendTimeBegin(begin);
		sysConfig.setSmSendTimeEnd(end);
		return sysConfig;
	}

	/**
	 * 获取系统参数
	 * 
	 * @param paramName
	 * @param provinceID
	 * @return
	 */
	private String getParameter(String paramName, String provinceID) {
		Connection m_sms_conn = SplitterConnnectionPool.getSMSConn();
		String paramValue = "";
		String sql = "select ParamValue from SM_System_Param where ParamName = '"
				+ paramName + "' and ProvinceID = " + provinceID;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = m_sms_conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				paramValue = rs.getString("ParamValue");
			}
		} catch (SQLException e) {
			Log.LogInfo(MelonDistributeSender.class + ".getParameter方法错误！");
		} finally {
			m_data_tools.closeResources(rs, stmt, m_sms_conn);
		}
		return paramValue;
	}

	/**
	 * 替换短信内容
	 * 
	 * @param melon
	 * @param send_sm
	 * @return
	 */
	private String replaceSMContent(Melon melon, AVS_Send_SM send_sm) {
		String smContent = send_sm.getSmcontent();
		smContent.replace("<分公司ID>", melon.getProvinceID());
		smContent.replace("<支公司ID>", melon.getBranchID());
		smContent.replace("<投保人姓名>", melon.getName());
		String sex = melon.getSex();
		if ("1".equals(sex)) {
			smContent.replace("<投保人性别>", "女士");
		} else if ("0".equals(sex))
			smContent.replace("<投保人性别>", "先生");
		else
			smContent.replace("<投保人性别>", "");
		smContent.replace("<保单号>", melon.getInsurID());
		smContent.replace("<险种名称>", melon.getInsurName());
		smContent.replace("<分红年度>", melon.getFiscalYear());
		return smContent;
	}

	/**
	 * 获取客户红利派发通知的SQL语句
	 * 
	 * @param sendTarget
	 * @param provinceID
	 * @return
	 */
	private String getMelonSQL(String sendTarget, String provinceID) {
		int nPos;
		// sendTarget是按#号分隔的拼接字段，因此需按#号截取成各个字段
		String[] fields = new String[8];
		String temp = sendTarget;
		fields = temp.split("#");
		String startMelonDate = fields[0];
		String endMelonDate = fields[1];
		String advertiseDate = fields[2];
		String customerType = fields[3];
		String sendType = fields[4];
		String insurCode = fields[5];
		String fiscalYear = fields[6];
		String branchID = fields[7];
		StringBuffer getMelonSQL = new StringBuffer();
		if ("0".equals(sendType)) {
			if ("0".equals(customerType)) {
				getMelonSQL.append("select ");
				getMelonSQL
						.append("insurinfo.provinceid,insurinfo.branchid,customerinfo.name,insurinfo.insurid,");
				getMelonSQL
						.append("customerinfo.sex,insurcode.insurname,fiscalyear,mobilephone ");
				getMelonSQL
						.append(" from insurinfo,customerinfo,meloninfo,insurcode");
				getMelonSQL.append(" where ");
				getMelonSQL.append("insurinfo.insurid=meloninfo.insurid");
				getMelonSQL
						.append(" and insurinfo.policyholderid=customerinfo.customerid");
				getMelonSQL.append(" and fiscalyear = '" + fiscalYear + "'");
				getMelonSQL
						.append(" and insurcode.insurcode = insurinfo.insurcode");
				getMelonSQL.append(" and insurinfo.provinceid = '" + provinceID
						+ "'");
				getMelonSQL
						.append(" and substring(insurInfo.InsurStatus,1,2)='00'");
				getMelonSQL.append(" and MelonDate = '" + advertiseDate + "'");
				getMelonSQL.append(" and insurinfo.customertype = '0'");
				getMelonSQL.append(" and mobilephone is not null");
				getMelonSQL.append(" and mobilephone <> ''");
			} else {
				getMelonSQL.append("select ");
				getMelonSQL
						.append(" insurinfo.provinceid,insurinfo.branchid,customerinfo.name,insurinfo.insurid,");
				getMelonSQL
						.append(" customerinfo.sex,insurcode.insurname,fiscalyear,mobilephone");
				getMelonSQL
						.append(" from insurinfo,customerinfo,meloninfo,insurcode");
				getMelonSQL.append(" where ");
				getMelonSQL.append("insurinfo.insurid=meloninfo.insurid");
				getMelonSQL
						.append(" and insurinfo.policyholderid=customerinfo.customerid");
				getMelonSQL.append(" and fiscalyear = '" + fiscalYear + "'");
				getMelonSQL
						.append(" and insurcode.insurcode = insurinfo.insurcode");
				getMelonSQL.append(" and insurinfo.provinceid = '" + provinceID
						+ "'");
				getMelonSQL
						.append(" and substring(insurInfo.InsurStatus,1,2)='00'");
				getMelonSQL.append(" and MelonDate = '" + advertiseDate + "'");
				getMelonSQL.append(" and insurinfo.customertype in ('1','2')");
				getMelonSQL.append(" and mobilephone is not null");
				getMelonSQL.append(" and mobilephone <> ''");
			}
		} else {
			if ("0".equals(customerType)) {
				getMelonSQL.append("select ");
				getMelonSQL
						.append("insurinfo.provinceid,insurinfo.branchid,customerinfo.name,insurinfo.insurid,");
				getMelonSQL
						.append("customerinfo.sex,insurcode.insurname,fiscalyear,mobilephone ");
				getMelonSQL
						.append(" from insurinfo,customerinfo,meloninfo,insurcode");
				getMelonSQL.append(" where ");
				getMelonSQL.append("insurinfo.insurid=meloninfo.insurid");
				getMelonSQL
						.append(" and insurinfo.policyholderid=customerinfo.customerid");
				getMelonSQL.append(" and fiscalyear = '" + fiscalYear + "'");
				getMelonSQL
						.append(" and MelonDate >= '" + startMelonDate + "'");
				getMelonSQL.append(" and MelonDate <= '" + endMelonDate + "'");
				getMelonSQL
						.append(" and insurcode.insurcode = insurinfo.insurcode");
				getMelonSQL.append(" and insurinfo.provinceid= '" + provinceID
						+ "'");
				getMelonSQL.append(" and substring(InsurStatus,1,2)='00'");
				getMelonSQL
						.append(" and DATEPART(month,InsurEffectdate) = DATEPART(month,MelonDate)");
				getMelonSQL
						.append(" and DATEPART(day,InsurEffectdate) = DATEPART(day,MelonDate)");
				getMelonSQL.append(" and insurinfo.customertype = '0'");
				getMelonSQL.append(" and mobilephone is not null");
			} else {
				getMelonSQL.append("select ");
				getMelonSQL
						.append("insurinfo.provinceid,insurinfo.branchid,customerinfo.name,insurinfo.insurid,");
				getMelonSQL
						.append("customerinfo.sex,insurcode.insurname,fiscalyear,mobilephone ");
				getMelonSQL
						.append("from insurinfo,customerinfo,meloninfo,insurcode");
				getMelonSQL.append(" where ");
				getMelonSQL.append("insurinfo.insurid=meloninfo.insurid");
				getMelonSQL
						.append(" and insurinfo.policyholderid=customerinfo.customerid");
				getMelonSQL.append(" and fiscalyear = '" + fiscalYear + "'");
				getMelonSQL
						.append(" and MelonDate >= '" + startMelonDate + "'");
				getMelonSQL.append(" and MelonDate <= '" + endMelonDate + "'");
				getMelonSQL
						.append(" and insurcode.insurcode = insurinfo.insurcode");
				getMelonSQL.append(" and insurinfo.provinceid= '" + provinceID
						+ "'");
				getMelonSQL.append(" and substring(InsurStatus,1,2)='00'");
				getMelonSQL
						.append(" and DATEPART(month,InsurEffectdate) = DATEPART(month,MelonDate)");
				getMelonSQL
						.append(" and DATEPART(day,InsurEffectdate) = DATEPART(day,MelonDate)");
				getMelonSQL.append(" and insurinfo.customertype in ('1','2')");
				getMelonSQL.append(" and mobilephone is not null");
			}
		}
		if (!insurCode.isEmpty()) {
			// 重新拼接险种代码
			temp = insurCode;
			insurCode = "";
			while ((nPos = temp.indexOf(',')) != -1) {
				insurCode += "'" + temp.substring(0, nPos) + "',";
				temp = temp.substring(0, nPos + 1);
			}
			if (!temp.isEmpty())
				insurCode += "'" + temp + "',";
			insurCode = "(" + insurCode.substring(0, insurCode.length() - 1)
					+ ")";
			getMelonSQL.append(" and InsurCode.InsurCode in " + insurCode);
		}
		if (!branchID.isEmpty()) {
			// 重新拼接BranchID
			StringBuffer branch_temp = new StringBuffer();
			String[] branchFields = branchID.split("\\|");
			branchID = "";
			for (int i = 0; i < branchFields.length; i++) {
				branch_temp.append("'").append(branchFields[i]).append("',");
				branchID = branch_temp.toString();
			}
			if (branchFields.length != 0) {
				branchID = "(" + branchID.substring(0, branchID.length() - 1)
						+ ")";
			}
			getMelonSQL.append(" and insurinfo.branchid in " + branchID);
		}
		return getMelonSQL.toString();
	}

}
