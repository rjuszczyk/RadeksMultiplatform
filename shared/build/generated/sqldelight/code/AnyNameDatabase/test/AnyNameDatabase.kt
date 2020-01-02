package test

import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.db.SqlDriver
import test.shared.newInstance
import test.shared.schema

interface AnyNameDatabase : Transacter {
  val weatherModelQueries: WeatherModelQueries

  companion object {
    val Schema: SqlDriver.Schema
      get() = AnyNameDatabase::class.schema

    operator fun invoke(driver: SqlDriver, WeatherModelAdapter: WeatherModel.Adapter):
        AnyNameDatabase = AnyNameDatabase::class.newInstance(driver, WeatherModelAdapter)}
}
