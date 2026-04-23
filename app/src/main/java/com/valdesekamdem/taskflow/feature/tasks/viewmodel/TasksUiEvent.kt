package com.valdesekamdem.taskflow.feature.tasks.viewmodel

import com.valdesekamdem.taskflow.ui.model.TaskUiModel

sealed interface TasksUiEvent {
    data class TaskClicked(val task: TaskUiModel) : TasksUiEvent

    data class TaskCheckboxToggled(val task: TaskUiModel) : TasksUiEvent
}
