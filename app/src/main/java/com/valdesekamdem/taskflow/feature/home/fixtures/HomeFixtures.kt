package com.valdesekamdem.taskflow.feature.home.fixtures

import com.valdesekamdem.taskflow.feature.home.viewmodel.HomeUiState
import com.valdesekamdem.taskflow.feature.home.viewmodel.TaskUiModel
import com.valdesekamdem.taskflow.ui.components.Priority

object HomeFixtures {
    val tasks = listOf(
        TaskUiModel(
            id = "task-1",
            title = "Finalize onboarding flow",
            description = "Tighten copy and handoff the last two onboarding screens.",
            priority = Priority.High,
            category = "Work",
            dueDateText = "Due Mar 27",
        ),
        TaskUiModel(
            id = "task-2",
            title = "Book dentist appointment",
            description = "Call the clinic and confirm the next available evening slot.",
            priority = Priority.Medium,
            category = "Personal",
            dueDateText = "Due Mar 29",
        ),
        TaskUiModel(
            id = "task-3",
            title = "Review sprint retro notes",
            description = "Pull action items into the next planning session.",
            priority = Priority.Low,
            category = "Planning",
            dueDateText = "Due Apr 1",
        ),
    )

    val homeUiState = HomeUiState(tasks = tasks)
}
