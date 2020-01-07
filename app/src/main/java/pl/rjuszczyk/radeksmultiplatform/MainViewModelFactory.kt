package pl.rjuszczyk.radeksmultiplatform

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.rjuszczyk.radeksmultiplatform.database.TodosDatabase
import pl.rjuszczyk.radeksmultiplatform.network.TodosApi
import pl.rjuszczyk.radeksmultiplatform.repository.TodosRepository
import pl.rjuszczyk.radeksmultiplatform.viewmodel.NativeMainViewModel
import test.createDatabase
import pl.rjuszczyk.radeksmultiplatform.viewmodel.MainViewModel

class MainViewModelFactory : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(
            NativeMainViewModel(
                TodosRepository(
                    TodosApi(),
                    TodosDatabase(createDatabase().todoModelQueries)
                    ))) as T
    }
}
