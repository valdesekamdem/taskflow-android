package com.valdesekamdem.taskflow.core.database

import com.valdesekamdem.taskflow.core.database.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {
    @Provides
    fun providesTaskDao(
        database: TaskFlowDatabase,
    ): TaskDao = database.taskDao()
}
