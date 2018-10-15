package com.wisdom.ncl.splitter.db.sql;


public class SQLDelete extends SQLWhere
{
    public SQLDelete(String table_name)
    {
        super(table_name);
    }

    public String getStatement()
    {
        m_statement = "delete from " + m_table_name;
        if (m_where_clause != null && m_where_clause.length() > 0)
        {
            m_statement += " where ";
            m_statement += m_where_clause;
        }

        return m_statement;
    }
}
