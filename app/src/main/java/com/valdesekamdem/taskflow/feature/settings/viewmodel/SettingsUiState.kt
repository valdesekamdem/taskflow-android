package com.valdesekamdem.taskflow.feature.settings.viewmodel

data class SettingsUiState(
    val username: String? = null,
    val monogram: Char? = null,
    val userNameSheet: UserNameSheetUiState? = null,
) {
    data class UserNameSheetUiState(
        val userName: String = "",
    )
}