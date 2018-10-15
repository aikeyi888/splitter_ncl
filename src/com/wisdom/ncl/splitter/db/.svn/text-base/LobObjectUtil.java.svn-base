package com.wisdom.ncl.splitter.db;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Properties;

import oracle.jdbc.driver.OracleResultSet;
import oracle.sql.BLOB;
import oracle.sql.CLOB;

public class LobObjectUtil
{
    
    private String[] logic_sign =
            {"="};

    public int updateLobFromString(Connection conn, String sql, String content)
            throws Exception
    {
        int ret = 0;
        if (content != null )
        {
            ret = this.updateLobFromByte(conn, sql, content.getBytes());
        }
        return ret;
    }

    public int updateLobFromByte(Connection conn, String sql, byte[] content)
            throws Exception
    {
        if (conn == null)
        {
            return -1;
        }

        String conn_type = conn.getMetaData().getDatabaseProductName();

        if ("Microsoft SQL Server".equalsIgnoreCase(conn_type))
        {
            PreparedStatement ps = null;
            try
            {
                int ret = -1;
                ByteArrayInputStream bis = new ByteArrayInputStream(content);
                ps = conn.prepareStatement(sql);
                ps.setBinaryStream(1, bis, content.length);
                ret = ps.executeUpdate();
                return ret;
            }
            catch (Exception e)
            {
                throw e;
            }
            finally
            {
                try
                {
                    if (ps != null)
                    {
                        ps.close();
                    }
                }
                catch(Exception e)
                {

                }
            }
        }
        else if ("Oracle".equalsIgnoreCase(conn_type))
        {
            String str_select_sql = this.getSelectSQL(sql);
            int ret = -1;

            if (conn.getAutoCommit())
            {
                throw new Exception(
                        "请在事务中使用LobObjectUtil.updateLobFromString方法！");
            }

            Statement sm = null;
            ResultSet rs = null;
            Statement sm2 = null;
            ResultSet rs2 = null;
            PreparedStatement ps = null;
            Statement update_sm = null;
            BLOB blob = null;
            CLOB clob = null;
            try
            {
                sm = conn.createStatement();
                rs = sm.executeQuery(str_select_sql);
                if (rs.next())
                {
                    ResultSetMetaData meta = rs.getMetaData();
                    int column_count = meta.getColumnCount();
                    if (column_count > 1 || column_count < 0)
                    {
                        throw new Exception("select sql is error");
                    }
                    int column_type = meta.getColumnType(1);
                    String update_str_sql;
                    switch (column_type)
                    {
                        case Types.BLOB:

                            blob = ((OracleResultSet) rs).getBLOB(1);

                            // blob.putBytes(1,
                            // content.toString().getBytes("ISO-8859-1"));
                            if (blob == null)
                            {
                                update_sm = conn.createStatement();
                                update_str_sql = sql.replaceAll("[?]",
                                        "EMPTY_BLOB()");
                                update_sm.execute(update_str_sql);

                                sm2 = conn.createStatement();
                                rs2 = sm2.executeQuery(str_select_sql);
                                if (rs2.next())
                                {
                                    blob = ((OracleResultSet) rs2).getBLOB(1);
                                }
                            }
                            blob.putBytes(1, content);
                            ps = conn.prepareStatement(sql);
                            ps.setBlob(1, blob);
                            ret = ps.executeUpdate();
                            try
                            {
                                blob.binaryStreamValue().close();
                            }
                            catch(Exception e)
                            {
                            }
                            break;
                        case Types.CLOB:

                            clob = ((OracleResultSet) rs).getCLOB(1);
                            if (clob == null)
                            {
                                update_sm = conn.createStatement();
                                update_str_sql = sql.replaceAll("[?]",
                                        "EMPTY_CLOB()");
                                update_sm.execute(update_str_sql);

                                sm2 = conn.createStatement();
                                rs2 = sm2.executeQuery(str_select_sql);
                                if (rs.next())
                                {
                                    clob = ((OracleResultSet) rs2).getCLOB(1);
                                }
                            }
                            clob.putString(1, content.toString());
                            ps = conn.prepareStatement(sql);
                            ps.setClob(1, clob);
                            ret = ps.executeUpdate();
                            break;

                        default:
                            throw new Exception("field type is cancel ");
                    }
                }
                return ret;
            }
            catch (Exception e)
            {
                throw e;
            }
            finally
            {
                try
                {
                    if (ps != null)
                    {
                        ps.close();
                    }
                }
                catch (Exception e)
                {

                }
                try
                {
                	//modify C.k ,判断条件加上blob是否打开
                    if (blob != null) 
                    {
                    	if(blob.isOpen())
                    	{
                    		blob.close();
                    	}
                    	else
                    	{
                    		blob = null;
                    	}
                    }
                }
                catch (Exception e)
                {

                }

                try
                {
                	//modify C.k ,判断条件加上blob是否打开
                    if (clob != null) 
                    {
                    	if(clob.isOpen())
                    	{
                    		clob.close();
                    	}
                    	else
                    	{
                    		clob = null;
                    	}
                    }
                }
                catch (Exception e)
                {

                }

                try
                {
                    if (rs2 != null)
                    {
                        rs2.close();
                    }
                }
                catch (Exception e)
                {

                }

                try
                {
                    if (sm2 != null)
                    {
                        sm2.close();
                    }
                }
                catch (Exception e)
                {

                }

                try
                {
                    if (update_sm != null)
                    {
                        update_sm.close();
                    }
                }
                catch (Exception e)
                {

                }

                try
                {
                    if (rs != null)
                    {
                        rs.close();
                    }
                }
                catch (Exception e)
                {

                }
                try
                {
                    if (sm != null)
                    {
                        sm.close();
                    }
                }
                catch (Exception e)
                {

                }
            }
        }
        return 0;
    }

