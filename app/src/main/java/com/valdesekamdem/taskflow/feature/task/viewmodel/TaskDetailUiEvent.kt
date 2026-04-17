package com.valdesekamdem.taskflow.feature.task.viewmodel

sealed interface TaskDetailUiEvent {
    data object BackClicked : TaskDetailUiEvent
}
