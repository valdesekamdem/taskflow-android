package com.valdesekamdem.taskflow.feature.task.screens

import com.valdesekamdem.taskflow.core.navigation.api.Screen
import kotlinx.serialization.Serializable

@Serializable
class TaskDetailScreen(
    val id: String,
    val title: String,
) : Screen