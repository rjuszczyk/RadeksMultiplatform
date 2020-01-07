package pl.rjuszczyk.radeksmultiplatform.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.serialization.UnstableDefault
import pl.rjuszczyk.radeksmultiplatform.toLiveData

@UnstableDefault
class MainViewModel(
    private val nativeMainViewModel: NativeMainViewModel
): ViewModel() {

    fun getStateLiveData() = nativeMainViewModel.getStateFlow().toLiveData()

    fun retry() {
        nativeMainViewModel.retry()
    }

    override fun onCleared() {
        super.onCleared()
        nativeMainViewModel.clear()
    }
}