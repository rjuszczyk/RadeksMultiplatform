package pl.rjuszczyk.radeksmultiplatform.database

import co.touchlab.sessionize.db.coroutines.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.startWith
import kotlinx.coroutines.withContext
import pl.rjuszczyk.radeksmultiplatform.network.Todo

class TodosDatabase(
    private val todoModelQueries: TodoModelQueries
) {
    val databaseDispatcher = Dispatchers.Main

    suspend fun insertTodos(todos: List<Todo>) {
        withContext(databaseDispatcher) {
            todos.forEach { todo ->
                todoModelQueries.insertItem(
                    todo.id,
                    todo.userId,
                    todo.title,
                    if(todo.completed) 1 else 0
                )
            }
        }
    }

    fun loadAllFlow(): Flow<List<Todo>> {
        return todoModelQueries
            .selectAll()
            .asFlow()
            .mapToList()
            .map { todos ->
                delay(1000)
                todos.toTodos()
            }.flowOn(databaseDispatcher)
    }

    suspend fun loadAll(): List<Todo> {
        return withContext(Dispatchers.Default) {
            todoModelQueries.selectAll().executeAsList().toTodos()
        }
    }

    fun List<TodoModel>.toTodos() = map {
        Todo(
            it.id,
            it.userId,
            it.title,
            it.completed == 1L
        )
    }

}