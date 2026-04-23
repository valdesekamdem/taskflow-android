package com.valdesekamdem.taskflow.feature.tasks.viewmodel

import app.cash.turbine.test
import com.valdesekamdem.taskflow.core.clock.fakes.FakeClock
import com.valdesekamdem.taskflow.core.clock.utils.DefaultLocaleRule
import com.valdesekamdem.taskflow.core.model.Category
import com.valdesekamdem.taskflow.core.model.Priority
import com.valdesekamdem.taskflow.core.model.Task
import com.valdesekamdem.taskflow.core.navigation.fakes.FakeNavigator
import com.valdesekamdem.taskflow.feature.task.data.fakes.FakeTaskRepository
import com.valdesekamdem.taskflow.feature.task.screens.TaskDetailScreen
import com.valdesekamdem.taskflow.feature.tasks.fixtures.TasksFixtures
import com.valdesekamdem.taskflow.ui.model.TaskUiModel
import com.valdesekamdem.taskflow.utils.test
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.time.ZoneId
import kotlin.time.Instant

class TasksViewModelTest {
    @get:Rule
    val defaultLocaleRule = DefaultLocaleRule()

    private val navigator = FakeNavigator()
    private val taskRepository = FakeTaskRepository()
    private val clock = FakeClock()
    private val zoneId = ZoneId.of("America/Toronto")

    private fun createViewModel() = TasksViewModel(
        navigator = navigator,
        taskRepository = taskRepository,
        clock = clock,
        zoneId = zoneId,
    )

    @Test
    fun `uiState initializes with empty task list`() = runTest {
        createViewModel().test {
            assertEquals(TasksUiState(tasks = emptyList()), uiState.value)
        }
    }

    @Test
    fun `uiState maps repository tasks to TaskUiModel list`() = runTest {
        val task = Task(
            id = 1,
            title = "Very rapid test",
            description = "Description",
            priority = Priority.High,
            category = Category.Work,
            completedAt = null,
            createdAt = Instant.parse("2026-01-01T10:00:00.00Z"),
        )

        createViewModel().uiState.test {
            assertEquals(TasksUiState(tasks = emptyList()), awaitItem())

            taskRepository.tasks.send(listOf(task))

            assertEquals(
                TasksUiState(
                    tasks = listOf(
                        TaskUiModel(
                            id = 1,
                            title = "Very rapid test",
                            description = "Description",
                            priority = Priority.High,
                            category = "Work",
                            dueDateText = "",
                            isTaskOverdue = false,
                            isCompleted = false,
                        ),
                    ),
                ),
                awaitItem(),
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

        createViewModel().uiState.test {
            awaitItem()
            taskRepository.tasks.send(listOf(task))

            with(awaitItem().tasks.first()) {
                assertEquals("Yesterday", dueDateText)
                assertEquals(true, isTaskOverdue)
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

        createViewModel().uiState.test {
            awaitItem()
            taskRepository.tasks.send(listOf(task))

            with(awaitItem().tasks.first()) {
                assertEquals("", dueDateText)
                assertEquals(false, isTaskOverdue)
            }
        }
    }

    @Test
    fun `TaskClicked event navigates to TaskDetailScreen`() = runTest {
        createViewModel().test {
            val task = TasksFixtures.tasks.first().copy(id = 42)
            onUiEvent(TasksUiEvent.TaskClicked(task))

            assertEquals(TaskDetailScreen(task.id), navigator.screens.awaitItem())
        }
    }

    @Test
    fun `TaskCheckboxToggled on incomplete task calls markTaskCompleted`() = runTest {
        createViewModel().test {
            val task = TasksFixtures.tasks.first().copy(id = 7, isCompleted = false)
            onUiEvent(TasksUiEvent.TaskCheckboxToggled(task))
            assertEquals(task.id, taskRepository.markTaskCompletedCalls.awaitItem())
        }
    }

    @Test
    fun `TaskCheckboxToggled on completed task calls unmarkTaskCompleted`() = runTest {
        createViewModel().test {
            val task = TasksFixtures.tasks.first().copy(id = 7, isCompleted = true)
            onUiEvent(TasksUiEvent.TaskCheckboxToggled(task))
            assertEquals(task.id, taskRepository.unmarkTaskCompletedCalls.awaitItem())
        }
    }
}
