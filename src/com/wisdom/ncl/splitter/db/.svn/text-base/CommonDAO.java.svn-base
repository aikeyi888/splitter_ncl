package com.wisdom.ncl.splitter.db;


import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;

import com.wisdom.ncl.splitter.base.DateUtil;
import com.wisdom.ncl.splitter.base.Misc;


public class CommonDAO
    extends DAO implements Cloneable, Serializable
{
    
    DataSource m_data_source;
    String m_select_sql;
    protected TableDesc m_table_desc;
    boolean m_table_desc_adjusted = false;

    ArrayList m_origin_record_value_list = new ArrayList();
    ArrayList m_record_value_list = new ArrayList();
    ArrayList m_column_status_list = new ArrayList();

    boolean m_selected = false;
    String m_check_box =  "unchecked";//checked,unchecked,disabled

    String m_table_name;

    protected CommonDAO()
    {
    }

    public CommonDAO(CommonDAO dao)
    {
        copyFrom(dao);
    }

    public CommonDAO(DataSource data_source, String select_sql,
                     TableDesc table_desc)
    {
        if (select_sql == null)
            return;

        m_data_source = data_source;
        m_select_sql = select_sql.trim();

        if (!m_select_sql.toLowerCase().startsWith("select "))
        {
            m_select_sql = null;
            return;
        }

        if (table_desc == null)
        {
            setTableDesc(TableDesc.parseFrom(m_select_sql));
        }
        else
        {
            setTableDesc(table_desc);
        }
    }

    public CommonDAO(DataSource data_source, String select_sql)
    {
        this(data_source, select_sql, null);

        if (data_source != null)
        {
            Connection conn = data_source.getConnection();
            Statement stmt = null;
            ResultSet rs = null;
            try
            {
                stmt = conn.createStatement();
                rs = stmt.executeQuery(m_select_sql);
                ResultSetMetaData rsmd = rs.getMetaData();
                adjustTableDesc(m_select_sql, rsmd);
                setTableDesc(m_table_desc);
            }
            catch (Exception e)
            {
            }
            finally
            {
                try
                {
                    if (rs != null)
                        rs.close();
                }
                catch (Exception e2)
                {
                }

                try
                {
                    if (stmt != null)
                        stmt.close();
                }
                catch (Exception e2)
                {
                }
                try
                {
                    if (conn != null)
                        conn.close();
                }
                catch (Exception e2)
                {
                }
            }
        }
    }

    public CommonDAO(String factory_name, TableDesc table_desc)
    {
        DAOFactory factory = DAOFactory.getFactory(factory_name);
        if (factory != null)
        {
            m_data_source = factory.getDataSource();
        }

        if (table_desc != null)
            setTableDesc(table_desc);
    }

    public CommonDAO(String factory_name, String table_name)
    {
        DAOFactory factory = DAOFactory.getFactory(factory_name);
        if (factory != null)
        {
            m_data_source = factory.getDataSource();
            m_table_desc = factory.getTable(table_name);
            if (m_table_desc != null)
            {
                setTableDesc(m_table_desc);
            }
            else
            {
                System.out.println("错误:DAO对象工厂中不存在名为" + table_name + "的对象描述!");
            }
        }
        else
        {
        }
    }

    public CommonDAO(TableDesc table_desc)
    {
        setTableDesc(table_desc);
    }

    public String getDataSourceName()
    {
        if (m_data_source != null)
            return m_data_source.getName();
        else
            return null;
    }

    public void setTableName(String table_name)
    {
        m_table_name = table_name;
    }

    public String getTableName()
    {
        if (m_table_name != null)
            return m_table_name;

        String table_name = "";

        if (m_table_desc != null)
            table_name = m_table_desc.getTableName();

        if (table_name != null && table_name.length() > 0)
            return table_name;

        try
        {
            int pos1 = m_select_sql.toLowerCase().indexOf(" from ");
            int pos2 = m_select_sql.toLowerCase().indexOf(" where ");
            int pos3 = m_select_sql.toLowerCase().indexOf(" order by ");
            int pos4 = m_select_sql.toLowerCase().indexOf(" group by ");
            int pos5 = m_select_sql.toLowerCase().indexOf(" having ");
            if (pos1 >= 0)
            {
                int pos = -1;
                if (pos2 >= 0)
                    pos = pos2;
                else if (pos3 >= 0)
                    pos = pos3;
                else if (pos4 >= 0)
                    pos = pos4;
                else if (pos5 >= 0)
                    pos = pos5;
                if (pos > 0)
                    table_name = m_select_sql.substring(pos1 + 6, pos);
                else
                    table_name = m_select_sql.substring(pos1 + 6);
            }
        }
        catch (Exception e)
        {
        }

        return table_name;
    }

    public TableDesc getTableDesc()
    {
        return m_table_desc;
    }

    /**
     * 拷贝结构
     * @param dao CommonDAO
     */
    public void copyFrom(CommonDAO dao)
    {
        if (dao == null)
            return;

        this.setWhereClause(dao.getWhereClause());
        this.setOrderByClause(dao.getOrderByClause());

        m_table_desc_adjusted = dao.m_table_desc_adjusted;
        m_table_desc = dao.m_table_desc; ;
        m_data_source = dao.m_data_source;
        m_select_sql = dao.m_select_sql;
        m_table_desc = dao.m_table_desc;

        m_origin_record_value_list = new ArrayList();
        m_record_value_list = new ArrayList();
        m_column_status_list = new ArrayList();

        if (m_table_desc != null)
        {
            for (int i = 0; i < m_table_desc.getColumnCount(); i++)
            {
                m_origin_record_value_list.add(null);
                m_record_value_list.add(null);
                m_column_status_list.add(new Integer( -1));
            }
        }
    }

    public boolean copyFrom(ResultSet rs)
    {
        if (rs == null)
            return false;

        try
        {
            ResultSetMetaData rsmd = rs.getMetaData();
            int column_count = rsmd.getColumnCount();
            for (int i = 0; i < column_count; i++)
                setColumn(this, rs, rsmd, i);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public int getCount(Connection conn)
    {
        String sql;
        if (m_select_sql != null && m_select_sql.length() > 0)
        {
            int pos = m_select_sql.toLowerCase().indexOf(" from ");
            if (pos < 0)
            {
                return 0;
            }
            else
            {
                sql = "select count(*)" +
                    extractOrderByFromSQL(m_select_sql.substring(pos));
            }
        }
        else
        {
            if (m_table_desc == null)
                return 0;

            sql = "select count(*) from " + getTableName();
            if (getWhereClause() != null && getWhereClause().length() > 0)
                sql += " where " + getWhereClause();
            //if (getOrderByClause() != null && getOrderByClause().length() > 0)
            //    sql += " order by " + getOrderByClause();
        }

        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            int count = 0;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next())
            {
                count = rs.getInt(1);
            }
            return count;
        }
        catch (Exception e)
        {
            return 0;
        }
        finally
        {
            try
            {
                rs.close();
            }
            catch(Exception e)
            {

            }
            try
            {
                stmt.close();
            }
            catch (Exception e2)
            {
            }
        }
    }

    public ResultSet select(Statement stmt)
    {
        String sql;
        if (m_select_sql != null && m_select_sql.length() > 0)
        {
            sql = addConditionToSQL(m_select_sql, getWhereClause(),
                                    getOrderByClause());
        }
        else
        {
            if (m_table_desc == null)
                return null;

            sql = "select * from " + getTableName();
            if (getWhereClause() != null && getWhereClause().length() > 0)
                sql += " where " + getWhereClause();
            if (getOrderByClause() != null && getOrderByClause().length() > 0)
                sql += " order by " + getOrderByClause();

        }

        try
        {
            ResultSet rs = stmt.executeQuery(sql);
            return rs;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * 根据查询出的结果集调整TableDesc的描述
     * @param rsmd ResultSetMetaData
     */
    private void adjustTableDesc(String select_sql, ResultSetMetaData rsmd)
    {
        if (m_table_desc_adjusted)
            return;

        if (m_table_desc == null)
            m_table_desc = new TableDesc("", "");

        try
        {
            String table_name = "";
            if (rsmd.getTableName(1) != null &&
                rsmd.getTableName(1).length() > 0)
                table_name = rsmd.getTableName(1);
            else
                table_name = getTableName();
            m_table_desc.setTableName(table_name);
            m_table_desc.setTableMemo(table_name);

            int column_count = rsmd.getColumnCount();
            int current_column_count = m_table_desc.getColumnCount();
            if (current_column_count > column_count)
            {
                for (int i = current_column_count - 1; i >= column_count; i--)
                    m_table_desc.removeColumnDesc(i);
            }
            else if (current_column_count < column_count)
            {
                for (int i = current_column_count; i < column_count; i++)
                {
                    ColumnDesc column_desc = new ColumnDesc(rsmd.getColumnName(
                        i + 1),
                        rsmd.getColumnLabel(i + 1),
                        rsmd.getColumnTypeName(i + 1),
                        rsmd.getColumnDisplaySize(i + 1),
                        rsmd.getColumnDisplaySize(i + 1),
                        false,
                        rsmd.isNullable(i + 1) == 0 ? false : true,
                        rsmd.isAutoIncrement(i + 1));
                    m_table_desc.addColumn(column_desc);
                }
            }
            for (int i = 0; i < current_column_count; i++)
            {
                ColumnDesc column_desc = m_table_desc.getColumnDesc(i);
                column_desc.setFieldName(rsmd.getColumnName(i + 1));
                column_desc.setFieldLabel(rsmd.getColumnLabel(i + 1));
                column_desc.setFieldType(rsmd.getColumnTypeName(i + 1));
                column_desc.setFieldLength(rsmd.getColumnDisplaySize(i + 1));
                column_desc.setFieldShowLen(rsmd.getColumnDisplaySize(i + 1));
                column_desc.setFieldNullOrNot(rsmd.isNullable(i + 1) == 0 ? false : true);
                column_desc.setIdentityOrNot(rsmd.isAutoIncrement(i + 1));
            }
        }
        catch (Exception e)
        {
        }

        m_table_desc_adjusted = true;
    }

    public ArrayList select(Connection conn)
    {
        String sql;
        if (m_select_sql != null && m_select_sql.length() > 0)
        {
            sql = addConditionToSQL(m_select_sql, getWhereClause(),
                                    getOrderByClause());
        }
        else
        {
            if (m_table_desc == null)
                return null;

            sql = "select * from " + getTableName();
            if (getWhereClause() != null && getWhereClause().length() > 0)
                sql += " where " + getWhereClause();
            if (getOrderByClause() != null && getOrderByClause().length() > 0)
                sql += " order by " + getOrderByClause();
        }

        ArrayList result = new ArrayList(20);
        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            ResultSetMetaData rsmd = rs.getMetaData();
            if (m_select_sql != null && m_select_sql.length() > 0)
                adjustTableDesc(m_select_sql, rsmd);

            int column_count = rsmd.getColumnCount();
            if (column_count > 0)
            {
                while (rs.next())
                {
                    CommonDAO dao = (CommonDAO)this.clone();

                   /* Class c_dao = this.getClass();
                    Object dao = c_dao.newInstance();
                    Class c_common_dao = Class.forName(
                        "com.wisdom.db.CommonDAO");
                    Class c_params[] = new Class[1];
                    c_params[0] = c_common_dao;
                    java.lang.reflect.Method m = c_dao.getMethod("copyFrom",
                        c_params);
                    Object params[] = new Object[1];
                    params[0] = this;
                    m.invoke(dao, params);*/
                    for (int i = 0; i < column_count; i++)
                        setColumn( (CommonDAO) dao, rs, rsmd, i);
                    result.add(dao);
                }
            }
        }
        catch (Exception e)
        {

        }
        finally
        {
            try
            {
                rs.close();
            }
            catch (Exception e2)
            {
            }

            try
            {
                stmt.close();
            }
            catch (Exception e2)
            {
            }
        }
        return result;
   }

    public ArrayList select(Connection conn, int offset, int length)
    {
        String sql;
        if (m_select_sql != null && m_select_sql.length() > 0)
        {
            sql = addConditionToSQL(m_select_sql, getWhereClause(),
                                    getOrderByClause());
        }
        else
        {
            if (m_table_desc == null)
                return null;

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
                sql = "select * from (select * from " +
                      getTableName();
                if (getWhereClause() != null &&
                    getWhereClause().length() > 0)
                    sql += " where  " + getWhereClause();
                if (getOrderByClause() != null &&
                    getOrderByClause().length() > 0)
                    sql += " order by " + getOrderByClause();
				sql += ") where rownum<=" + (offset + length);
            }
            break;

            case DBMS_INFORMIX:
            {
                sql = "select first " + (offset + length) + " * from " +
                      getTableName();
                if (getWhereClause() != null &&
                    getWhereClause().length() > 0)
                    sql += " where " + getWhereClause();
                if (getOrderByClause() != null &&
                    getOrderByClause().length() > 0)
                    sql += " order by " + getOrderByClause();

            }
            break;

            case DBMS_MYSQL:
            {
                sql = "select * from " +
                      getTableName();
                sql += " limit " + (offset + length);
                if (getWhereClause() != null &&
                    getWhereClause().length() > 0)
                    sql += " where " + getWhereClause();
                if (getOrderByClause() != null &&
                    getOrderByClause().length() > 0)
                    sql += " order by " + getOrderByClause();
            }
            break;

            case DBMS_SYBASE:
            {
                sql = "set rowcount " + (offset + length)  + " select * from " +
                      getTableName();
                if (getWhereClause() != null &&
                    getWhereClause().length() > 0)
                    sql += " where " + getWhereClause();
                if (getOrderByClause() != null &&
                    getOrderByClause().length() > 0)
                    sql += " order by " + getOrderByClause();
            }
            break;

            case DBMS_DB2:
            {
                sql = "select top " + (offset + length) + " * from " +
                      getTableName();
                if (getWhereClause() != null &&
                    getWhereClause().length() > 0)
                    sql += " where " + getWhereClause();
                if (getOrderByClause() != null &&
                    getOrderByClause().length() > 0)
                    sql += " order by " + getOrderByClause();
            }
            break;

            default:
            {
                sql = "select top " + (offset + length) + " * from " +
                      getTableName();
                if (getWhereClause() != null &&
                    getWhereClause().length() > 0)
                    sql += " where " + getWhereClause();
                if (getOrderByClause() != null &&
                    getOrderByClause().length() > 0)
                    sql += " order by " + getOrderByClause();
            }
            break;
            }
        }

        ArrayList result = new ArrayList(20);
        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            ResultSetMetaData rsmd = rs.getMetaData();
            if (m_select_sql != null && m_select_sql.length() > 0)
                adjustTableDesc(m_select_sql, rsmd);

            for (int i = 0; i < offset; i++)
            {
                if (!rs.next())
                {
                    return result;
                }
            }

            int column_count = rsmd.getColumnCount();
            if (column_count > 0)
            {
                for (int i = 0; i < length; i++)
                {
                    if (rs.next())
                    {
                        CommonDAO dao = (CommonDAO)this.clone();
                       /* Class c_dao = this.getClass();
                        Object dao = c_dao.newInstance();
                        Class c_common_dao = Class.forName(
                            "com.wisdom.db.CommonDAO");
                        Class c_params[] = new Class[1];
                        c_params[0] = c_common_dao;
                        java.lang.reflect.Method m = c_dao.getMethod(
                            "copyFrom", c_params);
                        Object params[] = new Object[1];
                        params[0] = this;
                        m.invoke(dao, params);

                        //CommonDAO dao = new CommonDAO(m_table_desc);*/
                        for (int j = 0; j < column_count; j++)
                            setColumn( (CommonDAO) dao, rs, rsmd, j);
                        result.add(dao);
                    }
                    else
                    {
                        return result;
                    }
                }
            }
        }
        catch (Exception e)
        {

        }
        finally
        {
            try
            {
                rs.close();
            }
            catch(Exception e)
            {

            }
            try
            {
                stmt.close();
            }
            catch (Exception e2)
            {
            }
        }
        return result;
    }

    public CommonDAO findByKeyword(Connection conn, ArrayList keyword)
    {
        if (m_table_desc == null)
            return null;

        if (keyword == null)
            return null;

        int primary_key_index[] = m_table_desc.getPrimaryKeyIndex();
        if (primary_key_index == null || primary_key_index.length == 0)
        {
            if (keyword.size() > m_table_desc.getColumnCount())
                primary_key_index = new int[m_table_desc.getColumnCount()];
            else
                primary_key_index = new int[keyword.size()];
            for (int i = 0; i< primary_key_index.length; i++)
            {
                primary_key_index[i] = i;
            }
        }

        if (keyword.size() < primary_key_index.length)
       {
            for (int i = keyword.size(); i < primary_key_index.length; i++)
            {
                keyword.add("");
            }
        }

        String where_clause = "";
        for (int i = 0; i < primary_key_index.length; i++)
        {
            String value = (String) keyword.get(i);
            ColumnDesc column = m_table_desc.getColumnDesc(primary_key_index[
                i]);
            where_clause += column.m_field_name + "=";
            if (ColumnDesc.isDate(column.m_field_type))
                where_clause += getDateExpr(conn, value);
            else if (ColumnDesc.isChar(column.m_field_type))
                where_clause += "'" + Misc.stringToDBString(value) + "'";
            else
                where_clause += value;

            if (i != primary_key_index.length - 1)
                where_clause += " and ";
        }

        String sql = "";
        if (m_select_sql != null && m_select_sql.length() > 0)
        {
            sql = addConditionToSQL(m_select_sql, where_clause, null);
        }
        else
        {
            sql = "select * from " + getTableName() +
                " where ";
            sql += where_clause;
        }

        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            CommonDAO dao = (CommonDAO)this.clone();

            /*Class c_dao = this.getClass();
            Object dao = c_dao.newInstance();
            Class c_common_dao = Class.forName("com.wisdom.db.CommonDAO");
            Class c_params[] = new Class[1];
            c_params[0] = c_common_dao;
            java.lang.reflect.Method m = c_dao.getMethod("copyFrom",
                c_params);
            Object params[] = new Object[1];
            params[0] = this;
            m.invoke(dao, params);
            //CommonDAO o = new CommonDAO(m_table_desc);*/

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int column_count = rsmd.getColumnCount();
            if (column_count > 0)
            {
                if (rs.next())
                {
                    for (int i = 0; i < column_count; i++)
                    {
                        setColumn( (CommonDAO) dao, rs, rsmd, i);
                    }
                }
                else
                {
                    dao = null;
                }
            }
            return (CommonDAO) dao;
        }
        catch (Exception e)
        {
        }
        finally
        {
            try
            {
                rs.close();
            }
            catch (Exception e2)
            {
            }
            try
            {
                stmt.close();
            }
            catch (Exception e2)
            {
            }
        }
        return null;
    }

    private void setColumn(CommonDAO o, ResultSet rs, ResultSetMetaData rsmd,
                           int i) throws SQLException
    {
        switch (rsmd.getColumnType(i + 1))
        {
            case Types.BIGINT:
                o.setLong(i, rs.getLong(i + 1));
                break;

            case Types.BIT:
            case Types.BOOLEAN:
            case Types.INTEGER:
            case Types.SMALLINT:
            case Types.TINYINT:
                o.setInt(i, rs.getInt(i + 1));
                break;

            case Types.DECIMAL:
            case Types.NUMERIC:
            case Types.REAL:
            case Types.DOUBLE:
                o.setDouble(i, rs.getDouble(i + 1));
                break;

            case Types.FLOAT:
                o.setFloat(i, rs.getFloat(i + 1));
                break;

            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
                o.setString(i, rs.getString(i + 1));
                break;

            case Types.DATE:
                o.setDate(i, rs.getDate(i + 1));
                break;

            case Types.TIME:
                Time t = rs.getTime(i + 1);
                if (t == null)
                    o.setTime(i, null);
                else
                    o.setTime(i, new java.util.Date(t.getTime()));
                break;

            case Types.TIMESTAMP:
                Timestamp t2 = rs.getTimestamp(i + 1);
                if (t2 == null)
                    o.setTimestamp(i, null);
                else
                    o.setTimestamp(i, new java.util.Date(t2.getTime()));
                break;

            default:
                o.setString(i, rs.getString(i + 1));
                break;
        }
    }

    public boolean deleteByKeyword(Connection conn, ArrayList keyword)
    {
        if (m_table_desc == null)
            return false;

        if (keyword == null)
            return false;

        int primary_key_index[] = m_table_desc.getPrimaryKeyIndex();
        if (primary_key_index == null || primary_key_index.length == 0)
        {
            primary_key_index = new int[keyword.size()];
            for (int i = 0; i < keyword.size(); i++)
                primary_key_index[i] = i;
        }

        if (keyword.size() < primary_key_index.length)
        {
            for (int i = keyword.size(); i < primary_key_index.length; i++)
            {
                keyword.add("");
            }
        }

        String sql = "delete from " + getTableName() + " where ";

        for (int i = 0; i < primary_key_index.length; i++)
        {
            String value = (String) keyword.get(i);
            ColumnDesc column = m_table_desc.getColumnDesc(primary_key_index[
                i]);
            sql += column.m_field_name + "=";
            if (ColumnDesc.isDate(column.m_field_type))
                sql += getDateExpr(conn, value);
            else if (ColumnDesc.isChar(column.m_field_type))
                sql += "'" + Misc.stringToDBString(value) + "'";
            else
                sql += value;

            if (i != primary_key_index.length - 1)
                sql += " and ";
        }

        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            int count = stmt.executeUpdate(sql);
            if (count > 0)
                return true;
            else
                return false;
        }
        catch (Exception e)
        {
            return false;
        }
        finally
        {
            try
           {
               stmt.close();
           }
           catch (Exception e2)
           {
           }
        }
    }

    public int delete(Connection conn)
    {
        if (m_select_sql != null && m_select_sql.length() > 0)
            return 0;

        if (m_table_desc == null)
            return 0;

        String sql = "delete from " + getTableName();
        if (getWhereClause() != null && getWhereClause().length() > 0)
            sql += " where " + getWhereClause();

        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            int count = stmt.executeUpdate(sql);
            return count;
        }
        catch (Exception e)
        {
            return 0;
        }
        finally
        {
            try
            {
                stmt.close();
            }
            catch (Exception e2)
            {
            }
        }
    }

    public static ArrayList parseKeywordString(String s)
    {
        if (s == null)
            return null;

        ArrayList result = new ArrayList();

        int i = s.indexOf("|");
        while (i >= 0)
        {
            if (i == 0)
            {
                result.add("");
                if (s.length() > 1)
                    s = s.substring(1);
                else
                {
                    s = null;
                    break;
                }
            }
          else
        {
               String token = s.substring(0, i);
                int pos = 0;
                pos = token.indexOf("$$");
                while (pos >= 0)
               {
                    if (token.length() == pos + 2)
                        token = token.substring(0, pos) + "|";
                    else
                        token = token.substring(0, pos) + "|" +
                            token.substring(pos + 2);
                    pos = token.indexOf("$$");
                }
                pos = token.indexOf("\\\\");
                while (pos >= 0)
                {
                    if (token.length() == pos + 2)
                        token = token.substring(0, pos) + "\\";
                    else
                        token = token.substring(0, pos) + "\\" +
                            token.substring(pos + 2);
                    pos = token.indexOf("\\\\");
                }
                pos = token.indexOf("\\$");
                while (pos >= 0)
                {
                    if (token.length() == pos + 2)
                        token = token.substring(0, pos) + "$";
                    else
                        token = token.substring(0, pos) + "$" +
                            token.substring(pos + 2);
                    pos = token.indexOf("\\$");
                }
                result.add(token);
                /*token = token.replaceAll("$$", "|");
                                 token = token.replaceAll("\\\\", "\\");
                                 token = token.replaceAll("\\$", "$");
                                 result.add(token);*/

                if (s.length() > i + 1)
                    s = s.substring(i+1);
                else
                {
                    s = null;
                    break;
                }
            }

            i = s.indexOf("|");
        }
        if (s != null)
        {
            String token = s;
            int pos = 0;
            pos = token.indexOf("$$");
            while (pos >= 0)
            {
                if (token.length() == pos + 2)
                    token = token.substring(0, pos) + "|";
                else
                    token = token.substring(0, pos) + "|" +
                        token.substring(pos + 2);
                pos = token.indexOf("$$");
            }
            pos = token.indexOf("\\\\");
            while (pos >= 0)
            {
                if (token.length() == pos + 2)
                    token = token.substring(0, pos) + "\\";
                else
                    token = token.substring(0, pos) + "\\" +
                        token.substring(pos + 2);
                pos = token.indexOf("\\\\");
            }
            pos = token.indexOf("\\$");
            while (pos >= 0)
            {
                if (token.length() == pos + 2)
                    token = token.substring(0, pos) + "$";
                else
                    token = token.substring(0, pos) + "$" +
                        token.substring(pos + 2);
                pos = token.indexOf("\\$");
            }
            result.add(token);
        }

        return result;
    }

    public void setTableDesc(TableDesc table_desc)
    {
        m_table_desc = table_desc;
        m_origin_record_value_list = new ArrayList();
        m_record_value_list = new ArrayList();
        m_column_status_list = new ArrayList();
        if (table_desc != null)
        {
            for (int i = 0; i < m_table_desc.getColumnCount(); i++)
            {
                m_origin_record_value_list.add(null);
                m_record_value_list.add(null);
                m_column_status_list.add(new Integer( -1));
            }
        }
    }

    private void resetColumnStatus()
    {
        if (m_column_status_list != null)
        {
            for (int i = 0; i < m_column_status_list.size(); i++)
            {
                m_column_status_list.set(i, new Integer(-1));
            }
        }
    }

    public void setString(String field_name, String s)
    {
        if (m_table_desc == null)
            return;

        int index = m_table_desc.getColumnIndex(field_name);
        setString(index, s);
    }

    public void setString(int index, String s)
    {
        try
        {
            if (!isSetted(index))
            {
                m_origin_record_value_list.set(index, s);
                m_record_value_list.set(index, s);
                m_column_status_list.set(index, new Integer(0));
            }
            else
            {
                m_record_value_list.set(index, s);
                m_column_status_list.set(index, new Integer(1));
            }
        }
        catch (Exception e)
        {
        }
    }

    public void setInt(String field_name, int i)
    {
        if (m_table_desc == null)
            return;

        int index = m_table_desc.getColumnIndex(field_name);
        setInt(index, i);
    }

    public void setInt(int index, int i)
    {
        try
        {
            if (!isSetted(index))
            {
                m_origin_record_value_list.set(index, new Integer(i));
                m_record_value_list.set(index, new Integer(i));
                m_column_status_list.set(index, new Integer(0));
            }
            else
            {
                m_record_value_list.set(index, new Integer(i));
                m_column_status_list.set(index, new Integer(1));
            }

        }
        catch (Exception e)
        {
        }
    }


    public void setInt(String field_name, Integer i)
    {
        if (m_table_desc == null)
            return;

        int index = m_table_desc.getColumnIndex(field_name);
        setInt(index, i);
    }

    public void setInt(int index, Integer i)
    {
        if (!isSetted(index))
        {
            m_origin_record_value_list.set(index, i);
            m_record_value_list.set(index, i);
            m_column_status_list.set(index, new Integer(0));
        }
        else
        {
            m_record_value_list.set(index, i);
            m_column_status_list.set(index, new Integer(1));
        }
    }

    public void setLong(String field_name, long l)
    {
        if (m_table_desc == null)
            return;

        int index = m_table_desc.getColumnIndex(field_name);
        setLong(index, l);
    }

    public void setLong(int index, long l)
    {
        try
        {
            if (!isSetted(index))
            {
                m_origin_record_value_list.set(index, new Long(l));
                m_record_value_list.set(index, new Long(l));
                m_column_status_list.set(index, new Integer(0));
            }
            else
            {
                m_record_value_list.set(index, new Long(l));
                m_column_status_list.set(index, new Integer(1));
            }
        }
        catch (Exception e)
        {
        }
    }

    public void setLong(String field_name, Long l)
    {
        if (m_table_desc == null)
            return;

        int index = m_table_desc.getColumnIndex(field_name);
        setLong(index, l);
    }

    public void setLong(int index, Long l)
    {
        if (!isSetted(index))
        {
            m_origin_record_value_list.set(index, l);
            m_record_value_list.set(index, l);
            m_column_status_list.set(index, new Integer(0));
        }
        else
        {
            m_record_value_list.set(index, l);
            m_column_status_list.set(index, new Integer(1));
        }
    }

    public void setFloat(String field_name, float f)
    {
        if (m_table_desc == null)
            return;

        int index = m_table_desc.getColumnIndex(field_name);
        setFloat(index, f);
    }

    public void setFloat(int index, float f)
    {
        try
        {
            if (!isSetted(index))
            {
                m_origin_record_value_list.set(index, new Float(f));
                m_record_value_list.set(index, new Float(f));
                m_column_status_list.set(index, new Integer(0));
            }
            else
            {
                m_record_value_list.set(index, new Float(f));
                m_column_status_list.set(index, new Integer(1));
            }
        }
        catch (Exception e)
        {
        }
    }

    public void setFloat(String field_name, Float f)
    {
        if (m_table_desc == null)
            return;

        int index = m_table_desc.getColumnIndex(field_name);
        setFloat(index, f);
    }

    public void setFloat(int index, Float f)
    {
        if (!isSetted(index))
        {
            m_origin_record_value_list.set(index, f);
            m_record_value_list.set(index, f);
            m_column_status_list.set(index, new Integer(0));
        }
        else
        {
            m_record_value_list.set(index, f);
            m_column_status_list.set(index, new Integer(1));
        }
    }

    public void setDouble(String field_name, double d)
    {
        if (m_table_desc == null)
            return;

        int index = m_table_desc.getColumnIndex(field_name);
        setDouble(index, d);
    }

    public void setDouble(int index, double d)
    {
        try
        {
            if (!isSetted(index))
            {
                m_origin_record_value_list.set(index, new Double(d));
                m_record_value_list.set(index, new Double(d));
                m_column_status_list.set(index, new Integer(0));
            }
            else
            {
                m_record_value_list.set(index, new Double(d));
                m_column_status_list.set(index, new Integer(1));
            }
        }
        catch (Exception e)
        {
        }
    }

    public void setDouble(String field_name, Double d)
    {
        if (m_table_desc == null)
            return;

        int index = m_table_desc.getColumnIndex(field_name);
        setDouble(index, d);
    }

    public void setDouble(int index, Double d)
    {
        if (!isSetted(index))
        {
            m_origin_record_value_list.set(index, d);
            m_record_value_list.set(index, d);
            m_column_status_list.set(index, new Integer(0));
        }
        else
        {
            m_record_value_list.set(index, d);
            m_column_status_list.set(index, new Integer(1));
        }
    }

    public void setDate(String field_name, java.util.Date d)
    {
        if (m_table_desc == null)
            return;

        int index = m_table_desc.getColumnIndex(field_name);
        setDate(index, d);
    }

    public void setDate(int index, java.util.Date d)
    {
        try
        {
            if (!isSetted(index))
            {
                m_origin_record_value_list.set(index, d);
                m_record_value_list.set(index, d);
                m_column_status_list.set(index, new Integer(0));
            }
            else
            {
                m_record_value_list.set(index, d);
                m_column_status_list.set(index, new Integer(1));
            }
        }
        catch (Exception e)
        {
        }
    }

    public void setTime(String field_name, java.util.Date t)
    {
        if (m_table_desc == null)
            return;

        int index = m_table_desc.getColumnIndex(field_name);
        setTime(index, t);
    }

    public void setTime(int index, java.util.Date t)
    {
        try
        {
            if (!isSetted(index))
            {
                m_origin_record_value_list.set(index, t);
                m_record_value_list.set(index, t);
                m_column_status_list.set(index, new Integer(0));
            }
            else
            {
                m_record_value_list.set(index, t);
                m_column_status_list.set(index, new Integer(1));
            }
        }
        catch (Exception e)
        {
        }
    }

    public void setTimestamp(String field_name, java.util.Date t)
    {
        if (m_table_desc == null)
            return;

        int index = m_table_desc.getColumnIndex(field_name);
        setTimestamp(index, t);
    }

    public void setTimestamp(int index, java.util.Date t)
    {
        try
        {
            if (!isSetted(index))
            {
                m_origin_record_value_list.set(index, t);
                m_record_value_list.set(index, t);
                m_column_status_list.set(index, new Integer(0));
            }
            else
            {
                m_record_value_list.set(index, t);
                m_column_status_list.set(index, new Integer(1));
            }
        }
        catch (Exception e)
        {
        }
    }

    private boolean isSetted(int index)
    {
        Integer i = (Integer) m_column_status_list.get(index);
        if (i != null && i.intValue() >= 0)
            return true;
        else
            return false;
    }

    private boolean isChanged(int index)
    {
        Integer i = (Integer) m_column_status_list.get(index);
        if (i != null && i.intValue() == 1)
            return true;
        else
            return false;
    }

    public boolean isNull(String field_name)
    {
        if (m_table_desc == null)
            return false;

        int index = m_table_desc.getColumnIndex(field_name);
        return isNull(index);
    }

    public boolean isNull(int index)
    {
        try
        {
            Object o = m_record_value_list.get(index);

            if (o == null)
                return true;
            else
                return false;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    protected String getOriginString(int index)
    {
        try
        {
            Object o = m_origin_record_value_list.get(index);
            if (o == null)
                return null;

            if (o instanceof Double)
            {
                Double d = (Double) o;
                return "" + d.doubleValue();
            }
            else if (o instanceof Float)
            {
                Float f = (Float) o;
                return "" + f.floatValue();
            }
            else if (o instanceof Integer)
            {
                Integer i = (Integer) o;
                return "" + i.intValue();
            }
            else if (o instanceof Long)
            {
                Long l = (Long) o;
                return "" + l.longValue();
            }
            else if (o instanceof String)
            {
                String s = (String) o;
                return s;
            }
            else if (o instanceof java.util.Date)
            {
                java.util.Date d = (java.util.Date) o;
                if (m_table_desc != null)
                {
                    ColumnDesc column_desc = m_table_desc.getColumnDesc(index);
                    if (column_desc != null)
                    {
                        if ("datetime".equalsIgnoreCase(column_desc.getFieldType()))
                        {
                            return Misc.dateToString(d);
                        }
                    }
                }

                return Misc.dateToDBString(d);
            }
        }
        catch (Exception e)
        {
        }

        return null;
    }

    public String getString(String field_name)
    {
        if (m_table_desc == null)
            return null;

        int index = m_table_desc.getColumnIndex(field_name);
        return getString(index);
    }

    public String getString(int index)
    {
        try
        {
            Object o = m_record_value_list.get(index);
            if (o == null)
                return null;

            if (o instanceof Double)
            {
                Double d = (Double) o;
                return "" + d.doubleValue();
           }
       else if (o instanceof Float)
            {
                Float f = (Float) o;
                return "" + f.floatValue();
            }
            else if (o instanceof Integer)
            {
                Integer i = (Integer) o;
                return "" + i.intValue();
            }
            else if (o instanceof Long)
            {
                Long l = (Long) o;
                return "" + l.longValue();
            }
            else if (o instanceof String)
            {
                String s = (String) o;
                return s;
            }
            else if (o instanceof java.util.Date)
            {
                java.util.Date d = (java.util.Date) o;
                if (m_table_desc != null)
                {
                    ColumnDesc column_desc = m_table_desc.getColumnDesc(index);
                    if (column_desc != null)
                    {
                        if ("datetime".equalsIgnoreCase(column_desc.getFieldType()))
                        {
                            return Misc.dateToString(d);
                        }
                    }
                }

                return Misc.dateToDBString(d);
            }
        }
        catch (Exception e)
        {
        }

        return null;
    }

    public int getInt(String field_name)
    {
        if (m_table_desc == null)
            return 0;

        int index = m_table_desc.getColumnIndex(field_name);
        return getInt(index);
    }

    public int getInt(int index)
    {
        try
        {
            Object o = m_record_value_list.get(index);
            if (o == null)
                return 0;

            if (o instanceof Double)
            {
                Double d = (Double) o;
                return (int) d.doubleValue();
            }
            else if (o instanceof Float)
            {
                Float f = (Float) o;
                return (int) f.floatValue();
            }
            else if (o instanceof Integer)
            {
                Integer i = (Integer) o;
                return (int) i.intValue();
            }
            else if (o instanceof Long)
            {
                Long l = (Long) o;
                return (int) l.longValue();
            }
            else if (o instanceof String)
            {
                String s = (String) o;
                return Integer.valueOf(s).intValue();
            }
        }
        catch (Exception e)
        {
        }

        return 0;
    }

    public long getLong(String field_name)
    {
        if (m_table_desc == null)
            return 0;

        int index = m_table_desc.getColumnIndex(field_name);
        return getLong(index);
    }

    public long getLong(int index)
    {
        try
        {
            Object o = m_record_value_list.get(index);
            if (o == null)
                return 0;

            if (o instanceof Double)
            {
                Double d = (Double) o;
                return (long) d.doubleValue();
            }
            else if (o instanceof Float)
            {
                Float f = (Float) o;
                return (long) f.floatValue();
            }
            else if (o instanceof Integer)
            {
                Integer i = (Integer) o;
                return (long) i.intValue();
            }
            else if (o instanceof Long)
            {
                Long l = (Long) o;
                return (long) l.longValue();
            }
            else if (o instanceof String)
            {
                String s = (String) o;
                return Long.valueOf(s).longValue();
            }
        }
        catch (Exception e)
        {
        }

        return 0;
    }

    public float getFloat(String field_name)
    {
        if (m_table_desc == null)
            return 0;

        int index = m_table_desc.getColumnIndex(field_name);
        return getFloat(index);
    }

    public float getFloat(int index)
    {
        try
        {
            Object o = m_record_value_list.get(index);
            if (o == null)
                return 0;

            if (o instanceof Double)
            {
                Double d = (Double) o;
                return (float) d.doubleValue();
            }
            else if (o instanceof Float)
            {
                Float f = (Float) o;
                return f.floatValue();
            }
            else if (o instanceof Integer)
            {
                Integer i = (Integer) o;
                return (float) i.intValue();
            }
            else if (o instanceof Long)
            {
                Long l = (Long) o;
                return (float) l.longValue();
            }
            else if (o instanceof String)
            {
                String s = (String) o;
                return Float.valueOf(s).floatValue();
            }
        }
        catch (Exception e)
        {
        }

        return 0;
    }

    public double getDouble(String field_name)
    {
        if (m_table_desc == null)
            return 0;

        int index = m_table_desc.getColumnIndex(field_name);
        return getDouble(index);
    }

    public double getDouble(int index)
    {
        try
        {
            Object o = m_record_value_list.get(index);
            if (o == null)
                return 0;

            if (o instanceof Double)
            {
                Double d = (Double) o;
                return d.doubleValue();
            }
            else if (o instanceof Float)
            {
                Float f = (Float) o;
                return (double) f.floatValue();
            }
            else if (o instanceof Integer)
            {
                Integer i = (Integer) o;
                return (double) i.intValue();
            }
            else if (o instanceof Long)
            {
                Long l = (Long) o;
                return (double) l.longValue();
            }
            else if (o instanceof String)
            {
                String s = (String) o;
                return Double.valueOf(s).doubleValue();
            }
        }
        catch (Exception e)
        {
        }

        return 0;
    }

    public java.util.Date getDate(String field_name)
    {
        if (m_table_desc == null)
            return null;

        int index = m_table_desc.getColumnIndex(field_name);
        return getDate(index);
    }

    public java.util.Date getDate(int index)
    {
        try
        {
            Object o = m_record_value_list.get(index);
            if (o == null)
                return null;

            if (o instanceof String)
            {
                java.util.Date d = DateUtil.stringToDateWithTime( (String) o);
                return d;
            }
            else if (o instanceof java.util.Date)
            {
                java.util.Date d = (java.util.Date) o;
                return d;
            }
        }
        catch (Exception e)
        {
        }

        return null;
    }

    public java.util.Date getTime(String field_name)
    {
        if (m_table_desc == null)
            return null;

        int index = m_table_desc.getColumnIndex(field_name);
        return getTime(index);
    }

    public java.util.Date getTime(int index)
    {
        try
        {
            Object o = m_record_value_list.get(index);
            if (o == null)
                return null;

            if (o instanceof String)
            {
                java.util.Date d = DateUtil.stringToDateWithTime( (String) o);
                return d;
            }
            else if (o instanceof java.util.Date)
            {
                java.util.Date t = (java.util.Date) o;
                return t;
            }
        }
        catch (Exception e)
        {
        }

        return null;
    }

    public java.util.Date getTimestamp(String field_name)
    {
        if (m_table_desc == null)
            return null;

        int index = m_table_desc.getColumnIndex(field_name);
        return getTimestamp(index);
    }

    public java.util.Date getTimestamp(int index)
    {
        try
        {
            Object o = m_record_value_list.get(index);
            if (o == null)
                return null;

            if (o instanceof String)
            {
                java.util.Date d = DateUtil.stringToDateWithTime( (String) o);
                return d;
            }
            else if (o instanceof java.util.Date)
            {
                java.util.Date t = (java.util.Date) o;
                return t;
            }
        }
        catch (Exception e)
        {
        }

        return null;
    }

    public ArrayList getValueList()
    {
        ArrayList result = new ArrayList();

        for (int i = 0; i < m_record_value_list.size(); i++)
        {
            try
            {
                String s = getString(i);
                result.add(s);
            }
            catch (Exception e)
            {
                result.add("");
            }
        }

        return result;
    }

    private String validateNumber(String s)
    {
        if (s == null || s.length() == 0)
            return "0";

        return s;
    }

    public boolean insertToDB(Connection conn)
    {
        if (m_select_sql != null && m_select_sql.length() > 0)
            return false;

        if (m_table_desc == null)
            return false;

        if (m_record_value_list.size() != m_table_desc.getColumnCount())
            return false;

        String sql = "insert into " + getTableName() + " (";
        for (int i = 0; i < m_table_desc.getColumnCount(); i++)
        {
            ColumnDesc column = m_table_desc.getColumnDesc(i);
            if (!column.isIdentityOrNot())
            {
                sql += column.getFieldName();
                sql += ", ";
            }
        }
        if (sql.endsWith(", "))
            sql = sql.substring(0, sql.length() - 2);

        sql += ") values(";
        for (int i = 0; i < m_record_value_list.size(); i++)
        {
            ColumnDesc column = m_table_desc.getColumnDesc(i);
            if (!column.isIdentityOrNot())
            {
                if (isNull(i) && !column.isPrimaryKeyOrNot() &&
                    column.isFieldNullOrNot())
                {
                    sql += "null";
                }
                else
                {
                    String value = getString(i);
                    if (ColumnDesc.isDate(column.m_field_type))
                        sql += getDateExpr(conn, value);
                    else if (ColumnDesc.isChar(column.m_field_type))
                        sql += "'" + Misc.stringToDBString(value) + "'";
                    else
                        sql += validateNumber(value);
                }
                sql += ", ";
            }
        }
        if (sql.endsWith(", "))
            sql = sql.substring(0, sql.length() - 2);

        sql += ")";

        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            int count = stmt.executeUpdate(sql);
            stmt.close();
            if (count > 0)
                return true;
            else
                return false;
        }
        catch (Exception e)
        {
            //e.printStackTrace();
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

    public boolean updateToDB(Connection conn)
    {
        if (m_table_desc == null)
            return false;

        if (m_record_value_list.size() != m_table_desc.getColumnCount())
            return false;

        int primary_key_index[] = m_table_desc.getPrimaryKeyIndex();
        if (primary_key_index == null || primary_key_index.length == 0)
            return false;

        String sql = "update " + getTableName() + " set ";
        for (int i = 0; i < m_table_desc.getColumnCount(); i++)
        {
            ColumnDesc column = m_table_desc.getColumnDesc(i);
            if (column.isIdentityOrNot())
                continue;

            sql += column.m_field_name + "=";
            if (isNull(i) && !column.isPrimaryKeyOrNot() &&
                column.isFieldNullOrNot())
            {
                sql += "null";
            }
            else
            {
                String value = getString(i);
                if (ColumnDesc.isDate(column.m_field_type))
                    sql += getDateExpr(conn, value);
                else if (ColumnDesc.isChar(column.m_field_type))
                    sql += "'" + Misc.stringToDBString(value) + "'";
                else
                    sql += validateNumber(value);
            }
            sql += ", ";
        }
        if (sql.endsWith(", "))
            sql = sql.substring(0, sql.length() - 2);

        sql += " where ";

        for (int i = 0; i < primary_key_index.length; i++)
        {
            ColumnDesc column = m_table_desc.getColumnDesc(primary_key_index[i]);

            sql += column.m_field_name;
            if (isNull(primary_key_index[i]))
                sql += " is null";
            else
            {

                String value = getOriginString(primary_key_index[i]);
                if (ColumnDesc.isDate(column.m_field_type))
                    sql += "=" + getDateExpr(conn, value);
                else if (ColumnDesc.isChar(column.m_field_type))
                    sql += "='" + Misc.stringToDBString(value) + "'";
                else
                    sql += "=" + validateNumber(value);
            }

            if (i != primary_key_index.length - 1)
                sql += " and ";
        }

        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();

            int count = stmt.executeUpdate(sql);
            stmt.close();
            if (count > 0)
                return true;
            else
                return false;
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

    public boolean deleteFromDB(Connection conn)
    {
        if (m_select_sql != null && m_select_sql.length() > 0)
            return false;

        if (m_table_desc == null)
            return false;

        if (m_record_value_list.size() != m_table_desc.getColumnCount())
            return false;

        int primary_key_index[] = m_table_desc.getPrimaryKeyIndex();
        if (primary_key_index == null || primary_key_index.length == 0)
        {
            primary_key_index = new int[m_record_value_list.size()];
            for (int i = 0; i < m_record_value_list.size(); i++)
                primary_key_index[i] = i;
        }

        String sql = "delete from " + getTableName() + " where ";

        for (int i = 0; i < primary_key_index.length; i++)
        {
            ColumnDesc column = m_table_desc.getColumnDesc(primary_key_index[i]);
            sql += column.m_field_name;
            if (isNull(i))
            {
                sql += " is null";
            }
            else
            {
                String value = getString(primary_key_index[i]);
                if (ColumnDesc.isDate(column.m_field_type))
                    sql += "=" + getDateExpr(conn, value);
                else if (ColumnDesc.isChar(column.m_field_type))
                    sql += "='" + Misc.stringToDBString(value) + "'";
                else
                    sql += "=" + validateNumber(value);
            }

            if (i != primary_key_index.length - 1)
                sql += " and ";
        }

       Statement stmt = null;
     try
       {
            stmt = conn.createStatement();
            int count = stmt.executeUpdate(sql);
            stmt.close();
            if (count > 0)
               return true;
            else
                return false;
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

    public ArrayList getColumnValues(Connection conn, String field_name)
    {
        String sql = "";
        if (m_select_sql != null && m_select_sql.length() > 0)
        {
            int pos = m_select_sql.toLowerCase().indexOf(" from ");
            if (pos < 0)
            {
                return null;
            }
            else
            {
                sql = "select distinct " + field_name +
                    m_select_sql.substring(pos);
                sql = addConditionToSQL(sql, getWhereClause(), null);
            }
        }
        else
        {
            if (m_table_desc == null)
                return null;

            sql = "select distinct " + field_name + " from " +
                getTableName();
            if (getWhereClause() != null && getWhereClause().length() > 0)
                sql += " where " + getWhereClause();
        }

        ArrayList result = new ArrayList();
        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next())
            {
                result.add(rs.getString(1));
            }
            rs.close();
            stmt.close();
            return result;
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
            return null;
        }
    }

    public double getSum(Connection conn, String field_name)
    {
        String sql = "";
        if (m_select_sql != null && m_select_sql.length() > 0)
        {
            int pos = m_select_sql.toLowerCase().indexOf(" from ");
            if (pos < 0)
            {
                return 0;
            }
            else
            {
                sql = "select sum(" + field_name + ")" +
                    m_select_sql.substring(pos);
                sql = addConditionToSQL(sql, getWhereClause(), null);
            }
        }
        else
        {
            if (m_table_desc == null)
                return 0;

            sql = "select sum(" + field_name + ") from " +
                getTableName();
            if (getWhereClause() != null && getWhereClause().length() > 0)
                sql += " where " + getWhereClause();
        }

        double count = 0;
        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next())
            {
                count = rs.getDouble(1);
            }
            rs.close();
            stmt.close();
            return count;
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
            return 0;
        }
    }

    public double getAverage(Connection conn, String field_name)
    {
        String sql = "";
        if (m_select_sql != null && m_select_sql.length() > 0)
        {

            int pos = m_select_sql.toLowerCase().indexOf(" from ");
            if (pos < 0)
            {
                return 0;
            }
            else
            {
                sql = "select average(" + field_name + ")" +
                    m_select_sql.substring(pos);
                sql = addConditionToSQL(sql, getWhereClause(), null);
            }
        }
        else
        {
            if (m_table_desc == null)
                return 0;

            sql = "select average(" + field_name + ") from " +
                getTableName();
            if (getWhereClause() != null && getWhereClause().length() > 0)
                sql += " where " + getWhereClause();
        }

        double count = 0;
        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next())
            {
                count = rs.getDouble(1);
            }
            rs.close();
            stmt.close();
            return count;
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
            return 0;
        }
    }

    public String getMin(Connection conn, String field_name)
    {
        String sql = "";
        if (m_select_sql != null && m_select_sql.length() > 0)
        {
            int pos = m_select_sql.toLowerCase().indexOf(" from ");
            if (pos < 0)
            {
                return null;
            }
            else
            {
                sql = "select min(" + field_name + ")" +
                    m_select_sql.substring(pos);
                sql = addConditionToSQL(sql, getWhereClause(), null);
            }
        }
        else
        {
            if (m_table_desc == null)
                return null;

            sql = "select min(" + field_name + ") from " +
                getTableName();
            if (getWhereClause() != null && getWhereClause().length() > 0)
                sql += " where " + getWhereClause();
        }

        String result = "";
        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next())
            {
                result = rs.getString(1);
            }
            rs.close();
            stmt.close();
            return result;
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
            return null;
        }
    }

    public String getMax(Connection conn, String field_name)
    {
        String sql = "";
        if (m_select_sql != null && m_select_sql.length() > 0)
        {
            int pos = m_select_sql.toLowerCase().indexOf(" from ");
            if (pos < 0)
            {
                return null;
            }
            else
            {
                sql = "select max(" + field_name + ")" +
                    m_select_sql.substring(pos);
                sql = addConditionToSQL(sql, getWhereClause(), null);
            }
        }
        else
        {
            if (m_table_desc == null)
                return null;

            sql = "select max(" + field_name + ") from " +
                getTableName();
            if (getWhereClause() != null && getWhereClause().length() > 0)
                sql += " where " + getWhereClause();
        }

        String result = "";
        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next())
            {
                result = rs.getString(1);
            }
            rs.close();
            stmt.close();
            return result;
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
            return null;
        }
    }

    public String getKeywordString()
    {
        if (m_table_desc == null)
            return "";

        ArrayList values = getValueList();

        if (values == null || values.size() == 0)
         return "";

        if (values.size() != m_table_desc.getColumnCount())
            return "";

        int primary_key_index[] = m_table_desc.getPrimaryKeyIndex();
        if (primary_key_index == null || primary_key_index.length == 0)
        {
            primary_key_index = new int[values.size()];
            for (int i = 0; i < values.size(); i++)
                primary_key_index[i] = i;
        }

        String s = "";
        for (int i = 0; i < primary_key_index.length; i++)
        {
            if (i != 0)
                s += "|";

            String value = (String) values.get(primary_key_index[i]);
            if (value != null)
            {
                StringBuffer sb = new StringBuffer(20);

                for (int j = 0; j < value.length(); j++)
                {
                    char c = value.charAt(j);
                    if (c == '\\')
                    {
                        sb.append("\\\\");
                    }
                    else if (c == '$')
                    {
                        sb.append("\\$");
                    }
                    else if (c == '|')
                    {
                        sb.append("$$");
                    }
                    else
                    {
                        sb.append(c);
                    }
                }
                s += sb.toString();

                /*value.replaceAll("\\", "\\\\");
                                 value.replaceAll("$", "\\$");
                                 value.replaceAll("|", "$$");
                                 s += value;*/
            }
        }

        return s;
    }

    public String toString()
    {
        return getKeywordString();
    }

    private String extractOrderByFromSQL(String select_sql)
    {
        if (select_sql == null || select_sql.length() == 0)
            return "";

        try
        {
            int pos2 = select_sql.toLowerCase().indexOf(" group by ");
            int pos3 = select_sql.toLowerCase().indexOf(" having ");
            int pos4 = select_sql.toLowerCase().indexOf(" order by ");

            int pos = pos4;
            if (pos2 >= pos)
                pos = pos2;
            else if (pos3 >= pos)
                pos = pos3;

            if (pos4 > 0)
            {
                if (pos > pos4)
                {
                    return select_sql.substring(0, pos4) +
                        select_sql.substring(pos);
                }
                else
                {
                    return select_sql.substring(0, pos4);
                }
            }
            else
            {
                return select_sql;
            }
        }
        catch (Exception e)
        {
        }

        return "";
    }

    private String extractSelectFromSQL(String select_sql)
    {
        if (select_sql == null || select_sql.length() == 0)
            return "";

        try
        {
            int pos = select_sql.indexOf(" from ");
            if (pos >= 0)
                return select_sql.substring(pos);
        }
        catch (Exception e)
        {
        }

        return "";
    }

    private String addConditionToSQL(String select_sql, String where_clause,
                                     String order_by_clause)
    {
        try
        {
            int pos1 = select_sql.toLowerCase().indexOf(" where ");
            int pos2 = select_sql.toLowerCase().indexOf(" group by ");
            int pos3 = select_sql.toLowerCase().indexOf(" having ");
            int pos4 = select_sql.toLowerCase().indexOf(" order by ");

            String result = select_sql;
            if (where_clause != null && where_clause.length() > 0)
            {
                int pos = -1;
                if (pos2 >= 0)
                    pos = pos2;
                else if (pos3 >= 0)
                    pos = pos3;
                else if (pos4 >= 0)
                    pos = pos4;

                String temp = "";
                if (pos1 >= 0)
                    temp = " and ";
                else
                    temp = " where ";
                if (pos >= 0)
                    result = result.substring(0, pos) + temp + "(" +
                        where_clause + ")" +
                        result.substring(pos);
                else
                    result = result + temp + where_clause;
            }
            if (order_by_clause != null && order_by_clause.length() > 0)
            {
                int pos = pos4;
                String temp = "";
                if (pos4 >= 0)
                {
                    if (pos2 > pos)
                        pos = pos2;
                    if (pos3 > pos)
                        pos = pos3;

                    if (pos != pos4)
                        result = result.substring(0, pos) + ", " +
                            order_by_clause + result.substring(pos);
                    else
                        result = result + ", " + order_by_clause;
                }
                else
                {
                    result = result + " order by " + order_by_clause;
                }
            }

            return result;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public void setSelected(boolean selected)
    {
        m_selected = selected;
    }

    public boolean getSelected()
    {
        return m_selected;
    }
    public void check()
    {
        m_check_box = "checked";
    }

    public void uncheck()
    {
        m_check_box = "unchecked";
    }

    public void disable()
    {
        m_check_box = "disabled";
    }

    public void setChecked(String checked)
    {
        m_check_box = checked;
    }

    public String getChecked()
    {
        return m_check_box;
    }

    public Object clone()
    {
        try
        {
            CommonDAO o = (CommonDAO)super.clone();
            if (m_origin_record_value_list != null)
                o.m_origin_record_value_list = (ArrayList)m_origin_record_value_list.clone();
            if (m_record_value_list != null)
                o.m_record_value_list = (ArrayList)m_record_value_list.clone();
            if (m_column_status_list != null)
                o.m_column_status_list = (ArrayList)m_column_status_list.clone();
            return o;
        }
        catch (Exception e)
        {
            return null;
        }

    }
}
