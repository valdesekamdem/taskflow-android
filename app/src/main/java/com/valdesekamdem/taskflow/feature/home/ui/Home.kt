package com.valdesekamdem.taskflow.feature.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valdesekamdem.taskflow.R
import com.valdesekamdem.taskflow.feature.home.fixtures.HomeFixtures
import com.valdesekamdem.taskflow.feature.home.viewmodel.HomeUiEvent
import com.valdesekamdem.taskflow.feature.home.viewmodel.HomeUiEvent.NewTaskClicked
import com.valdesekamdem.taskflow.feature.home.viewmodel.HomeUiEvent.TaskCheckboxToggled
import com.valdesekamdem.taskflow.feature.home.viewmodel.HomeUiState
import com.valdesekamdem.taskflow.ui.components.topappbar.MainTopAppBar
import com.valdesekamdem.taskflow.ui.theme.Spacing
import com.valdesekamdem.taskflow.ui.theme.TaskflowTheme

@Composable
fun Home(
    uiState: HomeUiState,
    onUiEvent: (HomeUiEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            MainTopAppBar(
                surtitle = uiState.todayDate,
                title = uiState.title,
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
        ) {
            overdueTasksSection(uiState, onUiEvent)

            item {
                Box(modifier = Modifier.size(Spacing.medium))
            }

            todayTasksSection(uiState, onUiEvent)
        }
    }
}

private fun LazyListScope.overdueTasksSection(
    uiState: HomeUiState,
    onUiEvent: (HomeUiEvent) -> Unit
) {
    val visibleTasks = with(uiState) {
        if (isOverdueTasksExpanded) overdueTasks else overdueTasks.take(maxVisibleTasks)
    }
    val hiddenTasksSize = uiState.overdueTasks.size - uiState.maxVisibleTasks

    item {
        SectionHeader(
            title = stringResource(R.string.home_overdue_title),
            count = uiState.overdueTasks.size,
            modifier = Modifier.padding(
                horizontal = Spacing.medium,
                vertical = Spacing.small,
            ),
        )
    }

    itemsIndexed(
        items = visibleTasks,
        key = { _, task -> task.id },
    ) { index, task ->
        if (index > 0) {
            Divider()
        }

        TaskCard(
            task = task,
            onClick = { onUiEvent(HomeUiEvent.TaskClicked(task)) },
            onCheckboxToggled = { onUiEvent(TaskCheckboxToggled(task)) },
            modifier = Modifier.animateItem(),
        )
    }

    if (uiState.overdueTasks.size > uiState.maxVisibleTasks) {
        item {
            SectionCaption(
                isExpanded = uiState.isOverdueTasksExpanded,
                maxVisibleTasks = uiState.maxVisibleTasks,
                hiddenOverdueTasksSize = hiddenTasksSize
            ) {
                onUiEvent(HomeUiEvent.OverdueSectionCaptionClicked)
            }
        }
    }
}

private fun LazyListScope.todayTasksSection(
    uiState: HomeUiState,
    onUiEvent: (HomeUiEvent) -> Unit
) {
    val visibleTasks = with(uiState) {
        if (isTodayTasksExpanded) todayTasks else todayTasks.take(maxVisibleTasks)
    }
    val hiddenTasksSize = uiState.todayTasks.size - uiState.maxVisibleTasks

    item {
        SectionHeader(
            title = stringResource(R.string.home_today_title),
            count = uiState.todayTasks.size,
            modifier = Modifier.padding(
                horizontal = Spacing.medium,
                vertical = Spacing.small,
            ),
        )
    }

    itemsIndexed(
        items = visibleTasks,
        key = { _, task -> task.id },
    ) { index, task ->
        if (index > 0) {
            Divider()
        }

        TaskCard(
            task = task,
            onClick = { onUiEvent(HomeUiEvent.TaskClicked(task)) },
            onCheckboxToggled = { onUiEvent(TaskCheckboxToggled(task)) },
            modifier = Modifier.animateItem(),
        )
    }

    if (uiState.todayTasks.size > uiState.maxVisibleTasks) {
        item {
            SectionCaption(
                isExpanded = uiState.isTodayTasksExpanded,
                maxVisibleTasks = uiState.maxVisibleTasks,
                hiddenOverdueTasksSize = hiddenTasksSize
            ) {
                onUiEvent(HomeUiEvent.TodaySectionCaptionClicked)
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    count: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Spacing.small),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Text(
            text = "$count",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun LazyItemScope.Divider() {
    HorizontalDivider(
        modifier = Modifier
            .padding(horizontal = Spacing.medium)
            .animateItem(),
        color = DividerDefaults.color.copy(0.6f),
    )
}

@Composable
private fun LazyItemScope.SectionCaption(
    isExpanded: Boolean,
    maxVisibleTasks: Int,
    hiddenOverdueTasksSize: Int,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .animateItem(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier
                .clickable { onClick() }
                .padding(horizontal = Spacing.medium, vertical = Spacing.small),
            text = if (isExpanded) {
                stringResource(R.string.home_show_only_max, maxVisibleTasks)
            } else {
                stringResource(R.string.home_show_more, hiddenOverdueTasksSize)
            },
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
        )
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
