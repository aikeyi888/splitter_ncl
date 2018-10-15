package com.wisdom.ncl.splitter.db;


import java.util.ArrayList;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
class DBKeyword
{
  public ArrayList m_keyword_list = new ArrayList();
  public DBKeyword()
  {
    init();
  }

  public void init()
  {
    addKeyword("ALL");
    addKeyword("ADD");
    addKeyword("ALTER");
    addKeyword("AND");
    addKeyword("ANY");
    addKeyword("AS");
    addKeyword("ASC");
    addKeyword("AUTHORIZATION");
    addKeyword("BACKUP");
    addKeyword("BEGIN");
    addKeyword("BETWEEN");
    addKeyword("BREAK");
    addKeyword("BROWSE");

    addKeyword("BULK");
    addKeyword("BY");
    addKeyword("CASCADE");
    addKeyword("CASE");
    addKeyword("CHECK");
    addKeyword("CHECKPOINT");
    addKeyword("CLOSE");
    addKeyword("CLUSTERED");
    addKeyword("COALESCE");
    addKeyword("COLLATE");
    addKeyword("COLUMN");
    addKeyword("COMMIT");
    addKeyword("COMPUTE");
    addKeyword("CONSTRAINT");
    addKeyword("CONTAINS");
    addKeyword("CONTAINSTABLE");
    addKeyword("CONTINUE");
    addKeyword("CONVERT");
    addKeyword("CREATE");
    addKeyword("CROSS");
    addKeyword("CURRENT");
    addKeyword("CURRENT_DATE");
    addKeyword("CURRENT_TIME");
    addKeyword("CURRENT_TIMESTAMP");
    addKeyword("CURRENT_USER");
    addKeyword("CURSOR");
    addKeyword("DATABASE");
    addKeyword("DBCC");
    addKeyword("DEALLOCATE");
    addKeyword("DECLARE");
    addKeyword("DEFAULT");
    addKeyword("DELETE");
    addKeyword("DENY");
    addKeyword("DESC");
    addKeyword("DISK");
    addKeyword("DISTINCT");
    addKeyword("DISTRIBUTED");
    addKeyword("DOUBLE");
    addKeyword("DROP");
    addKeyword("DUMMY");
    addKeyword("DUMP");

    /**
     ELSE
     END
     ERRLVL
     ESCAPE
     EXCEPT
     EXEC
     EXECUTE
     EXISTS
     EXIT
     FETCH
     FILE
     FILLFACTOR
     FOR
     FOREIGN
     FREETEXT
     FREETEXTTABLE
     FROM
     FULL
     FUNCTION
     GOTO
     GRANT
     GROUP
     HAVING
     HOLDLOCK
     IDENTITY
     IDENTITY_INSERT
     IDENTITYCOL
     IF
     IN
     INDEX
     INNER
     INSERT
     INTERSECT
     INTO
     IS
     JOIN
     KEY
     KILL
     LEFT
     LIKE
     LINENO
     LOAD
     NATIONAL
     NOCHECK
     NONCLUSTERED
     NOT
     NULL
     NULLIF
     OF
     OFF
     OFFSETS
     ON
     OPEN
     OPENDATASOURCE
     OPENQUERY
     OPENROWSET
     OPENXML
     OPTION
     OR
     ORDER
     OUTER
     OVER
     PERCENT
     PLAN
     PRECISION
     PRIMARY
     PRINT
     PROC
     PROCEDURE
     PUBLIC
     RAISERROR
     READ
     READTEXT
     RECONFIGURE
     REFERENCES
     REPLICATION
     RESTORE
     RESTRICT
     RETURN
     REVOKE
     RIGHT
     ROLLBACK
     ROWCOUNT
     ROWGUIDCOL
     RULE
     SAVE
     SCHEMA
     SELECT
     SESSION_USER
     SET
     SETUSER
     SHUTDOWN
     SOME
     STATISTICS
     SYSTEM_USER
     TABLE
     TEXTSIZE
     THEN
     TO
     TOP
     TRAN
     TRANSACTION
     TRIGGER
     TRUNCATE
     TSEQUAL
     UNION
     UNIQUE
     UPDATE
     UPDATETEXT
     USE
     USER
     VALUES
     VARYING
     VIEW
     WAITFOR
     WHEN
     WHERE
     WHILE
     WITH
     WRITETEXT
     */
    }

  public void addKeyword(String keyword)
  {
    if (keyword == null || keyword.length() ==0)
      return;

    m_keyword_list.add(keyword.toLowerCase());
  }

  public boolean exists(String keyword)
  {
    if (keyword == null)
      return false;
    return m_keyword_list.contains(keyword.toLowerCase());
  }

  public boolean isKeyword(String keyword)
  {
    return exists(keyword);
  }




}
