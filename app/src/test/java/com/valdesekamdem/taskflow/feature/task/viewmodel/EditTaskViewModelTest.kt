package com.valdesekamdem.taskflow.feature.task.viewmodel

import app.cash.turbine.test
import com.valdesekamdem.taskflow.core.clock.utils.DefaultLocaleRule
import com.valdesekamdem.taskflow.core.clock.utils.fromUtcToInstant
import com.valdesekamdem.taskflow.core.model.Category
import com.valdesekamdem.taskflow.core.model.Priority
import com.valdesekamdem.taskflow.core.model.Task
import com.valdesekamdem.taskflow.core.navigation.api.Back
import com.valdesekamdem.taskflow.core.navigation.fakes.FakeNavigator
import com.valdesekamdem.taskflow.feature.task.data.api.TaskModel
import com.valdesekamdem.taskflow.feature.task.data.api.TaskRepository
import com.valdesekamdem.taskflow.feature.task.data.fakes.FakeTaskRepository
import com.valdesekamdem.taskflow.feature.task.screens.EditTaskScreen
import com.valdesekamdem.taskflow.feature.task.viewmodel.EditTaskUiState.EditTaskForm
import com.valdesekamdem.taskflow.utils.skipItem
import com.valdesekamdem.taskflow.utils.test
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.time.ZoneId
import kotlin.time.Instant

class EditTaskViewModelTest {
    @get:Rule
    val defaultLocaleRule = DefaultLocaleRule()

    private val navigator = FakeNavigator()
    private val taskRepository = FakeTaskRepository()
    private val zoneId = ZoneId.of("UTC")
    private val createScreen = EditTaskScreen(id = null)

    private fun createViewModel(
        screen: EditTaskScreen = EditTaskScreen(id = null),
    ) = EditTaskViewModel(
        navigator = navigator,
        taskRepository = taskRepository,
        zoneId = zoneId,
        screen = screen,
    )

    private fun buildTask() = Task(
        id = 42,
        title = "Buy groceries",
        description = "Milk and eggs",
        priority = Priority.High,
        category = Category.Work,
        dueDate = Instant.parse("2026-03-15T00:00:00.00Z"),
        isCompleted = false,
        createdAt = Instant.parse("2026-01-01T00:00:00.00Z"),
    )

    @Test
    fun `new task screen title is New task`() = runTest {
        assertEquals("NEW TASK", createViewModel().uiState.value.title)
    }

    @Test
    fun `navigates to Back screen when received CloseClicked event`() = runTest {
        createViewModel().test {
            onUiEvent(EditTaskUiEvent.CloseClicked)
            assertEquals(Back, navigator.screens.awaitItem())
        }
    }

    @Test
    fun `form is updated when received text fields events`() = runTest {
        val viewModel = createViewModel()
        viewModel.uiState.test {
            with(awaitItem().form) {
                assertEquals(EditTaskForm(), this)
                assertFalse(this.isFormValid)
            }

            viewModel.onUiEvent(EditTaskUiEvent.TitleChanged("Call J"))
            with(awaitItem().form) {
                assertEquals(EditTaskForm("Call J"), this)
                assertTrue(this.isFormValid)
            }

            viewModel.onUiEvent(EditTaskUiEvent.DescriptionChanged("Desc"))
            with(awaitItem().form) {
                assertEquals(EditTaskForm(title = "Call J", description = "Desc"), this)
                assertTrue(this.isFormValid)
            }

            viewModel.onUiEvent(EditTaskUiEvent.PriorityChanged(Priority.Medium))
            with(awaitItem().form) {
                assertEquals(
                    EditTaskForm(
                        title = "Call J",
                        description = "Desc",
                        priority = Priority.Medium
                    ), this
                )
                assertTrue(this.isFormValid)
            }
        }
    }

    @Test
    fun `viewmodel passes data to the task repository on submit`() = runTest {
        createViewModel().test {
            onUiEvent(EditTaskUiEvent.TitleChanged("Call KKV tomorrow"))
            onUiEvent(EditTaskUiEvent.DescriptionChanged("Desc"))
            onUiEvent(EditTaskUiEvent.PriorityChanged(Priority.High))

            onUiEvent(EditTaskUiEvent.SubmitForm)
            assertTrue(uiState.value.isSubmitting)
            val expectedTaskModel = TaskModel(
                title = "Call KKV tomorrow",
                description = "Desc",
                category = Category.Personal,
                priority = Priority.High,
                dueDate = null,
            )
            assertEquals(expectedTaskModel, taskRepository.addTaskCalls.awaitItem())

            assertFalse(uiState.value.isSubmitting)
            assertEquals(Back, navigator.screens.awaitItem())
        }
    }

    @Test
    fun `CategoryChanged event updates form category`() = runTest {
        val viewModel = createViewModel()
        viewModel.uiState.test {
            assertEquals(Category.Personal, awaitItem().form.category)
            viewModel.onUiEvent(EditTaskUiEvent.CategoryChanged(Category.Work))
            assertEquals(Category.Work, awaitItem().form.category)
        }
    }

