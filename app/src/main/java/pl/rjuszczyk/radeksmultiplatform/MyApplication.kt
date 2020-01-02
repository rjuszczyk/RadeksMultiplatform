package pl.rjuszczyk.radeksmultiplatform

import android.app.Application
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import com.squareup.sqldelight.android.AndroidSqliteDriver
import test.AnyNameDatabase
import test.initialize

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeDriver()
    }

    private fun initializeDriver() {
        val config = SupportSQLiteOpenHelper.Configuration.builder(this)
            .name("database.db")
            .callback(object : SupportSQLiteOpenHelper.Callback(1) {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    val driver = AndroidSqliteDriver(db)
                    AnyNameDatabase.Schema.create(driver)
                }

                override fun onUpgrade(
                    db: SupportSQLiteDatabase?,
                    oldVersion: Int,
                    newVersion: Int
                ) {
                }

            })
            .build()

        val sqlHelper = FrameworkSQLiteOpenHelperFactory().create(config)

        initialize(AndroidSqliteDriver(sqlHelper))
    }
}