package com.valdesekamdem.taskflow.feature.task.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.valdesekamdem.taskflow.feature.task.viewmodel.TaskDetailUiState
import com.valdesekamdem.taskflow.ui.theme.Spacing
import com.valdesekamdem.taskflow.ui.theme.TaskflowTheme

@Composable
fun TaskDetail(
    uiState: TaskDetailUiState,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.medium)
    ) {
        Text(text = uiState.title)
    }
}

@Preview(showBackground = true)
@Composable
fun TaskDetailPreview() {
    TaskflowTheme {
        TaskDetail(uiState = TaskDetailUiState(title = "Task Detail"))
    }
}