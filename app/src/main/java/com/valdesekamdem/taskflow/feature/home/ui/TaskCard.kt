package com.valdesekamdem.taskflow.feature.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valdesekamdem.taskflow.feature.home.fixtures.HomeFixtures
import com.valdesekamdem.taskflow.feature.home.viewmodel.TaskUiModel
import com.valdesekamdem.taskflow.ui.components.CategoryBadge
import com.valdesekamdem.taskflow.ui.components.PriorityBadge
import com.valdesekamdem.taskflow.ui.theme.Rounded
import com.valdesekamdem.taskflow.ui.theme.Spacing
import com.valdesekamdem.taskflow.ui.theme.TaskflowTheme
import com.valdesekamdem.taskflow.ui.utils.accentColor

@Composable
fun TaskCard(
    task: TaskUiModel,
    onClick: () -> Unit,
) {
    OutlinedCard(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(
            topStart = 0.dp,
            bottomStart = 0.dp,
            topEnd = Rounded.medium,
            bottomEnd = Rounded.medium,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(Spacing.xsmall)
                    .background(task.priority.accentColor())
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.medium)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                )
                Text(
                    text = task.description,
                    maxLines = 1,
                )

                Spacer(Modifier.padding(top = Spacing.xsmall))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.small)
                ) {
                    PriorityBadge(task.priority)
                    CategoryBadge(task.category)

                    Text(
                        text = task.dueDateText,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun TaskCardPreview() {
    TaskflowTheme {
        TaskCard(
            task = HomeFixtures.tasks.first(),
            onClick = {}
        )
    }
}
