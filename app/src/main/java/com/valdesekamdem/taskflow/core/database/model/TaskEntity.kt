package com.valdesekamdem.taskflow.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.valdesekamdem.taskflow.core.model.Category
import com.valdesekamdem.taskflow.core.model.Priority
import kotlin.time.Instant

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val title: String,
    val description: String,
    val priority: Priority,
    val category: Category,
    @ColumnInfo(name = "due_date") val dueDate: Instant?,
    val reminder: Instant?,
    @ColumnInfo(name = "is_completed") val isCompleted: Boolean,
    @ColumnInfo(name = "created_at") val createdAt: Instant,
    @ColumnInfo(name = "updated_at") val updatedAt: Instant?,
    val notes: String?,
)
