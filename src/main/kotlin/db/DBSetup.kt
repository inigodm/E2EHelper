package db

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*

class DBSetup(val dbConnectionString: String) {

    fun given(sql: String): DBSetup {
        executeSql(sql)
        return this
    }

    fun givenEmptyTable(tableName: String) : DBSetup{
        executeSql("Delete from ${tableName}")
        return this
    }

    fun `when`(sql: String): DBResponse {
        return DBResponse(executeQuery(sql))
    }

    fun connect() : Connection {
        return DriverManager.getConnection(dbConnectionString)?: throw RuntimeException("Unable to stablish db connection to: $dbConnectionString")
    }

    @Throws(SQLException::class)
    private fun executeSql(sql: String) {
        connect()
            .use { db -> db.createStatement().use { statement -> statement.executeUpdate(sql) } }
    }

    @Throws(SQLException::class)
    private fun executeQuery(sql: String) : List<Map<String, String>> {
        return connect().use { db ->
            db.createStatement().use { statement ->
                statement.executeQuery(sql).use { rs -> rsToList(rs) }
            }
        }
    }

    @Throws(SQLException::class)
    private fun rsToList(rs: ResultSet): List<Map<String, String>> {
        val md = rs.metaData
        val columns = md.columnCount
        val results: MutableList<Map<String, String>> = ArrayList()
        while (rs.next()) {
            val row: MutableMap<String, String> = HashMap()
            for (i in 1..columns) {
                row[md.getColumnLabel(i).uppercase(Locale.getDefault())] = rs.getObject(i) as String
            }
            results.add(row)
        }
        return results
    }

}

inline fun <T : AutoCloseable?, R> T.use(block: (T) -> R): R {
    var exception: Throwable? = null
    try {
        return block(this)
    } catch (e: Throwable) {
        exception = e
        throw e
    } finally {
        when {
            this == null -> {}
            exception == null -> close()
            else ->
                try {
                    close()
                } catch (closeException: Throwable) {
                    // cause.addSuppressed(closeException) // ignored here
                }
        }
    }
}
