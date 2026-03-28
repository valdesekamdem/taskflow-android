package com.valdesekamdem.taskflow.feature.task.viewmodel

sealed interface EditTaskUiEvent {
    data object CloseClicked : EditTaskUiEvent

    data class TitleChanged(val title: String) : EditTaskUiEvent

    data class DescriptionChanged(val description: String) : EditTaskUiEvent

    data object SubmitForm : EditTaskUiEvent
}
