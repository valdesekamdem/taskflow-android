package com.valdesekamdem.taskflow.feature.task.data.real

import com.valdesekamdem.taskflow.feature.task.data.api.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TaskDataModule {

    @Binds
    abstract fun provideTaskRepository(
        real: RealTaskRepository
    ): TaskRepository
}