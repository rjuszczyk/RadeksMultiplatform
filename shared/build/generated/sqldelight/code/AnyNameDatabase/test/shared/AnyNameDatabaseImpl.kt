package test.shared

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.TransacterImpl
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.internal.copyOnWriteList
import kotlin.Any
import kotlin.Int
import kotlin.String
import kotlin.collections.MutableList
import kotlin.reflect.KClass
import test.AnyNameDatabase
import test.Coordinate2
import test.WeatherModel
import test.WeatherModelQueries

internal val KClass<AnyNameDatabase>.schema: SqlDriver.Schema
  get() = AnyNameDatabaseImpl.Schema

internal fun KClass<AnyNameDatabase>.newInstance(driver: SqlDriver,
    WeatherModelAdapter: WeatherModel.Adapter): AnyNameDatabase = AnyNameDatabaseImpl(driver,
    WeatherModelAdapter)

private class AnyNameDatabaseImpl(
  driver: SqlDriver,
  internal val WeatherModelAdapter: WeatherModel.Adapter
) : TransacterImpl(driver), AnyNameDatabase {
  override val weatherModelQueries: WeatherModelQueriesImpl = WeatherModelQueriesImpl(this, driver)

  object Schema : SqlDriver.Schema {
    override val version: Int
      get() = 1

    override fun create(driver: SqlDriver) {
      driver.execute(null, """
          |CREATE TABLE WeatherModel (
          |    coordinate TEXT,
          |    base TEXT NOT NULL
          |)
          """.trimMargin(), 0)
    }

    override fun migrate(
      driver: SqlDriver,
      oldVersion: Int,
      newVersion: Int
    ) {
    }
  }
}

private class WeatherModelQueriesImpl(
  private val database: AnyNameDatabaseImpl,
  private val driver: SqlDriver
) : TransacterImpl(driver), WeatherModelQueries {
  internal val selectAll: MutableList<Query<*>> = copyOnWriteList()

  override fun <T : Any> selectAll(mapper: (coordinate: Coordinate2?, base: String) -> T): Query<T>
      = Query(-1657442918, selectAll, driver, "WeatherModel.sq", "selectAll", """
  |SELECT *
  |FROM WeatherModel
  """.trimMargin()) { cursor ->
    mapper(
      cursor.getString(0)?.let(database.WeatherModelAdapter.coordinateAdapter::decode),
      cursor.getString(1)!!
    )
  }

  override fun selectAll(): Query<WeatherModel> = selectAll(WeatherModel::Impl)

  override fun insertItem(coordinate: Coordinate2?, base: String) {
    driver.execute(-1513722505,
        """INSERT OR FAIL INTO WeatherModel(coordinate, base) VALUES (?1, ?2)""", 2) {
      bindString(1, if (coordinate == null) null else
          database.WeatherModelAdapter.coordinateAdapter.encode(coordinate))
      bindString(2, base)
    }
    notifyQueries(-1513722505, {database.weatherModelQueries.selectAll})
  }
}
