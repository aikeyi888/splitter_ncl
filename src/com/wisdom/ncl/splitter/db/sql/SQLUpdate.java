package com.wisdom.ncl.splitter.db.sql;

import com.wisdom.ncl.splitter.base.Misc;

public class SQLUpdate extends SQLWhere
{
    String m_columns = "";

    public SQLUpdate(String table_name)
    {
        super(table_name);
    }

    public void setColumn(String field_name, String value)
    {
        if (m_columns != null && m_columns.length() > 0)
            m_columns += ",";
        m_columns += field_name + "=";

        if (value == null)
            m_columns += "null";
        else
            m_columns += "'" + Misc.stringToDBString(value) + "'";
    }

    public void setColumn(String field_name, int value)
    {
        if (m_columns != null && m_columns.length() > 0)
            m_columns += ",";
        m_columns += field_name + "=" + value;
    }

    public void setColumn(String field_name, long value)
    {
        if (m_columns != null && m_columns.length() > 0)
            m_columns += ",";
        m_columns += field_name + "=" + value;
    }

    public void setColumn(String field_name, float value)
    {
        if (m_columns != null && m_columns.length() > 0)
            m_columns += ",";
        m_columns += field_name + "=" + value;
     }

     public void setColumn(String field_name, double value)
     {
        if (m_columns != null && m_columns.length() > 0)
            m_columns += ",";
        m_columns += field_name + "=" + value;
     }

     public void setColumnNull(String field_name)
     {
        if (m_columns != null && m_columns.length() > 0)
            m_columns += ",";
        m_columns += field_name + "=null";
     }

    public String getStatement()
    {
        m_statement = "update " + m_table_name + " set ";
        m_statement += m_columns;
        if (m_where_clause != null && m_where_clause.length() > 0)
        {
            m_statement += " where ";
            m_statement += m_where_clause;
        }
        return m_statement;
    }

    public void reset()
    {
        super.reset();
        m_columns = "";
    }
}
