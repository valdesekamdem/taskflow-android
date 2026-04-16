package com.valdesekamdem.taskflow.feature.task.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valdesekamdem.taskflow.core.model.Category
import com.valdesekamdem.taskflow.core.model.Priority
import com.valdesekamdem.taskflow.feature.task.viewmodel.EditTaskUiEvent
import com.valdesekamdem.taskflow.feature.task.viewmodel.EditTaskUiState
import com.valdesekamdem.taskflow.ui.components.PriorityCard
import com.valdesekamdem.taskflow.ui.components.SelectCategoryTextField
import com.valdesekamdem.taskflow.ui.components.topappbar.NavigationType
import com.valdesekamdem.taskflow.ui.components.topappbar.TitleTopAppBar
import com.valdesekamdem.taskflow.ui.theme.Spacing
import com.valdesekamdem.taskflow.ui.theme.TaskflowTheme

@Composable
fun EditTask(
    uiState: EditTaskUiState,
    onUiEvent: (EditTaskUiEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            TitleTopAppBar(
                title = uiState.title,
                navigationType = NavigationType.CLOSE,
                onNavigationClicked = { onUiEvent(EditTaskUiEvent.CloseClicked) },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            EditTaskFormContent(
                form = uiState.form,
                onTitleChanged = { onUiEvent(EditTaskUiEvent.TitleChanged(it)) },
                onDescriptionChanged = { onUiEvent(EditTaskUiEvent.DescriptionChanged(it)) },
                onCategoryChanged = { onUiEvent(EditTaskUiEvent.CategoryChanged(it)) },
                onPriorityChanged = { onUiEvent(EditTaskUiEvent.PriorityChanged(it)) },
                onSubmit = { onUiEvent(EditTaskUiEvent.SubmitForm) },
                isSubmitting = uiState.isSubmitting,
                modifier = Modifier.padding(horizontal = Spacing.medium),
            )
        }
    }
}

@Composable
fun EditTaskFormContent(
    form: EditTaskUiState.EditTaskForm,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onCategoryChanged: (Category) -> Unit,
    onPriorityChanged: (Priority) -> Unit,
    onSubmit: () -> Unit,
    isSubmitting: Boolean,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(Spacing.medium)
    ) {
        OutlinedTextField(
            value = form.title,
            onValueChange = onTitleChanged,
            label = { Text("Title") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = form.description,
            onValueChange = onDescriptionChanged,
            label = { Text("Description") },
            minLines = 3,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        SelectCategoryTextField(
            selectedCategory = form.category,
            label = "Category",
            onChanged = onCategoryChanged,
            modifier = Modifier.fillMaxWidth()
        )

        FormRow(
            title = "Priority",
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectableGroup(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.small),
            ) {
                Priority.entries.forEach { priority ->
                    PriorityCard(
                        priority = priority,
                        onClick = { onPriorityChanged(priority) },
                        isSelected = form.priority == priority,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Button(
            onClick = onSubmit,
            enabled = form.isFormValid && !isSubmitting,
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (isSubmitting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(Modifier.size(Spacing.small))
            }
            Text("Save")
        }
    }
}

@Composable
private fun FormRow(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = Spacing.xsmall)
        )

        content()
    }
}

@Preview(showBackground = true)
@Composable
fun EditTaskPreview() {
    TaskflowTheme {
        EditTask(
            uiState = EditTaskUiState(
                title = "New task",
                form = EditTaskUiState.EditTaskForm(),
            ),
            onUiEvent = {}
        )
    }
}
