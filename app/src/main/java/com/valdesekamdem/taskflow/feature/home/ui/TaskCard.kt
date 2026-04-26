package com.valdesekamdem.taskflow.feature.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valdesekamdem.taskflow.R
import com.valdesekamdem.taskflow.feature.home.fixtures.HomeFixtures
import com.valdesekamdem.taskflow.ui.model.TaskUiModel
import com.valdesekamdem.taskflow.ui.theme.Spacing
import com.valdesekamdem.taskflow.ui.theme.TaskflowTheme
import com.valdesekamdem.taskflow.ui.utils.accentColor

private val PriorityDotSize = 10.dp
private val CheckboxSize = 20.dp

// Aligns description/metadata with the title: checkbox + spacer + dot + spacer
private val ContentIndent = CheckboxSize + Spacing.medium + PriorityDotSize + Spacing.small

@Composable
fun TaskCard(
    task: TaskUiModel,
    onClick: () -> Unit,
    onCheckboxToggled: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val mutedColor = MaterialTheme.colorScheme.onSurfaceVariant

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = Spacing.medium, vertical = Spacing.medium),
        verticalArrangement = Arrangement.spacedBy(Spacing.xsmall),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                modifier = Modifier.size(CheckboxSize),
                checked = task.isCompleted,
                onCheckedChange = { onCheckboxToggled() },
            )
            Spacer(Modifier.width(Spacing.medium))
            Box(
                modifier = Modifier
                    .size(PriorityDotSize)
                    .background(task.priority.accentColor(), shape = CircleShape)
            )
            Spacer(Modifier.width(Spacing.small))
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                ),
                color = if (task.isCompleted) mutedColor else MaterialTheme.colorScheme.onSurface,
            )
        }

        Text(
            modifier = Modifier.padding(start = ContentIndent),
            text = task.description,
            style = MaterialTheme.typography.bodySmall,
            color = mutedColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        if (task.dueDateText.isNotBlank()) {
            Row(
                modifier = Modifier.padding(start = ContentIndent),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.xsmall),
            ) {
                val dueDateColor = if (!task.isCompleted && task.isTaskOverdue) {
                    MaterialTheme.colorScheme.error
                } else {
                    mutedColor
                }

                Icon(
                    painter = painterResource(R.drawable.schedule_24),
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = dueDateColor,
                )

                Text(
                    text = task.dueDateText,
                    style = MaterialTheme.typography.labelSmall,
                    color = dueDateColor,
                )

                Text(
                    text = "•",
                    style = MaterialTheme.typography.labelSmall,
                    color = mutedColor,
                )

                Text(
                    text = task.category,
                    style = MaterialTheme.typography.labelSmall,
                    color = mutedColor,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskCardOverduePreview() {
    TaskflowTheme {
        TaskCard(
            task = HomeFixtures.tasks.first(),
            onClick = {},
            onCheckboxToggled = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TaskCardUpcomingPreview() {
    TaskflowTheme {
        TaskCard(
            task = HomeFixtures.tasks[1],
            onClick = {},
            onCheckboxToggled = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TaskCardCompletedPreview() {
    TaskflowTheme {
        TaskCard(
            task = HomeFixtures.tasks[2],
            onClick = {},
            onCheckboxToggled = {},
        )
    }
}
