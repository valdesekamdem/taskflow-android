package com.valdesekamdem.taskflow.feature.task.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valdesekamdem.taskflow.R
import com.valdesekamdem.taskflow.core.model.Category
import com.valdesekamdem.taskflow.core.model.Priority
import com.valdesekamdem.taskflow.feature.task.viewmodel.EditTaskUiState.EditTaskFormUiModel
import com.valdesekamdem.taskflow.ui.components.DatePickerModal
import com.valdesekamdem.taskflow.ui.components.FormRow
import com.valdesekamdem.taskflow.ui.components.FormSelectorCard
import com.valdesekamdem.taskflow.ui.components.SelectorBottomSheet
import com.valdesekamdem.taskflow.ui.components.TextField
import com.valdesekamdem.taskflow.ui.theme.Spacing
import com.valdesekamdem.taskflow.ui.theme.TaskflowTheme
import com.valdesekamdem.taskflow.ui.utils.accentColor

@Composable
fun EditTaskForm(
    form: EditTaskFormUiModel,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onCategoryChanged: (Category) -> Unit,
    onPriorityChanged: (Priority) -> Unit,
    onDueDateChanged: (Long?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    var activeSheet by remember { mutableStateOf<ActiveSheet?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(Spacing.medium)
            .verticalScroll(scrollState),
    ) {
        FormRow(
            label = stringResource(R.string.edit_task_title_label),
            required = true,
        ) {
            TextField(
                value = form.title,
                onValueChange = onTitleChanged,
                singleLine = true,
                textStyle = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium,
                placeholder = stringResource(R.string.edit_task_title_placeholder),
            )
        }

        Spacer(modifier = Modifier.size(Spacing.large))

        FormRow(
            label = stringResource(R.string.edit_task_description_label),
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = form.description,
                onValueChange = onDescriptionChanged,
                minLines = 2,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                ),
                placeholder = stringResource(R.string.edit_task_description_placeholder),
            )
        }

        Spacer(modifier = Modifier.size(Spacing.large))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.medium),
        ) {
            FormSelectorCard(
                label = stringResource(R.string.edit_task_due_date_label),
                value = form.formattedDueDate.ifEmpty { "—" },
                icon = painterResource(R.drawable.schedule_24),
                onClick = { showDatePicker = true },
                modifier = Modifier.weight(1f),
            )
            FormSelectorCard(
                label = stringResource(R.string.edit_task_category_label),
                icon = painterResource(R.drawable.folder_24),
                value = form.category.name,
                onClick = { activeSheet = ActiveSheet.Category },
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.size(Spacing.medium))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.medium),
        ) {
            FormSelectorCard(
                label = stringResource(R.string.edit_task_priority_label),
                icon = painterResource(R.drawable.priority_24),
                value = form.priority.name,
                valueColor = form.priority.accentColor(),
                onClick = { activeSheet = ActiveSheet.Priority },
                modifier = Modifier.weight(1f),
            )
            FormSelectorCard(
                label = stringResource(R.string.edit_task_reminder_label),
                icon = painterResource(R.drawable.reminder_24),
                value = stringResource(R.string.edit_task_reminder_no_value),
                enabled = false,
                modifier = Modifier.weight(1f),
            )
        }
    }

    if (showDatePicker) {
        DatePickerModal(
            selectedDateMillis = form.dueDate?.toEpochMilliseconds(),
            onDateSelected = onDueDateChanged,
            onDismiss = { showDatePicker = false },
        )
    }

    when (activeSheet) {
        ActiveSheet.Category -> SelectorBottomSheet(
            title = stringResource(R.string.edit_task_choose_category),
            items = Category.entries,
            selectedItem = form.category,
            itemContent = { Text(it.name) },
            onItemSelected = onCategoryChanged,
            onDismiss = { activeSheet = null },
        )

        ActiveSheet.Priority -> SelectorBottomSheet(
            title = stringResource(R.string.edit_task_choose_priority),
            items = Priority.entries,
            selectedItem = form.priority,
            itemContent = { priority ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.medium),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(priority.accentColor(), shape = CircleShape)
                    )

                    Text(
                        text = priority.name,
                    )
                }
            },
            onItemSelected = onPriorityChanged,
            onDismiss = { activeSheet = null },
        )

        null -> Unit
    }
}

private sealed interface ActiveSheet {
    data object Category : ActiveSheet
    data object Priority : ActiveSheet
}

@Preview(showBackground = true)
@Composable
fun EditTaskFormPreview() {
    TaskflowTheme {
        EditTaskForm(
            form = EditTaskFormUiModel(),
            onTitleChanged = {},
            onDescriptionChanged = {},
            onCategoryChanged = {},
            onPriorityChanged = {},
            onDueDateChanged = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
