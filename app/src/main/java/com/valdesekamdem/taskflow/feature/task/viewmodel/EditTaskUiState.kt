package com.valdesekamdem.taskflow.feature.task.viewmodel

data class EditTaskUiState(
    val title: String,
    val form: EditTaskForm,
    val isSubmitting: Boolean = false,
) {
    data class EditTaskForm(
        val title: String = "",
        val description: String = "",
//        val priority: Priority,
//        val dueDate: Date,
//        val category: String,
    )
}
