package com.wisdom.ncl.splitter.db.sql;

import com.wisdom.ncl.splitter.base.Misc;



public class SQLInsert extends SQLWhere
{
    String m_columns = "";
    String m_values = "";

    public SQLInsert(String table_name)
    {
        super(table_name);
    }

    public void setColumn(String field_name, String value)
    {
        if (m_columns != null && m_columns.length() > 0)
            m_columns += ",";
        m_columns += field_name;

        if (m_values != null && m_values.length() > 0)
            m_values += ",";

        if (value == null)
            m_values += "null";
        else
            m_values += "'" + Misc.stringToDBString(value) + "'";
    }

    public void setColumn(String field_name, int value)
    {
        if (m_columns != null && m_columns.length() > 0)
            m_columns += ",";
        m_columns += field_name;

        if (m_values != null && m_values.length() > 0)
            m_values += ",";
        m_values += value;
    }

    public void setColumn(String field_name, long value)
    {
        if (m_columns != null && m_columns.length() > 0)
            m_columns += ",";
        m_columns += field_name;

        if (m_values != null && m_values.length() > 0)
            m_values += ",";
         m_values += value;
    }

    public void setColumn(String field_name, float value)
    {
        if (m_columns != null && m_columns.length() > 0)
            m_columns += ",";
        m_columns += field_name;

        if (m_values != null && m_values.length() > 0)
            m_values += ",";
         m_values += value;
     }

     public void setColumn(String field_name, double value)
     {
         if (m_columns != null && m_columns.length() > 0)
             m_columns += ",";
         m_columns += field_name;

         if (m_values != null && m_values.length() > 0)
             m_values += ",";
          m_values += value;
     }

     public void setColumnNull(String field_name)
     {
         if (m_columns != null && m_columns.length() > 0)
             m_columns += ",";
         m_columns += field_name;

         if (m_values != null && m_values.length() > 0)
             m_values += ",";
          m_values += "null";
     }

    public String getStatement()
    {
        m_statement = "insert into " + m_table_name + "(";
        m_statement += m_columns;
        m_statement += ") values(";
        m_statement += m_values;
        m_statement += ")";

        return m_statement;
    }

    public void reset()
    {
        super.reset();

        m_columns = "";
        m_values = "";
    }
}
