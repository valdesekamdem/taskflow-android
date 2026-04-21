package com.valdesekamdem.taskflow.feature.main

import com.valdesekamdem.taskflow.core.presentation.UiFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class MainUiModule {

    @Binds
    @IntoSet
    abstract fun bindMainUiFactory(mainUiFactory: MainUiFactory): UiFactory
}
