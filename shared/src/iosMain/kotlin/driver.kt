package test

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.ios.NativeSqliteDriver

actual val driver: SqlDriver = NativeSqliteDriver(AnyNameDatabase.Schema, "galwaybus.db")