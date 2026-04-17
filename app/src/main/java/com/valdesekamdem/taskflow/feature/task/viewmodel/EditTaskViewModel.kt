package com.valdesekamdem.taskflow.feature.task.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valdesekamdem.taskflow.core.clock.utils.fromUtcToInstant
import com.valdesekamdem.taskflow.core.clock.utils.toMonthDayYear
import com.valdesekamdem.taskflow.core.navigation.api.Back
import com.valdesekamdem.taskflow.core.navigation.api.Navigator
import com.valdesekamdem.taskflow.core.presentation.StateHolder
import com.valdesekamdem.taskflow.feature.task.data.api.TaskModel
import com.valdesekamdem.taskflow.feature.task.data.api.TaskRepository
import com.valdesekamdem.taskflow.feature.task.screens.EditTaskScreen
import com.valdesekamdem.taskflow.feature.task.viewmodel.EditTaskUiEvent.CategoryChanged
import com.valdesekamdem.taskflow.feature.task.viewmodel.EditTaskUiEvent.CloseClicked
import com.valdesekamdem.taskflow.feature.task.viewmodel.EditTaskUiEvent.DescriptionChanged
import com.valdesekamdem.taskflow.feature.task.viewmodel.EditTaskUiEvent.DueDateChanged
import com.valdesekamdem.taskflow.feature.task.viewmodel.EditTaskUiEvent.PriorityChanged
import com.valdesekamdem.taskflow.feature.task.viewmodel.EditTaskUiEvent.SubmitForm
import com.valdesekamdem.taskflow.feature.task.viewmodel.EditTaskUiEvent.TitleChanged
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZoneId

@HiltViewModel(assistedFactory = EditTaskViewModel.Factory::class)
class EditTaskViewModel @AssistedInject constructor(
    private val navigator: Navigator,
    private val taskRepository: TaskRepository,
    private val zoneId: ZoneId,
    @Assisted private val screen: EditTaskScreen,
) : ViewModel(), StateHolder<EditTaskUiState, EditTaskUiEvent> {

    private val _uiState = MutableStateFlow(
        EditTaskUiState(
            title = if (screen.id != null) "Edit task" else "New task",
            form = EditTaskUiState.EditTaskForm(),
            isSubmitting = false,
        )
    )
    override val uiState: StateFlow<EditTaskUiState> = _uiState.asStateFlow()

    init {
        screen.id?.let { loadTask(it.toLong()) }
    }

    fun loadTask(id: Long) = viewModelScope.launch {
        taskRepository.getTask(id)
            .onSuccess { task ->
                if (task != null) {
                    reduce {
                        copy(
                            form = form.copy(
                                title = task.title,
                                description = task.description,
                                category = task.category,
                                priority = task.priority,
                                dueDate = task.dueDate,
                                formattedDueDate = task.dueDate?.toMonthDayYear(zoneId) ?: "",
                            )
                        )
                    }
                }
            }
    }

    override fun onUiEvent(event: EditTaskUiEvent) {
        when (event) {
            CloseClicked -> navigator.goTo(Back)

            is TitleChanged -> reduce { copy(form = form.copy(title = event.title)) }

            is DescriptionChanged -> reduce { copy(form = form.copy(description = event.description)) }

            is CategoryChanged -> reduce { copy(form = form.copy(category = event.category)) }

            is PriorityChanged -> reduce { copy(form = form.copy(priority = event.priority)) }

            is DueDateChanged -> reduce {
                val instant = event.dueDateUtc?.fromUtcToInstant(zoneId)
                val formattedDueDate = instant?.toMonthDayYear(zoneId) ?: ""
                copy(form = form.copy(dueDate = instant, formattedDueDate = formattedDueDate))
            }

            SubmitForm -> handleSubmit()
        }
    }

    private fun handleSubmit() {
        val currentForm = _uiState.value.form
        val (title, description, category, priority, dueDate) = currentForm

        reduce { copy(isSubmitting = true) }
        val taskModel = TaskModel(
            title = title,
            description = description,
            category = category,
            priority = priority,
            dueDate = dueDate,
        )

        viewModelScope.launch {
            try {
                if (screen.id != null) {
                    taskRepository.updateTask(screen.id.toLong(), taskModel)
                } else {
                    taskRepository.addTask(taskModel)
                }
                reduce { copy(isSubmitting = false) }
                navigator.goTo(Back)
            } catch (e: Exception) {
                reduce { copy(isSubmitting = false) }
                // TODO(valdese): Surface error to the user
            }
        }
    }

    private inline fun reduce(block: EditTaskUiState.() -> EditTaskUiState) {
        _uiState.update { it.block() }
    }

    @AssistedFactory
    fun interface Factory {
        fun create(screen: EditTaskScreen): EditTaskViewModel
    }
}
