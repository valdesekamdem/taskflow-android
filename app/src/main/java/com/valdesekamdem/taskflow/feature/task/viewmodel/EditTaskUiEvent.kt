package com.valdesekamdem.taskflow.feature.task.viewmodel

sealed interface EditTaskUiEvent {
    data class OnTitleChanged(val title: String) : EditTaskUiEvent

    data class OnDescriptionChanged(val title: String) : EditTaskUiEvent

    data object OnSubmit : EditTaskUiEvent
}
