package com.valdesekamdem.taskflow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.valdesekamdem.taskflow.ui.theme.Spacing
import com.valdesekamdem.taskflow.ui.theme.TaskflowTheme

@Composable
fun Badge(
    text: String,
    textColor: Color,
    background: Color,
    fontWeight: FontWeight? = MaterialTheme.typography.labelSmall.fontWeight
) {
    TaskflowTheme {
        Box(
            modifier = Modifier
                .background(color = background, shape = RoundedCornerShape(Spacing.xsmall))
                .padding(horizontal = Spacing.small)
        ) {
            Text(
                text = text,
                color = textColor,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = fontWeight,
                )
            )
        }
    }
}

@Preview
@Composable
fun BadgePreview() {
    Badge(
        text = "Work",
        textColor = MaterialTheme.colorScheme.onSurface,
        background = MaterialTheme.colorScheme.primaryContainer,
    )
}