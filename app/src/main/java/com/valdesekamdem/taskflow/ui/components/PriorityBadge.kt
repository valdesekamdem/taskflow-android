package com.valdesekamdem.taskflow.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.valdesekamdem.taskflow.core.model.Priority
import com.valdesekamdem.taskflow.ui.utils.accentColor

const val alpha = .2f

@Composable
fun PriorityBadge(
    priority: Priority,
) {
    val (textColor, backgroundColor) = when (priority) {
        Priority.Low -> priority.accentColor() to priority.accentColor().copy(alpha = alpha)
        Priority.Medium -> priority.accentColor() to priority.accentColor().copy(alpha = alpha)
        Priority.High -> priority.accentColor() to priority.accentColor().copy(alpha = alpha)
    }

    Badge(
        text = priority.name,
        textColor = textColor,
        background = backgroundColor,
        fontWeight = FontWeight.ExtraBold,
    )
}

@Preview(showBackground = true)
@Composable
fun PriorityBadgePreview() {
    PriorityBadge(Priority.High)
}