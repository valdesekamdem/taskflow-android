package com.valdesekamdem.taskflow.core.navigation.real

import com.valdesekamdem.taskflow.core.navigation.api.Back
import com.valdesekamdem.taskflow.core.navigation.api.Navigator
import com.valdesekamdem.taskflow.core.navigation.api.Screen
import com.valdesekamdem.taskflow.core.navigation.api.TabScreen
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealNavigator @Inject constructor() :
    Navigator, NavigationEventSource, TabNavigationEventSource {

    private val outerEventChannel = Channel<NavigationEvent>(capacity = Channel.BUFFERED)
    private val tabEventChannel = Channel<TabNavigationEvent>(capacity = Channel.BUFFERED)

    override val events: Flow<NavigationEvent> = outerEventChannel.receiveAsFlow()
    override val tabEvents: Flow<TabNavigationEvent> = tabEventChannel.receiveAsFlow()

    override fun goTo(screen: Screen) {
        when (screen) {
            is Back -> check(outerEventChannel.trySend(NavigationEvent.Back).isSuccess) {
                "Failed to enqueue navigation command: Back"
            }
            is TabScreen -> {
                check(tabEventChannel.trySend(TabNavigationEvent.NavigateTo(screen)).isSuccess) {
                    "Failed to enqueue navigation command: NavigateTo($screen)"
                }
                check(outerEventChannel.trySend(NavigationEvent.PopToRoot).isSuccess) {
                    "Failed to enqueue navigation command: PopToRoot"
                }
            }
            else -> check(outerEventChannel.trySend(NavigationEvent.NavigateTo(screen)).isSuccess) {
                "Failed to enqueue navigation command: NavigateTo($screen)"
            }
        }
    }
}
