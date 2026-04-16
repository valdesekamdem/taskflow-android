package com.valdesekamdem.taskflow.feature.task.viewmodel

import com.valdesekamdem.taskflow.core.model.Category
import com.valdesekamdem.taskflow.core.model.Priority

data class EditTaskUiState(
    val title: String,
    val form: EditTaskForm,
    val isSubmitting: Boolean = false,
) {
    data class EditTaskForm(
        val title: String = "",
        val description: String = "",
        val category: Category = Category.Personal,
        val priority: Priority = Priority.Low,
//        val dueDate: Date,
    ) {
        val isFormValid: Boolean
            get() = title.isNotEmpty()
    }
}
