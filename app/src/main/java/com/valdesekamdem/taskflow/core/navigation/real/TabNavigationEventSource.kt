package com.valdesekamdem.taskflow.core.navigation.real

import com.valdesekamdem.taskflow.core.navigation.api.TabScreen
import kotlinx.coroutines.flow.Flow

interface TabNavigationEventSource {
    val tabEvents: Flow<TabNavigationEvent>
}

sealed interface TabNavigationEvent {
    data class NavigateTo(val screen: TabScreen) : TabNavigationEvent
}
