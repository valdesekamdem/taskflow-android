package com.valdesekamdem.taskflow.feature.task.viewmodel

import androidx.lifecycle.ViewModel
import com.valdesekamdem.taskflow.core.navigation.api.Navigator
import com.valdesekamdem.taskflow.core.presentation.StateHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class EditTaskViewModel @Inject constructor(
    private val navigator: Navigator,
) : ViewModel(), StateHolder<EditTaskUiState, EditTaskUiEvent> {

    override val uiState: StateFlow<EditTaskUiState>
        get() = MutableStateFlow(
            EditTaskUiState(
                form = EditTaskUiState.EditTaskForm(),
                isSubmitting = false
            )
        )

    override fun onUiEvent(event: EditTaskUiEvent) {
        // TODO("Not yet implemented")
    }
}