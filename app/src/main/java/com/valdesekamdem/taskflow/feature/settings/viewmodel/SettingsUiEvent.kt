package com.valdesekamdem.taskflow.feature.settings.viewmodel

sealed interface SettingsUiEvent {
    data object EditUserNameClicked : SettingsUiEvent

    data class UserNameChanged(val userName: String) : SettingsUiEvent

    data object UserNameSheetDismissed : SettingsUiEvent

    data object SaveUserNameClicked : SettingsUiEvent
}
