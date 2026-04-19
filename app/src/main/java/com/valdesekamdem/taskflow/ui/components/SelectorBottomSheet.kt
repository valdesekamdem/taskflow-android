package com.valdesekamdem.taskflow.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.valdesekamdem.taskflow.R
import com.valdesekamdem.taskflow.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SelectorBottomSheet(
    title: String,
    items: List<T>,
    selectedItem: T,
    itemContent: @Composable (T) -> Unit,
    onItemSelected: (T) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Spacing.medium)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = Spacing.medium),
            )

            Spacer(Modifier.size(Spacing.medium))

            items.forEach { item ->
                val isSelected = item == selectedItem

                Row(
                    modifier = Modifier
                        .clickable {
                            onItemSelected(item)
                            onDismiss()
                        }
                        .fillMaxWidth()
                        .padding(Spacing.medium),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        itemContent(item)
                    }

                    if (isSelected) {
                        Icon(
                            painter = painterResource(R.drawable.check_small_24),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.padding(start = Spacing.medium),
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectorBottomSheetPreview() {
    val items = listOf("Low Priority", "Medium Priority", "High Priority")
    Surface {
        SelectorBottomSheet(
            title = "Choose category",
            items = items,
            selectedItem = items[1],
            itemContent = { Text(it) },
            onItemSelected = {},
            onDismiss = {}
        )
    }
}
