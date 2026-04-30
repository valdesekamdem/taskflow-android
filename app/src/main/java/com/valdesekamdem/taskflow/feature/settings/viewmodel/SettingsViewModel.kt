package com.valdesekamdem.taskflow.feature.settings.viewmodel

import androidx.lifecycle.ViewModel
import com.valdesekamdem.taskflow.core.presentation.StateHolder
import com.valdesekamdem.taskflow.feature.settings.data.api.SettingsRepository
import com.valdesekamdem.taskflow.feature.utils.stateInWhileSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

sealed interface SettingsUiEvent

@HiltViewModel
class SettingsViewModel @Inject constructor(
    settingsRepository: SettingsRepository,
) : ViewModel(), StateHolder<SettingsUiState, SettingsUiEvent> {

    override val uiState: StateFlow<SettingsUiState> = settingsRepository.userName.map { userName ->
        SettingsUiState(
            username = userName.takeIf { it != null } ?: "No name",
            monogram = userName?.firstOrNull() ?: '?',
        )
    }.stateInWhileSubscribed(SettingsUiState())

    override fun onUiEvent(event: SettingsUiEvent) = Unit
}
