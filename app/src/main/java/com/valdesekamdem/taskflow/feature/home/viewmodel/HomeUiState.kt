package com.valdesekamdem.taskflow.feature.home.viewmodel

import com.valdesekamdem.taskflow.ui.model.TaskUiModel

data class HomeUiState(
    val todayDate: String,
    val overdueTasks: List<TaskUiModel> = emptyList(),
    val todayTasks: List<TaskUiModel> = emptyList(),
    val isOverdueTasksExpanded: Boolean = false,
    val isTodayTasksExpanded: Boolean = false,
    val maxVisibleTasks: Int,
)
