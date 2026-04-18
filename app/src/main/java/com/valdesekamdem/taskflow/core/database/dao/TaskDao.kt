package com.valdesekamdem.taskflow.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.valdesekamdem.taskflow.core.database.model.TaskEntity
import com.valdesekamdem.taskflow.core.model.Category
import com.valdesekamdem.taskflow.core.model.Priority
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant

@Dao
interface TaskDao {
    @Insert
    fun insertAll(vararg tasks: TaskEntity)

    @Query("SELECT * FROM tasks")
    fun getAll(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTaskById(id: Int): Flow<TaskEntity?>

    @Query("UPDATE tasks SET title = :title, description = :description, priority = :priority, category = :category, due_date = :dueDate, updated_at = :updatedAt WHERE id = :id")
    suspend fun updateTask(
        id: Int,
        title: String,
        description: String,
        priority: Priority,
        category: Category,
        dueDate: Instant?,
        updatedAt: Instant
    )

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTask(id: Int)
}