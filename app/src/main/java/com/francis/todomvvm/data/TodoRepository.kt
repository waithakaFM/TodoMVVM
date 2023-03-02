package com.francis.todomvvm.data

import androidx.room.Delete
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

interface TodoRepository  {

    suspend fun insertTodo(todo: Todo)

    suspend fun deleteTodo(todo: Todo)

    suspend fun getTodoById(id: Int): Todo?

    fun getTodos(): Flow<List<Todo>>
}