    public int updateLobFromStringOfFile(Connection conn, String sql,
                                          byte[] content)
            throws Exception
    {
        if (conn == null || content == null || content.length == 0)
        {
            return -1;
        }

        String conn_type = conn.getMetaData().getDatabaseProductName();

        if ("Microsoft SQL Server".equalsIgnoreCase(conn_type))
        {
            PreparedStatement ps = null;
            try
            {
                int ret = -1;
                ByteArrayInputStream bis = new ByteArrayInputStream(content);
                ps = conn.prepareStatement(sql);
                ps.setBinaryStream(1, bis, content.length);
                ret = ps.executeUpdate();
                return ret;
            }
            catch (Exception e)
            {
                throw e;
            }
            finally
            {
                try
                {
                    if (ps != null)
                    {
                        ps.close();
                    }
                }
                catch(Exception e)
                {

                }
            }
        }
        else if ("Oracle".equalsIgnoreCase(conn_type))
        {
            String str_select_sql = this.getSelectSQL(sql);
            int ret = -1;

            if (conn.getAutoCommit())
            {
                throw new Exception(
                        "请在事务中使用LobObjectUtil.updateLobFromStringOfFile方法！");
            }

            Statement sm = null;
            ResultSet rs = null;
            Statement sm2 = null;
            ResultSet rs2 = null;
            PreparedStatement ps = null;
            Statement update_sm = null;
            BLOB blob = null;
            CLOB clob = null;
            try
            {
                sm = conn.createStatement();
                rs = sm.executeQuery(str_select_sql);
                if (rs.next())
                {
                    ResultSetMetaData meta = rs.getMetaData();
                    int column_count = meta.getColumnCount();
                    if (column_count > 1 || column_count < 0)
                    {
                        throw new Exception("select sql is error");
                    }
                    int column_type = meta.getColumnType(1);
                    String update_str_sql;
                    switch (column_type)
                    {
                        case Types.BLOB:

                            blob = ((OracleResultSet) rs).getBLOB(1);
                            if (blob == null)
                            {
                                update_sm = conn.createStatement();
                                update_str_sql = sql.replaceAll("[?]",
                                        "EMPTY_BLOB()");
                                update_sm.execute(update_str_sql);

                                sm2 = conn.createStatement();
                                rs2 = sm2.executeQuery(str_select_sql);
                                if (rs2.next())
                                {
                                    blob = ((OracleResultSet) rs2).getBLOB(1);
                                }
                            }

                            blob.putBytes(1, content);
                            ps = conn.prepareStatement(sql);
                            ps.setBlob(1, blob);
                            ret = ps.executeUpdate();

                            break;

                        case Types.CLOB:

                            clob = ((OracleResultSet) rs).getCLOB(1);
                            if (clob == null)
                            {
                                update_sm = conn.createStatement();
                                update_str_sql = sql.replaceAll("[?]",
                                        "EMPTY_CLOB()");
                                update_sm.execute(update_str_sql);

                                sm2 = conn.createStatement();
                                rs2 = sm2.executeQuery(str_select_sql);
                                if (rs2.next())
                                {
                                    clob = ((OracleResultSet) rs2).getCLOB(1);
                                }
                            }

                            clob.putString(1, content.toString());
                            ps = conn.prepareStatement(sql);
                            ps.setClob(1, clob);
                            ret = ps.executeUpdate();

                            break;

                        default:
                            throw new Exception("field type is cancel ");
                    }
                }
                return ret;
            }
            catch (Exception e)
            {
                throw e;
            }
            finally
            {
                try
               {
                   if (ps != null)
                   {
                       ps.close();
                   }
               }
               catch (Exception e)
               {

               }
               try
               {
                   if (blob != null)
                   {
                       blob.close();
                   }
               }
               catch (Exception e)
               {

               }

               try
               {
                   if (clob != null)
                   {
                       clob.close();
                   }
               }
               catch (Exception e)
               {

               }

               try
               {
                   if (rs2 != null)
                   {
                       rs2.close();
                   }
               }
               catch (Exception e)
               {

               }

               try
               {
                   if (sm2 != null)
                   {
                       sm2.close();
                   }
               }
               catch (Exception e)
               {

               }

               try
               {
                   if (update_sm != null)
                   {
                       update_sm.close();
                   }
               }
               catch (Exception e)
               {

               }

               try
               {
                   if (rs != null)
                   {
                       rs.close();
                   }
               }
               catch (Exception e)
               {

               }
               try
               {
                   if (sm != null)
                   {
                       sm.close();
                   }
               }
               catch (Exception e)
               {

               }

            }
        }
        return 0;
    }

