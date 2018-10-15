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
	// 服务器是否已经启动标识
	private boolean m_service_started = false;
	// 启动系统服务
	private boolean m_start_service = false;
	/**
	 * 
	 * 功能说明: 开启服务
	 */
	public void startService() {

		// 启动数据库连接池
		SplitterConnnectionPool.create();
		// 白名单缓存类
		WhiteListCache white_list_cache = new WhiteListCache();
		// 先加载白名单信息之后再进行任务分配
		white_list_cache.updateWhiteList();

		// 启动清理过期数据任务
		startWhiteListCacheJob();

		// 注册发送类型
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

		// 启动任务分发器任务
		while (m_start_service) {
			// 加载过滤表名称列表
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
	 * 功能说明: 停止服务
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
	 * 功能说明: 启动清理过期信息job
	 * 
	 */
	public void startWhiteListCacheJob() {
		// 清除过期数据时间间隔
		int while_list_check_cycle = Integer.parseInt(new IniTools().get(
				"whiteListCheck", "checkCycle").get(0));

		SchedulerFactory schedulerFactory = new StdSchedulerFactory();

		// ===================更新白名单缓存=====================
		JobDetail dataHandleJob = new JobDetail("whiteListCheck",
				"whiteListCheck", WhiteListCache.class);

		SimpleTrigger whiteListCacheTrigger = new SimpleTrigger(
				"WhiteListCacheTrigger", "WhiteListCacheGroup");
		// 即时执行
		whiteListCacheTrigger.setStartTime(new Date(
				System.currentTimeMillis() + 60000));
		// 每分钟执行一次
		whiteListCacheTrigger.setRepeatInterval(while_list_check_cycle);
		whiteListCacheTrigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);

		try {
			m_scheduler = schedulerFactory.getScheduler();
			m_scheduler.scheduleJob(dataHandleJob, whiteListCacheTrigger);
			// 计划开始
			m_scheduler.start();
		} catch (SchedulerException e) {
			Log.LogInfo(Splitter.class + "startOverDueInfoJob方法错误");
		}
	}

	/**
	 * 
	 * 功能说明: 停止清除过期信息服务
	 */
	public void stopWhiteListCacheJob() {
		try {
			m_scheduler.shutdown();
		} catch (SchedulerException e) {
			Log.LogInfo(Splitter.class + "stopOverDueInfoJob方法，停止定时任务服务异常。");
		}
	}

}
