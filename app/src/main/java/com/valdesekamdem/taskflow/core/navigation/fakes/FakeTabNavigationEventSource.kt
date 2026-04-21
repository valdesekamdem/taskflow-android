package com.valdesekamdem.taskflow.core.navigation.fakes

import com.valdesekamdem.taskflow.core.navigation.real.TabNavigationEvent
import com.valdesekamdem.taskflow.core.navigation.real.TabNavigationEventSource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class FakeTabNavigationEventSource : TabNavigationEventSource {
    val tabEventsChannel = Channel<TabNavigationEvent>(capacity = Channel.BUFFERED)
    override val tabEvents: Flow<TabNavigationEvent> = tabEventsChannel.receiveAsFlow()
}
