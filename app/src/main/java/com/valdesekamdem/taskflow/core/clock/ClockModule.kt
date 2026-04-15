package com.valdesekamdem.taskflow.core.clock

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.ZoneId
import javax.inject.Singleton
import kotlin.time.Clock

@Module
@InstallIn(SingletonComponent::class)
object ClockModule {

    @Singleton
    @Provides
    fun provideClock(): Clock = Clock.System

    @Provides
    fun provideZoneId(): ZoneId = ZoneId.systemDefault()
}
