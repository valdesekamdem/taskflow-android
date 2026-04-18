package com.valdesekamdem.taskflow.feature.task.viewmodel

sealed interface TaskDetailUiEvent {
    data object BackClicked : TaskDetailUiEvent

    data object EditClicked : TaskDetailUiEvent

    data object DeleteClicked : TaskDetailUiEvent

    data object DeleteConfirmed : TaskDetailUiEvent

    data object DeleteCancelled : TaskDetailUiEvent

    data object GoHomeClicked : TaskDetailUiEvent
}
