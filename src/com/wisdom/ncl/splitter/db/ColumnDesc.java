package com.wisdom.ncl.splitter.db;

import java.io.Serializable;

/**
 * <p>Title: 数据库字段描述对象类 </p>
 * <p>Description: 数据库表的字段描述，包括字段名称、字段类型、字段长度、是否关键字、是否可为空，
   是数据库表描述的基本组成要素。</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Oriental Wisdom</p>
 * @author  Fu Shaoyong
 * @version 1.0
 */

public class ColumnDesc implements Cloneable, Serializable
{
    public String m_table_name;

    public String m_field_name;
    public String m_field_label;
    public String m_field_type;
    public int m_field_length;
    public int m_field_show_len;
    public boolean m_primary_key_or_not;
    public boolean m_field_null_or_not;
    public boolean m_identity_or_not;
    public String m_description;
    public int m_id;

    public ColumnDesc()
    {
    }

    public ColumnDesc(String table_name, String field_name, String field_label, String field_type, int field_length, int field_show_len, boolean primary_key_or_not, boolean field_null_or_not, boolean identity_or_not)
    {
        m_table_name = table_name;
        m_field_name = field_name == null ? "" : field_name;
        m_field_label = field_label == null ? "" : field_label;
        m_field_type = field_type == null ? "varchar" : field_type.toLowerCase();
        m_field_length = field_length;
        m_field_show_len = field_show_len;
        m_field_null_or_not = field_null_or_not;
        m_primary_key_or_not = primary_key_or_not;
        m_identity_or_not = identity_or_not;
    }

    public ColumnDesc(String field_name, String field_label, String field_type, int field_length, int field_show_len, boolean primary_key_or_not, boolean field_null_or_not, boolean identity_or_not)
    {
        m_table_name = "";
        m_field_name = field_name == null ? "" : field_name;
        m_field_label = field_label == null ? "" : field_label;
        m_field_type = field_type == null ? "varchar" : field_type.toLowerCase();
        m_field_length = field_length;
        m_field_show_len = field_show_len;
        m_field_null_or_not = field_null_or_not;
        m_primary_key_or_not = primary_key_or_not;
        m_identity_or_not = identity_or_not;
    }

    public String toCreateTableClause()
    {
        String result = "";
        result += m_field_name + " ";
        if (m_field_type == null || m_field_type.equals("") || m_field_type.toLowerCase().equals("varchar"))
        {
            result += "varchar(" + m_field_length + ")";
        }
        else
        {
            result +=  m_field_type;
        }

        return result;
    }



    /***** JavaBean方法 *****/
    public void setTableName(String table_name)
    {
        m_table_name = table_name;
    }

    public String getTableName()
    {
        return m_table_name;
    }

    public void setFieldName(String field_name)
    {
        m_field_name = field_name;
    }

    public String getFieldName()
    {
        return m_field_name;
    }

    public String getLowerCaseFieldName()
    {
        if (m_field_name == null || m_field_name.length() == 0)
            return m_field_name;
        else
        {
            if (m_field_name.length() == 1)
                return m_field_name.toLowerCase();
            else
                return m_field_name.substring(0, 1).toLowerCase() + m_field_name.substring(1);
        }

    }

    public void setFieldType(String field_type)
    {
        if (field_type != null)
            m_field_type = field_type.toLowerCase();
        else
            m_field_type = field_type;
    }

    public String getFieldType()
    {
        return m_field_type;
    }

    public void setFieldLabel(String field_label)
    {
        m_field_label = field_label;
    }

    public String getFieldLabel()
    {
        return m_field_label;
    }

    public void setFieldLength(int field_length)
    {
        m_field_length = field_length;
    }

    public int getFieldLength()
    {
        return m_field_length;
    }

    public void setFieldShowLen(int field_show_len)
    {
        m_field_show_len = field_show_len;
    }

    public int getFieldShowLen()
    {
        return m_field_show_len;
    }

    public void setFieldNullOrNot(boolean field_null_or_not)
    {
        m_field_null_or_not = field_null_or_not;
    }

