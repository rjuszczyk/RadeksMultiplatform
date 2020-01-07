package pl.rjuszczyk.radeksmultiplatform.database

import kotlin.Long
import kotlin.String

interface TodoModel {
  val id: Long

  val userId: Long

  val title: String

  val completed: Long

  data class Impl(
    override val id: Long,
    override val userId: Long,
    override val title: String,
    override val completed: Long
  ) : TodoModel {
    override fun toString(): String = """
    |TodoModel.Impl [
    |  id: $id
    |  userId: $userId
    |  title: $title
    |  completed: $completed
    |]
    """.trimMargin()
  }
}
