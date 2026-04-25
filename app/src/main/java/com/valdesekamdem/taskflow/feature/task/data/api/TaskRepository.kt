package com.valdesekamdem.taskflow.feature.task.data.api

import com.valdesekamdem.taskflow.core.model.Category
import com.valdesekamdem.taskflow.core.model.Priority
import com.valdesekamdem.taskflow.core.model.Task
import com.valdesekamdem.taskflow.feature.task.data.api.filter.TaskFilter
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant

interface TaskRepository {
    suspend fun addTask(taskModel: TaskModel)

    suspend fun updateTask(id: Int, taskModel: TaskModel)

    fun getTasks(): Flow<List<Task>>

    fun getTasks(filter: TaskFilter): Flow<List<Task>>

    fun getTask(id: Int): Flow<Task?>

    suspend fun markTaskCompleted(id: Int)

    suspend fun unmarkTaskCompleted(id: Int)

    suspend fun deleteTask(id: Int)
}

data class TaskModel(
    val title: String,
    val description: String,
    val category: Category,
    val priority: Priority,
    val dueDate: Instant?,
)