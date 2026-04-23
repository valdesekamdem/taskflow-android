package com.valdesekamdem.taskflow.ui.model

import com.valdesekamdem.taskflow.core.clock.utils.toRelativeDateText
import com.valdesekamdem.taskflow.core.model.Task
import java.time.ZoneId
import kotlin.time.Instant

fun List<Task>.toTaskUiModels(now: Instant, zoneId: ZoneId) = map { it.toTaskUiModel(now, zoneId) }

fun Task.toTaskUiModel(now: Instant, zoneId: ZoneId) = TaskUiModel(
    id = id,
    title = title,
    description = description,
    priority = priority,
    category = category.name,
    dueDateText = dueDate?.toRelativeDateText(now, zoneId) ?: "",
    isTaskOverdue = dueDate?.let { it < now } ?: false,
    isCompleted = completedAt != null,
)
