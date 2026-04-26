package com.valdesekamdem.taskflow.feature.home.viewmodel

import com.valdesekamdem.taskflow.ui.model.TaskUiModel

data class HomeUiState(
    val todayDate: String,
    val overdueTasks: List<TaskUiModel> = emptyList(),
)
