package com.valdesekamdem.taskflow.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valdesekamdem.taskflow.core.clock.utils.atStartOfDay
import com.valdesekamdem.taskflow.core.clock.utils.isMorning
import com.valdesekamdem.taskflow.core.clock.utils.toMonthDay
import com.valdesekamdem.taskflow.core.navigation.api.Navigator
import com.valdesekamdem.taskflow.core.presentation.StateHolder
import com.valdesekamdem.taskflow.feature.home.viewmodel.HomeUiEvent.NewTaskClicked
import com.valdesekamdem.taskflow.feature.home.viewmodel.HomeUiEvent.OverdueSectionCaptionClicked
import com.valdesekamdem.taskflow.feature.home.viewmodel.HomeUiEvent.TaskCheckboxToggled
import com.valdesekamdem.taskflow.feature.home.viewmodel.HomeUiEvent.TaskClicked
import com.valdesekamdem.taskflow.feature.home.viewmodel.HomeUiEvent.TodaySectionCaptionClicked
import com.valdesekamdem.taskflow.feature.settings.data.api.SettingsRepository
import com.valdesekamdem.taskflow.feature.task.data.api.TaskRepository
import com.valdesekamdem.taskflow.feature.task.data.api.filter.DateFilter
import com.valdesekamdem.taskflow.feature.task.data.api.filter.TaskFilter
import com.valdesekamdem.taskflow.feature.task.screens.EditTaskScreen
import com.valdesekamdem.taskflow.feature.task.screens.TaskDetailScreen
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
import kotlin.time.Duration.Companion.days

private const val DEFAULT_VISIBLE_TASKS = 2

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val navigator: Navigator,
    private val taskRepository: TaskRepository,
    settingsRepository: SettingsRepository,
    private val clock: Clock,
    zoneId: ZoneId,
) : ViewModel(), StateHolder<HomeUiState, HomeUiEvent> {
    private val _uiState = MutableStateFlow(
        HomeUiState(
            todayDate = clock.now().toMonthDay(zoneId),
            title = "",
            overdueTasks = emptyList(),
            maxVisibleTasks = DEFAULT_VISIBLE_TASKS,
        )
    )

    private val titleFlow = settingsRepository.userName.map { username ->
        buildString {
            append(if (clock.now().isMorning(zoneId)) "Morning," else "Hi,")
            if (!username.isNullOrBlank()) {
                append(" $username.")
            }
        }
    }

    val todayStartOfDay = clock.now().atStartOfDay(zoneId)

    val overdueTasksFilter = TaskFilter(
        dueDate = DateFilter.Before(todayStartOfDay),
        isCompleted = false,
    )
    val overdueTasksFlow = taskRepository
        .getTasks(filter = overdueTasksFilter)
        .map { it.toTaskUiModels(clock.now(), zoneId) }

    val todayTasksFilter = TaskFilter(
        dueDate = DateFilter.Between(todayStartOfDay, todayStartOfDay.plus(1.days)),
        isCompleted = false,
    )
    val todayTasksFlow = taskRepository
        .getTasks(filter = todayTasksFilter)
        .map { it.toTaskUiModels(clock.now(), zoneId) }

    override val uiState: StateFlow<HomeUiState> = combine(
        _uiState,
        titleFlow,
        overdueTasksFlow,
        todayTasksFlow
    ) { state, title, overdueTasks, todayTasks ->
        state.copy(title = title, overdueTasks = overdueTasks, todayTasks = todayTasks)
    }.stateInWhileSubscribed(_uiState.value)

    init {
        viewModelScope.launch {
            // Reset the expanded state when the number of tasks drops below the default
            // This prevent the list to open expanded immediately without the user asking the next time tasks are added
            overdueTasksFlow.collect { tasks ->
                if (tasks.size <= DEFAULT_VISIBLE_TASKS) {
                    _uiState.value = _uiState.value.copy(isOverdueTasksExpanded = false)
                }
            }
        }
        viewModelScope.launch {
            todayTasksFlow.collect { tasks ->
                if (tasks.size <= DEFAULT_VISIBLE_TASKS) {
                    _uiState.value = _uiState.value.copy(isTodayTasksExpanded = false)
                }
            }
        }
    }

    override fun onUiEvent(event: HomeUiEvent) {
        when (event) {
            is TaskClicked -> navigator.goTo(
                screen = TaskDetailScreen(event.task.id)
            )

            is NewTaskClicked -> navigator.goTo(
                screen = EditTaskScreen(null)
            )

            is TaskCheckboxToggled -> viewModelScope.launch {
                if (!event.task.isCompleted) {
                    taskRepository.markTaskCompleted(event.task.id)
                }
            }

            OverdueSectionCaptionClicked -> {
                _uiState.value = _uiState.value.copy(
                    isOverdueTasksExpanded = !_uiState.value.isOverdueTasksExpanded
                )
            }

            TodaySectionCaptionClicked -> {
                _uiState.value = _uiState.value.copy(
                    isTodayTasksExpanded = !_uiState.value.isTodayTasksExpanded
                )
            }
        }
    }
}
