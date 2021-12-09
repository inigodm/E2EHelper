package db

import properties.PropertyLoader
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*

class DBAssertion {
    fun `when`(sql: String) {
        DBConnection().executeSql(sql)
    }
}

class DBConnection {
    val properties: Properties = PropertyLoader().load()
    val BD_CONNECTION_URL = properties["sqlite.url"].toString()

    fun connect(url: String): Connection? {
        try {
            // create a connection to the database
            println("Connecting to SQLite.")
            return DriverManager.getConnection(url)
        } catch (e: SQLException) {
            println(e.message)
            return null
        }
    }

    @Throws(SQLException::class)
    fun executeSql(sql: String?) {
        connect(BD_CONNECTION_URL)
            .use {
                    db -> db?.createStatement().use {
                    statement -> statement?.executeUpdate(sql) }
            }
    }

    @Throws(SQLException::class)
    fun executeQuery(sql: String?) {
        var res = mutableListOf<MutableMap<String, String>>()
        var columns = mutableSetOf<String?>()
        var auxMap : MutableMap<String, String> = mutableMapOf()
        connect(BD_CONNECTION_URL)
            .use {
                    db -> db?.createStatement().use {
                    statement -> statement?.executeQuery(sql).use {
                            val number = it?.metaData?.columnCount ?: 0
                            for (i in 0..number) {
                                columns.add(it?.metaData?.getColumnName(i))
                            }
                            while (it?.next() == true){
                                auxMap = mutableMapOf()
                                columns.map { column ->
                                    auxMap.put(column!!, it.getString(column))
                                }
                            }
                            res.add(auxMap)
                        }
                    }
            }
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

