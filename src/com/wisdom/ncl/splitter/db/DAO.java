package com.wisdom.ncl.splitter.db;

import java.sql.Connection;

import com.wisdom.ncl.splitter.db.sql.SQLWhere;

public class DAO extends SQLWhere implements DAOInterface
{
    public DAO()
    {
    }

    public final boolean saveToDB(Connection conn)
    {
        if (updateToDB(conn))
            return true;

        if (insertToDB(conn))
            return true;
        else
            return false;
    }

    public boolean updateToDB(Connection conn)
    {
        return false;
    }

    public boolean insertToDB(Connection conn)
    {
        return false;
    }

    public boolean deleteFromDB(Connection conn)
    {
        return false;
    }
}
