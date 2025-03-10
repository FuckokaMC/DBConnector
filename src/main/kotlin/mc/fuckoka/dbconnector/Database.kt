package mc.fuckoka.dbconnector

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection

object Database {
    @PublishedApi
    internal lateinit var dataSource: HikariDataSource

    inline fun <T> transaction(statement: Connection.() -> T): T {
        var result: T? = null

        val connection = dataSource.connection
        try {
            connection?.autoCommit = false
            result = statement(connection)
            connection?.commit()
        } catch (e: Exception) {
            connection?.rollback()
        } finally {
            connection?.autoCommit = true
            connection?.close()
        }
        return result!!
    }

    internal fun connect(url: String, driver: String = "", user: String = "", password: String = "") {
        val config = HikariConfig()
        config.jdbcUrl = url
        config.driverClassName = driver
        config.username = user
        config.password = password
        dataSource = HikariDataSource(config)
    }

    internal fun close() {
        dataSource.close()
    }
}
