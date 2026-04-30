package com.valdesekamdem.taskflow.feature.settings.viewmodel

import app.cash.turbine.test
import com.valdesekamdem.taskflow.feature.settings.data.fakes.FakeSettingsRepository
import com.valdesekamdem.taskflow.utils.skipItem
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SettingsViewModelTest {

    private val settingsRepository = FakeSettingsRepository()

    private fun createViewModel() = SettingsViewModel(
        settingsRepository = settingsRepository,
    )

    @Test
    fun `shows No name and question mark monogram when user name is null`() = runTest {
        createViewModel().uiState.test {
            assertEquals(SettingsUiState(username = "No name", monogram = '?'), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `shows user name and first char monogram when repo emits a name`() = runTest {
        settingsRepository.emitUserName("Alice")

        createViewModel().uiState.test {
            skipItem("initial value")
            assertEquals(SettingsUiState(username = "Alice", monogram = 'A'), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `shows No name and question mark monogram when user name switches back to null`() =
        runTest {
            settingsRepository.emitUserName("Alice")

            createViewModel().uiState.test {
                skipItem("initial value")
                skipItem("settings with Alice as user name")
                settingsRepository.emitUserName(null)
                assertEquals(SettingsUiState(username = "No name", monogram = '?'), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `monogram falls back to question mark for empty string user name`() = runTest {
        settingsRepository.emitUserName("")

        createViewModel().uiState.test {
            skipItem("initial value")
            assertEquals(SettingsUiState(username = "", monogram = '?'), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
