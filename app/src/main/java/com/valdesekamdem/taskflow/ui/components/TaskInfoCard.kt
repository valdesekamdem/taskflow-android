package com.valdesekamdem.taskflow.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.valdesekamdem.taskflow.ui.theme.Rounded
import com.valdesekamdem.taskflow.ui.theme.Spacing

@Composable
fun TaskInfoCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    isError: Boolean = false,
) {
    val containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    val contentColor = MaterialTheme.colorScheme.onSurface
    val errorColor = MaterialTheme.colorScheme.error

    Card(
        modifier = modifier,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(Rounded.medium),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.medium)
        ) {
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = contentColor.copy(alpha = 0.6f),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(Spacing.xsmall))

            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = if (isError) errorColor else contentColor
            )

            if (subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isError) errorColor.copy(alpha = 0.8f) else contentColor.copy(alpha = 0.6f)
                )
            }
        }
    }
}
