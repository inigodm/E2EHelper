package db.clientWrapper

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*

class DBClientWrapper(private val dbConnectionString: String) {
    @Throws(SQLException::class)
    fun executeSql(sql: String) {
        connect()
            .use { db -> db.createStatement().use { statement -> statement.executeUpdate(sql) } }
    }

    @Throws(SQLException::class)
    fun executeQuery(sql: String) : List<Map<String, String>> {
        return connect().use { db ->
            db.createStatement().use { statement ->
                statement.executeQuery(sql).use { rs -> rsToList(rs) }
            }
        }
    }

    private fun connect() : Connection {
        return DriverManager.getConnection(dbConnectionString)?: throw RuntimeException("Unable to establish db connection to: $dbConnectionString")
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
