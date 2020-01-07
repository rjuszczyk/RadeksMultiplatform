package pl.rjuszczyk.radeksmultiplatform.viewmodel

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.serialization.UnstableDefault
import pl.rjuszczyk.radeksmultiplatform.MutableFlow
import pl.rjuszczyk.radeksmultiplatform.network.Todo
import pl.rjuszczyk.radeksmultiplatform.repository.TodosRepository

@FlowPreview
@ExperimentalCoroutinesApi
@UnstableDefault
class NativeMainViewModel(
    private val todosRepository: TodosRepository
) {
    private val mutableStateFlow =
        MutableFlow<LoadingState>(LoadingInProgress)
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun getStateFlow(): Flow<State> = mutableStateFlow.combine(todosRepository.allTodos) { loadingState, data ->
        when(loadingState) {
            is LoadingInProgress ->Loading(data)
            is LoadingSucceed -> data?.let { Loaded(it) }?: Loading()
            is LoadingFailed -> Failed(loadingState.errorMessage, data)
            else -> throw IllegalStateException("impossible loadingState")
        }
    }

    init {
        performLoading()
    }

    private fun performLoading() {
        coroutineScope.launch {
            mutableStateFlow.postValue(LoadingInProgress)
            delay(2000)
            try {
                todosRepository.load()
                delay(2000)
                mutableStateFlow.postValue(LoadingSucceed)
            } catch (throwable: Throwable) {
                delay(2000)
                mutableStateFlow.postValue(LoadingFailed(throwable.message?:throwable.toString()))
            }
        }
    }

    fun clear() {
        coroutineScope.cancel()
    }

    fun retry() {
        performLoading()
    }

    interface LoadingState
    object LoadingInProgress: LoadingState
    data class LoadingFailed(val errorMessage: String): LoadingState
    object LoadingSucceed: LoadingState

    interface State
    data class Loading(val cachedData: List<Todo>? = null): State
    data class Failed(val errorMessage: String, val cachedData: List<Todo>? = null): State
    data class Loaded(val data: List<Todo>): State
}