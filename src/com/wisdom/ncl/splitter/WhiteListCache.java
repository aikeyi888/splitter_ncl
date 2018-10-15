package com.wisdom.ncl.splitter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import net.sf.ehcache.Element;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.wisdom.ncl.splitter.db.splitter.SplitterConnnectionPool;
import com.wisdom.ncl.splitter.tools.DataTools;
import com.wisdom.ncl.splitter.tools.DateTools;
import com.wisdom.ncl.splitter.tools.EhcacheTools;
import com.wisdom.ncl.splitter.tools.IniTools;
import com.wisdom.ncl.splitter.tools.Log;

public class WhiteListCache implements Job {
	// ���湤����
	EhcacheTools eh_tools = new EhcacheTools();
	// ���ڹ�����
	DataTools data_tools = new DataTools();

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// ���°�������Ϣ
		updateWhiteList();
	}

	public void updateWhiteList() {
		// װ�������ļ���
		Element whitelist_element = eh_tools.getElement("whiteList",
				"whiteListMap");
		Map<String, String> white_list_map = null;
		if (whitelist_element != null) {
			white_list_map = (Map) whitelist_element.getValue();
		}
		if (white_list_map == null) {
			white_list_map = new HashMap<String, String>();
		}

		// ִ������ ��λ����
		int while_list_check_cycle = Integer.parseInt(new IniTools().get(
				"whiteListCheck", "checkCycle").get(0));

		String now_time = DateTools.getDate("yyyy-MM-dd HH:mm:ss");
		// �ϴβ�ѯ��ʱ��
		String last_query_time = DateTools.getBeforeTimeBym(now_time,
				"yyyy-MM-dd HH:mm:ss", while_list_check_cycle / 60000,
				"yyyy-MM-dd HH:mm:ss");

		if (white_list_map != null && white_list_map.size() > 0) {
			// ��ѯ AVS_White_List_Del_Temp������ ��map��ɾ��
			Connection sms_conn_1 = SplitterConnnectionPool.getSMSConn();
			PreparedStatement psmt_1 = null;
			ResultSet rs_1 = null;
			String delete_sql = "select ProvinceID ,Phone from AVS_White_List_Del_Temp where UpdateTime > '"
					+ last_query_time + "'";
			try {
				psmt_1 = sms_conn_1.prepareStatement(delete_sql);
				rs_1 = psmt_1.executeQuery();
				while (rs_1.next()) {
					String provinceID = rs_1.getString("ProvinceID");
					String mobilePhone = rs_1.getString("Phone");
					String key = provinceID + "|" + mobilePhone;
					// �Ƴ���������Ϣ
					if (white_list_map.containsKey(key)) {
						white_list_map.remove(key);
					}
				}
			} catch (Exception e) {
				Log.LogInfo(WhiteListCache.class + "�ġ��Ƴ���������Ϣ������");
				e.printStackTrace();
			} finally {
				data_tools.closeResources(rs_1, psmt_1, sms_conn_1);
			}
		}

		// ��ѯ��������Ϣ ��ӵ�map��
		Connection sms_conn = SplitterConnnectionPool.getSMSConn();
		String white_list_sql = "";
		if (white_list_map != null && white_list_map.size() > 0) {
			white_list_sql = "select ProvinceID ,Phone from AVS_White_List where UpdateTime > '"
					+ last_query_time + "'";
		} else {
			white_list_sql = "select ProvinceID ,Phone from AVS_White_List";
		}

		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			psmt = sms_conn.prepareStatement(white_list_sql);
			rs = psmt.executeQuery();
			while (rs.next()) {
				String provinceID = rs.getString("ProvinceID");
				String mobilePhone = rs.getString("Phone");
				String key = provinceID + "|" + mobilePhone;
				white_list_map.put(key, provinceID);
			}
		} catch (Exception e) {
			Log.LogInfo(WhiteListCache.class + "�ġ���Ӱ�������Ϣ������");
		} finally {
			data_tools.closeResources(rs, psmt, sms_conn);
		}

		// ����������Ϣ��ӵ�������
		eh_tools.putObjectCached("whiteList", "whiteListMap", white_list_map);
	}
}
