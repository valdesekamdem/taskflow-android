package com.valdesekamdem.taskflow.feature.task.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valdesekamdem.taskflow.R
import com.valdesekamdem.taskflow.core.model.Category
import com.valdesekamdem.taskflow.core.model.Priority
import com.valdesekamdem.taskflow.feature.task.viewmodel.TaskDetailUiEvent
import com.valdesekamdem.taskflow.feature.task.viewmodel.TaskDetailUiEvent.BackClicked
import com.valdesekamdem.taskflow.feature.task.viewmodel.TaskDetailUiEvent.DeleteCancelled
import com.valdesekamdem.taskflow.feature.task.viewmodel.TaskDetailUiEvent.DeleteClicked
import com.valdesekamdem.taskflow.feature.task.viewmodel.TaskDetailUiEvent.DeleteConfirmed
import com.valdesekamdem.taskflow.feature.task.viewmodel.TaskDetailUiEvent.EditClicked
import com.valdesekamdem.taskflow.feature.task.viewmodel.TaskDetailUiEvent.GoHomeClicked
import com.valdesekamdem.taskflow.feature.task.viewmodel.TaskDetailUiEvent.MarkCompleteClicked
import com.valdesekamdem.taskflow.feature.task.viewmodel.TaskDetailUiEvent.UnmarkCompleteClicked
import com.valdesekamdem.taskflow.feature.task.viewmodel.TaskDetailUiState
import com.valdesekamdem.taskflow.feature.task.viewmodel.TaskDetailUiState.Content
import com.valdesekamdem.taskflow.feature.task.viewmodel.TaskDetailUiState.Deleted
import com.valdesekamdem.taskflow.ui.components.PriorityBadge
import com.valdesekamdem.taskflow.ui.components.TaskInfoCard
import com.valdesekamdem.taskflow.ui.components.topappbar.NavigationType
import com.valdesekamdem.taskflow.ui.components.topappbar.TitleTopAppBar
import com.valdesekamdem.taskflow.ui.theme.Spacing
import com.valdesekamdem.taskflow.ui.theme.TaskflowTheme

@Composable
fun TaskDetail(
    uiState: TaskDetailUiState,
    onUiEvent: (TaskDetailUiEvent) -> Unit,
) {
    when (uiState) {
        is Content -> ContentScreen(uiState, onUiEvent)
        is Deleted -> DeletedScreen(onUiEvent)
    }
}

@Composable
private fun ContentScreen(
    uiState: Content,
    onUiEvent: (TaskDetailUiEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            TitleTopAppBar(
                title = stringResource(R.string.task_detail_title),
                navigationType = NavigationType.BACK,
                onNavigationClicked = { onUiEvent(BackClicked) },
                actions = {
                    IconButton({ onUiEvent(EditClicked) }) {
                        Icon(
                            painter = painterResource(R.drawable.edit_24),
                            contentDescription = stringResource(R.string.task_detail_edit_icon_description),
                        )
                    }
                    IconButton({ onUiEvent(DeleteClicked) }) {
                        Icon(
                            painter = painterResource(R.drawable.delete_24),
                            contentDescription = stringResource(R.string.task_detail_delete_icon_description),
                            tint = MaterialTheme.colorScheme.error,
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Content(
                uiState = uiState,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = Spacing.medium)
            )

            if (uiState.isCompleted) {
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Spacing.medium),
                    onClick = { onUiEvent(UnmarkCompleteClicked) },
                ) {
                    Text(stringResource(R.string.task_detail_unmark_complete))
                }
            } else {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Spacing.medium),
                    onClick = { onUiEvent(MarkCompleteClicked) },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.check_24),
                        contentDescription = null,
                    )
                    Spacer(Modifier.width(Spacing.small))
                    Text(stringResource(R.string.task_detail_mark_complete))
                }
            }
        }
    }

    if (uiState.showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { onUiEvent(DeleteCancelled) },
            title = { Text(stringResource(R.string.task_detail_delete_dialog_title)) },
            text = { Text(stringResource(R.string.task_detail_delete_dialog_message)) },
            confirmButton = {
                TextButton(onClick = { onUiEvent(DeleteConfirmed) }) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { onUiEvent(DeleteCancelled) }) {
                    Text(stringResource(R.string.cancel))
                }
            },
        )
    }
}

@Composable
private fun DeletedScreen(
    onUiEvent: (TaskDetailUiEvent) -> Unit,
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(Spacing.medium),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    painter = painterResource(R.drawable.delete_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(48.dp),
                )
                Spacer(Modifier.height(Spacing.medium))
                Text(
                    text = stringResource(R.string.task_detail_deleted_title),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.height(Spacing.small))
                Text(
                    text = stringResource(R.string.task_detail_deleted_message),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(Spacing.large))
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onUiEvent(GoHomeClicked) },
            ) {
                Text(stringResource(R.string.task_detail_deleted_go_home))
            }
        }
    }
}

@Composable
private fun Content(
    uiState: Content,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(Spacing.medium)
    ) {
        PriorityBadge(uiState.priority)

        Text(
            text = uiState.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

        uiState.description?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.small),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(Spacing.small)
            ) {
                TaskInfoCard(
                    label = stringResource(R.string.edit_task_due_date_label),
                    value = uiState.dueDate.date,
                    subtitle = uiState.dueDate.countDown,
                    isError = uiState.dueDate.isOverdue,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
                TaskInfoCard(
                    label = stringResource(R.string.edit_task_category_label),
                    value = uiState.category.name,
                    subtitle = uiState.tasksInCategory,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(Spacing.small)
            ) {
                TaskInfoCard(
                    label = stringResource(R.string.task_detail_created_label),
                    value = uiState.createdAt,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
                TaskInfoCard(
                    label = stringResource(R.string.task_detail_reminder_label),
                    value = uiState.reminder,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskDetailPreview() {
    TaskflowTheme {
        TaskDetail(
            onUiEvent = {},
            uiState = Content(
                title = "Finalize Q1 report",
                description = "Review all quarterly numbers with the finance team. Make sure revenue projections align with the updated forecast model.",
                priority = Priority.Medium,
                dueDate = Content.DueDate(
                    date = "Mar 24, 2026",
                    countDown = "2 days overdue",
                    isOverdue = true
                ),
                category = Category.Work,
                tasksInCategory = "12 tasks in category",
                createdAt = "Mar 15, 2026",
                reminder = "Mar 23, 9AM"
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TaskDetailDeleteConfirmationPreview() {
    TaskflowTheme {
        TaskDetail(
            onUiEvent = {},
            uiState = Content(
                title = "Finalize Q1 report",
                description = null,
                priority = Priority.Medium,
                dueDate = Content.DueDate(
                    date = "Mar 24, 2026",
                    countDown = null,
                    isOverdue = false
                ),
                category = Category.Work,
                tasksInCategory = null,
                createdAt = "Mar 15, 2026",
                reminder = "-",
                showDeleteConfirmation = true,
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TaskDetailDeletedPreview() {
    TaskflowTheme {
        TaskDetail(
            onUiEvent = {},
            uiState = Deleted,
        )
    }
}
