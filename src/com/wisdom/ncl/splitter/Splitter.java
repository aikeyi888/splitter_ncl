package com.wisdom.ncl.splitter;
import java.util.Date;
import java.util.List;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

import com.wisdom.ncl.splitter.data.TempTable;
import com.wisdom.ncl.splitter.db.splitter.SplitterConnnectionPool;
import com.wisdom.ncl.splitter.sender.AgentGroupSender;
import com.wisdom.ncl.splitter.sender.ClientGroupSender;
import com.wisdom.ncl.splitter.sender.EmployeeAllSender;
import com.wisdom.ncl.splitter.sender.EmployeeGroupSender;
import com.wisdom.ncl.splitter.sender.EmployeeSelectSender;
import com.wisdom.ncl.splitter.sender.FileListDoubleSender;
import com.wisdom.ncl.splitter.sender.FileListSingleSender;
import com.wisdom.ncl.splitter.sender.MelonDistributeSender;
import com.wisdom.ncl.splitter.sender.MobilephoneSender;
import com.wisdom.ncl.splitter.sender.PersonalAllSender;
import com.wisdom.ncl.splitter.sender.PersonalGroupSender;
import com.wisdom.ncl.splitter.sender.PersonalSelectSender;
import com.wisdom.ncl.splitter.sender.StaticAddrSelectSender;
import com.wisdom.ncl.splitter.sender.StaticAddrSender;
import com.wisdom.ncl.splitter.tools.IniTools;
import com.wisdom.ncl.splitter.tools.Log;

public class Splitter {
	private TaskDistributor m_taskDistributor;

	private Scheduler m_scheduler;
	// �������Ƿ��Ѿ�������ʶ
	private boolean m_service_started = false;
	// ����ϵͳ����
	private boolean m_start_service = false;
	/**
	 * 
	 * ����˵��: ��������
	 */
	public void startService() {

		// �������ݿ����ӳ�
		SplitterConnnectionPool.create();
		// ������������
		WhiteListCache white_list_cache = new WhiteListCache();
		// �ȼ��ذ�������Ϣ֮���ٽ����������
		white_list_cache.updateWhiteList();

		// �������������������
		startWhiteListCacheJob();

		// ע�ᷢ������
		m_taskDistributor = new TaskDistributor();
		m_taskDistributor.registerSendType("1", PersonalAllSender.class);
		m_taskDistributor.registerSendType("2", EmployeeAllSender.class);
		m_taskDistributor.registerSendType("3", EmployeeGroupSender.class);
		m_taskDistributor.registerSendType("4", PersonalGroupSender.class);
		m_taskDistributor.registerSendType("5", ClientGroupSender.class);
		m_taskDistributor.registerSendType("6", AgentGroupSender.class);
		m_taskDistributor.registerSendType("7", StaticAddrSelectSender.class);
		m_taskDistributor.registerSendType("8", PersonalSelectSender.class);
		m_taskDistributor.registerSendType("9", EmployeeSelectSender.class);
		m_taskDistributor.registerSendType("10", MobilephoneSender.class);
		m_taskDistributor.registerSendType("11", FileListSingleSender.class);
		m_taskDistributor.registerSendType("12", FileListDoubleSender.class);
		m_taskDistributor.registerSendType("13", StaticAddrSender.class);
		m_taskDistributor.registerSendType("14", MelonDistributeSender.class);
		
		m_start_service = true;
		m_service_started = true;

		// ��������ַ�������
		while (m_start_service) {
			// ���ع��˱������б�
			TempTable temp_table = new TempTable();
			List<String> lst_tabname = temp_table.loadTempTable();
			int task_count = m_taskDistributor.distributeTask(lst_tabname);
			long sleep_duration = 500;
			if (task_count == 0) {
				sleep_duration = 2000;
			}
			try {
				Thread.sleep(sleep_duration);
			} catch (Exception e) {
			}
		}
		m_service_started = false;
	}

	/**
	 * 
	 * ����˵��: ֹͣ����
	 */
	public void stopService() {
		m_start_service = false;
		while (m_service_started) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
			}
		}
		stopWhiteListCacheJob();
	}

	/**
	 * 
	 * ����˵��: �������������Ϣjob
	 * 
	 */
	public void startWhiteListCacheJob() {
		// �����������ʱ����
		int while_list_check_cycle = Integer.parseInt(new IniTools().get(
				"whiteListCheck", "checkCycle").get(0));

		SchedulerFactory schedulerFactory = new StdSchedulerFactory();

		// ===================���°���������=====================
		JobDetail dataHandleJob = new JobDetail("whiteListCheck",
				"whiteListCheck", WhiteListCache.class);

		SimpleTrigger whiteListCacheTrigger = new SimpleTrigger(
				"WhiteListCacheTrigger", "WhiteListCacheGroup");
		// ��ʱִ��
		whiteListCacheTrigger.setStartTime(new Date(
				System.currentTimeMillis() + 60000));
		// ÿ����ִ��һ��
		whiteListCacheTrigger.setRepeatInterval(while_list_check_cycle);
		whiteListCacheTrigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);

		try {
			m_scheduler = schedulerFactory.getScheduler();
			m_scheduler.scheduleJob(dataHandleJob, whiteListCacheTrigger);
			// �ƻ���ʼ
			m_scheduler.start();
		} catch (SchedulerException e) {
			Log.LogInfo(Splitter.class + "startOverDueInfoJob��������");
		}
	}

	/**
	 * 
	 * ����˵��: ֹͣ���������Ϣ����
	 */
	public void stopWhiteListCacheJob() {
		try {
			m_scheduler.shutdown();
		} catch (SchedulerException e) {
			Log.LogInfo(Splitter.class + "stopOverDueInfoJob������ֹͣ��ʱ��������쳣��");
		}
	}

}
