package com.valdesekamdem.taskflow.feature.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valdesekamdem.taskflow.R
import com.valdesekamdem.taskflow.feature.home.fixtures.HomeFixtures
import com.valdesekamdem.taskflow.feature.home.viewmodel.HomeUiEvent
import com.valdesekamdem.taskflow.feature.home.viewmodel.HomeUiEvent.NewTaskClicked
import com.valdesekamdem.taskflow.feature.home.viewmodel.HomeUiState
import com.valdesekamdem.taskflow.ui.components.topappbar.LevelOneTopAppBar
import com.valdesekamdem.taskflow.ui.theme.Spacing
import com.valdesekamdem.taskflow.ui.theme.TaskflowTheme

@Composable
fun Home(
    uiState: HomeUiState,
    onUiEvent: (HomeUiEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            LevelOneTopAppBar(
                surtitle = uiState.todayDate,
                title = stringResource(R.string.home_title),
                actions = {
                    // TODO(valdese): Replace this with avatar component
                    Box(
                        modifier = Modifier
                            .padding(end = Spacing.small)
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("VK")
                    }
                }
            )
        },
        floatingActionButton = { NewTaskFloatingAction { onUiEvent(NewTaskClicked) } }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(Spacing.medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.medium),
        ) {
            items(
                items = uiState.tasks,
                key = { task -> task.id },
            ) { task ->
                TaskCard(
                    task = task,
                    onClick = { onUiEvent(HomeUiEvent.TaskClicked(task)) }
                )
            }
        }
    }
}

@Composable
private fun NewTaskFloatingAction(
    onClick: () -> Unit,
) {
    FloatingActionButton(
        onClick = onClick,
    ) {
        Icon(
            painter = painterResource(R.drawable.add),
            contentDescription = stringResource(R.string.add_new_task_button_description)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    TaskflowTheme {
        Home(uiState = HomeFixtures.homeUiState, onUiEvent = {})
    }
}
