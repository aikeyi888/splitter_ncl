package com.wisdom.ncl.splitter.db;


import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;

public class DataSource implements Cloneable, Serializable
{
    public String m_name = null; //名称
    public String m_description = null; //描述
    public String m_provider = null; //提供者

    public String m_product_name = null; //--目标数据库产品名称
    public String m_data_source = null; //--IP地址
    public String m_initial_catalog = null; //-数据库名称
    public String m_port = null;//端口
    public String m_user_id = null; //--帐号
    public String m_password = null; //--口令

    public String m_service_name = null; //-服务名称
    public String m_server_name = null; //--服务器名称
    public String m_protocol = null; //--连接协议

/*    public DataSource(String str_name, String str_description, String str_provider
                      , String str_user_id, String str_password, String str_initialcatalog
                      , String str_data_source, String str_product_name
                      , String str_service_name, String str_server_name, String str_protocol)
    {
        this.m_name = str_name;
        this.m_description = str_description;
        this.m_provider = str_provider;
        this.m_user_id = str_user_id;
        this.m_password = str_password;
        this.m_initial_catalog = str_initialcatalog;
        this.m_data_source = str_data_source;
        this.m_product_name = str_product_name;
        this.m_service_name = str_service_name;
        this.m_server_name = str_server_name;
        this.m_protocol = str_protocol;
    }

    public DataSource(String str_product_name,
                      String str_data_source,
                      String str_initial_catalog,
                      String str_user_id,
                      String str_password,
                      String str_service_name,
                      String str_server_name,
                      String str_protocol)
    {
        this.m_product_name = str_product_name;
        this.m_data_source = str_data_source;
        this.m_initial_catalog = str_initial_catalog;
        this.m_user_id = str_user_id;
        this.m_password = str_password;
        this.m_service_name = str_service_name;
        this.m_server_name = str_server_name;
        this.m_protocol = str_protocol;
    }*/

    public DataSource(String str_name,
                      String str_product_name,
                      String str_data_source,
                      String str_initial_catalog,
                      String str_port,
                      String str_user_id,
                      String str_password)
    {
        this.m_name = str_name;
        this.m_product_name = str_product_name;
        this.m_data_source = str_data_source;
        this.m_initial_catalog = str_initial_catalog;
        this.m_user_id = str_user_id;
        this.m_port = str_port;
        this.m_password = str_password;
    }

    public DataSource(String str_product_name,
                      String str_data_source,
                      String str_initial_catalog,
                      String str_port,
                      String str_user_id,
                      String str_password)
    {
        this.m_product_name = str_product_name;
        this.m_data_source = str_data_source;
        this.m_initial_catalog = str_initial_catalog;
        this.m_port = str_port;
        this.m_user_id = str_user_id;
        this.m_password = str_password;
    }


