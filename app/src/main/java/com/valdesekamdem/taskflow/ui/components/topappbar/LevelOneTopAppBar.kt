package com.valdesekamdem.taskflow.ui.components.topappbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valdesekamdem.taskflow.R
import com.valdesekamdem.taskflow.ui.theme.Purple40
import com.valdesekamdem.taskflow.ui.theme.TaskflowTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelOneTopAppBar(
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
                        style = MaterialTheme.typography.titleSmall,
                        color = LocalTextStyle.current.color.copy(.6f)
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
        LevelOneTopAppBar(
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
@Composable
fun TopAppBarWithSurtitlePreview() {
    TaskflowTheme {
        LevelOneTopAppBar(
            surtitle = "March 26",
            title = "Home",
            actions = {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Purple40)
                )
            }
        )
    }
}
