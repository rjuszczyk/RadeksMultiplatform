package pl.rjuszczyk.radeksmultiplatform

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun <T> Flow<T>.toCompletion(coroutineScope: CoroutineScope, completion: (T) -> (Unit)) {
    coroutineScope.launch { collect { value -> completion(value) } }
}