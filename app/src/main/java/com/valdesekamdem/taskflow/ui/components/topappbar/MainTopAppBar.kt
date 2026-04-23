package com.valdesekamdem.taskflow.ui.components.topappbar

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.valdesekamdem.taskflow.R
import com.valdesekamdem.taskflow.ui.theme.TaskflowTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    surtitle: String? = null,
    title: String,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = {
            Column {
                surtitle?.run {
                    Text(
                        text = surtitle,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                Text(
                    text = title,
                    style = if (surtitle == null) {
                        MaterialTheme.typography.headlineMedium
                    } else {
                        MaterialTheme.typography.headlineSmall
                    },
                    fontWeight = FontWeight.Bold,
                )
            }
        },
        actions = actions,
    )
}

@Preview
@Composable
fun TopAppBarPreview() {
    TaskflowTheme {
        MainTopAppBar(
            title = "Settings",
            actions = {
                IconButton({}) {
                    Icon(painterResource(R.drawable.add), null)
                }
            }
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TopAppBarWithSurtitlePreview() {
    TaskflowTheme {
        MainTopAppBar(
            surtitle = "March 26",
            title = "Morning, Sam.",
        )
    }
}
