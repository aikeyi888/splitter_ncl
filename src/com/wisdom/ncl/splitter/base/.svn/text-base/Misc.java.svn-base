package com.wisdom.ncl.splitter.base;

import java.io.*;
import java.util.*;

public class Misc
{
    public static short bytesToShort(byte[] b, int offset)
    {
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(2);
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeByte(b[offset]);
            dos.writeByte(b[offset + 1]);

            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(
                baos.toByteArray(), 0, 2));
            short result = dis.readShort();

            dos.close();
            dis.close();
            return result;
        }
        catch (IOException e)
        {
            return 0;
        }
    }

    public static int bytesToInt(byte[] b, int offset)
    {
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(4);
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeByte(b[offset]);
            dos.writeByte(b[offset + 1]);
            dos.writeByte(b[offset + 2]);
            dos.writeByte(b[offset + 3]);

            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(
                baos.toByteArray(), 0, 4));
            int result = dis.readInt();

            dos.close();
            dis.close();
            return result;
        }
        catch (IOException e)
        {
            return 0;
        }
    }

    public static byte[] shortToBytes(short value)
    {
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(2);
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeShort(value);

            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(
                baos.toByteArray(), 0, 2));
            byte[] result = new byte[2];
            result[0] = dis.readByte();
            result[1] = dis.readByte();

            dos.close();
            dis.close();
            return result;
        }
        catch (IOException e)
        {
            return null;
        }
    }

    public static byte[] intToBytes(int value)
    {
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(4);
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeInt(value);

            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(
                baos.toByteArray(), 0, 4));
            byte[] result = new byte[4];
            result[0] = dis.readByte();
            result[1] = dis.readByte();
            result[2] = dis.readByte();
            result[3] = dis.readByte();

            dos.close();
            dis.close();
            return result;
        }
        catch (IOException e)
        {
            return null;
        }
    }

    public static Date bytesToDate(byte[] data)
    {
        return null;
    }

    public static byte[] dateToBytes(Date date)
    {
        return null;
    }

    public static boolean compareStrings(String s1, String s2)
    {
        if (s1 == null || s1.equals(""))
        {
            if (s2 == null || s2.equals(""))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            if (s1.equals(s2))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    public static String formatString(String s, int length)
    {
        String result = "";
        int space = 0;

        if (s == null)
        {
            space = length;
        }
        else
        {
            if (s.getBytes().length > length)
            {
                result = s.substring(0, length);
                space = 0;
            }
            else
            {
                result = s;
                space = length - s.getBytes().length;
            }
        }

        for (int i = 0; i < space; i++)
        {
            result += " ";

        }
        return result;
    }

    public static String convertIPFormat(String ip, int direction)
    {
        try
        {
            if (ip == null || ip.equals(""))
            {
                return null;
            }

            switch (direction)
            {
                case 0:
                    int index1 = ip.indexOf(".");
                    int index2 = ip.indexOf(".", index1 + 1);
                    int index3 = ip.indexOf(".", index2 + 1);
                    String result = "";
                    String section1 = ip.substring(0, index1);
                    String section2 = ip.substring(index1 + 1, index2);
                    String section3 = ip.substring(index2 + 1, index3);
                    String section4 = ip.substring(index3 + 1);
                    String sections[] = new String[]
                        {
                        section1, section2, section3, section4};
                    for (int i = 0; i < sections.length; i++)
                    {
                        for (int j = 0; j < 3 - sections[i].length(); j++)
                        {
                            result += "0";
                        }
                        result += sections[i];
                    }
                    return result;

                default:
                    int s1 = 0;
                    int s2 = 0;
                    int s3 = 0;
                    int s4 = 0;
                    s1 = Integer.parseInt(ip.substring(0, 3));
                    s2 = Integer.parseInt(ip.substring(3, 6));
                    s3 = Integer.parseInt(ip.substring(6, 9));
                    s4 = Integer.parseInt(ip.substring(9));
                    return "" + s1 + "." + s2 + "." + s3 + "." + s4;
            }
        }
        catch (Exception e)
        {
            return ip;
        }
    }

    public static String stringToDBString(String s)
    {
        if (s == null)
        {
            return "";
        }

        return s.replaceAll("'", "''");
    }

    public static String stringToSQLDBString(String s)
    {
        if (s == null)
        {
            return "";
        }

        return s.replaceAll("'", "''");
    }


    public static String dateToString(java.util.Date time)
    {
        if (time == null)
        {
            return "";
        }
        String year, month, date, hour, minute, second;

        year = "" + (time.getYear() + 1900);
        month = "" + (time.getMonth() + 1 + 100);
        date = "" + (time.getDate() + 100);
        hour = "" + (time.getHours() + 100);
        minute = "" + (time.getMinutes() + 100);
        second = "" + (time.getSeconds() + 100);

        String s = year + "-" + month.substring(1) + "-" + date.substring(1) + " " + hour.substring(1) + ":" + minute.substring(1) + ":" + second.substring(1);
        return s;
    }

    public static String getFilePath(String file_path)
    {
        if (file_path == null || file_path.equals(""))
        {
            return "..";
        }

        String delimeter = System.getProperty("file.separator");
        int last_index, index;
        last_index = 0;
        index = file_path.indexOf(delimeter, last_index);
        try
        {
            while (index >= 0)
            {
                last_index = index;
                index = file_path.indexOf(delimeter, last_index + 1);
            }
        }
        catch (Exception e)
        {
        }

        if (last_index < 0)
        {
            return "..";
        }
        else
        {
            return file_path.substring(0, last_index + 1);
        }
    }

    public static String enddateToString(Date time, int complete_hour_end)
    {
        String year, month, date, hour, minute, second, s;
        if (time == null)
        {
            time = new Date();
        }
        year = "" + (time.getYear() + 1900);
        month = "" + (time.getMonth() + 1 + 100);
        date = "" + (time.getDate() + 100);
        hour = "" + String.valueOf(complete_hour_end);
        minute = "00";
        second = "00";

        if (hour.equals("10000"))
        {
            hour = "18";
            s = year + month.substring(1) + date.substring(1) + " " + hour +
                minute + second;
        }
        else
        {
            if (hour.length() == 2)
            {
                s = year + month.substring(1) + date.substring(1) + " " + hour +
                    minute + second;
            }
            else
            {
                hour = "0" + hour;
                s = year + month.substring(1) + date.substring(1) + " " + hour +
                    minute + second;
            }
        }
        return s;
    }

    public static String begindateToString(Date time, int complete_hour_begin)
    {
        String year, month, date, hour, minute, second, s;

        year = "" + (time.getYear() + 1900);
        month = "" + (time.getMonth() + 1 + 100);
        date = "" + (time.getDate() + 100);
        hour = "" + String.valueOf(complete_hour_begin);
        minute = "00";
        second = "00";
        if (hour.equals("10000"))
        {
            hour = "08";
            s = year + month.substring(1) + date.substring(1) + " " + hour +
                minute + second;
        }
        else
        {
            if (hour.length() == 2)
            {
                s = year + month.substring(1) + date.substring(1) + " " + hour +
                    minute + second;
            }
            else
            {
                hour = "0" + hour;
                s = year + month.substring(1) + date.substring(1) + " " + hour +
                    minute + second;
            }
        }
        return s;
    }

    public static String dateToDBString(Date time)
    {
        if (time == null)
        {
            return "";
        }
        String year, month, date, hour, minute, second;

        year = "" + (time.getYear() + 1900);
        month = "" + (time.getMonth() + 1 + 100);
        date = "" + (time.getDate() + 100);

        String s = year + "-" + month.substring(1) + "-" + date.substring(1);
        return s;
    }

    public static String dateToDate(String str_date)
    {
        String str_to_date = "";
        if (str_date != null)
        {
            int int_index = str_date.indexOf("-");
            if (int_index != -1)
            {
                str_to_date += str_date.substring(0, int_index);
                str_date = str_date.substring(int_index + 1, str_date.length());
                int_index = str_date.indexOf("-");
                if (int_index != -1)
                {
                    str_to_date += str_date.substring(0, int_index);
                    str_to_date +=
                        str_date.substring(int_index + 1, str_date.length());
                }
            }
        }
        return str_to_date;
    }

    public static boolean isValidMobilePhone(String phone)
    {
        if (phone == null || phone.equals(""))
            return false;

        if ( (phone.startsWith("13") &&
              phone.length() != 11) ||
            (phone.startsWith("013") &&
             phone.length() != 12) ||
            (phone.startsWith("08613") &&
             phone.length() != 14) &&
            (phone.startsWith("15") &&
              phone.length() != 11) ||
            (phone.startsWith("015") &&
             phone.length() != 12) ||
            (phone.startsWith("08615") &&
             phone.length() != 14))
        {
            return false;
        }

        try
        {
            Long.parseLong(phone);
        }
        catch (Exception er)
        {
            return false;
        }

        if (!phone.startsWith("0") && !phone.startsWith("13") && !phone.startsWith("15"))
        {
            return false;
        }

        return true;
    }

    public static int getSMSplitNumber(String sm)
    {
        if (sm == null)
            return 0;

        if (sm.length() % 70 == 0)
            return sm.length() / 70;
        else
            return sm.length() / 70 + 1;
    }


    public static String getKeywordString(ArrayList keywords)
    {
        if (keywords == null)
            return "";

        String s = "";
        for (int i = 0; i < keywords.size(); i++)
        {
            if (i != 0)
                s += "|";

            String value = (String) keywords.get(i);
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

    //²éÑ¯²»ÔÚSQLÓï¾ä×Ö·û´®ÖÐµÄ×Ö·û´®Î»ÖÃ
    public static int indexOfSQL(String sql, String target, int begin_index)
    {
        if (sql == null || target == null || begin_index < 0)
            return -1;

        int pos = begin_index;
        while (true)
        {
           pos = sql.indexOf(target, pos);
           if (pos < 0)
               return -1;

           int count = 0;
           for (int i=0; i<pos; i++)
           {
               char c = sql.charAt(i);
               if (c == '\'')
               {
                   count ++;
               }
           }

           if (count % 2 == 0)
               break;
           else
           {
               pos += target.length();
               continue;
           }
        }

        return pos;
    }

    public static String nonNull(String s)
    {
        if (s == null)
            return "";
        else
            return s;
    }

}
