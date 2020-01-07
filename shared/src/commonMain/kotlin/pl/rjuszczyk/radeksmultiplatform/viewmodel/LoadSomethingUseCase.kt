package pl.rjuszczyk.radeksmultiplatform.viewmodel

import kotlinx.serialization.UnstableDefault
import pl.rjuszczyk.radeksmultiplatform.network.Todo
import pl.rjuszczyk.radeksmultiplatform.network.TodosApi

@UnstableDefault
class LoadSomethingUseCase(private val todosApi: TodosApi) {
    suspend fun loadSomething(): List<Todo> {
        return todosApi.loadTodos()
    }
}