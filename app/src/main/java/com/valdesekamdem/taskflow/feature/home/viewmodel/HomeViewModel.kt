package com.valdesekamdem.taskflow.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import com.valdesekamdem.taskflow.core.navigation.api.Navigator
import com.valdesekamdem.taskflow.core.presentation.StateHolder
import com.valdesekamdem.taskflow.feature.home.fixtures.HomeFixtures
import com.valdesekamdem.taskflow.feature.home.viewmodel.HomeUiEvent.NewTaskClicked
import com.valdesekamdem.taskflow.feature.home.viewmodel.HomeUiEvent.TaskClicked
import com.valdesekamdem.taskflow.feature.task.screens.EditTaskScreen
import com.valdesekamdem.taskflow.feature.task.screens.TaskDetailScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val navigator: Navigator,
) : ViewModel(), StateHolder<HomeUiState, HomeUiEvent> {
    override val uiState: StateFlow<HomeUiState> = MutableStateFlow(HomeUiState(HomeFixtures.tasks))

    override fun onUiEvent(event: HomeUiEvent) = when (event) {
        is TaskClicked -> navigator.goTo(
            screen = TaskDetailScreen(event.task.id, event.task.title)
        )

        is NewTaskClicked -> navigator.goTo(
            screen = EditTaskScreen(null)
        )
    }
}
