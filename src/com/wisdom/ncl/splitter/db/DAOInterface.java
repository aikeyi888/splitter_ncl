package com.wisdom.ncl.splitter.db;


import java.sql.Connection;

public interface DAOInterface extends Cloneable
{
    public boolean saveToDB(Connection conn);

    public boolean updateToDB(Connection conn);

    public boolean insertToDB(Connection conn);

    public boolean deleteFromDB(Connection conn);
}
