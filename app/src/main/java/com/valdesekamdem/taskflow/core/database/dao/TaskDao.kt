package com.valdesekamdem.taskflow.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import com.valdesekamdem.taskflow.core.database.model.TaskEntity

@Dao
interface TaskDao {
    @Insert
    fun insertAll(vararg tasks: TaskEntity)
}