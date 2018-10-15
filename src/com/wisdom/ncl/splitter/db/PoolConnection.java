package com.wisdom.ncl.splitter.db;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;

/**
 * <p>Title: 连接</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class PoolConnection implements Connection
{
    Connection m_conn;
    ConnectionPool m_connection_pool;
    java.util.Date m_refresh_time = new java.util.Date(); //时间标记
    private final static long TIMEOUT = 6000;//43200000;//超时时间12小时
    boolean m_bln_time_out = false; //是否超时
    boolean m_bln_closed = false; //是否关闭

    public PoolConnection(Connection conn, ConnectionPool connection_pool)
    {
        m_conn = conn;
        m_connection_pool = connection_pool;
    }

    private void checkConnection()
    {
        boolean closed = true;
        try
        {
            closed = m_conn.isClosed();
        }
        catch (Exception e)
        {
        }

        if (m_conn == null || m_bln_closed || closed)
        {
            if (m_conn != null)
            {
//                if (closed)
//                {
                    m_conn = m_connection_pool.newConnection();
//                }
            }
            else
            {
                m_conn = m_connection_pool.newConnection();
            }

            m_bln_closed = false;
        }
    }

    /***** refresh: 连接已断，清除连接，等待checkConnection重新建立连接 *****/
    protected void refresh()
    {
        try
        {
            if (m_conn != null)
                m_conn.close();
        }
        catch (Exception e)
        {
        }

        m_conn = null;
    }

    public Statement createStatement() throws SQLException
    {
        checkConnection();
        Statement stmt = null;
        try
        {
           stmt = m_conn.createStatement();
        }
        catch(Exception e)
        {
            //e.printStackTrace();
            m_conn = m_connection_pool.newConnection();
            stmt = m_conn.createStatement();
        }
        return stmt;

    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException
    {
        checkConnection();
        return m_conn.createStatement(resultSetType, resultSetConcurrency);
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
    {
        checkConnection();
        return m_conn.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException
    {
        checkConnection();
        PreparedStatement stmt = null;
        try
        {
           stmt = m_conn.prepareStatement(sql);
        }
        catch(Exception e)
        {
            //e.printStackTrace();
            m_conn = m_connection_pool.newConnection();
            stmt = m_conn.prepareStatement(sql);
        }
        return stmt;

    }

    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException
    {
        checkConnection();
        return m_conn.prepareStatement(sql, columnIndexes);
    }

    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException
    {
        checkConnection();
        return m_conn.prepareStatement(sql, columnNames);
    }

    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException
    {
        checkConnection();
        return m_conn.prepareStatement(sql, autoGeneratedKeys);
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException
    {
        checkConnection();
        return m_conn.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
    {
        checkConnection();
        return m_conn.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public CallableStatement prepareCall(String sql) throws SQLException
    {
        checkConnection();
        return m_conn.prepareCall(sql);
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException
    {
        checkConnection();
        return m_conn.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException
    {
        checkConnection();
        return m_conn.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public String nativeSQL(String sql) throws SQLException
    {
        checkConnection();
        return m_conn.nativeSQL(sql);
    }

    public void setAutoCommit(boolean b) throws SQLException
    {
        checkConnection();
        m_conn.setAutoCommit(b);
    }

    public boolean getAutoCommit() throws SQLException
    {
        checkConnection();
        return m_conn.getAutoCommit();
    }

    public void commit() throws SQLException
    {
        checkConnection();
        m_conn.commit();
    }

    public void rollback() throws SQLException
    {
        checkConnection();
        m_conn.rollback();
    }

    public void close() throws SQLException
    {
        /*
         * 因为unused_connections列表中存放的都是PoolConnection，不是每次getConnection都返回new PoolConnection
         * m_closed被重置。
         * 所以被回收的PoolConnection的m_closed仍然为false。
         * 只有被关闭的PoolConnection的m_closed才是true。
         * 被关闭的PoolConnection设置m_closed已经没有意义了。被销毁了。
         */
        if (isTimeOut())
        {
            try
            {
                m_conn.close();
            }
            catch (Exception e)
            {
            }
            m_conn = null;
            m_bln_closed = true;
        }
        else
        {
            if (m_conn != null && !m_conn.isClosed())
            {
                refreshTime();
                try
                {
                    m_conn.commit();
                }
                catch (Exception e)
                {
                }
                m_connection_pool.revokeConnection(this);
            }
        }
    }

    public boolean isClosed() throws SQLException
    {
        if (m_conn == null || m_conn.isClosed())
            return true;

        return m_bln_closed;
    }

    public DatabaseMetaData getMetaData() throws SQLException
    {
        checkConnection();
        return m_conn.getMetaData();
    }

    public void setReadOnly(boolean b) throws SQLException
    {
        checkConnection();
        m_conn.setReadOnly(b);
    }

    public boolean isReadOnly() throws SQLException
    {
        checkConnection();
        return m_conn.isReadOnly();
    }

    public void setCatalog(String a) throws SQLException
    {
        checkConnection();
        m_conn.setCatalog(a);
    }

    public String getCatalog() throws SQLException
    {
        checkConnection();
        return m_conn.getCatalog();
    }

    public void setTransactionIsolation(int a) throws SQLException
    {
        checkConnection();
        m_conn.setTransactionIsolation(a);
    }

    public int getTransactionIsolation() throws SQLException
    {
        checkConnection();
        return m_conn.getTransactionIsolation();
    }

    public SQLWarning getWarnings() throws SQLException
    {
        checkConnection();
        return m_conn.getWarnings();
    }

    public void clearWarnings() throws SQLException
    {
        checkConnection();
        m_conn.clearWarnings();
    }

    public void setTypeMap(java.util.Map map) throws SQLException
    {
        checkConnection();
        m_conn.setTypeMap(map);
    }

    public Map getTypeMap() throws SQLException
    {
        checkConnection();
        return m_conn.getTypeMap();
    }

    public void setHoldability(int a) throws SQLException
    {
        checkConnection();
        m_conn.setHoldability(a);
    }

    public int getHoldability() throws SQLException
    {
        checkConnection();
        return m_conn.getHoldability();
    }

    public Savepoint setSavepoint() throws SQLException
    {
        checkConnection();
        return m_conn.setSavepoint();
    }

    public Savepoint setSavepoint(String s) throws SQLException
    {
        checkConnection();
        return m_conn.setSavepoint(s);
    }

    public void rollback(Savepoint a) throws SQLException
    {
        checkConnection();
        m_conn.rollback(a);
    }

    public void releaseSavepoint(Savepoint a) throws SQLException
    {
        checkConnection();
        m_conn.releaseSavepoint(a);
    }

    public void finalize()
    {
        try
        {
            close();
        }
        catch (Exception e)
        {
        }
    }

    /**添加的方法**/

    //在判断是否超时前，执行这个方法。把它提出来主要为setTimeOut考虑。
    public void checkTimeOut()
    {
        java.util.Date curr_time = new java.util.Date();
        if ((curr_time.getTime() - m_refresh_time.getTime()) > TIMEOUT)
        {
            m_bln_time_out = true;
        }
    }

    //设置PoolConnection超时，为关闭连接用。
    public void setTimeOut()
    {
        m_bln_time_out = true;
    }

    //判断PoolConnection是否超时
    public boolean isTimeOut()
    {
        return m_bln_time_out;
    }

    //在每次关闭PoolConnection的时候都会刷新这个时间
    private void refreshTime()
    {
        m_refresh_time = new java.util.Date();
    }

	@Override
	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Blob createBlob() throws SQLException {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Clob createClob() throws SQLException {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NClob createNClob() throws SQLException {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getClientInfo(String name) throws SQLException {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isValid(int timeout) throws SQLException {
		
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		
		// TODO Auto-generated method stub
		return null;
	}
}
