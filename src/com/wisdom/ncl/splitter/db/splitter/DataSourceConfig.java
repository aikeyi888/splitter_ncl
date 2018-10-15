package com.wisdom.ncl.splitter.db.splitter;

/**
 * <p>Title: 数据源配置</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: 北京东方般若科技发展有限公司</p>
 *
 * @author ：Jacky Wang
 * @version 1.0
 */
public class DataSourceConfig
{
    private int m_db_type = 0;
    private String m_db_url =
            "jdbc:microsoft:sqlserver:localhost:1433;DatabaseName=bxt_sms3"; //"jdbc:oracle:thin:@127.0.0.1:1521:wxl"; //"jdbc:microsoft:sqlserver:localhost:1433;DatabaseName=CXT";//"jdbc:mysql://localhost/sms";// "jdbc:odbc:monitor"; //"jdbc:mysql://localhost/monitor";
    private String m_db_server_ip = "127.0.0.1";
    private String m_db_port = "1521";
    private String m_db_init_catalog = "wxl";
    private String m_db_user_name = "sms"; //"sa";
    private String m_db_password = "shihua";
    private String m_db_driver = "com.microsoft.jdbc.sqlserver.SQLServerDriver"; //"oracle.jdbc.driver.OracleDriver"; //"com.microsoft.jdbc.sqlserver.SQLServerDriver"; //"com.mysql.jdbc.Driver";//"sun.jdbc.odbc.JdbcOdbcDriver"; //"org.gjt.mm.mysql.Driver";
    private int m_db_count = 5;

    DataSourceConfig()
    {
    }

    public int getDBType()
    {
        return m_db_type;
    }

    public void setDBType(int db_type)
    {
        m_db_type = db_type;
    }

    public String getDriver()
    {
        switch (m_db_type)
        {
        case 0:
            m_db_driver = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
            break;
        case 1:
            m_db_driver = "oracle.jdbc.driver.OracleDriver";
            break;
        case 2:
            m_db_driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            break;
        default:
            break;
        }
        return m_db_driver;
    }

    public void setDriver(String driver)
    {
        m_db_driver = driver;
    }

    public String getUrl()
    {
        switch (m_db_type)
        {
        case 0:
            m_db_url = "jdbc:microsoft:sqlserver://" + m_db_server_ip +
                       ":" + m_db_port + ";DatabaseName=" +
                       m_db_init_catalog+ ";SelectMethod=Cursor";
            break;

        case 1:
            if (m_db_server_ip.indexOf(",") > 0)
            {
                String[] server_ip_list = m_db_server_ip.split(",");
                StringBuffer sb_url = new StringBuffer();
                sb_url.append("jdbc:oracle:thin:@");
                if (server_ip_list.length > 1)
                {
                    sb_url.append("(DESCRIPTION=");
                    for (int i = 0; i < server_ip_list.length; i++)
                    {
                        sb_url.append("(ADDRESS = (PROTOCOL=TCP)(HOST=");
                        sb_url.append(server_ip_list[i]);
                        sb_url.append(")(PORT=");
                        sb_url.append(m_db_port);
                        sb_url.append("))");
                    }
                    sb_url.append("(LOAD_BALANCE = yes)");
                    sb_url.append("(CONNECT_DATA =");
                    sb_url.append("(SERVER = DEDICATED)(SERVICE_NAME = ");
                    sb_url.append(m_db_init_catalog);
                    sb_url.append(")))");
                    m_db_url = sb_url.toString();
                }
                else
                {
                    m_db_url = "jdbc:oracle:thin:@" + m_db_server_ip
                               + ":" + m_db_port + ":" + m_db_init_catalog;
                }
            }
            else
            {
                m_db_url = "jdbc:oracle:thin:@" + m_db_server_ip
                           + ":" + m_db_port + ":" + m_db_init_catalog;
            }
            break;


        case 2:
            m_db_url = "jdbc:sqlserver://" + m_db_server_ip +
                       ":" + m_db_port + ";DatabaseName=" +
                       m_db_init_catalog+ ";SelectMethod=Cursor";

            break;

        default:
            break;
        }

        return m_db_url;
    }

    public void setUrl(String url)
    {
        m_db_url = url;
    }

    public String getServerIP()
    {
        return m_db_server_ip;
    }

    public void setServerIP(String server_ip)
    {
        m_db_server_ip = server_ip;
    }

    public String getPort()
    {
        return m_db_port;
    }

    public void setPort(String port)
    {
        m_db_port = port;
    }

    public String getDBName()
    {
        return m_db_init_catalog;
    }

    public void setDBName(String db_name)
    {
        m_db_init_catalog = db_name;
    }
    public String getUserName()
    {
        return m_db_user_name;
    }

    public void setUserName(String user_name)
    {
        m_db_user_name = user_name;
    }

    public String getPassword()
    {
        return m_db_password;
    }

    public void setPassword(String password)
    {
        m_db_password = password;
    }

    public int getDBCount()
    {
        return m_db_count;
    }

    public void setDBCount(int db_count)
    {
        m_db_count = db_count;
    }
}
