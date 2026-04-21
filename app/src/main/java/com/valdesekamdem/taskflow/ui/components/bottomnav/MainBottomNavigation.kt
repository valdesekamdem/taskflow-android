package com.valdesekamdem.taskflow.ui.components.bottomnav

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import com.valdesekamdem.taskflow.R
import com.valdesekamdem.taskflow.core.navigation.api.TabScreen
import com.valdesekamdem.taskflow.feature.home.screens.HomeScreen
import com.valdesekamdem.taskflow.feature.settings.screens.SettingsScreen
import com.valdesekamdem.taskflow.feature.tasks.screens.TasksScreen
import com.valdesekamdem.taskflow.ui.theme.TaskflowTheme

@Composable
fun MainBottomNavigation(
    currentScreen: NavKey,
    onTabSelected: (TabScreen) -> Unit,
) {
    NavigationBar {
        NavItem(
            selected = currentScreen is HomeScreen,
            iconRes = R.drawable.home_24,
            labelRes = R.string.main_nav_home,
            onClick = { onTabSelected(HomeScreen) },
        )
        NavItem(
            selected = currentScreen is TasksScreen,
            iconRes = R.drawable.ballot_24,
            labelRes = R.string.main_nav_tasks,
            onClick = { onTabSelected(TasksScreen) },
        )
        NavItem(
            selected = currentScreen is SettingsScreen,
            iconRes = R.drawable.settings_24,
            labelRes = R.string.main_nav_settings,
            onClick = { onTabSelected(SettingsScreen) },
        )
    }
}

@Composable
private fun RowScope.NavItem(
    selected: Boolean,
    @DrawableRes iconRes: Int,
    @StringRes labelRes: Int,
    onClick: () -> Unit,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
            )
        },
        label = { Text(stringResource(labelRes)) },
    )
}

@Preview(showBackground = true)
@Composable
private fun MainBottomNavigationHomePreview() {
    TaskflowTheme {
        MainBottomNavigation(currentScreen = HomeScreen, onTabSelected = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun MainBottomNavigationTasksPreview() {
    TaskflowTheme {
        MainBottomNavigation(currentScreen = TasksScreen, onTabSelected = {})
    }
}
