package com.valdesekamdem.taskflow.feature.home

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.valdesekamdem.taskflow.core.presentation.BindScreen
import com.valdesekamdem.taskflow.core.presentation.UiFactory
import com.valdesekamdem.taskflow.feature.home.screens.HomeScreen
import com.valdesekamdem.taskflow.feature.home.ui.Home
import com.valdesekamdem.taskflow.feature.home.viewmodel.HomeViewModel
import javax.inject.Inject

class HomeUiFactory @Inject constructor() : UiFactory {

    override fun register(scope: EntryProviderScope<NavKey>) = with(scope) {
        entry<HomeScreen> { _ ->
            BindScreen(
                stateHolder = hiltViewModel<HomeViewModel>()
            ) { uiState, _ ->
                Home(uiState = uiState)
            }
        }
    }
}
