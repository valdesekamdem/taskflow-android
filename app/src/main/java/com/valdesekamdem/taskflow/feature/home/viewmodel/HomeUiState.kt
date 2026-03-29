package com.valdesekamdem.taskflow.feature.home.viewmodel

import com.valdesekamdem.taskflow.core.model.Priority

data class HomeUiState(
    val todayDate: String,
    val tasks: List<TaskUiModel> = emptyList(),
)

data class TaskUiModel(
    val id: String,
    val title: String,
    val description: String,
    val priority: Priority,
    val category: String,
    val dueDateText: String,
)
