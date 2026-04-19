package com.valdesekamdem.taskflow.core.model

import kotlin.time.Instant

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val priority: Priority,
    val category: Category,
    val dueDate: Instant? = null,
    val reminder: Instant? = null,
    val completedAt: Instant? = null,
    val createdAt: Instant,
    val updatedAt: Instant? = null,
    val notes: String? = null,
)
