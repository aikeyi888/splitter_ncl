package com.wisdom.ncl.splitter.db.sql;


public class SQLSelect extends SQLWhere
{
    String m_group_by_clause = "";

    public void setGroupBy(String group_by_clause)
    {
        m_group_by_clause = group_by_clause;
    }

    public void execute(String statement)
    {
        m_statement = statement;
    }

    public String getStatement()
    {
        String statement = "";
        if (m_statement != null)
            statement = m_statement;

        if (m_where_clause != null && m_where_clause.length() > 0)
        {
            statement += " where " + m_where_clause;
        }

        if (m_order_by_clause != null && m_order_by_clause.length() > 0)
        {
            statement += " order by " + m_order_by_clause;
        }

        if (m_group_by_clause != null && m_group_by_clause.length() > 0)
        {
            statement += " group by " + m_group_by_clause;
        }

        return statement;
    }
}
