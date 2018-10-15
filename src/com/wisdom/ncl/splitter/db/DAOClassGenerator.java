package com.wisdom.ncl.splitter.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Enumeration;

public class DAOClassGenerator
{
    public DAOClassGenerator()
    {
    }

    public boolean generateAll(String dir_path, String package_name)
    {
        Enumeration enums = DAOFactory.getFactoryEnumeration();
        while (enums.hasMoreElements())
        {
            String factory_name = (String) enums.nextElement();
            DAOFactory factory = DAOFactory.getFactory(factory_name);
            if (factory == null)
                continue;

            String package_path = package_name.replace('.', '\\');
            Enumeration enum2 = factory.getTableEnumeration();
            while (enum2.hasMoreElements())
            {
                String table_name = (String) enum2.nextElement();
                generate(dir_path, package_name, factory_name, table_name);
            }
        }

        return true;
    }

    public boolean generate(String dir_path, String package_name,
                            String factory_name, String table_name)
    {
        DAOFactory factory = DAOFactory.getFactory(factory_name);
        if (factory == null)
            return false;

        package_name += "." + factory.getFactoryName().toLowerCase();
        String package_path = package_name.replace('.', '\\');

        TableDesc table_desc = factory.getTable(table_name);
        if (table_desc == null)
            return false;

        try
        {
            File dir = new File(dir_path + "\\" + package_path);
            if (!dir.exists())
                dir.mkdirs();

            FileOutputStream fos = new FileOutputStream(dir_path + "\\" +
                package_path + "\\" + table_name + ".java");
            java.io.PrintStream ps = new PrintStream(fos);
            ps.println("package " + package_name + ";");
            ps.println();
            ps.println("import java.sql.*;");
            ps.println("import java.util.*;");
            ps.println("import com.wisdom.db.*;");
            ps.println();
            ps.println("public class " + table_name +
                       " extends CommonDAO");
            ps.println("{");
            ps.println("    public " + table_name + "()");
            ps.println("    {");
            ps.println("        super(\""+ factory_name + "\", \"" + table_name + "\");");
            ps.println("    }");
            ps.println();
            if (existNonIdentityPrimaryKeys(table_desc))
            {
                ps.print("    public " + table_name + "(");
                String param_list = "";
                for (int i = 0; i < table_desc.getColumnCount(); i++)
                {
                    ColumnDesc column_desc = table_desc.getColumnDesc(i);
                    if (!column_desc.isIdentityOrNot() && column_desc.isPrimaryKeyOrNot())
                        param_list +=
                            ColumnDesc.toJavaType(column_desc.getFieldType()) +
                            " " + toIdentifier(column_desc.getFieldName()) + ", ";
                }
                if (param_list.length() > 0)
                    param_list = param_list.substring(0,
                        param_list.length() - 2);
                ps.println(param_list + ")");
                ps.println("    {");
                ps.println("        this();");
                for (int i = 0; i < table_desc.getColumnCount(); i++)
                {
                    ColumnDesc column_desc = table_desc.getColumnDesc(i);
                    if (!column_desc.isIdentityOrNot() && column_desc.isPrimaryKeyOrNot())
                    {
                        ps.println("        set" +
                                   capitalizeFirst(column_desc.getFieldName()) +
                                   "(" + toIdentifier(column_desc.getFieldName()) + ");");
                    }
                }
                ps.println("    }");
                ps.println();
            }
            if (existNonIdentityNonNullableKeys(table_desc))
            {
                ps.print("    public " + table_name + "(");
                String param_list = "";
                for (int i = 0; i < table_desc.getColumnCount(); i++)
                {
                    ColumnDesc column_desc = table_desc.getColumnDesc(i);
                    if (!column_desc.isIdentityOrNot() && (column_desc.isPrimaryKeyOrNot() || !column_desc.isFieldNullOrNot()))
                        param_list +=
                            ColumnDesc.toJavaType(column_desc.getFieldType()) +
                            " " + toIdentifier(column_desc.getFieldName()) + ", ";
                }
                if (param_list.length() > 0)
                    param_list = param_list.substring(0,
                        param_list.length() - 2);
                ps.println(param_list + ")");
                ps.println("    {");
                ps.println("        this();");
                for (int i = 0; i < table_desc.getColumnCount(); i++)
                {
                    ColumnDesc column_desc = table_desc.getColumnDesc(i);
                    if (!column_desc.isIdentityOrNot() && (column_desc.isPrimaryKeyOrNot() || !column_desc.isFieldNullOrNot()))
                    {
                        ps.println("        set" +
                                   capitalizeFirst(column_desc.getFieldName()) +
                                   "(" + toIdentifier(column_desc.getFieldName()) + ");");
                    }
                }
                ps.println("    }");
                ps.println();
            }

            if (existNonIdentityNullableKeys(table_desc))
            {
                ps.print("    public " + table_name + "(");
                String param_list = "";
                for (int i = 0; i < table_desc.getColumnCount(); i++)
                {
                    ColumnDesc column_desc = table_desc.getColumnDesc(i);
                    if (!column_desc.isIdentityOrNot())
                        param_list +=
                            ColumnDesc.toJavaType(column_desc.getFieldType()) +
                            " " + toIdentifier(column_desc.getFieldName()) + ", ";
                }
                if (param_list.length() > 0)
                    param_list = param_list.substring(0,
                        param_list.length() - 2);
                ps.println(param_list + ")");
                ps.println("    {");
                ps.println("        this();");
                for (int i = 0; i < table_desc.getColumnCount(); i++)
                {
                    ColumnDesc column_desc = table_desc.getColumnDesc(i);
                    if (!column_desc.isIdentityOrNot())
                    {
                        ps.println("        set" +
                                   capitalizeFirst(column_desc.getFieldName()) +
                                   "(" + toIdentifier(column_desc.getFieldName()) + ");");
                    }
                }
                ps.println("    }");
                ps.println();
            }

            ps.print("    public " + table_name +
                     " findByKeyword(Connection conn");
            int indexs[] = table_desc.getPrimaryKeyIndex();
            if (indexs != null)
            {
                for (int i = 0; i < indexs.length; i++)
                {
                    ColumnDesc column_desc = table_desc.getColumnDesc(indexs[i]);
                    ps.print(", " +
                             ColumnDesc.toJavaType(column_desc.getFieldType()) +
                             " ");
                    ps.print(toIdentifier(column_desc.getFieldName()));
                }
            }
            ps.println(")");
            ps.println("    {");
            ps.println("        ArrayList keyword = new ArrayList();");
            for (int i = 0; i < indexs.length; i++)
            {
                ColumnDesc column_desc = table_desc.getColumnDesc(indexs[i]);
                ps.println("        keyword.add(\"\" + " +
                           toIdentifier(column_desc.getFieldName()) + ");");
            }
            ps.println("        CommonDAO dao = findByKeyword(conn, keyword);");
            ps.println("        if (dao != null)");
            ps.println("        {");
            ps.println("            return (" + table_name + ")dao;");
            ps.println("        }");
            ps.println("        else");
            ps.println("        {");
            ps.println("            return null;");
            ps.println("        }");
            ps.println("    }");
            ps.println();

            ps.print("    public boolean deleteByKeyword(Connection conn");
            if (indexs != null)
            {
                for (int i = 0; i < indexs.length; i++)
                {
                    ColumnDesc column_desc = table_desc.getColumnDesc(indexs[i]);
                    ps.print(", " +
                             ColumnDesc.toJavaType(column_desc.getFieldType()) +
                             " ");
                    ps.print(toIdentifier(column_desc.getFieldName()));
                }
            }
            ps.println(")");
            ps.println("    {");

            ps.println("        ArrayList keyword = new ArrayList();");
            for (int i = 0; i < indexs.length; i++)
            {
                ColumnDesc column_desc = table_desc.getColumnDesc(indexs[i]);
                ps.println("        keyword.add(\"\" + " +
                           toIdentifier(column_desc.getFieldName()) + ");");
            }
            ps.println("        boolean succ = deleteByKeyword(conn, keyword);");
            ps.println("        return succ;");
            ps.println("    }");
            ps.println();

            for (int i=0; i<table_desc.getColumnCount(); i++)
            {
                ColumnDesc column_desc = table_desc.getColumnDesc(i);
                ps.println("    public ArrayList getColumnValuesOf" + capitalizeFirst(column_desc.getFieldName()) + "(Connection conn)");
                ps.println("    {");
                ps.println("        return getColumnValues(conn, \"" + column_desc.getFieldName() + "\");");
                ps.println("    }");
                ps.println();
            }

            for (int i=0; i<table_desc.getColumnCount(); i++)
            {
                ColumnDesc column_desc = table_desc.getColumnDesc(i);
                if (!ColumnDesc.isChar(column_desc.getFieldType()))
                {
                    ps.println("    public double getSumOf" +
                               capitalizeFirst(column_desc.getFieldName()) +
                               "(Connection conn)");
                    ps.println("    {");
                    ps.println("        return getSum(conn, \"" +
                               column_desc.getFieldName() + "\");");
                    ps.println("    }");
                    ps.println();
                }
            }

            for (int i=0; i<table_desc.getColumnCount(); i++)
            {
                ColumnDesc column_desc = table_desc.getColumnDesc(i);
                if (!ColumnDesc.isChar(column_desc.getFieldType()))
                {
                    ps.println("    public double getAverageOf" +
                               capitalizeFirst(column_desc.getFieldName()) +
                               "(Connection conn)");
                    ps.println("    {");
                    ps.println("        return getAverage(conn, \"" +
                               column_desc.getFieldName() + "\");");
                    ps.println("    }");
                    ps.println();
                }
            }

            for (int i=0; i<table_desc.getColumnCount(); i++)
            {
                ColumnDesc column_desc = table_desc.getColumnDesc(i);
                ps.println("    public String getMinOf" +
                           capitalizeFirst(column_desc.getFieldName()) +
                           "(Connection conn)");
                ps.println("    {");
                ps.println("        return getMin(conn, \"" +
                           column_desc.getFieldName() + "\");");
                ps.println("    }");
                ps.println();
            }

            for (int i=0; i<table_desc.getColumnCount(); i++)
            {
                ColumnDesc column_desc = table_desc.getColumnDesc(i);
                ps.println("    public String getMaxOf" +
                           capitalizeFirst(column_desc.getFieldName()) +
                           "(Connection conn)");
                ps.println("    {");
                ps.println("        return getMax(conn, \"" +
                           column_desc.getFieldName() + "\");");
                ps.println("    }");
                ps.println();
            }

            ps.println("    /***** JavaBeans属性 *****/");
            for (int i = 0; i < table_desc.getColumnCount(); i++)
            {
                ColumnDesc column_desc = table_desc.getColumnDesc(i);
                ps.println("    public void set" +
                           capitalizeFirst(column_desc.getFieldName()) + "(" +
                           ColumnDesc.toJavaType(column_desc.getFieldType()) +
                           " p)");
                ps.println("    {");
                ps.println("        set" +
                           ColumnDesc.
                                           toJavaType2(column_desc.getFieldType()) +
                           "(" + i + ", p);");
                ps.println("    }");
                ps.println();

                ps.println("    public " +
                           ColumnDesc.toJavaType(column_desc.getFieldType()) +
                           " get" + capitalizeFirst(column_desc.getFieldName()) +
                           "()");
                ps.println("    {");
                ps.println("        return get" +
                           ColumnDesc.
                                           toJavaType2(column_desc.getFieldType()) +
                           "(" + i + ");");
                ps.println("    }");
                ps.println();
            }

            ps.print("}");
            fos.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return true;
    }

    private String capitalizeFirst(String s)
    {
        if (s == null || s.length() == 0)
            return s;

        String first = s.substring(0, 1);

        if (s.length() > 1)
            return first.toUpperCase() + s.substring(1);
        else
            return first.toUpperCase();
    }

    private String toIdentifier(String s)
    {
        if (s == null || s.length() == 0)
            return s;

        boolean first_flag = true;
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<s.length(); i++)
        {
            char c = s.charAt(i);
            if (c >= 'A' && c <= 'Z')
            {
                if (i != 0 && first_flag)
                    sb.append('_');
                c = (char)(c - ('A' - 'a'));
                sb.append(c);
                first_flag = false;
            }
            else
            {
                sb.append(c);
                first_flag = true;
            }
        }

        return sb.toString();
    }

