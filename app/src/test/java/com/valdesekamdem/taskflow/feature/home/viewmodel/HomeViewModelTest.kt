package com.valdesekamdem.taskflow.feature.home.viewmodel

import app.cash.turbine.awaitItem
import app.cash.turbine.test
import com.valdesekamdem.taskflow.core.clock.fakes.FakeClock
import com.valdesekamdem.taskflow.core.clock.utils.DefaultLocaleRule
import com.valdesekamdem.taskflow.core.model.Category
import com.valdesekamdem.taskflow.core.model.Priority
import com.valdesekamdem.taskflow.core.model.Task
import com.valdesekamdem.taskflow.core.navigation.fakes.FakeNavigator
import com.valdesekamdem.taskflow.feature.home.fixtures.HomeFixtures
import com.valdesekamdem.taskflow.feature.task.data.api.filter.DateFilter
import com.valdesekamdem.taskflow.feature.task.data.api.filter.TaskFilter
import com.valdesekamdem.taskflow.feature.task.data.fakes.FakeTaskRepository
import com.valdesekamdem.taskflow.feature.task.screens.EditTaskScreen
import com.valdesekamdem.taskflow.feature.task.screens.TaskDetailScreen
import com.valdesekamdem.taskflow.ui.model.TaskUiModel
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
    fun `uiState maps repository due tasks while preserving injected todayDate`() = runTest {
        val task = Task(
            id = 1,
            title = "Very rapid test",
            description = "Description",
            priority = Priority.High,
            category = Category.Work,
            completedAt = null,
            createdAt = Instant.parse("2026-01-01T10:00:00.00Z"),
        )

        viewModel.uiState.test {
            assertEquals(HomeUiState(todayDate = "January 01"), awaitItem())

            taskRepository.filteredTasks.send(listOf(task))

            assertEquals(
                HomeUiState(
                    todayDate = "January 01",
                    overdueTasks = listOf(
                        TaskUiModel(
                            id = 1,
                            title = "Very rapid test",
                            description = "Description",
                            priority = Priority.High,
                            category = "Work",
                            dueDateText = "",
                            isTaskOverdue = false,
                            isCompleted = false,
                        )
                    ),
                ),
                awaitItem(),
            )
            assertEquals(
                TaskFilter(
                    dueDate = DateFilter.Before(Instant.parse("2026-01-01T05:00:00.00Z")),
                    isCompleted = false,
                ),
                taskRepository.getTasksFilterCalls.awaitItem(),
            )
        }
    }

    @Test
    fun `uiState populates dueDateText with relative text when task has dueDate`() = runTest {
        val task = Task(
            id = 1,
            title = "Task with due date",
            description = "Description",
            priority = Priority.Low,
            category = Category.Personal,
            dueDate = Instant.parse("2025-12-31T12:00:00.00Z"),
            completedAt = null,
            createdAt = Instant.parse("2026-01-01T10:00:00.00Z"),
        )

        viewModel.uiState.test {
            awaitItem()
            taskRepository.filteredTasks.send(listOf(task))

            with(awaitItem().overdueTasks.first()) {
                assertEquals("Yesterday", dueDateText)
                assertEquals(true, isTaskOverdue)
            }
        }
    }

    @Test
    fun `uiState marks isDueDateOverdue false when dueDate is in the future`() = runTest {
        val task = Task(
            id = 1,
            title = "Future task",
            description = "Description",
            priority = Priority.Low,
            category = Category.Personal,
            dueDate = Instant.parse("2026-01-02T12:00:00.00Z"),
            completedAt = null,
            createdAt = Instant.parse("2026-01-01T10:00:00.00Z"),
        )

        viewModel.uiState.test {
            awaitItem()
            taskRepository.filteredTasks.send(listOf(task))

            with(awaitItem().overdueTasks.first()) {
                assertEquals("Tomorrow", dueDateText)
                assertEquals(false, isTaskOverdue)
            }
        }
    }

    @Test
    fun `uiState leaves dueDateText empty when task has no dueDate`() = runTest {
        val task = Task(
            id = 1,
            title = "No due date task",
            description = "Description",
            priority = Priority.Low,
            category = Category.Personal,
            dueDate = null,
            completedAt = null,
            createdAt = Instant.parse("2026-01-01T10:00:00.00Z"),
        )

        viewModel.uiState.test {
            awaitItem()
            taskRepository.filteredTasks.send(listOf(task))

            with(awaitItem().overdueTasks.first()) {
                assertEquals("", dueDateText)
                assertEquals(false, isTaskOverdue)
            }
        }
    }

    @Test
    fun `TaskClicked event navigates to TaskDetailScreen`() = runTest {
        viewModel.test {
            val task = HomeFixtures.tasks.first().copy(id = 42)
            onUiEvent(HomeUiEvent.TaskClicked(task))

            assertEquals(TaskDetailScreen(task.id), navigator.screens.awaitItem())
        }
    }

    @Test
    fun `NewTaskClicked event navigate EditTaskScreen`() = runTest {
        viewModel.test {
            onUiEvent(HomeUiEvent.NewTaskClicked)
            assertEquals(EditTaskScreen(null), navigator.screens.awaitItem())
        }
    }

    @Test
    fun `TaskCheckboxToggled on incomplete task calls markTaskCompleted`() = runTest {
        viewModel.test {
            val task = HomeFixtures.tasks.first().copy(id = 7, isCompleted = false)
            onUiEvent(HomeUiEvent.TaskCheckboxToggled(task))
            assertEquals(task.id, taskRepository.markTaskCompletedCalls.awaitItem())
        }
    }
}
