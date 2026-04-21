package com.valdesekamdem.taskflow.core.navigation.real

import app.cash.turbine.test
import com.valdesekamdem.taskflow.core.navigation.api.Back
import com.valdesekamdem.taskflow.feature.task.screens.TaskDetailScreen
import com.valdesekamdem.taskflow.feature.tasks.screens.TasksScreen
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class RealNavigatorTest {

    private fun createNavigator() = RealNavigator()

    @Test
    fun `goTo Back emits NavigationEvent Back on events`() = runTest {
        val navigator = createNavigator()
        navigator.events.test {
            navigator.goTo(Back)
            assertEquals(NavigationEvent.Back, awaitItem())
        }
    }

    @Test
    fun `goTo non-tab screen emits NavigationEvent NavigateTo on events`() = runTest {
        val navigator = createNavigator()
        val screen = TaskDetailScreen(id = 1)
        navigator.events.test {
            navigator.goTo(screen)
            assertEquals(NavigationEvent.NavigateTo(screen), awaitItem())
        }
    }

    @Test
    fun `goTo tab screen emits TabNavigationEvent NavigateTo on tabEvents`() = runTest {
        val navigator = createNavigator()
        navigator.tabEvents.test {
            navigator.goTo(TasksScreen)
            assertEquals(TabNavigationEvent.NavigateTo(TasksScreen), awaitItem())
        }
    }

    @Test
    fun `goTo tab screen emits NavigationEvent PopToRoot on events`() = runTest {
        val navigator = createNavigator()
        navigator.events.test {
            navigator.goTo(TasksScreen)
            assertEquals(NavigationEvent.PopToRoot, awaitItem())
        }
    }
}
