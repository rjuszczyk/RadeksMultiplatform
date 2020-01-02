package pl.rjuszczyk.radeksmultiplatform

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun testHello2(callback: (String)->(Unit)) {
    CoroutineScope(Dispatchers.Main).launch {
        callback("Loading 3")
        delay(1000)
        callback("Loading 2")
        delay(1000)
        callback("Loading 1")
        delay(1000)
        callback("Loading 0")
        delay(1000)
        callback("Hello from kotlin native")
    }
}