package com.wisdom.ncl.splitter.base;

import java.util.Date;
import java.util.Calendar;

public class GetDateTime {
    private String str_date = null;
    private String str_date_time = null;
    public GetDateTime() {
    }
    public String getMonthBeginDate()
    {
         Calendar m_calendar = Calendar.getInstance();
         m_calendar.setTime(new Date());
         int int_curr_year = m_calendar.get(Calendar.YEAR);
         int int_curr_month = m_calendar.get(Calendar.MONTH);
         m_calendar.set(int_curr_year,int_curr_month,1);
         int int_year = m_calendar.get(Calendar.YEAR);
         int int_month = m_calendar.get(Calendar.MONTH) + 1;
         int int_day = m_calendar.get(Calendar.DAY_OF_MONTH);
         str_date = String.valueOf(int_year);
         if (int_month < 10)
         {
            str_date += "-0" + String.valueOf(int_month);
         }
         else
         {
            str_date += "-" + String.valueOf(int_month);
         }
         if (int_day < 10)
         {
            str_date += "-0" + String.valueOf(int_day);
         }
         else
         {
            str_date += "-" + String.valueOf(int_day);
         }
         return this.str_date;
    }
    public String getDate()
    {
         Calendar m_calendar = Calendar.getInstance();
         m_calendar.setTime(new Date());
         int int_year = m_calendar.get(Calendar.YEAR);
         int int_month = m_calendar.get(Calendar.MONTH) + 1;
         int int_day = m_calendar.get(Calendar.DAY_OF_MONTH);
         str_date = String.valueOf(int_year);
         if (int_month < 10)
         {
            str_date += "-0" + String.valueOf(int_month);
         }
         else
         {
            str_date += "-" + String.valueOf(int_month);
         }
         if (int_day < 10)
         {
            str_date += "-0" + String.valueOf(int_day);
         }
         else
         {
            str_date += "-" + String.valueOf(int_day);
         }
         return this.str_date;
    }
    public String getDateTime()
    {
        Calendar m_calendar = Calendar.getInstance();
        m_calendar.setTime(new Date());
        int int_year = m_calendar.get(Calendar.YEAR);
        int int_month = m_calendar.get(Calendar.MONTH) + 1;
        int int_day = m_calendar.get(Calendar.DAY_OF_MONTH);
        int int_hour = m_calendar.get(Calendar.HOUR);
        int int_minute = m_calendar.get(Calendar.MINUTE);
        int int_second = m_calendar.get(Calendar.SECOND);
        int int_milli_second = m_calendar.get(Calendar.MILLISECOND);
        str_date_time = String.valueOf(int_year);
        if (int_month < 10)
        {
           str_date_time += "-0" + String.valueOf(int_month);
        }
        else
        {
           str_date_time += "-" + String.valueOf(int_month);
        }
        if (int_day < 10)
        {
           str_date_time += "-0" + String.valueOf(int_day);
        }
        else
        {
           str_date_time += "-" + String.valueOf(int_day);
        }
        if (int_hour < 10)
        {
           str_date_time += " 0" + String.valueOf(int_hour);
        }
        else
        {
           str_date_time += " " + String.valueOf(int_hour);
        }
        if (int_minute < 10)
        {
           str_date_time += ":0" + String.valueOf(int_minute);
        }
        else
        {
           str_date_time += ":" + String.valueOf(int_minute);
        }
        if (int_second < 10)
        {
           str_date_time += ":0" + String.valueOf(int_second);
        }
        else
        {
           str_date_time += ":" + String.valueOf(int_second);
        }
        str_date_time += "." + int_milli_second;
        return this.str_date_time;
    }
    public String getDateTime2()
    {
        Calendar m_calendar = Calendar.getInstance();
        m_calendar.setTime(new Date());
        int int_year = m_calendar.get(Calendar.YEAR);
        int int_month = m_calendar.get(Calendar.MONTH) + 1;
        int int_day = m_calendar.get(Calendar.DAY_OF_MONTH);
        int int_hour = m_calendar.get(Calendar.HOUR);
        int int_minute = m_calendar.get(Calendar.MINUTE);
        int int_second = m_calendar.get(Calendar.SECOND);
        int int_milli_second = m_calendar.get(Calendar.MILLISECOND);
        str_date_time = String.valueOf(int_year);
        if (int_month < 10)
        {
           str_date_time += "-0" + String.valueOf(int_month);
        }
        else
        {
           str_date_time += "-" + String.valueOf(int_month);
        }
        if (int_day < 10)
        {
           str_date_time += "-0" + String.valueOf(int_day);
        }
        else
        {
           str_date_time += "-" + String.valueOf(int_day);
        }
        if (int_hour < 10)
        {
           str_date_time += " 0" + String.valueOf(int_hour);
        }
        else
        {
           str_date_time += " " + String.valueOf(int_hour);
        }
        if (int_minute < 10)
        {
           str_date_time += ":0" + String.valueOf(int_minute);
        }
        else
        {
           str_date_time += ":" + String.valueOf(int_minute);
        }
        if (int_second < 10)
        {
           str_date_time += ":0" + String.valueOf(int_second);
        }
        else
        {
           str_date_time += ":" + String.valueOf(int_second);
        }
        return this.str_date_time;
    }
}
