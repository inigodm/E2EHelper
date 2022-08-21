package db

import db.clientWrapper.DBClientWrapper
import java.util.*

class DBSetup(val inner: DBClientWrapper) {
    constructor(dbConnectionString: String) : this(DBClientWrapper(dbConnectionString))
    fun given(sql: String): DBSetup {
        inner.executeSql(sql)
        return this
    }

    fun givenEmptyTable(tableName: String) : DBSetup{
        inner.executeSql("Delete from $tableName")
        return this
    }

    fun `when`(sql: String): DBResponse {
        return DBResponse(inner.executeQuery(sql))
    }
}
