package com.valdesekamdem.taskflow.feature.settings.data.fakes

import app.cash.turbine.Turbine
import com.valdesekamdem.taskflow.feature.settings.data.api.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeSettingsRepository : SettingsRepository {
    private val _userName = MutableStateFlow<String?>(null)
    fun emitUserName(value: String?) {
        _userName.value = value
    }

    override val userName: Flow<String?> = _userName

    val updateUserNameCalls = Turbine<String>()
    override suspend fun updateUserName(userName: String) {
        updateUserNameCalls.add(userName)
    }
}
