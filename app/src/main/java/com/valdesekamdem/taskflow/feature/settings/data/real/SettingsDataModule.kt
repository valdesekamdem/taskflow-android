package com.valdesekamdem.taskflow.feature.settings.data.real

import com.valdesekamdem.taskflow.feature.settings.data.api.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsDataModule {

    @Binds
    abstract fun bindSettingsRepository(
        realSettingsRepository: RealSettingsRepository
    ): SettingsRepository
}