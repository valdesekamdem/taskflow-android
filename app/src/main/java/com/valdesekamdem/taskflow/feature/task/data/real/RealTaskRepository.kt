package com.valdesekamdem.taskflow.feature.task.data.real

import com.valdesekamdem.taskflow.core.database.dao.TaskDao
import com.valdesekamdem.taskflow.core.database.model.TaskEntity
import com.valdesekamdem.taskflow.core.database.model.toTask
import com.valdesekamdem.taskflow.core.model.Task
import com.valdesekamdem.taskflow.feature.task.data.api.TaskModel
import com.valdesekamdem.taskflow.feature.task.data.api.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Clock

@Singleton
class RealTaskRepository @Inject constructor(
    private val taskDao: TaskDao,
    private val clock: Clock,
) : TaskRepository {

    override suspend fun addTask(taskModel: TaskModel) {
        val taskEntity = TaskEntity(
            id = null,
            title = taskModel.title,
            description = taskModel.description,
            priority = taskModel.priority,
            category = taskModel.category,
            dueDate = taskModel.dueDate,
            reminder = null,
            completedAt = null,
            createdAt = clock.now(),
            updatedAt = null,
            notes = null,
        )

        taskDao.insertAll(taskEntity)
    }

    override fun getTasks(): Flow<List<Task>> {
        return taskDao.getAll().map { taskEntities ->
            taskEntities.map { it.toTask() }
        }
    }

    override suspend fun updateTask(id: Int, taskModel: TaskModel) {
        taskDao.updateTask(
            id = id,
            title = taskModel.title,
            description = taskModel.description,
            priority = taskModel.priority,
            category = taskModel.category,
            dueDate = taskModel.dueDate,
            updatedAt = clock.now(),
        )
    }

    override fun getTask(id: Int): Flow<Task?> {
        return taskDao.getTaskById(id).map { it?.toTask() }
    }

    override suspend fun markTaskCompleted(id: Int) = taskDao.markTaskCompleted(id, clock.now())

    override suspend fun unmarkTaskCompleted(id: Int) = taskDao.unmarkTaskCompleted(id)

    override suspend fun deleteTask(id: Int) = taskDao.deleteTask(id)
}
