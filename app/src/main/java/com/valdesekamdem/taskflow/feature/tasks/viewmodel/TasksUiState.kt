package com.valdesekamdem.taskflow.feature.tasks.viewmodel

import com.valdesekamdem.taskflow.ui.model.TaskUiModel

data class TasksUiState(
    val tasks: List<TaskUiModel> = emptyList(),
)
