package com.valdesekamdem.taskflow.feature.main.viewmodel

import app.cash.turbine.test
import com.valdesekamdem.taskflow.core.navigation.api.Back
import com.valdesekamdem.taskflow.core.navigation.fakes.FakeNavigator
import com.valdesekamdem.taskflow.core.navigation.fakes.FakeTabNavigationEventSource
import com.valdesekamdem.taskflow.core.navigation.real.TabNavigationEvent
import com.valdesekamdem.taskflow.feature.home.screens.HomeScreen
import com.valdesekamdem.taskflow.feature.main.viewmodel.MainUiEvent.BackPressed
import com.valdesekamdem.taskflow.feature.main.viewmodel.MainUiEvent.TabSelected
import com.valdesekamdem.taskflow.feature.settings.screens.SettingsScreen
import com.valdesekamdem.taskflow.feature.tasks.screens.TasksScreen
import com.valdesekamdem.taskflow.utils.skipItem
import com.valdesekamdem.taskflow.utils.test
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class MainViewModelTest {

    private val navigator = FakeNavigator()
    private val tabNavigationEventSource = FakeTabNavigationEventSource()

    private fun createViewModel() = MainViewModel(
        navigator = navigator,
        tabNavigationEventSource = tabNavigationEventSource,
    )

    @Test
    fun `initial uiState has only HomeScreen in tabBackStack`() = runTest {
        createViewModel().test {
            assertEquals(MainUiState(tabBackStack = listOf(HomeScreen)), uiState.value)
        }
    }

    @Test
    fun `TabSelected with non-Home screen pushes it onto the backstack`() = runTest {
        val viewModel = createViewModel()
        viewModel.uiState.test {
            skipItem("Initial state")
            viewModel.onUiEvent(TabSelected(TasksScreen))
            assertEquals(MainUiState(tabBackStack = listOf(HomeScreen, TasksScreen)), awaitItem())
        }
    }

    @Test
    fun `TabSelected replaces current non-Home tab`() = runTest {
        val viewModel = createViewModel()
        viewModel.uiState.test {
            skipItem("Initial state")
            viewModel.onUiEvent(TabSelected(TasksScreen))
            assertEquals(MainUiState(tabBackStack = listOf(HomeScreen, TasksScreen)), awaitItem())
            viewModel.onUiEvent(TabSelected(SettingsScreen))
            assertEquals(MainUiState(tabBackStack = listOf(HomeScreen, SettingsScreen)), awaitItem())
        }
    }

    @Test
    fun `TabSelected with HomeScreen resets backstack to HomeScreen only`() = runTest {
        val viewModel = createViewModel()
        viewModel.uiState.test {
            skipItem("Initial state")
            viewModel.onUiEvent(TabSelected(TasksScreen))
            skipItem("[HomeScreen, TasksScreen]")
            viewModel.onUiEvent(TabSelected(HomeScreen))
            assertEquals(MainUiState(tabBackStack = listOf(HomeScreen)), awaitItem())
        }
    }

    @Test
    fun `BackPressed when on non-Home tab pops back to HomeScreen`() = runTest {
        val viewModel = createViewModel()
        viewModel.uiState.test {
            skipItem("Initial state")
            viewModel.onUiEvent(TabSelected(TasksScreen))
            skipItem("[HomeScreen, TasksScreen]")
            viewModel.onUiEvent(BackPressed)
            assertEquals(MainUiState(tabBackStack = listOf(HomeScreen)), awaitItem())
        }
    }

    @Test
    fun `BackPressed when on HomeScreen navigates Back via navigator`() = runTest {
        createViewModel().test {
            onUiEvent(BackPressed)
            assertEquals(Back, navigator.screens.awaitItem())
        }
    }

    @Test
    fun `tab deep-link event updates tabBackStack`() = runTest {
        val viewModel = createViewModel()
        viewModel.uiState.test {
            skipItem("Initial state")
            tabNavigationEventSource.tabEventsChannel.send(TabNavigationEvent.NavigateTo(TasksScreen))
            assertEquals(MainUiState(tabBackStack = listOf(HomeScreen, TasksScreen)), awaitItem())
        }
    }
}
