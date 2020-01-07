package test

import com.squareup.sqldelight.db.SqlDriver

expect val driver: SqlDriver

fun createDatabase(): AnyNameDatabase {

    return AnyNameDatabase(
        driver
    )
}