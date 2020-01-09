package pl.rjuszczyk.radeksmultiplatform.database

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Long
import kotlin.String

interface TodoModelQueries : Transacter {
  fun <T : Any> selectAll(mapper: (
    id: Long,
    userId: Long,
    title: String,
    completed: Long
  ) -> T): Query<T>

  fun selectAll(): Query<TodoModel>

  fun insertItem(
    id: Long,
    userId: Long,
    title: String,
    completed: Long
  )

  fun clearItems()
}