    @Test
    fun `DueDateChanged with valid timestamp sets dueDate and formattedDueDate`() = runTest {
        // 2026-03-15T00:00:00Z → local date March 15 → start of March 15 in UTC = same instant
        val epochMillis = Instant.parse("2026-03-15T00:00:00.00Z").toEpochMilliseconds()
        val expectedInstant = epochMillis.fromUtcToInstant(zoneId)
        val viewModel = createViewModel()

        viewModel.uiState.test {
            skipItem("Initial state")
            viewModel.onUiEvent(EditTaskUiEvent.DueDateChanged(epochMillis))
            with(awaitItem().form) {
                assertEquals(expectedInstant, dueDate)
                assertEquals("March 15, 2026", formattedDueDate)
            }
        }
    }

    @Test
    fun `DueDateChanged with null clears dueDate and formattedDueDate`() = runTest {
        val epochMillis = Instant.parse("2026-03-15T00:00:00.00Z").toEpochMilliseconds()
        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem() // initial state
            viewModel.onUiEvent(EditTaskUiEvent.DueDateChanged(epochMillis))
            awaitItem() // state with due date set

            viewModel.onUiEvent(EditTaskUiEvent.DueDateChanged(null))
            with(awaitItem().form) {
                assertNull(dueDate)
                assertEquals("", formattedDueDate)
            }
        }
    }

    @Test
    fun `submit failure resets isSubmitting without navigating`() = runTest {
        val throwingRepository = object : TaskRepository {
            override suspend fun addTask(taskModel: TaskModel): Unit =
                throw RuntimeException("Database error")

            override suspend fun updateTask(id: Int, taskModel: TaskModel): Unit =
                throw RuntimeException("Database error")

            override fun getTasks(): Flow<List<Task>> = emptyFlow()
            override fun getTask(id: Int): Flow<Task?> = emptyFlow()
            override suspend fun deleteTask(id: Int) {}
        }
        val viewModel = EditTaskViewModel(
            navigator = navigator,
            taskRepository = throwingRepository,
            zoneId = zoneId,
            screen = createScreen,
        )

        viewModel.uiState.test {
            skipItem("Initial state")
            viewModel.onUiEvent(EditTaskUiEvent.TitleChanged("Some task"))
            skipItem("Title updated")
            viewModel.onUiEvent(EditTaskUiEvent.SubmitForm)

            assertTrue(awaitItem().isSubmitting)
            assertFalse(awaitItem().isSubmitting)
            navigator.screens.expectNoEvents()
        }
    }

    @Test
    fun `edit task screen title is Edit task`() = runTest {
        val viewModel = createViewModel(EditTaskScreen(id = 42))
        taskRepository.task.send(buildTask())

        assertEquals("EDIT TASK", viewModel.uiState.value.title)
    }

    @Test
    fun `edit mode fetches task and prepopulates the form`() = runTest {
        val task = buildTask()
        val viewModel = createViewModel(EditTaskScreen(id = 42))

        viewModel.uiState.test {
            skipItem("Initial empty state")
            taskRepository.task.send(task)

            assertEquals(42, taskRepository.getTaskCalls.receive())
            with(awaitItem().form) {
                assertEquals(task.title, title)
                assertEquals(task.description, description)
                assertEquals(task.priority, priority)
                assertEquals(task.category, category)
                assertEquals(task.dueDate, dueDate)
                assertEquals("March 15, 2026", formattedDueDate)
            }
        }
    }

    @Test
    fun `submit in edit mode calls updateTask with correct data`() = runTest {
        val task = buildTask()
        val viewModel = createViewModel(EditTaskScreen(id = 42))

        viewModel.uiState.test {
            skipItem("Initial empty state")
            taskRepository.task.send(task)
            skipItem("Form prepopulated")

            viewModel.onUiEvent(EditTaskUiEvent.SubmitForm)
            assertTrue(awaitItem().isSubmitting)

            val (id, model) = taskRepository.updateTaskCalls.awaitItem()
            assertEquals(42, id)
            assertEquals(
                TaskModel(
                    title = task.title,
                    description = task.description,
                    category = task.category,
                    priority = task.priority,
                    dueDate = task.dueDate,
                ),
                model,
            )

            assertFalse(awaitItem().isSubmitting)
            assertEquals(Back, navigator.screens.awaitItem())
        }
    }

    @Test
    fun `submit in edit mode does not call addTask`() = runTest {
        val task = buildTask()
        val viewModel = createViewModel(EditTaskScreen(id = 42))

        viewModel.uiState.test {
            skipItem("Initial empty state")
            taskRepository.task.send(task)
            skipItem("Form prepopulated")

            viewModel.onUiEvent(EditTaskUiEvent.SubmitForm)
            skipItem("isSubmitting=true")
            taskRepository.updateTaskCalls.awaitItem()
            skipItem("isSubmitting=false")
            taskRepository.addTaskCalls.expectNoEvents()
        }
    }
}
