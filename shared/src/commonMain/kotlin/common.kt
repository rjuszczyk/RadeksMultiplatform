package pl.rjuszczyk.radeksmultiplatform

import co.touchlab.sessionize.db.coroutines.asFlow
import co.touchlab.sessionize.db.coroutines.mapToList
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import loadSomething
import test.Coordinate2
import test.WeatherModelQueries
import test.createDatabase

fun testHello(callback: (String)->(Unit)): Cancelable {
    return CoroutineScope(Dispatchers.Main).launch {
        callback("Loading 3")
        delay(1000)
        callback("Loading 2")
        delay(1000)
        callback("Loading 1")
        delay(1000)
        callback("Loading 0")
        delay(1000)
        callback("Hello from kotlin native")

        delay(1000)
        callback("I will do network call now...")
        delay(1000)
        val result = loadSomething()
        callback("this is result from network call: $result")
        delay(5000)

        weatherModelQueries
            .selectAll()
            .asFlow()
            .mapToList()
            .collect { weatherList ->
                val list = weatherList.map { it.base }
                if(list.isEmpty()) {
                    callback("empty")
                } else {
                    callback(list.reduce { acc, s -> "$acc $s" })
                }
            }
    }.let { job ->
        object : Cancelable {
            override fun cancel() {
                job.cancel(null)
            }

        }
    }
}

interface Cancelable {
    fun cancel()
}

var i = 0

fun insert() {
    i++
    weatherModelQueries
        .insertItem(Coordinate2(0f, 0f), "$i")
}
val weatherModelQueries: WeatherModelQueries by lazy {
    createDatabase().weatherModelQueries
}