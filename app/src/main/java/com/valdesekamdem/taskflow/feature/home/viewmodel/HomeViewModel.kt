package com.valdesekamdem.taskflow.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import com.valdesekamdem.taskflow.core.navigation.api.Navigator
import com.valdesekamdem.taskflow.core.presentation.StateHolder
import com.valdesekamdem.taskflow.feature.home.fixtures.HomeFixtures
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
        is HomeUiEvent.TaskClicked -> navigator.goTo(
            screen = TaskDetailScreen(event.task.id, event.task.title)
        )
    }
}
