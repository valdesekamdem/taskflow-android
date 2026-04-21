package com.valdesekamdem.taskflow.feature.settings.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.valdesekamdem.taskflow.R
import com.valdesekamdem.taskflow.feature.settings.viewmodel.SettingsUiEvent
import com.valdesekamdem.taskflow.feature.settings.viewmodel.SettingsUiState
import com.valdesekamdem.taskflow.ui.components.topappbar.LevelOneTopAppBar
import com.valdesekamdem.taskflow.ui.theme.TaskflowTheme

@Composable
fun Settings(
    uiState: SettingsUiState,
    onUiEvent: (SettingsUiEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            LevelOneTopAppBar(title = stringResource(R.string.settings_title))
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(stringResource(R.string.settings_coming_soon))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsPreview() {
    TaskflowTheme {
        Settings(uiState = SettingsUiState, onUiEvent = {})
    }
}
