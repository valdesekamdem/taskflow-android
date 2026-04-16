package com.valdesekamdem.taskflow.ui.utils

import androidx.compose.ui.graphics.Color
import com.valdesekamdem.taskflow.core.model.Priority
import com.valdesekamdem.taskflow.ui.theme.PriorityHigh
import com.valdesekamdem.taskflow.ui.theme.PriorityLow
import com.valdesekamdem.taskflow.ui.theme.PriorityMedium

private const val alpha = .2f

fun Priority.accentColor(): Color = when (this) {
    Priority.Low -> PriorityLow
    Priority.Medium -> PriorityMedium
    Priority.High -> PriorityHigh
}

fun Priority.backgroundColor(): Color = this.accentColor().copy(alpha = alpha)
