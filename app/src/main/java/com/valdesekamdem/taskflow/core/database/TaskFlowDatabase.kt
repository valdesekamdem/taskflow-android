package com.valdesekamdem.taskflow.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.valdesekamdem.taskflow.core.database.dao.TaskDao
import com.valdesekamdem.taskflow.core.database.model.TaskEntity
import com.valdesekamdem.taskflow.core.database.util.InstantConverter

@Database(
    entities = [
        TaskEntity::class,
    ],
    version = 1,
)
@TypeConverters(
    InstantConverter::class,
)
internal abstract class TaskFlowDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}