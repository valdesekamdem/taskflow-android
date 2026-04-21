package com.valdesekamdem.taskflow.feature.main.viewmodel

import com.valdesekamdem.taskflow.core.navigation.api.TabScreen

sealed interface MainUiEvent {
    data class TabSelected(val screen: TabScreen) : MainUiEvent
    data object BackPressed : MainUiEvent
}