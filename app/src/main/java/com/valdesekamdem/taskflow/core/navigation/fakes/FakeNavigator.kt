package com.valdesekamdem.taskflow.core.navigation.fakes

import app.cash.turbine.Turbine
import com.valdesekamdem.taskflow.core.navigation.api.Navigator
import com.valdesekamdem.taskflow.core.navigation.api.Screen

class FakeNavigator : Navigator {
    val screens = Turbine<Screen>()

    override fun goTo(screen: Screen) {
        screens.add(screen)
    }

    override fun getBack() {
        TODO("Not yet implemented")
    }
}