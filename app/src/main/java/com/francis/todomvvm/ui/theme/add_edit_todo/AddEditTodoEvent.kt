package com.francis.todomvvm.ui.theme.add_edit_todo

/**
 * Events that the user can do
 */
sealed class AddEditTodoEvent{
    data class OnTitleChange(val title: String): AddEditTodoEvent()
    data class OnDescriptionChange(val description: String): AddEditTodoEvent()
    object OnSaveTodoClick: AddEditTodoEvent()
}