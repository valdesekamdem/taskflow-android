package com.valdesekamdem.taskflow.feature.home.viewmodel

sealed interface HomeUiEvent {
    data class TaskClicked(val task: TaskUiModel) : HomeUiEvent

    data object NewTaskClicked : HomeUiEvent
}
