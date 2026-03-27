package com.valdesekamdem.taskflow.feature.task.viewmodel

import androidx.lifecycle.ViewModel
import com.valdesekamdem.taskflow.core.presentation.StateHolder
import com.valdesekamdem.taskflow.feature.task.screens.TaskDetailScreen
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel(assistedFactory = TaskDetailViewModel.Factory::class)
class TaskDetailViewModel @AssistedInject constructor(
    @Assisted private val screen: TaskDetailScreen,
) : ViewModel(), StateHolder<TaskDetailUiState, Unit> {

    private val _uiState = MutableStateFlow(TaskDetailUiState(title = screen.title))

    override val uiState: StateFlow<TaskDetailUiState> = _uiState.asStateFlow()

    override fun onUiEvent(event: Unit) {
        TODO("Not yet implemented")
    }

    @AssistedFactory
    fun interface Factory {
        fun create(screen: TaskDetailScreen): TaskDetailViewModel
    }
}