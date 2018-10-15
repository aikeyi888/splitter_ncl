package com.wisdom.ncl.splitter.base;

/*
* Copyright ? 2005, �������������Ƽ���չ���޹�˾
* All rights reserved
*
* �ļ����ƣ�Util.java
* ժҪ��    �ṩһЩ�����Ĳ���
*
*
* ��ǰ�汾��4.0
* ���ߣ�   ���Ө
* ������ڣ�2005-05-25
*
* ԭ�а汾��1.0
* ԭ���ߣ�  ���Ө
* ������ڣ�2003-08-10
*/

import java.io.*;

public class CommonUtil
{
    /**
     *  Field����
     */
    public final static String PERSON = "person:";
    /**
     *  Field����
     */
    public final static String ROLE = "role:";
    /**
     *  Field����
     */
    public final static String UNIT = "unit:";
    /**
     *  Field����
     */
    public final static String POST = "post:";

    private static Long serialNub;


    /**
     *  ������� CommonUtil
     */
    public CommonUtil() { }


    ///////////////////////////���溯���ǹ���String�Ĳ����ĸ��ֲ���

    /**
     *  ת���ַ����ĸ�ʽ <p>
     *
     *  ����toZn("a;dljfakdf","ISO-8859-1","GBK")</p>
     *
     *@param  source  Ҫת�����ַ���
     *@return         ��������ֵ��Ϣ
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
     *  �������ַ�����ת�崦��
     *
     *@param  chkstr  ��������
     *@return         ��������ֵ��Ϣ
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
     *  �������ַ�����ת�崦��(ACSII��ת�����ַ�)
     *
     *@param  chkstr  ��������
     *@return         ��������ֵ��Ϣ
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
