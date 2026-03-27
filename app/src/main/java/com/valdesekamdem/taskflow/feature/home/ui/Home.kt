package com.valdesekamdem.taskflow.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.valdesekamdem.taskflow.feature.home.fixtures.HomeFixtures
import com.valdesekamdem.taskflow.feature.home.viewmodel.HomeUiEvent
import com.valdesekamdem.taskflow.feature.home.viewmodel.HomeUiState
import com.valdesekamdem.taskflow.ui.theme.Spacing
import com.valdesekamdem.taskflow.ui.theme.TaskflowTheme

@Composable
fun Home(
    uiState: HomeUiState,
    onUiEvent: (HomeUiEvent) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
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

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    TaskflowTheme {
        Home(uiState = HomeFixtures.homeUiState, onUiEvent = {})
    }
}
