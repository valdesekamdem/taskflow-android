package com.valdesekamdem.taskflow.core.navigation.real

import com.valdesekamdem.taskflow.core.navigation.api.Navigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {

    @Binds
    abstract fun provideNavigator(realNavigator: RealNavigator): Navigator

    @Binds
    abstract fun provideNavigationEventSource(realNavigator: RealNavigator): NavigationEventSource
}