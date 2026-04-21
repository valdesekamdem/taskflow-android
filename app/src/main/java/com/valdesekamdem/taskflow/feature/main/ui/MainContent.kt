package com.valdesekamdem.taskflow.feature.main.ui

import android.util.Log
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.valdesekamdem.taskflow.feature.home.screens.HomeScreen
import com.valdesekamdem.taskflow.feature.main.viewmodel.MainUiEvent
import com.valdesekamdem.taskflow.feature.main.viewmodel.MainUiEvent.BackPressed
import com.valdesekamdem.taskflow.feature.main.viewmodel.MainUiEvent.TabSelected
import com.valdesekamdem.taskflow.feature.main.viewmodel.MainUiState
import com.valdesekamdem.taskflow.ui.components.bottomnav.MainBottomNavigation

@Composable
fun MainContent(
    uiState: MainUiState,
    onUiEvent: (MainUiEvent) -> Unit,
    tabEntryProvider: EntryProviderScope<NavKey>.() -> Unit,
) {
    val innerBackStack = rememberNavBackStack(HomeScreen)

    LaunchedEffect(uiState.tabBackStack) {
        val newStack = uiState.tabBackStack
        while (innerBackStack.size > newStack.size) innerBackStack.removeLastOrNull()
        newStack.forEachIndexed { index, screen ->
            when {
                index >= innerBackStack.size -> innerBackStack.add(screen)
                innerBackStack[index] != screen -> innerBackStack[index] = screen
            }
        }
    }

    Scaffold(
        bottomBar = {
            MainBottomNavigation(
                currentScreen = innerBackStack.last(),
                onTabSelected = { screen -> onUiEvent(TabSelected(screen)) },
            )
        },
    ) { innerPadding ->
        NavDisplay(
            backStack = innerBackStack,
            onBack = { onUiEvent(BackPressed) },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            entryProvider = entryProvider { tabEntryProvider() },
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .fillMaxSize(),
        )
    }
}
