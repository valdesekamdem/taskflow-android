package com.valdesekamdem.taskflow.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.valdesekamdem.taskflow.ui.theme.Spacing

@Composable
fun FormRow(
    label: String,
    modifier: Modifier = Modifier,
    required: Boolean = false,
    content: @Composable () -> Unit,
) {
    val annotatedLabel = buildAnnotatedString {
        append(label)
        if (required) {
            withStyle(SpanStyle(color = MaterialTheme.colorScheme.error)) {
                append(" *")
            }
        }
    }

    Column(modifier = modifier) {
        Text(
            text = annotatedLabel,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        )
        Spacer(modifier = Modifier.size(Spacing.xsmall))
        content()
    }
}