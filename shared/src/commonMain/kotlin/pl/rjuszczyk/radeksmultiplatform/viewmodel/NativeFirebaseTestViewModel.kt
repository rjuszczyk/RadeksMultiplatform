package pl.rjuszczyk.radeksmultiplatform.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import pl.rjuszczyk.radeksmultiplatform.repository.FirebaseTestRepository

class NativeFirebaseTestViewModel(
    private val firebaseTestRepository: FirebaseTestRepository
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    val state: Flow<State>
        get() = firebaseTestRepository.allItems.map {
            it?.let {
                Loaded(it)
            }?:run {
                Loading
            }
        }

    fun send(item: String) {
        coroutineScope.launch {
            firebaseTestRepository.addItem(item)
        }
    }

    interface State
    object Loading: State
    data class Loaded(val items: List<String>): State

    fun clear() {
        coroutineScope.cancel()
    }
}