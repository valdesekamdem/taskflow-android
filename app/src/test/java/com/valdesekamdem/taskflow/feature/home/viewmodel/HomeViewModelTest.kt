package com.valdesekamdem.taskflow.feature.home.viewmodel

import com.valdesekamdem.taskflow.core.navigation.fakes.FakeNavigator
import com.valdesekamdem.taskflow.feature.home.fixtures.HomeFixtures
import com.valdesekamdem.taskflow.feature.task.screens.EditTaskScreen
import com.valdesekamdem.taskflow.feature.task.screens.TaskDetailScreen
import com.valdesekamdem.taskflow.utils.test
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class HomeViewModelTest {
    private val navigator = FakeNavigator()
    private val viewModel = HomeViewModel(navigator)

    @Test
    fun `TaskClicked event navigates to TaskDetailScreen`() = runTest {
        viewModel.test {
            assertEquals(HomeUiState(HomeFixtures.tasks), uiState.value)

            val task = HomeFixtures.tasks.first().copy(
                id = "task-id",
                title = "Very rapid test"
            )
            onUiEvent(HomeUiEvent.TaskClicked(task))

            assertEquals(TaskDetailScreen(task.id, task.title), navigator.screens.awaitItem())
        }
    }

    @Test
    fun `NewTaskClicked event navigate EditTaskScreen`() = runTest {
        viewModel.test {
            onUiEvent(HomeUiEvent.NewTaskClicked)
            assertEquals(EditTaskScreen(null), navigator.screens.awaitItem())
        }
    }
}
