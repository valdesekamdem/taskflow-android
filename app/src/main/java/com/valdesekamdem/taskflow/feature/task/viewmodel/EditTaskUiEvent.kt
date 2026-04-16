package com.valdesekamdem.taskflow.feature.task.viewmodel

import com.valdesekamdem.taskflow.core.model.Category
import com.valdesekamdem.taskflow.core.model.Priority

sealed interface EditTaskUiEvent {
    data object CloseClicked : EditTaskUiEvent

    data class TitleChanged(val title: String) : EditTaskUiEvent

    data class DescriptionChanged(val description: String) : EditTaskUiEvent

    data class CategoryChanged(val category: Category) : EditTaskUiEvent

    data class PriorityChanged(val priority: Priority) : EditTaskUiEvent

    data object SubmitForm : EditTaskUiEvent
}