    protected String getURL()
    {
        if (m_product_name == null || m_data_source == null || m_initial_catalog == null)
            return null;

        String url = null;

        if (m_product_name.toLowerCase().equals("sql server") || m_product_name.toLowerCase().equals("sqlserver"))
        {
            if (m_port == null || "".equals(m_port))
            {
                m_port = "1433";
            }

            url = "jdbc:microsoft:sqlserver://" + m_data_source +
                ":" + m_port + ";DatabaseName=" + m_initial_catalog +
                ";SelectMethod=Cursor";
        }
        else if (m_product_name.toLowerCase().equals("sql server 2005") || m_product_name.toLowerCase().equals("sqlserver 2005"))
        {
            if (m_port == null || "".equals(m_port))
             {
                 m_port = "1433";
             }

             url = "jdbc:sqlserver://" + m_data_source +
                 ":" + m_port + ";DatabaseName=" + m_initial_catalog +
                 ";SelectMethod=Cursor";
        }
        else if (m_product_name.toLowerCase().equals("oracle"))
        {
            if (m_port == null || "".equals(m_port))
            {
                m_port = "1521";
            }

            if (m_data_source.indexOf(",") > 0)
            {
                String[] server_ip_list = m_data_source.split(",");
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
                        sb_url.append(m_port);
                        sb_url.append("))");
                    }
                    sb_url.append("(LOAD_BALANCE = yes)");
                    sb_url.append("(CONNECT_DATA =");
                    sb_url.append("(SERVER = DEDICATED)(SERVICE_NAME = ");
                    sb_url.append(m_initial_catalog);
                    sb_url.append(")))");
                    url = sb_url.toString();
                }
                else
                {
                    url = "jdbc:oracle:thin:@" + m_data_source + ":" + m_port +
                        ":" +
                        m_initial_catalog;
                }
            }
            else
            {
                url = "jdbc:oracle:thin:@" + m_data_source + ":" + m_port +
                    ":" +
                    m_initial_catalog;

            }
        }
        else if (m_product_name.toLowerCase().equals("informix"))
        {
            String server_name = "";
            if (m_server_name == null)
                server_name = "";
            else
                server_name = m_server_name;

            if (m_port == null || "".equals(m_port))
            {
                m_port = "1533";
            }

            url = "jdbc:informix-sqli://" + m_data_source
                + ":" + m_port + "/" + m_initial_catalog + ":INFORMIXSERVER="
                + server_name;
        }
        else if (m_product_name.toLowerCase().equals("txt"))
        {
            url = "jdbc:odbc:Driver={Microsoft Text Driver (*.txt; *.csv)};DBQ=" + (m_initial_catalog == null ? "" : m_initial_catalog);
        }

        return url;
    }

    public Connection getConnection()
    {
        Connection con = null;
        String url = getURL();

        if (m_product_name.toLowerCase().equals("sql server") || m_product_name.toLowerCase().equals("sqlserver"))
        {
            try
            {
                Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
                con = DriverManager.getConnection(url, m_user_id, m_password);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else if (m_product_name.toLowerCase().equals("sql server 2005") || m_product_name.toLowerCase().equals("sqlserver 2005"))
        {
            try
            {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                con = DriverManager.getConnection(url, m_user_id, m_password);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else if (m_product_name.toLowerCase().equals("oracle"))
        {
            try
            {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                con = DriverManager.getConnection(url, m_user_id, m_password);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else if (m_product_name.toLowerCase().equals("informix"))
        {
            try
            {
                Class.forName("com.informix.jdbc.IfxDriver");
                con = DriverManager.getConnection(url, m_user_id, m_password);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else if (m_product_name.toLowerCase().equals("txt"))
        {
            try
            {
                Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
                con = DriverManager.getConnection(url, m_user_id, m_password);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }


        return con;
    }

    public boolean createPool(String driver, int num)
    {
        String url = getURL();
        if (url == null)
            return false;

        return ConnectionPool.create(m_name, driver, url, m_user_id, m_password, num);
    }

    public boolean createPool(int num)
    {
        String driver = null;
        if (m_product_name.toLowerCase().equals("sql server") || m_product_name.toLowerCase().equals("sqlserver"))
        {
            driver = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
        }
        else if (m_product_name.toLowerCase().equals("sql server 2005") || m_product_name.toLowerCase().equals("sqlserver 2005"))
        {
            driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        }
        else if (m_product_name.toLowerCase().equals("oracle"))
        {
            driver = "oracle.jdbc.driver.OracleDriver";
        }
        else if (m_product_name.toLowerCase().equals("informix"))
        {
            driver = "com.informix.jdbc.IfxDriver";
        }
        else if (m_product_name.toLowerCase().equals("txt"))
        {
            driver = "sun.jdbc.odbc.JdbcOdbcDriver";
        }

        if (driver == null)
            return false;

        return createPool(driver, num);
    }

    /***** JavaBeans属性 *****/
    public void setName(String name)
    {
        m_name = name;
    }

    public String getName()
    {
        return m_name;
    }

    public void setProductName(String product_name)
    {
        m_product_name = product_name;
    }

    public String getProductName()
    {
        return m_product_name;
    }

    public void setDataSource(String data_source)
    {
        m_data_source = data_source;
    }

    public String getDataSource()
    {
        return m_data_source;
    }

    public void setInitialCatalog(String initial_catalog)
    {
        m_initial_catalog = initial_catalog;
    }

    public String getInitialCatalog()
    {
        return m_initial_catalog;
    }

    public void setUserID(String user_id)
    {
        m_user_id = user_id;
    }

    public String getUserID()
    {
        return m_user_id;
    }

    public void setPassword(String password)
    {
        m_password = password;
    }

    public String getPassword()
    {
        return m_password;
    }

    public Object clone()
    {
        try
        {
            return (DataSource)super.clone();
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
