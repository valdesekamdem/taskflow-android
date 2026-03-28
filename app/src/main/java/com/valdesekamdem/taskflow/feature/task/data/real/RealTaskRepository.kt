package com.valdesekamdem.taskflow.feature.task.data.real

import android.util.Log
import com.valdesekamdem.taskflow.feature.task.data.api.TaskModel
import com.valdesekamdem.taskflow.feature.task.data.api.TaskRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealTaskRepository @Inject constructor() : TaskRepository {

    override suspend fun addTask(taskModel: TaskModel) {
        Log.d(this.javaClass.simpleName, taskModel.toString())
        delay(200) // Temporary: Simulate saving operation
    }
}