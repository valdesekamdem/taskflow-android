package com.valdesekamdem.taskflow.feature.settings.viewmodel

import app.cash.turbine.test
import com.valdesekamdem.taskflow.feature.settings.data.fakes.FakeSettingsRepository
import com.valdesekamdem.taskflow.feature.settings.viewmodel.SettingsUiEvent.EditUserNameClicked
import com.valdesekamdem.taskflow.feature.settings.viewmodel.SettingsUiEvent.SaveUserNameClicked
import com.valdesekamdem.taskflow.feature.settings.viewmodel.SettingsUiEvent.UserNameChanged
import com.valdesekamdem.taskflow.feature.settings.viewmodel.SettingsUiEvent.UserNameSheetDismissed
import com.valdesekamdem.taskflow.feature.settings.viewmodel.SettingsUiState.UserNameSheetUiState
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
            skipItem("initial value")
            assertEquals(SettingsUiState(username = "No name", monogram = '?'), awaitItem())
        }
    }

    @Test
    fun `shows user name and first char monogram when repo emits a name`() = runTest {
        settingsRepository.emitUserName("Alice")

        createViewModel().uiState.test {
            skipItem("initial value")
            assertEquals(SettingsUiState(username = "Alice", monogram = 'A'), awaitItem())
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
            }
        }

    @Test
    fun `monogram falls back to question mark for empty string user name`() = runTest {
        settingsRepository.emitUserName("")

        createViewModel().uiState.test {
            skipItem("initial value")
            assertEquals(SettingsUiState(username = "", monogram = '?'), awaitItem())
        }
    }

    @Test
    fun `EditUserNameClicked opens sheet with empty string when user name is not set`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            skipItem("initial state")
            viewModel.onUiEvent(EditUserNameClicked)
            assertEquals(
                SettingsUiState(
                    username = "No name",
                    monogram = '?',
                    userNameSheet = UserNameSheetUiState("")
                ),
                awaitItem(),
            )
        }
    }

    @Test
    fun `EditUserNameClicked opens sheet with current user name`() = runTest {
        settingsRepository.emitUserName("Alice")
        val viewModel = createViewModel()

        viewModel.uiState.test {
            skipItem("initial state")
            skipItem("state with Alice")
            viewModel.onUiEvent(EditUserNameClicked)
            assertEquals(
                SettingsUiState(
                    username = "Alice",
                    monogram = 'A',
                    userNameSheet = UserNameSheetUiState("Alice")
                ),
                awaitItem(),
            )
        }
    }

    @Test
    fun `UserNameSheetDismissed closes the sheet`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            skipItem("initial state")
            viewModel.onUiEvent(EditUserNameClicked)
            skipItem("sheet opened")
            viewModel.onUiEvent(UserNameSheetDismissed)
            assertEquals(SettingsUiState(username = "No name", monogram = '?'), awaitItem())
        }
    }

    @Test
    fun `UserNameChanged updates user name in the sheet`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            skipItem("initial state")
            viewModel.onUiEvent(EditUserNameClicked)
            skipItem("sheet opened")
            viewModel.onUiEvent(UserNameChanged("Bob"))
            assertEquals(
                SettingsUiState(
                    username = "No name",
                    monogram = '?',
                    userNameSheet = UserNameSheetUiState("Bob")
                ),
                awaitItem(),
            )
        }
    }

    @Test
    fun `SaveUserNameClicked saves user name to repository and closes the sheet`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            skipItem("initial state")
            viewModel.onUiEvent(EditUserNameClicked)
            skipItem("sheet opened")
            viewModel.onUiEvent(UserNameChanged("Alice"))
            skipItem("name updated")
            viewModel.onUiEvent(SaveUserNameClicked)
            assertEquals("Alice", settingsRepository.updateUserNameCalls.awaitItem())
            assertEquals(SettingsUiState(username = "No name", monogram = '?'), awaitItem())
        }
    }
}
