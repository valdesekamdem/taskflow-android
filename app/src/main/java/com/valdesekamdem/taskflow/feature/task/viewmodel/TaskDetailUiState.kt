package com.valdesekamdem.taskflow.feature.task.viewmodel

import com.valdesekamdem.taskflow.core.model.Category
import com.valdesekamdem.taskflow.core.model.Priority

sealed interface TaskDetailUiState {

    data class Content(
        val title: String,
        val description: String?,
        val priority: Priority,
        val dueDate: DueDate,
        val category: Category,
        val tasksInCategory: String?,
        val createdAt: String,
        val reminder: String,
        val showDeleteConfirmation: Boolean = false,
    ) : TaskDetailUiState {
        data class DueDate(
            val date: String,
            val countDown: String?,
            val isOverdue: Boolean,
        )
    }

    data object Deleted : TaskDetailUiState
}
