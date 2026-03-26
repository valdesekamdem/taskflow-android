package com.valdesekamdem.taskflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.valdesekamdem.taskflow.core.navigation.api.Navigator
import com.valdesekamdem.taskflow.core.navigation.real.NavigationEvent
import com.valdesekamdem.taskflow.core.navigation.real.NavigationEventSource
import com.valdesekamdem.taskflow.core.presentation.UiFactory
import com.valdesekamdem.taskflow.feature.home.screens.HomeScreen
import com.valdesekamdem.taskflow.ui.theme.TaskflowTheme
import javax.inject.Inject
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var navigationEventSource: NavigationEventSource

    @Inject
    lateinit var uiFactories: Set<@JvmSuppressWildcards UiFactory>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val backStack = rememberNavBackStack(HomeScreen)
            BindNavigator(navigationEventSource = navigationEventSource, backStack = backStack)

            TaskflowTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Main(
                        backStack = backStack,
                        navigator = navigator,
                        uiFactories = uiFactories,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}

@Composable
fun BindNavigator(
    navigationEventSource: NavigationEventSource,
    backStack: NavBackStack<NavKey>,
) {
    LaunchedEffect(navigationEventSource, backStack) {
        navigationEventSource.events.collect { navigationEvent ->
            when (navigationEvent) {
                NavigationEvent.Back -> backStack.removeLastOrNull()
                is NavigationEvent.NavigateTo -> backStack.add(navigationEvent.screen)
            }
        }
    }
}

@Composable
fun Main(
    backStack: NavBackStack<NavKey>,
    navigator: Navigator,
    uiFactories: Set<UiFactory>,
    modifier: Modifier = Modifier
) {
    NavDisplay(
        backStack = backStack,
        onBack = { navigator.getBack() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            uiFactories.forEach { factory -> factory.register(this) }
        },
        modifier = modifier,
    )
}
