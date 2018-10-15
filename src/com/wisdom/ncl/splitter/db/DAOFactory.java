package com.wisdom.ncl.splitter.db;


import java.util.*;
import java.io.*;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DAOFactory implements Serializable
{
    private String m_factory_name = "";
    private DataSource m_data_source;
    Hashtable m_table_list = new Hashtable();

    private static Hashtable m_factory_list = new Hashtable();


    public DAOFactory(String factory_name)
    {
        m_factory_name = factory_name;
    }

    public String getFactoryName()
    {
        return m_factory_name;
    }

    public void setDataSource(DataSource data_source)
    {
        m_data_source = data_source;
    }

    public DataSource getDataSource()
    {
        return m_data_source;
    }

    public void addTable(TableDesc table_desc)
    {
        if (table_desc != null)
            m_table_list.put(table_desc.getTableName(), table_desc);
    }

    public void removeTable(String table_name)
    {
        m_table_list.remove(table_name);
    }

    public TableDesc getTable(String table_name)
    {
        return (TableDesc)m_table_list.get(table_name);
    }

    public int getTableCount()
    {
        return m_table_list.size();
    }

    public Enumeration getTableEnumeration()
    {
        return m_table_list.keys();
    }

    public static boolean addFactory(DAOFactory factory)
    {
        if (factory == null)
            return false;

        m_factory_list.put(factory.getFactoryName(), factory);
        return true;
    }

    public static boolean createFactory(String factory_name)
    {
        if (getFactory(factory_name) == null)
        {
            m_factory_list.put(factory_name, new DAOFactory(factory_name));
            return true;
        }
        else
            return false;
    }

    public static int getFactoryCount()
    {
        return m_factory_list.size();
    }

    public static Enumeration getFactoryEnumeration()
    {
        return m_factory_list.keys();
    }

    public static DAOFactory getFactory(String factory_name)
    {
        if (factory_name == null)
            return null;

        return (DAOFactory)m_factory_list.get(factory_name);
    }
}
