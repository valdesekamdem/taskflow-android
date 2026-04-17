package com.valdesekamdem.taskflow.feature.task.viewmodel

import androidx.lifecycle.ViewModel
import com.valdesekamdem.taskflow.core.clock.utils.toMonthDayYear
import com.valdesekamdem.taskflow.core.model.Category
import com.valdesekamdem.taskflow.core.model.Priority
import com.valdesekamdem.taskflow.core.navigation.api.Back
import com.valdesekamdem.taskflow.core.navigation.api.Navigator
import com.valdesekamdem.taskflow.core.presentation.StateHolder
import com.valdesekamdem.taskflow.feature.task.data.api.TaskRepository
import com.valdesekamdem.taskflow.feature.task.screens.EditTaskScreen
import com.valdesekamdem.taskflow.feature.task.screens.TaskDetailScreen
import com.valdesekamdem.taskflow.feature.task.viewmodel.TaskDetailUiEvent.BackClicked
import com.valdesekamdem.taskflow.feature.task.viewmodel.TaskDetailUiEvent.EditClicked
import com.valdesekamdem.taskflow.feature.utils.stateInWhileSubscribed
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import java.time.ZoneId
import kotlin.math.absoluteValue
import kotlin.time.Clock
import kotlin.time.Instant

@HiltViewModel(assistedFactory = TaskDetailViewModel.Factory::class)
class TaskDetailViewModel @AssistedInject constructor(
    private val navigator: Navigator,
    private val taskRepository: TaskRepository,
    private val clock: Clock,
    private val zoneId: ZoneId,
    @Assisted private val screen: TaskDetailScreen,
) : ViewModel(), StateHolder<TaskDetailUiState, TaskDetailUiEvent> {

    override val uiState: StateFlow<TaskDetailUiState> =
        taskRepository.getTask(screen.id)
            .map { task ->
                checkNotNull(task) // TODO: Implement error handling
                TaskDetailUiState(
                    title = task.title,
                    description = task.description,
                    priority = task.priority,
                    dueDate = task.dueDate!!.toDueDate(),
                    category = task.category,
                    tasksInCategory = null, // TODO: Implement this
                    createdAt = task.createdAt.toMonthDayYear(zoneId),
                    reminder = "-", // TODO: Implement this
                )
            }
            .stateInWhileSubscribed(
            initialValue = TaskDetailUiState(
                title = "",
                description = null,
                priority = Priority.Medium,
                dueDate = TaskDetailUiState.DueDate(date = "", countDown = null, isOverdue = false),
                category = Category.Personal,
                tasksInCategory = null,
                createdAt = "",
                reminder = ""
            ),
        )

    override fun onUiEvent(event: TaskDetailUiEvent) {
        when (event) {
            BackClicked -> navigator.goTo(Back)
            EditClicked -> navigator.goTo(EditTaskScreen(id = screen.id))
        }
    }

    private fun Instant.toDueDate(): TaskDetailUiState.DueDate {
        val daysLeft = this.minus(clock.now()).inWholeDays
        val countDown = when {
            daysLeft == 0L -> "Overdue today"
            daysLeft < 0 -> "Overdue ${daysLeft.absoluteValue} days ago"
            else -> "Due in $daysLeft days"
        }
        return TaskDetailUiState.DueDate(
            date = toMonthDayYear(zoneId),
            countDown = countDown,
            isOverdue = daysLeft < 0,
        )
    }

    @AssistedFactory
    fun interface Factory {
        fun create(screen: TaskDetailScreen): TaskDetailViewModel
    }
}
