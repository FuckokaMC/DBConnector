package mc.fuckoka.dbconnector

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection

object Database {
    private lateinit var dataSource: HikariDataSource

    /**
     * Database.transaction{}の外ではnullになります
     */
    var connection: Connection? = null
        private set

    fun <T> transaction(block: () -> T): T {
        var result: T? = null

        connection = dataSource.connection
        try {
            connection?.autoCommit = false
            result = block()
            connection?.commit()
        } catch (e: Exception) {
            connection?.rollback()
        } finally {
            connection?.autoCommit = true
            connection?.close()
        }
        connection = null

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
