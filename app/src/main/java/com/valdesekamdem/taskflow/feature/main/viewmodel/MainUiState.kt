package com.valdesekamdem.taskflow.feature.main.viewmodel

import com.valdesekamdem.taskflow.core.navigation.api.TabScreen
import com.valdesekamdem.taskflow.feature.home.screens.HomeScreen

data class MainUiState(
    val tabBackStack: List<TabScreen> = listOf(HomeScreen),
)
