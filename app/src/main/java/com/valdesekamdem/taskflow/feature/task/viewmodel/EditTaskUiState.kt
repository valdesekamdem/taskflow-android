package com.valdesekamdem.taskflow.feature.task.viewmodel

import com.valdesekamdem.taskflow.core.model.Category
import com.valdesekamdem.taskflow.core.model.Priority
import kotlin.time.Instant

data class EditTaskUiState(
    val isNewTask: Boolean = true,
    val form: EditTaskFormUiModel,
    val isSubmitting: Boolean = false,
) {
    data class EditTaskFormUiModel(
        val title: String = "",
        val description: String = "",
        val category: Category = Category.Personal,
        val priority: Priority = Priority.Low,
        val dueDate: Instant? = null,
        val formattedDueDate: String = "",
    ) {
        val isFormValid: Boolean
            get() = title.isNotEmpty()
    }
}
