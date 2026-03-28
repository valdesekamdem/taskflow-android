package com.valdesekamdem.taskflow.feature.task.screens

import com.valdesekamdem.taskflow.core.navigation.api.Screen
import kotlinx.serialization.Serializable

@Serializable
data class EditTaskScreen(val id: String?) : Screen
