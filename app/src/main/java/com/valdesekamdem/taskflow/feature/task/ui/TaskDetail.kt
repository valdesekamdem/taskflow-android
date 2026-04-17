package com.valdesekamdem.taskflow.feature.task.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.valdesekamdem.taskflow.R
import com.valdesekamdem.taskflow.core.model.Category
import com.valdesekamdem.taskflow.core.model.Priority
import com.valdesekamdem.taskflow.feature.task.viewmodel.TaskDetailUiState
import com.valdesekamdem.taskflow.ui.components.PriorityBadge
import com.valdesekamdem.taskflow.ui.components.TaskInfoCard
import com.valdesekamdem.taskflow.ui.components.topappbar.NavigationType
import com.valdesekamdem.taskflow.ui.components.topappbar.TitleTopAppBar
import com.valdesekamdem.taskflow.ui.theme.Spacing
import com.valdesekamdem.taskflow.ui.theme.TaskflowTheme

@Composable
fun TaskDetail(
    uiState: TaskDetailUiState,
) {
    Scaffold(
        topBar = {
            TitleTopAppBar(
                title = stringResource(R.string.task_detail_title),
                navigationType = NavigationType.BACK,
                onNavigationClicked = { },
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

        }
    }
}

@Composable
private fun Content(
    uiState: TaskDetailUiState,
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
            uiState = TaskDetailUiState(
                title = "Finalize Q1 report",
                description = "Review all quarterly numbers with the finance team. Make sure revenue projections align with the updated forecast model.",
                priority = Priority.Medium,
                dueDate = TaskDetailUiState.DueDate(
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
