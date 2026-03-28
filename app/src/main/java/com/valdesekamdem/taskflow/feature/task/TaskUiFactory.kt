package com.valdesekamdem.taskflow.feature.task

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.valdesekamdem.taskflow.core.presentation.BindScreen
import com.valdesekamdem.taskflow.core.presentation.UiFactory
import com.valdesekamdem.taskflow.feature.task.screens.EditTaskScreen
import com.valdesekamdem.taskflow.feature.task.screens.TaskDetailScreen
import com.valdesekamdem.taskflow.feature.task.ui.EditTask
import com.valdesekamdem.taskflow.feature.task.ui.TaskDetail
import com.valdesekamdem.taskflow.feature.task.viewmodel.EditTaskViewModel
import com.valdesekamdem.taskflow.feature.task.viewmodel.TaskDetailViewModel
import javax.inject.Inject

class TaskUiFactory @Inject constructor() : UiFactory {

    override fun register(scope: EntryProviderScope<NavKey>) = with(scope) {
        entry<TaskDetailScreen> { screen ->
            BindScreen(
                stateHolder = hiltViewModel<TaskDetailViewModel, TaskDetailViewModel.Factory> { factory ->
                    factory.create(screen)
                }
            ) { uiState, _ ->
                TaskDetail(uiState = uiState)
            }
        }
        entry<EditTaskScreen> {
            BindScreen(
                stateHolder = hiltViewModel<EditTaskViewModel>()
            ) { uiState, onUiEvent ->
                EditTask(uiState = uiState, onUiEvent = onUiEvent)
            }
        }
    }
}
