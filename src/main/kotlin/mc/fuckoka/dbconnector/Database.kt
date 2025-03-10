package mc.fuckoka.dbconnector

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection

object Database {
    @PublishedApi
    internal lateinit var dataSource: HikariDataSource

    @PublishedApi
    internal var connection: Connection? = null

    /**
     * コネクション取得
     * transactionスコープ外ではnull
     *
     * @return
     */
    fun getConnection(): Connection? = connection

    inline fun <T> transaction(statement: () -> T): T {
        var result: T? = null

        connection = dataSource.connection
        try {
            connection?.autoCommit = false
            result = statement()
            connection?.commit()
        } catch (e: Exception) {
            connection?.rollback()
        } finally {
            connection?.autoCommit = true
            connection?.close()
            connection = null
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
