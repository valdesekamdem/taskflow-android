package com.valdesekamdem.taskflow.feature.task.data.api

import com.valdesekamdem.taskflow.core.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun addTask(taskModel: TaskModel)

    fun getTasks(): Flow<List<Task>>
}

data class TaskModel(
    val title: String,
    val description: String,
)