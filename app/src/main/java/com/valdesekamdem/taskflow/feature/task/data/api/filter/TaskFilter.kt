package com.valdesekamdem.taskflow.feature.task.data.api.filter

data class TaskFilter(
    val dueDate: DateFilter? = null,
    val isCompleted: Boolean? = null,
)