    public int updateLobFromFile(Connection conn, String sql, String path)
            throws Exception
    {
        if (conn == null)
        {
            return -1;
        }
        byte[] b_file_content = null;
        FileInputStream in = null;

        try
        {
            File file = new File(path);
            long file_size = 0;
            if (file.exists())
            {
                file_size = file.length();
            }
            in = new FileInputStream(path);
            b_file_content = new byte[Integer.parseInt(String
                    .valueOf(file_size))];

            int i = 0;
            int index = 0;
            while ((i = in.read()) != -1)
            {
                b_file_content[index++] = (byte) i;
            }
        }
        catch (FileNotFoundException e)
        {
        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
            try
            {
                if (in != null)
                {
                    in.close();
                }
            }
            catch(Exception e)
            {

            }
        }

        return this.updateLobFromStringOfFile(conn, sql, b_file_content);
    }

    private int updateLobFromStream(Connection conn, String sql, InputStream in)
            throws Exception
    {
        if (conn == null)
        {
            return -1;
        }
        StringBuffer str_file_content = null;

        try
        {
            str_file_content = new StringBuffer();

            int i = 0;
            while ((i = in.read()) != -1)
            {
                str_file_content.append((char) i);
            }

            in.close();
        }
        catch (FileNotFoundException e)
        {
            throw e;
        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
            if (in != null)
            {
                in.close();
            }
        }

        // return this.updateLobFromString(conn, sql,
        // str_file_content.toString());
        return -1;
    }

    public boolean selectLobToFile(Connection conn, String sql, String path)
            throws SQLException, IOException
    {
        byte[] b_content = this.selectLobToByte(conn, sql);

        if (b_content == null)
        {
            return false;
        }

        boolean flag = false;
        FileOutputStream os = null;

        try
        {
            os = new FileOutputStream(path);
            os.write(b_content);
            os.close();
            flag = true;
        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
            try
            {
                if (os != null)
                {
                    os.close();
                }
            }
            catch(Exception e)
            {

            }
        }
        return flag;
    }

