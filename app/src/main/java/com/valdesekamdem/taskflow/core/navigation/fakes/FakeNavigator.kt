package com.valdesekamdem.taskflow.core.navigation.fakes

import app.cash.turbine.Turbine
import com.valdesekamdem.taskflow.core.navigation.api.Navigator
import com.valdesekamdem.taskflow.core.navigation.api.Screen
import com.valdesekamdem.taskflow.core.navigation.api.TabScreen

class FakeNavigator : Navigator {
    val screens = Turbine<Screen>()
    val tabScreens = Turbine<Screen>()

    override fun goTo(screen: Screen) {
        if (screen is TabScreen) tabScreens.add(screen) else screens.add(screen)
    }
}
