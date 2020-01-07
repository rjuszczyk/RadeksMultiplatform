package pl.rjuszczyk.radeksmultiplatform.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.serialization.UnstableDefault
import pl.rjuszczyk.radeksmultiplatform.toCompletion

@UnstableDefault
class MainViewModel (
    private val nativeMainViewModel: NativeMainViewModel
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun observeState(completion: (NativeMainViewModel.State) -> (Unit)) = nativeMainViewModel.getStateFlow().toCompletion(coroutineScope, completion)
//    fun observeState(completion: (NativeMainViewModel.State) -> (Unit)) = completion(NativeMainViewModel.Loading())

    fun retry() {
        nativeMainViewModel.retry()
    }

    fun cancel() {
        coroutineScope.cancel()
        nativeMainViewModel.clear()
    }
}