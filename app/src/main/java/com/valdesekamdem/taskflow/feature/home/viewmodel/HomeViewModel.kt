package com.valdesekamdem.taskflow.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valdesekamdem.taskflow.core.clock.utils.toMonthDay
import com.valdesekamdem.taskflow.core.clock.utils.toRelativeDateText
import com.valdesekamdem.taskflow.core.model.Task
import com.valdesekamdem.taskflow.core.navigation.api.Navigator
import com.valdesekamdem.taskflow.core.presentation.StateHolder
import com.valdesekamdem.taskflow.feature.home.viewmodel.HomeUiEvent.NewTaskClicked
import com.valdesekamdem.taskflow.feature.home.viewmodel.HomeUiEvent.TaskClicked
import com.valdesekamdem.taskflow.feature.home.viewmodel.HomeUiEvent.TaskCompleteClicked
import com.valdesekamdem.taskflow.feature.task.data.api.TaskRepository
import com.valdesekamdem.taskflow.feature.task.screens.EditTaskScreen
import com.valdesekamdem.taskflow.feature.task.screens.TaskDetailScreen
import com.valdesekamdem.taskflow.feature.utils.stateInWhileSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.ZoneId
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.Instant

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val navigator: Navigator,
    private val taskRepository: TaskRepository,
    private val clock: Clock,
    zoneId: ZoneId,
) : ViewModel(), StateHolder<HomeUiState, HomeUiEvent> {

    private val _uiState = MutableStateFlow(
        HomeUiState(
            todayDate = clock.now().toMonthDay(zoneId),
            tasks = emptyList(),
        )
    )

    override val uiState: StateFlow<HomeUiState> = combine(
        _uiState,
        taskRepository.getTasks(),
    ) { state, tasks ->
        state.copy(tasks = tasks.toTaskUiModels(clock.now(), zoneId))
    }.stateInWhileSubscribed(_uiState.value)

    override fun onUiEvent(event: HomeUiEvent) {
        when (event) {
            is TaskClicked -> navigator.goTo(
                screen = TaskDetailScreen(event.task.id)
            )

            is NewTaskClicked -> navigator.goTo(
                screen = EditTaskScreen(null)
            )

            is TaskCompleteClicked -> viewModelScope.launch {
                taskRepository.markTaskCompleted(event.task.id)
            }
        }
    }
}

private fun List<Task>.toTaskUiModels(now: Instant, zoneId: ZoneId) = map { it.toTaskUiModel(now, zoneId) }

private fun Task.toTaskUiModel(now: Instant, zoneId: ZoneId) = TaskUiModel(
    id = id,
    title = title,
    description = description,
    priority = priority,
    category = category.name,
    dueDateText = dueDate?.toRelativeDateText(now, zoneId) ?: "",
    isTaskOverdue = dueDate?.let { it < now } ?: false,
    isCompleted = completedAt != null,
)
