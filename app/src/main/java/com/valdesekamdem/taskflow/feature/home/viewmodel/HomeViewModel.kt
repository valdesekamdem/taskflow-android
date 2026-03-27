package com.valdesekamdem.taskflow.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import com.valdesekamdem.taskflow.core.presentation.StateHolder
import com.valdesekamdem.taskflow.feature.home.fixtures.HomeFixtures
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class HomeViewModel @Inject constructor(): ViewModel(), StateHolder<HomeUiState, Unit> {
    override val uiState: StateFlow<HomeUiState> = MutableStateFlow(HomeUiState(HomeFixtures.tasks))

    override fun onUiEvent(event: Unit) {}
}
