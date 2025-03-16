# 使い方
```kotlin
import mc.fuckoka.dbconnector.Database

Database.transaction {
    val connection = Database.getConnection() // java.sql.Connection
    // ~~~
}
```
