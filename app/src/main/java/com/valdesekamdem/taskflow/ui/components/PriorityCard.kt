package com.valdesekamdem.taskflow.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valdesekamdem.taskflow.core.model.Priority
import com.valdesekamdem.taskflow.ui.theme.Spacing
import com.valdesekamdem.taskflow.ui.theme.TaskflowTheme
import com.valdesekamdem.taskflow.ui.utils.accentColor
import com.valdesekamdem.taskflow.ui.utils.backgroundColor

@Composable
fun PriorityCard(
    priority: Priority,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
) {
    val accentColor = priority.accentColor()
    val borderColor = if (isSelected) accentColor else MaterialTheme.colorScheme.outlineVariant
    val containerColor =
        if (isSelected) priority.backgroundColor() else MaterialTheme.colorScheme.surface
    val textColor = if (isSelected) accentColor else MaterialTheme.colorScheme.onSurfaceVariant

    OutlinedCard(
        onClick = onClick,
        colors = CardDefaults.outlinedCardColors(
            containerColor = containerColor,
        ),
        border = BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = borderColor
        ),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Spacing.medium)
        ) {
            Box(
                modifier = Modifier
                    .background(color = accentColor, shape = CircleShape)
                    .size(10.dp)
            )
            Spacer(modifier = Modifier.height(Spacing.xsmall))
            Text(
                text = priority.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = textColor,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PriorityCardPreview() {
    TaskflowTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.medium)) {
            PriorityCard(
                priority = Priority.Low,
                onClick = {},
                isSelected = false,
                modifier = Modifier.weight(1f)
            )

            PriorityCard(
                priority = Priority.Medium,
                onClick = {},
                isSelected = false,
                modifier = Modifier.weight(1f)
            )

            PriorityCard(
                priority = Priority.High,
                onClick = {},
                isSelected = true,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
