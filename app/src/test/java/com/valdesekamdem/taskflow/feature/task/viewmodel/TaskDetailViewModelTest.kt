package com.valdesekamdem.taskflow.feature.task.viewmodel

import app.cash.turbine.TurbineTestContext
import app.cash.turbine.test
import com.valdesekamdem.taskflow.core.clock.fakes.FakeClock
import com.valdesekamdem.taskflow.core.clock.utils.DefaultLocaleRule
import com.valdesekamdem.taskflow.core.model.Category
import com.valdesekamdem.taskflow.core.model.Priority
import com.valdesekamdem.taskflow.core.model.Task
import com.valdesekamdem.taskflow.feature.task.data.fakes.FakeTaskRepository
import com.valdesekamdem.taskflow.feature.task.screens.TaskDetailScreen
import com.valdesekamdem.taskflow.utils.skipItem
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.time.ZoneId
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant

class TaskDetailViewModelTest {
    @get:Rule
    val defaultLocaleRule = DefaultLocaleRule()

    // FakeClock defaults to 2026-01-01T10:00:00Z
    private val clock = FakeClock()
    private val taskRepository = FakeTaskRepository()
    private val zoneId = ZoneId.of("UTC")
    private val screen = TaskDetailScreen(id = "1", title = "Test Task")

    private val viewModel = TaskDetailViewModel(
        taskRepository = taskRepository,
        clock = clock,
        zoneId = zoneId,
        screen = screen,
    )

    private suspend fun <T> TurbineTestContext<T>.loadTask(task: Task): T {
        skipItem("Initial state")
        taskRepository.task.add(Result.success(task))
        return awaitItem()
    }

    private fun buildTask(dueDate: Instant? = null) = Task(
        id = 1,
        title = "Buy groceries",
        description = "Milk and eggs",
        priority = Priority.High,
        category = Category.Personal,
        isCompleted = false,
        createdAt = Instant.parse("2026-01-01T10:00:00.00Z"),
        dueDate = dueDate,
    )

    @Test
    fun `successful task load maps all fields to uiState`() = runTest {
        val dueDate = Instant.parse("2026-01-15T10:00:00.00Z")
        val task = buildTask(dueDate = dueDate)

        viewModel.uiState.test {
            val actualState = loadTask(task)

            val expectedState = TaskDetailUiState(
                title = "Buy groceries",
                description = "Milk and eggs",
                priority = Priority.High,
                dueDate = TaskDetailUiState.DueDate(
                    date = "January 15, 2026",
                    countDown = "Due in 14 days",
                    isOverdue = false
                ),
                category = Category.Personal,
                tasksInCategory = null,
                createdAt = "January 1, 2026",
                reminder = "-",
            )

            assertEquals(expectedState, actualState)
        }
    }

    @Test
    fun `dueDate in the future shows Due in N days countdown`() = runTest {
        val dueDate = clock.now + 5.days
        val task = buildTask(dueDate = dueDate)

        viewModel.uiState.test {
            val dueDateState = loadTask(task).dueDate

            assertEquals("Due in 5 days", dueDateState.countDown)
            assertFalse(dueDateState.isOverdue)
        }
    }

    @Test
    fun `dueDate in the past shows Overdue N days ago countdown`() = runTest {
        val dueDate = clock.now - 3.days
        val task = buildTask(dueDate = dueDate)

        viewModel.uiState.test {
            val dueDateState = loadTask(task).dueDate

            assertEquals("Overdue 3 days ago", dueDateState.countDown)
            assertTrue(dueDateState.isOverdue)
        }
    }

    @Test
    fun `dueDate equal to now shows Overdue today countdown`() = runTest {
        val task = buildTask(dueDate = clock.now)

        viewModel.uiState.test {
            val dueDateState = loadTask(task).dueDate

            assertEquals("Overdue today", dueDateState.countDown)
            assertFalse(dueDateState.isOverdue)
        }
    }
}
