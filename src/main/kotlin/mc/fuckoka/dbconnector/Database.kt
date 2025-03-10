package mc.fuckoka.dbconnector

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection

object Database {
    @PublishedApi
    internal lateinit var dataSource: HikariDataSource

    @PublishedApi
    internal var conn: Connection? = null

    /**
     * コネクション取得
     * transactionスコープ外ではnull
     *
     * @return
     */
    fun getConnection(): Connection? = conn

    inline fun <T> transaction(statement: () -> T): T {
        var result: T? = null

        conn = dataSource.connection
        try {
            conn?.autoCommit = false
            result = statement()
            conn?.commit()
        } catch (e: Exception) {
            conn?.rollback()
        } finally {
            conn?.autoCommit = true
            conn?.close()
            conn = null
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
