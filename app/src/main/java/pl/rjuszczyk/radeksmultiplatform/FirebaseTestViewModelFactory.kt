package pl.rjuszczyk.radeksmultiplatform

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.rjuszczyk.radeksmultiplatform.repository.FirebaseTestRepository
import pl.rjuszczyk.radeksmultiplatform.viewmodel.NativeFirebaseTestViewModel
import test.pl.rjuszczyk.radeksmultiplatform.viewmodel.FirebaseTestViewModel

class FirebaseTestViewModelFactory : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FirebaseTestViewModel(
            NativeFirebaseTestViewModel(
                FirebaseTestRepository())
        ) as T
    }
}
