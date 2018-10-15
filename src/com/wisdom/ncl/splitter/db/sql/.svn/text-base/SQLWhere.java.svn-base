package com.wisdom.ncl.splitter.db.sql;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;

import com.wisdom.ncl.splitter.base.Misc;

public class SQLWhere
    extends SQLBase implements Cloneable, Serializable
{
    public final static String LOGIC_AND = "and";
    public final static String LOGIC_OR = "or";

    public final static String OPERATOR_EQUAL_TO = "=";
    public final static String OPERATOR_LARGER_THAN = ">";
    public final static String OPERATOR_LESS_THAN = "<";
    public final static String OPERATOR_NOT_LARGER_THAN = "<=";
    public final static String OPERATOR_NOT_LESS_THAN = ">=";
    public final static String OPERATOR_NOT_EQUAL_TO = "<>";
    public final static String OPERATOR_LIKE = "like";
    public final static String OPERATOR_NOT_LIKE = "not like";
    public final static String OPERATOR_IN = "in";

    public static final int DBMS_UNKNOWN = -1;
    public static final int DBMS_SQL_SERVER = 0;
    public static final int DBMS_ORACLE = 1;
    public static final int DBMS_INFORMIX = 2;
    public static final int DBMS_MYSQL = 3;
    public static final int DBMS_SYBASE = 4;
    public static final int DBMS_DB2 = 5;

    ArrayList m_where_condition_list = new ArrayList(5);

    String m_where_clause = "";
    String m_order_by_clause = "";

    protected int m_dbms_type = DBMS_UNKNOWN; //后台数据库产品

    public SQLWhere()
    {
    }

    public SQLWhere(String table_name)
    {
        super(table_name);
    }

    public void setDBMSType(int dbms_type)
    {
        m_dbms_type = dbms_type;
    }

    public int getDBMSType()
    {
        return m_dbms_type;
    }

    public String getStatement()
    {
        String temp = m_statement;

        if (m_where_clause != null && m_where_clause.length() > 0)
        {
            temp += " where ";
            temp += m_where_clause;
        }

        if (m_order_by_clause != null && m_order_by_clause.length() > 0)
        {
            temp += " order by " + m_order_by_clause;
        }

        return temp;
    }

    public void reset()
    {
        super.reset();
        m_where_clause = "";
        m_order_by_clause = "";
        m_where_condition_list.clear();
    }

    public void setOrderBy(String order_by_clause)
    {
        m_order_by_clause = order_by_clause;
    }

    public void setWhere(String where_clause)
    {
        if (m_where_clause != null && m_where_clause.length() > 0)
            m_where_clause += " and ";

        if (m_where_clause == null || m_where_clause.length() == 0)
            m_where_clause = where_clause;
        else
            m_where_clause += where_clause;
    }

    public void setWhereOR(String where_clause)
    {
        if (m_where_clause != null && m_where_clause.length() > 0)
            m_where_clause += " or ";

        if (m_where_clause == null || m_where_clause.length() == 0)
            m_where_clause = where_clause;
        else
            m_where_clause += where_clause;
    }

    public boolean setWhere(String logic, String field_name, String operator,
                                String value)
    {
        ConditionLogic condition = new ConditionLogic(logic, field_name, operator,
            value);
        m_where_condition_list.add(condition);

        if (m_where_clause != null && m_where_clause.length() > 0)
            m_where_clause += " " + logic + " ";

        if (value != null)
        {
            if (OPERATOR_LIKE.equals(operator) || OPERATOR_NOT_LIKE.equals(operator))
                m_where_clause += field_name + " " + operator + " '%" + Misc.stringToDBString(value) + "%'";
            else
                m_where_clause += field_name + " " + operator + " '" +
                    Misc.stringToDBString(value) + "'";
        }
        else
        {
            if (OPERATOR_EQUAL_TO.equals(operator))
                m_where_clause += field_name + " is null";
            else if (OPERATOR_NOT_EQUAL_TO.equals(operator))
                m_where_clause += field_name + " is not null";
            else if (OPERATOR_LIKE.equals(operator) || OPERATOR_NOT_LIKE.equals(operator))
                m_where_clause += field_name + " " + operator + " '%'";
            else
                m_where_clause += field_name + " " + operator + " ''";
        }

        return true;
    }

    public String getDateExpr(Connection conn, String value)
    {
        if (value == null)
            return "null";

        if (m_dbms_type == DBMS_UNKNOWN)
        {
            try
            {
                DatabaseMetaData md = conn.getMetaData();
                String product_name = md.getDatabaseProductName().toLowerCase();
                if (product_name.indexOf("sqlserver") >= 0 ||
                    product_name.indexOf("sql server") >= 0)
                    m_dbms_type = DBMS_SQL_SERVER;
                else if (product_name.indexOf("oracle") >= 0)
                    m_dbms_type = DBMS_ORACLE;
                else if (product_name.indexOf("informix") >= 0)
                    m_dbms_type = DBMS_INFORMIX;
                else if (product_name.indexOf("mysql") >= 0)
                    m_dbms_type = DBMS_MYSQL;
                else if (product_name.indexOf("sybase") >= 0)
                    m_dbms_type = DBMS_SYBASE;
                else if (product_name.indexOf("db2") >= 0)
                    m_dbms_type = DBMS_DB2;
                else if (product_name.indexOf("access") >= 0)
                    m_dbms_type = DBMS_SQL_SERVER;
            }
            catch (Exception e)
            {
            }
        }

        switch (m_dbms_type)
        {
        case DBMS_ORACLE:
        {
            String splits[] = value.split("-");
            if (splits != null && splits.length >= 3)
            {
                splits = value.split(":");
                if (splits != null && splits.length >= 3)
                    return "to_date('" + value + "', 'YYYY-MM-DD hh24:mi:ss')";
                else if (splits != null && splits.length == 2)
                    return "to_date('" + value + "', 'YYYY-MM-DD hh24:mi')";
                else
                    return "to_date('" + value + "', 'YYYY-MM-DD')";
            }
            else if (splits != null && splits.length == 2)
            {
                return "to_date('" + value + "', 'YYYY-MM')";
            }
            else
            {
                return "to_date('" + value + "', 'YYYY')";
            }
        }

        default:
            return "'" + value + "'";
        }
    }

    public boolean setWhereDate(Connection conn, String logic, String field_name,
                                String operator,
                                String value)
    {
        ConditionLogic condition = new ConditionLogic(logic, field_name,
            operator,
            value);
        m_where_condition_list.add(condition);

        if (m_where_clause != null && m_where_clause.length() > 0)
            m_where_clause += " " + logic + " ";

        if (value != null)
        {
            if (OPERATOR_LIKE.equals(operator) ||
                OPERATOR_NOT_LIKE.equals(operator))
                m_where_clause += field_name + " " + operator + " '%" +
                    Misc.stringToDBString(value) + "%'";
            else
            {
                if (m_dbms_type == DBMS_ORACLE)
                {
                    m_where_clause += field_name + " " + operator + " " +
                        getDateExpr(conn, Misc.stringToDBString(value));
                }
                else
                {
                    m_where_clause += field_name + " " + operator + " '" +
                        Misc.stringToDBString(value) + "'";
                }
            }
        }
        else
        {
            if (OPERATOR_EQUAL_TO.equals(operator))
                m_where_clause += field_name + " is null";
            else if (OPERATOR_NOT_EQUAL_TO.equals(operator))
                m_where_clause += field_name + " is not null";
            else if (OPERATOR_LIKE.equals(operator) ||
                     OPERATOR_NOT_LIKE.equals(operator))
                m_where_clause += field_name + " " + operator + " '%'";
            else
                m_where_clause += field_name + " " + operator + " ''";
        }

        return true;
    }

    public boolean setWhere(String logic, String field_name, String operator, int value)
    {
        ConditionLogic condition = new ConditionLogic(logic, field_name, operator,
            "" + value);
        m_where_condition_list.add(condition);

        if (m_where_clause != null && m_where_clause.length() > 0)
            m_where_clause += " " + logic + " ";

        if (this.OPERATOR_LIKE.equals(operator))
            m_where_clause += field_name + " = " + value;
        else if (this.OPERATOR_NOT_LIKE.equals(operator))
            m_where_clause += field_name + " <> " + value;
        else
            m_where_clause += field_name + " " + operator + " " + value;

        return true;
    }

    public boolean setWhere(String logic, String field_name, String operator, long value)
    {
        ConditionLogic condition = new ConditionLogic(field_name, operator,
            "" + value, logic);
        m_where_condition_list.add(condition);

        if (m_where_clause != null && m_where_clause.length() > 0)
            m_where_clause += " " + logic + " ";

        if (this.OPERATOR_LIKE.equals(operator))
            m_where_clause += field_name + " = " + value;
        else if (this.OPERATOR_NOT_LIKE.equals(operator))
            m_where_clause += field_name + " <> " + value;
        else
            m_where_clause += field_name + " " + operator + " " + value;

        return true;
    }

    public boolean setWhere(String logic, String field_name, String operator, float value)
    {
        ConditionLogic condition = new ConditionLogic(field_name, operator,
            "" + value, logic);
        m_where_condition_list.add(condition);

        if (m_where_clause != null && m_where_clause.length() > 0)
            m_where_clause += " " + logic + " ";

        if (this.OPERATOR_LIKE.equals(operator))
            m_where_clause += field_name + " = " + value;
        else if (this.OPERATOR_NOT_LIKE.equals(operator))
            m_where_clause += field_name + " <> " + value;
        else
            m_where_clause += field_name + " " + operator + " " + value;

        return true;
    }

    public boolean setWhere(String logic, String field_name, String operator,
                                double value)
    {
        ConditionLogic condition = new ConditionLogic(field_name, operator,
            "" + value, logic);
        m_where_condition_list.add(condition);

        if (m_where_clause != null && m_where_clause.length() > 0)
            m_where_clause += " " + logic + " ";

        if (this.OPERATOR_LIKE.equals(operator))
            m_where_clause += field_name + " = " + value;
        else if (this.OPERATOR_NOT_LIKE.equals(operator))
            m_where_clause += field_name + " <> " + value;
        else
            m_where_clause += field_name + " " + operator + " " + value;

        return true;
    }

    public boolean setWhere(String logic, String field_name, String operator,
                                java.util.Date value)
    {
        ConditionLogic condition = new ConditionLogic(field_name, operator,
            Misc.dateToString(value), logic);
        m_where_condition_list.add(condition);

        if (m_where_clause != null && m_where_clause.length() > 0)
            m_where_clause += " " + logic + " ";

        if (this.OPERATOR_LIKE.equals(operator))
            m_where_clause += field_name + " = '" + Misc.dateToString(value) + "'";
        else if (this.OPERATOR_NOT_LIKE.equals(operator))
            m_where_clause += field_name + " <> '" + Misc.dateToString(value) + "'";
        else
            m_where_clause += field_name + " " + operator + " '" + Misc.dateToString(value) + "'";

        return true;
    }

    public boolean setWhereDate(Connection conn, String logic, String field_name, String operator,
                                java.util.Date value)
    {
        ConditionLogic condition = new ConditionLogic(field_name, operator,
            Misc.dateToString(value), logic);
        m_where_condition_list.add(condition);

        if (m_where_clause != null && m_where_clause.length() > 0)
            m_where_clause += " " + logic + " ";

        if (this.OPERATOR_LIKE.equals(operator))
            m_where_clause += field_name + " = " + getDateExpr(conn, Misc.dateToString(value));
        else if (this.OPERATOR_NOT_LIKE.equals(operator))
            m_where_clause += field_name + " <> " + getDateExpr(conn, Misc.dateToString(value));
        else
            m_where_clause += field_name + " " + operator + " " + getDateExpr(conn, Misc.dateToString(value));

        return true;
    }

    /*********** AND语句 ***************/
    public void setWhere(String field_name, String operator, int value)
    {
        setWhere(LOGIC_AND, field_name, operator, value);
    }

    public void setWhere(String field_name, String operator, long value)
    {
        setWhere(LOGIC_AND, field_name, operator, value);
    }

    public void setWhere(String field_name, String operator, float value)
    {
        setWhere(LOGIC_AND, field_name, operator, value);
    }

    public void setWhere(String field_name, String operator, double value)
    {
        setWhere(LOGIC_AND, field_name, operator, value);
    }

    public void setWhere(String field_name, String operator, java.util.Date value)
    {
        setWhere(LOGIC_AND, field_name, operator, value);
    }

    public void setWhereDate(Connection conn, String field_name, String operator, java.util.Date value)
    {
        setWhereDate(conn, LOGIC_AND, field_name, operator, value);
    }

    public void setWhere(String field_name, String operator, String value)
    {
        setWhere(LOGIC_AND, field_name, operator, value);
    }

    public void setWhereDate(Connection conn, String field_name, String operator, String value)
    {
        setWhereDate(conn, LOGIC_AND, field_name, operator, value);
    }

    /************ OR语句 **************/
    public void setWhereOR(String field_name, String operator, int value)
    {
        setWhere(LOGIC_OR, field_name, operator, value);
    }

    public void setWhereOR(String field_name, String operator, long value)
    {
        setWhere(LOGIC_OR, field_name, operator, value);
    }

    public void setWhereOR(String field_name, String operator, float value)
    {
        setWhere(LOGIC_OR, field_name, operator, value);
    }

    public void setWhereOR(String field_name, String operator, double value)
    {
        setWhere(LOGIC_OR, field_name, operator, value);
    }

    public void setWhereOR(String field_name, String operator, java.util.Date value)
    {
        setWhere(LOGIC_OR, field_name, operator, value);
    }

    public void setWhereDateOR(Connection conn, String field_name, String operator, java.util.Date value)
    {
        setWhereDate(conn, LOGIC_OR, field_name, operator, value);
    }


    public void setWhereOR(String field_name, String operator, String value)
    {
        setWhere(LOGIC_OR, field_name, operator, value);
    }

    public void setWhereDateOR(Connection conn, String field_name, String operator, String value)
    {
        setWhereDate(conn, LOGIC_OR, field_name, operator, value);
    }

    /*********** AND = 语句 ***************/
    public void setWhere(String field_name, int value)
    {
        setWhere(LOGIC_AND, field_name, this.OPERATOR_EQUAL_TO, value);
    }

    public void setWhere(String field_name, long value)
    {
        setWhere(LOGIC_AND, field_name, this.OPERATOR_EQUAL_TO, value);
    }

    public void setWhere(String field_name, float value)
    {
        setWhere(LOGIC_AND, field_name, this.OPERATOR_EQUAL_TO, value);
    }

    public void setWhere(String field_name, double value)
    {
        setWhere(LOGIC_AND, field_name, this.OPERATOR_EQUAL_TO, value);
    }

    public void setWhere(String field_name, java.util.Date value)
    {
        setWhere(LOGIC_AND, field_name, this.OPERATOR_EQUAL_TO, value);
    }

    public void setWhereDate(Connection conn, String field_name, java.util.Date value)
    {
        setWhereDate(conn, LOGIC_AND, field_name, this.OPERATOR_EQUAL_TO, value);
    }

    public void setWhere(String field_name, String value)
    {
        setWhere(LOGIC_AND, field_name, this.OPERATOR_EQUAL_TO, value);
    }

    public void setWhereDate(Connection conn, String field_name, String value)
    {
        setWhereDate(conn, LOGIC_AND, field_name, this.OPERATOR_EQUAL_TO, value);
    }

    /************ OR = 语句 **************/
    public void setWhereOR(String field_name, int value)
    {
        setWhere(LOGIC_OR, field_name, this.OPERATOR_EQUAL_TO, value);
    }

    public void setWhereOR(String field_name, long value)
    {
        setWhere(LOGIC_OR, field_name, this.OPERATOR_EQUAL_TO, value);
    }

    public void setWhereOR(String field_name, float value)
    {
        setWhere(LOGIC_OR, field_name, this.OPERATOR_EQUAL_TO, value);
    }

    public void setWhereOR(String field_name, double value)
    {
        setWhere(LOGIC_OR, field_name, this.OPERATOR_EQUAL_TO, value);
    }

    public void setWhereOR(String field_name, java.util.Date value)
    {
        setWhere(LOGIC_OR, field_name, this.OPERATOR_EQUAL_TO, value);
    }

    public void setWhereDateOR(Connection conn, String field_name, java.util.Date value)
    {
        setWhereDate(conn, LOGIC_OR, field_name, this.OPERATOR_EQUAL_TO, value);
    }

    public void setWhereOR(String field_name, String value)
    {
        setWhere(LOGIC_OR, field_name, this.OPERATOR_EQUAL_TO, value);
    }

    public void setWhereDateOR(Connection conn, String field_name, String value)
    {
        setWhereDate(conn, LOGIC_OR, field_name, this.OPERATOR_EQUAL_TO, value);
    }

    public void setWhereClause(String where_clause)
    {
        m_where_clause = where_clause;
    }

    public String getWhereClause()
    {
        return m_where_clause;
    }

    public void setOrderByClause(String order_by_clause)
    {
        m_order_by_clause = order_by_clause;
    }

    public String getOrderByClause()
    {
        return m_order_by_clause;
    }

    public ArrayList getConditionList()
    {
        return m_where_condition_list;
    }

    public Object clone()
    {
        try
        {
            SQLWhere o = (SQLWhere)super.clone();
            if (m_where_condition_list != null)
                o.m_where_condition_list = (ArrayList)m_where_condition_list.clone();

            return o;
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
