package test

import com.squareup.sqldelight.ColumnAdapter
import kotlin.String

interface WeatherModel {
  val coordinate: Coordinate2?

  val base: String

  class Adapter(
    val coordinateAdapter: ColumnAdapter<Coordinate2, String>
  )

  data class Impl(
    override val coordinate: Coordinate2?,
    override val base: String
  ) : WeatherModel {
    override fun toString(): String = """
    |WeatherModel.Impl [
    |  coordinate: $coordinate
    |  base: $base
    |]
    """.trimMargin()
  }
}
