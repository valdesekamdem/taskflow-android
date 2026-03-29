package com.valdesekamdem.taskflow.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import com.valdesekamdem.taskflow.core.model.Task
import com.valdesekamdem.taskflow.core.navigation.api.Navigator
import com.valdesekamdem.taskflow.core.presentation.StateHolder
import com.valdesekamdem.taskflow.feature.home.viewmodel.HomeUiEvent.NewTaskClicked
import com.valdesekamdem.taskflow.feature.home.viewmodel.HomeUiEvent.TaskClicked
import com.valdesekamdem.taskflow.feature.task.data.api.TaskRepository
import com.valdesekamdem.taskflow.feature.task.screens.EditTaskScreen
import com.valdesekamdem.taskflow.feature.task.screens.TaskDetailScreen
import com.valdesekamdem.taskflow.feature.utils.stateInWhileSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val navigator: Navigator,
    private val taskRepository: TaskRepository,
) : ViewModel(), StateHolder<HomeUiState, HomeUiEvent> {

    private val _uiState = MutableStateFlow(
        HomeUiState(
            todayDate = "March 28",
            tasks = emptyList(),
        )
    )

    override val uiState: StateFlow<HomeUiState> = combine(
        _uiState,
        taskRepository.getTasks(),
    ) { state, tasks ->
        state.copy(tasks = tasks.toTaskUiModels())
    }.stateInWhileSubscribed(_uiState.value)

    override fun onUiEvent(event: HomeUiEvent) = when (event) {
        is TaskClicked -> navigator.goTo(
            screen = TaskDetailScreen(event.task.id, event.task.title)
        )

        is NewTaskClicked -> navigator.goTo(
            screen = EditTaskScreen(null)
        )
    }
}

private fun List<Task>.toTaskUiModels() = map { it.toTaskUiModel() }

private fun Task.toTaskUiModel() = TaskUiModel(
    id = id.toString(),
    title = title,
    description = description,
    priority = priority,
    category = category.name,
    dueDateText = "",
)
