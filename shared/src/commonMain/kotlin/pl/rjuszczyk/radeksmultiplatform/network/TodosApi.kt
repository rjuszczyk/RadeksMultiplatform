package pl.rjuszczyk.radeksmultiplatform.network

import io.ktor.client.request.get
import kotlinx.serialization.UnstableDefault
import pl.rjuszczyk.radeksmultiplatform.createHttpClient

@UnstableDefault
class TodosApi {

    suspend fun loadTodos(): List<Todo> {
        val client = createHttpClient()
        val todos = client.get<List<Todo>>("https://jsonplaceholder.typicode.com/todos")
        client.close()
        return todos
    }
}