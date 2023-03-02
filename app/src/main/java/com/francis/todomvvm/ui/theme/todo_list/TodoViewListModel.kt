package com.francis.todomvvm.ui.theme.todo_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.francis.todomvvm.data.Todo
import com.francis.todomvvm.data.TodoRepository
import com.francis.todomvvm.util.Routes
import com.francis.todomvvm.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewListModel @Inject constructor(
    private val repository: TodoRepository
): ViewModel(){

    val todos = repository.getTodos()

    // channel send one time event(such as snack-bar) and navigation
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    // cache the recently deleted Todo
    private var deletedTodo: Todo? = null

    fun onEvent(event: TodoListEvent){
        when(event){
            is TodoListEvent.OnTodoClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO + "?todoId=${event.todo.id}"))
            }

            is TodoListEvent.OnAddTodoClick ->{
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO))
            }
            is TodoListEvent.OnDeleteTodoClick ->{
                viewModelScope.launch {
                    deletedTodo = event.todo
                    repository.deleteTodo(event.todo)
                    sendUiEvent(UiEvent.ShowSnackbar(
                        message = "Todo deleted",
                        action = "Undo"
                    ))
                }
            }
            is TodoListEvent.OnUndoDeleteClick ->{
                deletedTodo?.let { todo ->
                    viewModelScope.launch {
                        repository.insertTodo(todo)
                    }
                }

            }
            is TodoListEvent.OnDoneChange ->{
                viewModelScope.launch {
                    repository.insertTodo(
                        event.todo.copy(
                            isDone = event.isDone
                        )
                    )
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent){
        viewModelScope.launch {
            // send to channel - send(which is a suspended function)
            _uiEvent.send(event)
        }
    }
}