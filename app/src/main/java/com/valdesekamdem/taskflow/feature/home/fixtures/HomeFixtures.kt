package com.valdesekamdem.taskflow.feature.home.fixtures

import com.valdesekamdem.taskflow.core.model.Priority
import com.valdesekamdem.taskflow.feature.home.viewmodel.HomeUiState
import com.valdesekamdem.taskflow.feature.home.viewmodel.TaskUiModel

object HomeFixtures {
    val tasks = listOf(
        TaskUiModel(
            id = 1,
            title = "Finalize onboarding flow",
            description = "Tighten copy and handoff the last two onboarding screens.",
            priority = Priority.High,
            category = "Work",
            dueDateText = "Yesterday",
            isTaskOverdue = true,
        ),
        TaskUiModel(
            id = 2,
            title = "Book dentist appointment",
            description = "Call the clinic and confirm the next available evening slot.",
            priority = Priority.Medium,
            category = "Personal",
            dueDateText = "Tomorrow",
            isTaskOverdue = false,
        ),
        TaskUiModel(
            id = 3,
            title = "Review sprint retro notes",
            description = "Pull action items into the next planning session.",
            priority = Priority.Low,
            category = "Planning",
            dueDateText = "In 4 days",
            isTaskOverdue = false,
        ),
    )

    val homeUiState = HomeUiState(todayDate = "March 28", tasks = tasks)
}
