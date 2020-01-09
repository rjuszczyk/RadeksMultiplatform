package test.pl.rjuszczyk.radeksmultiplatform.viewmodel

import androidx.lifecycle.ViewModel
import pl.rjuszczyk.radeksmultiplatform.toLiveData
import pl.rjuszczyk.radeksmultiplatform.viewmodel.NativeFirebaseTestViewModel

class FirebaseTestViewModel (
    private val nativeFirebaseTestViewModel: NativeFirebaseTestViewModel
): ViewModel()  {

    val stateLiveData = nativeFirebaseTestViewModel.state.toLiveData()

    fun send(item: String) {
        nativeFirebaseTestViewModel.send(item)
    }

    override fun onCleared() {
        super.onCleared()
        nativeFirebaseTestViewModel.clear()
    }
}