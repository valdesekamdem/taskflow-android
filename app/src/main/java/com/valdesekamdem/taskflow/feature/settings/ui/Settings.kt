package com.valdesekamdem.taskflow.feature.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valdesekamdem.taskflow.R
import com.valdesekamdem.taskflow.feature.settings.viewmodel.SettingsUiEvent
import com.valdesekamdem.taskflow.feature.settings.viewmodel.SettingsUiEvent.EditUserNameClicked
import com.valdesekamdem.taskflow.feature.settings.viewmodel.SettingsUiEvent.SaveUserNameClicked
import com.valdesekamdem.taskflow.feature.settings.viewmodel.SettingsUiEvent.UserNameChanged
import com.valdesekamdem.taskflow.feature.settings.viewmodel.SettingsUiState
import com.valdesekamdem.taskflow.ui.components.Cell
import com.valdesekamdem.taskflow.ui.components.topappbar.MainTopAppBar
import com.valdesekamdem.taskflow.ui.theme.Rounded
import com.valdesekamdem.taskflow.ui.theme.TaskflowTheme

@Composable
fun Settings(
    uiState: SettingsUiState,
    onUiEvent: (SettingsUiEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            MainTopAppBar(title = stringResource(R.string.settings_title))
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPadding),
        ) {
            Cell(
                icon = {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(Rounded.medium))
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = (uiState.monogram ?: '?').toString(),
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                text = uiState.username ?: stringResource(R.string.settings_no_username),
                subtext = stringResource(R.string.settings_username_subtext),
                trailingContent = {
                    TextButton(onClick = { onUiEvent(EditUserNameClicked) }) {
                        Text(text = stringResource(R.string.edit))
                    }
                }
            )
        }

        uiState.userNameSheet?.let { state ->
            EditUserNameSheet(
                state = state,
                onDismiss = { onUiEvent(SettingsUiEvent.UserNameSheetDismissed) },
                onUserNameChanged = { onUiEvent(UserNameChanged(it)) },
                onSaveUserNameClicked = { onUiEvent(SaveUserNameClicked) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsPreview() {
    TaskflowTheme {
        Settings(uiState = SettingsUiState(), onUiEvent = {})
    }
}
