package com.valdesekamdem.taskflow.feature.task.viewmodel

import app.cash.turbine.test
import com.valdesekamdem.taskflow.core.navigation.api.Back
import com.valdesekamdem.taskflow.core.navigation.fakes.FakeNavigator
import com.valdesekamdem.taskflow.feature.task.data.api.TaskModel
import com.valdesekamdem.taskflow.feature.task.data.fakes.FakeTaskRepository
import com.valdesekamdem.taskflow.feature.task.viewmodel.EditTaskUiState.EditTaskForm
import com.valdesekamdem.taskflow.utils.test
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class EditTaskViewModelTest {
    private val navigator = FakeNavigator()
    private val taskRepository = FakeTaskRepository()

    private val viewModel = EditTaskViewModel(
        navigator = navigator,
        taskRepository = taskRepository,
    )

    @Test
    fun `navigates to Back screen when received CloseClicked event`() = runTest {
        viewModel.test {
            onUiEvent(EditTaskUiEvent.CloseClicked)
            assertEquals(Back, navigator.screens.awaitItem())
        }
    }

    @Test
    fun `form is updated when received text fields events`() = runTest {
        viewModel.uiState.test {
            assertEquals(EditTaskForm(), awaitItem().form)

            viewModel.onUiEvent(EditTaskUiEvent.TitleChanged("Call J"))
            assertEquals(EditTaskForm("Call J"), awaitItem().form)

            viewModel.onUiEvent(EditTaskUiEvent.DescriptionChanged("Desc"))
            assertEquals(EditTaskForm(title = "Call J", description = "Desc"), awaitItem().form)
        }
    }

    @Test
    fun `viewmodel passes data to the task repository on submit`() = runTest {
        viewModel.test {
            viewModel.onUiEvent(EditTaskUiEvent.TitleChanged("Call KKV tomorrow"))
            viewModel.onUiEvent(EditTaskUiEvent.DescriptionChanged("Desc"))

            onUiEvent(EditTaskUiEvent.SubmitForm)
            val expectedTaskModel = TaskModel(title = "Call KKV tomorrow", description = "Desc")
            assertEquals(expectedTaskModel, taskRepository.addTaskCalls.awaitItem())
        }
    }
}