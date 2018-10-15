package com.wisdom.ncl.splitter.db;


import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * <p>Title: 数据库表描述对象 </p>
 * <p>Description: 数据库表描述，让DAO对象了解到所操作的目标数据库表的结构，能够正确地对数据库表进行操作。</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: oriental wisdom</p>
 * @author  majinying
 * @version 1.0
 */
public class TableDesc implements Cloneable, Serializable
{
    private String m_table_name; //表的名称
    private String m_table_memo; //表的说明
    private ArrayList m_column_desc_list = new ArrayList(); //字段描述的集合

    protected TableDesc()
    {
    }

    public TableDesc(String table_name, String table_memo)
    {
        this.m_table_name = table_name;
        this.m_table_memo = table_memo;
    }

   /* public boolean addColumn(String field_name, String field_label, String field_type, int field_length, int field_show_len, boolean field_null_or_not, boolean primary_key_or_not, boolean identity_or_not)
    {
        if (getColumnDesc(field_name) != null)
            return false;

        ColumnDesc desc = new ColumnDesc(m_table_name, field_name, field_label, field_type, field_length, field_show_len, field_null_or_not, primary_key_or_not, identity_or_not);
        m_column_desc_list.add(desc);
        return true;
    }*/

    public boolean addPrimaryColumn(String field_name, String field_label, String field_type, int field_length, int field_show_len)
    {
        if (getColumnDesc(field_name) != null)
            return false;

        ColumnDesc desc = new ColumnDesc(m_table_name, field_name, field_label, field_type, field_length, field_show_len, true, false, false);
        m_column_desc_list.add(desc);
        return true;
    }

    public boolean addPrimaryColumn(String field_name, String field_label, String field_type, int field_length)
    {
        if (getColumnDesc(field_name) != null)
            return false;

        ColumnDesc desc = new ColumnDesc(m_table_name, field_name, field_label, field_type, field_length, 0, true, false, false);
        m_column_desc_list.add(desc);
        return true;
    }

    public boolean addIdentityColumn(String field_name, String field_label, String field_type, int field_length, int field_show_len)
    {
        if (getColumnDesc(field_name) != null)
            return false;

        ColumnDesc desc = new ColumnDesc(m_table_name, field_name, field_label, field_type, field_length, field_show_len, false, false, true);
        m_column_desc_list.add(desc);
        return true;
    }

    public boolean addIdentityColumn(String field_name, String field_label, String field_type, int field_length)
    {
        if (getColumnDesc(field_name) != null)
            return false;

        ColumnDesc desc = new ColumnDesc(m_table_name, field_name, field_label, field_type, field_length, 0, false, false, true);
        m_column_desc_list.add(desc);
        return true;
    }


    public boolean addPrimaryIdentityColumn(String field_name, String field_label, String field_type, int field_length, int field_show_len)
    {
        if (getColumnDesc(field_name) != null)
            return false;

        ColumnDesc desc = new ColumnDesc(m_table_name, field_name, field_label, field_type, field_length, field_show_len, true, false, true);
        m_column_desc_list.add(desc);
        return true;
    }

    public boolean addPrimaryIdentityColumn(String field_name, String field_label, String field_type, int field_length)
    {
        if (getColumnDesc(field_name) != null)
            return false;

        ColumnDesc desc = new ColumnDesc(m_table_name, field_name, field_label, field_type, field_length, 0, true, false, true);
        m_column_desc_list.add(desc);
        return true;
    }

    public boolean addNonNullableColumn(String field_name, String field_label, String field_type, int field_length, int field_show_len)
    {
        if (getColumnDesc(field_name) != null)
            return false;

        ColumnDesc desc = new ColumnDesc(m_table_name, field_name, field_label, field_type, field_length, field_show_len, false, false, false);
        m_column_desc_list.add(desc);
        return true;
    }

