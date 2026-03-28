package com.valdesekamdem.taskflow.feature.task.data.fakes

import app.cash.turbine.Turbine
import com.valdesekamdem.taskflow.feature.task.data.api.TaskModel
import com.valdesekamdem.taskflow.feature.task.data.api.TaskRepository

class FakeTaskRepository : TaskRepository {

    val addTaskCalls = Turbine<TaskModel>()
    override suspend fun addTask(taskModel: TaskModel) {
        addTaskCalls.add(taskModel)
    }
}