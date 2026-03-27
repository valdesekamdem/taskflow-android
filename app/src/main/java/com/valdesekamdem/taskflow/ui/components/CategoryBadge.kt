package com.valdesekamdem.taskflow.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark

@Composable
fun CategoryBadge(
    text: String,
) {
    Badge(
        text = text,
        textColor = MaterialTheme.colorScheme.onSurface,
        background = MaterialTheme.colorScheme.surfaceContainer,
    )
}

@PreviewLightDark
@Composable
fun CategoryBadgePreview() {
    CategoryBadge(
        text = "Work",
    )
}