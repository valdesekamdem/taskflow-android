package com.valdesekamdem.taskflow.feature.home.viewmodel

import com.valdesekamdem.taskflow.ui.components.Priority

data class TaskUiModel(
    val id: String,
    val title: String,
    val description: String,
    val priority: Priority,
    val category: String,
    val dueDateText: String,
)
