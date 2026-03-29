package com.valdesekamdem.taskflow.feature.task.data.fakes

import app.cash.turbine.Turbine
import com.valdesekamdem.taskflow.core.model.Task
import com.valdesekamdem.taskflow.feature.task.data.api.TaskModel
import com.valdesekamdem.taskflow.feature.task.data.api.TaskRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class FakeTaskRepository : TaskRepository {

    val addTaskCalls = Turbine<TaskModel>()
    override suspend fun addTask(taskModel: TaskModel) {
        addTaskCalls.add(taskModel)
    }

    val tasks = Channel<List<Task>>()
    override fun getTasks(): Flow<List<Task>> {
        return tasks.receiveAsFlow()
    }
}