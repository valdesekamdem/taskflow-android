package com.valdesekamdem.taskflow.feature.task.viewmodel

import androidx.lifecycle.ViewModel
import com.valdesekamdem.taskflow.core.navigation.api.Navigator
import com.valdesekamdem.taskflow.core.presentation.StateHolder
import com.valdesekamdem.taskflow.feature.task.viewmodel.EditTaskUiEvent.BackClicked
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class EditTaskViewModel @Inject constructor(
    private val navigator: Navigator,
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
            else -> {}
        }
    }
}