package com.valdesekamdem.taskflow.feature.task.data.api

import com.valdesekamdem.taskflow.core.model.Category
import com.valdesekamdem.taskflow.core.model.Priority
import com.valdesekamdem.taskflow.core.model.Task
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant

interface TaskRepository {
    suspend fun addTask(taskModel: TaskModel)

    fun getTasks(): Flow<List<Task>>

    suspend fun getTask(id: Long): Result<Task?>
}

data class TaskModel(
    val title: String,
    val description: String,
    val category: Category,
    val priority: Priority,
    val dueDate: Instant?,
)