package com.wisdom.ncl.splitter.db.sql;

import java.io.*;

public class ConditionLogic implements Cloneable, Serializable
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
    public final static String OPERATOR_NOT_IN = "not in";

    public String m_logic;
    public String m_field_name;
    public String m_operator;
    public String m_value;

    public ConditionLogic(String logic, String field_name, String operator, String value)
    {
        m_field_name = field_name;
        m_operator = operator;
        m_value = value;
        m_logic = logic;
    }

    public ConditionLogic(String field_name, String operator, String value)
    {
        m_logic = LOGIC_AND;
        m_field_name = field_name;
        m_operator = operator;
        m_value = value;
    }

    /***** JavaBean·½·¨ *****/

    public void setFieldName(String field_name)
    {
        m_field_name = field_name;
    }

    public String getFieldName()
    {
        return m_field_name;
    }

    public void setOperator(String operator)
    {
        m_operator = operator;
    }

    public String getOperator()
    {
        return m_operator;
    }

    public void setValue(String value)
    {
        m_value = value;
    }

    public String getValue()
    {
        return m_value;
    }

    public void setLogic(String logic)
    {
        m_logic = logic;
    }

    public String getLogic()
    {
        return m_logic;
    }

    public Object clone()
    {
        try
        {
            return (ConditionLogic)super.clone();
        }
        catch (Exception e)
        {
            return null;
        }
    }

}
