package test.shared

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.TransacterImpl
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.internal.copyOnWriteList
import kotlin.Any
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.MutableList
import kotlin.reflect.KClass
import pl.rjuszczyk.radeksmultiplatform.database.TodoModel
import pl.rjuszczyk.radeksmultiplatform.database.TodoModelQueries
import test.AnyNameDatabase

internal val KClass<AnyNameDatabase>.schema: SqlDriver.Schema
  get() = AnyNameDatabaseImpl.Schema

internal fun KClass<AnyNameDatabase>.newInstance(driver: SqlDriver): AnyNameDatabase =
    AnyNameDatabaseImpl(driver)

private class AnyNameDatabaseImpl(
  driver: SqlDriver
) : TransacterImpl(driver), AnyNameDatabase {
  override val todoModelQueries: TodoModelQueriesImpl = TodoModelQueriesImpl(this, driver)

  object Schema : SqlDriver.Schema {
    override val version: Int
      get() = 1

    override fun create(driver: SqlDriver) {
      driver.execute(null, """
          |CREATE TABLE TodoModel (
          |    id INTEGER NOT NULL,
          |    userId INTEGER NOT NULL,
          |    title TEXT NOT NULL,
          |    completed INTEGER NOT NULL
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

private class TodoModelQueriesImpl(
  private val database: AnyNameDatabaseImpl,
  private val driver: SqlDriver
) : TransacterImpl(driver), TodoModelQueries {
  internal val selectAll: MutableList<Query<*>> = copyOnWriteList()

  override fun <T : Any> selectAll(mapper: (
    id: Long,
    userId: Long,
    title: String,
    completed: Long
  ) -> T): Query<T> = Query(-431734490, selectAll, driver, "TodoModel.sq", "selectAll", """
  |SELECT *
  |FROM TodoModel
  """.trimMargin()) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getString(2)!!,
      cursor.getLong(3)!!
    )
  }

  override fun selectAll(): Query<TodoModel> = selectAll(TodoModel::Impl)

  override fun insertItem(
    id: Long,
    userId: Long,
    title: String,
    completed: Long
  ) {
    driver.execute(2123500395,
        """INSERT OR FAIL INTO TodoModel(id, userId, title, completed) VALUES (?1, ?2, ?3, ?4)""",
        4) {
      bindLong(1, id)
      bindLong(2, userId)
      bindString(3, title)
      bindLong(4, completed)
    }
    notifyQueries(2123500395, {database.todoModelQueries.selectAll})
  }

  override fun clearItems() {
    driver.execute(404522866, """DELETE FROM TodoModel""", 0)
    notifyQueries(404522866, {database.todoModelQueries.selectAll})
  }
}
