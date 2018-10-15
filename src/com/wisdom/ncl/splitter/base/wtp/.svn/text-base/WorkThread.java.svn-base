package com.wisdom.ncl.splitter.base.wtp;

import java.util.*;



public class WorkThread extends Thread
{
    ArrayList m_work_list;
    int m_thread_index;

    boolean m_bln_is_working = false;

    public WorkThread(ArrayList work_list, int thread_index)
    {
        m_work_list = work_list;
        m_bln_is_working = false;
        m_thread_index = thread_index;
    }

    public boolean isWorking()
    {
        return m_bln_is_working;
    }

    public int getThreadIndex()
    {
        return m_thread_index;
    }

    public void run()
    {
        if (m_work_list == null)
            return;

        while (true)
        {
            Work work = null;
            synchronized (m_work_list)
            {
                while (m_work_list.size() == 0)
                {
                    try
                    {
                        m_work_list.wait();
                    }
                    catch (Exception e)
                    {
                    }
                }

                work = (Work) m_work_list.remove(0);
                m_bln_is_working = true;
            }

            if (work != null)
            {
                try
                {
                    work.execute(m_thread_index);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            m_bln_is_working = false;
        }
    }
}