    public byte[] selectLobToByte(Connection conn, String sql)
            throws SQLException, IOException
    {
        byte[] b_ret = null;
        if (conn == null)
        {
            return null;
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;

        try
        {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next())
            {
                is = rs.getBinaryStream(1);
                if (is == null)
                {
                    return null;
                }
                baos = new ByteArrayOutputStream();
                byte arr[] = new byte[1024];
                int len = 0;
                while ((len = is.read(arr)) > 0)
                {
                    baos.write(arr, 0, len);
                }
                b_ret = baos.toByteArray();
                is.close();
                baos.close();
            }
        }
        catch (Exception e)
        {
        }
        finally
        {
            try
            {
                if (baos != null)
                {
                    baos.close();
                }
            }
            catch(Exception e)
            {

            }

            try
            {
                if (is != null)
                {
                    is.close();
                }
            }
            catch(Exception e)
            {

            }

            try
            {
                if (rs != null)
                {
                    rs.close();
                }
            }
            catch(Exception e)
            {

            }
            try
            {
                if (ps != null)
                {
                    ps.close();
                }
            }
            catch(Exception e)
            {

            }


        }
        return b_ret;
    }

    public String selectLobToString(Connection conn, String sql)
            throws SQLException, IOException
    {
        byte[] b = this.selectLobToByte(conn, sql);
        if( b == null)
        {
            return null;
        }

        Properties p = System.getProperties();
        String str_charset = String.valueOf(p.get("file.encoding"));

        return new String(b, str_charset);
    }

    private String selectLobToString1(Connection conn, String sql)
            throws SQLException, IOException
    {
        String str_ret = null;
        if (conn == null)
        {
            return null;
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        InputStream is = null;
        FileOutputStream os = null;

        try
        {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next())
            {
                is = rs.getBinaryStream(1);
                if (is == null)
                {
                    return null;
                }
                int c = 0;
                StringBuffer sb = new StringBuffer();

                while ((c = is.read()) != -1)
                {
                    sb.append((char) c);
                }

                is.close();

                str_ret = sb.toString();

            }
        }
        catch (SQLException e)
        {
            throw e;
        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
            }
            catch(Exception e)
            {

            }
            try
            {
                if (ps != null)
                {
                    ps.close();
                }
            }
            catch(Exception e)
            {

            }
            try
            {
                if (is != null)
                {
                    is.close();
                }
            }
            catch(Exception e)
            {

            }
            try
            {
                if (os != null)
                {
                    os.close();
                }
            }
            catch(Exception e)
            {

            }

        }
        return str_ret;
    }

    private String getSelectSQL(String sql)
            throws Exception
    {
        if (sql == null || "".equals(sql))
        {
            throw new Exception("sql is null");
        }
        ArrayList str_sql_list = this.splitteSQL(sql);
        Object[] str_arr_word = str_sql_list.toArray();

        //System.out.println(str_arr_word.length);

        if (str_arr_word.length < 6)
        {
            throw new Exception("sql is 不完整");
        }

        int index = 0;
        String str_table_name = "";

        if ("update".equalsIgnoreCase(str_arr_word[index++].toString().trim()))
        {
            str_table_name = str_arr_word[index++].toString().trim()
                             .toLowerCase();
        }
        else
        {
            throw new Exception("sql is update语句");
        }

        String str_field = "";

        if ("set".equalsIgnoreCase(str_arr_word[index++].toString().trim()))
        {
            str_field = str_arr_word[index++].toString();

            if (str_field.indexOf("=") >= 0)
            {
                str_field = str_field.split("=")[0];
            }
            else
            {
                if (str_arr_word[index++].toString().indexOf("?") < 0)
                {
                    index++;
                }
            }
        }
        else
        {
            throw new Exception("sql语句没有找set子句");
        }

        int where_index = 0;

        if (str_arr_word[index++].toString().indexOf("where") >= 0)
        {
            where_index = index - 1;
        }
        else
        {
            throw new Exception("sql语句没有找到where子句");
        }

        String str_sql = "select " + str_field + " from " + str_table_name;

        for (int i = where_index; i < str_arr_word.length; i++)
        {
            str_sql += " " + str_arr_word[i];
        }
        str_sql += " for update";

        return str_sql;
    }

    public static void main(String[] args)
    {
        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }
        catch (java.lang.ClassNotFoundException e)
        {
            System.err.print(e.getMessage());
        }
        String url = "jdbc:oracle:thin:@192.168.1.8:1521:wxl";
        Connection conn = null;
        try
        {
            conn = DriverManager.getConnection(url, "bxt", "bxt");
        }
        catch (SQLException e1)
        {
            e1.printStackTrace();
        }

        LobObjectUtil lob = new LobObjectUtil();
        try
        {
            conn.setAutoCommit(false);
            lob.updateLobFromFile(conn,
                                    "update avs_send_section set picture=? " +
                                    "where sendid = '1'",
                                    "c:\\Sunset.jpg");
            conn.commit();
            conn.setAutoCommit(true);
            lob.selectLobToFile(conn, "select " +
                    "picture from avs_send_section where sendid = '1'", "C:\\2.jpg");
            System.out.println("ok");
        }
        catch (Exception e)
        {
        }

