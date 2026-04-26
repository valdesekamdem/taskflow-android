package com.valdesekamdem.taskflow.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valdesekamdem.taskflow.core.clock.utils.atStartOfDay
import com.valdesekamdem.taskflow.core.clock.utils.toMonthDay
import com.valdesekamdem.taskflow.core.navigation.api.Navigator
import com.valdesekamdem.taskflow.core.presentation.StateHolder
import com.valdesekamdem.taskflow.feature.home.viewmodel.HomeUiEvent.NewTaskClicked
import com.valdesekamdem.taskflow.feature.home.viewmodel.HomeUiEvent.TaskCheckboxToggled
import com.valdesekamdem.taskflow.feature.home.viewmodel.HomeUiEvent.TaskClicked
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
import kotlinx.coroutines.launch
import java.time.ZoneId
import javax.inject.Inject
import kotlin.time.Clock

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
            overdueTasks = emptyList(),
        )
    )

    val todayStartOfDay = clock.now().atStartOfDay(zoneId)
    val overdueTasksFilter = TaskFilter(
        dueDate = DateFilter.Before(todayStartOfDay),
        isCompleted = false,
    )

    override val uiState: StateFlow<HomeUiState> = combine(
        _uiState,
        taskRepository.getTasks(overdueTasksFilter),
    ) { state, dueTasks ->
        state.copy(overdueTasks = dueTasks.toTaskUiModels(clock.now(), zoneId))
    }.stateInWhileSubscribed(_uiState.value)

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
        }
    }
}