    public boolean isFieldNullOrNot()
    {
        return m_field_null_or_not;
    }

    public void setPrimaryKeyOrNot(boolean primary_key_or_not)
    {
        m_primary_key_or_not = primary_key_or_not;
    }

    public boolean isPrimaryKeyOrNot()
    {
        return m_primary_key_or_not;
    }

    public void setIdentityOrNot(boolean identity_or_not)
    {
        m_identity_or_not = identity_or_not;
    }

    public boolean isIdentityOrNot()
    {
        return m_identity_or_not;
    }

    public void setDescription(String description)
    {
        m_description = description;
    }

    public String getDescription()
    {
        return m_description;
    }

    public void setID(int id)
    {
        m_id = id;
    }

    public int getID()
    {
        return m_id;
    }

    public static String toJavaType(String field_type)
    {
        if (field_type == null || field_type.equals(""))
            return "String";

        if (field_type.trim().toLowerCase().indexOf("int") >= 0)
            return "int";

        if (field_type.trim().toLowerCase().equals("float"))
            return "float";

        if (field_type.trim().toLowerCase().equals("double"))
            return "double";

        if (field_type.trim().toLowerCase().equals("number"))
            return "double";

        if (field_type.trim().toLowerCase().equals("numeric"))
            return "double";

        if (field_type.trim().toLowerCase().equals("date"))
            return "String"; //"java.util.Date";

        if (field_type.trim().toLowerCase().equals("time"))
            return "String"; //"java.util.Date";

        if (field_type.trim().toLowerCase().equals("datetime"))
            return "String"; //"java.util.Date";

        if (field_type.trim().toLowerCase().equals("timestamp"))
            return "String"; //"java.util.Date";

        return "String";
    }

    public static String toJavaType2(String field_type)
{
    if (field_type == null || field_type.equals(""))
        return "String";

    if (field_type.trim().toLowerCase().indexOf("int") >= 0)
        return "Int";

    if (field_type.trim().toLowerCase().equals("float"))
        return "Float";

    if (field_type.trim().toLowerCase().equals("double"))
        return "Double";

    if (field_type.trim().toLowerCase().equals("number"))
        return "Double";

    if (field_type.trim().toLowerCase().equals("numeric"))
        return "Double";

    if (field_type.trim().toLowerCase().equals("date"))
        return "String"; //"Date";

    if (field_type.trim().toLowerCase().equals("time"))
        return "String"; //"Date";

    if (field_type.trim().toLowerCase().equals("datetime"))
        return "String"; //"Date";

    if (field_type.trim().toLowerCase().equals("timestamp"))
        return "String"; //"Date";

    return "String";
}


    public static boolean isChar(String field_type)
    {
        if (isDate(field_type))
            return false;

        if (field_type == null || field_type.equals(""))
            return true;

        if (field_type.trim().toLowerCase().indexOf("int") >= 0)
            return false;

        if (field_type.trim().toLowerCase().equals("float"))
            return false;

        if (field_type.trim().toLowerCase().equals("double"))
            return false;

        if (field_type.trim().toLowerCase().equals("number"))
            return false;

        if (field_type.trim().toLowerCase().equals("numeric"))
            return false;

        return true;
    }

    public static boolean isDate(String field_type)
    {
        if (field_type == null)
            return false;

        if (field_type.trim().toLowerCase().indexOf("date") >= 0)
            return true;

        if (field_type.trim().toLowerCase().indexOf("time") >= 0)
            return true;

        return false;
    }

    public static boolean isNumber(String field_type)
    {
        if (field_type == null)
            return false;

        if (field_type.trim().toLowerCase().indexOf("int") >= 0)
            return true;

        if (field_type.trim().toLowerCase().equals("float"))
            return true;

        if (field_type.trim().toLowerCase().equals("double"))
            return true;

        if (field_type.trim().toLowerCase().equals("number"))
            return true;

        if (field_type.trim().toLowerCase().equals("numeric"))
            return true;

        return false;
    }

    public Object clone()
    {
        try
        {
            return (ColumnDesc)super.clone();
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
