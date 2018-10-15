package com.wisdom.ncl.splitter.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Vector;

/**
 * <p>Title: 连接池</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ConnectionPool
{
    private static Vector m_pool_list = new Vector(5);  //连接池的初始大小
    String m_str_pool_name;//连接池的名称
    Vector m_unused_connections = new Vector(10, 10); //还没用的连接池大小
    Vector m_used_connections = new Vector(10, 10); //已经使用的连接列表
    boolean m_bln_is_valid = false;
    boolean m_bln_need_checktimeout = false; //是否需要检查超时 默认不需要
    String m_str_driver;//驱动
    String m_str_url;  //URL
    String m_str_user; //用户名
    String m_str_password;//密码
    int m_int_count;

    static
    {
        MonitorThread monitor_thread = new MonitorThread();
        monitor_thread.start();
    }


    public static synchronized boolean create(String pool_name, String driver, String url, String user, String password, int num)
    {
         for (int i=0; i<m_pool_list.size(); i++)
         {
             ConnectionPool pool = (ConnectionPool)m_pool_list.elementAt(i);
             if (pool != null && pool.m_str_pool_name.equals(pool_name))
             {
                 return false;
             }
         }

         ConnectionPool pool2 = new ConnectionPool(pool_name, driver, url, user, password, num);
         if (pool2.m_unused_connections.size() == 0)
             return false;
         else
         {
             m_pool_list.addElement(pool2); //将连接池加入到Vector中
             return true;
         }
    }

    public static synchronized boolean create(boolean txtconn,String pool_name, String driver, String url, String user, String password, int num)
    {
         for (int i=0; i<m_pool_list.size(); i++)
         {
             ConnectionPool pool = (ConnectionPool)m_pool_list.elementAt(i);
             if (pool != null && pool.m_str_pool_name.equals(pool_name))
             {
                 return false;
             }
         }

         ConnectionPool pool2 = new ConnectionPool(pool_name, driver, url, user, password, num);
         if (pool2.m_unused_connections.size() == 0)
         {
             m_pool_list.addElement(pool2);
             return false;
         }
         else
         {
             m_pool_list.addElement(pool2); //将连接池加入到Vector中
             return true;
         }
    }


    public static synchronized ConnectionPool get(String pool_name)
    {
        if (pool_name == null && m_pool_list.size() > 0)
            return (ConnectionPool)m_pool_list.elementAt(0);

        for (int i=0; i<m_pool_list.size(); i++)
        {
            ConnectionPool pool = (ConnectionPool)m_pool_list.elementAt(i);
            if (pool != null && pool.m_str_pool_name.equals(pool_name))
                return pool;
        }

        return null;
    }

    public static synchronized Vector getPoolList()
    {
        return m_pool_list;
    }

    //构造器
    public ConnectionPool(String pool_name, String driver, String url, String user, String password, int num)
    {
        m_str_pool_name = pool_name;
        m_str_driver = driver;
        m_str_url = url;
        m_str_user = user;
        m_str_password = password;
        m_int_count = num;
        initPool(driver, url, user, password, num);//初始化连接池
    }

    public String getURL()
    {
        return m_str_url;
    }

    public String getUser()
    {
        return m_str_user;
    }

    public String getPassword()
    {
        return m_str_password;
    }

    public String getName()
    {
        return m_str_pool_name;
    }

    //初始化连接池
    public synchronized void initPool(String driver, String url, String user, String password, int num)
    {
        try
        {
            java.lang.Class.forName(driver);
        }
        catch (Exception e)
        {
        }

        synchronized (m_used_connections)
        {
            for (int i=0; i<m_used_connections.size(); i++)//先关闭所有的连接，释放资源
            {
                try
                {
                    PoolConnection connection = (PoolConnection)m_used_connections.elementAt(i);
                    connection.setTimeOut();
                    connection.close();
                }
                catch (Exception e)
                {
                }
            }
            m_used_connections.removeAllElements();//移出m_unused_connections中所有的连接对象
        }

        synchronized (m_unused_connections)
        {
            for (int i=0; i<m_unused_connections.size(); i++)//先关闭所有的连接，释放资源
            {
                try
                {
                    PoolConnection connection = (PoolConnection)m_unused_connections.elementAt(i);
                    connection.setTimeOut();
                    connection.close();
                }
                catch (Exception e)
                {
                }
            }
            m_unused_connections.removeAllElements();//移出m_unused_connections中所有的连接对象

            for (int i=0; i<num; i++)
            {
                try
                {
                    Connection connection = DriverManager.getConnection(url, user, password);
                    if (connection != null)
                        m_unused_connections.addElement(new PoolConnection(connection, this));
                }
                catch (Exception e)
                {
                }
            }

            if (m_unused_connections.size() > 0)
                m_bln_is_valid = true;
            else
                m_bln_is_valid = false;
        }
    }

    public boolean isValid()
    {
        return m_bln_is_valid;
    }

    public void checkPool()
    {
    }

    public Connection newConnection()
    {
        try
        {
            Connection connection = DriverManager.getConnection(m_str_url, m_str_user, m_str_password);
            return connection;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public Connection getConnection()
    {
        try
        {
            return getConnection(5);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public Connection getConnection(int delay)
    {
        if (delay <= 0)
            delay = 1;

        for (int i=0; i<delay; i++)
        {
            synchronized (m_unused_connections)
            {
                synchronized (m_used_connections)
                {
                    if (m_unused_connections.size() > 0)
                    {
                        PoolConnection connection = (PoolConnection)
                            m_unused_connections.elementAt(0);
                        m_unused_connections.removeElementAt(0);

                        if (m_bln_need_checktimeout)
                        {
                            //检查连接从上次回收到再次被取出，是否已经超时。
                            connection.checkTimeOut();
                            if (connection.isTimeOut())
                            {
                                try
                                {
                                    connection.close();
                                }
                                catch (Exception e)
                                {

                                }
                                //补充一个新的连接到列表中
                                if (m_unused_connections.size() < m_int_count)
                                {
                                    Connection conn = newConnection();
                                    if (conn != null)
                                    {
                                        m_unused_connections.addElement(new
                                            PoolConnection(conn, this));
                                    }
                                }
                                break;
                            }
                            else
                            {
                                m_used_connections.addElement(connection);
                                return connection;
                            }
                        }
                        else
                        {
                            m_used_connections.addElement(connection);
                            return connection;
                        }
                    }
                }
            }

            if (delay > 1)
            {
                try
                {
                    Thread.sleep(2000);
                }
                catch (Exception e)
                {
                }
            }
        }

        try
        {
            Connection conn = newConnection();
            if (conn != null)
            {
                synchronized (m_used_connections)
                {
                    PoolConnection pool_conn = new PoolConnection(conn, this);
                    m_used_connections.addElement(pool_conn);
                    return pool_conn;
                }
            }
        }
        catch (Exception e)
        {
        }

        return null;
    }

    public void revokeConnection(PoolConnection connection)
    {
        synchronized (m_unused_connections)
        {
            synchronized (m_used_connections)
            {
                m_used_connections.removeElement(connection);

                if (m_unused_connections.size() >= m_int_count)
                {
                    try
                    {
                        connection.setTimeOut();
                        connection.close();
                    }
                    catch (Exception e)
                    {
                    }
                }
                else
                {
                    m_unused_connections.addElement(connection);
                }
            }
        }
    }

    public static synchronized void destory(String pool_name)
    {
        destroy(pool_name);
    }

    public static synchronized void destroy(String pool_name)
    {
        for (int i=0; i<m_pool_list.size(); i++)
        {
            Object o = m_pool_list.elementAt(i);
            if (o != null)
            {
                ConnectionPool conn_pool = (ConnectionPool)o;
                if ((pool_name == null && (conn_pool.getName() == null || conn_pool.getName().equals(""))) ||
                    (pool_name != null &&  pool_name.equals(conn_pool.getName())))
                {
                    conn_pool.destory();
                    return;
                }
            }
        }
    }

    public synchronized void destory()
    {
        destroy();
    }

    public synchronized void destroy()
    {
        for(int i=0; i<m_unused_connections.size(); i++)
        {
            Object o = m_unused_connections.elementAt(i);
            if (o != null)
            {
                PoolConnection conn = (PoolConnection)o;
                try
                {
                    conn.setTimeOut();
                    conn.close();
                }
                catch(Exception e)
                {
                }
            }
        }
        m_unused_connections.removeAllElements();


        for(int i=0; i<m_used_connections.size(); i++)
        {
            Object o = m_used_connections.elementAt(i);
            if (o != null)
            {
                PoolConnection conn = (PoolConnection)o;
                try
                {
                    conn.setTimeOut();
                    conn.close();
                }
                catch(Exception e)
                {
                }
            }
        }
        m_used_connections.removeAllElements();

    }

    public synchronized void refresh()
    {
        for(int i=0; i<m_unused_connections.size(); i++)
        {
            Object o = m_unused_connections.elementAt(i);
            if (o != null)
            {
                PoolConnection conn = (PoolConnection)o;
                try
                {
                    conn.setTimeOut();
                    conn.close();
                }
                catch(Exception e)
                {
                }
            }
        }
        m_unused_connections.removeAllElements();


        for(int i=0; i<m_used_connections.size(); i++)
        {
            Object o = m_used_connections.elementAt(i);
            if (o != null)
            {
                PoolConnection conn = (PoolConnection)o;
                try
                {
                    conn.refresh();
                }
                catch(Exception e)
                {
                }
            }
        }
        m_used_connections.removeAllElements();
    }
}


class MonitorThread extends Thread
{
    public void run()
    {
        while (true)
        {
            try
            {
                Vector pool_list = ConnectionPool.getPoolList();
                if (pool_list != null)
                {
                    for (int i=0; i<pool_list.size(); i++)
                    {
                        ConnectionPool pool = (ConnectionPool)pool_list.elementAt(i);
                        if (pool != null)
                        {
                            Connection conn;
                            try
                            {
                                conn = pool.getConnection();
                                if (conn != null) //检测连接是否有效
                                {
                                    conn.close();
                                    continue;
                                }
                            }
                            catch (Exception e)
                            {
                            }

                            pool.refresh();
                        }
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            try
            {
                Thread.sleep(5000);
            }
            catch (Exception e)
            {
            }
        }
    }
}



class ConnectionCollectThread extends Thread
{
    public void run()
    {
        while (true)
        {
            try
            {
                System.gc();

                Thread.sleep(1000);
            }
            catch (Exception e)
            {
            }
        }
    }
}
