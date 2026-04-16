package com.valdesekamdem.taskflow.feature.task.viewmodel

import com.valdesekamdem.taskflow.core.model.Priority

data class EditTaskUiState(
    val title: String,
    val form: EditTaskForm,
    val isSubmitting: Boolean = false,
) {
    data class EditTaskForm(
        val title: String = "",
        val description: String = "",
        val priority: Priority = Priority.Low,
//        val dueDate: Date,
//        val category: String,
    ) {
        val isFormValid: Boolean
            get() = title.isNotEmpty()
    }
}
