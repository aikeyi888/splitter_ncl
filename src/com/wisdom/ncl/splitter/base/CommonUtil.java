package com.wisdom.ncl.splitter.base;

/*
* Copyright ? 2005, 北京东方般若科技发展有限公司
* All rights reserved
*
* 文件名称：Util.java
* 摘要：    提供一些基本的操作
*
*
* 当前版本：4.0
* 作者：   马金莹
* 完成日期：2005-05-25
*
* 原有版本：1.0
* 原作者：  马金莹
* 完成日期：2003-08-10
*/

import java.io.*;

public class CommonUtil
{
    /**
     *  Field描述
     */
    public final static String PERSON = "person:";
    /**
     *  Field描述
     */
    public final static String ROLE = "role:";
    /**
     *  Field描述
     */
    public final static String UNIT = "unit:";
    /**
     *  Field描述
     */
    public final static String POST = "post:";

    private static Long serialNub;


    /**
     *  构造对象 CommonUtil
     */
    public CommonUtil() { }


    ///////////////////////////下面函数是关于String的操作的各种操作

    /**
     *  转换字符串的格式 <p>
     *
     *  例如toZn("a;dljfakdf","ISO-8859-1","GBK")</p>
     *
     *@param  source  要转换的字符串
     *@return         描述返回值信息
     */
    public static String toZn( String source )
    {
        try
        {
            if ( source != null )
            {
                return new String( source.getBytes( "ISO-8859-1" ), "GB2312" );
               //return new String( source.getBytes( "ISO-8859-1" ), "GBK" );
            } else
            {
                return "";
            }
        } catch ( java.io.UnsupportedEncodingException ex )
        {
            return source;
        }
    }
    /**
     *  对特殊字符进行转义处理
     *
     *@param  chkstr  参数描述
     *@return         描述返回值信息
     */
    public static String checkStr( String chkstr )
    {
        if ( chkstr == null || chkstr.length() == 0 )
        {
            return chkstr;
        }
        StringBuffer buf = new StringBuffer();
        char temp;
        for ( int i = 0; i < chkstr.length(); i++ )
        {
            temp = chkstr.charAt( i );
            if ( temp == '\"' )
            {
                buf.append( "&quot;" );
            } else if ( temp == '\'' )
            {
                buf.append( "&#39;" );
            } else if ( temp == '\\' )
            {
                buf.append( "&#92;" );
            } else if ( temp == '<' )
            {
                buf.append( "&#60;" );
            } else if ( temp == '>' )
            {
                buf.append( "&#62;" );
            } else if ( temp == '%' )
            {
                buf.append( "&#37;" );
            } else if ( temp == ' ' )
            {
                buf.append( "&nbsp;" );
            } else
            {
                buf.append( temp );
            }
        }
        return buf.toString();
    }
    /**
     *  对特殊字符进行转义处理(ACSII码转特殊字符)
     *
     *@param  chkstr  参数描述
     *@return         描述返回值信息
     */
    /*
    public static String ascToStr( String str_asc )
    {
        System.out.println(str_asc);
        String str_return = "";
        if ( str_asc == null || str_asc.length() == 0 )
        {
            str_return = str_asc;
        }
        if (str_asc.indexOf("&#39;")>-1)
        {
            str_return = str_asc.replaceAll("&#39;","'");
        }
        if (str_asc.indexOf("&quot;")>-1)
        {
            str_return = str_asc.replaceAll("&quot;","'\"'");
        }
         if (str_asc.indexOf("&#92;")>-1)
        {
            str_return = str_asc.replaceAll("&#92;","\\");
        }
        if (str_asc.indexOf("&#60;")>-1)
        {
            str_return = str_asc.replaceAll("&#60;",">");
        }
        if (str_asc.indexOf("&#62;")>-1)
        {
            str_return = str_asc.replaceAll("&#62;","<");
        }
        if (str_asc.indexOf("&#37;")>-1)
        {
            str_return = str_asc.replaceAll("&#37;","%");
        }
        if (str_asc.indexOf("&nbsp;")>-1)
        {
            str_return = str_asc.replaceAll("&nbsp;"," ");
        }
        else
            str_return = str_asc;
        System.out.println(str_return);
        return str_return;

    }
    */
    public static String ascToStr( String str_asc )
   {
       System.out.println(str_asc);

       if ( str_asc == null || str_asc.length() == 0 )
       {

       }
       if (str_asc.indexOf("&#39;")>-1)
       {
           str_asc = str_asc.replaceAll("&#39;","'");
       }
       if (str_asc.indexOf("&quot;")>-1)
       {
           str_asc = str_asc.replaceAll("&quot;","\"");
       }
        if (str_asc.indexOf("&#92;")>-1)
       {
           str_asc = str_asc.replaceAll("&#92;","\\");
       }
       if (str_asc.indexOf("&#60;")>-1)
       {
           str_asc = str_asc.replaceAll("&#60;",">");
       }
       if (str_asc.indexOf("&#62;")>-1)
       {
           str_asc = str_asc.replaceAll("&#62;","<");
       }
       if (str_asc.indexOf("&#37;")>-1)
       {
           str_asc = str_asc.replaceAll("&#37;","%");
       }
       if (str_asc.indexOf("&nbsp;")>-1)
       {
           str_asc = str_asc.replaceAll("&nbsp;"," ");
       }

       System.out.println(str_asc);
       return str_asc;

   }


}
