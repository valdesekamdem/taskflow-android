package com.valdesekamdem.taskflow.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valdesekamdem.taskflow.R
import com.valdesekamdem.taskflow.ui.theme.Spacing
import com.valdesekamdem.taskflow.ui.theme.TaskflowTheme

@Composable
fun Cell(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    icon: (@Composable () -> Unit)? = null,
    subtext: String? = null,
    trailingContent: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.medium),
        horizontalArrangement = Arrangement.spacedBy(Spacing.medium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon?.invoke()

        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = textColor,
            )
            subtext?.let {
                Text(
                    text = subtext,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        trailingContent?.invoke()
    }
}

@Composable
fun CellLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
fun CellChevron() {
    Icon(
        painter = painterResource(R.drawable.chevron_right_24),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
fun CellLabelWithChevron(text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Spacing.xsmall),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CellLabel(text)
        CellChevron()
    }
}

@Composable
fun CellSwitch(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Preview(showBackground = true)
@Composable
fun CellPreview() {
    TaskflowTheme {
        Cell(
            icon = {
                Icon(painter = painterResource(R.drawable.folder_24), contentDescription = null)
            },
            text = "Theme",
            trailingContent = { CellLabel("System") },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CellTogglePreview() {
    TaskflowTheme {
        Cell(
            icon = {
                Icon(painter = painterResource(R.drawable.folder_24), contentDescription = null)
            },
            text = "Reminders",
            subtext = "Reminders",
            trailingContent = { CellSwitch(checked = true, onCheckedChange = {}) },
        )
    }
}
