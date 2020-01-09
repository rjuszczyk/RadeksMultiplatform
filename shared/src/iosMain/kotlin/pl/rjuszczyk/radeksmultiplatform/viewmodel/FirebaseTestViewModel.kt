package test.pl.rjuszczyk.radeksmultiplatform.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.serialization.UnstableDefault
import pl.rjuszczyk.radeksmultiplatform.toCompletion
import pl.rjuszczyk.radeksmultiplatform.viewmodel.NativeFirebaseTestViewModel

@UnstableDefault
class FirebaseTestViewModel (
    private val nativeFirebaseTestViewModel: NativeFirebaseTestViewModel
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun observeState(completion: (NativeFirebaseTestViewModel.State) -> (Unit)) = nativeFirebaseTestViewModel.state.toCompletion(coroutineScope, completion)

    fun send(item: String) {
        nativeFirebaseTestViewModel.send(item)
    }

    fun cancel() {
        coroutineScope.cancel()
        nativeFirebaseTestViewModel.clear()
    }
}