package com.valdesekamdem.taskflow.feature.settings.viewmodel

import androidx.lifecycle.ViewModel
import com.valdesekamdem.taskflow.core.presentation.StateHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

sealed interface SettingsUiEvent

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel(), StateHolder<SettingsUiState, SettingsUiEvent> {

    override val uiState: StateFlow<SettingsUiState> =
        MutableStateFlow(SettingsUiState()).asStateFlow()

    override fun onUiEvent(event: SettingsUiEvent) = Unit
}
