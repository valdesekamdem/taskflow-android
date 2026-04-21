package com.valdesekamdem.taskflow.feature.main

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.valdesekamdem.taskflow.core.presentation.BindScreen
import com.valdesekamdem.taskflow.core.presentation.UiFactory
import com.valdesekamdem.taskflow.feature.home.screens.HomeScreen
import com.valdesekamdem.taskflow.feature.home.ui.Home
import com.valdesekamdem.taskflow.feature.home.viewmodel.HomeViewModel
import com.valdesekamdem.taskflow.feature.main.screens.MainScreen
import com.valdesekamdem.taskflow.feature.main.ui.MainContent
import com.valdesekamdem.taskflow.feature.main.viewmodel.MainViewModel
import com.valdesekamdem.taskflow.feature.settings.screens.SettingsScreen
import com.valdesekamdem.taskflow.feature.settings.ui.Settings
import com.valdesekamdem.taskflow.feature.settings.viewmodel.SettingsViewModel
import com.valdesekamdem.taskflow.feature.tasks.screens.TasksScreen
import com.valdesekamdem.taskflow.feature.tasks.ui.Tasks
import com.valdesekamdem.taskflow.feature.tasks.viewmodel.TasksViewModel
import javax.inject.Inject

class MainUiFactory @Inject constructor() : UiFactory {
    private val tabEntryProvider: EntryProviderScope<NavKey>.() -> Unit = {
        entry<HomeScreen> { _ ->
            BindScreen(hiltViewModel<HomeViewModel>()) { s, e -> Home(s, e) }
        }
        entry<TasksScreen> { _ ->
            BindScreen(hiltViewModel<TasksViewModel>()) { s, e -> Tasks(s, e) }
        }
        entry<SettingsScreen> { _ ->
            BindScreen(hiltViewModel<SettingsViewModel>()) { s, e -> Settings(s, e) }
        }
    }

    override fun register(scope: EntryProviderScope<NavKey>) = with(scope) {
        entry<MainScreen> { _ ->
            BindScreen(hiltViewModel<MainViewModel>()) { uiState, onUiEvent ->
                MainContent(
                    uiState = uiState,
                    onUiEvent = onUiEvent,
                    tabEntryProvider = tabEntryProvider,
                )
            }
        }
    }
}
