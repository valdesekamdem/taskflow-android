package com.valdesekamdem.taskflow.feature.task.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valdesekamdem.taskflow.R
import com.valdesekamdem.taskflow.feature.task.viewmodel.EditTaskUiEvent
import com.valdesekamdem.taskflow.feature.task.viewmodel.EditTaskUiState
import com.valdesekamdem.taskflow.ui.components.topappbar.NavigationType
import com.valdesekamdem.taskflow.ui.components.topappbar.TitleTopAppBar
import com.valdesekamdem.taskflow.ui.theme.Spacing
import com.valdesekamdem.taskflow.ui.theme.TaskflowTheme

@Composable
fun EditTask(
    uiState: EditTaskUiState,
    onUiEvent: (EditTaskUiEvent) -> Unit,
) {
    val screenTitle = if (uiState.isNewTask) {
        stringResource(R.string.edit_task_new_title)
    } else {
        stringResource(R.string.edit_task_edit_title)
    }
    val submitLabel = if (uiState.isNewTask) {
        stringResource(R.string.edit_task_create_button)
    } else {
        stringResource(R.string.save)
    }

    Scaffold(
        topBar = {
            TitleTopAppBar(
                title = screenTitle,
                navigationType = NavigationType.CLOSE,
                onNavigationClicked = { onUiEvent(EditTaskUiEvent.CloseClicked) },
                actions = {
                    TextButton(
                        onClick = { onUiEvent(EditTaskUiEvent.SubmitForm) },
                        enabled = uiState.form.isFormValid && !uiState.isSubmitting,
                        modifier = Modifier.padding(end = Spacing.small),
                    ) {
                        if (uiState.isSubmitting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(Spacing.medium),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        } else {
                            Text(submitLabel)
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        EditTaskForm(
            form = uiState.form,
            onTitleChanged = { onUiEvent(EditTaskUiEvent.TitleChanged(it)) },
            onDescriptionChanged = { onUiEvent(EditTaskUiEvent.DescriptionChanged(it)) },
            onCategoryChanged = { onUiEvent(EditTaskUiEvent.CategoryChanged(it)) },
            onPriorityChanged = { onUiEvent(EditTaskUiEvent.PriorityChanged(it)) },
            onDueDateChanged = { onUiEvent(EditTaskUiEvent.DueDateChanged(it)) },
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditTaskPreview() {
    TaskflowTheme {
        EditTask(
            uiState = EditTaskUiState(
                isNewTask = true,
                form = EditTaskUiState.EditTaskFormUiModel(),
            ),
            onUiEvent = {}
        )
    }
}
