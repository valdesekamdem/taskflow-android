package com.valdesekamdem.taskflow.feature.task

import com.valdesekamdem.taskflow.core.presentation.UiFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class TaskUiModule {

    @Binds
    @IntoSet
    abstract fun bindTaskUiFactory(taskUiFactory: TaskUiFactory): UiFactory
}
