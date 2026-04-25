package com.valdesekamdem.taskflow.feature.task.data.fakes

import app.cash.turbine.Turbine
import com.valdesekamdem.taskflow.core.model.Task
import com.valdesekamdem.taskflow.feature.task.data.api.TaskModel
import com.valdesekamdem.taskflow.feature.task.data.api.TaskRepository
import com.valdesekamdem.taskflow.feature.task.data.api.filter.TaskFilter
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class FakeTaskRepository : TaskRepository {

    val addTaskCalls = Turbine<TaskModel>()
    override suspend fun addTask(taskModel: TaskModel) {
        addTaskCalls.add(taskModel)
    }

    val updateTaskCalls = Turbine<Pair<Int, TaskModel>>()
    override suspend fun updateTask(id: Int, taskModel: TaskModel) {
        updateTaskCalls.add(id to taskModel)
    }

    val tasks = Channel<List<Task>>()
    override fun getTasks(): Flow<List<Task>> {
        return tasks.receiveAsFlow()
    }

    val getTasksFilterCalls = Channel<TaskFilter>(Channel.UNLIMITED)
    val filteredTasks = Channel<List<Task>>(Channel.UNLIMITED)
    override fun getTasks(filter: TaskFilter): Flow<List<Task>> {
        getTasksFilterCalls.trySend(filter)
        return filteredTasks.receiveAsFlow()
    }

    val getTaskCalls = Channel<Int>(Channel.UNLIMITED)
    val task = Channel<Task?>(Channel.UNLIMITED)
    override fun getTask(id: Int): Flow<Task?> {
        getTaskCalls.trySend(id)
        return task.receiveAsFlow()
    }

    val markTaskCompletedCalls = Turbine<Int>()
    override suspend fun markTaskCompleted(id: Int) {
        markTaskCompletedCalls.add(id)
    }

    val unmarkTaskCompletedCalls = Turbine<Int>()
    override suspend fun unmarkTaskCompleted(id: Int) {
        unmarkTaskCompletedCalls.add(id)
    }

    val deleteTaskCalls = Turbine<Int>()
    override suspend fun deleteTask(id: Int) {
        deleteTaskCalls.add(id)
    }
}