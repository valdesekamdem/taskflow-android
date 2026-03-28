package com.valdesekamdem.taskflow.feature.task.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valdesekamdem.taskflow.feature.task.viewmodel.EditTaskUiEvent
import com.valdesekamdem.taskflow.feature.task.viewmodel.EditTaskUiState
import com.valdesekamdem.taskflow.ui.theme.Spacing
import com.valdesekamdem.taskflow.ui.theme.TaskflowTheme

@Composable
fun EditTask(
    uiState: EditTaskUiState,
    onUiEvent: (EditTaskUiEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.medium)
    ) {
        EditTaskFormContent(
            form = uiState.form,
            onTitleChanged = { onUiEvent(EditTaskUiEvent.OnTitleChanged(it)) },
            onDescriptionChanged = { onUiEvent(EditTaskUiEvent.OnDescriptionChanged(it)) },
            onSubmit = { onUiEvent(EditTaskUiEvent.OnSubmit) },
            isSubmitting = uiState.isSubmitting,
        )
    }
}

@Composable
fun EditTaskFormContent(
    form: EditTaskUiState.EditTaskForm,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
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
                capitalization = KeyboardCapitalization.Words,
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
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onSubmit,
            enabled = !isSubmitting,
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (isSubmitting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(Modifier.width(Spacing.small))
            }
            Text("Save")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditTaskFormPreview() {
    TaskflowTheme {
        EditTaskFormContent(
            form = EditTaskUiState.EditTaskForm(),
            onTitleChanged = {},
            onDescriptionChanged = {},
            onSubmit = {},
            isSubmitting = false,
            modifier = Modifier.padding(Spacing.medium)
        )
    }
}