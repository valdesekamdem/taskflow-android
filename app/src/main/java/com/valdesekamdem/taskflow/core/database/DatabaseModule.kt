package com.valdesekamdem.taskflow.core.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun providesTaskFlowDatabase(
        @ApplicationContext context: Context,
    ): TaskFlowDatabase = Room.databaseBuilder(
        context,
        TaskFlowDatabase::class.java,
        "taskflow-database",
    ).build()
}
