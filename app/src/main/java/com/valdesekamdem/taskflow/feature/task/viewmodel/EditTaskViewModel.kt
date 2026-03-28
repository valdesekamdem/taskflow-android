package com.valdesekamdem.taskflow.feature.task.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valdesekamdem.taskflow.core.navigation.api.Navigator
import com.valdesekamdem.taskflow.core.presentation.StateHolder
import com.valdesekamdem.taskflow.feature.task.data.api.TaskModel
import com.valdesekamdem.taskflow.feature.task.data.api.TaskRepository
import com.valdesekamdem.taskflow.feature.task.viewmodel.EditTaskUiEvent.BackClicked
import com.valdesekamdem.taskflow.feature.task.viewmodel.EditTaskUiEvent.DescriptionChanged
import com.valdesekamdem.taskflow.feature.task.viewmodel.EditTaskUiEvent.SubmitForm
import com.valdesekamdem.taskflow.feature.task.viewmodel.EditTaskUiEvent.TitleChanged
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTaskViewModel @Inject constructor(
    private val navigator: Navigator,
    private val taskRepository: TaskRepository,
) : ViewModel(), StateHolder<EditTaskUiState, EditTaskUiEvent> {

    private val _uiState = MutableStateFlow(
        EditTaskUiState(
            title = "New task",
            form = EditTaskUiState.EditTaskForm(),
            isSubmitting = false
        )
    )
    override val uiState: StateFlow<EditTaskUiState> = _uiState.asStateFlow()

    override fun onUiEvent(event: EditTaskUiEvent) {
        when (event) {
            BackClicked -> navigator.getBack()
            is TitleChanged -> reduce {
                copy(
                    form = form.copy(title = event.title),
                )
            }

            is DescriptionChanged -> _uiState.update {
                it.copy(
                    form = it.form.copy(description = event.description)
                )
            }

            SubmitForm -> handleSubmit()
        }
    }

    private fun handleSubmit() {
        val currentForm = _uiState.value.form
        val (title, description) = currentForm
        check(title.isNotBlank()) { // FIXME(valdese): Handle validate for required fields
            "Title is required"
        }

        reduce { copy(isSubmitting = true) }
        val taskModel = TaskModel(
            title = title,
            description = description,
        )

        viewModelScope.launch {
            taskRepository.addTask(taskModel)

            reduce { copy(isSubmitting = false) }
            navigator.getBack()
        }
    }

    private inline fun reduce(block: EditTaskUiState.() -> EditTaskUiState) {
        _uiState.update { it.block() }
    }
}