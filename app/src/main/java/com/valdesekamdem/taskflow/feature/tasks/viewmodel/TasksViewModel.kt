package com.valdesekamdem.taskflow.feature.tasks.viewmodel

import androidx.lifecycle.ViewModel
import com.valdesekamdem.taskflow.core.presentation.StateHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data object TasksUiState

sealed interface TasksUiEvent

@HiltViewModel
class TasksViewModel @Inject constructor() : ViewModel(), StateHolder<TasksUiState, TasksUiEvent> {

    override val uiState: StateFlow<TasksUiState> =
        MutableStateFlow(TasksUiState).asStateFlow()

    override fun onUiEvent(event: TasksUiEvent) = Unit
}
