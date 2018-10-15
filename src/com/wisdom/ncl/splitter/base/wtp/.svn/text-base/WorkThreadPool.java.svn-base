package com.wisdom.ncl.splitter.base.wtp;

import java.util.*;

public class WorkThreadPool
{
    ArrayList m_work_thread_list = new ArrayList(10);

    ArrayList m_work_list = new ArrayList();

    public WorkThreadPool(int pool_num)
    {
        for (int i=0; i<pool_num; i++)
        {
            WorkThread work_thread = new WorkThread(m_work_list, i);
            m_work_thread_list.add(work_thread);
        }
    }

    public synchronized void addWork(Work work)
    {
        synchronized (m_work_list)
        {
            m_work_list.add(work);
            m_work_list.notify();
        }
    }

    public synchronized boolean cancelWork(Work work)
    {
        synchronized (m_work_list)
        {
            return m_work_list.remove(work);
        }
    }

    public synchronized boolean isIdle()
    {
        if (m_work_list.size() > 0)
            return false;

        for (int i=0; i<m_work_thread_list.size(); i++)
        {
            WorkThread work_thread = (WorkThread)m_work_thread_list.get(i);
            if (work_thread.isWorking())
                return false;
        }

        return true;
    }

    public synchronized void start()
    {
        for (int i=0; i<m_work_thread_list.size(); i++)
        {
            WorkThread work_thread = (WorkThread)m_work_thread_list.get(i);
            work_thread.start();
        }
    }

    public synchronized void stop()
    {
        for (int i=0; i<m_work_thread_list.size(); i++)
        {
            WorkThread work_thread = (WorkThread)m_work_thread_list.get(i);
            work_thread.stop();
        }
    }

    public void finalize()
    {
        stop();
    }
}
