package com.valdesekamdem.taskflow.feature.settings.data.real

import androidx.datastore.preferences.core.emptyPreferences
import app.cash.turbine.test
import com.valdesekamdem.taskflow.core.datastore.InMemoryDataStore
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class RealSettingsRepositoryTest {

    private fun createRepository() = RealSettingsRepository(
        dataStore = InMemoryDataStore(emptyPreferences()),
    )

    @Test
    fun `userName emits null when nothing stored`() = runTest {
        createRepository().userName.test {
            assertEquals(null, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `userName emits value after updateUserName`() = runTest {
        val repository = createRepository()

        repository.userName.test {
            awaitItem()
            repository.updateUserName("Alice")
            assertEquals("Alice", awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `userName emits updated value on second call`() = runTest {
        val repository = createRepository()

        repository.userName.test {
            awaitItem()
            repository.updateUserName("Alice")
            awaitItem()
            repository.updateUserName("Bob")
            assertEquals("Bob", awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
