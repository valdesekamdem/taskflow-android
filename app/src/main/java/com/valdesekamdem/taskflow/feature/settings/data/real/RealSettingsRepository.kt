package com.valdesekamdem.taskflow.feature.settings.data.real

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.valdesekamdem.taskflow.feature.settings.data.api.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("PrivatePropertyName")
@Singleton
class RealSettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : SettingsRepository {
    private val USER_NAME_PREF_KEY = stringPreferencesKey("user_name")

    override val userName: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_NAME_PREF_KEY]
    }

    override suspend fun updateUserName(userName: String) {
        dataStore.updateData {
            it.toMutablePreferences().also { preferences ->
                preferences[USER_NAME_PREF_KEY] = userName
            }
        }
    }
}
