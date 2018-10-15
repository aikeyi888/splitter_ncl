package com.wisdom.ncl.splitter.base;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/*
 * Copyright \u00A9 2005, 北京东方般若科技发展有限公司
 * All rights reserved
 *
 * 文件名称：日期类型与字符串类型相互转换
 * 摘要：
 *
 *
 * 完成日期：2005-02-27
 */

/**
 * 日期类型与字符串类型相互转换
 */
public class DateUtil
{

    /**
     * Base ISO 8601 Date format yyyyMMdd i.e., 20021225 for the 25th day of December in the year 2002
     */
    public static final String ISO_DATE_FORMAT = "yyyyMMdd";
    
    public static final String DATE_FORMAT = "yyyyMMdd HH:mm:ss";

    /**
     * Expanded ISO 8601 Date format yyyy-MM-dd i.e., 2002-12-25 for the 25th day of December in the year 2002
     */
    public static final String ISO_EXPANDED_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * yyyy-MM-dd hh:mm:ss
     */
    public static String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * Default lenient setting for getDate.
     */
    private static boolean LENIENT_DATE = false;

    /**
     * 暂时不用
     * @param JD
     * @return
     */
    protected static final float normalizedJulian(float JD)
    {

        float f = Math.round(JD + 0.5f) - 0.5f;

        return f;
    }

    /**
     * 浮点值转换成日期格式<br>
     * 暂时不用
         * Returns the Date from a julian. The Julian date will be converted to noon GMT,
         * such that it matches the nearest half-integer (i.e., a julian date of 1.4 gets
     * changed to 1.5, and 0.9 gets changed to 0.5.)
     *
     * @param JD the Julian date
     * @return the Gregorian date
     */
    public static final Date toDate(float JD)
    {

        /* To convert a Julian Day Number to a Gregorian date, assume that it is for 0 hours, Greenwich time (so
         * that it ends in 0.5). Do the following calculations, again dropping the fractional part of all
         * multiplicatons and divisions. Note: This method will not give dates accurately on the
         * Gregorian Proleptic Calendar, i.e., the calendar you get by extending the Gregorian
             * calendar backwards to years earlier than 1582. using the Gregorian leap year
         * rules. In particular, the method fails if Y<400. */
        float Z = (normalizedJulian(JD)) + 0.5f;
        float W = (int) ( (Z - 1867216.25f) / 36524.25f);
        float X = (int) (W / 4f);
        float A = Z + 1 + W - X;
        float B = A + 1524;
        float C = (int) ( (B - 122.1) / 365.25);
        float D = (int) (365.25f * C);
        float E = (int) ( (B - D) / 30.6001);
        float F = (int) (30.6001f * E);
        int day = (int) (B - D - F);
        int month = (int) (E - 1);

        if (month > 12)
        {
            month = month - 12;
        }

        int year = (int) (C - 4715); //(if Month is January or February) or C-4716 (otherwise)

        if (month > 2)
        {
            year--;
        }

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1); // damn 0 offsets
        c.set(Calendar.DATE, day);

