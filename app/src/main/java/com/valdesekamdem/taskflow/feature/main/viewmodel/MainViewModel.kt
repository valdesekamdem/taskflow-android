package com.valdesekamdem.taskflow.feature.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valdesekamdem.taskflow.core.navigation.api.Back
import com.valdesekamdem.taskflow.core.navigation.api.Navigator
import com.valdesekamdem.taskflow.core.navigation.api.TabScreen
import com.valdesekamdem.taskflow.core.navigation.real.TabNavigationEvent
import com.valdesekamdem.taskflow.core.navigation.real.TabNavigationEventSource
import com.valdesekamdem.taskflow.core.presentation.StateHolder
import com.valdesekamdem.taskflow.feature.home.screens.HomeScreen
import com.valdesekamdem.taskflow.feature.main.viewmodel.MainUiEvent.BackPressed
import com.valdesekamdem.taskflow.feature.main.viewmodel.MainUiEvent.TabSelected
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val navigator: Navigator,
    tabNavigationEventSource: TabNavigationEventSource,
) : ViewModel(), StateHolder<MainUiState, MainUiEvent> {

    private val _uiState = MutableStateFlow(MainUiState())
    override val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            tabNavigationEventSource.tabEvents.collect { event ->
                when (event) {
                    is TabNavigationEvent.NavigateTo -> navigateTo(event.screen)
                }
            }
        }
    }

    override fun onUiEvent(event: MainUiEvent) {
        when (event) {
            is TabSelected -> navigateTo(event.screen)
            is BackPressed -> handleBack()
        }
    }

    private fun navigateTo(target: TabScreen) {
        _uiState.update { state ->
            val stack = state.tabBackStack
            val newStack = when {
                target is HomeScreen -> listOf(HomeScreen)
                stack.size > 1 -> stack.dropLast(1) + target
                else -> stack + target
            }
            state.copy(tabBackStack = newStack)
        }
    }

    private fun handleBack() {
        val stack = _uiState.value.tabBackStack
        if (stack.size > 1) {
            _uiState.update { it.copy(tabBackStack = it.tabBackStack.dropLast(1)) }
        } else {
            navigator.goTo(Back)
        }
    }
}
