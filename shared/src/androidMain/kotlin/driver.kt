package test

import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

lateinit var androidSqliteDriver: AndroidSqliteDriver
fun initialize(driver: AndroidSqliteDriver) {
    androidSqliteDriver = driver
}


actual val driver: SqlDriver
    get() = androidSqliteDriver