package com.wisdom.ncl.splitter.db.sql;


import java.io.*;

public class SQLBase implements Cloneable, Serializable
{
    String m_table_name = "";
    String m_statement = "";

    protected SQLBase()
    {
    }

    public SQLBase(String table_name)
    {
        if (table_name == null)
            m_table_name = "";
        else
            m_table_name = table_name;
    }

    public void reset()
    {
        m_table_name = "";
        m_statement = "";
    }

    public void setTableName(String table_name)
    {
        m_table_name = table_name;
    }

    public String getTableName()
    {
        return m_table_name;
    }

    public void setStatement(String statement)
    {
        m_statement = statement;
    }

    public String getStatement()
    {
        return m_statement;
    }

    public Object clone()
    {
        try
        {
            return (SQLBase)super.clone();
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