        return c.getTime();
    }

    /**
     * Returns the days between two dates. Positive values indicate that
     * the second date is after the first, and negative values indicate, well,
     * the opposite. Relying on specific times is problematic.
     *
     * @param early the "first date"
     * @param late the "second date"
     * @return the days between the two dates
     */
    public static final int daysBetween(Date early, Date late)
    {

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(early);
        c2.setTime(late);

        return daysBetween(c1, c2);
    }

    public static final int monthsBetween(Date early, Date late)
    {
        if (early == null || late == null)
            return -1;
        int early_year = early.getYear();
        int early_month = early.getMonth();
        int late_year = late.getYear();
        int late_month = late.getMonth();
        if (early_month == 0)
        {
            early_month = 12;
            early_year = early_year - 1;
        }
        if (late_month == 0)
        {
            late_month = 12;
            late_year = late_year - 1;
        }
        int month_def;
        month_def = (late_year - early_year) * 12 + (late_month - early_month);

        return month_def;
    }

    /**
     * Returns the days between two dates. Positive values indicate that
     * the second date is after the first, and negative values indicate, well,
     * the opposite.
     *
     * @param early
     * @param late
     * @return the days between two dates.
     */
    public static final int daysBetween(Calendar early, Calendar late)
    {

        return (int) (toJulian(late) - toJulian(early));
    }

    /**
     * Return a Julian date based on the input parameter. This is
     * based from calculations found at
     * <a href="http://quasar.as.utexas.edu/BillInfo/JulianDatesG.html">Julian Day Calculations
     * (Gregorian Calendar)</a>, provided by Bill Jeffrys.
     * @param c a calendar instance
     * @return the julian day number
     */
    public static final float toJulian(Calendar c)
    {

        int Y = c.get(Calendar.YEAR);
        int M = c.get(Calendar.MONTH);
        int D = c.get(Calendar.DATE);
        int A = Y / 100;
        int B = A / 4;
        int C = 2 - A + B;
        float E = (int) (365.25f * (Y + 4716));
        float F = (int) (30.6001f * (M + 1));
        float JD = C + D + E + F - 1524.5f;

        return JD;
    }

    /**
     * 暂时不用
     * Return a Julian date based on the input parameter. This is
     * based from calculations found at
     * <a href="http://quasar.as.utexas.edu/BillInfo/JulianDatesG.html">Julian Day Calculations
     * (Gregorian Calendar)</a>, provided by Bill Jeffrys.
     * @param date
     * @return the julian day number
     */
    public static final float toJulian(Date date)
    {

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        return toJulian(c);
    }

    /**
     * 日期增加
     * @param isoString 日期字符串
     * @param fmt 格式
     * @param field 年/月/日 Calendar.YEAR/Calendar.MONTH/Calendar.DATE
     * @param amount 增加数量
     * @return
     * @throws ParseException
     */
    public static final String dateIncrease(String isoString, String fmt,
                                            int field, int amount)
    {

        try
        {
            Calendar cal = GregorianCalendar.getInstance(TimeZone.getTimeZone(
                "GMT"));
            cal.setTime(stringToDate(isoString, fmt, true));
            cal.add(field, amount);

            return dateToString(cal.getTime(), fmt);

        }
        catch (Exception ex)
        {
            return null;
        }
    }

    /**
     * Time Field Rolling function.
     * Rolls (up/down) a single unit of time on the given time field.
     *
     * @param isoString
     * @param field the time field.
     * @param up Indicates if rolling up or rolling down the field value.
     * @param expanded use formating char's
     * @exception ParseException if an unknown field value is given.
     */
    public static final String roll(String isoString, String fmt, int field,
                                    boolean up) throws ParseException
    {

        Calendar cal = GregorianCalendar.getInstance(TimeZone.getTimeZone(
            "GMT"));
        cal.setTime(stringToDate(isoString, fmt));
        cal.roll(field, up);

        return dateToString(cal.getTime(), fmt);
    }

    /**
     * Time Field Rolling function.
     * Rolls (up/down) a single unit of time on the given time field.
     *
     * @param isoString
     * @param field the time field.
     * @param up Indicates if rolling up or rolling down the field value.
     * @exception ParseException if an unknown field value is given.
     */
    public static final String roll(String isoString, int field, boolean up) throws
        ParseException
    {
        return roll(isoString, DATETIME_PATTERN, field, up);
    }

    public static Date stringToDateWithTime(String s)
    {
        int year = 0;
        int month = 0;
        int date = 0;
        int hour = 0;
        int minute = 0;
        int second = 0;

        if (s == null)
            return new Date();

        try
        {
            int pos = 0;
            pos = s.indexOf("-");
            if (pos < 0)
                return new Date();
            else
            {
                year = Integer.parseInt(s.substring(0, pos));
            }

            s = s.substring(pos + 1);
            pos = s.indexOf("-");
            if (pos < 0)
                return new Date();
            else
            {
                month = Integer.parseInt(s.substring(0, pos));
            }

            try
            {
                s = s.substring(pos + 1);
                pos = s.indexOf(" ");
                if (pos < 0)
                {
                    date = Integer.parseInt(s);
                    s = null;
                }
                else
                {
                    date = Integer.parseInt(s.substring(0, pos));
                }
            }
            catch (Exception e)
            {
                s = null;
            }

            try
            {
                s = s.substring(pos + 1);
                pos = s.indexOf(":");
                if (pos < 0)
                {
                    hour = Integer.parseInt(s);
                    s = null;
                }
                else
                {
                    hour = Integer.parseInt(s.substring(0, pos));
                }
            }
            catch (Exception e)
            {
                s = null;
            }

            try
            {
                s = s.substring(pos + 1);
                pos = s.indexOf(":");
                if (pos < 0)
                {
                    minute = Integer.parseInt(s);
                    s = null;
                }
                else
                {
                    minute = Integer.parseInt(s.substring(0, pos));
                }
            }
            catch (Exception e)
            {
                s = null;
            }

            try
            {
                s = s.substring(pos + 1);
                pos = s.indexOf(".");
                if (pos < 0)
                {
                    second = Integer.parseInt(s);
                    s = null;
                }
                else
                {
                    second = Integer.parseInt(s.substring(0, pos));
                }
            }
            catch (Exception e)
            {
            }
        }
        catch (Exception e)
        {
            return new Date();
        }

        Date time = new Date(year-1900, month-1, date, hour, minute, second);
        return time;
    }

    /**
     * 字符串转换为日期java.util.Date
     * @param dateText 字符串
     * @param format 日期格式
     * @param lenient 日期越界标志
     * @return
     */
    public static Date stringToDate(String dateText, String format,
                                    boolean lenient)
    {

        if (dateText == null)
        {

            return null;
        }

        DateFormat df = null;

        try
        {

            if (format == null)
            {
                df = new SimpleDateFormat();
            }
            else
            {
                df = new SimpleDateFormat(format);
            }

            // setLenient avoids allowing dates like 9/32/2001
            // which would otherwise parse to 10/2/2001
            df.setLenient(false);

            return df.parse(dateText);
        }
        catch (ParseException e)
        {

            return null;
        }
    }

    /**
     * 字符串转换为日期java.util.Date
     * @param dateText 字符串
     * @param format 日期格式
     * @return
     */
    public static Date stringToDate(String dateString, String format)
    {

        return stringToDate(dateString, format, LENIENT_DATE);
    }

    /**
     * 字符串转换为日期java.util.Date
     * @param dateText 字符串
     */
    public static Date stringToDate(String dateString)
    {
        return stringToDate(dateString, ISO_EXPANDED_DATE_FORMAT, LENIENT_DATE);
    }

    /** 根据时间变量返回时间字符串
     * @return 返回时间字符串
     * @param pattern 时间字符串样式
     * @param date 时间变量
     */
    public static String dateToString(Date date, String pattern)
    {

        if (date == null)
        {

            return null;
        }

        try
        {

            SimpleDateFormat sfDate = new SimpleDateFormat(pattern);
            sfDate.setLenient(false);

            return sfDate.format(date);
        }
        catch (Exception e)
        {

            return null;
        }
    }

    /**
     * 根据时间变量返回时间字符串 yyyy-MM-dd
     * @param date
     * @return
     */
    public static String dateToString(Date date)
    {
        return dateToString(date, ISO_EXPANDED_DATE_FORMAT);
    }

    /** 返回当前时间
     * @return 返回当前时间
     */
    public static Date getCurrentDateTime()
    {
        java.util.Calendar calNow = java.util.Calendar.getInstance();
        java.util.Date dtNow = calNow.getTime();

        return dtNow;
    }

    /**
     * 返回当前日期字符串
     * @param pattern 日期字符串样式
     * @return
     */
    public static String getCurrentDateString(String pattern)
    {
        return dateToString(getCurrentDateTime(), pattern);
    }

    /**
     * 返回当前日期字符串 yyyy-MM-dd
     * @return
     */
    public static String getCurrentDateString()
    {
        return dateToString(getCurrentDateTime(), ISO_EXPANDED_DATE_FORMAT);
    }

    /**
     * 返回当前日期+时间字符串 yyyy-MM-dd hh:mm:ss
     * @param date
     * @return
     */
    public static String dateToStringWithTime(Date date)
    {

        return dateToString(date, DATETIME_PATTERN);
    }

    /**
     * 日期增加-按日增加
     * @param date
     * @param days
     * @return java.util.Date
     */
    public static Date dateIncreaseByDay(Date date, int days)
    {

        Calendar cal = GregorianCalendar.getInstance(TimeZone.getTimeZone(
            "GMT"));
        cal.setTime(date);
        cal.add(Calendar.DATE, days);

        return cal.getTime();
    }

    /**
     * 日期增加-按月增加
     * @param date
     * @param days
     * @return java.util.Date
     */
    public static Date dateIncreaseByMonth(Date date, int mnt)
    {

        Calendar cal = GregorianCalendar.getInstance(TimeZone.getTimeZone(
            "GMT"));
        cal.setTime(date);
        cal.add(Calendar.MONTH, mnt);

        return cal.getTime();
    }

    /**
     * 日期增加-按年增加
     * @param date
     * @param mnt
     * @return java.util.Date
     */
    public static Date dateIncreaseByYear(Date date, int mnt)
    {

        Calendar cal = GregorianCalendar.getInstance(TimeZone.getTimeZone(
            "GMT"));
        cal.setTime(date);
        cal.add(Calendar.YEAR, mnt);

        return cal.getTime();
    }

    /**
     * 日期增加
     * @param date 日期字符串 yyyy-MM-dd
     * @param days
     * @return 日期字符串 yyyy-MM-dd
     */
    public static String dateIncreaseByDay(String date, int days)
    {
        return dateIncreaseByDay(date, ISO_DATE_FORMAT, days);
    }

    /**
     * 日期增加
     * @param date 日期字符串
     * @param fmt 日期格式
     * @param days
     * @return
     */
    public static String dateIncreaseByDay(String date, String fmt, int days)
    {
        return dateIncrease(date, fmt, Calendar.DATE, days);
    }

    /**
     * 日期字符串格式转换
     * @param src 日期字符串
     * @param srcfmt 源日期格式
     * @param desfmt 目标日期格式
     * @return
     */
    public static String stringToString(String src, String srcfmt,
                                        String desfmt)
    {
        return dateToString(stringToDate(src, srcfmt), desfmt);
    }

    /**
     * 取当前日期的年份
     * @param date 当前日期
     * @return string
     */
    public static String getYear(Date date)
    {
        java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat("yyyy");
        String cur_year = formater.format(date);
        return cur_year;
    }

    /**
     * 取当前日期的年份
     * @param date 当前日期
     * @return string
     */
    public static String getMonth(Date date)
    {
        java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat("MM");
        String cur_month = formater.format(date);
        return cur_month;
    }

    /**
     * 取当前日期的年份
     * @param date 当前日期
     * @return string
     */
    public static String getDay(Date date)
    {
        java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat("dd");
        String cur_day = formater.format(date);
        return cur_day;
    }
    /**
     * 取得月份天数
     * @param date Date
     * @return String
     */
    public static String getDaysInMonth(Date date)
    {
      String days_in_month = "";
      String month = getMonth(date);

      if (month.equals("02"))
      {
        int year = Integer.parseInt(getMonth(date));

        if ((year % 4 == 0) && (year % 100 != 0))
        {
          days_in_month = "29";
        }
        else
        {
            days_in_month = "28";
        }
      }
      else if (month.equals("04") || month.equals("06")
          || month.equals("09") || month.equals("11"))
      {
        days_in_month = "30";
      }
      else
      {
          days_in_month = "31";
      }

      return days_in_month;
    }
    /**
     * 取得月份天数
     * @param date Date
     * @return String
     */
    public static String getDaysInMonth(String str_date)
    {
      String days_in_month = "";
//      String month = getMonth(date);
      String str_year = str_date.substring(0,4);
      String str_month = str_date.substring(5,7);

      if (str_month.equals("02"))
      {
        int year = Integer.parseInt(str_year);

        if ((year % 4 == 0) && (year % 100 != 0))
        {
          days_in_month = "29";
        }
        else
        {
            days_in_month = "28";
        }
      }
      else if (str_month.equals("04") || str_month.equals("06")
          || str_month.equals("09") || str_month.equals("11"))
      {
        days_in_month = "30";
      }
      else
      {
          days_in_month = "31";
      }

      return days_in_month;
    }

    /**
     * 获取当前 年
     * @return int
     */
    public static int getYear()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new java.util.Date());
        int year = calendar.get(calendar.YEAR);
        return year;
    }

    /**
     * 获取当前 月
     * @return int
     */
    public static int getMonth()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new java.util.Date());
        int month = calendar.get(calendar.MONTH);
        return month;
    }

    /**
     * 获取当前 日
     * @return int
     */
    public static int getDay()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new java.util.Date());
        int day = calendar.get(calendar.DATE);
        return day;
    }

    /**
     * 获取当前 时
     * @return int
     */
    public static int getHour()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new java.util.Date());
        int hour = calendar.get(calendar.HOUR_OF_DAY);
        return hour;
    }


    /**
     * 获取当前 分
     * @return int
     */
    public static int getMinute()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new java.util.Date());
        int minut = calendar.get(calendar.MINUTE);
        return minut;
    }

    /**
     * 获取当前 秒
     * @return int
     */
    public static int getSecond()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new java.util.Date());
        int second = calendar.get(calendar.SECOND);
        return second;
    }
    
    public static boolean compare_date(String DATE1, String DATE2,String DATE3) {
        
        
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            Date dt3 = df.parse(DATE3);
            if (dt1.getTime() >= dt2.getTime()&&dt1.getTime() <= dt3.getTime()) {
                return true;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        return false;
    }
 



    public static void main(String[] args)
    {
        /*RegisterManager x = new RegisterManager();
                 try {
            int tempInt = x.Register("1234", "13331016539", 12, "wanghua",
                                     "cowboywh@sina.com", "33333", "12345");
            System.out.println("" + tempInt);
                 }
                 catch (Exception e) {
            System.out.println("wrong");
                 }*/

        String str = "2005-03-12";
        System.out.println(stringToDate(str));
        System.out.print(getDay(stringToDate(str)));
        java.util.Date date = getCurrentDateTime();
        System.out.print(date) ;
        System.out.println(date.getYear() + " " + date.getMonth());

        Calendar cal = GregorianCalendar.getInstance(TimeZone.getTimeZone(
            "GMT"));
        cal.setTime(date);
        System.out.print(cal.YEAR) ;
        System.out.print(date) ;

        java.text.SimpleDateFormat fort = new java.text.SimpleDateFormat("dd");
        String cur = fort.format(date);
        System.out.print(cur) ;
//System.out.println(getCurrentDateString());
        java.util.Date test_date = new java.util.Date(2004, 3, 31);
//        System.out.println(dateIncreaseByMonth(test_date, -4).getYear() + " " +
//                           dateIncreaseByMonth(test_date, -4).getMonth() + " " +
//                           dateIncreaseByMonth(test_date, -4).getDate());
    }

}