//		Properties p = System.getProperties();
//		System.out.println(p.get("file.encoding"));

        // String str_sql = "update tm_mms_content set content = ? where
        // contentid = '1'";
        // String str_path = "C:\\1.txt";
        // try
        // {
        // conn.setAutoCommit(false);
        // int i = lob.updateLobFromString(conn, str_sql, "中文测试");
        // conn.commit();
        // conn.setAutoCommit(true);
        // //int i = lob.updateLobFromFile(conn, str_sql, str_path);
        // System.out.println(i);
        // // boolean flag = lob.selectLobToFile(conn, "select picture from
        // // avs_send_section where sendid = '67221085310136735617'",
        // // "c:\\2.jpg");
        // String flag = lob
        // .selectLobToString(conn,
        // "select content from tm_mms_content where contentid = '1'");
        // System.out.println(new String(flag.getBytes("ISO-8859-1")));
        // } catch (Exception e)
        // {
        // try
        // {
        // conn.rollback();
        // conn.setAutoCommit(true);
        // } catch (SQLException e1)
        // {
        // }
        // e.printStackTrace();
        // }
        // LobObjectUtil lob = new LobObjectUtil();
        // System.out.println(lob.splitteSQL("update t set a =? where a=1"));
        // try
        // {
        //
        // } catch (SQLException e)
        // {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // } catch (IOException e)
        // {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
    }

    private ArrayList splitteSQL(String str_update_sql)
    {
        ArrayList key = new ArrayList();
        String[] str_key = str_update_sql.split(" ");

        for (int i = 0; i < str_key.length; i++)
        {
            key.addAll(this.splitter(str_key[i]));
        }
        return key;
    }

    private ArrayList splitter(String str)
    {
        ArrayList ret = new ArrayList();
        if (str == null || "".equals(str))
            return ret;

        if (this.logic_sign.length <= 0)
        {
            return ret;
        }

        boolean flag = false;
        for (int i = 0; i < this.logic_sign.length; i++)
        {
            int index = str.indexOf(logic_sign[i]);
            if (index >= 0)
            {
                String[] str_list = str.split(logic_sign[i]);
                if (str_list.length > 1)
                {
                    for (int j = 0; j < str_list.length; j++)
                    {
                        ret.addAll(splitter(str_list[j]));
                        if (j <= str_list.length - 2)
                        {
                            ret.add(logic_sign[i]);
                        }
                        flag = true;
                    }
                }
                else
                {
                    if (str_list.length > 0)
                    {
                        ret.add(str_list[0]);
                        ret.add(logic_sign[i]);
                        flag = true;
                    }
                }
            }
        }
        if (!flag)
        {
            if (str != null && !"".equals(str.trim()))
                ret.add(str);
        }
        return ret;
    }
}