    public boolean addNonNullableColumn(String field_name, String field_label, String field_type, int field_length)
    {
        if (getColumnDesc(field_name) != null)
            return false;

        ColumnDesc desc = new ColumnDesc(m_table_name, field_name, field_label, field_type, field_length, 0, false, false, false);
        m_column_desc_list.add(desc);
        return true;
    }

    public boolean addColumn(String field_name, String field_label, String field_type, int field_length, int field_show_len)
    {
        if (getColumnDesc(field_name) != null)
            return false;

        ColumnDesc desc = new ColumnDesc(m_table_name, field_name, field_label, field_type, field_length, field_show_len, false, true, false);
        m_column_desc_list.add(desc);
        return true;
    }

    public boolean addColumn(String field_name, String field_label, String field_type, int field_length)
    {
        if (getColumnDesc(field_name) != null)
            return false;

        ColumnDesc desc = new ColumnDesc(m_table_name, field_name, field_label, field_type, field_length, 0, false, true, false);
        m_column_desc_list.add(desc);
        return true;
    }

    public boolean addColumn(ColumnDesc desc)
    {
        if (desc == null)
            return false;

        if (getColumnDesc(desc.getFieldName()) != null)
            return false;

        desc.setTableName(m_table_name);
        m_column_desc_list.add(desc);
        return true;
    }

    public int getColumnCount()
    {
        return m_column_desc_list.size();
    }

    public ColumnDesc getColumnDesc(int index)
    {
        try
        {
            ColumnDesc desc = (ColumnDesc) m_column_desc_list.get(index);
            return desc;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public ColumnDesc getColumnDesc(String field_name)
    {
        if (field_name == null)
            return null;

        for (int i=0; i<getColumnCount(); i++)
        {
            ColumnDesc desc = getColumnDesc(i);
            if (desc != null && field_name.equalsIgnoreCase(desc.getFieldName()))
            {
                return desc;
            }
        }

        return null;
    }

    public int getColumnIndex(String field_name)
    {
        if (field_name == null)
            return -1;

        for (int i=0; i<getColumnCount(); i++)
        {
            ColumnDesc desc = getColumnDesc(i);
            if (desc != null && field_name.equalsIgnoreCase(desc.getFieldName()))
            {
                return i;
            }
        }

        return -1;
    }

    public boolean removeColumnDesc(int index)
    {
        if (m_column_desc_list.remove(index) != null)
            return true;
        else
            return false;
    }

    public boolean removeColumnDesc(String field_name)
    {
        if (field_name == null)
            return false;

        for (int i = 0; i < getColumnCount(); i++)
        {
            ColumnDesc column_desc = getColumnDesc(i);
            if (column_desc != null && field_name.equalsIgnoreCase(column_desc.m_field_name))
            {
                m_column_desc_list.remove(i);
                return true;
            }
        }
        return false;
    }

    public ArrayList getColumnDescList()
    {
        return m_column_desc_list;
    }

    public int getPrimaryKeyCount()
    {
        int count = 0;
        for (int i = 0; i < getColumnCount(); i++)
        {
            ColumnDesc column_desc = getColumnDesc(i);
            if (column_desc != null && column_desc.isPrimaryKeyOrNot())
                count++;
        }

        return count;
    }

    public ArrayList getPrimaryKeyColumnDescList()
    {
        ArrayList result = new ArrayList(5);
        for (int i = 0; i < getColumnCount(); i++)
        {
            ColumnDesc column_desc = getColumnDesc(i);
            if (column_desc != null && column_desc.isPrimaryKeyOrNot())
                result.add(column_desc);
        }

        return result;
    }

    public int[] getPrimaryKeyIndex()
    {
        int indexes[] = new int[getPrimaryKeyCount()];

        try
        {
            int count = 0;
            for (int i = 0; i < getColumnCount(); i++)
            {
                ColumnDesc column_desc = getColumnDesc(i);
                if (column_desc != null && column_desc.isPrimaryKeyOrNot())
                    indexes[count++] = i;
            }
        }
        catch (Exception e)
        {
            //防止indexes越界
        }

        return indexes;
    }

    public boolean isExistInDB(Connection conn)
    {
        String sql = "select * from " + m_table_name;

        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            rs.close();
            stmt.close();
            return true;
        }
        catch (Exception e)
        {
            try
            {
                stmt.close();
            }
            catch (Exception e2)
            {
            }
            return false;
        }
    }

    public boolean createTableInDB(Connection conn)
    {
        if (isExistInDB(conn))
            return true;

        String primary_key_string = "";

        String sql = "create table " + m_table_name + "(";
        for (int i = 0; i < getColumnCount(); i++)
        {
            ColumnDesc column_desc = getColumnDesc(i);
            sql += column_desc.toCreateTableClause();
            sql += ",";

            if (column_desc.m_primary_key_or_not)
            {
                primary_key_string += column_desc.m_field_name;
                primary_key_string += ",";
            }
        }

        if (primary_key_string.length() > 0)
        {
            primary_key_string = primary_key_string.substring(0, primary_key_string.length() - 1);
            sql += "primary key (" + primary_key_string + ")";
        }
        else
        {
            sql = sql.substring(0, sql.length() - 1);
        }

        sql += ")";

        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            return true;
        }
        catch (Exception e)
        {
            try
            {
                stmt.close();
            }
            catch (Exception e2)
            {
            }
            return false;
        }
    }

