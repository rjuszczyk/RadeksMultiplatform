package pl.rjuszczyk.radeksmultiplatform

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun <T> Flow<T>.toLiveData(): LiveData<T> {
     return object: LiveData<T>() {
        var job: Job? = null
        override fun onActive() {
            job = CoroutineScope(Dispatchers.Main).launch {
                collect{
                    value = it
                }
            }
        }

        override fun onInactive() {
            job?.cancel()
        }
    }
}