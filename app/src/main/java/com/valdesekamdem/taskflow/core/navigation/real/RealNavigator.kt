package com.valdesekamdem.taskflow.core.navigation.real

import com.valdesekamdem.taskflow.core.navigation.api.Navigator
import com.valdesekamdem.taskflow.core.navigation.api.Screen
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealNavigator @Inject constructor(): Navigator, NavigationEventSource {
    private val eventChannel = Channel<NavigationEvent>(capacity = Channel.BUFFERED)
    override val events: Flow<NavigationEvent> = eventChannel.receiveAsFlow()

    override fun goTo(screen: Screen) {
        check(eventChannel.trySend(NavigationEvent.NavigateTo(screen)).isSuccess) {
            "Failed to enqueue navigation command: NavigateTo($screen)"
        }
    }

    override fun getBack() {
        check(eventChannel.trySend(NavigationEvent.Back).isSuccess) {
            "Failed to enqueue navigation command: Back"
        }
    }
}
