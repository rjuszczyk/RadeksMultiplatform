package test

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.String

interface WeatherModelQueries : Transacter {
  fun <T : Any> selectAll(mapper: (coordinate: Coordinate2?, base: String) -> T): Query<T>

  fun selectAll(): Query<WeatherModel>

  fun insertItem(coordinate: Coordinate2?, base: String)
}
