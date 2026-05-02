package com.valdesekamdem.taskflow.feature.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valdesekamdem.taskflow.core.presentation.StateHolder
import com.valdesekamdem.taskflow.feature.settings.data.api.SettingsRepository
import com.valdesekamdem.taskflow.feature.settings.viewmodel.SettingsUiEvent.EditUserNameClicked
import com.valdesekamdem.taskflow.feature.settings.viewmodel.SettingsUiEvent.SaveUserNameClicked
import com.valdesekamdem.taskflow.feature.settings.viewmodel.SettingsUiEvent.UserNameChanged
import com.valdesekamdem.taskflow.feature.settings.viewmodel.SettingsUiEvent.UserNameSheetDismissed
import com.valdesekamdem.taskflow.feature.settings.viewmodel.SettingsUiState.UserNameSheetUiState
import com.valdesekamdem.taskflow.feature.utils.stateInWhileSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ViewModel(), StateHolder<SettingsUiState, SettingsUiEvent> {
    private data class UiState(
        val username: String? = null,
        val userNameSheet: UserNameSheetUiState? = null,
    )

    private val _internalState: MutableStateFlow<UiState> = MutableStateFlow(UiState())

    init {
        viewModelScope.launch {
            settingsRepository.userName.collect { username ->
                _internalState.update { it.copy(username = username) }
            }
        }
    }

    override val uiState: StateFlow<SettingsUiState> = _internalState.map { state ->
        SettingsUiState(
            username = state.username ?: "No name",
            monogram = state.username?.firstOrNull() ?: '?',
            userNameSheet = state.userNameSheet,
        )
    }.stateInWhileSubscribed(SettingsUiState())

    override fun onUiEvent(event: SettingsUiEvent) {
        when (event) {
            is EditUserNameClicked -> {
                _internalState.update {
                    it.copy(
                        userNameSheet = UserNameSheetUiState(
                            it.username ?: ""
                        )
                    )
                }
            }

            is UserNameSheetDismissed -> {
                _internalState.update { it.copy(userNameSheet = null) }
            }

            is UserNameChanged -> {
                _internalState.update {
                    it.copy(
                        userNameSheet = it.userNameSheet!!.copy(
                            userName = event.userName,
                        )
                    )
                }
            }

            is SaveUserNameClicked -> {
                viewModelScope.launch {
                    settingsRepository.updateUserName(_internalState.value.userNameSheet!!.userName)
                    _internalState.update { it.copy(userNameSheet = null) }
                }
            }
        }
    }
}
