package com.valdesekamdem.taskflow.feature.home.viewmodel

import app.cash.turbine.test
import com.valdesekamdem.taskflow.core.clock.fakes.FakeClock
import com.valdesekamdem.taskflow.core.clock.utils.DefaultLocaleRule
import com.valdesekamdem.taskflow.core.model.Category
import com.valdesekamdem.taskflow.core.model.Priority
import com.valdesekamdem.taskflow.core.model.Task
import com.valdesekamdem.taskflow.core.navigation.fakes.FakeNavigator
import com.valdesekamdem.taskflow.feature.home.fixtures.HomeFixtures
import com.valdesekamdem.taskflow.feature.task.data.fakes.FakeTaskRepository
import com.valdesekamdem.taskflow.feature.task.screens.EditTaskScreen
import com.valdesekamdem.taskflow.feature.task.screens.TaskDetailScreen
import com.valdesekamdem.taskflow.utils.test
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.time.ZoneId
import kotlin.time.Instant

class HomeViewModelTest {
    @get:Rule
    val defaultLocaleRule = DefaultLocaleRule()

    private val navigator = FakeNavigator()
    private val taskRepository = FakeTaskRepository()
    private val clock = FakeClock()
    private val zoneId = ZoneId.of("America/Toronto")

    private val viewModel = HomeViewModel(
        navigator = navigator,
        taskRepository = taskRepository,
        clock = clock,
        zoneId = zoneId,
    )

    @Test
    fun `uiState initializes todayDate from injected clock`() = runTest {
        viewModel.test {
            assertEquals(HomeUiState(todayDate = "January 01"), uiState.value)
        }
    }

    @Test
    fun `uiState maps repository tasks while preserving injected todayDate`() = runTest {
        val task = Task(
            id = 1,
            title = "Very rapid test",
            description = "Description",
            priority = Priority.High,
            category = Category.Work,
            isCompleted = false,
            createdAt = Instant.parse("2026-01-01T10:00:00.00Z"),
        )

        viewModel.uiState.test {
            assertEquals(HomeUiState(todayDate = "January 01"), awaitItem())

            taskRepository.tasks.send(listOf(task))

            assertEquals(
                HomeUiState(
                    todayDate = "January 01",
                    tasks = listOf(
                        TaskUiModel(
                            id = "1",
                            title = "Very rapid test",
                            description = "Description",
                            priority = Priority.High,
                            category = "Work",
                            dueDateText = "",
                        )
                    ),
                ),
                awaitItem(),
            )
        }
    }

    @Test
    fun `TaskClicked event navigates to TaskDetailScreen`() = runTest {
        viewModel.test {
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