    public boolean dropTableFromDB(Connection conn)
    {
        String sql = "drop table " + m_table_name;

        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            return true;
        }
        catch (Exception e)
        {
            try
            {
                stmt.close();
            }
            catch (Exception e2)
            {
            }
            return false;
        }
    }

    public static TableDesc parseFrom(String sql)
    {
        if (sql == null)
            return null;

        sql = sql.trim();
        if (!sql.toLowerCase().startsWith("select "))
            return null;

        int pos1, pos2;
        pos1 = 7;
        pos2 = sql.toLowerCase().indexOf(" from ");
        if (pos2 < 0)
            return null;

        String table_name;
        int pos3;
        pos3 = sql.toLowerCase().indexOf(" where ", pos2 + 6);
        if (pos3 > 0)
            table_name = sql.substring(pos2 + 6, pos3);
        else
            table_name = "";

        TableDesc table_desc = new TableDesc(table_name, table_name);
        String columns = sql.substring(pos1, pos2);
        StringTokenizer tokenizer = new StringTokenizer(",");
        while (tokenizer.hasMoreTokens())
        {
            String column_name;
            String column = (String)tokenizer.nextToken();
            int pos = column.toLowerCase().indexOf(" as ");
            if (pos > 0)
            {
                column_name = column.substring(pos + 4);
            }
            else
            {
                column_name = column;
            }
            table_desc.addColumn(column_name, column_name, "varchar", 100);
        }

        return table_desc;
    }


    /******* JavaBean属性 *********/

    public void setTableName(String table_name)
    {
        this.m_table_name = table_name;
        for (int i=0; i<m_column_desc_list.size(); i++)
        {
            ColumnDesc desc = getColumnDesc(i);
            if (desc != null)
                desc.setTableName(table_name);
        }
    }

    public String getTableName()
    {
        return m_table_name;
    }

    public void setTableMemo(String table_memo)
    {
        this.m_table_memo = table_memo;
    }

    public String getTableMemo()
    {
        return m_table_memo;
    }

    public Object clone()
    {
        try
        {
            TableDesc o = (TableDesc)super.clone();
            if (m_column_desc_list != null)
            {
                o.m_column_desc_list = new ArrayList();
                for (int i = 0; i < m_column_desc_list.size(); i++)
                {
                    ColumnDesc column_desc = (ColumnDesc) m_column_desc_list.
                                               get(i);
                    if (column_desc != null)
                        o.m_column_desc_list.add(column_desc.clone());
                    else
                        o.m_column_desc_list.add(null);
                }
            }

            return o;
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
