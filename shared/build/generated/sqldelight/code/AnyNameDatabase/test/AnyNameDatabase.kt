package test

import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.db.SqlDriver
import pl.rjuszczyk.radeksmultiplatform.database.TodoModelQueries
import test.shared.newInstance
import test.shared.schema

interface AnyNameDatabase : Transacter {
  val todoModelQueries: TodoModelQueries

  companion object {
    val Schema: SqlDriver.Schema
      get() = AnyNameDatabase::class.schema

    operator fun invoke(driver: SqlDriver): AnyNameDatabase =
        AnyNameDatabase::class.newInstance(driver)}
}
