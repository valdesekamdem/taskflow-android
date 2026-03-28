package com.valdesekamdem.taskflow.feature.task.data.api

interface TaskRepository {
    suspend fun addTask(taskModel: TaskModel)
}

data class TaskModel(
    val title: String,
    val description: String,
)