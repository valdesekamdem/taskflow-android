package com.valdesekamdem.taskflow.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.valdesekamdem.taskflow.core.database.model.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert
    fun insertAll(vararg tasks: TaskEntity)

    @Query("SELECT * FROM tasks")
    fun getAll(): Flow<List<TaskEntity>>
}