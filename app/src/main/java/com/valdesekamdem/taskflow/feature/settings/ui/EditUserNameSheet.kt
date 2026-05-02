package com.valdesekamdem.taskflow.feature.settings.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.valdesekamdem.taskflow.R
import com.valdesekamdem.taskflow.feature.settings.viewmodel.SettingsUiState
import com.valdesekamdem.taskflow.feature.settings.viewmodel.SettingsUiState.UserNameSheetUiState
import com.valdesekamdem.taskflow.ui.components.TextField
import com.valdesekamdem.taskflow.ui.theme.Spacing
import com.valdesekamdem.taskflow.ui.theme.TaskflowTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EditUserNameSheet(
    state: UserNameSheetUiState,
    onDismiss: () -> Unit,
    onUserNameChanged: (String) -> Unit,
    onSaveUserNameClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(Spacing.medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.large),
        ) {
            Text(
                text = stringResource(R.string.settings_update_user_name_sheet_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )

            TextField(
                value = state.userName,
                onValueChange = onUserNameChanged,
                singleLine = true,
            )

            Button(
                onClick = onSaveUserNameClicked,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.save))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsUserNamePreview() {
    TaskflowTheme {
        Settings(
            uiState = SettingsUiState(
                userNameSheet = UserNameSheetUiState(
                    userName = "Valdese"
                )
            ),
            onUiEvent = {}
        )
    }
}
