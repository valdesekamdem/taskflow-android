package com.valdesekamdem.taskflow.feature.tasks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valdesekamdem.taskflow.core.navigation.api.Navigator
import com.valdesekamdem.taskflow.core.presentation.StateHolder
import com.valdesekamdem.taskflow.feature.task.data.api.TaskRepository
import com.valdesekamdem.taskflow.feature.task.screens.TaskDetailScreen
import com.valdesekamdem.taskflow.feature.tasks.viewmodel.TasksUiEvent.TaskCheckboxToggled
import com.valdesekamdem.taskflow.feature.tasks.viewmodel.TasksUiEvent.TaskClicked
import com.valdesekamdem.taskflow.feature.utils.stateInWhileSubscribed
import com.valdesekamdem.taskflow.ui.model.toTaskUiModels
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.ZoneId
import javax.inject.Inject
import kotlin.time.Clock

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val navigator: Navigator,
    private val taskRepository: TaskRepository,
    private val clock: Clock,
    zoneId: ZoneId,
) : ViewModel(), StateHolder<TasksUiState, TasksUiEvent> {

    private val _uiState = MutableStateFlow(TasksUiState())

    private val tasksFlow = taskRepository
        .getTasks()
        .map { it.toTaskUiModels(clock.now(), zoneId) }

    override val uiState: StateFlow<TasksUiState> = combine(
        _uiState,
        tasksFlow,
    ) { state, tasks ->
        state.copy(tasks = tasks)
    }.stateInWhileSubscribed(_uiState.value)

    override fun onUiEvent(event: TasksUiEvent) {
        when (event) {
            is TaskClicked -> navigator.goTo(TaskDetailScreen(event.task.id))

            is TaskCheckboxToggled -> viewModelScope.launch {
                if (event.task.isCompleted) {
                    taskRepository.unmarkTaskCompleted(event.task.id)
                } else {
                    taskRepository.markTaskCompleted(event.task.id)
                }
            }
        }
    }
}
