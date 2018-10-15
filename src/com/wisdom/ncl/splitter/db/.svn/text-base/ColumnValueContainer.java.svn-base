package com.wisdom.ncl.splitter.db;


import java.io.*;

public class ColumnValueContainer implements Cloneable, Serializable
{
    String m_value;

    public ColumnValueContainer(String value)
    {
        m_value = value;
    }

    public void setValue(String value)
    {
        m_value = value;
    }

    public String getValue()
    {
        return m_value;
    }

    public Object clone()
    {
        try
        {
           return (ColumnValueContainer)super.clone();
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
