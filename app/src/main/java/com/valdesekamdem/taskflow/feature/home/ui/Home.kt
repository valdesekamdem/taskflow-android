package com.valdesekamdem.taskflow.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.valdesekamdem.taskflow.R
import com.valdesekamdem.taskflow.feature.home.fixtures.HomeFixtures
import com.valdesekamdem.taskflow.feature.home.viewmodel.HomeUiEvent
import com.valdesekamdem.taskflow.feature.home.viewmodel.HomeUiEvent.NewTaskClicked
import com.valdesekamdem.taskflow.feature.home.viewmodel.HomeUiState
import com.valdesekamdem.taskflow.ui.theme.Spacing
import com.valdesekamdem.taskflow.ui.theme.TaskflowTheme

@Composable
fun Home(
    uiState: HomeUiState,
    onUiEvent: (HomeUiEvent) -> Unit,
) {
    Scaffold(
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
