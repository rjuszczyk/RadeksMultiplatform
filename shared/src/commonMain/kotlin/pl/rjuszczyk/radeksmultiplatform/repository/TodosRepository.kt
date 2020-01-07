package pl.rjuszczyk.radeksmultiplatform.repository

import co.touchlab.firebase.firestore.*
import kotlinx.coroutines.flow.*
import pl.rjuszczyk.radeksmultiplatform.database.TodosDatabase
import pl.rjuszczyk.radeksmultiplatform.network.Todo
import pl.rjuszczyk.radeksmultiplatform.network.TodosApi

class TodosRepository (
    private val todosApi: TodosApi,
    private val todosDatabase: TodosDatabase) {

    suspend fun load() {
        val todos = todosApi.loadTodos()
//        todosDatabase.insertTodos(todos)

        todos.subList(0,1).forEach {
            getFirebaseInstance().collection("testdata").suspendAdd(
                mapOf(    "name" to it.title)
            )
            Unit
        }
    }

    val allTodos: Flow<List<Todo>?> =
        getFirebaseInstance().collection("testdata")
            .asFlow().map {
                it.documents_.map {
                    it.data_()?.get("name") as String?
                }.filterNotNull().map {
                    Todo(1,1,it,true)
                }
            }

//        todosDatabase.loadAllFlow()
//        .map { it as List<Todo>? }
//        .onStart {
//            emit(null)
//        }

}