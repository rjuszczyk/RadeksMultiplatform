package test

import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.db.SqlDriver
import kotlinx.serialization.Serializable

expect val driver: SqlDriver

fun createDatabase(): AnyNameDatabase {
    val coordinateAdapter = object : ColumnAdapter<Coordinate2, String> {
        override fun decode(databaseValue: String): Coordinate2 {
            val split = databaseValue.split(":")
            return Coordinate2(split[0].toFloat(), split[1].toFloat())
        }

        override fun encode(value: Coordinate2): String {
            return "${value.lat}:${value.lon}"
        }
    }

    return AnyNameDatabase(
        driver,
        WeatherModel.Adapter(
            coordinateAdapter = coordinateAdapter
        )
    )
}


@Serializable
data class Weather(val coord: Coordinate2, val base: String)
@Serializable
data class Coordinate2(val lon: Float, val lat: Float)