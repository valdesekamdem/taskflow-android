package com.valdesekamdem.taskflow.feature.task.viewmodel

import app.cash.turbine.TurbineTestContext
import app.cash.turbine.test
import com.valdesekamdem.taskflow.core.clock.fakes.FakeClock
import com.valdesekamdem.taskflow.core.clock.utils.DefaultLocaleRule
import com.valdesekamdem.taskflow.core.model.Category
import com.valdesekamdem.taskflow.core.model.Priority
import com.valdesekamdem.taskflow.core.model.Task
import com.valdesekamdem.taskflow.core.navigation.api.Back
import com.valdesekamdem.taskflow.core.navigation.fakes.FakeNavigator
import com.valdesekamdem.taskflow.feature.task.data.fakes.FakeTaskRepository
import com.valdesekamdem.taskflow.feature.task.screens.EditTaskScreen
import com.valdesekamdem.taskflow.feature.task.screens.TaskDetailScreen
import com.valdesekamdem.taskflow.feature.task.viewmodel.TaskDetailUiEvent.BackClicked
import com.valdesekamdem.taskflow.feature.task.viewmodel.TaskDetailUiEvent.DeleteCancelled
import com.valdesekamdem.taskflow.feature.task.viewmodel.TaskDetailUiEvent.DeleteClicked
import com.valdesekamdem.taskflow.feature.task.viewmodel.TaskDetailUiEvent.DeleteConfirmed
import com.valdesekamdem.taskflow.feature.task.viewmodel.TaskDetailUiEvent.EditClicked
import com.valdesekamdem.taskflow.feature.task.viewmodel.TaskDetailUiEvent.GoHomeClicked
import com.valdesekamdem.taskflow.feature.task.viewmodel.TaskDetailUiEvent.MarkCompleteClicked
import com.valdesekamdem.taskflow.feature.task.viewmodel.TaskDetailUiEvent.UnmarkCompleteClicked
import com.valdesekamdem.taskflow.utils.skipItem
import com.valdesekamdem.taskflow.utils.test
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
    private val navigator = FakeNavigator()
    private val taskRepository = FakeTaskRepository()
    private val zoneId = ZoneId.of("UTC")
    private val screen = TaskDetailScreen(id = 1)

    private val viewModel = TaskDetailViewModel(
        navigator = navigator,
        taskRepository = taskRepository,
        clock = clock,
        zoneId = zoneId,
        screen = screen,
    )

    private suspend fun TurbineTestContext<TaskDetailUiState>.loadTask(task: Task): TaskDetailUiState.Content {
        skipItem("Initial state")
        taskRepository.task.send(task)
        return awaitItem() as TaskDetailUiState.Content
    }

    private fun buildTask(dueDate: Instant? = null, completedAt: Instant? = null) = Task(
        id = 1,
        title = "Buy groceries",
        description = "Milk and eggs",
        priority = Priority.High,
        category = Category.Personal,
        completedAt = completedAt,
        createdAt = Instant.parse("2026-01-01T10:00:00.00Z"),
        dueDate = dueDate,
    )

    @Test
    fun `navigates to Back screen when BackClicked event is received`() = runTest {
        viewModel.test {
            onUiEvent(BackClicked)
            assertEquals(Back, navigator.screens.awaitItem())
        }
    }

    @Test
    fun `navigates to EditTaskScreen with correct id when EditClicked event is received`() =
        runTest {
            viewModel.test {
                onUiEvent(EditClicked)
                assertEquals(EditTaskScreen(id = screen.id), navigator.screens.awaitItem())
            }
        }

    @Test
    fun `navigates to Back screen when GoHomeClicked event is received`() = runTest {
        viewModel.test {
            onUiEvent(GoHomeClicked)
            assertEquals(Back, navigator.screens.awaitItem())
        }
    }

    @Test
    fun `successful task load maps all fields to uiState`() = runTest {
        val dueDate = Instant.parse("2026-01-15T10:00:00.00Z")
        val task = buildTask(dueDate = dueDate)

        viewModel.uiState.test {
            val actualState = loadTask(task)

            val expectedState = TaskDetailUiState.Content(
                title = "Buy groceries",
                description = "Milk and eggs",
                priority = Priority.High,
                dueDate = TaskDetailUiState.Content.DueDate(
                    date = "January 15, 2026",
                    countDown = "Due in 14 days",
                    isOverdue = false
                ),
                category = Category.Personal,
                tasksInCategory = null,
                createdAt = "January 1, 2026",
                reminder = "-",
                isCompleted = false,
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

    @Test
    fun `showDeleteConfirmation is false on initial content state`() = runTest {
        viewModel.uiState.test {
            val content = loadTask(buildTask(dueDate = clock.now))
            assertFalse(content.showDeleteConfirmation)
        }
    }

    @Test
    fun `showDeleteConfirmation becomes true when DeleteClicked event is received`() = runTest {
        viewModel.uiState.test {
            loadTask(buildTask(dueDate = clock.now))

            viewModel.onUiEvent(DeleteClicked)
            assertTrue((awaitItem() as TaskDetailUiState.Content).showDeleteConfirmation)
        }
    }

    @Test
    fun `showDeleteConfirmation becomes false when DeleteCancelled event is received`() = runTest {
        viewModel.uiState.test {
            loadTask(buildTask(dueDate = clock.now))

            viewModel.onUiEvent(DeleteClicked)
            skipItem("showDeleteConfirmation=true")

            viewModel.onUiEvent(DeleteCancelled)
            assertFalse((awaitItem() as TaskDetailUiState.Content).showDeleteConfirmation)
        }
    }

    @Test
    fun `DeleteConfirmed calls deleteTask on the repository with the correct id`() = runTest {
        taskRepository.task.send(buildTask(dueDate = clock.now))
        viewModel.onUiEvent(DeleteConfirmed)
        assertEquals(screen.id, taskRepository.deleteTaskCalls.awaitItem())
    }

    @Test
    fun `DeleteConfirmed resets showDeleteConfirmation before transitioning to Deleted`() =
        runTest {
            viewModel.uiState.test {
                loadTask(buildTask(dueDate = clock.now))

                viewModel.onUiEvent(DeleteClicked)
                skipItem("showDeleteConfirmation=true")

                viewModel.onUiEvent(DeleteConfirmed)
                skipItem("showDeleteConfirmation reset to false")
                taskRepository.deleteTaskCalls.awaitItem()

                assertEquals(TaskDetailUiState.Deleted, awaitItem())
            }
        }

    @Test
    fun `uiState transitions to Deleted after DeleteConfirmed`() = runTest {
        viewModel.uiState.test {
            loadTask(buildTask(dueDate = clock.now))

            viewModel.onUiEvent(DeleteConfirmed)
            taskRepository.deleteTaskCalls.awaitItem()

            assertEquals(TaskDetailUiState.Deleted, awaitItem())
        }
    }

    @Test
    fun `isCompleted is false in uiState when task completedAt is null`() = runTest {
        viewModel.uiState.test {
            val content = loadTask(buildTask(dueDate = clock.now, completedAt = null))
            assertFalse(content.isCompleted)
        }
    }

    @Test
    fun `isCompleted is true in uiState when task completedAt is set`() = runTest {
        viewModel.uiState.test {
            val content = loadTask(buildTask(dueDate = clock.now, completedAt = clock.now))
            assertTrue(content.isCompleted)
        }
    }

    @Test
    fun `MarkCompleteClicked calls markTaskCompleted with correct id`() = runTest {
        taskRepository.task.send(buildTask(dueDate = clock.now))
        viewModel.onUiEvent(MarkCompleteClicked)
        assertEquals(screen.id, taskRepository.markTaskCompletedCalls.awaitItem())
    }

    @Test
    fun `UnmarkCompleteClicked calls unmarkTaskCompleted with correct id`() = runTest {
        taskRepository.task.send(buildTask(dueDate = clock.now))
        viewModel.onUiEvent(UnmarkCompleteClicked)
        assertEquals(screen.id, taskRepository.unmarkTaskCompletedCalls.awaitItem())
    }
}
