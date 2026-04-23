package com.valdesekamdem.taskflow.feature.tasks.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.valdesekamdem.taskflow.R
import com.valdesekamdem.taskflow.feature.home.ui.TaskCard
import com.valdesekamdem.taskflow.feature.tasks.fixtures.TasksFixtures
import com.valdesekamdem.taskflow.feature.tasks.viewmodel.TasksUiEvent
import com.valdesekamdem.taskflow.feature.tasks.viewmodel.TasksUiEvent.TaskCheckboxToggled
import com.valdesekamdem.taskflow.feature.tasks.viewmodel.TasksUiEvent.TaskClicked
import com.valdesekamdem.taskflow.feature.tasks.viewmodel.TasksUiState
import com.valdesekamdem.taskflow.ui.components.topappbar.MainTopAppBar
import com.valdesekamdem.taskflow.ui.theme.Spacing
import com.valdesekamdem.taskflow.ui.theme.TaskflowTheme

@Composable
fun Tasks(
    uiState: TasksUiState,
    onUiEvent: (TasksUiEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            MainTopAppBar(title = stringResource(R.string.tasks_title))
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            itemsIndexed(
                items = uiState.tasks,
                key = { _, task -> task.hashCode() },
            ) { index, task ->
                if (index > 0) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = Spacing.medium),
                        color = DividerDefaults.color.copy(0.6f),
                    )
                }
                TaskCard(
                    task = task,
                    onClick = { onUiEvent(TaskClicked(task)) },
                    onCheckboxToggled = { onUiEvent(TaskCheckboxToggled(task)) },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TasksPreview() {
    TaskflowTheme {
        Tasks(uiState = TasksFixtures.tasksUiState, onUiEvent = {})
    }
}
