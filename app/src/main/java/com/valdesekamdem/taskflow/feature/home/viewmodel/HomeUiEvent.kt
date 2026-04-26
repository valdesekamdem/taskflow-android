package com.valdesekamdem.taskflow.feature.home.viewmodel

import com.valdesekamdem.taskflow.ui.model.TaskUiModel

sealed interface HomeUiEvent {
    data class TaskClicked(val task: TaskUiModel) : HomeUiEvent

    data object NewTaskClicked : HomeUiEvent

    data class TaskCheckboxToggled(val task: TaskUiModel) : HomeUiEvent

    data object OverdueSectionCaptionClicked : HomeUiEvent
}
