package db

import db.clientWrapper.DBClientWrapper

class E2EDB(val inner: DBClientWrapper) {
    constructor(dbConnectionString: String) : this(DBClientWrapper(dbConnectionString))
    fun given(sql: String): E2EDB {
        inner.executeSql(sql)
        return this
    }

    fun givenEmptyTable(tableName: String) : E2EDB{
        inner.executeSql("Delete from $tableName")
        return this
    }

    fun doQuery(sql: String): E2EDBResponse {
        return E2EDBResponse(inner.executeQuery(sql))
    }
}