    /**
     * 是否存在非Identity的关键字
     * @param table_desc TableDesc
     * @return boolean
     */
    private boolean existNonIdentityPrimaryKeys(TableDesc table_desc)
    {
        for (int i=0; i<table_desc.getColumnCount(); i++)
        {
            ColumnDesc column_desc = table_desc.getColumnDesc(i);
            if (column_desc.isPrimaryKeyOrNot() && !column_desc.isIdentityOrNot())
                return true;
        }

        return false;
    }

    /**
     * 是否存在非Identity的关键字
     * @param table_desc TableDesc
     * @return boolean
     */
    private boolean existNonIdentityNonNullableKeys(TableDesc table_desc)
    {
        for (int i = 0; i < table_desc.getColumnCount(); i++)
        {
            ColumnDesc column_desc = table_desc.getColumnDesc(i);
            if (!column_desc.isFieldNullOrNot() && !column_desc.isPrimaryKeyOrNot() && !column_desc.isIdentityOrNot())
                return true;
        }

        return false;
    }

    /**
     * 是否存在非Identity的非关键字
     * @param table_desc TableDesc
     * @return boolean
     */
    private boolean existNonIdentityNullableKeys(TableDesc table_desc)
    {
        for (int i=0; i<table_desc.getColumnCount(); i++)
        {
            ColumnDesc column_desc = table_desc.getColumnDesc(i);
            if (!column_desc.isPrimaryKeyOrNot() && column_desc.isFieldNullOrNot() && !column_desc.isIdentityOrNot())
                return true;
        }

        return false;
    }
}
