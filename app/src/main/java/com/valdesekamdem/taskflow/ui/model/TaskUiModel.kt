package com.valdesekamdem.taskflow.ui.model

import com.valdesekamdem.taskflow.core.model.Priority

data class TaskUiModel(
    val id: Int,
    val title: String,
    val description: String,
    val priority: Priority,
    val category: String,
    val dueDateText: String,
    val isTaskOverdue: Boolean,
    val isCompleted: Boolean,
)
