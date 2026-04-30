package com.valdesekamdem.taskflow.feature.settings.data.api

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val userName: Flow<String?>

    suspend fun updateUserName(userName: String)
}