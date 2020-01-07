package pl.rjuszczyk.radeksmultiplatform

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.broadcast
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
@FlowPreview
class MutableFlow<T>(initialValue: T? = null): Flow<T> {

    private val scope = CoroutineScope(Dispatchers.Main)
    private var lastValue: T? = initialValue
    private val valueChannel = Channel<T>()

    private val valueFlow = scope.broadcast {
        for(value in valueChannel) {
            lastValue = value
            send(value)
        }
    }.asFlow().onStart {
        lastValue?.let { emit(it) }
    }

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<T>) {
        valueFlow.collect(collector)
    }

    fun postValue(value: T) {
        if (!valueChannel.offer(value)) {
            lastValue = value
        }
    }
}