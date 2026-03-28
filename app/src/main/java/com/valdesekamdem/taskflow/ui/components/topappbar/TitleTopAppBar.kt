package com.valdesekamdem.taskflow.ui.components.topappbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.valdesekamdem.taskflow.R
import com.valdesekamdem.taskflow.ui.theme.TaskflowTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleTopAppBar(
    title: String,
    navigationType: NavigationType = NavigationType.BACK,
    onNavigationClicked: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
) {
    val iconRes = remember(navigationType) {
        when (navigationType) {
            NavigationType.BACK -> R.drawable.arrow_back_24
            NavigationType.CLOSE -> R.drawable.close_24
        }
    }

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigationClicked) {
                Icon(painter = painterResource(iconRes), null)
            }
        },
        actions = actions,
    )
}

enum class NavigationType {
    BACK, CLOSE
}

@Preview
@Composable
fun TitleTopAppBarPreview() {
    TaskflowTheme {
        TitleTopAppBar(
            title = "Task detail",
            onNavigationClicked = {},
            actions = {
                IconButton({}) {
                    Icon(painterResource(R.drawable.add), null)
                }
            }
        )
    }
}
